package possibletriangle.skygrid.generator.custom;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.OverworldBiomeProvider;
import net.minecraft.world.biome.provider.OverworldBiomeProviderSettings;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.LayerUtil;
import possibletriangle.skygrid.data.loading.DimensionLoader;

import java.util.Set;
import java.util.function.Supplier;

public class CustomBiomeProvider extends BiomeProvider {

    private CreateOptions options;

    private static CreateOptions findCreateOptions(DimensionType type) {
        return DimensionLoader.findConfig(type.getRegistryName()).getCreateOptions().orElseThrow(() ->
                new IllegalStateException("Only custom dimensions for registered skygrid configs with create options are allowed")
        );
    }

    public CustomBiomeProvider(DimensionType type, long seed) {
        super(findCreateOptions(type).getBiomes());
        this.genBiomes = LayerUtil.func_227474_a_(seed, WorldType.DEFAULT, new OverworldGenSettings());
    }

    private final Layer genBiomes;

    public Biome getNoiseBiome(int x, int y, int z) {
        return this.genBiomes.func_215738_a(x, z);
    }
}
