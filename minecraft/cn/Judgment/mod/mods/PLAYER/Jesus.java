package cn.Judgment.mod.mods.PLAYER;

import java.util.ArrayList;
import java.util.Arrays;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventLiquidCollide;
import cn.Judgment.events.EventPreMotion;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.util.PlayerUtil;
import cn.Judgment.util.timeUtils.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import optifine.MathUtils;

public class Jesus
extends Mod {
	int stage, water;
    TimeHelper timer = new TimeHelper();
    private int tick;
    private Value<String> mode = new Value("Jesus", "Mode", 0);
    
    public Jesus() {
        super("Jesus", Category.MOVEMENT);
        this.mode.mode.add("Jump");
		this.mode.mode.add("Sprint");
        this.showValue = this.mode;
    }
    
    @Override
    public void onEnable(){
    	stage = 0;
    	water = 0;
    }


    @EventTarget
    public void onUpdate(EventPreMotion em) {

    	boolean sh = shouldJesus();
		if(mc.thePlayer.isInWater() && !mc.thePlayer.isSneaking() && sh && !mode.isCurrentMode("AAC")){
			            	  		 mc.thePlayer.motionY = 0.09;
		}
    	if(mode.isCurrentMode("Jump")||mode.isCurrentMode("Sprint")){       
    		this.setDisplayName(mode.isCurrentMode("Sprint")? "Sprint" : "Jump");
    		if(mc.thePlayer.onGround && !mc.thePlayer.isInWater() && sh){
    			stage = 1;
				timer.reset();
    		}
    		if(stage > 0 && !timer.delay(mode.isCurrentMode("Sprint")? 1200 : 2500)){
    			if((mc.thePlayer.isCollidedVertically  && !MathUtils.isOnGround(0.001)) || mc.thePlayer.isSneaking()){
    				stage = -1;
    			}
    			mc.thePlayer.motionX *= 0;
    			mc.thePlayer.motionZ *= 0;
    			if(!PlayerUtil.isInLiquid() && !mc.thePlayer.isInWater()){
    				MathUtils.setMotion(0.25 + MathUtils.getSpeedEffect() * 0.05);
    			}
    			double motionY = getMotionY(stage);
    			if(motionY != -999){
    				mc.thePlayer.motionY = motionY;
    				
    			}
    				
    				stage +=1;
    			}
    		}
    	if(mode.isCurrentMode("AAC")){    
    		this.setDisplayName("AAC");
			if(mc.thePlayer.isInWater() && isTotalOnLiquid(-1)){
				if(water == 1){
					stage ++;
            		double motionY = -0.021;
            		if(stage >= 2){
            			if(stage == 2){
                			motionY = -0.004;
            			}else{
            				stage = 0;
                			motionY = 0.025;
            			}
            		}
            		mc.thePlayer.motionX *= 1.17;
            		mc.thePlayer.motionZ *= 1.17;
            		mc.thePlayer.motionY = 0;
            		double x = mc.thePlayer.posX;
            		double z = mc.thePlayer.posZ;
            		double y = mc.thePlayer.posY;
                	if(!mc.gameSettings.keyBindJump.isPressed())
                		mc.thePlayer.setPosition(x, y + motionY, z);
            		
				}
				water = 1;
        		
        	}else{
        		water = 0;
        	}
		}
	
    }
    
    @EventTarget
    public void onBlockCollide(EventLiquidCollide ebb) {
		
		int nigga = -1;
		 if(mode.isCurrentMode("AAC"))return;
        if (ebb.getPos().getY() + 0.9 < mc.thePlayer.boundingBox.minY) {
        	if(mc.theWorld.getBlockState(ebb.getPos()).getProperties().get(BlockLiquid.LEVEL) instanceof Integer){
    			nigga = (int)mc.theWorld.getBlockState(ebb.getPos()).getProperties().get(BlockLiquid.LEVEL);
    		} 
        	if(nigga <= 4){
        		ebb.setBounds(new AxisAlignedBB(ebb.getPos().getX(), ebb.getPos().getY(), ebb.getPos().getZ(), ebb.getPos().getX() + 1, ebb.getPos().getY() + (mode.isCurrentMode("Normal")? 1 : 0.999), ebb.getPos().getZ() + 1));
                ebb.setCancelled(shouldSetBoundingBox());
        	}
            
        }
    }

    public static boolean isOnLiquid() {
        if (Minecraft.thePlayer == null) return false;
        boolean onLiquid = false;
        final int y = (int) Minecraft.thePlayer.getEntityBoundingBox().offset(0.0D, -0.01D, 0.0D).minY;
        for (int x = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().maxX) + 1; x++) {
            for (int z = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().maxZ) + 1; z++) {
                final Block block = Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null && block.getMaterial() != Material.air) {
                    if (!(block instanceof BlockLiquid)) return false;
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
    }
    public static boolean isOnLiquid(double profondeur)
	  {
	    boolean onLiquid = false;
	    
	    if(Minecraft.theWorld.getBlockState(new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - profondeur, Minecraft.thePlayer.posZ)).getBlock().getMaterial().isLiquid()) {
	      onLiquid = true;
	    }
	    return onLiquid;
	  }
    public static boolean isTotalOnLiquid(double profondeur)
	  {	    
	    for(double x = Minecraft.thePlayer.boundingBox.minX; x < Minecraft.thePlayer.boundingBox.maxX; x +=0.01f){
	    	
			for(double z = Minecraft.thePlayer.boundingBox.minZ; z < Minecraft.thePlayer.boundingBox.maxZ; z +=0.01f){
				Block block = Minecraft.theWorld.getBlockState(new BlockPos(x, Minecraft.thePlayer.posY - profondeur,z)).getBlock();
  			if(!(block instanceof BlockLiquid) && !(block instanceof BlockAir)){
  				return false;
  			}
  		}
		}
	    return true;
	  }
    
	    boolean shouldJesus(){
	    	double x = mc.thePlayer.posX; double y = mc.thePlayer.posY; double z = mc.thePlayer.posZ;
	    	ArrayList<BlockPos>pos = new ArrayList<BlockPos>(Arrays.asList(new BlockPos(x + 0.3, y, z+0.3),
	    			 new BlockPos(x - 0.3, y, z+0.3),new BlockPos(x + 0.3, y, z-0.3),new BlockPos(x - 0.3, y, z-0.3)));
	    	for(BlockPos po : pos){
	    		if(!(mc.theWorld.getBlockState(po).getBlock() instanceof BlockLiquid))
	    			continue;
	    		if(mc.theWorld.getBlockState(po).getProperties().get(BlockLiquid.LEVEL) instanceof Integer){
	        		if((int)mc.theWorld.getBlockState(po).getProperties().get(BlockLiquid.LEVEL) <= 4){
	        			return true;
	        		}
	        	}
	    	}
	    	
	    	
	    	return false;
	    }
	    	    public double getMotionY(double stage){
	    	stage --;
	    	double[] motion = new double[]{0.500,0.484,0.468,0.436,0.404,0.372,0.340,0.308,0.276,0.244,0.212,0.180,0.166,0.166,
	    			0.156,0.123,0.135,0.111,0.086,0.098,0.073,0.048,0.06,0.036,0.0106,0.015,0.004,0.004,0.004,0.004,
	    					-0.013,-0.045,-0.077,-0.109};
	    	if(mode.isCurrentMode("Sprint")) {
	    		motion = new double[]{0.180,0.166,0.166,
		    			0.156,0.123,0.135,0.111,0.086,0.098,0.073,0.048,0.06,0.036,0.0106,0.015,0.004
		    					-0.013,-0.045,-0.077,-0.109};
	    	}else {
	    		motion = new double[]{0.500,0.484,0.468,0.436,0.404,0.372,0.340,0.308,0.276,0.244,0.212,0.180,0.166,0.166,
		    			0.156,0.123,0.135,0.111,0.086,0.098,0.073,0.048,0.06,0.036,0.0106,0.015,0.004,0.004,0.004,0.004,
		    					-0.013,-0.045,-0.077,-0.109};
			}
	    	if(stage < motion.length && stage >= 0)
	    		return motion[(int)stage];
	    	else
	    		return -999;
	    	
	    }
	    private boolean shouldSetBoundingBox() { 	
	        return (!mc.thePlayer.isSneaking()) && (mc.thePlayer.fallDistance < 12.0F);
	    }
}