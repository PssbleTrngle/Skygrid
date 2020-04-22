package possibletriangle.skygrid.data.loading;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.tags.NetworkTagManager;
import net.minecraft.tags.Tag;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import possibletriangle.skygrid.RandomCollection;
import possibletriangle.skygrid.Skygrid;
import possibletriangle.skygrid.generator.custom.CreateOptions;
import possibletriangle.skygrid.provider.*;
import possibletriangle.skygrid.provider.property.CycleProperty;
import possibletriangle.skygrid.provider.property.PropertyProvider;
import possibletriangle.skygrid.provider.property.SetProperty;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class XMLLoader {

    private final NetworkTagManager tags;

    public XMLLoader(NetworkTagManager tags) {
        this.tags = tags;
    }

    private static final Logger LOGGER = LogManager.getLogger();

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

    /**
     * @param e The XML Element
     * @return The block provider without attaches or properties
     */
    private Stream<BlockProvider> parseRawProvider(Element e) {
        final String mod = e.getAttribute("mod");
        final String id = e.getAttribute("id");
        final Stream<Pair<Float, BlockProvider>> children = findProviders(e);

        switch (e.getNodeName().toLowerCase()) {

            case "block":
            case "fill":
                return Stream.of(new SingleBlock(new ResourceLocation(mod, id)));

            case "tag":
                List<Tag<Block>> except = elements(e, "except")
                        .map(this::findTag)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());

                Predicate<Block> include = b -> except.stream().noneMatch(t -> t.contains(b));

                return findTag(e)
                        .map(Tag::getAllElements)
                        .map(Collection::stream)
                        .map(s -> s
                                .filter(include)
                                .map(SingleBlock::new)
                                .map(p -> (BlockProvider) p))
                        .orElseGet(Stream::of);

            case "collection":
                return Stream.of(new RandomCollectionProvider(RandomCollection.from(children)));

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
            RandomCollectionProvider provider = new RandomCollectionProvider(RandomCollection.from(findProviders(e)));

            switch (e.getNodeName().toLowerCase()) {

                case "offset":
                    int x = Integer.parseInt(e.getAttribute("x"));
                    int y = Integer.parseInt(e.getAttribute("y"));
                    int z = Integer.parseInt(e.getAttribute("z"));
                    return Optional.of(new OffsetBlock(provider, new BlockPos(x, y, z), probability, shared));

                case "side":
                    Direction side = Direction.byName(e.getAttribute("on"));
                    int by = Integer.parseInt(e.getAttribute("by"));
                    return Optional.ofNullable(side).map(s -> new OffsetBlock(provider, new BlockPos(0, 0, 0).offset(s, by), probability, shared));

                default:
                    return Optional.<OffsetBlock>empty();

            }
        }).filter(Optional::isPresent).map(Optional::get);
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
                .peek(p -> p.setProperties(findProperties(e)))
                .peek(p -> p.setOffets(findOffsets(e)));
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
            factory.setSchema(getSchema());
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

    private <T> LazyOptional<T> parse(InputStream input, Function<Element, T> parser) {
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setSchema(getSchema());
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new ErrorHandler());

            Document xml = builder.parse(input);

            Element node = xml.getDocumentElement();
            node.normalize();

            return LazyOptional.of(() -> parser.apply(node));

        } catch (SAXException | IOException | ParserConfigurationException ex) {
            LOGGER.error("Palette XML Error: {}", ex.getMessage());
            return LazyOptional.empty();
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

    public Optional<DimensionConfig> parseConfig(Element node) {

        boolean replace = Boolean.parseBoolean(node.getAttribute("replace"));
        boolean create = Boolean.parseBoolean(node.getAttribute("create"));

        BlockPos distance = findPos(node, "distance");
        BlockPos cluster = findPos(node, "cluster");

        RandomCollection<BlockProvider> providers = RandomCollection.from(findProviders(node));
        BlockProvider fill = elements(node, "fill").findFirst()
                .map(this::parseSingleProvider)
                .flatMap(Function.identity())
                .orElse(null);

        return Optional.of(new DimensionConfig(replace, create, providers, distance, cluster, fill)).filter(DimensionConfig::isValid);
    }

    public Optional<RandomCollectionProvider> parseSingleProvider(Element node) {

        RandomCollection<BlockProvider> r = new RandomCollection<>();
        parseProvider(node).forEach(p -> r.add(p, 1F));
        return Optional.of(new RandomCollectionProvider(r)).filter(BlockProvider::isValid);

    }

    public Optional<CreateOptions> parseCreateOptions(Element node) {

        IForgeRegistry<Biome> BIOMES = GameRegistry.findRegistry(Biome.class);

        Stream<Biome> biomes = elements(node, "biome").map(e -> {
            String mod = e.getAttribute("mod");
            String id = e.getAttribute("id");
            ResourceLocation r = new ResourceLocation(mod, id);
            return Optional.ofNullable(BIOMES.getValue(r)).filter($ -> BIOMES.containsKey(r));
        }).filter(Optional::isPresent).map(Optional::get);

        return Optional.of(new CreateOptions(biomes)).filter(CreateOptions::isValid);

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

    private static Schema getSchema() throws SAXException {
        File file = new File("C:\\Users\\firef\\Coding\\Java\\MC\\1.15\\Skygrid\\src\\main\\resources\\data\\schema.xsd");
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        return factory.newSchema(file);
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
