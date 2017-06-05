package org.cantaloupe.math.color;

import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class MutableColorRGB implements ColorRGB {
    private Vector3f rgb = null;

    private MutableColorRGB(Vector3f rgb) {
        this.rgb = rgb;
    }

    public static MutableColorRGB of(ColorRGB rgb) {
        return new MutableColorRGB(new Vector3f(rgb.getR(), rgb.getG(), rgb.getB()));
    }

    public static MutableColorRGB of(Vector3i rgb) {
        return new MutableColorRGB(new Vector3f(rgb.x, rgb.y, rgb.z));
    }

    public static MutableColorRGB of(Vector3f rgb) {
        return new MutableColorRGB(new Vector3f(rgb));
    }

    public static MutableColorRGB of(Vector3d rgb) {
        return new MutableColorRGB(new Vector3f((float) rgb.x, (float) rgb.y, (float) rgb.z));
    }

    public static MutableColorRGB of(int r, int g, int b) {
        return new MutableColorRGB(new Vector3f(r, g, b));
    }

    public static MutableColorRGB of(float r, float g, float b) {
        return new MutableColorRGB(new Vector3f(r, g, b));
    }

    public static MutableColorRGB of(double r, double g, double b) {
        return new MutableColorRGB(new Vector3f((float) r, (float) g, (float) b));
    }

    public static MutableColorRGB of(int n) {
        return new MutableColorRGB(new Vector3f(n));
    }

    public static MutableColorRGB of(float n) {
        return new MutableColorRGB(new Vector3f(n));
    }

    public static MutableColorRGB of(double n) {
        return new MutableColorRGB(new Vector3f((float) n));
    }

    @Override
    public MutableColorRGB add(Vector3i vector) {
        return MutableColorRGB.of(this.rgb.x + vector.x, this.rgb.y + vector.y, this.rgb.z + vector.z);
    }

    @Override
    public MutableColorRGB add(Vector3f vector) {
        return MutableColorRGB.of(this.rgb.x + vector.x, this.rgb.y + vector.y, this.rgb.z + vector.z);
    }

    @Override
    public MutableColorRGB add(Vector3d vector) {
        return MutableColorRGB.of(this.rgb.x + vector.x, this.rgb.y + vector.y, this.rgb.z + vector.z);
    }

    @Override
    public MutableColorRGB add(int x, int y, int z) {
        return MutableColorRGB.of(this.rgb.x + x, this.rgb.y + y, this.rgb.z + z);
    }

    @Override
    public MutableColorRGB add(float x, float y, float z) {
        return MutableColorRGB.of(this.rgb.x + x, this.rgb.y + y, this.rgb.z + z);
    }

    @Override
    public MutableColorRGB add(double x, double y, double z) {
        return MutableColorRGB.of(this.rgb.x + x, this.rgb.y + y, this.rgb.z + z);
    }

    @Override
    public MutableColorRGB add(int n) {
        return MutableColorRGB.of(this.rgb.x + n, this.rgb.y + n, this.rgb.z + n);
    }

    @Override
    public MutableColorRGB add(float n) {
        return MutableColorRGB.of(this.rgb.x + n, this.rgb.y + n, this.rgb.z + n);
    }

    @Override
    public MutableColorRGB add(double n) {
        return MutableColorRGB.of(this.rgb.x + n, this.rgb.y + n, this.rgb.z + n);
    }

    @Override
    public MutableColorRGB subtract(Vector3i vector) {
        return MutableColorRGB.of(this.rgb.x - vector.x, this.rgb.y - vector.y, this.rgb.z - vector.z);
    }

    @Override
    public MutableColorRGB subtract(Vector3f vector) {
        return MutableColorRGB.of(this.rgb.x - vector.x, this.rgb.y - vector.y, this.rgb.z - vector.z);
    }

    @Override
    public MutableColorRGB subtract(Vector3d vector) {
        return MutableColorRGB.of(this.rgb.x - vector.x, this.rgb.y - vector.y, this.rgb.z - vector.z);
    }

    @Override
    public MutableColorRGB subtract(int x, int y, int z) {
        return MutableColorRGB.of(this.rgb.x - x, this.rgb.y - y, this.rgb.z - z);
    }

    @Override
    public MutableColorRGB subtract(float x, float y, float z) {
        return MutableColorRGB.of(this.rgb.x - x, this.rgb.y - y, this.rgb.z - z);
    }

    @Override
    public MutableColorRGB subtract(double x, double y, double z) {
        return MutableColorRGB.of(this.rgb.x - x, this.rgb.y - y, this.rgb.z - z);
    }

    @Override
    public MutableColorRGB subtract(int n) {
        return MutableColorRGB.of(this.rgb.x - n, this.rgb.y - n, this.rgb.z - n);
    }

    @Override
    public MutableColorRGB subtract(float n) {
        return MutableColorRGB.of(this.rgb.x - n, this.rgb.y - n, this.rgb.z - n);
    }

    @Override
    public MutableColorRGB subtract(double n) {
        return MutableColorRGB.of(this.rgb.x - n, this.rgb.y - n, this.rgb.z - n);
    }

    @Override
    public MutableColorRGB mult(Vector3i vector) {
        return MutableColorRGB.of(this.rgb.x * vector.x, this.rgb.y * vector.y, this.rgb.z * vector.z);
    }

    @Override
    public MutableColorRGB mult(Vector3f vector) {
        return MutableColorRGB.of(this.rgb.x * vector.x, this.rgb.y * vector.y, this.rgb.z * vector.z);
    }

    @Override
    public MutableColorRGB mult(Vector3d vector) {
        return MutableColorRGB.of(this.rgb.x * vector.x, this.rgb.y * vector.y, this.rgb.z * vector.z);
    }

    @Override
    public MutableColorRGB mult(int x, int y, int z) {
        return MutableColorRGB.of(this.rgb.x * x, this.rgb.y * y, this.rgb.z * z);
    }

    @Override
    public MutableColorRGB mult(float x, float y, float z) {
        return MutableColorRGB.of(this.rgb.x * x, this.rgb.y * y, this.rgb.z * z);
    }

    @Override
    public MutableColorRGB mult(double x, double y, double z) {
        return MutableColorRGB.of(this.rgb.x * x, this.rgb.y * y, this.rgb.z * z);
    }

    @Override
    public MutableColorRGB mult(int n) {
        return MutableColorRGB.of(this.rgb.x * n, this.rgb.y * n, this.rgb.z * n);
    }

    @Override
    public MutableColorRGB mult(float n) {
        return MutableColorRGB.of(this.rgb.x * n, this.rgb.y * n, this.rgb.z * n);
    }

    @Override
    public MutableColorRGB mult(double n) {
        return MutableColorRGB.of(this.rgb.x * n, this.rgb.y * n, this.rgb.z * n);
    }

    @Override
    public MutableColorRGB divide(Vector3i vector) {
        return MutableColorRGB.of(this.rgb.x / vector.x, this.rgb.y / vector.y, this.rgb.z / vector.z);
    }

    @Override
    public MutableColorRGB divide(Vector3f vector) {
        return MutableColorRGB.of(this.rgb.x / vector.x, this.rgb.y / vector.y, this.rgb.z / vector.z);
    }

    @Override
    public MutableColorRGB divide(Vector3d vector) {
        return MutableColorRGB.of(this.rgb.x / vector.x, this.rgb.y / vector.y, this.rgb.z / vector.z);
    }

    @Override
    public MutableColorRGB divide(int x, int y, int z) {
        return MutableColorRGB.of(this.rgb.x / x, this.rgb.y / y, this.rgb.z / z);
    }

    @Override
    public MutableColorRGB divide(float x, float y, float z) {
        return MutableColorRGB.of(this.rgb.x / x, this.rgb.y / y, this.rgb.z / z);
    }

    @Override
    public MutableColorRGB divide(double x, double y, double z) {
        return MutableColorRGB.of(this.rgb.x / x, this.rgb.y / y, this.rgb.z / z);
    }

    @Override
    public MutableColorRGB divide(int n) {
        return MutableColorRGB.of(this.rgb.x / n, this.rgb.y / n, this.rgb.z / n);
    }

    @Override
    public MutableColorRGB divide(float n) {
        return MutableColorRGB.of(this.rgb.x / n, this.rgb.y / n, this.rgb.z / n);
    }

    @Override
    public MutableColorRGB divide(double n) {
        return MutableColorRGB.of(this.rgb.x / n, this.rgb.y / n, this.rgb.z / n);
    }

    @Override
    public MutableColorRGB blend(ColorRGB other, BlendMode mode) {
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

                this.rgb.set(r, g, b);

                return this;
            case AVERAGE:
                r = (int) ((this.rgb.x + other.getR()) / 2.0);
                g = (int) ((this.rgb.y + other.getG()) / 2.0);
                b = (int) ((this.rgb.z + other.getB()) / 2.0);

                this.rgb.set(r, g, b);

                return this;
            default:
                return this;
        }
    }

    @Override
    public MutableColorRGB lerp(ColorRGB other, float n) {
        float r = this.rgb.x + (other.getR() - this.rgb.x) * n;
        float g = this.rgb.y + (other.getG() - this.rgb.y) * n;
        float b = this.rgb.z + (other.getB() - this.rgb.z) * n;

        return MutableColorRGB.of(r, g, b);
    }

    @Override
    public MutableColorRGB clone() {
        return MutableColorRGB.of(this);
    }

    public ImmutableColorRGB toImmutable() {
        return ImmutableColorRGB.of(this);
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