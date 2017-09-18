package org.cantaloupe.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * A class used to manage a YAML configuration file.
 * 
 * @author Dylan Scheltens
 *
 */
public class YMLConfig {
    private File              file   = null;
    private FileConfiguration object = null;

    private YMLConfig(File file) {
        this.file = file;
        this.object = new YamlConfiguration();
    }

    /**
     * Creates and returns a new YML config.
     * 
     * @param file
     *            The config file
     * @return The config
     */
    public static YMLConfig of(File file) {
        return new YMLConfig(file);
    }

    /**
     * Creates and returns a new YML config.
     * 
     * @param path
     *            The path of the config file
     * @return The config
     */
    public static YMLConfig of(String path) {
        return new YMLConfig(new File(path));
    }

    /**
     * Loads the config.
     * 
     * @throws IOException
     *             If the file doesn't exist
     * @throws InvalidConfigurationException
     *             If the read data isn't valid YML
     */
    public void load() throws IOException, InvalidConfigurationException {
        try {
            this.object.load(this.file);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the config.
     * 
     * @throws IOException
     *             If the file doesn't exist
     */
    public void save() throws IOException {
        this.object.save(this.file);
    }

    /**
     * Creates a section in the config.
     * 
     * @param name
     *            The name of the section
     */
    public void createSection(String name) {
        this.object.createSection(name);
    }

    /**
     * Stores a value in the config
     * 
     * @param key
     *            The key
     * @param value
     *            The value
     */
    public void put(String key, Object value) {
        this.object.set(key, value);
    }

    /**
     * Gets a section from the config.
     * 
     * @param name
     *            The name of the section
     * @return The section
     */
    public ConfigurationSection getSection(String name) {
        return this.object.getConfigurationSection(name);
    }

    @SuppressWarnings("unchecked")
    /**
     * Gets a value from the config.
     * 
     * @param key
     *            The key
     * @return The value
     */
    public <T> T get(String key) {
        return (T) this.object.get(key);
    }
}