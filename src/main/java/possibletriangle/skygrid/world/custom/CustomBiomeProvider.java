package possibletriangle.skygrid.world.custom;

import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.LayerUtil;
import possibletriangle.skygrid.util.loading.CreateOptions;

import java.util.function.Supplier;

public class CustomBiomeProvider extends BiomeProvider {

    public CustomBiomeProvider(Supplier<CreateOptions> options, long seed) {
        super(options.get().getBiomes());
        this.genBiomes = LayerUtil.func_227474_a_(seed, WorldType.DEFAULT, new OverworldGenSettings());
    }

    private final Layer genBiomes;

    public Biome getNoiseBiome(int x, int y, int z) {
        return this.genBiomes.func_215738_a(x, z);
    }
}
