package fr.robotv2.spigotboilerplate.sections;

import fr.robotv2.spigotboilerplate.SpigotBoilerplate;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;

@UtilityClass
public class SectionLoader {

    public <T> List<T> loadFromSection(ConfigurationSection section, Class<T> tClass) {
        Objects.requireNonNull(section, "ConfigurationSection cannot be null");

        Constructor<T> constructor = getConstructor(tClass).orElseThrow(() ->
                new IllegalStateException(tClass.getSimpleName() + " does not have a required constructor with ConfigurationSection"));

        List<T> resultList = new ArrayList<>();

        section.getKeys(false).forEach(key -> {
            Optional.ofNullable(section.getConfigurationSection(key))
                    .flatMap(keySection -> createInstance(constructor, keySection))
                    .ifPresent(resultList::add);
        });

        return resultList;
    }

    public <T> T loadFromSection(ConfigurationSection section, String path, Class<T> tClass) {
        Objects.requireNonNull(section, "ConfigurationSection cannot be null");

        Constructor<T> constructor = getConstructor(tClass).orElseThrow(() ->
                new IllegalStateException(tClass.getSimpleName() + " does not have a required constructor with ConfigurationSection"));

        return Optional.ofNullable(section.getConfigurationSection(path))
                .flatMap(keySection -> createInstance(constructor, keySection))
                .orElse(null);
    }

    private <T> Optional<Constructor<T>> getConstructor(Class<T> tClass) {
        try {
            final Constructor<T> constructor = tClass.getDeclaredConstructor(ConfigurationSection.class);
            return Optional.of(constructor);
        } catch (NoSuchMethodException e) {
            SpigotBoilerplate.INSTANCE.getLogger().warning(tClass.getSimpleName() + " doesn't seem to have a Constructor with Configuration Section.");
            return Optional.empty();
        }
    }

    private <T> Optional<T> createInstance(Constructor<T> constructor, ConfigurationSection section) {
        try {
            return Optional.of(constructor.newInstance(section));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            SpigotBoilerplate.INSTANCE.getLogger().log(Level.WARNING, "Could not instantiate object of type " + constructor.getDeclaringClass().getSimpleName(), exception);
            return Optional.empty();
        }
    }
}

