package org.cantaloupe.statue;

import java.util.ArrayList;
import java.util.List;

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
    private ImmutableLocation  location    = null;
    private EntityType         entityType  = null;
    private FakeEntity         entity      = null;
    private Text               displayName = null;
    private boolean            invisible   = false;

    private final List<Player> players;

    private Statue(ImmutableLocation location, EntityType entityType, Text displayName, boolean invisible) {
        this.location = location;
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
                .world(this.location.getWorld())
                .position(new Vector3d(this.location.getPosition().x + 0.5, this.location.getPosition().y, this.location.getPosition().z + 0.5))
                .rotation(this.location.getRotation())
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
        if (player.getLocation().getPosition().distance(this.getLocation().getPosition()) <= 48) {
            this.placeFor(player);
        } else {
            this.removeFor(player);
        }
    }

    public void setPosition(Vector3d position) {
        this.setLocation(ImmutableLocation.of(this.location.getWorld(), position));
    }

    public void setLocation(ImmutableLocation location) {
        this.entity.setPosition(this.players, new Vector3d(location.getPosition().x + 0.5, location.getPosition().y, location.getPosition().z + 0.5));
        this.location = location;
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
            this.players.add(player);
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

    public EntityType getEntityType() {
        return this.entityType;
    }

    public Text getDisplayName() {
        return this.displayName;
    }

    public static final class Builder {
        private ImmutableLocation location    = null;
        private World             world       = null;
        private Vector3d          position    = null;
        private Vector2f          rotation    = null;
        private EntityType        entityType  = null;
        private Text              displayName = null;
        private boolean           invisible   = false;

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

            Statue statue = new Statue(this.location, this.entityType, this.displayName, this.invisible);
            statue.create();

            return statue;
        }
    }
}