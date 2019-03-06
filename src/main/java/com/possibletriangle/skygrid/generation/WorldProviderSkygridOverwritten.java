package com.possibletriangle.skygrid.generation;

import com.possibletriangle.skygrid.Skygrid;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.DungeonHooks;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.Random;

public class WorldProviderSkygridOverwritten extends WorldProviderSkygrid {

    private WorldProvider old;
    public WorldProviderSkygridOverwritten() {
    }

    /* -------------------- OLD -------------------- */

    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        return old.calculateCelestialAngle(worldTime, partialTicks);
    }

    @Override
    public int getMoonPhase(long worldTime) {
        return old.getMoonPhase(worldTime);
    }

    @Override
    public boolean isSurfaceWorld() {
        return old.isSurfaceWorld();
    }

    @Nullable
    @Override
    public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {
        return old.calcSunriseSunsetColors(celestialAngle, partialTicks);
    }

    @Override
    public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) {
        return old.getFogColor(p_76562_1_, p_76562_2_);
    }

    @Override
    public boolean canRespawnHere() {
        return old.canRespawnHere();
    }

    @Override
    public float getCloudHeight() {
        return old.getCloudHeight();
    }

    @Override
    public boolean isSkyColored() {
        return old.isSkyColored();
    }

    @Override
    public boolean doesWaterVaporize() {
        return old.doesWaterVaporize();
    }

    @Override
    protected void init() {
        super.init();
        this.old = DimensionHelper.OLD.get(getName());
        old.setWorld(world);
    }

    @Override
    public boolean hasSkyLight() {
        return old.hasSkyLight();
    }

    @Override
    public boolean isNether() {
        return old.isNether();
    }

    @Override
    public float[] getLightBrightnessTable() {
        return old.getLightBrightnessTable();
    }

    @Override
    public double getMovementFactor() {
        return old.getMovementFactor();
    }

    @Override
    public boolean shouldClientCheckLighting() {
        return old.shouldClientCheckLighting();
    }

    @Nullable
    @Override
    public IRenderHandler getSkyRenderer() {
        return old.getSkyRenderer();
    }

    @Nullable
    @Override
    public IRenderHandler getCloudRenderer() {
        return old.getCloudRenderer();
    }

    @Nullable
    @Override
    public IRenderHandler getWeatherRenderer() {
        return old.getWeatherRenderer();
    }

    @Override
    public boolean shouldMapSpin(String entity, double x, double z, double rotation) {
        return old.shouldMapSpin(entity, x, z, rotation);
    }

    @Nullable
    @Override
    public MusicTicker.MusicType getMusicType() {
        return old.getMusicType();
    }

    @Override
    public Vec3d getSkyColor(Entity cameraEntity, float partialTicks) {
        return old.getSkyColor(cameraEntity, partialTicks);
    }

    @Override
    public Vec3d getCloudColor(float partialTicks) {
        return old.getCloudColor(partialTicks);
    }

    @Override
    public void getLightmapColors(float partialTicks, float sunBrightness, float skyLight, float blockLight, float[] colors) {
        old.getLightmapColors(partialTicks, sunBrightness, skyLight, blockLight, colors);
    }

    @Override
    public BiomeProvider getBiomeProvider() {
        return old.getBiomeProvider();
    }

    @Override
    public boolean canCoordinateBeSpawn(int x, int z) {
        return old.canCoordinateBeSpawn(x, z);
    }

    @Override
    public int getAverageGroundLevel() {
        return old.getAverageGroundLevel();
    }

    @Override
    public double getVoidFogYFactor() {
        return old.getVoidFogYFactor();
    }

    @Override
    public boolean doesXZShowFog(int x, int z) {
        return old.doesXZShowFog(x, z);
    }

    @Override
    public void setSkyRenderer(IRenderHandler skyRenderer) {
        old.setSkyRenderer(skyRenderer);
    }

    @Override
    public void setCloudRenderer(IRenderHandler renderer) {
        old.setCloudRenderer(renderer);
    }

    @Override
    public void setWeatherRenderer(IRenderHandler renderer) {
        old.setWeatherRenderer(renderer);
    }

    @Override
    public int getRespawnDimension(EntityPlayerMP player) {
        return old.getRespawnDimension(player);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities() {
        return old.initCapabilities();
    }

    @Override
    public WorldSleepResult canSleepAt(EntityPlayer player, BlockPos pos) {
        return old.canSleepAt(player, pos);
    }

    @Override
    public Biome getBiomeForCoords(BlockPos pos) {
        return old.getBiomeForCoords(pos);
    }

    @Override
    public boolean isDaytime() {
        return old.isDaytime();
    }

    @Override
    public float getSunBrightnessFactor(float par1) {
        return old.getSunBrightnessFactor(par1);
    }

    @Override
    public float getCurrentMoonPhaseFactor() {
        return old.getCurrentMoonPhaseFactor();
    }

    @Override
    public float getSunBrightness(float par1) {
        return old.getSunBrightness(par1);
    }

    @Override
    public float getStarBrightness(float par1) {
        return old.getStarBrightness(par1);
    }

    @Override
    public void setAllowedSpawnTypes(boolean allowHostile, boolean allowPeaceful) {
        old.setAllowedSpawnTypes(allowHostile, allowPeaceful);
    }

    @Override
    public void calculateInitialWeather() {
        old.calculateInitialWeather();
    }

    @Override
    public void updateWeather() {
        old.updateWeather();
    }

    @Override
    public boolean canBlockFreeze(BlockPos pos, boolean byWater) {
        return old.canBlockFreeze(pos, byWater);
    }

    @Override
    public boolean canSnowAt(BlockPos pos, boolean checkLight) {
        return old.canSnowAt(pos, checkLight);
    }

    @Override
    public void setSpawnPoint(BlockPos pos) {
        old.setSpawnPoint(pos);
    }

    @Override
    public boolean canMineBlock(EntityPlayer player, BlockPos pos) {
        return old.canMineBlock(player, pos);
    }

    @Override
    public boolean isBlockHighHumidity(BlockPos pos) {
        return old.isBlockHighHumidity(pos);
    }

    @Override
    public int getHeight() {
        return old.getHeight();
    }

    @Override
    public int getActualHeight() {
        return old.getActualHeight();
    }

    @Override
    public void resetRainAndThunder() {
        old.resetRainAndThunder();
    }

    @Override
    public boolean canDoLightning(Chunk chunk) {
        return old.canDoLightning(chunk);
    }

    @Override
    public boolean canDoRainSnowIce(Chunk chunk) {
        return old.canDoRainSnowIce(chunk);
    }

    @Override
    public void onPlayerAdded(EntityPlayerMP player) {
        old.onPlayerAdded(player);
    }

    @Override
    public void onPlayerRemoved(EntityPlayerMP player) {
        old.onPlayerRemoved(player);
    }

    @Override
    public void onWorldSave() {
        old.onWorldSave();
    }

    @Override
    public void onWorldUpdateEntities() {
        old.onWorldUpdateEntities();
    }

    @Override
    public boolean canDropChunk(int x, int z) {
        return old.canDropChunk(x, z);
    }
}
