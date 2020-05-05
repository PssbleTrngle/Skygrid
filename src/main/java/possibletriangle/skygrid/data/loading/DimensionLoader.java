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
import org.xml.sax.SAXException;
import possibletriangle.skygrid.Skygrid;
import possibletriangle.skygrid.world.custom.CustomDimension;
import possibletriangle.skygrid.provider.BlockProvider;
import possibletriangle.skygrid.provider.RandomCollectionProvider;

import javax.annotation.Nonnull;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystemNotFoundException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DimensionLoader extends ReloadListener<List<LoadingResource<?>>> {

    private final MinecraftServer server;

    public DimensionLoader(MinecraftServer server) {
        this.server = server;
    }

    public static Stream<ResourceLocation> allConfigs() {
        return CONFIGS.keySet().stream();
    }

    public static Optional<BlockProvider> findRef(ResourceLocation name) {
        return Optional.ofNullable(REFS.get(name));
    }

    private static final HashMap<ResourceLocation, DimensionConfig> CONFIGS = new HashMap<>();
    private static final HashMap<ResourceLocation, List<Consumer<DimensionConfig>>> listeners = Maps.newHashMap();
    private static final HashMap<ResourceLocation, BlockProvider> REFS = Maps.newHashMap();

    private static void updateConfigs() {
        listeners.forEach((dimension, listeners) -> {
            DimensionConfig config = findOrDefault(dimension);
            listeners.forEach(l -> l.accept(config));
        });
    }

    public static DimensionConfig findOrDefault(ResourceLocation dimension) {
        return find(dimension).orElse(DimensionConfig.FALLBACK);
    }

    public static Optional<DimensionConfig> find(ResourceLocation dimension) {
        return Optional.ofNullable(CONFIGS.get(dimension));
    }

    public static void subscribeConfig(DimensionType dimension, Consumer<DimensionConfig> listener) {
        ResourceLocation id = dimension.getRegistryName();
        listeners.putIfAbsent(id, Lists.newArrayList());
        listeners.get(id).add(listener);
        listener.accept(findOrDefault(id));
    }

    private static final Logger LOGGER = LogManager.getLogger();

    private XMLLoader createXMLLoader(IResourceManager manager) throws IOException, SAXException {
        ResourceLocation r = manager.getAllResourceLocations(Skygrid.MODID, f -> f.endsWith(".xsd"))
                .stream().findFirst().orElseThrow(() -> new FileSystemNotFoundException("Could not find any xsd schema resource"));

        InputStream input = manager.getResource(r).getInputStream();

        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new StreamSource(input));
        return new XMLLoader(server.getNetworkTagManager(), server.getLootTableManager(), schema);
    }

    @Nonnull
    @Override
    protected List<LoadingResource<?>> prepare(IResourceManager manager, IProfiler profiler) {

        XMLLoader xmlLoader;
        try {
            xmlLoader = createXMLLoader(manager);
        } catch (Exception ex) {
            LOGGER.error("Could not load skygrid xml schema");
            return Lists.newArrayList();
        }

        Collection<ResourceLocation> dimensions = manager.getAllResourceLocations(Skygrid.MODID + "/dimensions", s -> s.endsWith(".xml"));
        Collection<ResourceLocation> refs = manager.getAllResourceLocations(Skygrid.MODID + "/blocks", s -> s.endsWith(".xml"));

        LOGGER.info("Found {} skygrid configurations", dimensions.size());

        return Stream.of(

                dimensions.stream()
                        .peek(r -> profiler.startSection(r::toString))
                        .map(name -> loadConfig(manager, name, xmlLoader))
                        .peek(r -> profiler.endSection()),

                refs.stream()
                        .peek(r -> profiler.startSection(r::toString))
                        .map(name -> loadProvider(manager, name, xmlLoader))
                        .peek(r -> profiler.endSection())

        ).flatMap(Function.identity()).collect(Collectors.toList());
    }

    private LoadingResource<DimensionConfig> loadConfig(IResourceManager manager, ResourceLocation name, XMLLoader xmlLoader) {
        return LoadingResource.attempt(xmlLoader, name, e -> xmlLoader.parseConfig(e, name), DimensionConfig::merge, CONFIGS::put, tryLoading(manager, name));
    }

    private LoadingResource<RandomCollectionProvider> loadProvider(IResourceManager manager, ResourceLocation name, XMLLoader xmlLoader) {
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
