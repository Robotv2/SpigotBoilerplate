package fr.robotv2.spigotboilerplate.util;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Optional;

@UtilityClass
public class ItemUtil {
    public Optional<ItemMeta> meta(ItemStack stack) {
        return Optional.ofNullable(stack).map(ItemStack::getItemMeta);
    }
}
