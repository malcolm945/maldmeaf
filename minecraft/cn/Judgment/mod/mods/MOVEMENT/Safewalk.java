package cn.Judgment.mod.mods.MOVEMENT;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.events.EventMove;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;

public class Safewalk extends Mod {

	public Safewalk() {
		super("Safewalk", Category.MOVEMENT);
	}
	
	@EventTarget
	public void onMove(EventMove event) {
		double x = event.getX();
        double y = event.getY();
        double z = event.getZ();
        if (mc.thePlayer.onGround) {
            double increment = 0.05;
            while (x != 0.0) {
                if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(x, -1.0, 0.0)).isEmpty()) {
                    break;
                }
                if (x < increment && x >= -increment) {
                    x = 0.0;
                }
                else if (x > 0.0) {
                    x -= increment;
                }
                else {
                    x += increment;
                }
            }
            while (z != 0.0) {
                if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, -1.0, z)).isEmpty()) {
                    break;
                }
                if (z < increment && z >= -increment) {
                    z = 0.0;
                }
                else if (z > 0.0) {
                    z -= increment;
                }
                else {
                    z += increment;
                }
            }
            while (x != 0.0 && z != 0.0 && mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(x, -1.0, z)).isEmpty()) {
                if (x < increment && x >= -increment) {
                    x = 0.0;
                }
                else if (x > 0.0) {
                    x -= increment;
                }
                else {
                    x += increment;
                }
                if (z < increment && z >= -increment) {
                    z = 0.0;
                }
                else if (z > 0.0) {
                    z -= increment;
                }
                else {
                    z += increment;
                }
            }
        }
        event.setX(x);
        event.setY(y);
        event.setZ(z);
	}

}
