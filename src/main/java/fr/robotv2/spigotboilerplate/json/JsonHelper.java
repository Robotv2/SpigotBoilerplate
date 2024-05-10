package fr.robotv2.spigotboilerplate.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;

import java.util.logging.Logger;

public abstract class JsonHelper<ID, T extends Identifiable<ID>> {

    private static final Logger LOGGER = Logger.getLogger(JsonHelper.class.getSimpleName());
    private static final Gson GSON = new Gson();

    private void writeToWriter(File file, Consumer<BufferedWriter> writeOperation) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writeOperation.accept(writer);
        }
    }

    public void writeToFile(File file, T value) throws IOException {
        writeToWriter(file, writer -> GSON.toJson(value, writer));
    }

    public void writeToFile(File file, Collection<T> values) throws IOException {
        final JsonArray array = new JsonArray();
        values.forEach(value -> array.add(GSON.toJsonTree(value)));

        writeToWriter(file, writer -> GSON.toJson(array, writer));
    }

    public void writeToFile(File file, Map<ID, T> map) throws IOException {
        final JsonObject jsonObject = new JsonObject();
        map.forEach((key, value) -> jsonObject.add(key.toString(), GSON.toJsonTree(value)));

        writeToWriter(file, writer -> GSON.toJson(jsonObject, writer));
    }

    public T fromFile(File file, Class<T> tClass) throws IOException {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            return GSON.fromJson(fileReader, tClass);
        }
    }

    public Collection<T> collectionFromFile(File file, Class<T> tClass) throws IOException {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            JsonArray array = JsonParser.parseReader(fileReader).getAsJsonArray();
            List<T> list = new ArrayList<>();
            array.forEach(element -> list.add(GSON.fromJson(element, tClass)));
            return list;
        }
    }

    public Map<ID, T> mapFromFile(File file, Class<T> tClass) throws IOException {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            JsonObject jsonObject = JsonParser.parseReader(fileReader).getAsJsonObject();
            Map<ID, T> map = new HashMap<>();
            jsonObject.entrySet().forEach(entry -> {
                try {
                    T value = GSON.fromJson(entry.getValue(), tClass);
                    map.put(value.getIdentification(), value);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error processing JSON with ID: " + entry.getKey(), e);
                }
            });
            return map;
        }
    }
}
