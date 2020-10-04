package cn.Judgment.mod.mods.PLAYER;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventUpdate;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;

public class Timer
extends Mod {
    private Value<Double> speed = new Value<Double>("Timer_Speed", 1.0, 0.1, 10.0);

    public Timer() {
        super("Timer", Category.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setColor(-12799119);
        this.mc.timer.timerSpeed = this.speed.getValueState().floatValue();
    }
    @Override
    public void onDisable() {
        super.onDisable();
        this.mc.timer.timerSpeed = 1.0f;
      
    }
    
}


