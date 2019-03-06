package com.possibletriangle.skygrid.random;

import com.possibletriangle.skygrid.Skygrid;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomCollectionBlocks extends RandomCollection<Object> {

    @Override
    public RandomCollectionBlocks add(double weight, Object result) {

        if(result instanceof ResourceLocation) {

            ResourceLocation r = (ResourceLocation) result;
            ResourceLocation name = r.getResourcePath().contains(":") ? new ResourceLocation(r.getResourceDomain(), r.getResourcePath().substring(0, r.getResourcePath().indexOf(':'))) : r;

            Block block = Block.REGISTRY.getObject(name);
            if(block == Blocks.AIR) {
                Skygrid.LOGGER.error("Block does not exist: \"{}\"", result);
                return this;
            }

            return (RandomCollectionBlocks) super.add(weight, result);

        } else if(result instanceof Block || result instanceof IBlockState || result instanceof String)
            return (RandomCollectionBlocks) super.add(weight, result);

        return this;
    }

    @Override
    public IBlockState next(Random random) {

        Object o = super.next(random);

        IBlockState ret = null;

        for(int i = 0; i < 100; i++) {

            if (o instanceof ResourceLocation)
                return stateFrom((ResourceLocation) o, random);

            if (o instanceof IBlockState)
                return (IBlockState) o;

            else if (o instanceof Block)
                return stateFrom(((Block) o).getRegistryName(), random);

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
                    return ores.get(index);
                }
            }
        }

        return null;
    }

    private static IBlockState stateFrom(ResourceLocation r, Random random) {

        ResourceLocation name = r.getResourcePath().contains(":") ? new ResourceLocation(r.getResourceDomain(), r.getResourcePath().substring(0, r.getResourcePath().indexOf(':'))) : r;
        Block block = Block.REGISTRY.getObject(name);

        ArrayList<Integer> metas = new ArrayList<>();
        for(IBlockState state : block.getBlockState().getValidStates())
            metas.add(block.getMetaFromState(state));
        int meta = metas.isEmpty() ? 0 : metas.get(random.nextInt(metas.size()));

        if(r.getResourcePath().contains(":"))
            try {
                meta = Integer.parseInt(r.getResourcePath().substring(r.getResourcePath().indexOf(':')+1));
            } catch (NumberFormatException ex) {
            }

        return block.getStateFromMeta(meta);
    }

}
