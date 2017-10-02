package org.cantaloupe.world.location;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.world.World;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class MutableLocation implements Location {
    protected final World               world;
    protected final org.bukkit.Location handle;

    private MutableLocation(Location other) {
        this.handle = other.toHandle().clone();
        this.world = other.getWorld();
    }

    private MutableLocation(org.bukkit.Location handle) {
        this.handle = handle.clone();
        this.world = Cantaloupe.getWorldManager().getWorldFromHandle(handle.getWorld());
    }

    private MutableLocation(World world, Vector3d position) {
        this.handle = new org.bukkit.Location(world.toHandle(), position.x, position.y, position.z);
        this.world = world;
    }

    private MutableLocation(World world, Vector3f position) {
        this.handle = new org.bukkit.Location(world.toHandle(), position.x, position.y, position.z);
        this.world = world;
    }

    private MutableLocation(World world, Vector3i position) {
        this.handle = new org.bukkit.Location(world.toHandle(), position.x, position.y, position.z);
        this.world = world;
    }

    private MutableLocation(World world, Vector3d position, Vector2f rotation) {
        this.handle = new org.bukkit.Location(world.toHandle(), position.x, position.y, position.z, rotation.x, rotation.y);
        this.world = world;
    }

    private MutableLocation(World world, Vector3f position, Vector2f rotation) {
        this.handle = new org.bukkit.Location(world.toHandle(), position.x, position.y, position.z, rotation.x, rotation.y);
        this.world = world;
    }

    private MutableLocation(World world, Vector3i position, Vector2f rotation) {
        this.handle = new org.bukkit.Location(world.toHandle(), position.x, position.y, position.z, rotation.x, rotation.y);
        this.world = world;
    }

    public static MutableLocation of(Location other) {
        return new MutableLocation(other);
    }

    public static MutableLocation of(org.bukkit.Location handle) {
        return new MutableLocation(handle);
    }

    public static MutableLocation of(World world, Vector3d position) {
        return new MutableLocation(world, position);
    }

    public static MutableLocation of(World world, Vector3f position) {
        return new MutableLocation(world, position);
    }

    public static MutableLocation of(World world, Vector3i position) {
        return new MutableLocation(world, position);
    }

    public static MutableLocation of(World world, Vector3d position, Vector2f rotation) {
        return new MutableLocation(world, position, rotation);
    }

    public static MutableLocation of(World world, Vector3f position, Vector2f rotation) {
        return new MutableLocation(world, position, rotation);
    }

    public static MutableLocation of(World world, Vector3i position, Vector2f rotation) {
        return new MutableLocation(world, position, rotation);
    }

    @Override
    public MutableLocation add(Vector3i vector) {
        this.handle.add(vector.x, vector.y, vector.z);

        return this;
    }

    @Override
    public MutableLocation add(Vector3f vector) {
        this.handle.add(vector.x, vector.y, vector.z);

        return this;
    }

    @Override
    public MutableLocation add(Vector3d vector) {
        this.handle.add(vector.x, vector.y, vector.z);

        return this;
    }

    @Override
    public MutableLocation add(int x, int y, int z) {
        this.handle.add(x, y, z);

        return this;
    }

    @Override
    public MutableLocation add(float x, float y, float z) {
        this.handle.add(x, y, z);

        return this;
    }

    @Override
    public MutableLocation add(double x, double y, double z) {
        this.handle.add(x, y, z);

        return this;
    }

    @Override
    public MutableLocation add(int n) {
        this.handle.add(n, n, n);

        return this;
    }

    @Override
    public MutableLocation add(float n) {
        this.handle.add(n, n, n);

        return this;
    }

    @Override
    public MutableLocation add(double n) {
        this.handle.add(n, n, n);

        return this;
    }

    @Override
    public MutableLocation subtract(Vector3i vector) {
        this.handle.subtract(vector.x, vector.y, vector.z);

        return this;
    }

    @Override
    public MutableLocation subtract(Vector3f vector) {
        this.handle.subtract(vector.x, vector.y, vector.z);

        return this;
    }

    @Override
    public MutableLocation subtract(Vector3d vector) {
        this.handle.subtract(vector.x, vector.y, vector.z);

        return this;
    }

    @Override
    public MutableLocation subtract(int x, int y, int z) {
        this.handle.subtract(x, y, z);

        return this;
    }

    @Override
    public MutableLocation subtract(float x, float y, float z) {
        this.handle.subtract(x, y, z);

        return this;
    }

    @Override
    public MutableLocation subtract(double x, double y, double z) {
        this.handle.subtract(x, y, z);

        return this;
    }

    @Override
    public MutableLocation subtract(int n) {
        this.handle.subtract(n, n, n);

        return this;
    }

    @Override
    public MutableLocation subtract(float n) {
        this.handle.subtract(n, n, n);

        return this;
    }

    @Override
    public MutableLocation subtract(double n) {
        this.handle.subtract(n, n, n);

        return this;
    }

    @Override
    public MutableLocation mult(Vector3i vector) {
        this.handle.setX(this.handle.getX() * vector.x);
        this.handle.setX(this.handle.getY() * vector.y);
        this.handle.setX(this.handle.getZ() * vector.z);

        return this;
    }

    @Override
    public MutableLocation mult(Vector3f vector) {
        this.handle.setX(this.handle.getX() * vector.x);
        this.handle.setX(this.handle.getY() * vector.y);
        this.handle.setX(this.handle.getZ() * vector.z);

        return this;
    }

    @Override
    public MutableLocation mult(Vector3d vector) {
        this.handle.setX(this.handle.getX() * vector.x);
        this.handle.setX(this.handle.getY() * vector.y);
        this.handle.setX(this.handle.getZ() * vector.z);

        return this;
    }

    @Override
    public MutableLocation mult(int x, int y, int z) {
        this.handle.setX(this.handle.getX() * x);
        this.handle.setX(this.handle.getY() * y);
        this.handle.setX(this.handle.getZ() * z);

        return this;
    }

    @Override
    public MutableLocation mult(float x, float y, float z) {
        this.handle.setX(this.handle.getX() * x);
        this.handle.setX(this.handle.getY() * y);
        this.handle.setX(this.handle.getZ() * z);

        return this;
    }

    @Override
    public MutableLocation mult(double x, double y, double z) {
        this.handle.setX(this.handle.getX() * x);
        this.handle.setX(this.handle.getY() * y);
        this.handle.setX(this.handle.getZ() * z);

        return this;
    }

    @Override
    public MutableLocation mult(int n) {
        this.handle.setX(this.handle.getX() * n);
        this.handle.setX(this.handle.getY() * n);
        this.handle.setX(this.handle.getZ() * n);

        return this;
    }

    @Override
    public MutableLocation mult(float n) {
        this.handle.setX(this.handle.getX() * n);
        this.handle.setX(this.handle.getY() * n);
        this.handle.setX(this.handle.getZ() * n);

        return this;
    }

    @Override
    public MutableLocation mult(double n) {
        this.handle.setX(this.handle.getX() * n);
        this.handle.setX(this.handle.getY() * n);
        this.handle.setX(this.handle.getZ() * n);

        return this;
    }

    @Override
    public MutableLocation divide(Vector3i vector) {
        this.handle.setX(this.handle.getX() / vector.x);
        this.handle.setX(this.handle.getY() / vector.y);
        this.handle.setX(this.handle.getZ() / vector.z);

        return this;
    }

    @Override
    public MutableLocation divide(Vector3f vector) {
        this.handle.setX(this.handle.getX() / vector.x);
        this.handle.setX(this.handle.getY() / vector.y);
        this.handle.setX(this.handle.getZ() / vector.z);

        return this;
    }

    @Override
    public MutableLocation divide(Vector3d vector) {
        this.handle.setX(this.handle.getX() / vector.x);
        this.handle.setX(this.handle.getY() / vector.y);
        this.handle.setX(this.handle.getZ() / vector.z);

        return this;
    }

    @Override
    public MutableLocation divide(int x, int y, int z) {
        this.handle.setX(this.handle.getX() / x);
        this.handle.setX(this.handle.getY() / y);
        this.handle.setX(this.handle.getZ() / z);

        return this;
    }

    @Override
    public MutableLocation divide(float x, float y, float z) {
        this.handle.setX(this.handle.getX() / x);
        this.handle.setX(this.handle.getY() / y);
        this.handle.setX(this.handle.getZ() / z);

        return this;
    }

    @Override
    public MutableLocation divide(double x, double y, double z) {
        this.handle.setX(this.handle.getX() / x);
        this.handle.setX(this.handle.getY() / y);
        this.handle.setX(this.handle.getZ() / z);

        return this;
    }

    @Override
    public MutableLocation divide(int n) {
        this.handle.setX(this.handle.getX() / n);
        this.handle.setX(this.handle.getY() / n);
        this.handle.setX(this.handle.getZ() / n);

        return this;
    }

    @Override
    public MutableLocation divide(float n) {
        this.handle.setX(this.handle.getX() / n);
        this.handle.setX(this.handle.getY() / n);
        this.handle.setX(this.handle.getZ() / n);

        return this;
    }

    @Override
    public MutableLocation divide(double n) {
        this.handle.setX(this.handle.getX() / n);
        this.handle.setX(this.handle.getY() / n);
        this.handle.setX(this.handle.getZ() / n);

        return this;
    }

    @Override
    public Location clone() {
        return MutableLocation.of(this);
    }

    public ImmutableLocation toImmutable() {
        return ImmutableLocation.of(this);
    }

    @Override
    public org.bukkit.Location toHandle() {
        return this.handle;
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof MutableLocation)) {
            return false;
        }

        MutableLocation location = (MutableLocation) obj;
        if (!location.getPosition().equals(this.getPosition()) || !location.getRotation().equals(this.getRotation())) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "[X: " + this.getPosition().x + ", Y: " + this.getPosition().y + ", Z: " + this.getPosition().z + ", Yaw: " + this.getRotation().x + ", Pitch: " + this.getRotation().y + "]";
    }
}