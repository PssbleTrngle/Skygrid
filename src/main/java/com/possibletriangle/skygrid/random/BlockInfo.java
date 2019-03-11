package com.possibletriangle.skygrid.random;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.possibletriangle.skygrid.ConfigSkygrid;
import com.possibletriangle.skygrid.IJsonAble;
import com.possibletriangle.skygrid.Skygrid;
import com.possibletriangle.skygrid.blocks.BlockFrame;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.fluids.IFluidBlock;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BlockInfo implements IJsonAble {

    @Override
    public String key() {
        return "blockinfo";
    }

    private final RandomCollectionBlocks block = new RandomCollectionBlocks();
    private final HashMap<BlockPos, RandomCollectionBlocks> at = new HashMap<>();
    private Condition condition;
    private boolean valid = false;

    @Override
    public boolean isValid() { return valid; }

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

    public void generateAt(ChunkPrimer primer, int x, int z, int y, Random random, IBlockState fillblock) {

        if(x < 0 || x > 15 || y < 0 || y > 255 || z < 0 || z > 15) {
            Skygrid.LOGGER.error("Illegal block generation at {}/{}/{}", x, y, z);
            return;
        }

        IBlockState block = this.block.next(random);
        if(block != null) {
            primer.setBlockState(x, y, z, randomizeState(block, random));

            for(BlockPos p : at.keySet()) if(!(p.getX()+x < 0 || p.getX()+x > 15 || p.getY()+y < 0 || p.getY()+y > 255 || p.getZ()+z < 0 || p.getZ()+z > 15)) {
                IBlockState blockAt = at.get(p).next(random);
                if(blockAt != null)
                    primer.setBlockState(p.getX() + x, p.getY() + y, p.getZ() + z, randomizeState(blockAt, random));

            }

            if(random.nextDouble() <= ConfigSkygrid.FRAME_CHANCE) {
                if (block.getBlock() instanceof BlockLiquid || block.getBlock() instanceof IFluidBlock) {
                    for (EnumFacing face : EnumFacing.values()) {
                        BlockPos p = new BlockPos(0, 0, 0).offset(face);
                        if (primer.getBlockState(p.getX() + x, p.getY() + y, p.getZ() + z).getBlock() == fillblock.getBlock())
                            primer.setBlockState(p.getX() + x, p.getY() + y, p.getZ() + z, BlockFrame.FRAME.getDefaultState());

                    }
                }
            }
        } else Skygrid.LOGGER.error("BlockInfo is empty or does not contain any existing blocks: {}", this::toJSON);

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
                block = block.withProperty(BlockLeaves.CHECK_DECAY, false);

        }
        return block.withRotation(rot);

    }

    public double factorAt(int y) {
        return condition == null ? 1 : condition.factorAt(y);
    }

    public interface Condition {
        double factorAt(int y);
    }

    @Override
    public void fromJSON(JsonElement json) {

        block.clear();
        at.clear();

        block.fromJSON(json.getAsJsonObject().get("blocks"));
        for(Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) if(entry.getKey().startsWith("at")) {
            String[] i = entry.getKey().replace("at[", "").replace("]", "").split(",");
            BlockPos p = new BlockPos(Integer.parseInt(i[0]), Integer.parseInt(i[1]), Integer.parseInt(i[2]));
            RandomCollectionBlocks r = new RandomCollectionBlocks();
            r.fromJSON(entry.getValue());
            at.put(p, r);
        }

    }

    @Override
    public JsonElement toJSON() {

        JsonObject json = new JsonObject();

        json.add("blocks", block.toJSON());
        for(BlockPos pos : at.keySet()) {
            String key = "at[" + pos.getX() + "," + pos.getY() + "," + pos.getZ() + "]";
            json.add(key, at.get(pos).toJSON());
        }

        return json;

    }

    public void validate() {
        valid = block.next(new Random()) != null;
    }

}
