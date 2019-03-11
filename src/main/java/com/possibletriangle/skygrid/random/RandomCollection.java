package com.possibletriangle.skygrid.random;

import java.util.*;

public class RandomCollection<E> implements Iterable<E> {
    protected final NavigableMap<Double, Object> map = new TreeMap<>();
    private double total = 0;

    @Override
    public Iterator<E> iterator() {
        ArrayList<E> list = new ArrayList<>();
        for(Object o : map.values())
            if(o instanceof RandomCollection)
                for(E e : ((RandomCollection<E>) o))
                    list.add(e);
            else
                list.add((E) o);

        return list.iterator();
    }

    RandomCollection() {}

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
