package com.possibletriangle.skygrid.random;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.possibletriangle.skygrid.IJsonAble;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class RandomCollectionRL extends RandomCollection<ResourceLocation> implements IJsonAble {

    @Override
    public boolean isValid() {
        return size() > 0;
    }

    @Override
    public void validate() {}

    @Override
    public String key() {
        return "values";
    }

    @Override
    public void fromJSON(JsonElement json) {
        clear();
        for(JsonElement e : json.getAsJsonArray()) {
            double weight = e.getAsJsonObject().get("weight").getAsDouble();

            if(e.getAsJsonObject().has("value"))
                add(weight, new ResourceLocation(e.getAsJsonObject().get("value").getAsString()));
            else if(e.getAsJsonObject().has("values")) {
                RandomCollectionRL sub = new RandomCollectionRL();
                sub.fromJSON(e.getAsJsonObject().get("values"));
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

            if (entry.getValue() instanceof RandomCollectionRL) {

                o.add("values", ((RandomCollectionRL) entry.getValue()).toJSON());

            } else {

                if(entry.getValue() instanceof BlockInfo) {
                    o.add("value", ((BlockInfo) entry.getValue()).toJSON());
                } else
                    o.addProperty("value", entry.getValue().toString());

            }

            array.add(o);
        }

        return array;
    }

}
