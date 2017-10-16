package org.cantaloupe.service.services;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.data.DataContainer;
import org.cantaloupe.plugin.CantaloupePlugin;
import org.cantaloupe.service.IService;

public class ScheduleService implements IService {
    private DataContainer<String, DataContainer<String, Integer>> services  = null;
    private BukkitScheduler                                       scheduler = null;
    private int                                                   checkTask = -1;

    @Override
    public void load() {
        this.services = DataContainer.of();
        this.scheduler = Bukkit.getScheduler();

        this.checkTask = this.scheduler.scheduleSyncRepeatingTask(Cantaloupe.getInstance(), new Runnable() {
            @Override
            public void run() {
                services.forEach((pluginName, tasks) -> {
                    ArrayList<String> finishedTasks = new ArrayList<String>();

                    tasks.forEach((name, task) -> {
                        if (!scheduler.isQueued(task) && !scheduler.isCurrentlyRunning(task)) {
                            finishedTasks.add(name);
                        }
                    });

                    for (String task : finishedTasks) {
                        tasks.remove(task);
                    }
                });
            }
        }, 0L, 0L);
    }

    @Override
    public void unload() {
        this.services.forEach((pluginID, tasks) -> {
            tasks.forEach((name, task) -> {
                this.scheduler.cancelTask(task);
            });

            tasks.clear();
        });

        this.services.clear();
        this.services = null;

        this.scheduler.cancelTask(this.checkTask);
        this.scheduler = null;
    }

    public void delay(CantaloupePlugin plugin, String name, Runnable runnable, long delay) {
        this.trySetTask(plugin.getID(), name, this.scheduler.scheduleSyncDelayedTask(plugin, runnable, delay));
    }

    public void delay(CantaloupePlugin plugin, String name, Runnable runnable) {
        this.trySetTask(plugin.getID(), name, this.scheduler.scheduleSyncDelayedTask(plugin, runnable));
    }

    public void delay(String name, Runnable runnable, long delay) {
        this.trySetTask("cantaloupe", name, this.scheduler.scheduleSyncDelayedTask(Cantaloupe.getInstance(), runnable, delay));
    }

    public void delay(String name, Runnable runnable) {
        this.trySetTask("cantaloupe", name, this.scheduler.scheduleSyncDelayedTask(Cantaloupe.getInstance(), runnable));
    }

    public void repeat(CantaloupePlugin plugin, String name, Runnable runnable, long delay, long period) {
        this.trySetTask(plugin.getID(), name, this.scheduler.scheduleSyncRepeatingTask(Cantaloupe.getInstance(), runnable, delay, period));
    }

    public void repeat(CantaloupePlugin plugin, String name, Runnable runnable, long period) {
        this.trySetTask(plugin.getID(), name, this.scheduler.scheduleSyncRepeatingTask(Cantaloupe.getInstance(), runnable, 0L, period));
    }

    public void repeat(String name, Runnable runnable, long delay, long period) {
        this.trySetTask("cantaloupe", name, this.scheduler.scheduleSyncRepeatingTask(Cantaloupe.getInstance(), runnable, delay, period));
    }

    public void repeat(String name, Runnable runnable, long period) {
        this.trySetTask("cantaloupe", name, this.scheduler.scheduleSyncRepeatingTask(Cantaloupe.getInstance(), runnable, 0L, period));
    }

    public void repeat(String name, Runnable runnable) {
        this.trySetTask("cantaloupe", name, this.scheduler.scheduleSyncRepeatingTask(Cantaloupe.getInstance(), runnable, 0L, 0L));
    }

    public void cancel(CantaloupePlugin plugin, String name) {
        if (this.services.containsKey(plugin.getID())) {
            if (this.services.get(plugin.getID()).containsKey(name)) {
                this.scheduler.cancelTask(this.services.get(plugin.getID()).get(name));

                this.services.get(plugin.getID()).remove(name);
            }
        }
    }

    public void cancel(String name) {
        if (this.services.containsKey("cantaloupe")) {
            if (this.services.get("cantaloupe").containsKey(name)) {
                this.scheduler.cancelTask(this.services.get("cantaloupe").get(name));

                this.services.get("cantaloupe").remove(name);
            }
        }
    }

    private void trySetTask(String pluginName, String name, int task) {
        if (this.services.containsKey(pluginName)) {
            if (this.services.get(pluginName).containsKey(name)) {
                this.scheduler.cancelTask(this.services.get(pluginName).get(name));

                this.services.get(pluginName).remove(name);
            }
        } else {
            this.services.put(pluginName, DataContainer.<String, Integer>of());
        }

        this.services.get(pluginName).put(name, task);
    }

    public boolean isTaskRunning(CantaloupePlugin plugin, String name) {
        return this.services.containsKey(plugin.getID()) && this.services.get(plugin.getID()).containsKey(name);
    }

    public boolean isTaskRunning(String name) {
        return this.services.containsKey("cantaloupe") && this.services.get("cantaloupe").containsKey(name);
    }

    @Override
    public String getName() {
        return "schedule";
    }
}