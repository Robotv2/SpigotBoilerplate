package fr.robotv2.spigotboilerplate.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.robotv2.spigotboilerplate.SpigotBoilerplate;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.Map;
import java.util.logging.Level;

public abstract class LoadableJson<ID, T> {

    private final File file;
    private final Class<T> tClass;

    public LoadableJson(Plugin plugin, String fileName, Class<T> tClass) {
        this(new File(plugin.getDataFolder(), fileName), tClass);
    }

    public LoadableJson(File file, Class<T> tClass) {
        this.file = file;
        this.tClass = tClass;
    }

    public void save() throws IOException {

        final Gson gson = new Gson();
        final JsonObject jsonObject = new JsonObject();

        for(Map.Entry<ID, T> entry : getMutableMap().entrySet()) {
            final JsonElement jsonElement = gson.toJsonTree(entry.getValue(), tClass);
            jsonObject.add(entry.getKey().toString(), jsonElement);
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            gson.toJson(jsonObject, writer);
        }
    }

    public void load() throws IOException {

        getMutableMap().clear();

        if(!file.exists()) {
            return;
        }

        final Gson gson = new Gson();
        final BufferedReader fileReader = new BufferedReader(new FileReader(file));
        final JsonObject json = JsonParser.parseReader(fileReader).getAsJsonObject();

        for(Map.Entry<String, JsonElement> entry : json.entrySet()) {
            try {

                final T value = gson.fromJson(entry.getValue(), tClass);
                final ID id = extractId(value);
                getMutableMap().put(id, value);

            } catch (Exception exception) {
                SpigotBoilerplate.INSTANCE.getLogger().log(Level.SEVERE, "An error occurred while registering json with id: " + entry.getKey(), exception);
            }
        }

        fileReader.close();
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

    public abstract ID extractId(T value);

    public abstract Map<ID, T> getMutableMap();
}
