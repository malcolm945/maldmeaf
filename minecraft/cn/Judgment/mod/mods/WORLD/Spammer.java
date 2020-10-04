package cn.Judgment.mod.mods.WORLD;

import java.io.IOException;
import java.util.Random;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.command.commands.CommandSpammer;
import cn.Judgment.events.EventUpdate;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.util.timeUtils.TimeHelper;

public class Spammer extends Mod {
	
	TimeHelper timer = new TimeHelper();
	private Value<Double> delay = new Value<Double>("Spammer_Delay", Double.valueOf(1.0D), Double.valueOf(0.1D), Double.valueOf(10.0D), 0.1D);
	private Value<Double> random = new Value<Double>("Spammer_Random", Double.valueOf(6D), Double.valueOf(1D), Double.valueOf(36D), 1D);
	
	public static String message = "Judgment Client kill your mother";

    	public Spammer() {
    		super("Spammer", Category.WORLD);
        try {
            CommandSpammer.loadMessage();;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @EventTarget
    public void onUpdate(EventUpdate e) {
    	if(this.timer.isDelayComplete(((Double)this.delay.getValueState()).longValue() * 1000L)) {
    		this.mc.thePlayer.sendChatMessage("[Judgment] "+ message + " [" + getRandomString(this.random.getValueState().doubleValue()) + "]");
    		timer.reset();
    	}
    }
    
    public static String getRandomString(double d)
    {
      String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
      Random random = new Random();
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < d; i++)
      {
        int number = random.nextInt(62);
        sb.append(str.charAt(number));
      }
      return sb.toString();
    }
}