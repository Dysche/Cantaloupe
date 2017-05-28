package org.cantaloupe.world;

import org.bukkit.block.Block;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class Location {
    private org.bukkit.Location handle;

    private Location(org.bukkit.Location handle) {
        this.handle = handle;
    }

    private Location(World world, Vector3d position) {
        this.handle = new org.bukkit.Location(world.getHandle(), position.x, position.y, position.z);
    }

    private Location(World world, Vector3f position) {
        this.handle = new org.bukkit.Location(world.getHandle(), position.x, position.y, position.z);
    }

    private Location(World world, Vector3i position) {
        this.handle = new org.bukkit.Location(world.getHandle(), position.x, position.y, position.z);
    }

    private Location(World world, Vector3d position, Vector2f rotation) {
        this.handle = new org.bukkit.Location(world.getHandle(), position.x, position.y, position.z, rotation.x, rotation.y);
    }

    private Location(World world, Vector3f position, Vector2f rotation) {
        this.handle = new org.bukkit.Location(world.getHandle(), position.x, position.y, position.z, rotation.x, rotation.y);
    }

    private Location(World world, Vector3i position, Vector2f rotation) {
        this.handle = new org.bukkit.Location(world.getHandle(), position.x, position.y, position.z, rotation.x, rotation.y);
    }

    public static Location of(org.bukkit.Location handle) {
        return new Location(handle);
    }

    public static Location of(World world, Vector3d position) {
        return new Location(world, position);
    }

    public static Location of(World world, Vector3f position) {
        return new Location(world, position);
    }

    public static Location of(World world, Vector3i position) {
        return new Location(world, position);
    }

    public static Location of(World world, Vector3d position, Vector2f rotation) {
        return new Location(world, position, rotation);
    }

    public static Location of(World world, Vector3f position, Vector2f rotation) {
        return new Location(world, position, rotation);
    }

    public static Location of(World world, Vector3i position, Vector2f rotation) {
        return new Location(world, position, rotation);
    }

    public Vector3i getBlockPosition() {
        return new Vector3i(this.handle.getBlockX(), this.handle.getBlockY(), this.handle.getBlockZ());
    }

    public Vector3i getChunkPosition() {
        return new Vector3i(this.handle.getChunk().getX(), 0, this.handle.getChunk().getZ());
    }

    public Vector3d getDirection() {
        return new Vector3d(this.handle.getDirection().getX(), this.handle.getDirection().getY(), this.handle.getDirection().getZ());
    }

    public Vector2f getRotation() {
        return new Vector2f(this.getYaw(), this.getPitch());
    }

    public float getYaw() {
        return this.handle.getYaw();
    }

    public float getPitch() {
        return this.handle.getPitch();
    }

    public Block getBlock() {
        return this.handle.getBlock();
    }

    public org.bukkit.Location getHandle() {
        return this.handle;
    }
}