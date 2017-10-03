package org.cantaloupe.statue;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.BlockFace;
import org.cantaloupe.data.DataContainer;
import org.cantaloupe.entity.FakeArmorStand;
import org.cantaloupe.inventory.EnumItemSlot;
import org.cantaloupe.inventory.ItemStack;
import org.cantaloupe.inventory.Skull;
import org.cantaloupe.player.Player;
import org.cantaloupe.text.Text;
import org.cantaloupe.util.MathUtils;
import org.cantaloupe.world.World;
import org.cantaloupe.world.WorldObject;
import org.cantaloupe.world.location.ImmutableLocation;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class ArmorStandStatue extends WorldObject {
    private ImmutableLocation                      location     = null;
    private BlockFace                              blockFace    = null;
    private float                                  headRotation = 0f;
    private Vector3f                               headPose     = null, bodyPose = null, leftArmPose = null, rightArmPose = null, leftLegPose = null, rightLegPose = null;
    private FakeArmorStand                         entity       = null;
    private Text                                   displayName  = null;
    private DataContainer<EnumItemSlot, ItemStack> equipment    = null;
    private boolean                                invisible    = false, small = false, basePlate = true, arms = false;

    private final List<Player>                     players;

    private ArmorStandStatue(ImmutableLocation location, BlockFace blockFace, float headRotation, Vector3f headPose, Vector3f bodyPose, Vector3f leftArmPose, Vector3f rightArmPose, Vector3f leftLegPose, Vector3f rightLegPose, DataContainer<EnumItemSlot, ItemStack> equipment, Text displayName, boolean invisible,
            boolean small, boolean basePlate, boolean arms) {
        this.location = location;
        this.blockFace = blockFace;
        this.headRotation = headRotation;
        this.headPose = headPose;
        this.bodyPose = bodyPose;
        this.leftArmPose = leftArmPose;
        this.rightArmPose = rightArmPose;
        this.leftLegPose = leftLegPose;
        this.rightLegPose = rightLegPose;
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
                .location(this.location.add(0.5, 0, 0.5))
                .facing(this.blockFace)
                .headRotation(this.headRotation)
                .headPose(this.headPose)
                .bodyPose(this.bodyPose)
                .leftArmPose(this.leftArmPose)
                .rightArmPose(this.rightArmPose)
                .leftLegPose(this.leftLegPose)
                .rightLegPose(this.rightLegPose)
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

    public void setHeadRotation(float headRotation) {
        this.entity.setHeadRotation(this.players, headRotation);
        this.headRotation = headRotation;
    }

    public void setHeadPose(Vector3f headPose) {
        this.entity.setHeadPose(this.players, headPose);
        this.headPose = new Vector3f(headPose.x, headPose.y, headPose.z);
    }

    public void setBodyPose(Vector3f bodyPose) {
        this.entity.setBodyPose(this.players, bodyPose);
        this.bodyPose = new Vector3f(bodyPose.x, bodyPose.y, bodyPose.z);
    }

    public void setLeftArmPose(Vector3f leftArmPose) {
        this.entity.setLeftArmPose(this.players, leftArmPose);
        this.leftArmPose = new Vector3f(leftArmPose.x, leftArmPose.y, leftArmPose.z);
    }

    public void setRightArmPose(Vector3f rightArmPose) {
        this.entity.setRightArmPose(this.players, rightArmPose);
        this.rightArmPose = new Vector3f(rightArmPose.x, rightArmPose.y, rightArmPose.z);
    }

    public void setLeftLegPose(Vector3f leftLegPose) {
        this.entity.setLeftLegPose(this.players, leftLegPose);
        this.leftLegPose = new Vector3f(leftLegPose.x, leftLegPose.y, leftLegPose.z);
    }

    public void setRightLegPose(Vector3f rightLegPose) {
        this.entity.setRightLegPose(this.players, rightLegPose);
        this.rightLegPose = new Vector3f(rightLegPose.x, rightLegPose.y, rightLegPose.z);
    }

    public void setDisplayName(Text displayName) {
        Text oldDisplayName = this.displayName;

        if (!displayName.toLegacy().equalsIgnoreCase(oldDisplayName.toLegacy())) {
            this.entity.setCustomName(this.players, displayName.toLegacy());
        }

        this.displayName = displayName;
    }

    public void setInvisible(boolean invisible) {
        this.entity.setInvisible(this.players, invisible);
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

    public Vector3f getHeadPose() {
        return new Vector3f(this.headPose.x, this.headPose.y, this.headPose.z);
    }

    public Vector3f getBodyPose() {
        return new Vector3f(this.bodyPose.x, this.bodyPose.y, this.bodyPose.z);
    }

    public Vector3f getLeftArmPose() {
        return new Vector3f(this.leftArmPose.x, this.leftArmPose.y, this.leftArmPose.z);
    }

    public Vector3f getRightArmPose() {
        return new Vector3f(this.rightArmPose.x, this.rightArmPose.y, this.rightArmPose.z);
    }

    public Vector3f getLeftLegPose() {
        return new Vector3f(this.leftLegPose.x, this.leftLegPose.y, this.leftLegPose.z);
    }

    public Vector3f getRightLegPose() {
        return new Vector3f(this.rightLegPose.x, this.rightLegPose.y, this.rightLegPose.z);
    }

    public Text getDisplayName() {
        return this.displayName;
    }

    public DataContainer<EnumItemSlot, ItemStack> getEquipment() {
        return this.equipment.clone();
    }

    public ItemStack getItemInMainHand() {
        return this.equipment.get(EnumItemSlot.MAINHAND);
    }

    public ItemStack getItemInOffHand() {
        return this.equipment.get(EnumItemSlot.OFFHAND);
    }

    public ItemStack getHelmet() {
        return this.equipment.get(EnumItemSlot.HEAD);
    }

    public ItemStack getChestPlate() {
        return this.equipment.get(EnumItemSlot.CHEST);
    }

    public ItemStack getLeggings() {
        return this.equipment.get(EnumItemSlot.LEGS);
    }

    public ItemStack getBoots() {
        return this.equipment.get(EnumItemSlot.FEET);
    }

    public FakeArmorStand getEntity() {
        return this.entity;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public static final class Builder {
        private ImmutableLocation                      location     = null;
        private BlockFace                              blockFace    = null;
        private World                                  world        = null;
        private Vector3d                               position     = null;
        private Vector2f                               rotation     = null;
        private float                                  headRotation = -1f;
        private Vector3f                               headPose     = null, bodyPose = null, leftArmPose = null, rightArmPose = null, leftLegPose = null, rightLegPose = null;
        private Text                                   displayName  = null;
        private boolean                                invisible    = false, small = false, basePlate = true, arms = false;
        private DataContainer<EnumItemSlot, ItemStack> equipment    = null;

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

        public Builder facing(BlockFace blockFace) {
            this.blockFace = blockFace;

            return this;
        }

        public Builder headRotation(float headRotation) {
            this.headRotation = headRotation;

            return this;
        }

        public Builder headPose(Vector3f headPose) {
            this.headPose = headPose;

            return this;
        }

        public Builder bodyPose(Vector3f bodyPose) {
            this.bodyPose = bodyPose;

            return this;
        }

        public Builder leftArmPose(Vector3f leftArmPose) {
            this.leftArmPose = leftArmPose;

            return this;
        }

        public Builder rightArmPose(Vector3f rightArmPose) {
            this.rightArmPose = rightArmPose;

            return this;
        }

        public Builder leftLegPose(Vector3f leftLegPose) {
            this.leftLegPose = leftLegPose;

            return this;
        }

        public Builder rightLegPose(Vector3f rightLegPose) {
            this.rightLegPose = rightLegPose;

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

        public Builder helmet(Skull skull) {
            return this.helmet(skull.toHandle());
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

            ArmorStandStatue statue = new ArmorStandStatue(this.location, this.blockFace, this.headRotation, this.headPose, this.bodyPose, this.leftArmPose, this.rightArmPose, this.leftLegPose, this.rightLegPose, this.equipment, this.displayName, this.invisible, this.small, this.basePlate, this.arms);
            statue.create();

            return statue;
        }
    }
}