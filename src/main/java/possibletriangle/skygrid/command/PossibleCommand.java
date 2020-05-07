package possibletriangle.skygrid.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.floats.FloatComparator;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockStateArgument;
import net.minecraft.command.arguments.DimensionArgument;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.DistExecutor;
import possibletriangle.skygrid.Skygrid;
import possibletriangle.skygrid.data.loading.DimensionConfig;
import possibletriangle.skygrid.data.loading.DimensionLoader;
import possibletriangle.skygrid.provider.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PossibleCommand {

    public static LiteralArgumentBuilder<CommandSource> register() {
        return Commands.literal("blocks")
                .then(Commands.argument("config", DimensionConfigArgument.getConfig())
                        .executes(PossibleCommand::run)
                        .then(registerContains())
                        .then(registerExport())
                ).executes(PossibleCommand::run)
                .then(registerContains())
                .then(registerExport());
    }

    public static LiteralArgumentBuilder<CommandSource> registerContains() {
        return Commands.literal("contains")
                .then(Commands.argument("search", BlockStateArgument.blockState())
                        .executes(PossibleCommand::find));
    }

    public static LiteralArgumentBuilder<CommandSource> registerExport() {
        return Commands.literal("export")
                .then(Commands.argument("file", StringArgumentType.string())
                        .executes(ctx -> export(ctx, false))
                        .then(Commands.literal("json").executes(ctx -> export(ctx, true)))
                )
                .then(Commands.literal("json").executes(ctx -> export(ctx, true)))
                .executes(ctx -> export(ctx, false));
    }

    public static DimensionConfig optionalConfig(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        try {
            return DimensionConfigArgument.getConfigArgument(ctx, "config");
        } catch (IllegalArgumentException ex) {
            ResourceLocation name = ctx.getSource().getWorld().getDimension().getType().getRegistryName();
            return DimensionLoader.find(name).orElseThrow(() -> DimensionConfigArgument.NOT_FOUND.create(name));
        }
    }

    private static Stream<Pair<Float, Block>> getBlocks(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        DimensionConfig config = optionalConfig(ctx);
        return config.getPossibleBlocks();
    }

    public static File getExportDir() {
        return new File(DistExecutor.runForDist(
                () -> () -> Minecraft.getInstance().gameDir,
                () -> () -> new File(".")
        ).getPath() + "/exports");
    }

    private static Stream<JsonObject> parseProvider(BlockProvider provider, float weight) {
        JsonObject object = new JsonObject();

        object.addProperty("weight", weight);
        if(provider instanceof INamed) ((INamed) provider).getName().filter(s -> s.length() > 0).ifPresent(name -> object.addProperty("name", name));
        if(provider instanceof CollectionProvider) {
            JsonArray children = new JsonArray();
            ((CollectionProvider) provider).getAllProviders()
                    .map(p -> parseProvider(p.getSecond(), p.getFirst()))
                    .flatMap(Function.identity())
                    .forEach(children::add);

            object.add("children", children);
        } else if(provider instanceof SingleBlock) {
            ResourceLocation name = ((SingleBlock) provider).getBlock().getRegistryName();
            assert name != null;
            object.addProperty("id", name.toString());
        }

        return Stream.of(
                Stream.of(object),
                provider.getOffsets().map(o -> parseProvider(o.provider, o.probability * weight))
                    .flatMap(Function.identity())
        ).flatMap(Function.identity());
    }

    private static void exportJSON(File file, DimensionConfig config) throws IOException {

        JsonArray array = new JsonArray();
        config.providers.stream()
                .map(p -> parseProvider(p.getSecond(), p.getFirst()))
                .flatMap(Function.identity())
                .forEach(array::add);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Files.write(file.toPath(), Collections.singleton(gson.toJson(array)));

    }

    private static void exportCSV(File file, DimensionConfig config) throws IOException {

        List<Map.Entry<Block,Float>> blocks = config.getUniqueBlocks().collect(Collectors.toList());

        List<String> lines = blocks.stream()
                .map(p -> Stream.of(p.getKey().getRegistryName(), p.getValue(), p.getKey().getDefaultState().getMaterial().getColor().colorValue)
                        .map(Objects::toString)
                        .reduce((a, b) -> a + "," + b)
                ).map(Optional::get)
                .collect(Collectors.toList());

        lines.add(0, "Block,Weight,Color");

        Files.write(file.toPath(), lines);
    }

    private static int export(CommandContext<CommandSource> ctx, boolean json) throws CommandSyntaxException {

        DimensionConfig config = optionalConfig(ctx);

        try {
            File dir = getExportDir();

            String name;
            try {
                name = StringArgumentType.getString(ctx, "file");
            } catch (IllegalArgumentException ignored) {
                name = Skygrid.MODID;
            }

            String suffix = json ? ".json" : ".csv";

            File file = new File( dir.getPath() + "/" + name + suffix);
            file.getParentFile().mkdirs();
            if (!file.exists()) file.createNewFile();

            if(json) exportJSON(file, config);
            else exportCSV(file, config);

            ClickEvent open = new ClickEvent(ClickEvent.Action.OPEN_FILE, file.getAbsolutePath());
            HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("command.skygrid.blocks.exports.open"));
            ctx.getSource().sendFeedback(new TranslationTextComponent("command.skygrid.blocks.exports.success")
                    .setStyle(new Style().setClickEvent(open).setHoverEvent(hover)), false);

        } catch (IOException ex) {
            ex.printStackTrace();
            throw new CommandException(new TranslationTextComponent("command.skygrid.blocks.exports.error"));
        }

        return 1;
    }

    private static int find(CommandContext<CommandSource> ctx) throws CommandSyntaxException {

        Block search = BlockStateArgument.getBlockState(ctx, "search").getState().getBlock();
        float weight = getBlocks(ctx).filter(b -> search == b.getSecond())
                .reduce(0F, (a, b) -> a + b.getFirst(), (a, b) -> b);

        ctx.getSource().sendFeedback(new TranslationTextComponent("command.skygrid.blocks.contains." + (weight > 0), weight), true);

        return weight > 0 ? 1 : 0;
    }

    private static int run(CommandContext<CommandSource> ctx) throws CommandSyntaxException {

        List<Pair<Float, Block>> blocks = getBlocks(ctx).collect(Collectors.toList());
        ctx.getSource().sendFeedback(new TranslationTextComponent("command.skygrid.blocks.count", blocks.size()), true);
        return blocks.size();
    }

}
