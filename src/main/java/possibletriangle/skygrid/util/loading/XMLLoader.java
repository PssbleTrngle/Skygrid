package possibletriangle.skygrid.util.loading;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.tags.NetworkTagManager;
import net.minecraft.tags.Tag;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import possibletriangle.skygrid.util.RandomCollection;
import possibletriangle.skygrid.Skygrid;
import possibletriangle.skygrid.util.provider.*;
import possibletriangle.skygrid.util.provider.property.CycleProperty;
import possibletriangle.skygrid.util.provider.property.PropertyProvider;
import possibletriangle.skygrid.util.provider.property.SetProperty;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class XMLLoader {

    private static final Logger LOGGER = LogManager.getLogger();

    private final NetworkTagManager tags;
    private final LootTableManager loot;
    private final Schema schema;

    public XMLLoader(NetworkTagManager tags, LootTableManager loot, Schema schema) {
        this.tags = tags;
        this.loot = loot;
        this.schema = schema;
    }

    public Optional<Tag<Block>> findTag(String mod, String id) {

        Function<String, Optional<Tag<Block>>> find = s -> {
            ResourceLocation r = new ResourceLocation(s, id.replace("#", ""));
            return Optional.ofNullable(this.tags.getBlocks().get(r));
        };

        return Stream.of(mod, Skygrid.MODID, "forge")
                .map(find)
                .filter(Optional::isPresent)
                .findFirst()
                .flatMap(Function.identity());

    }

    private Optional<Tag<Block>> findTag(Element e) {
        final String mod = e.getAttribute("mod");
        final String id = e.getAttribute("id");
        return findTag(mod, id);
    }

    public Predicate<Block> findFilter(Element node) {
        return Stream.of(

                elements(node, "tag")
                        .map(this::findTag)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .<Predicate<Block>>map(t -> t::contains),

                elements(node, "name")
                    .map(e -> e.getAttribute("pattern"))
                    .map(Pattern::compile)
                    .map(Pattern::asPredicate)
                    .<Predicate<Block>>map(p -> b -> p.test(b.getRegistryName().toString()))

            ).flatMap(Function.identity()).reduce(Predicate::or).orElse($ -> true);
    }

    /**
     * @param e The XML Element
     * @return The block provider without attaches or properties
     */
    private Stream<BlockProvider> parseRawProvider(Element e) {
        final String mod = e.getAttribute("mod");
        final String id = e.getAttribute("id");
        final Stream<Pair<Float, BlockProvider>> children = findProviders(e);
        final String name = e.getAttribute("name");

        switch (e.getNodeName().toLowerCase()) {

            case "block":
            case "fill":
                return Stream.of(new SingleBlock(new ResourceLocation(mod, id)));

            case "tag":
                Predicate<Block> include = elements(e, "except")
                        .findFirst()
                        .map(this::findFilter)
                        .map(Predicate::negate)
                        .orElse($ -> true);

                return findTag(e)
                        .map(Tag::getAllElements)
                        .map(Collection::stream)
                        .map(s -> s
                                .filter(include)
                                .map(SingleBlock::new)
                                .map(p -> (BlockProvider) p))
                        .orElseGet(Stream::of);

            case "collection":
                return Stream.of(new RandomCollectionProvider(RandomCollection.from(children), name));

            case "fallback":
                return children.map(Pair::getSecond).findFirst().map(Stream::of).orElseGet(Stream::of);

            case "reference":
                return Stream.of(new BlockReference(new ResourceLocation(mod, id)));

            default:
                return Stream.of();

        }
    }

    public Stream<OffsetBlock> findOffsets(Element node) {
        return elements(node).map(e -> {

            boolean shared = e.hasAttribute("shared") && Boolean.parseBoolean(e.getAttribute("shared"));
            float probability = e.hasAttribute("probability") ? Float.parseFloat(e.getAttribute("probability")) : 0;
            RandomCollectionProvider provider = new RandomCollectionProvider(RandomCollection.from(findProviders(e)), null);
            int by = e.hasAttribute("by") ? Integer.parseInt(e.getAttribute("by")) : 1;

            switch (e.getNodeName().toLowerCase()) {

                case "offset":
                    int x = Integer.parseInt(e.getAttribute("x"));
                    int y = Integer.parseInt(e.getAttribute("y"));
                    int z = Integer.parseInt(e.getAttribute("z"));
                    return Stream.of(new OffsetBlock(provider, new BlockPos(x, y, z), probability, shared));

                case "side":
                    Direction side = Direction.byName(e.getAttribute("on"));
                    return Stream.of(side).filter(Objects::nonNull)
                            .map(s -> new BlockPos(0, 0, 0).offset(s, by))
                            .map(pos -> new OffsetBlock(provider, pos, probability, shared));

                case "horizontal":
                    return Arrays.stream(Direction.values())
                            .filter(d -> d.getAxis() != Direction.Axis.Y)
                            .map(s -> new OffsetBlock(
                                    provider.addProperties(Stream.of(new SetProperty("facing", s.getOpposite().getName()))),
                                    new BlockPos(0, 0, 0).offset(s, by),
                                    probability / 4, shared
                            ));

                default:
                    return Stream.<OffsetBlock>of();

            }
        }).flatMap(Function.identity());
    }

    public Stream<PropertyProvider> findProperties(Element node) {
        return elements(node).map(e -> {
            String key = e.getAttribute("key");

            switch (e.getNodeName().toLowerCase()) {

                case "cycle":
                    return Optional.of(new CycleProperty(key));

                case "set":
                    String value = e.getAttribute("value");
                    return Optional.of(new SetProperty(key, value));

                default:
                    return Optional.<PropertyProvider>empty();

            }
        }).filter(Optional::isPresent).map(Optional::get);
    }

    public Stream<BlockProvider> parseProvider(Element e) {
        return parseRawProvider(e)
                .peek(p -> p.addProperties(findProperties(e)))
                .peek(p -> p.addOffsets(findOffsets(e)));
    }

    public Stream<Pair<Float, BlockProvider>> findProviders(Element node) {
        return elements(node).map(e -> {

            List<BlockProvider> provider = parseProvider(e).collect(Collectors.toList());
            float weight = e.hasAttribute("weight") ? Float.parseFloat(e.getAttribute("weight")) / provider.size() : 0;
            return provider.stream().map(p -> new Pair<>(weight, p));

        }).flatMap(Function.identity()).filter(p -> p.getSecond().isValid());
    }

    public Optional<Element> parse(InputStream input) {
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);

            factory.setSchema(schema);

            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new ErrorHandler());

            Document xml = builder.parse(input);

            Element node = xml.getDocumentElement();
            node.normalize();

            return Optional.of(node);

        } catch (SAXException | IOException | ParserConfigurationException ex) {
            LOGGER.error("Palette XML Error: {}", ex.getMessage());
            return Optional.empty();
        }
    }

    public BlockPos findPos(Element parent, String name) {
        return elements(parent, name).findFirst().map(e -> {
            int x = Integer.parseInt(e.getAttribute("x"));
            int y = Integer.parseInt(e.getAttribute("y"));
            int z = Integer.parseInt(e.getAttribute("z"));
            return new BlockPos(x, y, z);
        }).orElseGet(() -> new BlockPos(0, 0, 0));
    }

    public Optional<ResourceLocation> findLootTable(String mod, String id) {
        Set<ResourceLocation> lootKeys = loot.getLootTableKeys();

        Optional<ResourceLocation> table = Optional.of(new ResourceLocation(mod, id)).filter(lootKeys::contains);

        if (table.isPresent()) return table;
        if (!id.startsWith("chests/")) return findLootTable(mod, "chests/" + id);
        return Optional.empty();
    }

    public Optional<DimensionConfig> parseConfig(Element node, ResourceLocation name) {

        boolean replace = Boolean.parseBoolean(node.getAttribute("replace"));
        CreateOptions create = elements(node, "create").findFirst()
                .map(this::parseCreateOptions)
                .flatMap(Function.identity())
                .orElse(null);

        BlockPos distance = findPos(node, "distance");
        BlockPos cluster = findPos(node, "cluster");

        RandomCollection<BlockProvider> providers = RandomCollection.from(findProviders(node));
        BlockProvider fill = elements(node, "fill").findFirst()
                .map(this::parseSingleProvider)
                .flatMap(Function.identity())
                .orElse(null);

        Element lootElement = elements(node, "loot").findFirst().orElseThrow(() -> new IllegalArgumentException("No loot defined for " + name));
        RandomCollection<ResourceLocation> tables = RandomCollection.from(elements(lootElement, "table").map(e -> {

            String mod = e.getAttribute("mod");
            String id = e.getAttribute("id");
            Float weight = Float.parseFloat(e.getAttribute("weight"));
            return findLootTable(mod, id).map(t -> new Pair<>(weight, t));

        }).filter(Optional::isPresent).map(Optional::get));

        return Optional.of(new DimensionConfig(replace, create, providers, distance, cluster, fill, tables)).filter(c -> c.isValid(name));
    }

    public Optional<RandomCollectionProvider> parseSingleProvider(Element node) {

        RandomCollection<BlockProvider> r = new RandomCollection<>();
        parseProvider(node).forEach(p -> r.add(p, 1F));
        return Optional.of(new RandomCollectionProvider(r, null)).filter(BlockProvider::isValid);

    }

    public Vec3d parseColor(Element e) {
        String hex = e.getAttribute("hex").replace("#", "");
        double[] color = IntStream.range(0, 3)
                .mapToObj(i -> hex.substring(i * 2, (i + 1) * 2))
                .mapToInt(s -> Integer.parseInt(s, 16))
                .mapToDouble(i -> i / 255D)
                .toArray();

        return new Vec3d(color[0], color[1], color[2]);
    }

    public Optional<CreateOptions> parseCreateOptions(Element node) {

        IForgeRegistry<Biome> BIOMES = GameRegistry.findRegistry(Biome.class);

        Stream<Biome> biomes = elements(node, "biome").map(e -> {
            String mod = e.getAttribute("mod");
            String id = e.getAttribute("id");
            ResourceLocation r = new ResourceLocation(mod, id);
            return Optional.ofNullable(BIOMES.getValue(r)).filter($ -> BIOMES.containsKey(r));
        }).filter(Optional::isPresent).map(Optional::get);

        boolean enable = Boolean.parseBoolean(node.getAttribute("enable"));
        boolean respawn = Boolean.parseBoolean(node.getAttribute("respawn"));
        boolean skylight = Boolean.parseBoolean(node.getAttribute("skylight"));
        boolean hot = Boolean.parseBoolean(node.getAttribute("hot"));
        String daytime = node.getAttribute("daytime").toLowerCase();

        Vec3d fog = elements(node, "fog").findFirst().map(this::parseColor).orElse(null);
        Vec3d sky = elements(node, "sky").findFirst().map(this::parseColor).orElse(null);

        return Optional.of(new CreateOptions(biomes, respawn, skylight, hot, fog, sky, daytime, enable)).filter(CreateOptions::isValid);

    }

    private static Stream<Element> elements(Element parent) {
        NodeList list = parent.getChildNodes();
        return IntStream.range(0, list.getLength())
                .mapToObj(list::item)
                .filter(Element.class::isInstance)
                .map(n -> (Element) n);
    }

    private static Stream<Element> elements(Element parent, String name) {
        return elements(parent).filter(e -> e.getNodeName().equalsIgnoreCase(name));
    }

    public static class ErrorHandler implements org.xml.sax.ErrorHandler {

        @Override
        public void warning(SAXParseException e) {
        }

        @Override
        public void error(SAXParseException e) throws SAXException {
            throw e;
        }

        @Override
        public void fatalError(SAXParseException e) throws SAXException {
            throw e;
        }
    }
}
