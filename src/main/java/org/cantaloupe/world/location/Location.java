package org.cantaloupe.world.location;

import org.bukkit.block.Block;
import org.cantaloupe.world.World;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

public interface Location {
    public Location add(Vector3i vector);

    public Location add(Vector3f vector);

    public Location add(Vector3d vector);

    public Location add(int x, int y, int z);

    public Location add(float x, float y, float z);

    public Location add(double x, double y, double z);

    public Location add(int n);

    public Location add(float n);

    public Location add(double n);

    public Location subtract(Vector3i vector);

    public Location subtract(Vector3f vector);

    public Location subtract(Vector3d vector);

    public Location subtract(int x, int y, int z);

    public Location subtract(float x, float y, float z);

    public Location subtract(double x, double y, double z);

    public Location subtract(int n);

    public Location subtract(float n);

    public Location subtract(double n);

    public Location mult(Vector3i vector);

    public Location mult(Vector3f vector);

    public Location mult(Vector3d vector);

    public Location mult(int x, int y, int z);

    public Location mult(float x, float y, float z);

    public Location mult(double x, double y, double z);

    public Location mult(int n);

    public Location mult(float n);

    public Location mult(double n);

    public Location divide(Vector3i vector);

    public Location divide(Vector3f vector);

    public Location divide(Vector3d vector);

    public Location divide(int x, int y, int z);

    public Location divide(float x, float y, float z);

    public Location divide(double x, double y, double z);

    public Location divide(int n);

    public Location divide(float n);

    public Location divide(double n);

    public default double distance(Location other) {
        return this.getPosition().distance(other.getPosition());
    }

    public Location clone();

    public org.bukkit.Location toHandle();

    public World getWorld();

    public default Vector3d getPosition() {
        return new Vector3d(this.toHandle().getX(), this.toHandle().getY(), this.toHandle().getZ());
    }

    public default Vector3i getBlockPosition() {
        return new Vector3i(this.toHandle().getBlockX(), this.toHandle().getBlockY(), this.toHandle().getBlockZ());
    }

    public default Vector3i getChunkPosition() {
        return new Vector3i(this.toHandle().getChunk().getX(), 0, this.toHandle().getChunk().getZ());
    }

    public default Vector3d getDirection() {
        return new Vector3d(this.toHandle().getDirection().getX(), this.toHandle().getDirection().getY(), this.toHandle().getDirection().getZ());
    }

    public default Vector2f getRotation() {
        return new Vector2f(this.getYaw(), this.getPitch());
    }

    public default float getYaw() {
        return this.toHandle().getYaw();
    }

    public default float getPitch() {
        return this.toHandle().getPitch();
    }

    public default Block getBlock() {
        return this.toHandle().getBlock();
    }
}