package org.cantaloupe.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.plugin.CantaloupePlugin;
import org.cantaloupe.service.services.NMSService;
import org.cantaloupe.service.services.PacketService;
import org.cantaloupe.service.services.ParticleService;
import org.cantaloupe.service.services.ScheduleService;

public class ServiceManager {
    private HashMap<String, HashMap<Class<? extends Service>, Service>> providers = null;

    public ServiceManager() {
        this.providers = new LinkedHashMap<String, HashMap<Class<? extends Service>, Service>>();
    }

    public void load() {
        HashMap<Class<? extends Service>, Service> services = new LinkedHashMap<Class<? extends Service>, Service>();
        services.put(NMSService.class, new NMSService());
        services.put(PacketService.class, new PacketService());
        services.put(ParticleService.class, new ParticleService());
        services.put(ScheduleService.class, new ScheduleService());

        this.providers.put("cantaloupe", services);

        services.forEach((serviceClass, service) -> service.load());
    }

    public void unload() {
        for (HashMap<Class<? extends Service>, Service> services : this.providers.values()) {
            services.forEach((serviceClass, service) -> service.unload());
            services.clear();
        }

        this.providers.clear();
    }

    @SuppressWarnings("unchecked")
    public <T extends Service> T provide(String name) {
        for (String pluginID : this.providers.keySet()) {
            for (Service service : this.providers.get(pluginID).values()) {
                if (service.getName().equals(name)) {
                    return (T) service;
                }
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends Service> T provide(Class<T> serviceClass) {
        for (String pluginID : this.providers.keySet()) {
            for (Service service : this.providers.get(pluginID).values()) {
                if (service.getClass() == serviceClass) {
                    return (T) service;
                }
            }
        }

        return null;
    }

    private void checkService(CantaloupePlugin plugin, Class<? extends Service> serviceClass) {
        List<Class<? extends Service>> coreServices = new ArrayList<Class<? extends Service>>();
        coreServices.add(NMSService.class);
        coreServices.add(PacketService.class);
        coreServices.add(ParticleService.class);
        coreServices.add(ScheduleService.class);

        if (coreServices.contains(serviceClass)) {
            throw new RuntimeException("'" + plugin.getID() + "' is trying to initialize a Cantaloupe core service.");
        }
    }

    public void setProvider(CantaloupePlugin plugin, Class<? extends Service> serviceClass, Service service) {
        this.checkService(plugin, serviceClass);
        
        if (!this.providers.containsKey(plugin.getName())) {
            this.providers.put(plugin.getName(), new LinkedHashMap<Class<? extends Service>, Service>());
        }

        if (this.providers.get(plugin.getName()).containsKey(serviceClass)) {
            this.providers.get(plugin.getName()).get(serviceClass).unload();
        }

        service.load();

        this.providers.get(plugin.getName()).put(serviceClass, service);
    }

    public CantaloupePlugin getProvider(Class<? extends Service> serviceClass) {
        for (String pluginID : this.providers.keySet()) {
            for (Service service : this.providers.get(pluginID).values()) {
                if (service.getClass() == serviceClass) {
                    return Cantaloupe.getPluginManager().getPlugin(pluginID);
                }
            }
        }

        return null;
    }
}