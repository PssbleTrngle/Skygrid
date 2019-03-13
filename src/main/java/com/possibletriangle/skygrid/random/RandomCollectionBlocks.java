package com.possibletriangle.skygrid.random;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.possibletriangle.skygrid.IJsonAble;
import com.possibletriangle.skygrid.Skygrid;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class RandomCollectionBlocks extends RandomCollection<Object> implements IJsonAble {

    @Override
    public boolean isValid() {
        return size() > 0;
    }

    @Override
    public void validate() {}

    @Override
    public String key() {
        return "values";
    }

    @Override
    public RandomCollectionBlocks add(double weight, Object result) {

        return (RandomCollectionBlocks) super.add(weight, result);
    }

    @Override
    public IBlockState next(Random random) {

        for(int i = 0; i < 100; i++) {
            Object o = super.next(random);
            IBlockState rs = null;

            if (o instanceof ResourceLocation)
                rs = stateFrom((ResourceLocation) o, random);

            if (o instanceof IBlockState)
                rs =  (IBlockState) o;

            else if (o instanceof Block)
                rs =  stateFrom(((Block) o).getRegistryName(), random);

            else if (o instanceof String) {

                String ore = (String) o;
                boolean r = false;
                if(ore.contains(":")) {
                    if("random".equals(ore.substring(0, ore.indexOf(':'))))
                        r = true;
                    ore = ore.substring(ore.lastIndexOf(':')+1);
                }

                ArrayList<IBlockState> ores = new ArrayList<>();
                for (ItemStack stack : OreDictionary.getOres(ore))
                    if (stack.getItem() instanceof ItemBlock)
                        ores.add(((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata()));
                if (!ores.isEmpty()) {
                    int index = r ? random.nextInt(ores.size()) : 0;
                    rs =  ores.get(index);
                }
            }

            if(rs != null) return rs;

        }

        return null;
    }

    @Nullable
    private static IBlockState stateFrom(@Nullable ResourceLocation r, Random random) {

        if(r == null) return null;

        ResourceLocation name = r.getResourcePath().contains(":") ? new ResourceLocation(r.getResourceDomain(), r.getResourcePath().substring(0, r.getResourcePath().indexOf(':'))) : r;
        Block block = Block.REGISTRY.getObject(name);

        if(!Block.REGISTRY.containsKey(name)) return null;
        if(block == Blocks.AIR) return block.getDefaultState();

        ArrayList<Integer> metas = new ArrayList<>();
        for(IBlockState state : block.getBlockState().getValidStates())
            metas.add(block.getMetaFromState(state));
        int meta = metas.isEmpty() ? 0 : metas.get(random.nextInt(metas.size()));

        if(r.getResourcePath().contains(":")) {
            String m = r.getResourcePath().substring(r.getResourcePath().indexOf(':')+1);
            try {
                meta = Integer.parseInt(m);
            } catch (NumberFormatException ex) {
                Skygrid.LOGGER.error("{} is not a valid metadata (\"{}\")", m, r);
            }
        }

        return block.getStateFromMeta(meta);
    }

    @Override
    public void fromJSON(JsonElement json) {

        for(JsonElement e : json.getAsJsonArray()) {

            JsonObject o = e.getAsJsonObject();

            double weight = o.get("weight").getAsDouble();
            if(o.has("values")) {
                RandomCollectionBlocks sub = new RandomCollectionBlocks();
                sub.fromJSON(o.get("values"));
                add(weight, sub);
            } else {
                int meta = o.has("meta") ? o.get("meta").getAsInt() : -1;
                String ore = o.has("ore") ? o.get("ore").getAsString() : null;
                ResourceLocation block = o.has("block") ? new ResourceLocation(o.get("block").getAsString()) : null;
                if(meta != -1 && block != null) {
                    block = new ResourceLocation(block.getResourceDomain(), block.getResourcePath() + ":" + meta);
                }

                if(ore != null)
                    add(weight, ore);
                else if(block != null)
                    add(weight, block);
            }

        }

    }

    @Override
    public JsonElement toJSON() {

        JsonArray array = new JsonArray();

        double total = 0;
        for(Map.Entry<Double, Object> entry : map.entrySet()) {

            JsonObject o = new JsonObject();
            o.addProperty("weight", entry.getKey()-total);
            total = entry.getKey();

            if(entry.getValue() instanceof RandomCollectionBlocks) {

                o.add("values", ((RandomCollectionBlocks) entry.getValue()).toJSON());

            } else {
                String s = null;
                ResourceLocation r = null;
                int meta = -1;

                if (entry.getValue() instanceof Block)
                    r = ((Block) entry.getValue()).getRegistryName();
                else if (entry.getValue() instanceof IBlockState) {
                    r = ((IBlockState) entry.getValue()).getBlock().getRegistryName();
                    meta = ((IBlockState) entry.getValue()).getBlock().getMetaFromState((IBlockState) entry.getValue());
                }
                else if (entry.getValue() instanceof ResourceLocation) {
                    r = (ResourceLocation) entry.getValue();
                    if(r.getResourcePath().contains(":"))
                        try {
                            meta = Integer.parseInt(r.getResourcePath().substring(r.getResourcePath().indexOf(':')+1));
                        } catch (NumberFormatException ignored) {
                        }

                    r = r.getResourcePath().contains(":") ? new ResourceLocation(r.getResourceDomain(), r.getResourcePath().substring(0, r.getResourcePath().indexOf(':'))) : r;

                } else if(entry.getValue() instanceof String) {
                    s = (String) entry.getValue();
                }

                if(r != null)
                    o.addProperty("block", r.toString());
                else if(s != null)
                    o.addProperty("ore", s);

                if(meta != -1)
                    o.addProperty("meta", meta);

            }

            array.add(o);

        }

        return array;

    }

}
