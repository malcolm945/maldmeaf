package cn.Judgment.mod.mods.COMBAT;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventPreMotion;
import cn.Judgment.events.EventReceivePacket;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.util.Colors;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity extends Mod {
   private static final boolean AACPull = false;
private double motionY;
private double motionX;
   private double motionZ;
   private Value<String> mode;
   private Value<Double> vertical;
   private Value<Double> horizontal;
   private Value<Double> xz;
   private Value<Double> y;
private String modname;

   public Velocity() {
      super("Velocity",Category.COMBAT);
      this.mode = new Value<String>("Velocity", "Mode", 0);
      this.vertical = new Value<Double>("Velocity_Vertical", 0.0, 0.0, 1.0, 0.01);
      this.horizontal = new Value<Double>("Velocity_Horizontal", 0.0, 0.0, 1.0, 0.01);
      this.xz = new Value<Double>("Velocity_X/Z", 0.0, 0.0, 1.0, 0.01);
      this.y = new Value<Double>("Velocity_Y", 0.0, 0.0, 1.0, 0.01);
      this.mode.addValue("Simple");
      this.mode.addValue("AAC1");
      this.mode.addValue("AAC2");
     
   }


   @EventTarget
   public void onEvent(final EventPreMotion event) {
       this.showValue = null;
       this.setColor(Colors.AQUA.c);
           if (this.mode.isCurrentMode("AAC1")) {
        	   this.setDisplayName("AAC1");
           if (this.mc.thePlayer.hurtTime == 1 || this.mc.thePlayer.hurtTime == 2 || this.mc.thePlayer.hurtTime == 3 || this.mc.thePlayer.hurtTime == 4 || this.mc.thePlayer.hurtTime == 5 || this.mc.thePlayer.hurtTime == 6 || this.mc.thePlayer.hurtTime == 7 || this.mc.thePlayer.hurtTime == 8) {
               if (this.mc.thePlayer.onGround) {
                   return;
               }
               double yaw = this.mc.thePlayer.rotationYawHead;
               yaw = Math.toRadians(yaw);
               double dX = (- Math.sin(yaw)) * 0.08;
               double dZ = Math.cos(yaw) * 0.08;
               if (this.mc.thePlayer.getHealth() >= 6.0f) {
                   this.mc.thePlayer.motionX = dX;
                   this.mc.thePlayer.motionZ = dZ;
               }
           }
       }
       
       else if (this.mode.isCurrentMode("AAC2")) {
    	   this.setDisplayName("AAC2");
           if (this.mc.thePlayer.hurtTime > 0 && this.mc.thePlayer.fallDistance < 3.0f) {
               if (this.mc.thePlayer.moveForward == 0.0f && this.mc.thePlayer.moveStrafing == 0.0f) {
                   this.mc.thePlayer.motionY -= this.y.getValueState();
                   this.mc.thePlayer.motionX *= this.xz.getValueState();
                   this.mc.thePlayer.motionZ *= this.xz.getValueState();
                   this.mc.thePlayer.motionY += this.y.getValueState();
               } else {
                   this.mc.thePlayer.motionY -= this.y.getValueState();
                   this.mc.thePlayer.motionX *= (this.xz.getValueState()) + 0.2;
                   this.mc.thePlayer.motionZ *= (this.xz.getValueState()) + 0.2;
                   this.mc.thePlayer.motionY += this.y.getValueState();
               }
           }
       }
   }
   

   @EventTarget
   public void onEvent(EventReceivePacket event) {
	   if (this.mode.isCurrentMode("Simple") ) {
      	 this.setDisplayName("Simple");
	     if (event.getPacket() instanceof S12PacketEntityVelocity) {
	         S12PacketEntityVelocity packet = (S12PacketEntityVelocity)event.getPacket();
	         if (packet.entityID == this.mc.thePlayer.getEntityId()) {
	             if (((Double)this.vertical.getValueState()).doubleValue() == 0.0D && ((Double)this.horizontal.getValueState()).doubleValue() == 0.0D) {
	                event.cancel = true;
	             } else {
	                packet.motionX = (int)((double)packet.motionX * ((Double)this.horizontal.getValueState()).doubleValue());
	                packet.motionZ = (int)((double)packet.motionZ * ((Double)this.horizontal.getValueState()).doubleValue());
	                packet.motionY = (int)((double)packet.motionY * ((Double)this.vertical.getValueState()).doubleValue());
	             }
	          }
	       }
	      
	      
      if (event.getPacket() instanceof S27PacketExplosion) {
         S27PacketExplosion packet = (S27PacketExplosion)event.getPacket();
            if (((Double)this.vertical.getValueState()).doubleValue() == 0.0D && ((Double)this.horizontal.getValueState()).doubleValue() == 0.0D) {
               event.cancel = true;
            } else {
               packet.field_149152_f = (float)((double)packet.field_149152_f * ((Double)this.horizontal.getValueState()).doubleValue());
               packet.field_149153_g = (float)((double)packet.field_149153_g * ((Double)this.horizontal.getValueState()).doubleValue());
               packet.field_149159_h = (float)((double)packet.field_149159_h * ((Double)this.vertical.getValueState()).doubleValue());
            }
         }
      }


   }
}

