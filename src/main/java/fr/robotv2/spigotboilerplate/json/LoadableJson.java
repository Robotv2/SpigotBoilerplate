package fr.robotv2.spigotboilerplate.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.robotv2.spigotboilerplate.SpigotBoilerplate;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@Deprecated
public abstract class LoadableJson<ID, T extends Identifiable<ID>> {

    private final File file;
    private final Class<T> tClass;

    public LoadableJson(Plugin plugin, String fileName, Class<T> tClass) {
        this(new File(plugin.getDataFolder(), fileName), tClass);
    }

    public LoadableJson(File file, Class<T> tClass) {
        this.file = file;
        this.tClass = tClass;
    }

    public static <ID, T extends Identifiable<ID>> void save(File file, Map<ID, T> map, Class<T> tClass) throws IOException {

        final Gson gson = new Gson();
        final JsonObject jsonObject = new JsonObject();

        for(Map.Entry<ID, T> entry : map.entrySet()) {
            final JsonElement jsonElement = gson.toJsonTree(entry.getValue(), tClass);
            jsonObject.add(entry.getKey().toString(), jsonElement);
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            gson.toJson(jsonObject, writer);
        }
    }

    public void save() throws IOException {
        LoadableJson.save(file, getMutableMap(), tClass);
    }

    public static <ID, T extends Identifiable<ID>> Map<ID, T> load(File file, Class<T> tClass) throws IOException {

        if(!file.exists()) {
            return Collections.emptyMap();
        }

        final Map<ID, T> map = new HashMap<>();
        final Gson gson = new Gson();
        final BufferedReader fileReader = new BufferedReader(new FileReader(file));

        final JsonObject json = JsonParser.parseReader(fileReader).getAsJsonObject();

        for(Map.Entry<String, JsonElement> entry : json.entrySet()) {
            try {
                final T value = gson.fromJson(entry.getValue(), tClass);
                map.put(value.getIdentification(), value);
            } catch (Exception exception) {
                SpigotBoilerplate.INSTANCE.getLogger().log(Level.SEVERE, "An error occurred while registering json with id: " + entry.getKey(), exception);
            }
        }

        fileReader.close();
        return map;
    }

    public void load() throws IOException {
        getMutableMap().clear();
        getMutableMap().putAll(load(file, tClass));
    }

    public void saveSilently() {
        try {
            save();
        } catch (IOException ignored) {
        }
    }

    public void loadSilently() {
        try {
            load();
        } catch (IOException ignored) {
        }
    }

    public abstract Map<ID, T> getMutableMap();
}
