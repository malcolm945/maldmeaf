package cn.Judgment.mod.mods.MOVEMENT;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventMove;
import cn.Judgment.events.EventPostMotion;
import cn.Judgment.events.EventPreMotion;
import cn.Judgment.events.EventReceivePacket;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.ui.ClientNotification;
import cn.Judgment.util.ClientUtil;
import cn.Judgment.util.PlayerUtil;
import cn.Judgment.util.Timer;
import cn.Judgment.util.Vec3Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Vec3;

public class Fly extends Mod {
	
	

	public int counter2 = 0;
	public Value<String> mode = new Value("Fly", "Mode", 0);
	   public Value<Boolean> Lagback = new Value("Fly_Lagback", false);
	public Value<Double> Speed = new Value("Fly_Speed",1d, 1d, 20d, 1d);
	   private int zoom;
	   private Timer lastCheck = new Timer();
	   private Timer boostDelay = new Timer();
	public Fly() {
		super("Fly", Category.MOVEMENT);
		this.mode.mode.add("Hypixel");
		this.mode.mode.add("Vanilla");
		this.mode.mode.add("Motion");

		
		

		
	}
	
	@Override
	public void onEnable() {
		if(this.mode.isCurrentMode("Hypixel")) {
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0, mc.thePlayer.posZ);
		}
		super.onEnable();
	}
	
	@EventTarget
	public void onPre(EventPostMotion event) {
		if(this.mode.isCurrentMode("DAC")) {
			this.setDisplayName("DAC");
			if(mc.thePlayer.fallDistance > 3) {
				mc.timer.timerSpeed = 0.1f;
				mc.thePlayer.motionY *= 0.6f;
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
				Vec3 pos = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
				Vec3Util vec = new Vec3Util(pos, -mc.thePlayer.rotationYaw, 0.0F, 7);
				mc.thePlayer.setPosition(vec.getEndVector().xCoord, vec.getEndVector().yCoord, vec.getEndVector().zCoord);
				mc.thePlayer.fallDistance = 0;
			} else {
				mc.timer.timerSpeed = 1.0f;
			}
		}
	}
	
	  @EventTarget
	    public void onEvent(EventReceivePacket ep) {
		   Packet p = ep.getPacket();
		   if (p instanceof S08PacketPlayerPosLook) {
           S08PacketPlayerPosLook pac = (S08PacketPlayerPosLook) ep.getPacket();
           if (Lagback.getValueState()) {
        	   ClientUtil.INSTANCE.sendClientMessage("Lagback checks!", ClientNotification.Type.WARNING);
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
	public void onPre(EventPreMotion event) {
		if(this.mode.isCurrentMode("Motion")) {
			this.setDisplayName("Motion");
			mc.thePlayer.onGround = true;
			double speed = Math.max((double) this.Speed.getValueState().floatValue(), getBaseMoveSpeed());
		//	  mc.thePlayer.fallDistance = 0.0F;
			   if (mc.thePlayer.movementInput.jump) {
	                  mc.thePlayer.motionY = speed * 0.6D;
	               } else if (mc.thePlayer.movementInput.sneak) {
	                  mc.thePlayer.motionY = -speed * 0.6D;
	               } else {
	                  mc.thePlayer.motionY = 0.0D;
	               }
		}
	}
	
	@EventTarget
	public void onMove(EventMove event) {
		if(this.mode.isCurrentMode("Hypixel")) {
			this.setDisplayName("Hypixel");
			mc.thePlayer.onGround = false;
			mc.thePlayer.capabilities.isFlying = false;
			
			if(mc.gameSettings.keyBindSneak.isKeyDown()) {
				mc.thePlayer.motionY *= 0.0d;
			}else if(mc.gameSettings.keyBindJump.isKeyDown()) {
				mc.thePlayer.motionY *= 0.0d;
			}
			
			if(PlayerUtil.MovementInput()) {
				this.setSpeed(PlayerUtil.getBaseMovementSpeed() - 0.05d);
			} else {
				this.setSpeed(0.0d);
			}
			
			this.counter2 += 1;
			
			if (PlayerUtil.MovementInput()) {
				this.setSpeed(PlayerUtil.getBaseMovementSpeed());
			} else {
				mc.thePlayer.motionX *= 0.0D;
			    mc.thePlayer.motionZ *= 0.0D;
			    mc.timer.timerSpeed = 1.0F;
			}
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
			switch (counter2) {
			case 1:
				break;
			case 2:
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-5, mc.thePlayer.posZ);
                counter2 = 0;
                break;
			case 3:
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.0E-5, mc.thePlayer.posZ);
                counter2 = 0;
                break;
			}
			event.y = mc.thePlayer.motionY = 0f;
		}
	}
	

	
	@EventTarget
	public void onMove1(EventMove event) {
		int Speed = this.Speed.getValueState().intValue();
		if(this.mode.isCurrentMode("Vanilla")) {
			this.setDisplayName("Vanilla");
            this.mc.thePlayer.motionY = this.mc.thePlayer.movementInput.jump ? 1.0 : (this.mc.thePlayer.movementInput.sneak ? -1.0 : 0.0);
            if (this.mc.thePlayer.moving()) {
                this.mc.thePlayer.setSpeed(Speed);
            } else {
                this.mc.thePlayer.setSpeed(0.0);
            }
			event.y = mc.thePlayer.motionY;
		}
		if(this.mode.isCurrentMode("Motion")) {
            double speed = (double) this.Speed.getValueState().floatValue();

               if (this.boostDelay.delay(10000.0F)) {
                  this.boostDelay.reset();
               }
            

            float boost =  this.Speed.getValueState().floatValue();
            if (this.zoom > 0 && boost > 0.0F && !this.boostDelay.delay(5000.0F)) {
            	
               mc.timer.timerSpeed = 1.0F + boost;
               if (this.zoom < 10) {
                  float percent = (float)(this.zoom / 10);
                  if ((double)percent > 1.0D) {
                     percent = 1.0F;
                  }

                  mc.timer.timerSpeed = 1.0F + boost * percent;
               }
            } else {
               mc.timer.timerSpeed = 1.0F;
            }

            --this.zoom;
            double forward = (double)mc.thePlayer.movementInput.moveForward;
            double strafe = (double)mc.thePlayer.movementInput.moveStrafe;
            float yaw = mc.thePlayer.rotationYaw;
            if (forward == 0.0D && strafe == 0.0D) {
            	event.setX(0.0D);
            	event.setZ(0.0D);
            } else {
               if (forward != 0.0D) {
                  if (strafe > 0.0D) {
                     yaw += (float)(forward > 0.0D ? -45 : 45);
                  } else if (strafe < 0.0D) {
                     yaw += (float)(forward > 0.0D ? 45 : -45);
                  }

                  strafe = 0.0D;
                  if (forward > 0.0D) {
                     forward = 1.0D;
                  } else if (forward < 0.0D) {
                     forward = -1.0D;
                  }
               }
               double movespeed = Math.max((double) this.Speed.getValueState().floatValue(), getBaseMoveSpeed());
               event.setX(forward * movespeed * Math.cos(Math.toRadians((double)(yaw + 90.0F))) + strafe * movespeed * Math.sin(Math.toRadians((double)(yaw + 90.0F))));
               event.setZ(forward * movespeed * Math.sin(Math.toRadians((double)(yaw + 90.0F))) - strafe * movespeed * Math.cos(Math.toRadians((double)(yaw + 90.0F))));
            }
	
	}
 }
	
	
	public void setSpeed(double speed) {
		mc.thePlayer.motionX = (-MathHelper.sin(PlayerUtil.getDirection()) * speed);
	    mc.thePlayer.motionZ = (MathHelper.cos(PlayerUtil.getDirection()) * speed);
	}
	
	public static double getBaseMoveSpeed() {
	      double baseSpeed = 0.2873D;
	      if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
	         int amplifier = Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
	         baseSpeed *= 1.0D + 0.2D * (double)(amplifier + 1);
	      }

	      return baseSpeed;
	   }
	
	
	@Override
	public void onDisable() {
		if(mc.thePlayer.capabilities.isCreativeMode == false) {
			mc.thePlayer.capabilities.isFlying = false;
    		mc.thePlayer.onGround = false;
        	mc.thePlayer.capabilities.allowFlying = false;
        	mc.thePlayer.capabilities.setFlySpeed(0.0F);
		}
		mc.timer.timerSpeed = 1f;
		mc.thePlayer.speedInAir = 0.02f;
		super.onDisable();
	}

}
