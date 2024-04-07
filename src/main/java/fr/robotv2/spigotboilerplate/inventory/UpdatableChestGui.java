package fr.robotv2.spigotboilerplate.inventory;

import com.github.stefvanschie.inventoryframework.adventuresupport.TextHolder;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UpdatableChestGui extends ChestGui {

    private final List<UpdatablePane> updatablePaneList = new ArrayList<>();

    public UpdatableChestGui(int rows, @NotNull String title) {
        super(rows, title);
    }

    public UpdatableChestGui(int rows, @NotNull TextHolder title) {
        super(rows, title);
    }

    public UpdatableChestGui(int rows, @NotNull String title, @NotNull Plugin plugin) {
        super(rows, title, plugin);
    }

    public UpdatableChestGui(int rows, @NotNull TextHolder title, @NotNull Plugin plugin) {
        super(rows, title, plugin);
    }

    @Override
    public void addPane(@NotNull Pane pane) {

        if(pane instanceof UpdatablePane updatablePane) {
            updatablePaneList.add(updatablePane);
        }

        super.addPane(pane);
    }

    @Override
    public void update() {
        updatablePaneList.forEach(UpdatablePane::updatePane);
        super.update();
    }
}
