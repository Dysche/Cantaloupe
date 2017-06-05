package org.cantaloupe.world.location;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.world.World;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class ImmutableLocation implements Location {
    protected final World               world;
    protected final org.bukkit.Location handle;

    private ImmutableLocation(Location other) {
        this.handle = other.toHandle().clone();
        this.world = other.getWorld();
    }
    
    private ImmutableLocation(org.bukkit.Location handle) {
        this.handle = handle.clone();
        this.world = Cantaloupe.getWorldManager().getWorldFromHandle(handle.getWorld());
    }

    private ImmutableLocation(World world, Vector3d position) {
        this.handle = new org.bukkit.Location(world.toHandle(), position.x, position.y, position.z);
        this.world = world;
    }

    private ImmutableLocation(World world, Vector3f position) {
        this.handle = new org.bukkit.Location(world.toHandle(), position.x, position.y, position.z);
        this.world = world;
    }

    private ImmutableLocation(World world, Vector3i position) {
        this.handle = new org.bukkit.Location(world.toHandle(), position.x, position.y, position.z);
        this.world = world;
    }

    private ImmutableLocation(World world, Vector3d position, Vector2f rotation) {
        this.handle = new org.bukkit.Location(world.toHandle(), position.x, position.y, position.z, rotation.x, rotation.y);
        this.world = world;
    }

    private ImmutableLocation(World world, Vector3f position, Vector2f rotation) {
        this.handle = new org.bukkit.Location(world.toHandle(), position.x, position.y, position.z, rotation.x, rotation.y);
        this.world = world;
    }

    private ImmutableLocation(World world, Vector3i position, Vector2f rotation) {
        this.handle = new org.bukkit.Location(world.toHandle(), position.x, position.y, position.z, rotation.x, rotation.y);
        this.world = world;
    }
    
    public static ImmutableLocation of(Location other) {
        return new ImmutableLocation(other);
    }
    
    public static ImmutableLocation of(org.bukkit.Location handle) {
        return new ImmutableLocation(handle);
    }

    public static ImmutableLocation of(World world, Vector3d position) {
        return new ImmutableLocation(world, position);
    }

    public static ImmutableLocation of(World world, Vector3f position) {
        return new ImmutableLocation(world, position);
    }

    public static ImmutableLocation of(World world, Vector3i position) {
        return new ImmutableLocation(world, position);
    }

    public static ImmutableLocation of(World world, Vector3d position, Vector2f rotation) {
        return new ImmutableLocation(world, position, rotation);
    }

    public static ImmutableLocation of(World world, Vector3f position, Vector2f rotation) {
        return new ImmutableLocation(world, position, rotation);
    }

    public static ImmutableLocation of(World world, Vector3i position, Vector2f rotation) {
        return new ImmutableLocation(world, position, rotation);
    }

    @Override
    public ImmutableLocation add(Vector3i vector) {
        return ImmutableLocation.of(this.handle.clone().add(vector.x, vector.y, vector.z));
    }

    @Override
    public ImmutableLocation add(Vector3f vector) {
        return ImmutableLocation.of(this.handle.clone().add(vector.x, vector.y, vector.z));
    }

    @Override
    public ImmutableLocation add(Vector3d vector) {
        return ImmutableLocation.of(this.handle.clone().add(vector.x, vector.y, vector.z));
    }

    @Override
    public ImmutableLocation add(int x, int y, int z) {
        return ImmutableLocation.of(this.handle.clone().add(x, y, z));
    }

    @Override
    public ImmutableLocation add(float x, float y, float z) {
        return ImmutableLocation.of(this.handle.clone().add(x, y, z));
    }

    @Override
    public ImmutableLocation add(double x, double y, double z) {
        return ImmutableLocation.of(this.handle.clone().add(x, y, z));
    }

    @Override
    public ImmutableLocation add(int n) {
        return ImmutableLocation.of(this.handle.clone().add(n, n, n));
    }

    @Override
    public ImmutableLocation add(float n) {
        return ImmutableLocation.of(this.handle.clone().add(n, n, n));
    }

    @Override
    public ImmutableLocation add(double n) {
        return ImmutableLocation.of(this.handle.clone().add(n, n, n));
    }

    @Override
    public ImmutableLocation subtract(Vector3i vector) {
        return ImmutableLocation.of(this.handle.clone().subtract(vector.x, vector.y, vector.z));
    }

    @Override
    public ImmutableLocation subtract(Vector3f vector) {
        return ImmutableLocation.of(this.handle.clone().subtract(vector.x, vector.y, vector.z));
    }

    @Override
    public ImmutableLocation subtract(Vector3d vector) {
        return ImmutableLocation.of(this.handle.clone().subtract(vector.x, vector.y, vector.z));
    }

    @Override
    public ImmutableLocation subtract(int x, int y, int z) {
        return ImmutableLocation.of(this.handle.clone().subtract(x, y, z));
    }

    @Override
    public ImmutableLocation subtract(float x, float y, float z) {
        return ImmutableLocation.of(this.handle.clone().subtract(x, y, z));
    }

    @Override
    public ImmutableLocation subtract(double x, double y, double z) {
        return ImmutableLocation.of(this.handle.clone().subtract(x, y, z));
    }

    @Override
    public ImmutableLocation subtract(int n) {
        return ImmutableLocation.of(this.handle.clone().subtract(n, n, n));
    }

    @Override
    public ImmutableLocation subtract(float n) {
        return ImmutableLocation.of(this.handle.clone().subtract(n, n, n));
    }

    @Override
    public ImmutableLocation subtract(double n) {
        return ImmutableLocation.of(this.handle.clone().subtract(n, n, n));
    }

    @Override
    public ImmutableLocation mult(Vector3i vector) {
        return ImmutableLocation.of(new org.bukkit.Location(this.world.toHandle(), this.handle.getX() * vector.x, this.handle.getY() * vector.y, this.handle.getZ() * vector.z));
    }

    @Override
    public ImmutableLocation mult(Vector3f vector) {
        return ImmutableLocation.of(new org.bukkit.Location(this.world.toHandle(), this.handle.getX() * vector.x, this.handle.getY() * vector.y, this.handle.getZ() * vector.z));
    }

    @Override
    public ImmutableLocation mult(Vector3d vector) {
        return ImmutableLocation.of(new org.bukkit.Location(this.world.toHandle(), this.handle.getX() * vector.x, this.handle.getY() * vector.y, this.handle.getZ() * vector.z));
    }

    @Override
    public ImmutableLocation mult(int x, int y, int z) {
        return ImmutableLocation.of(new org.bukkit.Location(this.world.toHandle(), this.handle.getX() * x, this.handle.getY() * y, this.handle.getZ() * z));
    }

    @Override
    public ImmutableLocation mult(float x, float y, float z) {
        return ImmutableLocation.of(new org.bukkit.Location(this.world.toHandle(), this.handle.getX() * x, this.handle.getY() * y, this.handle.getZ() * z));
    }

    @Override
    public ImmutableLocation mult(double x, double y, double z) {
        return ImmutableLocation.of(new org.bukkit.Location(this.world.toHandle(), this.handle.getX() * x, this.handle.getY() * y, this.handle.getZ() * z));
    }

    @Override
    public ImmutableLocation mult(int n) {
        return ImmutableLocation.of(new org.bukkit.Location(this.world.toHandle(), this.handle.getX() * n, this.handle.getY() * n, this.handle.getZ() * n));
    }

    @Override
    public ImmutableLocation mult(float n) {
        return ImmutableLocation.of(new org.bukkit.Location(this.world.toHandle(), this.handle.getX() * n, this.handle.getY() * n, this.handle.getZ() * n));
    }

    @Override
    public ImmutableLocation mult(double n) {
        return ImmutableLocation.of(new org.bukkit.Location(this.world.toHandle(), this.handle.getX() * n, this.handle.getY() * n, this.handle.getZ() * n));
    }

    @Override
    public ImmutableLocation divide(Vector3i vector) {
        return ImmutableLocation.of(new org.bukkit.Location(this.world.toHandle(), this.handle.getX() / vector.x, this.handle.getY() / vector.y, this.handle.getZ() / vector.z));
    }

    @Override
    public ImmutableLocation divide(Vector3f vector) {
        return ImmutableLocation.of(new org.bukkit.Location(this.world.toHandle(), this.handle.getX() / vector.x, this.handle.getY() / vector.y, this.handle.getZ() / vector.z));
    }

    @Override
    public ImmutableLocation divide(Vector3d vector) {
        return ImmutableLocation.of(new org.bukkit.Location(this.world.toHandle(), this.handle.getX() / vector.x, this.handle.getY() / vector.y, this.handle.getZ() / vector.z));
    }

    @Override
    public ImmutableLocation divide(int x, int y, int z) {
        return ImmutableLocation.of(new org.bukkit.Location(this.world.toHandle(), this.handle.getX() / x, this.handle.getY() / y, this.handle.getZ() / z));
    }

    @Override
    public ImmutableLocation divide(float x, float y, float z) {
        return ImmutableLocation.of(new org.bukkit.Location(this.world.toHandle(), this.handle.getX() / x, this.handle.getY() / y, this.handle.getZ() / z));
    }

    @Override
    public ImmutableLocation divide(double x, double y, double z) {
        return ImmutableLocation.of(new org.bukkit.Location(this.world.toHandle(), this.handle.getX() / x, this.handle.getY() / y, this.handle.getZ() / z));
    }

    @Override
    public ImmutableLocation divide(int n) {
        return ImmutableLocation.of(new org.bukkit.Location(this.world.toHandle(), this.handle.getX() / n, this.handle.getY() / n, this.handle.getZ() / n));
    }

    @Override
    public ImmutableLocation divide(float n) {
        return ImmutableLocation.of(new org.bukkit.Location(this.world.toHandle(), this.handle.getX() / n, this.handle.getY() / n, this.handle.getZ() / n));
    }

    @Override
    public ImmutableLocation divide(double n) {
        return ImmutableLocation.of(new org.bukkit.Location(this.world.toHandle(), this.handle.getX() / n, this.handle.getY() / n, this.handle.getZ() / n));
    }

    @Override
    public Location clone() {
        return ImmutableLocation.of(this);
    }
    
    public MutableLocation toMutable() {
        return MutableLocation.of(this);
    }
    
    @Override
    public org.bukkit.Location toHandle() {
        return this.handle;
    }

    @Override
    public World getWorld() {
        return this.world;
    }
}