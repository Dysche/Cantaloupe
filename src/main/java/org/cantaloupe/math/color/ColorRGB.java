package org.cantaloupe.math.color;

import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

/**
 * A RGB color class.
 * 
 * @author Dylan Scheltens
 *
 */
public interface ColorRGB {
    public ColorRGB add(Vector3i vector);

    public ColorRGB add(Vector3f vector);

    public ColorRGB add(Vector3d vector);

    public ColorRGB add(int x, int y, int z);

    public ColorRGB add(float x, float y, float z);

    public ColorRGB add(double x, double y, double z);

    public ColorRGB add(int n);

    public ColorRGB add(float n);

    public ColorRGB add(double n);

    public ColorRGB subtract(Vector3i vector);

    public ColorRGB subtract(Vector3f vector);

    public ColorRGB subtract(Vector3d vector);

    public ColorRGB subtract(int x, int y, int z);

    public ColorRGB subtract(float x, float y, float z);

    public ColorRGB subtract(double x, double y, double z);

    public ColorRGB subtract(int n);

    public ColorRGB subtract(float n);

    public ColorRGB subtract(double n);

    public ColorRGB mult(Vector3i vector);

    public ColorRGB mult(Vector3f vector);

    public ColorRGB mult(Vector3d vector);

    public ColorRGB mult(int x, int y, int z);

    public ColorRGB mult(float x, float y, float z);

    public ColorRGB mult(double x, double y, double z);

    public ColorRGB mult(int n);

    public ColorRGB mult(float n);

    public ColorRGB mult(double n);

    public ColorRGB divide(Vector3i vector);

    public ColorRGB divide(Vector3f vector);

    public ColorRGB divide(Vector3d vector);

    public ColorRGB divide(int x, int y, int z);

    public ColorRGB divide(float x, float y, float z);

    public ColorRGB divide(double x, double y, double z);

    public ColorRGB divide(int n);

    public ColorRGB divide(float n);

    public ColorRGB divide(double n);

    /**
     * Blends two colors together.
     * 
     * @param other
     *            The other color
     * @param mode
     *            The blend mode
     * @return The blended color
     */
    public ColorRGB blend(ColorRGB other, BlendMode mode);

    /**
     * Linear-interpolates two colors.
     * 
     * @param other
     *            The other color
     * @param n
     *            The alpha
     * @return The lerped color
     */
    public ColorRGB lerp(ColorRGB other, float n);

    /**
     * Clones the color.
     * 
     * @return The cloned color
     */
    public ColorRGB clone();

    /**
     * Returns a vector containing the color values.
     * 
     * @return The vector
     */
    public default Vector3f toVector() {
        return new Vector3f(this.getR(), this.getG(), this.getB());
    }

    /**
     * Gets the red value of the color.
     * 
     * @return The red value
     */
    public float getR();

    /**
     * Gets the green value of the color.
     * 
     * @return The green value
     */
    public float getG();

    /**
     * Gets the blue value of the color.
     * 
     * @return The blue value
     */
    public float getB();
}