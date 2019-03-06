package com.possibletriangle.skygrid.random;

import java.util.*;

public class RandomCollection<E> {
    private final NavigableMap<Double, Object> map = new TreeMap<>();
    private double total = 0;

    public static <E> RandomCollection<E> merge(RandomCollection<E>... eeees) {

        RandomCollection<E> merged = new RandomCollection<>();

        int max_size = 0;
        for(RandomCollection<E> eeee : eeees)
            max_size = Math.max(max_size, eeee.size());

        for(RandomCollection<E> eeee : eeees) {
            int size = eeee.size();
            for(Map.Entry<Double, Object> entry : eeee.map.entrySet()) {
                double weight = (double) max_size / size * entry.getKey();
                Object value = entry.getValue();
                if(value instanceof RandomCollection)
                    merged.add(weight, (RandomCollection<E>) value);
                if(!merged.map.containsValue(value))
                    merged.add(weight, (E) value);

            }
        }

        return merged;

    }

    public RandomCollection<E> add(double weight, E result) {
        if (weight <= 0) return this;
        total += weight;
        map.put(total, result);
        return this;
    }

    public RandomCollection<E> add(double weight, RandomCollection<E> sub) {
        if (weight <= 0 || sub.total == 0) return this;
        total += weight;
        map.put(total, sub);
        return this;
    }

    public E next(Random random) {
        if(total == 0)
            return null;

        double value = random.nextDouble() * total;
        Object o = map.higherEntry(value).getValue();

        if(o instanceof RandomCollection) {
            return ((RandomCollection<E>) o).next(random);
        } else
            return (E) o;
    }

    public void clear() {
        map.clear();
        total = 0;
    }

    public int size() {
        return map.size();
    }

}
