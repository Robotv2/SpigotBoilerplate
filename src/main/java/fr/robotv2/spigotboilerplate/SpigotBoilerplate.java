package fr.robotv2.spigotboilerplate;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

@Getter
public enum SpigotBoilerplate {

    INSTANCE,
    ;

    private JavaPlugin plugin;
    private boolean loaded;
    private Logger logger;

    public void load(JavaPlugin plugin) {

        if(isLoaded()) {
            return;
        }

        this.loaded = true;
        this.logger = plugin.getLogger();
        this.plugin = plugin;
    }
}
