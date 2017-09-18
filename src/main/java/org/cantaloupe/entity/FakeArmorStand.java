package org.cantaloupe.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.data.DataContainer;
import org.cantaloupe.inventory.EnumItemSlot;
import org.cantaloupe.inventory.ItemStack;
import org.cantaloupe.inventory.Skull;
import org.cantaloupe.player.Player;
import org.cantaloupe.service.services.NMSService;
import org.cantaloupe.service.services.PacketService;
import org.cantaloupe.text.Text;
import org.cantaloupe.util.ReflectionHelper;
import org.cantaloupe.world.World;
import org.cantaloupe.world.location.ImmutableLocation;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;

/**
 * A class used to create a "fake" armorstand entity.
 * 
 * @author Dylan Scheltens
 *
 */
public class FakeArmorStand extends FakeEntity {
    private DataContainer<EnumItemSlot, ItemStack> equipment = null;

    private FakeArmorStand(ImmutableLocation location, float headRotation, Vector3f headPose, DataContainer<EnumItemSlot, ItemStack> equipment, String customName, boolean customNameVisible, boolean invisible, boolean small, boolean basePlate, boolean arms) {
        super(EntityType.ARMOR_STAND, location, headRotation, customName, customNameVisible, invisible, true);

        this.equipment = equipment;
        this.create(headPose, small, basePlate, arms);
    }

    private void create(Vector3f headPose, boolean small, boolean basePlate, boolean arms) {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            ReflectionHelper.invokeMethod("setSmall", this.handle, new Class<?>[] {
                    boolean.class
            }, small);

            ReflectionHelper.invokeMethod("setBasePlate", this.handle, new Class<?>[] {
                    boolean.class
            }, !basePlate);

            ReflectionHelper.invokeMethod("setArms", this.handle, new Class<?>[] {
                    boolean.class
            }, arms);

            if (headPose != null) {
                ReflectionHelper.invokeMethod("setHeadPose", this.handle, new Class<?>[] {
                        service.NMS_VECTOR3F_CLASS
                }, service.getVector3f(headPose));
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void spawn(Collection<Player> players) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            Object spawnPacket = nmsService.NMS_PACKET_OUT_SPAWNENTITYLIVING_CLASS.getConstructor(nmsService.NMS_ENTITY_LIVING_CLASS).newInstance(this.handle);
            List<Object> equipPackets = new ArrayList<Object>();

            for (EnumItemSlot slot : this.equipment.keySet()) {
                if (nmsService.getIntVersion() < 8) {
                    equipPackets.add(nmsService.NMS_PACKET_OUT_ENTITYEQUIPMENT_CLASS.getConstructor(int.class, int.class, nmsService.NMS_ITEMSTACK_CLASS).newInstance(this.getEntityID(), slot.getSlotID(), this.equipment.get(slot).toNMS()));
                } else {
                    equipPackets.add(nmsService.NMS_PACKET_OUT_ENTITYEQUIPMENT_CLASS.getConstructor(int.class, nmsService.NMS_ENUM_ITEMSLOT_CLASS, nmsService.NMS_ITEMSTACK_CLASS).newInstance(this.getEntityID(), slot.toNMS(), this.equipment.get(slot).toNMS()));
                }
            }

            for (Player player : players) {
                packetService.sendPacket(player, spawnPacket);

                for (Object packet : equipPackets) {
                    packetService.sendPacket(player, packet);
                }
            }
        } catch (IllegalArgumentException | SecurityException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }

        this.updatePassenger(players, this.getPassenger());
    }

    /**
     * Creates and returns a new builder.
     * 
     * @return The builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Sets the head pose of the entity for a collection of players.
     * 
     * @param players
     *            The collection of players
     * @param headPose
     *            The head post
     */
    public void setHeadPose(Collection<Player> players, Vector3f headPose) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            ReflectionHelper.invokeMethod("setHeadPose", this.handle, new Class<?>[] {
                    nmsService.NMS_VECTOR3F_CLASS
            }, nmsService.getVector3f(headPose));

            // Object dataWatcher =
            // ReflectionHelper.getDeclaredField("datawatcher", this.handle);

            Object packet = nmsService.NMS_PACKET_OUT_ENTITYMETA_CLASS.getConstructor(int.class, nmsService.NMS_DATAWATCHER_CLASS, boolean.class).newInstance(this.getEntityID(), ReflectionHelper.getDeclaredField("datawatcher", this.handle), true);
            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static final class Builder extends FakeEntity.Builder {
        private Vector3f                               headPose  = null;
        private DataContainer<EnumItemSlot, ItemStack> equipment = null;
        private boolean                                small     = false, basePlate = true, arms = false;

        private Builder() {
            this.equipment = DataContainer.of();
        }

        /**
         * Sets the location of the builder.
         * 
         * @param location
         *            The location
         * @return The builder
         */
        @Override
        public Builder location(ImmutableLocation location) {
            this.location = location;

            return this;
        }

        /**
         * Sets the world of the builder.
         * 
         * @param world
         *            The world
         * @return The builder
         */
        @Override
        public Builder world(World world) {
            this.world = world;

            return this;
        }

        /**
         * Sets the position of the builder.
         * 
         * @param position
         *            The position
         * @return The builder
         */
        @Override
        public Builder position(Vector3d position) {
            this.position = position;

            return this;
        }

        /**
         * Sets the rotation of the builder.
         * 
         * @param rotation
         *            The rotation
         * @return The builder
         */
        @Override
        public Builder rotation(Vector2f rotation) {
            this.rotation = rotation;

            return this;
        }

        /**
         * Sets the head rotation of the builder.
         * 
         * @param headRotation
         *            The head rotation
         * @return The builder
         */
        @Override
        public Builder headRotation(float headRotation) {
            this.headRotation = headRotation;

            return this;
        }

        /**
         * Sets the head pose of the builder.
         * 
         * @param headPose
         *            The head pose
         * @return The builder
         */
        public Builder headPose(Vector3f headPose) {
            this.headPose = headPose;

            return this;
        }

        @Deprecated
        public Builder type(EntityType type) {
            this.type = type;

            return this;
        }

        /**
         * Sets the custom name of the builder.
         * 
         * @param customName
         *            The custom name
         * @return The builder
         */
        @Override
        public Builder customName(Text customName) {
            this.customName = customName;

            return this;
        }

        /**
         * Sets whether or not the builder's custom name is visible.
         * 
         * @param customNameVisible
         *            Whether or not the custom name is visible
         * @return The builder
         */
        @Override
        public Builder customNameVisible(boolean customNameVisible) {
            this.customNameVisible = customNameVisible;

            return this;
        }

        /**
         * Sets whether or not the builder is invisible.
         * 
         * @param invisible
         *            Whether or not the builder is invisible
         * @return The builder
         */
        @Override
        public Builder invisible(boolean invisible) {
            this.invisible = invisible;

            return this;
        }

        /**
         * Sets whether or not the builder is small.
         * 
         * @param small
         *            Whether or not the builder is small
         * @return The builder
         */
        public Builder small(boolean small) {
            this.small = small;

            return this;
        }

        /**
         * Sets whether or not the builder has a base plate.
         * 
         * @param basePlate
         *            Whether or not the builder has a base plate
         * @return The builder
         */
        public Builder basePlate(boolean basePlate) {
            this.basePlate = basePlate;

            return this;
        }

        /**
         * Sets whether or not the builder has arms.
         * 
         * @param arms
         *            Whether or not the builder has arms
         * @return The builder
         */
        public Builder arms(boolean arms) {
            this.arms = arms;

            return this;
        }

        /**
         * Sets the equipment of this builder.
         * 
         * @param equipment
         *            The equipment
         * @return The builder
         */
        public Builder equipment(DataContainer<EnumItemSlot, ItemStack> equipment) {
            this.equipment = equipment.clone();

            return this;
        }

        /**
         * Sets the item in the builder's hand.
         * 
         * @param stack
         *            The item stack
         * @param mainHand
         *            Whether or not to put the item in the main hand
         * @return The builder
         */
        public Builder hand(ItemStack stack, boolean mainHand) {
            this.equipment.put(mainHand ? EnumItemSlot.MAINHAND : EnumItemSlot.OFFHAND, stack);

            return this;
        }

        /**
         * Sets the helmet of the builder.
         * 
         * @param skull
         *            The skull
         * @return The builder
         */
        public Builder helmet(Skull skull) {
            return this.helmet(skull.toHandle());
        }

        /**
         * Sets the helmet of the builder.
         * 
         * @param stack
         *            The item stack
         * @return The builder
         */
        public Builder helmet(ItemStack stack) {
            this.equipment.put(EnumItemSlot.HEAD, stack);

            return this;
        }

        /**
         * Sets the chest plate of the builder.
         * 
         * @param stack
         *            The item stack
         * @return The builder
         */
        public Builder chestplate(ItemStack stack) {
            this.equipment.put(EnumItemSlot.CHEST, stack);

            return this;
        }

        /**
         * Sets the leggings of the builder.
         * 
         * @param stack
         *            The item stack
         * @return The builder
         */
        public Builder leggings(ItemStack stack) {
            this.equipment.put(EnumItemSlot.LEGS, stack);

            return this;
        }

        /**
         * Sets the boots of the builder.
         * 
         * @param stack
         *            The item stack
         * @return The builder
         */
        public Builder boots(ItemStack stack) {
            this.equipment.put(EnumItemSlot.FEET, stack);

            return this;
        }

        /**
         * Creates and returns a new entity from the builder.
         * 
         * @return The entity
         */
        public FakeArmorStand build() {
            if (this.location == null) {
                if (this.rotation != null) {
                    this.location = ImmutableLocation.of(this.world, this.position, this.rotation);
                } else {
                    this.location = ImmutableLocation.of(this.world, this.position);
                }
            }

            return new FakeArmorStand(this.location, this.headRotation, this.headPose, this.equipment, this.customName != null ? this.customName.toLegacy() : "", this.customNameVisible, this.invisible, this.small, this.basePlate, this.arms);
        }
    }
}