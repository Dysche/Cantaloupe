package org.cantaloupe.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * A class used to manage a JSON configuration file.
 * 
 * @author Dylan Scheltens
 *
 */
@SuppressWarnings("unchecked")
public class JSONConfig {
    private File       file   = null;
    private JSONObject object = null;

    private JSONConfig(File file) {
        this.file = file;
        this.object = new JSONObject();
    }

    /**
     * Creates and returns a new JSON config.
     * 
     * @param file
     *            The config file
     * @return The config
     */
    public static JSONConfig of(File file) {
        return new JSONConfig(file);
    }

    /**
     * Creates and returns a new JSON config.
     * 
     * @param path
     *            The path of the config file
     * @return The config
     */
    public static JSONConfig of(String path) {
        return new JSONConfig(new File(path));
    }

    /**
     * Loads the config.
     * 
     * @throws IOException
     *             If the file doesn't exist
     * @throws ParseException
     *             If the read data isn't valid JSON
     */
    public void load() throws IOException, ParseException {
        this.object = (JSONObject) new JSONParser().parse(new FileReader(this.file));
    }

    /**
     * Saves the config.
     * 
     * @throws IOException
     *             If the file doesn't exist
     */
    public void save() throws IOException {
        if (!this.file.exists()) {
            if (this.file.getParentFile() != null) {
                this.file.getParentFile().mkdirs();
            }

            this.file.createNewFile();
        }

        FileWriter writer = new FileWriter(this.file);
        writer.write(this.object.toJSONString());
        writer.flush();
        writer.close();
    }

    /**
     * Stores a value in the config.
     * 
     * @param key
     *            The key
     * @param value
     *            The value
     */
    public void put(Object key, Object value) {
        this.object.put(key, value);
    }

    /**
     * Stores an array in the config.
     * 
     * @param key
     *            The key
     * @param data
     *            The array
     */
    public void putArray(Object key, Object[] data) {
        JSONArray value = new JSONArray();

        for (Object o : data) {
            value.add(o);
        }

        this.object.put(key, value);
    }

    /**
     * Stores a collection in the config.
     * 
     * @param key
     *            The key
     * @param data
     *            The collection
     */
    public void putCollection(Object key, Collection<Object> data) {
        JSONArray value = new JSONArray();
        value.addAll(data);

        this.object.put(key, value);
    }

    /**
     * Stores a collection in the config at a specified index.
     * 
     * @param key
     *            The key
     * @param data
     *            The collection
     * @param index
     *            The index to store it at
     */
    public void putCollection(Object key, Collection<Object> data, int index) {
        JSONArray value = new JSONArray();
        value.addAll(index, data);

        this.object.put(key, value);
    }

    /**
     * Checks if the configuration file exists.
     * 
     * @return True if it does, false if not
     */
    public boolean exists() {
        return this.file.exists();
    }

    /**
     * Gets a value from the config.
     * 
     * @param <T>
     *            The type to cast the value to
     * @param key
     *            The key
     * @return The value
     */
    public <T> T get(Object key) {
        return (T) this.object.get(key);
    }
}