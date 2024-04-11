package fr.robotv2.spigotboilerplate.items;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import fr.robotv2.spigotboilerplate.sections.SectionLoader;
import fr.robotv2.spigotboilerplate.util.CommandList;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
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
                            section,
                            tValue.name().toLowerCase(Locale.ROOT),
                            ItemSection.class
                    )
            );
        }
    }

    public <T extends Enum<T>> ItemSection itemSection(T enumValue) {
        return items.get(enumValue.name());
    }

    public <T extends Enum<T>> ItemStack item(T enumValue) {
        return new ApplicableItem(items.get(enumValue.name()).toItemStack())
                .color()
                .getItemStack();
    }

    public <T extends Enum<T>> ApplicableItem applicableItem(T enumValue) {
        return new ApplicableItem(item(enumValue));
    }

    public <T extends Enum<T>> GuiItem guiItem(T enumValue) {
        final ItemSection itemSection = itemSection(enumValue);
        final ItemStack itemStack = new ApplicableItem(itemSection.toItemStack()).color().getItemStack();
        return itemSection.getOnClick().isEmpty()
                ? new GuiItem(itemStack)
                : new GuiItem(itemStack, event -> {
                    new CommandList(itemSection.getOnClick()).execute((Player) event.getWhoClicked());
                });
    }

    public <T extends Enum<T>> GuiItem guiItem(T enumValue, Consumer<InventoryClickEvent> eventConsumer) {
        final ItemSection itemSection = itemSection(enumValue);
        final ItemStack itemStack = new ApplicableItem(itemSection.toItemStack()).color().getItemStack();
        return itemSection.getOnClick().isEmpty()
                ? new GuiItem(itemStack, eventConsumer)
                : new GuiItem(itemStack, event -> {
            eventConsumer.accept(event);
            new CommandList(itemSection.getOnClick()).execute((Player) event.getWhoClicked());
        });
    }
}
