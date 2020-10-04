package cn.Judgment.mod.mods.PLAYER;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventPreMotion;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;

public class Lobby extends Mod{
	
	   public static Value mode = new Value("Lobby", "Mode", 0);
	   
    public Lobby() {
        super("Lobby", Category.PLAYER);
	      mode.mode.add("Lobby");
	      mode.mode.add("Kill");
  }
    
    @Override
    public void onEnable() {

    	if(!mode.isCurrentMode("Kill"))
    		mc.thePlayer.sendChatMessage("/lobby");
    	else {
    		mc.thePlayer.sendChatMessage("/kill");
    		this.toggle();
    	}
    }
    
    @EventTarget
    private void onpre(EventPreMotion event) {
    	if(mc.currentScreen == null) {
        	if(!mode.isCurrentMode("Kill")) {
        		new Thread(()->{
        			try {
    					Thread.sleep(350L);
    				} catch (InterruptedException e) {
    					e.printStackTrace();
    				}
        		mc.thePlayer.sendChatMessage("/back");
        		}).start();;

        	}
            	this.toggle();
    	}
	}
}
