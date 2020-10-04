/*
 * Decompiled with CFR 0.149.
 */
package cn.Judgment.util.utilities.angle;

import java.util.Random;

import cn.Judgment.util.utilities.vector.impl.Vector3;

public class AngleUtility {
    private static float minYawSmoothing;
    private static float maxYawSmoothing;
    private static float minPitchSmoothing;
    private static float maxPitchSmoothing;
    private static Vector3<Float> delta;
    private static Angle smoothedAngle;
    private static Random random;

    public AngleUtility(float minYawSmoothing, float maxYawSmoothing, float minPitchSmoothing, float maxPitchSmoothing) {
        AngleUtility.minYawSmoothing = minYawSmoothing;
        AngleUtility.maxYawSmoothing = maxYawSmoothing;
        AngleUtility.minPitchSmoothing = minPitchSmoothing;
        AngleUtility.maxPitchSmoothing = maxPitchSmoothing;
        random = new Random();
        delta = new Vector3<Float>(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(0.0f));
        smoothedAngle = new Angle(Float.valueOf(0.0f), Float.valueOf(0.0f));
    }

    public static float randomFloat(float min, float max) {
        return min + random.nextFloat() * (max - min);
    }

    public static Angle calculateAngle(Vector3<Double> destination, Vector3<Double> source) {
        Angle angles = new Angle(Float.valueOf(0.0f), Float.valueOf(0.0f));
        float height = 1.5f;
        delta.setX(Float.valueOf(((Number)destination.getX()).floatValue() - ((Number)source.getX()).floatValue())).setY(Float.valueOf(((Number)destination.getY()).floatValue() + height - (((Number)source.getY()).floatValue() + height))).setZ(Float.valueOf(((Number)destination.getZ()).floatValue() - ((Number)source.getZ()).floatValue()));
        double hypotenuse = Math.hypot(((Number)delta.getX()).doubleValue(), ((Number)delta.getZ()).doubleValue());
        float yawAtan = (float)Math.atan2(((Number)delta.getZ()).floatValue(), ((Number)delta.getX()).floatValue());
        float pitchAtan = (float)Math.atan2(((Number)delta.getY()).floatValue(), hypotenuse);
        float deg = 57.29578f;
        float yaw = yawAtan * deg - 90.0f;
        float pitch = -(pitchAtan * deg);
        return angles.setYaw(Float.valueOf(yaw)).setPitch(Float.valueOf(pitch)).constrantAngle();
    }

    public static Angle smoothAngle(Angle destination, Angle source, float i, float j) {
        return smoothedAngle.setYaw(Float.valueOf(source.getYaw().floatValue() - destination.getYaw().floatValue())).setPitch(Float.valueOf(source.getPitch().floatValue() - destination.getPitch().floatValue())).constrantAngle().setYaw(Float.valueOf(source.getYaw().floatValue() - smoothedAngle.getYaw().floatValue() / 100.0f * AngleUtility.randomFloat(minYawSmoothing, maxYawSmoothing))).setPitch(Float.valueOf(source.getPitch().floatValue() - smoothedAngle.getPitch().floatValue() / 100.0f * AngleUtility.randomFloat(minPitchSmoothing, maxPitchSmoothing))).constrantAngle();
    }
}

