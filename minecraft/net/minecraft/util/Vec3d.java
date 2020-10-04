/*
 * Decompiled with CFR 0.149.
 */
package net.minecraft.util;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

public class Vec3d {
    public final double xCoord;
    public final double yCoord;
    public final double zCoord;
    private static final String __OBFID = "CL_00000612";

    public static float sqrt(float p_76129_0_) {
        return (float)Math.sqrt(p_76129_0_);
    }

    public static float sqrt(double p_76133_0_) {
        return (float)Math.sqrt(p_76133_0_);
    }

    public Vec3d(double x, double y, double z) {
        if (x == -0.0) {
            x = 0.0;
        }
        if (y == -0.0) {
            y = 0.0;
        }
        if (z == -0.0) {
            z = 0.0;
        }
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
    }

    public Vec3d(Vec3i vector) {
        this(vector.getX(), vector.getY(), vector.getZ());
    }

    public Vec3d scale(double p_186678_1_) {
        return new Vec3d(this.xCoord * p_186678_1_, this.yCoord * p_186678_1_, this.zCoord * p_186678_1_);
    }

    public Vec3d subtractReverse(Vec3d vec) {
        return new Vec3d(vec.xCoord - this.xCoord, vec.yCoord - this.yCoord, vec.zCoord - this.zCoord);
    }

    public Vec3d normalize() {
        double var1 = Vec3d.sqrt(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
        return var1 < 1.0E-4 ? new Vec3d(0.0, 0.0, 0.0) : new Vec3d(this.xCoord / var1, this.yCoord / var1, this.zCoord / var1);
    }

    public double dotProduct(Vec3d vec) {
        return this.xCoord * vec.xCoord + this.yCoord * vec.yCoord + this.zCoord * vec.zCoord;
    }

    public Vec3d crossProduct(Vec3d vec) {
        return new Vec3d(this.yCoord * vec.zCoord - this.zCoord * vec.yCoord, this.zCoord * vec.xCoord - this.xCoord * vec.zCoord, this.xCoord * vec.yCoord - this.yCoord * vec.xCoord);
    }

    public Vec3d subtract(Vec3 vec3) {
        return this.subtract(vec3.xCoord, vec3.yCoord, vec3.zCoord);
    }

    public Vec3d subtract(double p_178786_1_, double p_178786_3_, double p_178786_5_) {
        return this.addVector(-p_178786_1_, -p_178786_3_, -p_178786_5_);
    }

    public Vec3d add(Vec3d p_178787_1_) {
        return this.addVector(p_178787_1_.xCoord, p_178787_1_.yCoord, p_178787_1_.zCoord);
    }

    public Vec3d addVector(double x, double y, double z) {
        return new Vec3d(this.xCoord + x, this.yCoord + y, this.zCoord + z);
    }

    public double distanceTo(Vec3 vec3) {
        double var2 = vec3.xCoord - this.xCoord;
        double var3 = vec3.yCoord - this.yCoord;
        double var4 = vec3.zCoord - this.zCoord;
        return Vec3d.sqrt(var2 * var2 + var3 * var3 + var4 * var4);
    }

    public double squareDistanceTo(Vec3d vec) {
        double var2 = vec.xCoord - this.xCoord;
        double var3 = vec.yCoord - this.yCoord;
        double var4 = vec.zCoord - this.zCoord;
        return var2 * var2 + var3 * var3 + var4 * var4;
    }

    public double lengthVector() {
        return Vec3d.sqrt(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
    }

    public Vec3d getIntermediateWithXValue(Vec3d vec, double x) {
        double var4 = vec.xCoord - this.xCoord;
        double var5 = vec.yCoord - this.yCoord;
        double var6 = vec.zCoord - this.zCoord;
        if (var4 * var4 < (double)1.0E-7f) {
            return null;
        }
        double var7 = (x - this.xCoord) / var4;
        return var7 >= 0.0 && var7 <= 1.0 ? new Vec3d(this.xCoord + var4 * var7, this.yCoord + var5 * var7, this.zCoord + var6 * var7) : null;
    }

    public Vec3d getIntermediateWithYValue(Vec3d vec, double y) {
        double var4 = vec.xCoord - this.xCoord;
        double var5 = vec.yCoord - this.yCoord;
        double var6 = vec.zCoord - this.zCoord;
        if (var5 * var5 < (double)1.0E-7f) {
            return null;
        }
        double var7 = (y - this.yCoord) / var5;
        return var7 >= 0.0 && var7 <= 1.0 ? new Vec3d(this.xCoord + var4 * var7, this.yCoord + var5 * var7, this.zCoord + var6 * var7) : null;
    }

    public Vec3d getIntermediateWithZValue(Vec3d vec, double z) {
        double var4 = vec.xCoord - this.xCoord;
        double var5 = vec.yCoord - this.yCoord;
        double var6 = vec.zCoord - this.zCoord;
        if (var6 * var6 < (double)1.0E-7f) {
            return null;
        }
        double var7 = (z - this.zCoord) / var6;
        return var7 >= 0.0 && var7 <= 1.0 ? new Vec3d(this.xCoord + var4 * var7, this.yCoord + var5 * var7, this.zCoord + var6 * var7) : null;
    }

    public String toString() {
        return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
    }

    public Vec3d rotatePitch(float p_178789_1_) {
        float var2 = MathHelper.cos(p_178789_1_);
        float var3 = MathHelper.sin(p_178789_1_);
        double var4 = this.xCoord;
        double var5 = this.yCoord * (double)var2 + this.zCoord * (double)var3;
        double var6 = this.zCoord * (double)var2 - this.yCoord * (double)var3;
        return new Vec3d(var4, var5, var6);
    }

    public Vec3d rotateYaw(float p_178785_1_) {
        float var2 = MathHelper.cos(p_178785_1_);
        float var3 = MathHelper.sin(p_178785_1_);
        double var4 = this.xCoord * (double)var2 + this.zCoord * (double)var3;
        double var5 = this.yCoord;
        double var6 = this.zCoord * (double)var2 - this.xCoord * (double)var3;
        return new Vec3d(var4, var5, var6);
    }
}

