package fr.robotv2.spigotboilerplate.inventory;

import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import fr.robotv2.spigotboilerplate.SpigotBoilerplate;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

@UtilityClass
public class InventoryUtil {

    public void openSync(Gui gui, HumanEntity entity) {
        Bukkit.getScheduler().runTask(SpigotBoilerplate.INSTANCE.getPlugin(), () -> gui.show(entity));
    }

    public void openSync(Inventory inventory, HumanEntity entity) {
        Bukkit.getScheduler().runTask(SpigotBoilerplate.INSTANCE.getPlugin(), () -> entity.openInventory(inventory));
    }
}
