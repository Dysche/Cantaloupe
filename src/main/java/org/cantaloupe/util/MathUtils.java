package org.cantaloupe.util;

import org.bukkit.block.BlockFace;

public class MathUtils {
    private static final BlockFace[] axis   = {
            BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST
    };

    private static final BlockFace[] radial = {
            BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST
    };

    public static BlockFace yawToFaceRadial(float yaw) {
        return radial[Math.round(yaw / 45f) & 0x7];
    }

    public static BlockFace yawToFace(float yaw) {
        return axis[Math.round(yaw / 90f) & 0x3];
    }

    public static BlockFace yawToFace(float yaw, float pitch) {
        if (pitch < -80) {
            return BlockFace.UP;
        } else if (pitch > 80) {
            return BlockFace.DOWN;
        }

        return axis[Math.round(yaw / 90f) & 0x3];
    }

    public static float faceToYaw(BlockFace blockFace) {
        switch (blockFace) {
            case NORTH:
                return 0;
            case NORTH_EAST:
                return 45;
            case EAST:
                return 90;
            case SOUTH_EAST:
                return 135;
            case SOUTH:
                return 180;
            case SOUTH_WEST:
                return 225;
            case WEST:
                return 270;
            case NORTH_WEST:
                return 315;
            default:
                return 0;
        }
    }
}
