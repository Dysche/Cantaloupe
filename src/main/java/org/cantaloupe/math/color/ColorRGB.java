package org.cantaloupe.math.color;

import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

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
    
    public ColorRGB blend(ColorRGB other, BlendMode mode);
    
    public ColorRGB lerp(ColorRGB other, float n);
    
    public ColorRGB clone();
    
    public default Vector3f toVector() {
        return new Vector3f(this.getR(), this.getG(), this.getB());
    }
    
    public float getR();

    public float getG();

    public float getB();
}