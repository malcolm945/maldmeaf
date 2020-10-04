package cn.Judgment.util;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotationUtils {
    public static float[] getRotations(EntityLivingBase ent) {
        double x = ent.posX;
        double z = ent.posZ;
        double y = ent.posY + ent.getEyeHeight() / 2.0F;
        return getRotationFromPosition(x, z, y);
    }

    public static float[] getAverageRotations(List<EntityLivingBase> targetList) {
        double posX = 0.0D;
        double posY = 0.0D;
        double posZ = 0.0D;
        for (Entity ent : targetList) {
            posX += ent.posX;
            posY += ent.boundingBox.maxY - 2.0D;
            posZ += ent.posZ;
        }
        posX /= targetList.size();
        posY /= targetList.size();
        posZ /= targetList.size();
        return new float[]{getRotationFromPosition(posX, posZ, posY)[0], getRotationFromPosition(posX, posZ, posY)[1]};
    }

    public static float[] getBowAngles(final Entity entity) {
        final double xDelta = entity.posX - entity.lastTickPosX;
        final double zDelta = entity.posZ - entity.lastTickPosZ;
        double d = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
        d -= d % 0.8;
        double xMulti = 1.0;
        double zMulti = 1.0;
        final boolean sprint = entity.isSprinting();
        xMulti = d / 0.8 * xDelta * (sprint ? 1.25 : 1.0);
        zMulti = d / 0.8 * zDelta * (sprint ? 1.25 : 1.0);
        final double x = entity.posX + xMulti - Minecraft.getMinecraft().thePlayer.posX;
        final double z = entity.posZ + zMulti - Minecraft.getMinecraft().thePlayer.posZ;
        final double y = Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight() - (entity.posY + entity.getEyeHeight());
        final double dist = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
        final float yaw = (float) Math.toDegrees(Math.atan2(z, x)) - 90.0f;
        final float pitch = (float) Math.toDegrees(Math.atan2(y, dist));
        return new float[]{yaw, pitch};
    }

    private static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 0.6D;
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / Math.PI);
        return new float[]{yaw, pitch};
    }

    public static float getTrajAngleSolutionLow(float d3, float d1, float velocity) {
        float g = 0.006F;
        float sqrt = velocity * velocity * velocity * velocity - g * (g * (d3 * d3) + 2.0F * d1 * (velocity * velocity));
        return (float) Math.toDegrees(Math.atan((velocity * velocity - Math.sqrt(sqrt)) / (g * d3)));
    }

    public static float getYawChange(double posX, double posZ) {
        double deltaX = posX - Minecraft.getMinecraft().thePlayer.posX;
        double deltaZ = posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double yawToEntity;
        if ((deltaZ < 0.0D) && (deltaX < 0.0D)) {
            yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
            yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else {
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }
        return MathHelper.wrapAngleTo180_float(-(Minecraft.getMinecraft().thePlayer.rotationYaw - (float) yawToEntity));
    }

    public static float getPitchChange(Entity entity, double posY) {
        double deltaX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
        double deltaZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double deltaY = posY - 2.2D + entity.getEyeHeight() - Minecraft.getMinecraft().thePlayer.posY;
        double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return -MathHelper.wrapAngleTo180_float(Minecraft.getMinecraft().thePlayer.rotationPitch - (float) pitchToEntity) - 2.5F;
    }

    public static float getNewAngle(float angle) {
        angle %= 360.0F;
        if (angle >= 180.0F) {
            angle -= 360.0F;
        }
        if (angle < -180.0F) {
            angle += 360.0F;
        }
        return angle;
    }

    public static float getDistanceBetweenAngles(float angle1, float angle2) {
        float angle = Math.abs(angle1 - angle2) % 360.0F;
        if (angle > 180.0F) {
            angle = 360.0F - angle;
        }
        return angle;
    }

	public static float[] grabBlockRotations(BlockPos position) {
		// TODO Auto-generated method stub
		return null;
	}
	 public static Vec3 getEyesPos() {
	        return new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ);
	    }
	public static float[] getNeededRotations(Vec3 vec) {
        Vec3 eyesPos = RotationUtils.getEyesPos();
        double diffX = vec.xCoord - eyesPos.xCoord + 0.5;
        double diffY = vec.yCoord - eyesPos.yCoord + 0.5;
        double diffZ = vec.zCoord - eyesPos.zCoord + 0.5;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)((- Math.atan2(diffY, diffXZ)) * 180.0 / 3.141592653589793);
        float[] arrf = new float[]{MathHelper.wrapAngleTo180_float(yaw), Minecraft.getMinecraft().gameSettings.keyBindJump.pressed ? 90.0f : MathHelper.wrapAngleTo180_float(pitch)};
        return arrf;
    }

	public static void faceVectorPacketInstant(Vec3 vec) {
        float[] rotations = RotationUtils.getNeededRotations(vec);
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(rotations[0], rotations[1], Minecraft.thePlayer.onGround));
    }
}

