package cn.Judgment.mod.mods.PLAYER;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.events.EventReceivePacket;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.mod.mods.COMBAT.Killaura;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;




public class NoRotate extends Mod {
	    public NoRotate() {
	        super("NoRotate", Category.PLAYER);
	  }
	    
	    @EventTarget
	    public void onEvent(EventReceivePacket event) {
	        this.setColor(-564656);
	        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
	            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)event.getPacket();
	            if (this.mc.thePlayer.rotationYaw > -90.0f && this.mc.thePlayer.rotationPitch < 90.0f || Killaura.target != null) {
	                packet.yaw = this.mc.thePlayer.rotationYaw;
	                packet.pitch = this.mc.thePlayer.rotationPitch;
	            }
	        }
	    }
	 
	    
}

