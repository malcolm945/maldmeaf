/*
 * Decompiled with CFR 0.149.
 */
package cn.Judgment.mod.mods.MOVEMENT;

import javax.management.Notification;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventMove;
import cn.Judgment.events.EventPreUpdate;
import cn.Judgment.events.EventPullback;
import cn.Judgment.events.EventStep;
import cn.Judgment.events.EventUpdate;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.mod.ModManager;
import cn.Judgment.util.BlockUtils;
import cn.Judgment.util.ClientUtil;
import cn.Judgment.util.MoveUtils;
import cn.Judgment.util.PlayerUtil;
import cn.Judgment.util.timeUtils.TimeHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;
import cn.Judgment.ui.ClientNotification;
public class Speed
extends Mod {
    private int stage = 1;
    private double lastDist;
    private double moveSpeedOG;
    private double lastDistOG;
    public static Value mode = new Value("Speed", "Mode", 0);
    public boolean shouldslow = false;
    double count = 0.0;
    boolean collided = false;
    int spoofSlot = 0;
    public static TimeHelper timer = new TimeHelper();
    public static TimeHelper lastcheck = new TimeHelper();
    boolean lessSlow;
    double less;
    double stair;
    TimeHelper lastCheck = new TimeHelper();
    double randomY = this.random(1334.0, -1332.0) / 250000.0;
    private double a;
    private boolean moving;
    private int c;
    private long ms;
    private boolean onGround;
    private double f;
    private boolean g;
    private double distance;
    public Value<Boolean> lagback = new Value<Boolean>("Speed_LagBackChecks", true);
    private double moveSpeed;
    private double speed = 0.08f;

    public Speed() {
        super("Speed", Category.MOVEMENT);
        Speed.mode.mode.add("Hypixel");
        Speed.mode.mode.add("HypixelCN");
        this.showValue = mode;
    }

    private double random(double min, double max) {
        return Math.random() * (min - max) + max;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setDisplayName(Speed.mode.getModeAt(mode.getCurrentMode()));
        if (mode.isCurrentMode("HypixelCN")) {
            this.lastDist = Math.sqrt((Minecraft.thePlayer.posX - Minecraft.thePlayer.prevPosX) * (Minecraft.thePlayer.posX - Minecraft.thePlayer.prevPosX) + (Minecraft.thePlayer.posZ - Minecraft.thePlayer.prevPosZ) * (Minecraft.thePlayer.posZ - Minecraft.thePlayer.prevPosZ));
        }
        if (mode.isCurrentMode("Hypixel")) {
            double var7 = Minecraft.thePlayer.posX - Minecraft.thePlayer.prevPosX;
            double zDist = Minecraft.thePlayer.posZ - Minecraft.thePlayer.prevPosZ;
            this.distance = Math.sqrt(var7 * var7 + zDist * zDist);
        }
    }

    @EventTarget
    public void onStep(EventStep event) {
        double height = Minecraft.thePlayer.getEntityBoundingBox().minY - Minecraft.thePlayer.posY;
        if (height > 0.7) {
            this.less = 0.0;
        }
        if (height == 0.5) {
            this.stair = 0.75;
        }
    }

    @EventTarget
    public void onPullback(EventPullback e) {
        if (this.lagback.getValueState().booleanValue()) {
            ClientUtil.sendClientMessage("(LagBackCheck) Speed Disabled", ClientNotification.Type.WARNING);
            this.set(false);
        }
    }

    @EventTarget
    public void onMotion(EventMove event) {
        if (mode.isCurrentMode("Hypixel")) {
            if (Minecraft.thePlayer.isCollidedHorizontally) {
                this.collided = true;
            }
            if (this.collided) {
                Timer.timerSpeed = 1.0f;
                this.stage = -1;
            }
            if (this.stair > 0.0) {
                this.stair -= 0.25;
            }
            this.less -= this.less > 1.0 ? 0.12 : 0.11;
            if (this.less < 0.0) {
                this.less = 0.0;
            }
    		if (!BlockUtils.isInLiquid() && MoveUtils.isOnGround(0.01D) && isMoving()) {
                this.collided = Minecraft.thePlayer.isCollidedHorizontally;
                if (this.stage >= 0 || this.collided) {
                    this.stage = 0;
                    this.a = 0.4086666 + (double)PlayerUtil.getJumpEffect() * 0.1;
                    if (this.stair == 0.0) {
                        Minecraft.thePlayer.jump();
                        Minecraft.thePlayer.motionY = this.a;
                        event.setY(Minecraft.thePlayer.motionY);
                    }
                    this.less += 1.0;
                    boolean bl = this.lessSlow = this.less > 1.0 && !this.lessSlow;
                    if (this.less > 1.12) {
                        this.less = 1.12;
                    }
                }
            }
            this.speed = this.getHypixelSpeed(this.stage) + 0.0331;
            this.speed *= 0.91;
            if (this.stair > 0.0) {
                this.speed *= 0.66 - (double)PlayerUtil.getSpeedEffect() * 0.1;
            }
            if (this.stage < 0) {
                this.speed = this.getBaseMoveSpeed();
            }
            if (this.lessSlow) {
                this.speed *= 0.93;
            }
            if (PlayerUtil.isInLiquid()) {
                this.speed = 0.12;
            }
            if (this.isMoving()) {
                if (!((TargetStrafe)ModManager.getModByName("TargetStrafe")).doStrafeAtSpeed(event, this.speed)) {
                    this.setMotion(event, this.speed);
                
                ++this.stage;
            }else 
            if (mode.isCurrentMode("HypixelCN")) {
   			 if (this.mc.thePlayer.isCollidedHorizontally) {
   	                this.collided = true;
   	            }
   	            if (this.collided) {
   	                this.mc.timer.timerSpeed = 1.0f;
   	                this.stage = -1;
   	            }
   	            if (this.stair > 0.0) {
   	                this.stair -= 0.25;
   	            }
   	            final double less2 = this.less;
   	            double n6;
   	            if (this.less > 1.0) {
   	                n6 = 0.12;
   	            }
   	            else {
   	                n6 = 0.11;
   	            }
   	            this.less = less2 - n6;
   	            if (this.less < 0.0) {
   	                this.less = 0.0;
   	            }
   	            if (!BlockUtils.isInLiquid() && MoveUtils.isOnGround(0.01D) && isMoving()) {
   	                this.collided = this.mc.thePlayer.isCollidedHorizontally;
   	                if (this.stage >= 0 || this.collided) {
   	                    this.stage = 0;
   	                    double n7;
   	                    if (ModManager.getModuleByClass(Scaffold.class).isEnabled()) {
   	                        n7 = 0.407;
   	                    }
   	                    else {
   	                        n7 = 0.41999742;
   	                    }
   	                    final double motionY3 = n7 + MoveUtils.getJumpEffect() * 0.1;
   	                    if (this.stair == 0.0) {
   	                        this.mc.thePlayer.jump();
   	                        EventMove.setY(this.mc.thePlayer.motionY = motionY3);
   	
   	                    }
   	                    ++this.less;
   	                    if (this.less > 1.0 && !this.lessSlow) {
   	                        this.lessSlow = true;
   	                    }
   	                    else {
   	                        this.lessSlow = false;
   	                    }
   	                    if (this.less > 1.12) {
   	                        this.less = 1.12;
   	                    }
   	                }
   	            }
   	            this.speed = this.getHypixelSpeed(this.stage) + 0.01 + Math.random() / 500.0;
   	            this.speed *= 0.87;
   	            if (this.stair > 0.0) {
   	                this.speed *= 0.7 - MoveUtils.getSpeedEffect() * 0.1;
   	            }
   	            if (this.stage < 0) {
   	                this.speed = PlayerUtil.defaultSpeed();
   	            }
   	            if (this.lessSlow) {
   	                this.speed *= 0.95;
   	            }
   	            if (BlockUtils.isInLiquid()) {
   	                this.speed = 0.12;
   	            }
   	            if (this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f) {
   	            	this.setMotion(event, this.speed);
   	                ++this.stage;
   	            }
            }
            }
        }
    }

    private void setMotion(EventMove em, double speed) {
        double forward = MovementInput.moveForward;
        double strafe = MovementInput.moveStrafe;
        float yaw = Minecraft.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            em.setX(0.0);
            em.setZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            Minecraft.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians((double)yaw + 88.0)) + strafe * speed * Math.sin(Math.toRadians((double)yaw + 87.9000815258789));
            em.setX(Minecraft.thePlayer.motionX);
            Minecraft.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians((double)yaw + 88.0)) - strafe * speed * Math.cos(Math.toRadians((double)yaw + 87.9000815258789));
            em.setZ(Minecraft.thePlayer.motionZ);
        }
    }

    @Override
    public void onDisable() {
        if (Minecraft.thePlayer == null) {
            return;
        }
        Timer.timerSpeed = 1.0f;
        super.onDisable();
    }

    public static boolean isNotCollidingBelow(double paramDouble) {
        return !Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(0.0, -paramDouble, 0.0)).isEmpty();
    }

    @Override
    public void onEnable() {
        boolean thePlayer;
        boolean bl = thePlayer = Minecraft.thePlayer == null;
        boolean bl2 = thePlayer ? false : (this.collided = Minecraft.thePlayer.isCollidedHorizontally);
        this.spoofSlot = thePlayer ? 1 : Minecraft.thePlayer.inventory.currentItem;
        this.lessSlow = false;
        if (Minecraft.thePlayer != null) {
            this.speed = PlayerUtil.defaultSpeed();
        }
        this.less = 0.0;
        this.count = 0.0;
        this.lastDist = 0.0;
        this.stage = 2;
        Timer.timerSpeed = 1.0f;
        super.onEnable();
    }

    private double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    private double getHypixelSpeed(int stage) {
        double value = PlayerUtil.defaultSpeed() + 0.028 * (double)PlayerUtil.getSpeedEffect() + (double)PlayerUtil.getSpeedEffect() / 15.0;
        double firstvalue = 0.4145 + (double)PlayerUtil.getSpeedEffect() / 12.5;
        double thirdvalue = 0.4045 + (double)PlayerUtil.getSpeedEffect() / 12.5;
        double decr = (double)stage / 500.0 * 3.0;
        if (stage == 0) {
            if (timer.isDelayComplete(300L)) {
                timer.reset();
            }
            if (!this.lastCheck.isDelayComplete(500L)) {
                if (!this.shouldslow) {
                    this.shouldslow = true;
                }
            } else if (this.shouldslow) {
                this.shouldslow = false;
            }
            value = 0.64 + ((double)PlayerUtil.getSpeedEffect() + 0.028 * (double)PlayerUtil.getSpeedEffect()) * 0.134;
        } else if (stage == 1) {
            value = firstvalue;
        } else if (stage == 2) {
            value = thirdvalue;
        } else if (stage >= 3) {
            value = thirdvalue - decr;
        }
        if (this.shouldslow || !this.lastCheck.isDelayComplete(500L) || this.collided) {
            value = 0.2;
            if (stage == 0) {
                value = 0.0;
            }
        }
        return Math.max(value, this.shouldslow ? value : PlayerUtil.defaultSpeed() + 0.028 * (double)PlayerUtil.getSpeedEffect());
    }

    protected boolean isMoving() {
        if (MovementInput.moveForward == 0.0f) {
            if (MovementInput.moveStrafe == 0.0f) {
                return false;
            }
        }
        return true;
    }
}

