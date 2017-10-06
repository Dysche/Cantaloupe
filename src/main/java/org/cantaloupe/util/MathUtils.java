package org.cantaloupe.util;

import org.bukkit.block.BlockFace;

import io.netty.util.internal.ThreadLocalRandom;

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

    public static boolean randomBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    public static int randomInt(int n) {
        return ThreadLocalRandom.current().nextInt(n);
    }

    public static int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static long randomLong(long n) {
        return ThreadLocalRandom.current().nextLong(n);
    }

    public static long randomLong(long min, long max) {
        return ThreadLocalRandom.current().nextLong(min, max);
    }

    public static float randomFloat(float n) {
        return ThreadLocalRandom.current().nextFloat() * n;
    }

    public static float randomFloat(float min, float max) {
        return min + (ThreadLocalRandom.current().nextFloat() * (max - min));
    }

    public static double randomDouble(double n) {
        return ThreadLocalRandom.current().nextDouble(n);
    }

    public static double randomDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
}