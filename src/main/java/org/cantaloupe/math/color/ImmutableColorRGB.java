package org.cantaloupe.math.color;

import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

/**
 * An immutable RGB color class.
 * 
 * @author Dylan Scheltens
 *
 */
public class ImmutableColorRGB implements ColorRGB {
    private Vector3f rgb = null;

    private ImmutableColorRGB(Vector3f rgb) {
        this.rgb = rgb;
    }

    public static ImmutableColorRGB of(ColorRGB rgb) {
        return new ImmutableColorRGB(new Vector3f(rgb.getR(), rgb.getG(), rgb.getB()));
    }

    public static ImmutableColorRGB of(Vector3i rgb) {
        return new ImmutableColorRGB(new Vector3f(rgb.x, rgb.y, rgb.z));
    }

    public static ImmutableColorRGB of(Vector3f rgb) {
        return new ImmutableColorRGB(new Vector3f(rgb));
    }

    public static ImmutableColorRGB of(Vector3d rgb) {
        return new ImmutableColorRGB(new Vector3f((float) rgb.x, (float) rgb.y, (float) rgb.z));
    }

    public static ImmutableColorRGB of(int r, int g, int b) {
        return new ImmutableColorRGB(new Vector3f(r, g, b));
    }

    public static ImmutableColorRGB of(float r, float g, float b) {
        return new ImmutableColorRGB(new Vector3f(r, g, b));
    }

    public static ImmutableColorRGB of(double r, double g, double b) {
        return new ImmutableColorRGB(new Vector3f((float) r, (float) g, (float) b));
    }

    public static ImmutableColorRGB of(int n) {
        return new ImmutableColorRGB(new Vector3f(n));
    }

    public static ImmutableColorRGB of(float n) {
        return new ImmutableColorRGB(new Vector3f(n));
    }

    public static ImmutableColorRGB of(double n) {
        return new ImmutableColorRGB(new Vector3f((float) n));
    }

    @Override
    public ImmutableColorRGB add(Vector3i vector) {
        return ImmutableColorRGB.of(this.rgb.x + vector.x, this.rgb.y + vector.y, this.rgb.z + vector.z);
    }

    @Override
    public ImmutableColorRGB add(Vector3f vector) {
        return ImmutableColorRGB.of(this.rgb.x + vector.x, this.rgb.y + vector.y, this.rgb.z + vector.z);
    }

    @Override
    public ImmutableColorRGB add(Vector3d vector) {
        return ImmutableColorRGB.of(this.rgb.x + vector.x, this.rgb.y + vector.y, this.rgb.z + vector.z);
    }

    @Override
    public ImmutableColorRGB add(int x, int y, int z) {
        return ImmutableColorRGB.of(this.rgb.x + x, this.rgb.y + y, this.rgb.z + z);
    }

    @Override
    public ImmutableColorRGB add(float x, float y, float z) {
        return ImmutableColorRGB.of(this.rgb.x + x, this.rgb.y + y, this.rgb.z + z);
    }

    @Override
    public ImmutableColorRGB add(double x, double y, double z) {
        return ImmutableColorRGB.of(this.rgb.x + x, this.rgb.y + y, this.rgb.z + z);
    }

    @Override
    public ImmutableColorRGB add(int n) {
        return ImmutableColorRGB.of(this.rgb.x + n, this.rgb.y + n, this.rgb.z + n);
    }

    @Override
    public ImmutableColorRGB add(float n) {
        return ImmutableColorRGB.of(this.rgb.x + n, this.rgb.y + n, this.rgb.z + n);
    }

    @Override
    public ImmutableColorRGB add(double n) {
        return ImmutableColorRGB.of(this.rgb.x + n, this.rgb.y + n, this.rgb.z + n);
    }

    @Override
    public ImmutableColorRGB subtract(Vector3i vector) {
        return ImmutableColorRGB.of(this.rgb.x - vector.x, this.rgb.y - vector.y, this.rgb.z - vector.z);
    }

    @Override
    public ImmutableColorRGB subtract(Vector3f vector) {
        return ImmutableColorRGB.of(this.rgb.x - vector.x, this.rgb.y - vector.y, this.rgb.z - vector.z);
    }

    @Override
    public ImmutableColorRGB subtract(Vector3d vector) {
        return ImmutableColorRGB.of(this.rgb.x - vector.x, this.rgb.y - vector.y, this.rgb.z - vector.z);
    }

    @Override
    public ImmutableColorRGB subtract(int x, int y, int z) {
        return ImmutableColorRGB.of(this.rgb.x - x, this.rgb.y - y, this.rgb.z - z);
    }

    @Override
    public ImmutableColorRGB subtract(float x, float y, float z) {
        return ImmutableColorRGB.of(this.rgb.x - x, this.rgb.y - y, this.rgb.z - z);
    }

    @Override
    public ImmutableColorRGB subtract(double x, double y, double z) {
        return ImmutableColorRGB.of(this.rgb.x - x, this.rgb.y - y, this.rgb.z - z);
    }

    @Override
    public ImmutableColorRGB subtract(int n) {
        return ImmutableColorRGB.of(this.rgb.x - n, this.rgb.y - n, this.rgb.z - n);
    }

    @Override
    public ImmutableColorRGB subtract(float n) {
        return ImmutableColorRGB.of(this.rgb.x - n, this.rgb.y - n, this.rgb.z - n);
    }

    @Override
    public ImmutableColorRGB subtract(double n) {
        return ImmutableColorRGB.of(this.rgb.x - n, this.rgb.y - n, this.rgb.z - n);
    }

    @Override
    public ImmutableColorRGB mult(Vector3i vector) {
        return ImmutableColorRGB.of(this.rgb.x * vector.x, this.rgb.y * vector.y, this.rgb.z * vector.z);
    }

    @Override
    public ImmutableColorRGB mult(Vector3f vector) {
        return ImmutableColorRGB.of(this.rgb.x * vector.x, this.rgb.y * vector.y, this.rgb.z * vector.z);
    }

    @Override
    public ImmutableColorRGB mult(Vector3d vector) {
        return ImmutableColorRGB.of(this.rgb.x * vector.x, this.rgb.y * vector.y, this.rgb.z * vector.z);
    }

    @Override
    public ImmutableColorRGB mult(int x, int y, int z) {
        return ImmutableColorRGB.of(this.rgb.x * x, this.rgb.y * y, this.rgb.z * z);
    }

    @Override
    public ImmutableColorRGB mult(float x, float y, float z) {
        return ImmutableColorRGB.of(this.rgb.x * x, this.rgb.y * y, this.rgb.z * z);
    }

    @Override
    public ImmutableColorRGB mult(double x, double y, double z) {
        return ImmutableColorRGB.of(this.rgb.x * x, this.rgb.y * y, this.rgb.z * z);
    }

    @Override
    public ImmutableColorRGB mult(int n) {
        return ImmutableColorRGB.of(this.rgb.x * n, this.rgb.y * n, this.rgb.z * n);
    }

    @Override
    public ImmutableColorRGB mult(float n) {
        return ImmutableColorRGB.of(this.rgb.x * n, this.rgb.y * n, this.rgb.z * n);
    }

    @Override
    public ImmutableColorRGB mult(double n) {
        return ImmutableColorRGB.of(this.rgb.x * n, this.rgb.y * n, this.rgb.z * n);
    }

    @Override
    public ImmutableColorRGB divide(Vector3i vector) {
        return ImmutableColorRGB.of(this.rgb.x / vector.x, this.rgb.y / vector.y, this.rgb.z / vector.z);
    }

    @Override
    public ImmutableColorRGB divide(Vector3f vector) {
        return ImmutableColorRGB.of(this.rgb.x / vector.x, this.rgb.y / vector.y, this.rgb.z / vector.z);
    }

    @Override
    public ImmutableColorRGB divide(Vector3d vector) {
        return ImmutableColorRGB.of(this.rgb.x / vector.x, this.rgb.y / vector.y, this.rgb.z / vector.z);
    }

    @Override
    public ImmutableColorRGB divide(int x, int y, int z) {
        return ImmutableColorRGB.of(this.rgb.x / x, this.rgb.y / y, this.rgb.z / z);
    }

    @Override
    public ImmutableColorRGB divide(float x, float y, float z) {
        return ImmutableColorRGB.of(this.rgb.x / x, this.rgb.y / y, this.rgb.z / z);
    }

    @Override
    public ImmutableColorRGB divide(double x, double y, double z) {
        return ImmutableColorRGB.of(this.rgb.x / x, this.rgb.y / y, this.rgb.z / z);
    }

    @Override
    public ImmutableColorRGB divide(int n) {
        return ImmutableColorRGB.of(this.rgb.x / n, this.rgb.y / n, this.rgb.z / n);
    }

    @Override
    public ImmutableColorRGB divide(float n) {
        return ImmutableColorRGB.of(this.rgb.x / n, this.rgb.y / n, this.rgb.z / n);
    }

    @Override
    public ImmutableColorRGB divide(double n) {
        return ImmutableColorRGB.of(this.rgb.x / n, this.rgb.y / n, this.rgb.z / n);
    }

    @Override
    public ImmutableColorRGB blend(ColorRGB other, BlendMode mode) {
        float r = 0f;
        float g = 0f;
        float b = 0f;

        switch (mode) {
            case ADDITIVE:
                this.rgb.set((this.rgb.x * other.getR()) / 255f, (this.rgb.y * other.getG()) / 255f, (this.rgb.z * other.getB()) / 255f);
            case SUBTRACTIVE:
                r = (float) (255.0 - Math.sqrt((Math.pow((255.0 - this.rgb.x), 2.0) + Math.pow((255.0 - other.getR()), 2.0)) / 2.0));
                g = (float) (255.0 - Math.sqrt((Math.pow((255.0 - this.rgb.y), 2.0) + Math.pow((255.0 - other.getG()), 2.0)) / 2.0));
                b = (float) (255.0 - Math.sqrt((Math.pow((255.0 - this.rgb.z), 2.0) + Math.pow((255.0 - other.getB()), 2.0)) / 2.0));

                return ImmutableColorRGB.of(r, g, b);
            case AVERAGE:
                r = (int) ((this.rgb.x + other.getR()) / 2.0);
                g = (int) ((this.rgb.y + other.getG()) / 2.0);
                b = (int) ((this.rgb.z + other.getB()) / 2.0);

                return ImmutableColorRGB.of(r, g, b);
            default:
                return this;
        }
    }

    @Override
    public ImmutableColorRGB lerp(ColorRGB other, float n) {
        float r = this.rgb.x + (other.getR() - this.rgb.x) * n;
        float g = this.rgb.y + (other.getG() - this.rgb.y) * n;
        float b = this.rgb.z + (other.getB() - this.rgb.z) * n;

        return ImmutableColorRGB.of(r, g, b);
    }

    @Override
    public ImmutableColorRGB clone() {
        return ImmutableColorRGB.of(this);
    }

    public MutableColorRGB toMutable() {
        return MutableColorRGB.of(this);
    }

    @Override
    public float getR() {
        return this.rgb.x;
    }

    @Override
    public float getG() {
        return this.rgb.y;
    }

    @Override
    public float getB() {
        return this.rgb.z;
    }
}