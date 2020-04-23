package possibletriangle.skygrid.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.command.arguments.DimensionArgument;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import possibletriangle.skygrid.data.loading.DimensionConfig;
import possibletriangle.skygrid.data.loading.DimensionLoader;
import possibletriangle.skygrid.generator.SkygridChunkGenerator;

import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

public class GenerateCommand {

    public static LiteralArgumentBuilder<CommandSource> register() {
        return Commands.literal("generate")
                .then(registerRun(Commands.argument("from", BlockPosArgument.blockPos()))
                .then(registerRun(Commands.argument("to", BlockPosArgument.blockPos()))));
    }

    public static <T extends ArgumentBuilder<CommandSource, T>> ArgumentBuilder<CommandSource,T> registerRun(ArgumentBuilder<CommandSource, T> builder) {
        return builder.executes(GenerateCommand::run)
                .then(Commands.argument("dimension", DimensionArgument.getDimension())
                        .executes(GenerateCommand::run));
    }

    public static BiConsumer<BlockPos, BlockState> getGenerator(World world, BlockPos anchor, DimensionConfig config) {
        Random random = new Random();
        BlockState f = Blocks.STONE_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.TOP);

        return SkygridChunkGenerator.getGenerator(
                (p, s) -> {
                    if(s.getBlock() instanceof FallingBlock) {
                        world.setBlockState(anchor.add(p).down(), f);
                    }
                    world.setBlockState(anchor.add(p), s);
                },
                (p, t) -> {
                    TileEntity te = world.getTileEntity(anchor.add(p));
                    if(te != null) {
                        t.setPos(anchor.add(p));
                        te.read(t.write(new CompoundNBT()));
                    }
                },
                () -> config.randomLoot(random),
                world
        );
    }

    private static int run(CommandContext<CommandSource> ctx) throws CommandSyntaxException {

        DimensionType dimension = PossibleCommand.optionalDimension(ctx);
        DimensionConfig config = DimensionLoader.findConfig(dimension.getRegistryName());

        BlockPos from = BlockPosArgument.getLoadedBlockPos(ctx, "from");
        BlockPos to = from;
        try {
            to = BlockPosArgument.getLoadedBlockPos(ctx, "to");
        } catch (IllegalArgumentException ignored) {}

        ServerWorld world = ctx.getSource().getWorld();

        Random random = new Random();

        Predicate<BlockPos> generateHere = SkygridChunkGenerator.generateHere(new ChunkPos(0,0), DimensionConfig.DEFAULT_DISTANCE_POS, DimensionConfig.DEFAULT_CLUSTER_POS);

        int fx = Math.min(from.getX(), to.getX()) - 1;
        int fy = Math.min(from.getY(), to.getY()) - 1;
        int fz = Math.min(from.getZ(), to.getZ()) - 1;
        int tx = Math.max(from.getX(), to.getX()) + 1;
        int ty = Math.max(from.getY(), to.getY()) + 2;
        int tz = Math.max(from.getZ(), to.getZ()) + 1;

        StreamSupport.stream(BlockPos.getAllInBoxMutable(fx, fy, fz, tx, ty, tz).spliterator(), false)
                .filter(generateHere.negate())
                .forEach(pos -> config.getFill().generate(getGenerator(world, pos, config), random));

        long generated = StreamSupport.stream(BlockPos.getAllInBoxMutable(from, to).spliterator(), false)
                .filter(generateHere)
                .peek(pos -> config.randomProvider(random).generate(getGenerator(world, pos, config), random))
                .count();

        return (int) generated;

    }

}
