package org.cantaloupe.service;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ServiceManager {
    private HashMap<String, Service> services = null;

    public ServiceManager() {
        this.services = new LinkedHashMap<String, Service>();
    }

    public void registerService(Class<? extends Service> serviceClass) {
        try {
            Service service = serviceClass.newInstance();

            this.services.put(service.getName(), service);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        this.services.forEach((name, service) -> {
            service.load();
        });
    }
    
    public void unload() {
        this.services.forEach((name, service) -> {
            service.unload();
        });

        this.services.clear();
    }

    @SuppressWarnings("unchecked")
    public <T extends Service> T getService(String name) {
        return (T) this.services.get(name);
    }

    @SuppressWarnings("unchecked")
    public <T extends Service> T getService(Class<T> serviceClass) {
        for (Service service : this.services.values()) {
            if (service.getClass() == serviceClass) {
                return (T) service;
            }
        }

        return null;
    }
}