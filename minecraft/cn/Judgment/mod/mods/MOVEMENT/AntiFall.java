package cn.Judgment.mod.mods.MOVEMENT;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventMove;
import cn.Judgment.events.EventPacket;
import cn.Judgment.events.EventReceivePacket;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.mod.ModManager;
import cn.Judgment.util.timeUtils.TimeHelper;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

public class AntiFall extends Mod{
	

	private TimeHelper timer = new TimeHelper();
    private boolean saveMe;
    public static Value<Boolean> VOID = new Value("AntiFall_Void", true);
    public Value<Double> DISTANCE = new Value<Double>("AntiFall_distance", 5.0, 1.0, 20.0, 1.0);
    public Value<String> mode = new Value("AntiFall", "Mode", 0);


    public AntiFall() {
    	super("AntiFall", Category.MOVEMENT);
    	this.mode.mode.add("Hypixel");
  
}

    @EventTarget
    public void onPre(EventMove em) {
     this.setDisplayName("Hypixel");
            if (this.isBlockUnder() && !this.saveMe) {
                if (ModManager.getModByName("NoFall").isEnabled()) return;
            }

            if ( mc.thePlayer.capabilities.isFlying) {
                return;
            }
            if (this.saveMe && this.timer.delay(350.0f) ||  mc.thePlayer.isCollidedVertically) {
                this.saveMe = false;
                this.timer.reset();
                return;
            }
            int dist =  this.DISTANCE.getValueState().intValue();
            if (!(  mc.thePlayer.fallDistance < (float)dist ||ModManager.getModByName("ZoomFly").isEnabled() || ModManager.getModByName("ZoomFly").isEnabled() ||  mc.theWorld.getBlockState(new BlockPos( mc.thePlayer.posX,  mc.thePlayer.posY - 1.0,  mc.thePlayer.posZ)).getBlock() != Blocks.air || ((Boolean)this.VOID.getValueState().booleanValue() && this.isBlockUnder()))&&this.timer.delay(900.0f) ) {
              	if(this.timer.delay(400.0f))
              	   mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + (double) dist,
                           mc.thePlayer.posZ, false));
              timer.reset();
            }
        
}


 
@EventTarget
  public void onPre(EventPacket ep) {
 if (!ep.isIncoming()) return;
        if (!(ep.getPacket() instanceof S08PacketPlayerPosLook)) return;
        if (mc.thePlayer.fallDistance > 0.0f) {
            mc.thePlayer.fallDistance = 0.0f;
        }
        mc.thePlayer.motionZ = 0.0;
        mc.thePlayer.motionX = 0.0;
        this.saveMe = false;
        this.timer.reset();
}



 private boolean isBlockUnder() {
        int i = (int)mc.thePlayer.posY;
        while (i > 0) {
            BlockPos pos = new BlockPos(mc.thePlayer.posX, (double)i, mc.thePlayer.posZ);
            if (!(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)) {
                return true;
            }
            --i;
        }
        return false;
    }

}

