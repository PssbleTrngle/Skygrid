package com.possibletriangle.skygrid.blocks;

import com.possibletriangle.skygrid.Skygrid;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStructureVoid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class BlockFrame extends BlockStructureVoid {

    public static final BlockFrame FRAME = new BlockFrame();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(FRAME);
    }

    public BlockFrame() {
        setRegistryName(Skygrid.MODID, "frame");
        setUnlocalizedName(Skygrid.MODID + ".frame");
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    }
}
