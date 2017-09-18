package org.cantaloupe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.data.DataContainer;
import org.cantaloupe.plugin.CantaloupePlugin;
import org.cantaloupe.service.services.LightService;
import org.cantaloupe.service.services.MongoService;
import org.cantaloupe.service.services.NMSService;
import org.cantaloupe.service.services.PacketService;
import org.cantaloupe.service.services.ParticleService;
import org.cantaloupe.service.services.ScheduleService;
import org.cantaloupe.service.services.ScreenService;

/**
 * A class used to manage services.
 * 
 * @author Dylan Scheltens
 *
 */
public class ServiceManager {
    private final DataContainer<String, DataContainer<Class<? extends IService>, IService>> providers;

    private ServiceManager() {
        this.providers = DataContainer.<String, DataContainer<Class<? extends IService>, IService>>of(true);
    }

    /**
     * Creates and returns a new service manager.
     * 
     * @return The service manager
     */
    public static ServiceManager of() {
        return new ServiceManager();
    }

    /**
     * Loads the service manager.
     */
    public void load() {
        DataContainer<Class<? extends IService>, IService> services = DataContainer.<Class<? extends IService>, IService>of(true);
        services.put(NMSService.class, new NMSService());
        services.put(PacketService.class, new PacketService());
        services.put(ParticleService.class, new ParticleService());
        services.put(ScheduleService.class, new ScheduleService());
        services.put(LightService.class, new LightService());
        services.put(ScreenService.class, new ScreenService());
        services.put(MongoService.class, new MongoService());

        this.providers.put("cantaloupe", services);

        services.forEach((serviceClass, service) -> service.load());
    }

    /**
     * Unloads the service manager.
     */
    public void unload() {
        for (DataContainer<Class<? extends IService>, IService> services : this.providers.valueSet()) {
            services.forEach((serviceClass, service) -> service.unload());
            services.clear();
        }

        this.providers.clear();
    }

    /**
     * Returns a service from the service manager by provider.
     * 
     * @param <T>
     *            The type of the service
     * @param provider
     *            The provider
     * @param name
     *            The name of the service
     * 
     * @return The service
     */
    public <T extends IService> T provide(CantaloupePlugin provider, String name) {
        return this.<T>provide(provider.getID(), name);
    }

    /**
     * Returns a service from the service manager by provider.
     * 
     * @param <T>
     *            The type of the service
     * @param provider
     *            The provider
     * @param serviceClass
     *            The type of the service
     * 
     * @return The service
     */
    public <T extends IService> T provide(CantaloupePlugin provider, Class<T> serviceClass) {
        return this.<T>provide(provider.getID(), serviceClass);
    }

    /**
     * Returns a service from the service manager by plugin.
     * 
     * @param <T>
     *            The type of the service
     * @param pluginID
     *            The ID of the provider
     * @param name
     *            The name of the service
     * 
     * @return The service
     */
    @SuppressWarnings("unchecked")
    public <T extends IService> T provide(String pluginID, String name) {
        if (this.providers.containsKey(pluginID)) {
            for (IService service : this.providers.get(pluginID).valueSet()) {
                if (service.getName().equals(name)) {
                    return (T) service;
                }
            }
        }

        return null;
    }

    /**
     * Returns a service from the service manager by plugin.
     * 
     * @param <T>
     *            The type of the service
     * @param pluginID
     *            The ID of the provider
     * @param serviceClass
     *            The type of the service
     * 
     * @return The service
     */
    @SuppressWarnings("unchecked")
    public <T extends IService> T provide(String pluginID, Class<T> serviceClass) {
        if (this.providers.containsKey(pluginID)) {
            for (IService service : this.providers.get(pluginID).valueSet()) {
                if (service.getClass() == serviceClass) {
                    return (T) service;
                }
            }
        }

        return null;
    }

    /**
     * Returns a service from the service manager.
     * 
     * @param <T>
     *            The type of the service
     * @param name
     *            The name of the service
     * 
     * @return The service
     */
    @SuppressWarnings("unchecked")
    public <T extends IService> T provide(String name) {
        for (String pluginID : this.providers.keySet()) {
            for (IService service : this.providers.get(pluginID).valueSet()) {
                if (service.getName().equals(name)) {
                    return (T) service;
                }
            }
        }

        return null;
    }

    /**
     * Returns a service from the service manager.
     * 
     * @param <T>
     *            The type of the service
     * @param serviceClass
     *            The type of the service
     * 
     * @return The service
     */
    @SuppressWarnings("unchecked")
    public <T extends IService> T provide(Class<T> serviceClass) {
        for (String pluginID : this.providers.keySet()) {
            for (IService service : this.providers.get(pluginID).valueSet()) {
                if (service.getClass() == serviceClass) {
                    return (T) service;
                }
            }
        }

        return null;
    }

    private void checkService(CantaloupePlugin plugin, Class<? extends IService> serviceClass) {
        List<Class<? extends IService>> coreServices = new ArrayList<Class<? extends IService>>();
        coreServices.add(NMSService.class);
        coreServices.add(PacketService.class);
        coreServices.add(ParticleService.class);
        coreServices.add(ScheduleService.class);
        coreServices.add(LightService.class);
        coreServices.add(ScreenService.class);
        coreServices.add(MongoService.class);

        if (coreServices.contains(serviceClass)) {
            throw new RuntimeException("'" + plugin.getID() + "' is trying to initialize a Cantaloupe core service.");
        }
    }

    /**
     * Sets the provider of a service.
     * 
     * @param plugin
     *            The provider
     * @param serviceClass
     *            The type of the service
     * @param service
     *            The service
     */
    public void setProvider(CantaloupePlugin plugin, Class<? extends IService> serviceClass, IService service) {
        this.checkService(plugin, serviceClass);

        if (!this.providers.containsKey(plugin.getName())) {
            this.providers.put(plugin.getName(), DataContainer.<Class<? extends IService>, IService>of(true));
        }

        if (this.providers.get(plugin.getName()).containsKey(serviceClass)) {
            this.providers.get(plugin.getName()).get(serviceClass).unload();
        }

        service.load();

        this.providers.get(plugin.getName()).put(serviceClass, service);
    }

    /**
     * Gets the provider of a service.
     * 
     * @param serviceClass
     *            The type of the service
     * 
     * @return The provider
     */
    public Optional<CantaloupePlugin> getProvider(Class<? extends IService> serviceClass) {
        for (String pluginID : this.providers.keySet()) {
            for (IService service : this.providers.get(pluginID).valueSet()) {
                if (service.getClass() == serviceClass) {
                    return Cantaloupe.getPluginManager().getPlugin(pluginID);
                }
            }
        }

        return null;
    }
}