package org.cantaloupe.model;

import java.util.Optional;

import org.cantaloupe.data.DataContainer;

public class ModelManager {
    private final DataContainer<String, Model> models;

    private ModelManager() {
        this.models = DataContainer.of();
    }

    public static ModelManager of() {
        return new ModelManager();
    }

    public void unload() {
        this.models.clear();
    }

    public void addModel(Model model) {
        this.models.put(model.getName(), model);
    }

    public void removeModel(String name) {
        this.models.remove(name);
    }

    public Optional<Model> getModel(String name) {
        return Optional.ofNullable(this.models.get(name));
    }
}