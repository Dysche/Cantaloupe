package org.cantaloupe.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.data.DataContainer;
import org.cantaloupe.inventory.EnumItemSlot;
import org.cantaloupe.inventory.ItemStack;
import org.cantaloupe.player.Player;
import org.cantaloupe.service.services.NMSService;
import org.cantaloupe.service.services.PacketService;
import org.cantaloupe.text.Text;
import org.cantaloupe.util.ReflectionHelper;
import org.cantaloupe.world.World;
import org.cantaloupe.world.location.ImmutableLocation;
import org.joml.Vector2f;
import org.joml.Vector3d;

public class FakeArmorStand extends FakeEntity {
    private DataContainer<EnumItemSlot, ItemStack> equipment = null;

    private FakeArmorStand(ImmutableLocation location, DataContainer<EnumItemSlot, ItemStack> equipment, String customName, boolean customNameVisible, boolean invisible, boolean small, boolean basePlate, boolean arms) {
        super(EntityType.ARMOR_STAND, location, customName, customNameVisible, invisible, true);

        this.equipment = equipment;
        this.create(small, basePlate, arms);
    }

    private void create(boolean small, boolean basePlate, boolean arms) {
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
                    equipPackets.add(nmsService.NMS_PACKET_OUT_ENTITYEQUIPMENT_CLASS.getConstructor(int.class, int.class, nmsService.NMS_ITEMSTACK_CLASS).newInstance(this.getEntityID(), slot.getSlot(), this.equipment.get(slot).toNMS()));
                } else {
                    equipPackets
                            .add(nmsService.NMS_PACKET_OUT_ENTITYEQUIPMENT_CLASS.getConstructor(int.class, nmsService.NMS_ENUM_ITEMSLOT_CLASS, nmsService.NMS_ITEMSTACK_CLASS).newInstance(this.getEntityID(), slot.toNMS(), this.equipment.get(slot).toNMS()));
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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends FakeEntity.Builder {
        private DataContainer<EnumItemSlot, ItemStack> equipment = null;
        private boolean                                small     = false, basePlate = true, arms = false;

        private Builder() {
            this.equipment = DataContainer.of();
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

        public Builder location(ImmutableLocation location) {
            this.location = location;

            return this;
        }

        @Deprecated
        public Builder type(EntityType type) {
            this.type = type;

            return this;
        }

        public Builder customName(Text customName) {
            this.customName = customName;

            return this;
        }

        public Builder customNameVisible(boolean customNameVisible) {
            this.customNameVisible = customNameVisible;

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

        public FakeArmorStand build() {
            if (this.location == null) {
                if (this.rotation != null) {
                    this.location = ImmutableLocation.of(this.world, this.position, this.rotation);
                } else {
                    this.location = ImmutableLocation.of(this.world, this.position);
                }
            }

            return new FakeArmorStand(this.location, this.equipment, this.customName != null ? this.customName.toLegacy() : "", this.customNameVisible, this.invisible, this.small, this.basePlate, this.arms);
        }
    }
}