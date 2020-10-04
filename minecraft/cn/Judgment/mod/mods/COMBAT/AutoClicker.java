
package cn.Judgment.mod.mods.COMBAT;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventUpdate;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Mouse;

public class AutoClicker extends Mod {
    private Value<Double> aps = new Value<Double>("AutoClicker_APS", 8.0, 0.0, 20.0, 1.0);
    private Value<Boolean> random = new Value<Boolean>("AutoClicker_Random", true);
    private Random rand = new Random();
    private long delayCount;

    public AutoClicker() {
        super("AutoClicker", Category.COMBAT);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
    	 int aps = this.aps.getValueState().intValue();
    	int delayNeed = 1000 / aps;
       
        if (this.random.getValueState().booleanValue()) {
            delayNeed += this.rand.nextInt(80) - 40;
        }
        if (System.currentTimeMillis() - this.delayCount >= (long)delayNeed) {
            if (Mouse.isButtonDown((int)0) && this.mc.currentScreen == null) {
                if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.entityHit != null) {
                    this.mc.thePlayer.swingItem();
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(this.mc.objectMouseOver.entityHit, C02PacketUseEntity.Action.ATTACK));
                } else {
                    this.mc.thePlayer.swingItem();
                }
                this.delayCount = System.currentTimeMillis();
            }
            if (Mouse.isButtonDown((int)1) && this.mc.currentScreen == null) {
                this.mc.rightClickMouse();
                this.delayCount = System.currentTimeMillis();
            }
        }
    }
    @Override
    public void onDisable() {
        super.onDisable();

    }
    public void onEnable() {
    	super.isEnabled();

    }
}

