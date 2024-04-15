package fr.robotv2.spigotboilerplate.items;

import lombok.Data;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

@Data
public class ItemSection {

    private ItemStack itemStack;

    private final Material material;
    private final String name;
    private final List<String> description;
    private final int customModelData;
    private final List<String> onClick;

    public ItemSection(ConfigurationSection section) {
        this.material = Material.matchMaterial(section.getString("material", "AIR"));
        this.name = section.getString("name");
        this.description = section.getStringList("description");
        this.customModelData = section.getInt("custom_model_data", Integer.MIN_VALUE);
        this.onClick = section.getStringList("on_click");
    }

    public ItemStack toItemStack() {

        if(itemStack == null) {
            buildItemStack();
        }

        return itemStack.clone();
    }

    private void buildItemStack() {

        final ItemStack itemStack = new ItemStack(material);

        if(material == Material.AIR) {
            this.itemStack = itemStack;
            return;
        }

        final ItemMeta meta = Objects.requireNonNull(itemStack.getItemMeta());

        if(name != null && !name.isEmpty()) {
            meta.setDisplayName(name);
        }

        if(!description.isEmpty()) {
            meta.setLore(description);
        }

        if(customModelData != Integer.MIN_VALUE) {
            meta.setCustomModelData(customModelData);
        }

        itemStack.setItemMeta(meta);
        this.itemStack = itemStack;
    }
}
