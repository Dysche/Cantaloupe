package org.cantaloupe.statue;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.cantaloupe.entity.FakeFallingBlock;
import org.cantaloupe.player.Player;
import org.cantaloupe.util.MathUtils;
import org.cantaloupe.world.World;
import org.cantaloupe.world.WorldObject;
import org.cantaloupe.world.location.ImmutableLocation;
import org.joml.Vector2f;
import org.joml.Vector3d;

public class FallingBlockStatue extends WorldObject {
    private ImmutableLocation  location  = null;
    private BlockFace          blockFace = null;
    private FakeFallingBlock   entity    = null;
    private Material           material  = null;
    private byte               data      = 0;

    private final List<Player> players;

    private FallingBlockStatue(ImmutableLocation location, BlockFace blockFace, Material material, byte data) {
        this.location = location;
        this.blockFace = blockFace;
        this.material = material;
        this.data = data;

        this.players = new ArrayList<Player>();
    }

    public static Builder builder() {
        return new Builder();
    }

    public void place() {
        this.location.getWorld().place(this);
    }

    public void remove() {
        this.location.getWorld().remove(this);
    }

    private void create() {
        this.entity = FakeFallingBlock.builder()
                .location(this.location.add(0.5, 0, 0.5))
                .facing(this.blockFace)
                .material(this.material)
                .data(this.data)
                .build();
    }

    public void placeFor(Player player) {
        if (!this.isPlacedFor(player)) {
            this.entity.spawn(player);
            this.players.add(player);
        }
    }

    public void removeFor(Player player) {
        if (this.isPlacedFor(player)) {
            this.entity.despawn(player);
            this.players.remove(player);
        }
    }

    @Override
    public void tickFor(Player player) {
        if (player.isDirty()) {
            this.removeFor(player);
        } else {
            if (player.getLocation().getPosition().distance(this.getLocation().getPosition()) <= 48 && player.getWorld() == this.getLocation().getWorld()) {
                this.placeFor(player);
            } else {
                this.removeFor(player);
            }
        }
    }

    public void setLocation(ImmutableLocation location) {
        this.entity.setLocation(this.players, ImmutableLocation.of(location.getWorld(), new Vector3d(location.getPosition().x + 0.5, location.getPosition().y, location.getPosition().z + 0.5), location.getRotation()));
        this.location = location;
    }

    public void setPosition(Vector3d position) {
        this.entity.setPosition(this.players, new Vector3d(position.x + 0.5, position.y, position.z + 0.5));
        this.location = ImmutableLocation.of(this.location.getWorld(), position);
    }

    public void setRotation(Vector2f rotation) {
        this.entity.setRotation(this.players, rotation);
        this.location = ImmutableLocation.of(this.location.getWorld(), this.location.getPosition(), rotation);
    }

    public void setBlockFace(BlockFace blockFace) {
        this.setRotation(new Vector2f(MathUtils.faceToYaw(blockFace), 0).add(180, 0));

        this.blockFace = blockFace;
    }

    public void setMaterial(Material material) {
        this.entity.setMaterial(this.players, material);
        this.material = material;
    }

    public void setData(byte data) {
        this.entity.setData(this.players, data);
        this.data = data;
    }

    protected void onPlaced() {
        for (Player player : this.location.getWorld().getPlayers()) {
            this.tickFor(player);
        }
    }

    @Override
    protected void onRemoved() {
        for (Player player : this.players) {
            this.entity.despawn(player);
        }

        this.players.clear();
    }

    public boolean isPlacedFor(Player player) {
        return this.players.contains(player);
    }

    @Override
    public ImmutableLocation getLocation() {
        return this.location;
    }

    public Vector3d getPosition() {
        return this.location.getPosition();
    }

    public Vector2f getRotation() {
        return this.location.getRotation();
    }

    public BlockFace getBlockFace() {
        return this.blockFace;
    }

    public Material getMaterial() {
        return this.material;
    }

    public byte getData() {
        return this.data;
    }

    public FakeFallingBlock getEntity() {
        return this.entity;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public static final class Builder {
        private ImmutableLocation location  = null;
        private BlockFace         blockFace = null;
        private World             world     = null;
        private Vector3d          position  = null;
        private Vector2f          rotation  = null;
        private Material          material  = null;
        private byte              data      = 0;

        private Builder() {

        }

        public Builder location(ImmutableLocation location) {
            this.location = location;

            return this;
        }

        public Builder world(World world) {
            this.world = world;

            return this;
        }

        public Builder position(Vector3d position) {
            this.position = position;

            return this;
        }

        public Builder rotation(Vector2f rotation) {
            this.rotation = rotation;

            return this;
        }

        public Builder facing(BlockFace blockFace) {
            this.blockFace = blockFace;
 
            return this;
        }

        public Builder material(Material material) {
            this.material = material;

            return this;
        }

        public Builder data(byte data) {
            this.data = data;

            return this;
        }

        public FallingBlockStatue build() {
            if (this.location == null) {
                if (this.rotation != null) {
                    this.location = ImmutableLocation.of(this.world, this.position, this.rotation);
                } else {
                    this.location = ImmutableLocation.of(this.world, this.position);
                }
            }

            FallingBlockStatue statue = new FallingBlockStatue(this.location, this.blockFace, this.material, this.data);
            statue.create();

            return statue;
        }
    }
}