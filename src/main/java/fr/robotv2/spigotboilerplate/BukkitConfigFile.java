package fr.robotv2.spigotboilerplate;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class BukkitConfigFile {

    private final String filePath;
    private final JavaPlugin plugin;

    private File file;
    private YamlConfiguration configuration;

    public BukkitConfigFile(String filePath, JavaPlugin plugin) {
        this.filePath = filePath;
        this.plugin = plugin;
    }

    public BukkitConfigFile(File folder, String fileName, JavaPlugin plugin) {
        if (!folder.exists()) folder.mkdirs();
        this.filePath = new File(folder, fileName).getPath();
        this.plugin = plugin;
    }

    public void setup() {

        if(file == null) {
            this.file = new File(plugin.getDataFolder(), filePath);
        }

        if(!file.exists()) {
            this.plugin.saveResource(filePath, false);
        }
    }

    public void save() throws IOException {

        if(file == null || configuration == null) {
            return;
        }

        this.configuration.save(this.file);
    }

    public YamlConfiguration getConfiguration() {

        if(this.configuration == null) {
            reload();
        }

        return this.configuration;
    }

    public void reload() {

        if(this.file == null) {
            file = new File(plugin.getDataFolder(), filePath);
        }

        this.configuration = YamlConfiguration.loadConfiguration(this.file);
        final InputStream defaultStream = plugin.getResource(filePath);

        if(defaultStream != null) {
            try (InputStreamReader reader = new InputStreamReader(defaultStream)) {
                YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(reader);
                this.configuration.setDefaults(defaultConfig);
            } catch (IOException exception) {
                SpigotBoilerplate.INSTANCE.getLogger().log(Level.WARNING, "An error occurred while saving file at path " + filePath, exception);
            }
        }
    }
}
