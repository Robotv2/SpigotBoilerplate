package fr.robotv2.spigotboilerplate.sections;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Manager<T> {

    private final Class<T> tClass;
    private final Map<String, T> map = new HashMap<>();

    public Manager(Class<T> clazz) {
        this.tClass = clazz;
    }

    public void registers(ConfigurationSection section) {

        map.clear();

        if(section == null) {
            return;
        }

        section.getKeys(false).forEach(key -> {
            final T value = SectionLoader.loadFromSection(section, key, tClass);
            if(value == null) return;
            register(key.toUpperCase(Locale.ROOT), value);
        });
    }

    public void register(String id, T value) {
        map.put(id.toUpperCase(Locale.ROOT), value);
    }

    @UnmodifiableView
    public Collection<T> getValues() {
        return Collections.unmodifiableCollection(map.values());
    }

    public T getValue(String id) {
        return map.get(id.toUpperCase(Locale.ROOT));
    }

    public <U> Collection<U> map(Function<T, U> mapper) {
        return getValues().stream().map(mapper).collect(Collectors.toList());
    }

    public Collection<T> filter(Predicate<T> predicate) {
        return getValues().stream().filter(predicate).collect(Collectors.toList());
    }

    public T random() {
        final Collection<T> values = getValues();
        if(getValues().isEmpty()) return null;
        return new ArrayList<>(values).get(ThreadLocalRandom.current().nextInt(values.size()));
    }
}

