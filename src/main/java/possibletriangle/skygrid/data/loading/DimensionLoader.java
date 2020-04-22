package possibletriangle.skygrid.data.loading;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import possibletriangle.skygrid.Skygrid;
import possibletriangle.skygrid.generator.custom.CustomDimension;
import possibletriangle.skygrid.provider.BlockProvider;
import possibletriangle.skygrid.provider.RandomCollectionProvider;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DimensionLoader extends ReloadListener<List<LoadingResource<?>>> {

    private final XMLLoader xmlLoader;

    public DimensionLoader(MinecraftServer server) {
        this.xmlLoader = new XMLLoader(server.getNetworkTagManager(), server.getLootTableManager());
    }

    public static Optional<BlockProvider> findRef(ResourceLocation name) {
        return Optional.ofNullable(REFS.get(name));
    }

    private static final HashMap<ResourceLocation, DimensionConfig> CONFIGS = new HashMap<>();
    private static final HashMap<ResourceLocation, List<Consumer<DimensionConfig>>> listeners = Maps.newHashMap();
    private static final HashMap<ResourceLocation, BlockProvider> REFS = Maps.newHashMap();

    private static void updateConfigs() {
        listeners.forEach((dimension, listeners) -> {
            DimensionConfig config = findConfig(dimension);
            listeners.forEach(l -> l.accept(config));
        });
    }

    public static DimensionConfig findConfig(ResourceLocation dimension) {
        return CONFIGS.getOrDefault(dimension, DimensionConfig.FALLBACK);
    }

    public static void findConfig(DimensionType dimension, Consumer<DimensionConfig> listener) {
        ResourceLocation id = dimension.getRegistryName();
        listeners.putIfAbsent(id, Lists.newArrayList());
        listeners.get(id).add(listener);
        listener.accept(findConfig(id));
    }

    private static final Logger LOGGER = LogManager.getLogger();

    @Nonnull
    @Override
    protected List<LoadingResource<?>> prepare(IResourceManager manager, IProfiler profiler) {

        Collection<ResourceLocation> dimensions = manager.getAllResourceLocations(Skygrid.MODID + "/dimensions", s -> s.endsWith(".xml"));
        Collection<ResourceLocation> refs = manager.getAllResourceLocations(Skygrid.MODID + "/blocks", s -> s.endsWith(".xml"));

        return Stream.of(

                dimensions.stream()
                        .peek(r -> profiler.startSection(r::toString))
                        .map(name -> loadConfig(manager, name))
                        .peek(r -> profiler.endSection()),

                refs.stream()
                        .peek(r -> profiler.startSection(r::toString))
                        .map(name -> loadProvider(manager, name))
                        .peek(r -> profiler.endSection())

        ).flatMap(Function.identity()).collect(Collectors.toList());
    }

    private LoadingResource<DimensionConfig> loadConfig(IResourceManager manager, ResourceLocation name) {
        return LoadingResource.attempt(xmlLoader, name, xmlLoader::parseConfig, DimensionConfig::merge, CONFIGS::put, tryLoading(manager, name));
    }

    private LoadingResource<RandomCollectionProvider> loadProvider(IResourceManager manager, ResourceLocation name) {
        return LoadingResource.attempt(xmlLoader, name, xmlLoader::parseSingleProvider, (a, b) -> b, REFS::put, tryLoading(manager, name));
    }

    private static List<IResource> tryLoading(IResourceManager manager, ResourceLocation name) {
        try {
            return manager.getAllResources(name);
        } catch (IOException e) {
            LOGGER.warn("Error on loading resource for {}", name.toString());
            return new ArrayList<>();
        }
    }

    @Override
    protected void apply(List<LoadingResource<?>> loading, IResourceManager manager, IProfiler profiler) {
        CONFIGS.clear();
        REFS.clear();

        loading.forEach(LoadingResource::parse);

        LOGGER.info("Loaded {} skygrid configs", CONFIGS.size());


        List<ResourceLocation> existing = existingDimensions().collect(Collectors.toList());
        CONFIGS.entrySet().stream().sorted((a, b) -> {
            if (existing.contains(a.getKey())) return 1;
            return 0;
        }).forEachOrdered(e -> e.getValue().getCreateOptions().ifPresent($ -> register(e.getKey())));

        updateConfigs();
    }

    private static void register(ResourceLocation name) {
        DimensionManager.registerOrGetDimension(name, CustomDimension.create(name), null, true);
    }

    public static Stream<ResourceLocation> existingDimensions() {
        return SAVED_DIMENSIONS.stream();
    }

    private static final List<ResourceLocation> SAVED_DIMENSIONS = Lists.newArrayList();

    public static void readRegistry(CompoundNBT data) {

        ListNBT list = data.getList("entries", 10);
        for (int x = 0; x < list.size(); x++) {

            ResourceLocation name = new ResourceLocation(data.getString("name"));
            ResourceLocation type = data.contains("type", 8) ? new ResourceLocation(data.getString("type")) : null;

            if (type != null) {
                ModDimension mod = ForgeRegistries.MOD_DIMENSIONS.getValue(type);
                if (mod == null) SAVED_DIMENSIONS.add(name);
            }
        }
    }

}
