
package cn.Judgment.mod.mods.PLAYER;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.events.EventMove;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;

public class Respawn extends Mod {
    public Respawn() {
		super("Respawn", Category.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventMove event) {
        this.setColor(-6697780);
        if (!this.mc.thePlayer.isEntityAlive()) {
            this.mc.thePlayer.respawnPlayer();
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