package possibletriangle.skygrid.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.*;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.IClearable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistryEntry;
import possibletriangle.skygrid.data.loading.DimensionConfig;
import possibletriangle.skygrid.world.SkygridChunkGenerator;

import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class GenerateCommand {

    public static LiteralArgumentBuilder<CommandSource> register() {
        return Commands.literal("generate")
                .then(registerRun(Commands.argument("from", BlockPosArgument.blockPos()))
                        .then(registerRun(Commands.argument("to", BlockPosArgument.blockPos()))));
    }

    public static <T extends ArgumentBuilder<CommandSource, T>> ArgumentBuilder<CommandSource, T> registerRun(ArgumentBuilder<CommandSource, T> builder) {
        return builder.executes(GenerateCommand::run)
                .then(Commands.argument("config", DimensionConfigArgument.getConfig())
                        .executes(GenerateCommand::run));
    }

    private static final List<ResourceLocation> spawns = Stream.of(EntityType.ZOMBIE)
            .map(ForgeRegistryEntry::getRegistryName)
            .collect(Collectors.toList());

    public static BiConsumer<BlockPos, BlockState> getGenerator(World world, BlockPos anchor, DimensionConfig config, List<BlockPos> updated) {
        Random random = new Random();
        BlockState f = Blocks.TRIPWIRE.getDefaultState();

        return SkygridChunkGenerator.getGenerator(
                (p, s) -> {
                    TileEntity tileentity = world.getTileEntity(p);
                    IClearable.clearObj(tileentity);

                    if (s.getBlock() instanceof FallingBlock) {
                        if (world.setBlockState(p.down(), f, 2)) updated.add(p.down().toImmutable());
                    }

                    if (world.setBlockState(p, s, 2)) updated.add(p.toImmutable());
                },
                (p, t) -> {
                    TileEntity te = world.getTileEntity(p);
                    if (te != null) {
                        t.setPos(p);
                        te.read(t.write(new CompoundNBT()));
                    }
                },
                () -> config.randomLoot(random),
                () -> spawns.get(random.nextInt(spawns.size())),
                world, random, anchor
        );
    }

    private static int run(CommandContext<CommandSource> ctx) throws CommandSyntaxException {

        DimensionConfig config = PossibleCommand.optionalConfig(ctx);

        BlockPos from = BlockPosArgument.getLoadedBlockPos(ctx, "from");
        BlockPos to = from;
        try {
            to = BlockPosArgument.getLoadedBlockPos(ctx, "to");
        } catch (IllegalArgumentException ignored) {
        }

        ServerWorld world = ctx.getSource().getWorld();

        Random random = new Random();

        Predicate<BlockPos> generateHere = SkygridChunkGenerator.generateHere(new ChunkPos(0, 0), DimensionConfig.DEFAULT_DISTANCE_POS, DimensionConfig.DEFAULT_CLUSTER_POS);

        int fx = Math.min(from.getX(), to.getX()) - 1;
        int fy = Math.min(from.getY(), to.getY()) - 1;
        int fz = Math.min(from.getZ(), to.getZ()) - 1;
        int tx = Math.max(from.getX(), to.getX()) + 1;
        int ty = Math.max(from.getY(), to.getY()) + 2;
        int tz = Math.max(from.getZ(), to.getZ()) + 1;

        List<BlockPos> updated = Lists.newArrayList();

        StreamSupport.stream(BlockPos.getAllInBoxMutable(fx, fy, fz, tx, ty, tz).spliterator(), false)
                .filter(generateHere.negate())
                .forEach(pos -> config.getFill().generate(getGenerator(world, pos, config, updated), random));

        long generated = StreamSupport.stream(BlockPos.getAllInBoxMutable(from, to).spliterator(), false)
                .filter(generateHere)
                .peek(pos -> config.randomProvider(random).generate(getGenerator(world, pos, config, updated), random))
                .count();

        updated.stream()
                .distinct()
                .forEach(p -> {
                    Block block = world.getBlockState(p).getBlock();
                    world.notifyNeighbors(p, block);
                });

        return (int) generated;

    }

}
