package org.cantaloupe.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class YMLConfig {
    private File              file   = null;
    private FileConfiguration object = null;

    private YMLConfig(File file) {
        this.file = file;
        this.object = new YamlConfiguration();
    }

    public static YMLConfig of(File file) {
        return new YMLConfig(file);
    }

    public static YMLConfig of(String path) {
        return new YMLConfig(new File(path));
    }

    public void load() throws IOException {
        try {
            this.object.load(this.file);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() throws IOException {
        this.object.save(this.file);
    }

    public void createSection(String name) {
        this.object.createSection(name);
    }

    public void put(String key, Object value) {
        this.object.set(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) this.object.get(key);
    }
}