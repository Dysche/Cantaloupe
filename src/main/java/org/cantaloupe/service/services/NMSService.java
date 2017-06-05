package org.cantaloupe.service.services;

import org.bukkit.Bukkit;
import org.cantaloupe.service.Service;

public class NMSService implements Service {
    private String serverPackage = null;
    private String bukkitPackage = null;

    @Override
    public void load() {
        this.serverPackage = "net.minecraft.server." + this.getStringVersion() + ".";
        this.bukkitPackage = "org.bukkit.craftbukkit." + this.getStringVersion() + ".";
    }

    @Override
    public void unload() {
        this.serverPackage = null;
        this.bukkitPackage = null;
    }

    public String getStringVersion() {
        String version = Bukkit.getServer().getClass().getPackage().getName();

        return version.substring(version.lastIndexOf('.') + 1);
    }

    public int getVersion() {
        return Integer.parseInt(this.getStringVersion().split("_")[1]);
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