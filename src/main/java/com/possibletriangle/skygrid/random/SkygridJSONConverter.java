package com.possibletriangle.skygrid.random;

import com.google.gson.*;
import com.possibletriangle.skygrid.ConfigSkygrid;
import com.possibletriangle.skygrid.Skygrid;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import java.io.*;
import java.util.Map;

public class SkygridJSONConverter {

    private static final String DIR = "/dimensions/";

    public static String[] getConfigs() {

        File[] files = new File(ConfigSkygrid.DIR + DIR).listFiles();
        if(files == null) return new String[0];

        String[] names = new String[files.length];
        for(int i = 0; i < files.length; i++)
            names[i] = files[i].getName().replaceAll(".json", "");

        return names;

    }

    public static boolean existsConfig(ResourceLocation name) {
        return new File(ConfigSkygrid.DIR + DIR + name.getResourcePath() + ".json").exists();
    }

    private static void readFromJson(SkygridOptions options, JsonObject json) {

        options.onlyOverride = !json.has("only_override") || json.get("only_override").getAsBoolean();
        options.HEIGHT = json.get("height").getAsInt();
        JsonArray offset = json.getAsJsonArray("offset");
        options.OFFSET = new BlockPos(offset.get(0).getAsInt(), offset.get(1).getAsInt(), offset.get(2).getAsInt());

        for(JsonElement e : json.getAsJsonArray("mobs")) {
            options.MOBS.add(e.getAsJsonObject().get("weight").getAsDouble(), new ResourceLocation(e.getAsJsonObject().get("value").getAsString()));
        }

        for(JsonElement e : json.getAsJsonArray("loot")) {
            options.LOOT.add(e.getAsJsonObject().get("weight").getAsDouble(), new ResourceLocation(e.getAsJsonObject().get("value").getAsString()));
        }

        for(Map.Entry<String, JsonElement> entry : json.get("fill_blocks").getAsJsonObject().entrySet()) {
            int i = Integer.parseInt(entry.getKey().replace("at[", "").replace("]", ""));
            ResourceLocation r = new ResourceLocation(entry.getValue().getAsJsonObject().get("block").getAsString());
            int meta = entry.getValue().getAsJsonObject().get("meta").getAsInt();
            IBlockState b = Block.REGISTRY.containsKey(r) ? Block.REGISTRY.getObject(r).getStateFromMeta(meta) : Blocks.AIR.getDefaultState();
            options.FILL_BLOCK.put(i, b);
        }

        for(Map.Entry<String, JsonElement> entry : json.get("blocks").getAsJsonObject().entrySet()) {
            int i = Integer.parseInt(entry.getKey().replace("at[", "").replace("]", ""));
            RandomCollectionJson<BlockInfo> r = new RandomCollectionJson<>(BlockInfo.class);
            r.fromJSON(entry.getValue());
            options.BLOCKS.put(i, r);
        }

    }

    public static void readFromConfig(SkygridOptions options, ResourceLocation name) {

        File file = new File(ConfigSkygrid.DIR + DIR + name.getResourcePath() + ".json");
        if(!file.exists())
            return;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String s = "", line = null;
            while((line = reader.readLine()) != null)
                s += line;

            reader.close();

            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(s).getAsJsonObject();
            readFromJson(options, json);

            Skygrid.LOGGER.info("Read config for \"{}\"", name.getResourcePath());

        } catch (IOException e) {
            Skygrid.LOGGER.error("Error reading config for \"{}\"", name.getResourcePath());
        }
    }

    public static void createDefaultFile(SkygridOptions options, ResourceLocation name) {

        File file = new File(ConfigSkygrid.DIR + DIR + name.getResourcePath() + ".json");
        JsonObject json = toJSON(options);

        try {
            file.getParentFile().mkdirs();
            file.createNewFile();

            FileWriter writer = new FileWriter(file);
            Gson gs = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            writer.write(gs.toJson(json));
            writer.close();

            Skygrid.LOGGER.info("Created default config for \"{}\"",name.getResourcePath());

        } catch (IOException e) {
            Skygrid.LOGGER.error("Error creating default config for \"{}\"", name.getResourcePath());
        }


    }

    public static JsonObject toJSON(SkygridOptions options) {

        JsonObject json = new JsonObject();

        json.addProperty("height", options.HEIGHT);
        json.addProperty("only_override", options.onlyOverride);

        JsonArray offset = new JsonArray();
        offset.add(options.OFFSET.getX());
        offset.add(options.OFFSET.getY());
        offset.add(options.OFFSET.getZ());
        json.add("offset", offset);

        json.add("mobs", options.MOBS.toJSON());
        json.add("loot", options.LOOT.toJSON());

        JsonObject fill_blocks = new JsonObject();
        for(int floor : options.FILL_BLOCK.keySet()) {
            JsonObject o = new JsonObject();
            o.addProperty("block", options.FILL_BLOCK.get(floor).getBlock().getRegistryName().toString());
            o.addProperty("meta", options.FILL_BLOCK.get(floor).getBlock().getMetaFromState(options.FILL_BLOCK.get(floor)));
            fill_blocks.add("at[" + floor + "]", o);
        }
        json.add("fill_blocks", fill_blocks);

        JsonObject blocks = new JsonObject();
        for(int floor : options.BLOCKS.keySet()) {
            blocks.add("at[" + floor + "]", options.BLOCKS.get(floor).toJSON());
        }
        json.add("blocks", blocks);

        return json;

    }

}
