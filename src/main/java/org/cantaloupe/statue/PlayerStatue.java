package org.cantaloupe.statue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.entity.FakePlayer;
import org.cantaloupe.player.Player;
import org.cantaloupe.service.services.ScheduleService;
import org.cantaloupe.text.Text;
import org.cantaloupe.world.World;
import org.cantaloupe.world.WorldObject;
import org.cantaloupe.world.location.ImmutableLocation;
import org.joml.Vector3d;

public class PlayerStatue extends WorldObject {
    private ImmutableLocation  location   = null;
    private FakePlayer         entity     = null;
    private UUID               uuid       = null;
    private Text               name       = null;
    private boolean            keepInTab  = false;

    private final List<Player> players;

    private PlayerStatue(ImmutableLocation location, UUID uuid, Text name, boolean keepInTab) {
        this.location = location;
        this.uuid = uuid;
        this.name = name;
        this.keepInTab = keepInTab;

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
        this.entity = FakePlayer.builder()
                .world(this.location.getWorld())
                .position(new Vector3d(this.location.getPosition().x + 0.5, this.location.getPosition().y, this.location.getPosition().z + 0.5))
                .uuid(this.uuid)
                .name(this.name.toLegacy())
                .build();
    }

    public void placeFor(Player player) {
        if (!this.isPlacedFor(player)) {
            this.entity.addToTab(player);

            if (!this.keepInTab) {
                Cantaloupe.getServiceManager().provide(ScheduleService.class).delay(this.uuid.toString() + ":removeTabTask", new Runnable() {
                    @Override
                    public void run() {
                        entity.removeFromTab(player);
                    }
                });
            }

            this.entity.spawn(player);
            this.players.add(player);
        }
    }

    public void removeFor(Player player) {
        if (this.isPlacedFor(player)) {
            if (this.keepInTab) {
                this.entity.removeFromTab(player);
            }

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

    public void addToTab() {
        this.entity.addToTab(this.players);
    }

    public void removeFromTab() {
        this.entity.removeFromTab(this.players);
    }

    protected void onPlaced() {
        for (Player player : this.location.getWorld().getPlayers()) {
            this.tickFor(player);
            this.players.add(player);
        }
    }

    @Override
    protected void onRemoved() {
        this.entity.removeFromTab(this.players);

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

    public UUID getPlayerUUID() {
        return this.uuid;
    }

    public Text getName() {
        return this.name;
    }

    public static final class Builder {
        private Vector3d          position   = null;
        private World             world      = null;
        private ImmutableLocation location   = null;
        private UUID              uuid       = null;
        private Text              name       = null;
        private boolean           keepInTab  = false;

        private Builder() {

        }

        public Builder location(ImmutableLocation location) {
            this.location = location;

            return this;
        }

        public Builder position(Vector3d position) {
            this.position = position;

            return this;
        }

        public Builder world(World world) {
            this.world = world;

            return this;
        }

        public Builder uuid(String uuid) {
            this.uuid = UUID.fromString(uuid);

            return this;
        }

        public Builder uuid(UUID uuid) {
            this.uuid = uuid;

            return this;
        }

        public Builder name(Text name) {
            this.name = name;

            return this;
        }

        public Builder keepInTab(boolean keepInTab) {
            this.keepInTab = keepInTab;

            return this;
        }

        public PlayerStatue build() {
            if (this.location == null) {
                this.location = ImmutableLocation.of(this.world, this.position);
            }

            PlayerStatue statue = new PlayerStatue(this.location, this.uuid, this.name, this.keepInTab);
            statue.create();

            return statue;
        }
    }
}