package fr.robotv2.spigotboilerplate.storage;

import fr.robotv2.spigotboilerplate.json.Identifiable;

import java.util.Optional;

public interface StorageManager<ID, T extends Identifiable<ID>> {

    Optional<T> select(ID id);

    void insert(T value);

    void update(ID id, T value);

    void remove(T value);

    void removeFromId(ID id);

    void close();
}
