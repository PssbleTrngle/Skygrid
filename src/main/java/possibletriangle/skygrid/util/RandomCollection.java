package possibletriangle.skygrid.util;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class RandomCollection<T> {

    private final NavigableMap<Float, T> map = new TreeMap<>();
    private float total = 0;

    @SafeVarargs
    public RandomCollection(T... ts) {
        this.addAll(ts);
    }

    public RandomCollection<T> add(T t, float weight) {
        if (weight <= 0) return this;
        total += weight;
        map.put(total, t);
        return this;
    }

    public RandomCollection<T> addAll(RandomCollection<T> other) {
        other.map.forEach((weight, value) -> this.add(value, weight));
        return this;
    }

    @SafeVarargs
    public final RandomCollection<T> addAll(T... ts) {
        for(T t : ts)
            add(t, 1);
        return this;
    }

    public Optional<T> next(Random random) {
        if(this.total == 0) return Optional.empty();
        float value = random.nextFloat() * total;
        return Optional.of(map.higherEntry(value).getValue());
    }

    public int size() {
        return map.values().size();
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public Collection<T> all() {
        return map.values();
    }

    public RandomCollection<T> filter(Predicate<T> by) {
        RandomCollection<T> filtered = new RandomCollection<>();
        map.forEach((weight, value) -> {
            if(by.test(value)) filtered.add(value, weight);
        });
        return filtered;
    }

    public void clear() {
        this.map.clear();
    }

    public static <T> RandomCollection<T> from(Stream<Pair<Float, T>> pairs) {
        RandomCollection<T> collection = new RandomCollection<>();
        pairs.forEach(p -> collection.add(p.getSecond(), p.getFirst()));
        return collection;
    }

    public Stream<Pair<Float,T>> stream() {
        float last = 0;
        List<Pair<Float,T>> list = Lists.newArrayList();
        for(Map.Entry<Float,T> e : this.map.entrySet()) {
            float weight = e.getKey() - last;
            list.add(new Pair<>(weight / total, e.getValue()));
            last = e.getKey();
        }

        return list.stream();
    }

}