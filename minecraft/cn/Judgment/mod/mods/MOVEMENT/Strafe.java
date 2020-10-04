package cn.Judgment.mod.mods.MOVEMENT;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventPreMotion;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.mod.ModManager;
import cn.Judgment.util.PlayerUtil;

public class Strafe
extends Mod {
    public static Value<String> mode = new Value("Strafe", "Mode", 0);

    public Strafe() {
        super("Strafe", Category.MOVEMENT);
        Strafe.mode.mode.add("NCP");
        Strafe.mode.mode.add("AAC");
        this.showValue = mode;
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        this.showValue = mode;
        if (PlayerUtil.MovementInput() && !ModManager.getModByName("Speed").isEnabled()) {
            if (this.mc.gameSettings.keyBindJump.pressed) {
            	this.mc.thePlayer.setSpeed(mode.isCurrentMode("NCP") ? 0.26 : 0.23);
            } else {
            	this.mc.thePlayer.setSpeed(mode.isCurrentMode("AAC") ? 0.17 : 0.135);
            }
        }
    }

}