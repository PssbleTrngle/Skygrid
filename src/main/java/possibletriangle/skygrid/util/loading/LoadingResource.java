package possibletriangle.skygrid.util.loading;

import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;
import org.w3c.dom.Element;

import java.util.List;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Collectors;

public class LoadingResource<T> {

    private final ResourceLocation name;
    private final Function<Element, Optional<T>> parser;
    private final BinaryOperator<T> merger;
    private final List<Element> elements;
    private final BiConsumer<ResourceLocation,T> consumer;

    private LoadingResource(ResourceLocation name, Function<Element, Optional<T>> parser, BinaryOperator<T> merger, BiConsumer<ResourceLocation,T> consumer, List<Element> elements) {
        String path = name.getPath();
        String mod = name.getNamespace();
        this.name = new ResourceLocation(mod, path.substring(path.lastIndexOf('/') + 1, path.length() - 4));

        this.consumer = consumer;
        this.merger = merger;
        this.parser = parser;
        this.elements = elements;
    }

    public boolean parse() {
        Optional<T> value = this.elements.stream()
                .map(parser)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .reduce(merger);

        value.ifPresent(v -> consumer.accept(name, v));

        return value.isPresent();
    }

    public static <T> LoadingResource<T> attempt(XMLLoader loader, ResourceLocation name, Function<Element, Optional<T>> parser, BinaryOperator<T> merger, BiConsumer<ResourceLocation,T> consumer, List<IResource> input) {
        List<Element> elements = input.stream()
                .map(i -> loader.parse(i.getInputStream()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return new LoadingResource<>(name, parser, merger, consumer, elements);
    }

}
