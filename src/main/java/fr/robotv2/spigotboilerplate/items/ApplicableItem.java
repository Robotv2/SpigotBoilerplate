package fr.robotv2.spigotboilerplate.items;

import fr.robotv2.spigotboilerplate.placeholders.InternalPlaceholder;
import fr.robotv2.spigotboilerplate.placeholders.ValuePlaceholder;
import fr.robotv2.spigotboilerplate.util.ItemUtil;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ApplicableItem {

    private ItemStack itemStack;

    public ApplicableItem material(Material material) {
        itemStack.setType(material);
        return this;
    }

    public ApplicableItem name(String name) {
        ItemUtil.meta(itemStack).ifPresent(itemMeta -> itemMeta.setDisplayName(name));
        return this;
    }

    public ApplicableItem lore(List<String> lore) {
        ItemUtil.meta(itemStack).ifPresent(itemMeta -> itemMeta.setLore(lore));
        return this;
    }

    public ApplicableItem apply(String from, String to) {
        return apply(new InternalPlaceholder(from, to));
    }

    public ApplicableItem apply(InternalPlaceholder placeholder) {

        if(itemStack == null || itemStack.getType().isAir()) {
            return this;
        }

        final ItemStack clone = itemStack.clone();
        final ItemMeta cloneMeta = Objects.requireNonNull(clone.getItemMeta());

        if (cloneMeta.hasDisplayName()) {
            cloneMeta.setDisplayName(placeholder.apply(cloneMeta.getDisplayName()));
        }

        if (cloneMeta.hasLore() && cloneMeta.getLore() != null) {
            cloneMeta.setLore(cloneMeta.getLore().stream().map(placeholder::apply).collect(Collectors.toList()));
        }

        clone.setItemMeta(cloneMeta);
        this.itemStack = clone;
        return this;
    }

    public <T> ApplicableItem apply(ValuePlaceholder<T> placeholder, T value) {

        if(itemStack == null || itemStack.getType().isAir()) {
            return this;
        }

        final ItemStack clone = itemStack.clone();
        final ItemMeta cloneMeta = Objects.requireNonNull(clone.getItemMeta());

        if (cloneMeta.hasDisplayName()) {
            cloneMeta.setDisplayName(placeholder.apply(cloneMeta.getDisplayName(), value));
        }

        if (cloneMeta.hasLore() && cloneMeta.getLore() != null) {
            cloneMeta.setLore(cloneMeta.getLore().stream().map(line -> placeholder.apply(line, value)).collect(Collectors.toList()));
        }

        clone.setItemMeta(cloneMeta);
        this.itemStack = clone;
        return this;
    }

    public <T> ApplicableItem applyIf(ValuePlaceholder<T> placeholder, Class<T> tClass, Object value) {

        if(tClass.isAssignableFrom(value.getClass())) {
            apply(placeholder, (T) value);
        }

        return this;
    }

    public ApplicableItem color() {
        return apply("&", String.valueOf(ChatColor.COLOR_CHAR));
    }
}
