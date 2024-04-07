package fr.robotv2.spigotboilerplate.inventory.pane;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * An extension of StaticPane that supports dynamic updates at a specified interval.
 * It allows for items within the pane to be updated or modified over time,
 * making it suitable for creating interactive and dynamic GUIs in Minecraft.
 */
public class UpdatedPane extends StaticPane {

    /**
     * A map to hold items and their corresponding slots that should be updated.
     */
    private final Map<Slot, Supplier<GuiItem>> updatedItems = new HashMap<>();

    /**
     * A BukkitTask representing the scheduled task for updating the pane, if any.
     */
    private BukkitTask task = null;

    /**
     * An optional Runnable to be executed on each update cycle.
     */
    private Runnable onUpdate = null;

    /**
     * The update interval in server ticks (20 ticks = 1 second).
     */
    private long timer = 20;

    /**
     * Constructs an UpdatedPane with specific slot, dimensions, and priority.
     *
     * @param slot The slot position of the pane.
     * @param length The length of the pane.
     * @param height The height of the pane.
     * @param priority The rendering priority of the pane.
     */
    public UpdatedPane(Slot slot, int length, int height, @NotNull Priority priority) {
        super(slot, length, height, priority);
    }

    /**
     * Constructs an UpdatedPane with specific coordinates, dimensions, and priority.
     *
     * @param x The x coordinate of the pane.
     * @param y The y coordinate of the pane.
     * @param length The length of the pane.
     * @param height The height of the pane.
     * @param priority The rendering priority of the pane.
     */
    public UpdatedPane(int x, int y, int length, int height, @NotNull Priority priority) {
        super(x, y, length, height, priority);
    }

    /**
     * Constructs an UpdatedPane with a specific slot and dimensions.
     *
     * @param slot The slot position of the pane.
     * @param length The length of the pane.
     * @param height The height of the pane.
     */
    public UpdatedPane(Slot slot, int length, int height) {
        super(slot, length, height);
    }

    /**
     * Constructs an UpdatedPane with specific coordinates and dimensions.
     *
     * @param x The x coordinate of the pane.
     * @param y The y coordinate of the pane.
     * @param length The length of the pane.
     * @param height The height of the pane.
     */
    public UpdatedPane(int x, int y, int length, int height) {
        super(x, y, length, height);
    }

    /**
     * Constructs an UpdatedPane with specific dimensions.
     *
     * @param length The length of the pane.
     * @param height The height of the pane.
     */
    public UpdatedPane(int length, int height) {
        super(length, height);
    }

    /**
     * Sets the timer for updates in server ticks.
     *
     * @param timer The timer in server ticks.
     */
    public void setTimer(long timer) {
        this.timer = timer;
    }

    /**
     * Sets a Runnable that is called on every update.
     *
     * @param onUpdate The Runnable to execute.
     */
    public void setOnUpdate(Runnable onUpdate) {
        this.onUpdate = onUpdate;
    }

    /**
     * Executes the onUpdate Runnable if it is not null.
     */
    public void callOnUpdate() {
        if(this.onUpdate != null) {
            onUpdate.run();
        }
    }

    /**
     * Adds an item to be updated in the pane.
     *
     * @param supplier The supplier of the GuiItem.
     * @param slot The slot where the item should be placed.
     */
    public void addUpdatedItem(Supplier<GuiItem> supplier, Slot slot) {
        this.updatedItems.put(slot, supplier);
    }

    /**
     * Adds an item to be updated in the pane.
     *
     * @param supplier The supplier of the GuiItem.
     * @param x the x coordinate of the item
     * @param y the y coordinate of the item
     */
    public void addUpdatedItem(Supplier<GuiItem> supplier, int x, int y) {
        addUpdatedItem(supplier, Slot.fromXY(x, y));
    }

    /**
     * Starts the updating process using the class's plugin.
     */
    public void startUpdating() {
        startUpdating(JavaPlugin.getProvidingPlugin(UpdatedPane.class));
    }

    /**
     * Starts or restarts the updating process, given a plugin.
     *
     * @param plugin The plugin responsible for the task scheduling.
     */
    public void startUpdating(JavaPlugin plugin) {
        stopUpdating(); // Ensure no previous tasks are running
        this.task = Bukkit.getScheduler().runTaskTimer(plugin, this::update, 0, timer);
    }

    /**
     * Stops the updating process if it is currently running.
     */
    public void stopUpdating() {
        if(this.task != null) {
            this.task.cancel();
            this.task = null;
        }
    }

    /**
     * Updates the pane by refreshing the items
     */
    private void update() {

        for(Map.Entry<Slot, Supplier<GuiItem>> entry : updatedItems.entrySet()) {
            addItem(entry.getValue().get(), entry.getKey()); // Updates or adds the item to the pane
        }

        if(onUpdate != null) {
            onUpdate.run();
        }
    }
}
