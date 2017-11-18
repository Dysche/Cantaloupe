package org.cantaloupe.statue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.block.BlockFace;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.entity.FakePlayer;
import org.cantaloupe.player.Player;
import org.cantaloupe.service.services.ScheduleService;
import org.cantaloupe.skin.Skin;
import org.cantaloupe.text.Text;
import org.cantaloupe.util.MathUtils;
import org.cantaloupe.world.World;
import org.cantaloupe.world.WorldObject;
import org.cantaloupe.world.location.ImmutableLocation;
import org.joml.Vector2f;
import org.joml.Vector3d;

public class PlayerStatue extends WorldObject {
    private ImmutableLocation  location    = null;
    private BlockFace          blockFace   = null;
    private UUID               uuid        = null;
    private Text               displayName = null, tabListName = null;
    private boolean            keepInTab   = false;
    private Skin               skin        = null;

    private FakePlayer         entity      = null;

    private final List<Player> players;

    private PlayerStatue(ImmutableLocation location, BlockFace blockFace, Text displayName, boolean keepInTab, Skin skin) {
        this.location = location;
        this.blockFace = blockFace;
        this.displayName = displayName;
        this.keepInTab = keepInTab;
        this.skin = skin;

        this.uuid = UUID.randomUUID();
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
        this.entity = FakePlayer.builder().location(this.location.add(0.5, 0, 0.5)).facing(this.blockFace).uuid(this.uuid).displayName(this.displayName).skin(this.skin).build();
    }

    public void placeFor(Player player) {
        if (!this.isPlacedFor(player)) {
            this.entity.addToTab(player);

            if (!this.keepInTab) {
                this.entity.setTabListName(Text.fromLegacy("&8[NPC]" + this.uuid.toString().substring(0, 16).replaceAll("-", "")), player);

                Cantaloupe.getServiceManager().provide(ScheduleService.class).delay(this.uuid.toString() + ":removeTabTask", new Runnable() {
                    @Override
                    public void run() {
                        entity.removeFromTab(player);
                    }
                }, 45);
            }

            this.entity.spawn(player);
            this.entity.setSkinFeaturesEnabled(true, player);
            
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

    public void addToTab() {
        this.entity.addToTab(this.players);
    }

    public void removeFromTab() {
        this.entity.removeFromTab(this.players);
    }

    public void setLocation(ImmutableLocation location) {
        this.entity.setLocation(this.players, ImmutableLocation.of(location.getWorld(), new Vector3d(location.getPosition().x + 0.5, location.getPosition().y, location.getPosition().z + 0.5), location.getRotation()));
        this.location = location;
    }

    public void setPosition(Vector3d position) {
        this.entity.setPosition(this.players, new Vector3d(position.x + 0.5, position.y, position.z + 0.5));
        this.location = ImmutableLocation.of(this.location.getWorld(), position, this.location.getRotation());
    }

    public void setRotation(Vector2f rotation) {
        this.entity.setRotation(this.players, rotation);

        this.location = ImmutableLocation.of(this.location.getWorld(), this.location.getPosition(), rotation);
        this.blockFace = MathUtils.rotationToFace(rotation);
    }

    public void setBlockFace(BlockFace blockFace) {
        this.entity.setBlockFace(this.players, blockFace);

        this.blockFace = blockFace;
        this.location = ImmutableLocation.of(this.location.getWorld(), this.location.getPosition(), new Vector2f(blockFace != BlockFace.UP && blockFace != BlockFace.DOWN ? MathUtils.faceToYaw(blockFace) : 0, blockFace == BlockFace.UP ? 90 : blockFace == BlockFace.DOWN ? -90 : 0));
    }

    public void setTabListName(Text tabListName) {
        this.entity.setTabListName(this.players, tabListName);
        this.tabListName = tabListName;
    }

    protected void onPlaced() {
        for (Player player : this.location.getWorld().getPlayers()) {
            this.tickFor(player);
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

    public Vector3d getPosition() {
        return this.location.getPosition();
    }

    public Vector2f getRotation() {
        return this.location.getRotation();
    }

    public BlockFace getBlockFace() {
        return this.blockFace;
    }

    public UUID getPlayerUUID() {
        return this.uuid;
    }

    public Text getDisplayName() {
        return this.displayName;
    }

    public Text getTabListName() {
        return this.tabListName;
    }

    public Skin getSkin() {
        return this.skin;
    }

    public FakePlayer getEntity() {
        return this.entity;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public static final class Builder {
        private ImmutableLocation location    = null;
        private BlockFace         blockFace   = null;
        private World             world       = null;
        private Vector3d          position    = null;
        private Vector2f          rotation    = null;
        private Text              displayName = null;
        private boolean           keepInTab   = false;
        private Skin              skin        = null;

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

        public Builder displayName(Text displayName) {
            this.displayName = displayName;

            return this;
        }

        public Builder keepInTab(boolean keepInTab) {
            this.keepInTab = keepInTab;

            return this;
        }

        public Builder skin(Skin skin) {
            this.skin = skin;

            return this;
        }

        public PlayerStatue build() {
            if (this.location == null) {
                if (this.rotation != null) {
                    this.location = ImmutableLocation.of(this.world, this.position, this.rotation);
                } else {
                    this.location = ImmutableLocation.of(this.world, this.position);
                }
            }

            PlayerStatue statue = new PlayerStatue(this.location, this.blockFace, this.displayName, this.keepInTab, this.skin);
            statue.create();

            return statue;
        }
    }
}