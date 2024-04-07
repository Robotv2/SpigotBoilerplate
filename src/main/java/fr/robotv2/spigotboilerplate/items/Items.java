package fr.robotv2.spigotboilerplate.items;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import fr.robotv2.spigotboilerplate.sections.SectionLoader;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

@UtilityClass
public class Items {

    private final Map<String, ItemSection> items = new HashMap<>();

    public <T extends Enum<T>> void put(Class<T> tClass, ConfigurationSection section) {
        for(T tValue : tClass.getEnumConstants()) {
            items.put(
                    tValue.name(),
                    SectionLoader.loadFromSection(
                            section, tValue.name().toLowerCase(Locale.ROOT),
                            ItemSection.class
                    )
            );
        }
    }

    public <T extends Enum<T>> ItemSection itemSection(T enumValue) {
        return items.get(enumValue.name());
    }

    public <T extends Enum<T>> ItemStack item(T enumValue) {
        return items.get(enumValue.name()).getItemStack();
    }

    public <T extends Enum<T>> GuiItem guiItem(T enumValue) {
        return new GuiItem(item(enumValue));
    }

    public <T extends Enum<T>> GuiItem guiItem(T enumValue, Consumer<InventoryClickEvent> eventConsumer) {
        return new GuiItem(item(enumValue), eventConsumer);
    }
}
