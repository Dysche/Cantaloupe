package org.cantaloupe.inventory;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.service.services.NMSService;
import org.cantaloupe.util.ReflectionHelper;

/**
 * An enum containing the possible item slots.
 * 
 * @author Dylan Scheltens
 *
 */
public enum EnumItemSlot {
    // Enum Values
    MAINHAND(0), FEET(1), LEGS(2), CHEST(3), HEAD(4), OFFHAND(5);

    // Enum Structure
    private final int slotID;

    private EnumItemSlot(int slotID) {
        this.slotID = slotID;
    }

    /**
     * Gets the slot ID.
     * 
     * @return The slot ID
     */
    public int getSlotID() {
        return this.slotID;
    }

    /**
     * Returns the NMS version of the enum.
     * 
     * @return The NMS enum
     */
    public Object toNMS() {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            return ReflectionHelper.getStaticField(this.name(), service.NMS_ENUM_ITEMSLOT_CLASS);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}