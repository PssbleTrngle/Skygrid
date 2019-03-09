package com.possibletriangle.skygrid.generation;

import net.minecraft.init.Biomes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class WorldProviderLimbo extends WorldProviderSkygrid {

    public void init() {
        this.biomeProvider = new BiomeProviderSingle(Biomes.SKY);
    }

    public float calculateCelestialAngle(long worldTime, float partialTicks)
    {
        return 0.0F;
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks)
    {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) {

        float f = MathHelper.cos(p_76562_1_ * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        float f1 = 0.627451F;
        float f2 = 0.5019608F;
        float f3 = 0.627451F;
        f1 = f1 * (f * 0.0F + 0.15F);
        f2 = f2 * (f * 0.0F + 0.15F);
        f3 = f3 * (f * 0.0F + 0.15F);
        return new Vec3d((double)f1, (double)f2, (double)f3);
    }


    @SideOnly(Side.CLIENT)
    public boolean isSkyColored()
    {
        return false;
    }

    public boolean isSurfaceWorld() {
        return false;
    }


}
