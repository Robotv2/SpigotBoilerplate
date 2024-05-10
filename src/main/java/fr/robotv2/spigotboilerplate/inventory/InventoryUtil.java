package fr.robotv2.spigotboilerplate.inventory;

import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import fr.robotv2.spigotboilerplate.SpigotBoilerplate;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class InventoryUtil {

    public void openSync(Gui gui, HumanEntity entity) {
        Bukkit.getScheduler().runTask(SpigotBoilerplate.INSTANCE.getPlugin(), () -> gui.show(entity));
    }

    public void openSync(Inventory inventory, HumanEntity entity) {
        Bukkit.getScheduler().runTask(SpigotBoilerplate.INSTANCE.getPlugin(), () -> entity.openInventory(inventory));
    }

    public Map<ItemStack, Integer> totalAmount(ItemStack... stacks) {
        final Map<ItemStack, Integer> requiredItems = new HashMap<>();

        for (ItemStack item : stacks) {

            if(item == null) {
                continue;
            }

            final ItemStack key = requiredItems.keySet().stream().filter(itemStack -> itemStack.isSimilar(item)).findFirst().orElse(null);

            if (key != null) {
                requiredItems.put(key, requiredItems.get(key) + item.getAmount());
            } else {
                ItemStack itemClone = item.clone();
                itemClone.setAmount(1);  // Normalize amount to 1 for key purposes
                requiredItems.put(itemClone, item.getAmount());
            }
        }

        return requiredItems;
    }

    public boolean hasItemstack(Player player, ItemStack... stacks) {
        return totalAmount(stacks).entrySet().stream().allMatch(entry -> player.getInventory().containsAtLeast(entry.getKey(), entry.getValue()));
    }

    public Map<ItemStack, Integer> removeItemstack(Player player, ItemStack... stacks) {
        final ItemStack[] leftovers = player.getInventory().removeItem(stacks).values().toArray(new ItemStack[0]);
        return totalAmount(leftovers);
    }
}
