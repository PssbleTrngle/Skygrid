package com.possibletriangle.skygrid.random;

import com.possibletriangle.skygrid.Skygrid;
import com.sun.javafx.beans.IDProperty;
import net.minecraft.block.*;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.dispenser.IPosition;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.registries.ForgeRegistry;
import scala.sys.PropImpl;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Predicate;

public class BlockInfo {

    private final RandomCollectionBlocks block = new RandomCollectionBlocks();
    private final HashMap<BlockPos, RandomCollectionBlocks> at = new HashMap<>();
    private Condition condition;

    public BlockInfo cond(Condition condition) {
        this.condition = condition;
        return this;
    }

    public BlockInfo add(Object result, double weight) {
        block.add(weight, result);
        return this;
    }

    public BlockInfo add(Object result) {
        return add(result, 1);
    }

    public BlockInfo addAtAll(Object result) {
        return addAtAll(result, 1);
    }

    public BlockInfo addAtAll(Object result, double weight) {
        for(EnumFacing side : EnumFacing.values()) if(at.get(new BlockPos(0, 0, 0).offset(side)) == null) addAt(side, result, weight);
        return this;
    }

    public BlockInfo addAt(BlockPos pos, Object result, double weight) {
        RandomCollectionBlocks r = at.containsKey(pos) ? at.get(pos) : new RandomCollectionBlocks();
        r.add(weight, result);
        at.put(pos, r);
        return this;
    }

    public BlockInfo addAt(BlockPos pos, Object result) {
        return addAt(pos, result, 1);
    }

    public BlockInfo addAt(EnumFacing face, Object result, double weight) {
        return addAt(new BlockPos(0,0,0).offset(face), result, weight);
    }

    public BlockInfo addAt(EnumFacing face, Object result) {
        return addAt(face, result, 1);
    }

    public void generateAt(ChunkPrimer primer, int x, int z, int y, Random random) {

        if(x < 0 || x > 15 || y < 0 || y > 255 || z < 0 || z > 15) {
            Skygrid.LOGGER.error("Illegal block generation at {}/{}/{}", x, y, z);
            return;
        }

        IBlockState block = this.block.next(random);
        if(block != null)
            primer.setBlockState(x, y, z, randomizeState(block, random));

        for(BlockPos p : at.keySet()) if(!(p.getX()+x < 0 || p.getX()+x > 15 || p.getY()+y < 0 || p.getY()+y > 255 || p.getZ()+z < 0 || p.getZ()+z > 15)) {
            IBlockState blockAt = at.get(p).next(random);
            if(blockAt != null)
                primer.setBlockState(p.getX() + x, p.getY() + y, p.getZ() + z, randomizeState(blockAt, random));

        }

    }

    private static IBlockState randomizeState(IBlockState block, Random random) {

        if(block.getBlock() instanceof BlockLiquid || block.getBlock() instanceof IFluidBlock)
            return block.getBlock().getDefaultState();

        Rotation rot = Rotation.values()[random.nextInt(Rotation.values().length)];
        BlockLog.EnumAxis axis = BlockLog.EnumAxis.values()[random.nextInt(BlockLog.EnumAxis.values().length)];

        for(IProperty prop : block.getProperties().keySet()) {
            if(prop.getValueClass().isInstance(BlockLog.EnumAxis.Y))
                block = block.withProperty(prop, axis);
            else if(prop.getValueClass().isInstance(EnumDyeColor.ORANGE)) {
                Object[] values = prop.getAllowedValues().toArray();
                block = block.withProperty(prop, (EnumDyeColor) values[random.nextInt(values.length)]);
            }
            else if("age".equals(prop.getName()) && prop instanceof PropertyInteger) {
                Integer[] values = ((PropertyInteger) prop).getAllowedValues().toArray(new Integer[0]);
                int age = values[random.nextInt(values.length)];
                block = block.withProperty(prop, age);
            }
            else if(BlockLeaves.DECAYABLE.equals(prop))
                block = block.withProperty(BlockLeaves.DECAYABLE, false);

        }
        return block.withRotation(rot);

    }

    public double factorAt(int y) {
        return condition == null ? 1 : condition.factorAt(y);
    }

    public interface Condition {
        double factorAt(int y);
    }
}
