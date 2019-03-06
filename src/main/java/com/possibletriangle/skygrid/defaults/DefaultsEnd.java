package com.possibletriangle.skygrid.defaults;

import com.google.common.base.Predicates;
import com.possibletriangle.skygrid.Skygrid;
import com.possibletriangle.skygrid.random.BlockInfo;
import com.possibletriangle.skygrid.random.RandomCollection;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.function.DoubleUnaryOperator;

@Mod.EventBusSubscriber
public class DefaultsEnd extends Defaults {

    public static final BlockInfo PORTAL = new BlockInfo()
            .add(Blocks.AIR)
            
            .addAt(new BlockPos(+1, 0, -1), Blocks.END_PORTAL_FRAME.getDefaultState())
            .addAt(new BlockPos(-1, 0, +1), Blocks.END_PORTAL_FRAME.getDefaultState())
            .addAt(new BlockPos(+1, 0, +1), Blocks.END_PORTAL_FRAME.getDefaultState())
            .addAt(new BlockPos(-1, 0, -1), Blocks.END_PORTAL_FRAME.getDefaultState())
            
            .addAt(new BlockPos(+0, 0, +1), Blocks.END_PORTAL_FRAME.getDefaultState())
            .addAt(new BlockPos(+0, 0, -1), Blocks.END_PORTAL_FRAME.getDefaultState())
            .addAt(new BlockPos(+1, 0, +0), Blocks.END_PORTAL_FRAME.getDefaultState())
            .addAt(new BlockPos(-1, 0, +0), Blocks.END_PORTAL_FRAME.getDefaultState());

    @Override
    public void registerBlocks(RandomCollection<BlockInfo> blocks, int floor) {

        RandomCollection<BlockInfo> purpur = new RandomCollection<BlockInfo>()
                .add(2, new BlockInfo().add(Blocks.PURPUR_BLOCK))
                .add(1, new BlockInfo().add(Blocks.PURPUR_PILLAR));

        RandomCollection<BlockInfo> duskbound = new RandomCollection<BlockInfo>()
                .add(1, new BlockInfo().add(new ResourceLocation("quark", "duskbound_block")))
                .add(0.5, new BlockInfo().add(new ResourceLocation("quark", "duskbound_lantern")));

        blocks.add(0.1, new BlockInfo().add(new ResourceLocation("thermalfoundation", "fluid_ender")).addAtAll(FRAME_BLOCK));
        blocks.add(0.1, new BlockInfo().add(Blocks.PURPLE_SHULKER_BOX));
        blocks.add(0.1, new BlockInfo().add(Blocks.ENDER_CHEST));
        blocks.add(0.2, purpur);
        blocks.add(0.2, duskbound);
        blocks.add(1, rock());
        blocks.add(0.25, ores());
        blocks.add(0.05, oreBlocks());
        blocks.add(0.4, new BlockInfo().add(new ResourceLocation("natura", "clouds:1")));
        blocks.add(0.05, new BlockInfo().add(Blocks.MOB_SPAWNER));

    }

    @Override
    public void registerLoot(RandomCollection<ResourceLocation> tables) {
        tables.add(10, LootTableList.CHESTS_END_CITY_TREASURE);
    }

    @Override
    public void registerMobs(RandomCollection<ResourceLocation> mob) {
        mob.add(10, new ResourceLocation("enderman"));
        mob.add(8, new ResourceLocation("endermite"));
        mob.add(2, new ResourceLocation("shulker"));
    }

    public static RandomCollection<BlockInfo> ores() {
        return new RandomCollection<BlockInfo>()
                .add(5, new BlockInfo().add("oreEndOsmium"))
                .add(5, new BlockInfo().add("oreEndSilver"))
                .add(1, new BlockInfo().add("oreEndPlatinum"))
                .add(8, new BlockInfo().add("oreEndLead"))
                .add(0.5, new BlockInfo().add("oreEndIridium"))
                .add(1, new BlockInfo().add("oreEndDiamond"))
                .add(2, new BlockInfo().add("oreAmethyst"))
                .add(8, new BlockInfo().add("oreBiotite"))
                .add(5, new BlockInfo().add("oreClathrateEnder"));
    }

    public static RandomCollection<BlockInfo> oreBlocks() {
        return new RandomCollection<BlockInfo>()
                .add(1, new BlockInfo().add(new ResourceLocation("biomesoplenty", "gem_block:0")))
                .add(7, new BlockInfo().add(new ResourceLocation("quark", "biotite_block")));
    }

    public static RandomCollection<BlockInfo> rock() {
        return new RandomCollection<BlockInfo>()
                .add(0.05, new BlockInfo().add(new ResourceLocation("biomesoplenty", "grass:0")))
                .add(0.06, new BlockInfo().add(Blocks.END_STONE).addAt(EnumFacing.UP, Blocks.CHORUS_FLOWER))
                .add(1, new BlockInfo().add(Blocks.END_STONE))
                .add(0.2, new BlockInfo().add(Blocks.OBSIDIAN));
    }

    @SubscribeEvent
    public static void clickBlock(PlayerInteractEvent.RightClickBlock event) {

        if(event.getWorld().isRemote)
            return;

        IBlockState state = event.getWorld().getBlockState(event.getPos());
        if(state.getBlock() == Blocks.END_PORTAL_FRAME) {

            boolean b = true;
            boolean eye = !event.getItemStack().isEmpty() && event.getItemStack().getItem() == Items.ENDER_EYE;

            BlockPos anchor = anchorPos(event.getPos(), event.getWorld());
            for(int x = 0; x <= 2; x++)
                for(int z = 0; z <= 2; z++)
                    if((z != 1 || x != 1) && !(anchor.add(x, 0, z).equals(event.getPos()) && eye))  {
                        IBlockState s = event.getWorld().getBlockState(anchor.add(x, 0, z));
                        b = b && s.getBlock() == Blocks.END_PORTAL_FRAME && s.getValue(BlockEndPortalFrame.EYE);
                    }

            if(b)
                event.getWorld().setBlockState(anchor.add(1, 0, 1), Blocks.END_PORTAL.getDefaultState(), 2);

        }

    }

    private static BlockPos anchorPos(BlockPos p, World world) {

        if(world.getBlockState(p.add(-1, 0, 0)).getBlock() == Blocks.END_PORTAL_FRAME)
            return anchorPos(p.add(-1, 0, 0), world);

        if(world.getBlockState(p.add(0, 0, -1)).getBlock() == Blocks.END_PORTAL_FRAME)
            return anchorPos(p.add(0, 0, -1), world);

        return p;

    }

}
