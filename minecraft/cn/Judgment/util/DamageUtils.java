package cn.Judgment.util;

import cn.Judgment.mod.mods.MOVEMENT.ZoomFly;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;

public class DamageUtils{
	private Minecraft mc = Minecraft.getMinecraft();
    //Damage method.
    //Jump potion supported.
    public static void damage(){
        double fallDist = 0;
        double offset1 = 4.999997959349E-2 * (MoveUtils.getJumpEffect() + 1);
        double offset2 = 0.00000114514 * (MoveUtils.getJumpEffect() + 1);
        while (fallDist < (3.5 + MoveUtils.getJumpEffect() + (ZoomFly.uhc.getValueState().booleanValue() ? 1f : 0))){
            sendPacket(offset1,false);
            sendPacket(0,false);
            sendPacket(offset2,false);
            fallDist+=0.05 * (MoveUtils.getJumpEffect() + 1);
        }
        sendPacket(0.0,true);
    }
    static void sendPacket(double addY,boolean ground){
    	Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(
    			Minecraft.getMinecraft().thePlayer.posX,Minecraft.getMinecraft().thePlayer.posY+addY,Minecraft.getMinecraft().thePlayer.posZ,Minecraft.getMinecraft().thePlayer.rotationYawHead,Minecraft.getMinecraft().thePlayer.rotationPitch,ground
        ));
    }
}