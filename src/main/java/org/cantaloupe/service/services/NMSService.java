package org.cantaloupe.service.services;

import org.bukkit.Bukkit;
import org.cantaloupe.service.Service;

public class NMSService implements Service {
    private String serverPackage = null;
    private String bukkitPackage = null;

    private String serverVersion = null;
    private int    intVersion    = -1;

    @Override
    public void load() {
        String version = Bukkit.getServer().getClass().getPackage().getName();
        this.serverVersion = version.substring(version.lastIndexOf('.') + 1);       
        this.intVersion = Integer.parseInt(this.getServerVersion().split("_")[1]);
        
        this.serverPackage = "net.minecraft.server." + this.getServerVersion() + ".";
        this.bukkitPackage = "org.bukkit.craftbukkit." + this.getServerVersion() + ".";
    }

    @Override
    public void unload() {
        this.serverPackage = null;
        this.bukkitPackage = null;
    }

    public String getServerVersion() {
        return this.serverVersion;
    }

    public int getIntVersion() {
        return this.intVersion;
    }

    public String getServerPackage() {
        return this.serverPackage;
    }

    public String getBukkitPackage() {
        return this.bukkitPackage;
    }

    public Class<?> getNMSClass(String path) {
        try {
            return Class.forName(this.serverPackage + path);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Class<?> getNMSBukkitClass(String path) {
        try {
            return Class.forName(this.bukkitPackage + path);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getName() {
        return "nms";
    }
}