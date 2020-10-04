package optifine;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public class MathUtils
{
    public static int getAverage(int[] p_getAverage_0_)
    {
        if (p_getAverage_0_.length <= 0)
        {
            return 0;
        }
        else
        {
            int i = 0;

            for (int j = 0; j < p_getAverage_0_.length; ++j)
            {
                int k = p_getAverage_0_[j];
                i += k;
            }

            int l = i / p_getAverage_0_.length;
            return l;
        }
    }

	   public static boolean isOnGround(double height) {
	        if (!Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty()) {
	            return true;
	        } else {
	            return false;
	        }
	    }

	 public static int getSpeedEffect() {
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed))
            return Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        else
            return 0;
    }

	 public static void setMotion(double speed) {
	        double forward = Minecraft.thePlayer.movementInput.moveForward;
	        double strafe = Minecraft.thePlayer.movementInput.moveStrafe;
	        float yaw = Minecraft.thePlayer.rotationYaw;
	        if ((forward == 0.0D) && (strafe == 0.0D)) {
	        	Minecraft.thePlayer.motionX = 0;
	        	Minecraft.thePlayer.motionZ = 0;
	        } else {
	            if (forward != 0.0D) {
	                if (strafe > 0.0D) {
	                    yaw += (forward > 0.0D ? -45 : 45);
	                } else if (strafe < 0.0D) {
	                    yaw += (forward > 0.0D ? 45 : -45);
	                }
	                strafe = 0.0D;
	                if (forward > 0.0D) {
	                    forward = 1;
	                } else if (forward < 0.0D) {
	                    forward = -1;
	                }
	            }
	            Minecraft.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)); 
	            Minecraft.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F));
	        }
	    }

	 public static double defaultSpeed() {
	        double baseSpeed = 0.2873D;
	        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
	            int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
	            	baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
	        }
	        return baseSpeed;
	    }
}
