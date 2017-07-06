package org.cantaloupe.statue;

import java.util.ArrayList;
import java.util.List;

import org.cantaloupe.data.DataContainer;
import org.cantaloupe.entity.FakeArmorStand;
import org.cantaloupe.entity.FakeEntity;
import org.cantaloupe.inventory.EnumItemSlot;
import org.cantaloupe.inventory.ItemStack;
import org.cantaloupe.player.Player;
import org.cantaloupe.text.Text;
import org.cantaloupe.world.World;
import org.cantaloupe.world.WorldObject;
import org.cantaloupe.world.location.ImmutableLocation;
import org.joml.Vector2f;
import org.joml.Vector3d;

public class ArmorStandStatue extends WorldObject {
    private ImmutableLocation                      location    = null;
    private FakeEntity                             entity      = null;
    private Text                                   displayName = null;
    private DataContainer<EnumItemSlot, ItemStack> equipment   = null;
    private boolean                                invisible   = false, small = false, basePlate = true, arms = false;

    private final List<Player>                     players;

    private ArmorStandStatue(ImmutableLocation location, DataContainer<EnumItemSlot, ItemStack> equipment, Text displayName, boolean invisible, boolean small, boolean basePlate, boolean arms) {
        this.location = location;
        this.equipment = equipment;
        this.displayName = displayName;
        this.invisible = invisible;
        this.small = small;
        this.basePlate = basePlate;
        this.arms = arms;

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
        this.entity = FakeArmorStand.builder()
                .world(this.location.getWorld())
                .position(new Vector3d(this.location.getPosition().x + 0.5, this.location.getPosition().y, this.location.getPosition().z + 0.5))
                .rotation(this.location.getRotation())
                .equipment(this.equipment)
                .customName(this.displayName)
                .customNameVisible(this.displayName != null ? true : false)
                .invisible(this.invisible)
                .small(this.small)
                .basePlate(this.basePlate)
                .arms(this.arms)
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

    public Text getDisplayName() {
        return this.displayName;
    }

    public static final class Builder {
        private ImmutableLocation                      location    = null;
        private World                                  world       = null;
        private Vector3d                               position    = null;
        private Vector2f                               rotation    = null;
        private Text                                   displayName = null;
        private boolean                                invisible   = false, small = false, basePlate = true, arms = false;
        private DataContainer<EnumItemSlot, ItemStack> equipment   = null;

        private Builder() {
            this.equipment = DataContainer.of();
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

        public Builder displayName(Text displayName) {
            this.displayName = displayName;

            return this;
        }

        public Builder invisible(boolean invisible) {
            this.invisible = invisible;

            return this;
        }

        public Builder small(boolean small) {
            this.small = small;

            return this;
        }

        public Builder basePlate(boolean basePlate) {
            this.basePlate = basePlate;

            return this;
        }

        public Builder arms(boolean arms) {
            this.arms = arms;

            return this;
        }

        public Builder equipment(DataContainer<EnumItemSlot, ItemStack> equipment) {
            this.equipment = equipment.clone();

            return this;
        }

        public Builder hand(ItemStack stack, boolean mainHand) {
            this.equipment.put(mainHand ? EnumItemSlot.MAINHAND : EnumItemSlot.OFFHAND, stack);

            return this;
        }

        public Builder helmet(ItemStack stack) {
            this.equipment.put(EnumItemSlot.HEAD, stack);

            return this;
        }

        public Builder chestplate(ItemStack stack) {
            this.equipment.put(EnumItemSlot.CHEST, stack);

            return this;
        }

        public Builder leggings(ItemStack stack) {
            this.equipment.put(EnumItemSlot.LEGS, stack);

            return this;
        }

        public Builder boots(ItemStack stack) {
            this.equipment.put(EnumItemSlot.FEET, stack);

            return this;
        }

        public ArmorStandStatue build() {
            if (this.location == null) {
                if (this.rotation != null) {
                    this.location = ImmutableLocation.of(this.world, this.position, this.rotation);
                } else {
                    this.location = ImmutableLocation.of(this.world, this.position);
                }
            }

            ArmorStandStatue statue = new ArmorStandStatue(this.location, this.equipment, this.displayName, this.invisible, this.small, this.basePlate, this.arms);
            statue.create();

            return statue;
        }
    }
}