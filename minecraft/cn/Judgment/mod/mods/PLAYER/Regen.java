package cn.Judgment.mod.mods.PLAYER;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventPreMotion;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.util.Colors;
import cn.Judgment.util.timeUtils.TimeHelper;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Regen
extends Mod {
    private Value<Double> packet;
    TimeHelper delay = new TimeHelper();
	private Value<Double> packetdelay;

    public Regen() {
        super("Regen", Category.PLAYER);
        this.showValue = this.packet = new Value<Double>("Regen_Packets", 500.0, 1.0, 1000.0, 1.0);
        this.showValue = this.packetdelay = new Value<Double>("Regen_PacketsDelay", 500.0, 1.0, 1000.0, 1.0);
    }

    @EventTarget
    public void onMotion(EventPreMotion event) {
    	if (this.delay.isDelayComplete(this.packetdelay.getValueState().longValue())) {
        if (this.mc.thePlayer.getHealth() < 20.0f && this.mc.thePlayer.getFoodStats().getFoodLevel() >= 19) {
            int i = 0;
            while ((double)i < this.packet.getValueState()) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
                ++i;
            }
        }
        this.delay.reset();
    	}
    }
    }
