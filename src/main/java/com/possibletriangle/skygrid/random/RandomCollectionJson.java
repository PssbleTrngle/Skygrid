package com.possibletriangle.skygrid.random;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.possibletriangle.skygrid.IJsonAble;

import java.util.Map;
import java.util.Random;

public class RandomCollectionJson<E extends IJsonAble> extends RandomCollection<E> implements IJsonAble {

    @Override
    public boolean isValid() {
        return size() > 0;
    }

    @Override
    public void validate() {
        for(E e : this)
            e.validate();
    }

    @Override
    public String key() {
        return "values";
    }

    private final Class<? extends E> clazz;
    private String key = "value";
    public RandomCollectionJson(Class<? extends E> clazz) {
        this.clazz = clazz;
        try {
            key = clazz.newInstance().key();
        } catch (Exception e) {}
    }

    @Override
    public E next(Random random) {
        E e;
        int i = 0;

        do {
            e = super.next(random);
        } while ( i++ < 100 && (e == null || !e.isValid()));

        return e.isValid() ? e : null;
    }

    @Override
    public void fromJSON(JsonElement json) {
        clear();
        for(JsonElement e : json.getAsJsonArray()) {
            double weight = e.getAsJsonObject().get("weight").getAsDouble();

            if(e.getAsJsonObject().has(key))
                try {
                    E value = clazz.newInstance();
                    value.fromJSON(e.getAsJsonObject().get(key));
                    add(weight, value);
                } catch (Exception ex) {
                }

            else if(e.getAsJsonObject().has(key())) {
                RandomCollectionJson<E> sub = new RandomCollectionJson<>(clazz);
                sub.fromJSON(e.getAsJsonObject().get(key()));
                add(weight, sub);
            }

        }
    }

    @Override
    public JsonElement toJSON() {

        JsonArray array = new JsonArray();

        double total = 0;
        for (Map.Entry<Double, Object> entry : map.entrySet()) {

            JsonObject o = new JsonObject();
            o.addProperty("weight", entry.getKey()-total);
            total = entry.getKey();

            if(entry.getValue() instanceof IJsonAble) {
                o.add(((IJsonAble) entry.getValue()).key(), ((IJsonAble) entry.getValue()).toJSON());
            } else
                o.addProperty(key, entry.getValue().toString());

            array.add(o);
        }

        return array;
    }

}
