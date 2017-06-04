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

@SuppressWarnings("unchecked")
public class JSONConfig {
    private File       file   = null;
    private JSONObject object = null;

    private JSONConfig(File file) {
        this.file = file;
        this.object = new JSONObject();
    }

    public static JSONConfig of(File file) {
        return new JSONConfig(file);
    }
    
    public static JSONConfig of(String path) {
        return new JSONConfig(new File(path));
    }

    public void load() throws IOException {
        try {
            this.object = (JSONObject) new JSONParser().parse(new FileReader(this.file));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void save() throws IOException {
        try (FileWriter file = new FileWriter(this.file)) {
            file.write(this.object.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void put(Object key, Object value) {
        this.object.put(key, value);
    }

    public void putArray(Object key, Object[] data) {
        JSONArray value = new JSONArray();

        for (Object o : data) {
            value.add(o);
        }

        this.object.put(key, value);
    }

    public void putCollection(Object key, Collection<Object> data) {
        JSONArray value = new JSONArray();
        value.addAll(data);

        this.object.put(key, value);
    }

    public void putCollection(Object key, Collection<Object> data, int index) {
        JSONArray value = new JSONArray();
        value.addAll(index, data);

        this.object.put(key, value);
    }

    public <T> T get(Object key) {
        return (T) this.object.get(key);
    }
}