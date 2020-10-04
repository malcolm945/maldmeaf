package cn.Judgment.mod.mods.WORLD;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventBlockBB;
import cn.Judgment.events.EventMove;
import cn.Judgment.events.EventPacket;
import cn.Judgment.events.EventReceivePacket;
import cn.Judgment.events.EventTick;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.util.timeUtils.TimerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class Phase extends Mod{
    private int moveUnder;
    public Value<String> mode = new Value("Phase","mode",0);
    private Value<Double> distance = new Value("Phase_Distance", 2.0, 0.0, 10.0, 0.1);
    private Value<Double> vanillaspeed = new Value("Phase_VanillaSpeed", 0.5, 0.0, 3.5, 0.1);
    private Value<Double> verticalspeed = new Value("Phase_VerticalSpeed", 0.5, 0.0, 3.5, 0.1);
    public static boolean phasing;
    private int delay;
    private int state;
    private boolean NCPSetup;
    private TimerUtil timer = new TimerUtil();
	
    
	public Phase() {
		super("Phase", Category.WORLD);
        mode.addValue("Hypixel");
        mode.addValue("Aris");
        mode.addValue("Vanilla");
        mode.addValue("NCP");
	}


	@EventTarget
	private void onTick(EventTick e) {
	
		 if (mc.thePlayer == null) return;
	        if (mode.isCurrentMode("Vanilla")) {
	            if (mc.thePlayer != null && moveUnder == 1) {
	                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2.0, mc.thePlayer.posZ, true));
	                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, true));
	                moveUnder = 0;
	            }
	            double multiplier = 1;
	            double mx = -Math.sin(Math.toRadians(mc.thePlayer.rotationYaw));
	            double mz = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw));
	            double x = mc.thePlayer.movementInput.moveForward * multiplier * mx + mc.thePlayer.movementInput.moveStrafe * multiplier * mz;
	            double z = mc.thePlayer.movementInput.moveForward * multiplier * mz - mc.thePlayer.movementInput.moveStrafe * multiplier * mx;
	            if (mc.thePlayer.isCollidedHorizontally && !isInsideBlock() && mc.thePlayer.isMoving()) {
	                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z, true));
	                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, true));
	            }
	        }
	        if (mode.isCurrentMode("NCP")) {
	            if (mc.thePlayer.isCollidedHorizontally && mc.gameSettings.keyBindSprint.isKeyDown()) {
	                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.05, mc.thePlayer.posZ, true));
	                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
	                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.05, mc.thePlayer.posZ, true));
	            }
	        }
	}
	
	@EventTarget
    public void run(EventBlockBB event) {
        if (mode.isCurrentMode("Aris")) {
			this.setDisplayName("Aris");
            if (event.getBoundingBox() != null && event.getBoundingBox().maxY > mc.thePlayer.getEntityBoundingBox().minY && mc.thePlayer.isSneaking()) {
                event.setBoundingBox(null);
                event.setCancelled(true);
            }
        }
        if (mode.isCurrentMode("Hypixel")) {
			this.setDisplayName("Hypixel");
            if (isInsideBlock()) {
                event.setBoundingBox(null);
                event.setCancelled(true);
            }
        }
        if (mode.isCurrentMode("Vanilla")) {
			this.setDisplayName("Vanilla");
            if (isInsideBlock()) {
                event.setBoundingBox(null);
                event.setCancelled(true);
            }
        }
        if (mode.isCurrentMode("NCP")) {
			this.setDisplayName("NCP");
            if (this.isInBlock(mc.thePlayer, 0.0f) && !mc.gameSettings.keyBindSprint.isKeyDown() && event.getPos().getY() > mc.thePlayer.getEntityBoundingBox().minY - 0.4 && event.getPos().getY() < mc.thePlayer.getEntityBoundingBox().maxY + 1.0) {
                mc.thePlayer.jumpMovementFactor = 0;
                event.setBoundingBox(null);
                event.setCancelled(true);
            }
            if (this.isInBlock(mc.thePlayer, 0.0f) && mc.gameSettings.keyBindSprint.isKeyDown()) {
                event.setBoundingBox(null);
                event.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void onPacketSend(EventReceivePacket event) {
        if ( event.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = (S02PacketChat) event.getPacket();
            if (packet.getChatComponent().getFormattedText().contains("You cannot go past the border.")) {
                event.setCanceled(true);
            }
        }
        if (mode.isCurrentMode("Vanilla")) {
            if (event.getPacket() instanceof S08PacketPlayerPosLook && moveUnder == 2) {
                moveUnder = 1;
            }
        }
        if (mode.isCurrentMode("NCP")) {
                if (event.getPacket() instanceof C03PacketPlayer && !mc.thePlayer.isMoving() && mc.thePlayer.posY == mc.thePlayer.lastTickPosY) {
                    event.setCanceled(true);
                }
            }
    }

    
	@EventTarget
	    public void onMove(EventMove event) {
		mc.thePlayer.noClip = true;
	        if (mode.isCurrentMode("Hypixel")) {
	            if (isInsideBlock()) {
	                if (mc.gameSettings.keyBindJump.isKeyDown()) event.setY(mc.thePlayer.motionY += 0.09f);
	                else if (mc.gameSettings.keyBindSneak.isKeyDown()) event.setY(mc.thePlayer.motionY -= 0.00);
	                else event.setY(mc.thePlayer.motionY = 0.0f);
	                setMoveSpeed(event, 0.3);
	            }
	        }
	        if (mode.isCurrentMode("Aris")) {
	            if (isInsideBlock() && mc.thePlayer.isSneaking()) {
	                final float yaw = mc.thePlayer.rotationYaw;
	                phasing = true;
	                mc.thePlayer.getEntityBoundingBox().offsetAndUpdate(distance.getValueState() * -Math.sin(Math.toRadians(yaw)), 0.0, distance.getValueState() * Math.cos(Math.toRadians(yaw)));
	            } else {
	                phasing = false;
	            }
	        }
	        if (mode.isCurrentMode("Vanilla")) {
	            if (isInsideBlock()) {
	                if (mc.gameSettings.keyBindJump.isKeyDown()) {
	                    event.setY(mc.thePlayer.motionY = verticalspeed.getValueState());
	                } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
	                    event.setY(mc.thePlayer.motionY = -verticalspeed.getValueState());
	                } else {
	                    event.setY(mc.thePlayer.motionY = 0.0);
	                }
	                setMoveSpeed(event, vanillaspeed.getValueState());
	            }
	        }
	    }

	
	 
	
    @Override
    public void onEnable() {
        phasing = false;
    }

    @Override
    public void onDisable() {
        this.delay = 0;
        this.NCPSetup = false;
        timer.reset();
    }
	
    private void setMoveSpeed(EventMove event, double speed) {
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
    
    
	   private boolean isInBlock(Entity e, float offset) {
	        for (int x = MathHelper.floor_double(e.getEntityBoundingBox().minX); x < MathHelper.floor_double(e.getEntityBoundingBox().maxX) + 1; ++x) {
	            for (int y = MathHelper.floor_double(e.getEntityBoundingBox().minY); y < MathHelper.floor_double(e.getEntityBoundingBox().maxY) + 1; ++y) {
	                for (int z = MathHelper.floor_double(e.getEntityBoundingBox().minZ); z < MathHelper.floor_double(e.getEntityBoundingBox().maxZ) + 1; ++z) {
	                    final Block block = mc.theWorld.getBlockState(new BlockPos(x, y + offset, z)).getBlock();
	                    if (block != null && !(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
	                        final AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.theWorld, new BlockPos(x, y + offset, z), mc.theWorld.getBlockState(new BlockPos(x, y + offset, z)));
	                        if (boundingBox != null && e.getEntityBoundingBox().intersectsWith(boundingBox)) {
	                            return true;
	                        }
	                    }
	                }
	            }
	        }
	        return false;
	    }


	    private boolean isInsideBlock() {
	        for (int x = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; x++) {
	            for (int y = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minY); y < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxY) + 1; y++) {
	                for (int z = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; z++) {
	                    Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
	                    if ((block != null) && (!(block instanceof BlockAir))) {
	                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.theWorld, new BlockPos(x, y, z), mc.theWorld.getBlockState(new BlockPos(x, y, z)));
	                        if ((block instanceof BlockHopper)) {
	                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
	                        }
	                        if ((boundingBox != null) && (mc.thePlayer.getEntityBoundingBox().intersectsWith(boundingBox))) {
	                            return true;
	                        }
	                    }
	                }
	            }
	        }
	        return false;
	    }

	    private void teleport(double dist) {
	        double forward = mc.thePlayer.movementInput.moveForward;
	        double strafe = mc.thePlayer.movementInput.moveStrafe;
	        float yaw = mc.thePlayer.rotationYaw;
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
	        double x = mc.thePlayer.posX;
	        double y = mc.thePlayer.posY;
	        double z = mc.thePlayer.posZ;
	        double xspeed = forward * dist * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * dist * Math.sin(Math.toRadians(yaw + 90.0F));
	        double zspeed = forward * dist * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * dist * Math.cos(Math.toRadians(yaw + 90.0F));
	        mc.thePlayer.setPosition(x + xspeed, y, z + zspeed);

	    }
	    
}
