package fr.robotv2.spigotboilerplate.items;

import fr.robotv2.spigotboilerplate.placeholders.PlaceholderSupport;
import fr.robotv2.spigotboilerplate.util.ItemUtil;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ApplicableItem extends PlaceholderSupport<ApplicableItem> {

    private ItemStack itemStack;

    public ApplicableItem material(Material material) {
        itemStack.setType(material);
        return this;
    }

    public ApplicableItem name(String name) {
        ItemUtil.meta(itemStack).ifPresent(itemMeta -> {
            itemMeta.setDisplayName(name);
            itemStack.setItemMeta(itemMeta);
        });
        return this;
    }

    public ApplicableItem lore(List<String> lore) {
        ItemUtil.meta(itemStack).ifPresent(itemMeta -> {
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
        });
        return this;
    }

    @Override
    public ApplicableItem apply(Function<String, String> function) {

        if(itemStack == null || itemStack.getType().isAir()) {
            return this;
        }

        final ItemStack clone = itemStack.clone();
        final ItemMeta cloneMeta = Objects.requireNonNull(clone.getItemMeta());

        if (cloneMeta.hasDisplayName()) {
            cloneMeta.setDisplayName(function.apply(cloneMeta.getDisplayName()));
        }

        if (cloneMeta.hasLore() && cloneMeta.getLore() != null) {
            cloneMeta.setLore(cloneMeta.getLore().stream().map(function).collect(Collectors.toList()));
        }

        clone.setItemMeta(cloneMeta);
        this.itemStack = clone;

        return this;
    }

    public ItemStack getItemStack() {
        return itemStack.clone();
    }
}
