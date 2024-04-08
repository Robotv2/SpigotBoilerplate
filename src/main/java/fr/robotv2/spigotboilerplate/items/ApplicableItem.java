package fr.robotv2.spigotboilerplate.items;

import fr.robotv2.spigotboilerplate.placeholders.InternalPlaceholder;
import fr.robotv2.spigotboilerplate.placeholders.RelationalValuePlaceholder;
import fr.robotv2.spigotboilerplate.placeholders.TriRelationalValuePlaceholder;
import fr.robotv2.spigotboilerplate.placeholders.ValuePlaceholder;
import fr.robotv2.spigotboilerplate.util.ColorUtil;
import fr.robotv2.spigotboilerplate.util.ItemUtil;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
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

    private ApplicableItem apply(Function<String, String> function) {

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

    public ApplicableItem apply(String from, String to) {
        return apply(new InternalPlaceholder(from, to));
    }

    public ApplicableItem apply(InternalPlaceholder placeholder) {
        return apply(placeholder::apply);
    }

    public <T> ApplicableItem apply(ValuePlaceholder<T> placeholder, T value) {
        return apply(s -> placeholder.apply(s, value));
    }

    public <A, B> ApplicableItem apply(RelationalValuePlaceholder<A, B> placeholder, A fst, B snd) {
        return apply(s -> placeholder.apply(s, fst, snd));
    }

    public <A, B, C> ApplicableItem apply(TriRelationalValuePlaceholder<A, B, C> placeholder, A fst, B snd, C thd) {
        return apply(s -> placeholder.apply(fst, snd, thd));
    }

    public ApplicableItem applyIf(Predicate<String> predicate, String from, String to) {
        return applyIf(predicate, new InternalPlaceholder(from, to));
    }

    public ApplicableItem applyIf(Predicate<String> predicate, InternalPlaceholder placeholder) {
        return apply(s -> predicate.test(s) ? placeholder.apply(s) : s);
    }

    public <T> ApplicableItem applyIf(ValuePlaceholder<T> placeholder, Class<T> tClass, Object value) {

        if(tClass.isAssignableFrom(value.getClass())) {
            apply(placeholder, (T) value);
        }

        return this;
    }

    public ApplicableItem color() {
        return apply(ColorUtil::color);
    }

    public ItemStack getItemStack() {
        return itemStack.clone();
    }
}
