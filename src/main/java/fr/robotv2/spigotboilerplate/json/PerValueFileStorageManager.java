package fr.robotv2.spigotboilerplate.json;

import fr.robotv2.spigotboilerplate.SpigotBoilerplate;
import fr.robotv2.spigotboilerplate.storage.StorageManager;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PerValueFileStorageManager<ID, T extends Identifiable<ID>> extends JsonHelper<ID, T> implements StorageManager<ID, T> {

    private final Map<ID, T> valueCache = new ConcurrentHashMap<>();
    private final Map<ID, File> fileCache = new ConcurrentHashMap<>();

    private final File folder;
    private final Class<T> tClass;

    public PerValueFileStorageManager(File folder, Class<T> tClass) {
        if (!folder.exists()) {
            folder.mkdirs();
        }

        this.folder = folder;
        this.tClass = tClass;
    }

    private File getFileFor(ID id) {
        return fileCache.computeIfAbsent(id, identification -> new File(folder, identification.toString() + ".json"));
    }

    @Override
    public Optional<T> select(ID id) {
        return Optional.ofNullable(valueCache.computeIfAbsent(id, k -> {
            try {
                return fromFile(getFileFor(id), tClass);
            } catch (IOException exception) {
                throw new RuntimeException("Failed to load data with id " + id, exception);
            }
        }));
    }

    @Override
    public void insert(T value) {
        try {
            writeToFile(getFileFor(value.getIdentification()), value);
            valueCache.put(value.getIdentification(), value);
        } catch (IOException exception) {
            throw new RuntimeException("An error occurred while inserting value with id " + value.getIdentification(), exception);
        }
    }
    @Override
    public void update(ID id, T value) {
        insert(value);
    }

    @Override
    public void remove(T value) {
        removeFromId(value.getIdentification());
    }

    @Override
    public void removeFromId(ID id) {
        final File file = getFileFor(id);

        if (file.exists() && !file.delete()) {
            SpigotBoilerplate.INSTANCE.getLogger().warning("Failed to delete file: " + file.getAbsolutePath());
        }

        valueCache.remove(id);
        fileCache.remove(id);
    }

    @Override
    public void close() {
        valueCache.clear();
        fileCache.clear();
    }
}
