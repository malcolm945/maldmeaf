package cn.Judgment.mod.mods.COMBAT;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3d;

class RotUtil {
   static Minecraft mc = Minecraft.getMinecraft();
   public static Random rnd = new Random();
   public static float[] rot;

   static {
      rnd.setSeed(133789232L);
   }

   public static float getYawChangeGiven(double posX, double posZ, float yaw) {
      Minecraft.getMinecraft();
      double deltaX = posX - Minecraft.thePlayer.posX;
      Minecraft.getMinecraft();
      double deltaZ = posZ - Minecraft.thePlayer.posZ;
      double yawToEntity;
      if(deltaZ < 0.0D && deltaX < 0.0D) {
         yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
      } else if(deltaZ < 0.0D && deltaX > 0.0D) {
         yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
      } else {
         yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
      }

      return MathHelper.wrapAngleTo180_float(-(yaw - (float)yawToEntity));
   }

   public static float[] getRotations(EntityLivingBase ent) {
      double x = ent.posX + (ent.posX - ent.lastTickPosX);
      double z = ent.posZ + (ent.posZ - ent.lastTickPosZ);
      double y = ent.posY + (double)(ent.getEyeHeight() / 2.1F);
      return getRotationFromPosition(x, z, y);
   }

   public static float[] getPredictedRotations(EntityLivingBase ent) {
      double x = ent.posX + (ent.posX - ent.lastTickPosX);
      double z = ent.posZ + (ent.posZ - ent.lastTickPosZ);
      double y = ent.posY + (double)(ent.getEyeHeight() / 2.0F);
      return getRotationFromPosition(x, z, y);
   }

   public static Vec3d getLook() {
      return func_174806_f(rot[1], rot[0]);
   }

   public static final Vec3d func_174806_f(float p_174806_1_, float p_174806_2_) {
      float var3 = MathHelper.cos(-p_174806_2_ * 0.017453292F - 3.1415927F);
      float var4 = MathHelper.sin(-p_174806_2_ * 0.017453292F - 3.1415927F);
      float var5 = -MathHelper.cos(-p_174806_1_ * 0.017453292F);
      float var6 = MathHelper.sin(-p_174806_1_ * 0.017453292F);
      return new Vec3d((double)(var4 * var5), (double)var6, (double)(var3 * var5));
   }

   public static Vec3d func_174824_e() {
      return new Vec3d(Minecraft.getMinecraft().renderViewEntity.posX, Minecraft.getMinecraft().renderViewEntity.posY + (double)Minecraft.getMinecraft().renderViewEntity.getEyeHeight(), Minecraft.getMinecraft().renderViewEntity.posZ);
   }

   public static boolean isRotationIn(float[] rotation, AxisAlignedBB box) {
      float[] maxRotations = getMaxRotations(box);
      return maxRotations[0] < rotation[0] && maxRotations[2] < rotation[1] && maxRotations[1] > rotation[0] && maxRotations[3] > rotation[1];
   }

   public static Vec3d[] getCorners(AxisAlignedBB box) {
      return new Vec3d[]{new Vec3d(box.minX, box.minY, box.minZ), new Vec3d(box.maxX, box.minY, box.minZ), new Vec3d(box.minX, box.maxY, box.minZ), new Vec3d(box.minX, box.minY, box.maxZ), new Vec3d(box.maxX, box.maxY, box.minZ), new Vec3d(box.minX, box.maxY, box.maxZ), new Vec3d(box.maxX, box.minY, box.maxZ), new Vec3d(box.maxX, box.maxY, box.maxZ)};
   }

   public static float[] getMaxRotations(AxisAlignedBB box) {
      float minYaw = 2.14748365E9F;
      float maxYaw = -2.14748365E9F;
      float minPitch = 2.14748365E9F;
      float maxPitch = -2.14748365E9F;
      Vec3d[] arrVec3d = getCorners(box);
      int n = arrVec3d.length;

      for(int n2 = 0; n2 < n; ++n2) {
         Vec3d pos = arrVec3d[n2];
         float[] rot = getRotationFromPosition(pos.xCoord, pos.yCoord, pos.zCoord);
         if(rot[0] < minYaw) {
            minYaw = rot[0];
         }

         if(rot[0] > maxYaw) {
            maxYaw = rot[0];
         }

         if(rot[1] < minPitch) {
            minPitch = rot[1];
         }

         if(rot[1] > maxPitch) {
            maxPitch = rot[1];
         }
      }

      return new float[]{minYaw, maxYaw, minPitch, maxPitch};
   }

   public static float[] getRotationNeededHypixelBetter(Entity p) {
      double d1 = p.posX - Minecraft.thePlayer.posX;
      double d2 = p.posY + (double)p.getEyeHeight() - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
      double d3 = p.posZ - Minecraft.thePlayer.posZ;
      double d4 = Math.sqrt(d1 * d1 + d3 * d3);
      float f1 = (float)(Math.atan2(d3, d1) * 180.0D / 3.141592653589793D) - 90.0F;
      float f2 = (float)(-Math.atan2(d2, d4) * 180.0D / 3.141592653589793D);
      return new float[]{f1, f2};
   }

   public static float changeRotation(float p_706631, float p_706632, float p_706633) {
      float var4 = MathHelper.wrapAngleTo180_float(p_706632 - p_706631);
      if(var4 > p_706633) {
         var4 = p_706633;
      }

      if(var4 < -p_706633) {
         var4 = -p_706633;
      }

      return p_706631 + var4;
   }

   public static float[] getAverageRotations(List targetList) {
      double posX = 0.0D;
      double posY = 0.0D;
      double posZ = 0.0D;

      Entity ent;
      for(Iterator var8 = targetList.iterator(); var8.hasNext(); posZ += ent.posZ) {
         ent = (Entity)var8.next();
         posX += ent.posX;
         posY += ent.boundingBox.maxY - 2.0D;
      }

      posX /= (double)targetList.size();
      posY /= (double)targetList.size();
      posZ /= (double)targetList.size();
      return new float[]{getRotationFromPosition(posX, posZ, posY)[0], getRotationFromPosition(posX, posZ, posY)[1]};
   }

   public static float getStraitYaw() {
      float YAW = MathHelper.wrapAngleTo180_float(Minecraft.thePlayer.rotationYaw);
      if(YAW < 45.0F && YAW > -45.0F) {
         YAW = 0.0F;
      } else if(YAW > 45.0F && YAW < 135.0F) {
         YAW = 90.0F;
      } else if(YAW <= 135.0F && YAW >= -135.0F) {
         YAW = -90.0F;
      } else {
         YAW = 180.0F;
      }

      return YAW;
   }

   public static float[] getBowAngles(Entity entity) {
      double xDelta = (entity.posX - entity.lastTickPosX) * 0.4D;
      double zDelta = (entity.posZ - entity.lastTickPosZ) * 0.4D;
      Minecraft.getMinecraft();
      double d = (double)Minecraft.thePlayer.getDistanceToEntity(entity);
      d -= d % 0.8D;
      double xMulti = 1.0D;
      double zMulti = 1.0D;
      boolean sprint = entity.isSprinting();
      xMulti = d / 0.8D * xDelta * (sprint?1.25D:1.0D);
      zMulti = d / 0.8D * zDelta * (sprint?1.25D:1.0D);
      double var10000 = entity.posX + xMulti;
      Minecraft.getMinecraft();
      double x = var10000 - Minecraft.thePlayer.posX;
      var10000 = entity.posZ + zMulti;
      Minecraft.getMinecraft();
      double z = var10000 - Minecraft.thePlayer.posZ;
      Minecraft.getMinecraft();
      var10000 = Minecraft.thePlayer.posY;
      Minecraft.getMinecraft();
      double y = var10000 + (double)Minecraft.thePlayer.getEyeHeight() - (entity.posY + (double)entity.getEyeHeight());
      Minecraft.getMinecraft();
      double dist = (double)Minecraft.thePlayer.getDistanceToEntity(entity);
      float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0F;
      double d1 = (double)MathHelper.sqrt_double(x * x + z * z);
      float pitch = (float)(-(Math.atan2(y, d1) * 180.0D / 3.141592653589793D)) + (float)dist * 0.11F;
      return new float[]{yaw, -pitch};
   }

   public static float[] getRotationFromPosition(double x, double z, double y) {
      Minecraft.getMinecraft();
      double xDiff = x - Minecraft.thePlayer.posX;
      Minecraft.getMinecraft();
      double zDiff = z - Minecraft.thePlayer.posZ;
      Minecraft.getMinecraft();
      double yDiff = y - Minecraft.thePlayer.posY - 1.2D;
      double dist = (double)MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
      float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
      float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D));
      return new float[]{yaw, pitch};
   }

   public static float getTrajAngleSolutionLow(float d3, float d1, float velocity) {
      float g = 0.006F;
      float sqrt = velocity * velocity * velocity * velocity - g * (g * d3 * d3 + 2.0F * d1 * velocity * velocity);
      return (float)Math.toDegrees(Math.atan(((double)(velocity * velocity) - Math.sqrt((double)sqrt)) / (double)(g * d3)));
   }

   public static float getYawChange(float yaw, double posX, double posZ) {
      Minecraft.getMinecraft();
      double deltaX = posX - Minecraft.thePlayer.posX;
      Minecraft.getMinecraft();
      double deltaZ = posZ - Minecraft.thePlayer.posZ;
      double yawToEntity = 0.0D;
      if(deltaZ < 0.0D && deltaX < 0.0D) {
         if(deltaX != 0.0D) {
            yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
         }
      } else if(deltaZ < 0.0D && deltaX > 0.0D) {
         if(deltaX != 0.0D) {
            yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
         }
      } else if(deltaZ != 0.0D) {
         yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
      }

      return MathHelper.wrapAngleTo180_float(-(yaw - (float)yawToEntity));
   }

   public static float pq(float v) {
      if((v %= 360.0F) >= 180.0F) {
         v -= 360.0F;
      }

      if(v < -180.0F) {
         v += 360.0F;
      }

      return v;
   }

   public static float getPitchChange(float pitch, Entity entity, double posY) {
      double var10000 = entity.posX;
      Minecraft.getMinecraft();
      double deltaX = var10000 - Minecraft.thePlayer.posX;
      var10000 = entity.posZ;
      Minecraft.getMinecraft();
      double deltaZ = var10000 - Minecraft.thePlayer.posZ;
      var10000 = posY - 2.2D + (double)entity.getEyeHeight();
      Minecraft.getMinecraft();
      double deltaY = var10000 - Minecraft.thePlayer.posY;
      double distanceXZ = (double)MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
      double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
      return -MathHelper.wrapAngleTo180_float(pitch - (float)pitchToEntity) - 2.5F;
   }

   public static float getNewAngle(float angle) {
      angle %= 360.0F;
      if(angle >= 180.0F) {
         angle -= 360.0F;
      }

      if(angle < -180.0F) {
         angle += 360.0F;
      }

      return angle;
   }

   public static float getDistanceBetweenAngles(float angle1, float angle2) {
      float angle = Math.abs(angle1 - angle2) % 360.0F;
      if(angle > 180.0F) {
         angle = 360.0F - angle;
      }

      return angle;
   }
}
