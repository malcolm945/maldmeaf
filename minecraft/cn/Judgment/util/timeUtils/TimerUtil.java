package cn.Judgment.util.timeUtils;

import net.minecraft.client.Minecraft;

public final class TimerUtil {
    public double time;

    public TimerUtil() {
        this.time = (System.nanoTime() / 1000000l);
    }

    public boolean hasTimeElapsed(double time, boolean reset) {
        if (getTime() >= time) {
            if (reset) {
                reset();
            }

            return true;
        }

        return false;
    }

    public double getTime() {
        return System.nanoTime() / 1000000l - this.time;
    }

    public void reset() {
        this.time = (System.nanoTime() / 1000000l);
    }
    
    private long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }
    
    public boolean hasReached(double milliseconds) {
        if ((double)(this.getCurrentMS() - this.time) >= milliseconds) {
            return true;
        }
        return false;
    }


	public boolean delay(float milliSec) {
		if ((float) (this.getTime() - this.time) >= milliSec) {
			return true;
		}
		return false;
	}

}