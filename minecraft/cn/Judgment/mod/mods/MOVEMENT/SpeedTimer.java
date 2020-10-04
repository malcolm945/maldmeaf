package cn.Judgment.mod.mods.MOVEMENT;

public class SpeedTimer
{
    private long time;
    private long prevMS;
    
    public SpeedTimer() {
        this.prevMS = 0L;
        this.time = System.nanoTime() / 1000000L;
    }
    
    public boolean delay(final float milliSec) {
        return this.getTime() - this.prevMS >= milliSec;
    }
    
    public long getTime() {
        return System.nanoTime() / 1000000L;
    }
    
    public boolean hasTimeElapsed(final long time, final boolean reset) {
        if (this.time() >= time) {
            if (reset) {
                this.reset();
            }
            return true;
        }
        return false;
    }
    
    public boolean hasTimeElapsed(final long time) {
        return this.time() >= time;
    }
    
    public boolean hasTicksElapsed(final int ticks) {
        return this.time() >= 1000 / ticks - 50;
    }
    
    public boolean hasTicksElapsed(final int time, final int ticks) {
        return this.time() >= time / ticks - 50;
    }
    
    public long time() {
        return System.nanoTime() / 1000000L - this.time;
    }
    
    public void resetsig() {
        this.prevMS = this.getTime();
    }
    
    public void reset() {
        this.time = System.nanoTime() / 1000000L;
    }
}
