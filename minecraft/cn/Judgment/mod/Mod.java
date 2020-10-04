package cn.Judgment.mod;

import com.darkmagician6.eventapi.EventManager;

import cn.Judgment.Client;
import cn.Judgment.Value;
import cn.Judgment.ui.ClientNotification;
import cn.Judgment.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

public class Mod {
	

	public static Minecraft mc = Minecraft.getMinecraft();
    public Value showValue;
    private String name;
    private int color1;
    private int key;
    private Category category;
    private boolean isEnabled;
    private String desc;
    public boolean openValues;
    public double arrowAnlge;
    public double animateX;
    public double hoverOpacity;
    public float circleValue;
    public boolean canSeeCircle;
    public int[] circleCoords;
    public boolean clickedCircle;
    public String displayName = "";
    
    public static boolean TS = false;
    
	public Mod(String name, Category category) {
        this.arrowAnlge = 0.0;
        this.animateX = 0.0;
        this.hoverOpacity = 0.0;
        this.name = name;
        this.key = -1;
        this.category = category;
        this.circleCoords = new int[2];
    }
		


	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		 String suffix = displayName.toString();
	      if(suffix.isEmpty()) {
	         this.displayName = suffix;
	      } else {
		  this.displayName = String.format("\u00a77\u00a7f%s\u00a77", EnumChatFormatting.WHITE + displayName+" ");
	
	      
	      }
	}

	public void onEnable() {
		
    }

    public void onDisable() {
    	
    }

    public void onToggle() {
    }

    public void disableValues() {
    }

    public String getValue() {
        if (this.showValue == null) {
            return "";
        }
        return this.showValue.isValueMode ? this.showValue.getModeAt(this.showValue.getCurrentMode()) : String.valueOf(this.showValue.getValueState());
    }

    public void set(boolean state) {
        this.set(state, false);
        Client.instance.fileMgr.saveMods();
    }

    public void set(boolean state, boolean safe) {
        this.isEnabled = state;
        this.onToggle();
        if (state) {
            if (this.mc.theWorld != null) {
       			ClientUtil.sendClientMessage(this.getName() + " \247aEnabled", ClientNotification.Type.SUCCESS);
 	
                this.onEnable();
            }
            EventManager.register(this);
        } else {
            if (this.mc.theWorld != null) {
  			ClientUtil.sendClientMessage(this.getName() + " \247cDisabled", ClientNotification.Type.ERROR);
                this.onDisable();
            }
            EventManager.unregister(this);
        }
        if (safe) {
        	Client.instance.fileMgr.saveMods();
        }
    }
    public String getName() {
        return this.name;
    }
    
    public void setColor(int color) {
        this.color1 = color;
    }

    public int getKey() {
        return this.key;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Category getCategory() {
        return this.category;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean hasValues() {
        for (Value value : Value.list) {
            String name = value.getValueName().split("_")[0];
            if (!name.equalsIgnoreCase(this.getName())) continue;
            return true;
        }
        return false;
    }
    
    public void portMove(float yaw, float multiplyer, float up) {
        double moveX = -Math.sin(Math.toRadians((double)yaw)) * (double)multiplyer;
        double moveZ = Math.cos(Math.toRadians((double)yaw)) * (double)multiplyer;
        double moveY = (double)up;
        this.mc.thePlayer.setPosition(moveX + this.mc.thePlayer.posX, moveY + this.mc.thePlayer.posY, moveZ + this.mc.thePlayer.posZ);
    }

    public void move(float yaw, float multiplyer, float up) {
        double moveX = -Math.sin(Math.toRadians((double)yaw)) * (double)multiplyer;
        double moveZ = Math.cos(Math.toRadians((double)yaw)) * (double)multiplyer;
        this.mc.thePlayer.motionX = moveX;
        this.mc.thePlayer.motionY = (double)up;
        this.mc.thePlayer.motionZ = moveZ;
    }
    
    public void move(float yaw, float multiplyer) {
        double moveX = -Math.sin(Math.toRadians((double)yaw)) * (double)multiplyer;
        double moveZ = Math.cos(Math.toRadians((double)yaw)) * (double)multiplyer;
        this.mc.thePlayer.motionX = moveX;
        this.mc.thePlayer.motionZ = moveZ;
    }



	public void toggle() {
	      try {
	         if(this.isEnabled()) {
	            this.set(false);
	         } else {
	            this.set(true);
	         }
	      } catch (Exception var2) {
	         var2.printStackTrace();
	      }

	   }



	public void setEnabled(boolean b) {
		// TODO 自动生成的方法存根
		
	}
	
	
}
