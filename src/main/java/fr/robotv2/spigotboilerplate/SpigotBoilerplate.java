package fr.robotv2.spigotboilerplate;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

@Getter
public enum SpigotBoilerplate {

    INSTANCE,
    ;

    private boolean loaded;
    private Logger logger;

    public void load(JavaPlugin plugin) {

        if(isLoaded()) {
            return;
        }

        loaded = true;
        logger = plugin.getLogger();
    }
}
