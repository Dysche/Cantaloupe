package org.cantaloupe.inventory;

public enum EnumColor {
    // Enum Variables
    WHITE((short) 0),
    LIGHT_GRAY((short) 8),
    GRAY((short) 7),
    BLACK((short) 15),

    LIGHT_BLUE((short) 3),
    CYAN((short) 9),
    BLUE((short) 11),
    PURPLE((short) 10),
    MAGENTA((short) 2),
    PINK((short) 6),
    RED((short) 14),
    ORANGE((short) 1),
    YELLOW((short) 4),
    LIME((short) 5),
    GREEN((short) 13),
    BROWN((short) 12);

    // Enum Structure
    private final short color;

    EnumColor(short color) {
        this.color = color;
    }

    public short getColor() {
        return this.color;
    }

    public static EnumColor valueOf(short color) {
        for (EnumColor c : EnumColor.values()) {
            if (c.getColor() == color) {
                return c;
            }
        }

        return EnumColor.WHITE;
    }
}