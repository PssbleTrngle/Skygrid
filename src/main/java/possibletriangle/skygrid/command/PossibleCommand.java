package possibletriangle.skygrid.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockStateArgument;
import net.minecraft.command.arguments.DimensionArgument;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.dimension.DimensionType;
import possibletriangle.skygrid.data.loading.DimensionConfig;
import possibletriangle.skygrid.data.loading.DimensionLoader;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PossibleCommand {

    public static LiteralArgumentBuilder<CommandSource> register() {
        return Commands.literal("blocks")
                .then(Commands.argument("dimension", DimensionArgument.getDimension())
                        .executes(PossibleCommand::run)
                        .then(registerContains())
                ).executes(PossibleCommand::run)
                .then(registerContains());
    }

    public static LiteralArgumentBuilder<CommandSource> registerContains() {
        return Commands.literal("contains")
                .then(Commands.argument("search", BlockStateArgument.blockState())
                .executes(PossibleCommand::find));
    }

    public static DimensionType optionalDimension(CommandContext<CommandSource> ctx) {
        try {
            return DimensionArgument.getDimensionArgument(ctx, "dimension");
        } catch (IllegalArgumentException ex) {
            return ctx.getSource().getWorld().getDimension().getType();
        }
    }
    private static Stream<Block> getBlocks(CommandContext<CommandSource> ctx) {

        DimensionType dimension = optionalDimension(ctx);
        DimensionConfig config = DimensionLoader.findConfig(dimension.getRegistryName());

        return config.getPossibleBlocks();

    }

    private static int find(CommandContext<CommandSource> ctx) {

        Block search = BlockStateArgument.getBlockState(ctx, "search").getState().getBlock();
        int matches = (int) getBlocks(ctx).filter(b -> search == b).count();

        ctx.getSource().sendFeedback(new TranslationTextComponent("command.skygrid.blocks.contains." + (matches > 0)), true);

        return matches;
    }

    private static int run(CommandContext<CommandSource> ctx) {

        List<Block> blocks = getBlocks(ctx).collect(Collectors.toList());

        ctx.getSource().sendFeedback(new TranslationTextComponent("command.skygrid.blocks.count", blocks.size()), true);

        return blocks.size();
    }

}
