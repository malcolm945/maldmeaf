package cn.Judgment.mod.mods.MOVEMENT;

import java.util.Random;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventMove;
import cn.Judgment.events.EventReceivePacket;
import cn.Judgment.events.EventUpdate;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.ui.ClientNotification;
import cn.Judgment.util.ClientUtil;
import cn.Judgment.util.DamageUtils;
import cn.Judgment.util.PlayerUtil;
import cn.Judgment.util.Timer;
import cn.Judgment.util.timeUtils.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

public class ZoomFly extends Mod {
	public Value<String> mode = new Value("ZoomFly", "Zoom Mode", 0);
	   public Value<Boolean> Lagback = new Value("ZoomFly_Lagback", false);
	   public static Value<Boolean> uhc = new Value("ZoomFly_UHC", true);

	private Timer lastCheck = new Timer();
    private TimerUtil timer = new TimerUtil();
    private double movementSpeed;
    private int hypixelCounter;
    private int hypixelCounter2;
    int counter, level;
    double moveSpeed, lastDist;   
    boolean b2,nigga;
    boolean onfly = false;
   
    public ZoomFly() {
    	super("ZoomFly", Category.MOVEMENT);
        this.mode.mode.add("CNHypixel");
        this.mode.mode.add("Hypixel");
        
    }

    public void damagePlayer(int damage) {
		if (damage < 1)
			damage = 1;
		if (damage > MathHelper.floor_double(mc.thePlayer.getMaxHealth()))
			damage = MathHelper.floor_double(mc.thePlayer.getMaxHealth());

		double offset = 0.0625;
		if (mc.thePlayer != null && mc.getNetHandler() != null && mc.thePlayer.onGround) {
			for (int i = 0; i <= ((3 + damage) / offset); i++) {
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY, mc.thePlayer.posZ, (i == ((3 + damage) / offset))));
			}
		}
	}

    
    @Override
    public void onEnable() {
    	 if (this.mode.isCurrentMode("CNHypixel")) {
    		 this.setDisplayName("CNHypixel");
    			new Thread(()->{
        			try {
    					Thread.sleep(150);
    				} catch (InterruptedException ep) {
    					ep.printStackTrace();
    				}
        			damagePlayer(1);
        		}).start();;
            this.hypixelCounter = 0;
            this.hypixelCounter2 = 1000;
    	 }
//     	 if (this.mode.isCurrentMode("Hypixel")) {
//     		 setDisplayName("Hypixel");
//     	 }
         nigga = false;
		level = 1;
		moveSpeed = 0.1D;
		b2 = true;
		lastDist = 0.0D;
    }

    @Override
    public void onDisable() {
		if(mc.thePlayer.capabilities.isCreativeMode == false) {
			mc.thePlayer.capabilities.isFlying = false;
    		mc.thePlayer.onGround = false;
        	mc.thePlayer.capabilities.allowFlying = false;
        	mc.thePlayer.capabilities.setFlySpeed(0.0F);
		}
        if (mc.thePlayer == null || mc.theWorld == null) return;
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionZ = 0;
        mc.thePlayer.motionY = 0;
        mc.thePlayer.speedInAir = 0.02f;
        mc.timer.timerSpeed = 1.0f;
		level = 1;
		moveSpeed = 0.1D;
		b2 = false;
		lastDist = 0.0D;
	    nigga = false;
    }

    @EventTarget
    public void onEvent(EventReceivePacket ep) {
	   Packet p = ep.getPacket();
	   if (p instanceof S08PacketPlayerPosLook) {
         S08PacketPlayerPosLook pac = (S08PacketPlayerPosLook) ep.getPacket();
         if (Lagback.getValueState()) {
      	   ClientUtil.INSTANCE.sendClientMessage("(LagBackCheck) ZoomFly Disabled", ClientNotification.Type.WARNING);
             mc.thePlayer.onGround = false;
             mc.thePlayer.motionX *= 0;
             mc.thePlayer.motionZ *= 0;
             mc.thePlayer.jumpMovementFactor = 0;
             this.toggle();
         }else if (lastCheck.delay(300)) {
             pac.yaw = mc.thePlayer.rotationYaw;
             pac.pitch = mc.thePlayer.rotationPitch;
         }
         lastCheck.reset();
	   }
	}
    
    @EventTarget
    private void onUpdate(EventUpdate e) {
   	 if (this.mode.isCurrentMode("CNHypixel")) {
   		 
    	if(mc.thePlayer.hurtResistantTime == 19 && mc.thePlayer.onGround ){
    		onfly = true;
    	}
   	 if(!onfly) {
         mc.thePlayer.setPosition(mc.thePlayer.prevPosX, mc.thePlayer.posY, mc.thePlayer.prevPosZ);    
	 }
    	if(!onfly)
    		return;

   			++counter;
   			if (Minecraft.getMinecraft().thePlayer.moveForward == 0
   					&& Minecraft.getMinecraft().thePlayer.moveStrafing == 0) {
   				Minecraft.getMinecraft().thePlayer.setPosition(
   						Minecraft.getMinecraft().thePlayer.posX + 1.0D,
   						Minecraft.getMinecraft().thePlayer.posY + 1.0D,
   						Minecraft.getMinecraft().thePlayer.posZ + 1.0D);
   				Minecraft.getMinecraft().thePlayer.setPosition(Minecraft.getMinecraft().thePlayer.prevPosX,
   						Minecraft.getMinecraft().thePlayer.prevPosY,
   						Minecraft.getMinecraft().thePlayer.prevPosZ);
   				Minecraft.getMinecraft().thePlayer.motionX = 0.0D;
   				Minecraft.getMinecraft().thePlayer.motionZ = 0.0D;
   			}
   			Minecraft.getMinecraft().thePlayer.motionY = 0.0D;
   			if (Minecraft.getMinecraft().gameSettings.keyBindJump.pressed)
   				Minecraft.getMinecraft().thePlayer.motionY += 0.5f;
   			if (Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed)
   				Minecraft.getMinecraft().thePlayer.motionY -= 0.5f;
   			if (counter != 1 && counter == 2) {
   				Minecraft.getMinecraft().thePlayer.setPosition(Minecraft.getMinecraft().thePlayer.posX,
   						Minecraft.getMinecraft().thePlayer.posY + 1.0E-10D,
   						Minecraft.getMinecraft().thePlayer.posZ);
   				counter = 0;
   			}
    	 }
     	 if (this.mode.isCurrentMode("Hypixel")) {
      		 setDisplayName("Hypixel");
      	    if (mc.thePlayer.fallDistance > 0 && !nigga) {
                nigga = true;
            } 
            if (nigga) {
                if ((mc.thePlayer.ticksExisted % 2) == 0) {
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + getRandomInRange(0.00000000000001235423532523523532521, 0.0000000000000123542353252352353252 * 10), mc.thePlayer.posZ);
                }
                mc.thePlayer.motionY = 0;
                
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.thePlayer.motionY = 0.5;
                }
                
            }
      	 }
      	 
    }
    public static double getRandomInRange(double min, double max) {
        Random random = new Random();
        double range = max - min;
        double scaled = random.nextDouble() * range;
        if (scaled > max) {
            scaled = max;
        }
        double shifted = scaled + min;

        if (shifted > max) {
            shifted = max;
        }
        return shifted;
    }
    
    @EventTarget
    public void onPost(EventUpdate e) {
 			double xDist = Minecraft.getMinecraft().thePlayer.posX
 					- Minecraft.getMinecraft().thePlayer.prevPosX;
 			double zDist = Minecraft.getMinecraft().thePlayer.posZ
 					- Minecraft.getMinecraft().thePlayer.prevPosZ;
 			lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    }
    
    @EventTarget
    private void onMove(EventMove e) {

    	 if (this.mode.isCurrentMode("CNHypixel")) {
    	    	if(!onfly)
    	    		return;
        	 if (Minecraft.thePlayer.onGround) {
                e.setY(0.418);
        	 }
			float forward = MovementInput.moveForward;
			float strafe = MovementInput.moveStrafe;
			float yaw = Minecraft.thePlayer.rotationYaw;
			double mx = Math.cos(Math.toRadians((double) (yaw + 90.0F)));
			double mz = Math.sin(Math.toRadians((double) (yaw + 90.0F)));
			
			if (forward == 0.0F && strafe == 0.0F) {
				e.x = 0.0D;
				e.z = 0.0D;
			} else if (forward != 0.0F) {
				if (strafe >= 1.0F) {
					yaw += (float) (forward > 0.0F ? -45 : 45);
					strafe = 0.0F;
				} else if (strafe <= -1.0F) {
					yaw += (float) (forward > 0.0F ? 45 : -45);
					strafe = 0.0F;
				}

				if (forward > 0.0F) {
					forward = 1.0F;
				} else if (forward < 0.0F) {
					forward = -1.0F;
				}
			}
			if (b2) {
				if (level != 1 || Minecraft.getMinecraft().thePlayer.moveForward == 0.0F
						&& Minecraft.getMinecraft().thePlayer.moveStrafing == 0.0F) {
					if (level == 2) {
						level = 3;
						moveSpeed *= 2.149D;
					} else if (level == 3) {
						level = 4;
						double difference = (Minecraft.thePlayer.ticksExisted % 2 == 0 ? 0.0103D : 0.0123D)
								* (lastDist - PlayerUtil.getBaseMovementSpeed());
						moveSpeed = lastDist - difference;
					} else {
						if (Minecraft.getMinecraft().theWorld
								.getCollidingBoundingBoxes(Minecraft.getMinecraft().thePlayer,
										Minecraft.getMinecraft().thePlayer.boundingBox.offset(0.0D,
												Minecraft.getMinecraft().thePlayer.motionY, 0.0D))
								.size() > 0 || Minecraft.getMinecraft().thePlayer.isCollidedVertically) {
							level = 1;
						}
						moveSpeed = lastDist - lastDist / 159.0D;
					}
				} else {
					level = 2;
					int amplifier = Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)
							? Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed)
									.getAmplifier() + 1
							: 0;
					double boost = Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed) ? 1.56
							: 2.034;
					moveSpeed = boost * PlayerUtil.getBaseMovementSpeed();
				}
				moveSpeed = Math.max(moveSpeed,PlayerUtil.getBaseMovementSpeed());
				
				e.setX(forward * moveSpeed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * moveSpeed *  Math.sin(Math.toRadians(yaw + 90.0F)));
			    e.setZ(forward * moveSpeed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * moveSpeed *  Math.cos(Math.toRadians(yaw + 90.0F)));
				if (forward == 0.0F && strafe == 0.0F) {
					e.x = 0.0D;
					e.z = 0.0D;
				}
			}
		}
      	 if (this.mode.isCurrentMode("Hypixel")) {
             float yaw = mc.thePlayer.rotationYaw;
             double strafe = mc.thePlayer.movementInput.moveStrafe;
             double forward = mc.thePlayer.movementInput.moveForward;
             double mx = -Math.sin(Math.toRadians(yaw)), mz = Math.cos(Math.toRadians(yaw));
             if (forward == 0.0F && strafe == 0.0F) {
                 e.setX(0);
                 e.setZ(0);
             }
             if (forward != 0 && strafe != 0) {
                 forward = forward * Math.sin(Math.PI / 4);
                 strafe = strafe * Math.cos(Math.PI / 4);
             }
             double motionY = 0.3999;
             if (level != 1) {
                 if (level == 2) {
                     if ((mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) && mc.thePlayer.onGround && !nigga) {
//                         damagePlayer();
             		 	DamageUtils.damage();

                         level = 3;
                         if (mc.thePlayer.isPotionActive(Potion.jump)) motionY += ((mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
                         e.setY(mc.thePlayer.motionY = motionY);
                         moveSpeed *= 2.149;
                         nigga = true;
                     }
                 } else if (level == 3) {
                     level = 4;
                 	double difference = (Minecraft.thePlayer.ticksExisted % 2 == 0 ? 0.0000000001D : 0.00000000000001D)
							* (lastDist - PlayerUtil.getBaseMovementSpeed());
                     moveSpeed = lastDist - difference;
                 } else {
                     if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0 || mc.thePlayer.isCollidedVertically) {
                         level = 1;
                     }
                     moveSpeed = lastDist - lastDist / 159.0D;
                 }
             } else {
                 level = 2;
         		double boost = Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed) ? 1.70
						:1.8;
				moveSpeed = boost * PlayerUtil.getBaseMovementSpeed();
             }
             moveSpeed = Math.max(moveSpeed, PlayerUtil.getBaseMovementSpeed());
             e.setX(forward * moveSpeed * mx + strafe * moveSpeed * mz);
             e.setZ(forward * moveSpeed * mz - strafe * moveSpeed * mx);
             if (forward == 0.0F && strafe == 0.0F) {
                 e.setX(0.0);
                 e.setZ(0.0);
             }
      	 }
      	 
    }

    public void damagePlayer() {
        if (!mc.thePlayer.onGround) return;
        int count = 65 + (int) (mc.thePlayer.getActivePotionEffect(Potion.jump) != null ? mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() / 3f : 0);
        for (int i = 0; i < count; i++) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.046456, mc.thePlayer.posZ, false));
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0000000035, mc.thePlayer.posZ, false));
        }
        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
    }
    
    double getBaseMoveSpeed() {
        double baseSpeed = 0.275;
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }
}

