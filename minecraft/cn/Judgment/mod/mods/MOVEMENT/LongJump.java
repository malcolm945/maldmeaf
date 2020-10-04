package cn.Judgment.mod.mods.MOVEMENT;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventMove;
import cn.Judgment.events.EventReceivePacket;
import cn.Judgment.events.EventUpdate;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.ui.ClientNotification;
import cn.Judgment.util.ClientUtil;
import cn.Judgment.util.Timer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;

public class LongJump extends Mod {
    private double moveSpeed, lastDist;
    private int level;
	
    public Value<Boolean> vanilla = new Value("LongJump_vanilla", false);
    public Value<Double> boostval = new Value("LongJump_Boost", 3.0, 1.0, 5.0, 0.1);
	   public Value<Boolean> Lagback = new Value("LongJump_Lagback", false);
	private Timer lastCheck = new Timer();
	public LongJump() {
		super("LongJump", Category.MOVEMENT);
	}

	@EventTarget
	public void OnUpadate(EventUpdate e) {
        if (mc.thePlayer == null) return;
        this.setDisplayName(vanilla.getValueState() ? "Vanilla":"Hypixel");
        if (mc.thePlayer.isOnLiquid() || mc.thePlayer.isInLiquid()) return;
            double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            lastDist = Math.sqrt((xDist * xDist) + (zDist * zDist));
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
	public void onMove(EventMove event) {

	    if (mc.thePlayer == null) return;
        if (mc.thePlayer.isOnLiquid() || mc.thePlayer.isInLiquid()) return;
        if (vanilla.getValueState()) {
            setMoveSpeed(event,3);
            if (mc.thePlayer.isMoving()) {
                if (mc.thePlayer.onGround) {
                    event.setY(mc.thePlayer.motionY = 0.41);
                }
            } else {
                mc.thePlayer.motionX = 0.0;
                mc.thePlayer.motionZ = 0.0;
            }
        } else {
            double forward = mc.thePlayer.movementInput.moveForward;
            double strafe = mc.thePlayer.movementInput.moveStrafe;
            float yaw = mc.thePlayer.rotationYaw;
            if (forward == 0.0F && strafe == 0.0F) {
                event.setX(0);
                event.setZ(0);
            }
            if (forward != 0 && strafe != 0) {
                forward = forward * Math.sin(Math.PI / 4);
                strafe = strafe * Math.cos(Math.PI / 4);
            }
            if (level != 1 || mc.thePlayer.moveForward == 0.0F && mc.thePlayer.moveStrafing == 0.0F) {
                if (level == 2) {
                    ++level;
                    double motionY = 0.40123128;
                    if ((mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) && mc.thePlayer.onGround) {
                        if (mc.thePlayer.isPotionActive(Potion.jump)) motionY += ((mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
                        event.setY(mc.thePlayer.motionY = motionY);
                        moveSpeed *= 2.149;
                    }
                } else if (level == 3) {
                    ++level;
                    double difference = 0.763D * (lastDist - getBaseMoveSpeed());
                    moveSpeed = lastDist - difference;
                } else {
                    if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0 || mc.thePlayer.isCollidedVertically) {
                        level = 1;
                    }
                    moveSpeed = lastDist - lastDist / 159.0D;
                }
            } else {
                level = 2;
                double boost = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? boostval.getValueState().doubleValue() : boostval.getValueState().doubleValue() + 1.1;
                moveSpeed = boost * getBaseMoveSpeed() - 0.01D;
            }
            moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
            final double mx = -Math.sin(Math.toRadians(yaw));
            final double mz = Math.cos(Math.toRadians(yaw));
            event.setX(forward * moveSpeed * mx + strafe * moveSpeed * mz);
            event.setZ(forward * moveSpeed * mz - strafe * moveSpeed * mx);
        }
        
	}
	
	 private void setMoveSpeed(final EventMove event, final double speed) {
	        double forward = mc.thePlayer.movementInput.moveForward;
	        double strafe = mc.thePlayer.movementInput.moveStrafe;
	        float yaw = mc.thePlayer.rotationYaw;
	        if (forward == 0.0 && strafe == 0.0) {
	            event.setX(0.0);
	            event.setZ(0.0);
	        } else {
	            if (forward != 0.0) {
	                if (strafe > 0.0) {
	                    yaw += ((forward > 0.0) ? -45 : 45);
	                } else if (strafe < 0.0) {
	                    yaw += ((forward > 0.0) ? 45 : -45);
	                }
	                strafe = 0.0;
	                if (forward > 0.0) {
	                    forward = 1.0;
	                } else if (forward < 0.0) {
	                    forward = -1.0;
	                }
	            }
	            event.setX(forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw)));
	            event.setZ(forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw)));
	        }
	    }

	    private double getBaseMoveSpeed() {
	        double n = 0.2873;
	        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
	            n *= 1.0 + 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
	        }
	        return n;
	    }

	    
	
    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1F;
        if (mc.thePlayer != null) {
            moveSpeed = getBaseMoveSpeed();
        }
        lastDist = 0.0D;
    }

    @Override
    public void onEnable() {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        level = 0;
        lastDist = 0.0D;
    }
}
