package fr.robotv2.spigotboilerplate.inventory;

public interface UpdatablePane {

    void populatePane();

    void clearPane();

    default void updatePane() {
        clearPane();
        populatePane();
    }
}
