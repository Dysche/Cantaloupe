package org.cantaloupe.statue;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.BlockFace;
import org.cantaloupe.entity.EntityType;
import org.cantaloupe.entity.FakeEntity;
import org.cantaloupe.player.Player;
import org.cantaloupe.text.Text;
import org.cantaloupe.world.World;
import org.cantaloupe.world.WorldObject;
import org.cantaloupe.world.location.ImmutableLocation;
import org.joml.Vector2f;
import org.joml.Vector3d;

public class Statue extends WorldObject {
    private ImmutableLocation  location     = null;
    private BlockFace          blockFace    = null;
    private float              headRotation = 0f;
    private EntityType         entityType   = null;
    private FakeEntity         entity       = null;
    private Text               displayName  = null;
    private boolean            invisible    = false;

    private final List<Player> players;

    private Statue(ImmutableLocation location, BlockFace blockFace, float headRotation, EntityType entityType, Text displayName, boolean invisible) {
        this.location = location;
        this.blockFace = blockFace;
        this.headRotation = headRotation;
        this.entityType = entityType;
        this.displayName = displayName;
        this.invisible = invisible;

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
        this.entity = FakeEntity.builder()
                .type(this.entityType)
                .location(this.location.add(0.5, 0, 0.5))
                .facing(this.blockFace)
                .headRotation(this.headRotation)
                .customName(this.displayName)
                .customNameVisible(this.displayName != null ? true : false)
                .invisible(this.invisible)
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

    public void setHeadRotation(float headRotation) {
        this.entity.setHeadRotation(this.players, headRotation);
        this.headRotation = headRotation;
    }

    public void setDisplayName(Text displayName) {
        Text oldDisplayName = this.displayName;

        if (!displayName.toLegacy().equalsIgnoreCase(oldDisplayName.toLegacy())) {
            this.entity.setCustomName(this.players, displayName.toLegacy());
        }

        this.displayName = displayName;
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

    public boolean isInvisible() {
        return this.invisible;
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

    public float getHeadRotation() {
        return this.headRotation;
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    public Text getDisplayName() {
        return this.displayName;
    }

    public FakeEntity getEntity() {
        return this.entity;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public static final class Builder {
        private ImmutableLocation location     = null;
        private BlockFace         blockFace    = null;
        private World             world        = null;
        private Vector3d          position     = null;
        private Vector2f          rotation     = null;
        private float             headRotation = -1f;
        private EntityType        entityType   = null;
        private Text              displayName  = null;
        private boolean           invisible    = false;

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

        public Builder headRotation(float headRotation) {
            this.headRotation = headRotation;

            return this;
        }

        public Builder entityType(EntityType entityType) {
            this.entityType = entityType;

            return this;
        }

        public Builder displayName(Text displayName) {
            this.displayName = displayName;

            return this;
        }

        public Builder invisible(boolean invisible) {
            this.invisible = invisible;

            return this;
        }

        public Statue build() {
            if (this.location == null) {
                if (this.rotation != null) {
                    this.location = ImmutableLocation.of(this.world, this.position, this.rotation);
                } else {
                    this.location = ImmutableLocation.of(this.world, this.position);
                }
            }

            Statue statue = new Statue(this.location, this.blockFace, this.headRotation, this.entityType, this.displayName, this.invisible);
            statue.create();

            return statue;
        }
    }
}