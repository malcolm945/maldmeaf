	package cn.Judgment.mod.mods.RENDER;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Client;
import cn.Judgment.Value;
import cn.Judgment.events.Event;
import cn.Judgment.events.EventKeyboard;
import cn.Judgment.events.EventRender2D;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.mod.ModManager;
import cn.Judgment.mod.mods.COMBAT.Killaura;
import cn.Judgment.mod.mods.MOVEMENT.Scaffold;
import cn.Judgment.ui.ClientNotification;
import cn.Judgment.ui.font.FontLoaders;
import cn.Judgment.util.ClientUtil;
import cn.Judgment.util.Colors;
import cn.Judgment.util.R3DUtil;
import cn.Judgment.util.RenderUtil;
import cn.Judgment.util.RenderUtils;
import cn.Judgment.util.fontRenderer.FontManager;
import cn.Judgment.util.fontRenderer.UnicodeFontRenderer;
import cn.Judgment.util.fontRenderer.CFont.CFontRenderer;
import cn.Judgment.util.timeUtils.TimeHelper;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class HUD extends Mod {

	private Value<Double> rainbow = new Value<Double>("HUD_rainbow", 200.0, 0.0, 2000.0, 100.0);
	private Value<Double> bg = new Value<Double>("HUD_background", 100.0, 0.0, 255.0, 1);
	public Value<String> logo_mode = new Value("HUD", "Logo", 0);
	public static Value<String> Array_mode = new Value("HUD", "Array", 0);
	public Value<String> Effect_mode = new Value("HUD", "Effect", 0);
	public Value<Boolean> logo = new Value("HUD_Logo", true);
	public static Value<Boolean> hotbar = new Value("HUD_Hotbar", false);
	public static Value<Boolean> TabGui = new Value("HUD_TabGui", true);
	public Value<Boolean> array = new Value("HUD_ArrayList", true);
	public static Value<Boolean> ArmorHUD = new Value("HUD_ArmorHUD", true);
	public static Value<Boolean> info = new Value("HUD_info", true);
	
	
	
	TimeHelper time = new TimeHelper();
    public int debug;
	
	//药水显示
    public Minecraft mc = Minecraft.getMinecraft();
    CFontRenderer font = FontLoaders.kiona16;
    CFontRenderer font1 = FontLoaders.kiona28;
    CFontRenderer Sigma = FontLoaders.kiona16;
    CFontRenderer BLC = FontLoaders.Backs16;
    CFontRenderer Judgment = FontLoaders.kiona14;

    private int s = 0;
    int x = 0;
	
	   public static float YPort;
	   
	   public ArrayList categoryValues = new ArrayList();
	   public int currentCategoryIndex = 0;
	   public int currentModIndex = 0;
	   public int currentSettingIndex = 0;
	   public int screen = 0;
	  static int movingAlpha = 20;
    
    
    
    public int etb_color = new Color(105, 180, 255).getRGB();
	
	public HUD() {
		super("HUD", Category.RENDER);
		this.categoryValues.addAll(Arrays.asList(Category.values()));
		this.logo_mode.mode.add("Sigma");
		this.logo_mode.mode.add("Csgo");
		this.logo_mode.mode.add("Font");
		this.logo_mode.mode.add("Voice");
		this.logo_mode.mode.add("Judgment");
		this.logo_mode.mode.add("Flowly");
		
		this.Array_mode.mode.add("SIGMA");
		this.Array_mode.mode.add("Backs");
		this.Array_mode.mode.add("BLC");
		
		
		this.Effect_mode.mode.add("NONE");
		this.Effect_mode.mode.add("BLC");

		
		

	}
	
	FontRenderer fr = mc.fontRendererObj;
	boolean lowhealth = false;
	public static boolean toggledlyric = false;

	
	
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		
		
		
		 
		//Health low warning
		ScaledResolution sr = new ScaledResolution(mc);
		if(mc.thePlayer.getHealth() < 6 && !lowhealth) {
			ClientUtil.sendClientMessage("Your Health is Low!", ClientNotification.Type.WARNING);
			lowhealth = true;
		}
		if(mc.thePlayer.getHealth() > 6 && lowhealth) {lowhealth = false;}
		
		this.renderToggled(sr);
		
		this.renderPotionStatus(sr);
		if(this.logo.getValueState().booleanValue()) {
			this.renderLogo();
		}
		if(this.TabGui.getValueState().booleanValue()) {
			renderTabgui();
		 }
	

		
		//Coords Render START
		//("FPS:" + Minecraft.getDebugFPS()
		//String info = "\247bCoords: \247r" + (int)mc.thePlayer.posX + " " + (int)mc.thePlayer.posY + " " + (int)mc.thePlayer.posZ;
		String info = "\247bX: \247r" + (int)mc.thePlayer.posX + " "  + "\247bY: \247r" + (int)mc.thePlayer.posY + " "  + "\247bZ: \247r" + (int)mc.thePlayer.posZ;
		String fps = "\247bFPS: \247r" + (int)Minecraft.getDebugFPS() + (mc.isSingleplayer() ? " \247bPing: \247r0ms" : " \247bPing: \247r" + mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime() + "ms");
		String Dev = "\247bUser: \247r" + "Dev" + (mc.isSingleplayer() ? " \247bPing: \247r0ms" : " \247bPing: \247r" + mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime() + "ms");
		String Other = "\247bUser: \247r" + "Other" + (mc.isSingleplayer() ? " \247bPing: \247r0ms" : " \247bPing: \247r" + mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime() + "ms");

		 
	String text = (Object)((Object)EnumChatFormatting.RED) + "X" + (Object)((Object)EnumChatFormatting.WHITE) + ": " + MathHelper.floor_double(this.mc.thePlayer.posX) + " " + (Object)((Object)EnumChatFormatting.RED) + "Y" + (Object)((Object)EnumChatFormatting.WHITE) + ": " + MathHelper.floor_double(this.mc.thePlayer.posY) + " " + (Object)((Object)EnumChatFormatting.RED) + "Z" + (Object)((Object)EnumChatFormatting.WHITE) + ": " + MathHelper.floor_double(this.mc.thePlayer.posZ);
	String FPS = (Object)((Object)EnumChatFormatting.RED) + "FPS" + (Object)((Object)EnumChatFormatting.WHITE) + ": " + Minecraft.getDebugFPS() + " " + (Object)((Object)EnumChatFormatting.RED) + "PING" + (Object)((Object)EnumChatFormatting.WHITE) + ": " + mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime() + "ms" ;
	
	if(this.info.getValueState().booleanValue()) {
		 int ychat;
		 int ychat1;
		 int zchat;
		 
         int n = ychat = this.mc.ingameGUI.getChatGUI().getChatOpen() ? 22 : 8;
         
         int n1 = ychat1 = this.mc.ingameGUI.getChatGUI().getChatOpen() ? 12 : 8;
         
         int n3 = zchat = this.mc.ingameGUI.getChatGUI().getChatOpen() ? 120 : 4;
         SimpleDateFormat startY = new SimpleDateFormat("yyyy/MM/dd");
         Date startModsX = Calendar.getInstance().getTime();
         String startModsY = startY.format(startModsX);
         SimpleDateFormat m = new SimpleDateFormat("HH:mm:ss");
         Date time = Calendar.getInstance().getTime();
         String rendertime = m.format(time);
         String xd = "\2477" +Client.CLIENT_name+" build" + " - \247f"+Client.CLIENT_Bulid + " \2477- "+"\247a"+Client.UserName;
         String d = (Object)((Object)EnumChatFormatting.WHITE) + "" +rendertime+" "+startModsY;
         
				
         font.drawStringWithShadow(text, (float) 4.0, new ScaledResolution(this.mc).getScaledHeight() - ychat, new Color(11, 12, 17).getRGB());
         font.drawStringWithShadow(FPS, (float) zchat, new ScaledResolution(this.mc).getScaledHeight() - ychat1-10, new Color(11, 12, 17).getRGB());
         font.drawStringWithShadow(d, (float) 779.0, new ScaledResolution(this.mc).getScaledHeight() - 4.0-15, new Color(11, 12, 17).getRGB());
			
         mc.fontRendererObj.drawStringWithShadow(xd, (float)sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(xd) - 2.0F, (float)(sr.getScaledHeight() - (11)), Colors.getColor(255, 220));
        
			}

			GlStateManager.color(1, 1, 1);
			//Coords Render END
			

		}	
		

	
	
	public void renderLogo() {
		if(this.logo_mode.isCurrentMode("Font")) {
			YPort = 15.0F;
			DateFormat dft = new SimpleDateFormat("HH:mm:ss");
            Date time = Calendar.getInstance().getTime();
            String rendertime = dft.format(time);
          
            font1.drawCenteredString(Client.CLIENT_name, 35.0f, 4.0f, -1);
		}
		if(this.logo_mode.isCurrentMode("Flowly")) {
			//GuiIngame.drawRect1(55, 1, 3, 3, RenderUtil.rainbow(1));
			//GuiIngame.drawRect1(1.0, 1.0, font.getStringWidth("Flowly") + 30.0f, 16.0, new Color(255, 255, 255, 90).getRGB());
			YPort = 21.0F;
			DateFormat dft = new SimpleDateFormat("HH:mm:ss");
            Date time = Calendar.getInstance().getTime();
            String rendertime = dft.format(time);        
            Client.fontManager.BLC55.drawCenteredString("Flowly", 35.0f, -6.0f, -1);
		}
		if(this.logo_mode.isCurrentMode("Sigma")) {
			YPort = 35.0F;
			DateFormat dft = new SimpleDateFormat("yyyy/MM/dd");
            Date time = Calendar.getInstance().getTime();
            String rendertime = dft.format(time);
			YPort = 35.0F;
         //   GuiIngame.drawRect1(1.0, 1.0, font.getStringWidth("Csgo") + 28.0f, 13.0, new Color(10, 10, 10, 220).getRGB());
            Client.fontManager.tahoma40.drawCenteredString("Sigma", 30.0f, 1.0f, -1);
            Client.fontManager.tahoma25.drawCenteredString("Jello", 15.0f, 22.0f, -1);
          //  Client.fontManager.Sigma7.drawCenteredString("5.0", 65.0f, 10.0f, -1);
           // GuiIngame.drawRect1(1.3, 1.3, 3.0, 12.3, new Color(255, 255, 255).getRGB());
		}
	if(this.logo_mode.isCurrentMode("Csgo")) {
		//	DateFormat dy = new SimpleDateFormat("hh:mm a");
			DateFormat dft = new SimpleDateFormat("yyyy/MM/dd");
            Date time = Calendar.getInstance().getTime();
            String rendertime = dft.format(time);
			YPort = 35.0F;
            GuiIngame.drawRect1(3.0, 10.0, font.getStringWidth("Csgo") + 110.0f, 32.0, new Color(10, 10, 10, 100).getRGB());
            Client.fontManager.consolasbold20.drawCenteredString(Client.CLIENT_name+"|"+Client.UserName+"|"+"v"+Client.CLIENT_VER, 65.0f, 17.0f, -1);
            GuiIngame.drawRect1(125, 16, 7.0, 16.3, RenderUtil.rainbow(1));
		}	
	if(this.logo_mode.isCurrentMode("Voice")) {
			DateFormat dft = new SimpleDateFormat("hh:mm a");
		//	DateFormat dft = new SimpleDateFormat("yyyy/MM/dd");
            Date time = Calendar.getInstance().getTime();
            String rendertime = dft.format(time);
			YPort = 16.0F;
         //   GuiIngame.drawRect1(1.0, 1.0, font.getStringWidth("Csgo") + 28.0f, 13.0, new Color(10, 10, 10, 220).getRGB());
            Client.fontManager.consolasbold18.drawCenteredString("Voice"+"["+dft.format(time)+"]", 40.0f, 1.0f, -1);
            Client.fontManager.Sigma7.drawCenteredString("v1.5.3", 14.0f, 10.0f, -1);
           // GuiIngame.drawRect1(1.3, 1.3, 3.0, 12.3, new Color(255, 255, 255).getRGB());
		}
	
	if(this.logo_mode.isCurrentMode("Judgment")) {
		YPort = 35.0F;
		DateFormat dft = new SimpleDateFormat("hh:mm a");
        Date time = Calendar.getInstance().getTime();
        String rendertime = dft.format(time);
        GuiIngame.drawRect1(1.0, 1.0, font.getStringWidth("Judgment") + 25.0f, 11.0, new Color(255, 255, 255, 90).getRGB());
        Client.fontManager.consolasbold18.drawCenteredString(Client.CLIENT_name+"v"+Client.CLIENT_VER, 30.0f, 1.0f, -1);
       GuiIngame.drawRect1(65, 11, 1.0, 10, RenderUtil.rainbow(1));
	}
	}
	private void renderToggled(ScaledResolution sr) {

		int rainbow = this.rainbow.getValueState().intValue();
		int bg = this.bg.getValueState().intValue();

		if(this.array.getValueState().booleanValue()) {
			ArrayList<Mod> mods = new ArrayList(Client.instance.modMgr.getToggled());
			int counter[] = {0};
		
			//mods.sort((m1, m2) ->this.mc.fontRendererObj.getStringWidth(String.valueOf(m2.getName()) + m2.getDisplayName()) - this.mc.fontRendererObj.getStringWidth(String.valueOf(m1.getName()) + m1.getDisplayName()));
			if(this.Array_mode.isCurrentMode("BLC")) {
				mods.sort((m1, m2) ->font.getStringWidth(String.valueOf(m2.getName()) + m2.getDisplayName()) - font.getStringWidth(String.valueOf(m1.getName()) + m1.getDisplayName()));
			}
			if(this.Array_mode.isCurrentMode("Backs")) {
				mods.sort((m1, m2) ->BLC.getStringWidth(String.valueOf(m2.getName()) + m2.getDisplayName()) - BLC.getStringWidth(String.valueOf(m1.getName()) + m1.getDisplayName()));
			}
			if(this.Array_mode.isCurrentMode("SIGMA")) {
				mods.sort((m1, m2) ->Sigma.getStringWidth(String.valueOf(m2.getName()) + m2.getDisplayName()) - Sigma.getStringWidth(String.valueOf(m1.getName()) + m1.getDisplayName()));
			}
			if(this.Array_mode.isCurrentMode("Judgment")) {
				mods.sort((m1, m2) ->Sigma.getStringWidth(String.valueOf(m2.getName()) + m2.getDisplayName()) - Judgment.getStringWidth(String.valueOf(m1.getName()) + m1.getDisplayName()));
			}
			//Judgment
			//mods.sort((o1, o2) -> font.getStringWidth(o2.getDisplayName().isEmpty() ? o2.getName() : String.format("%s %s", o2.getName(), o2.getDisplayName())) - font.getStringWidth(o1.getDisplayName().isEmpty() ? o1.getName() : String.format("%s %s", o1.getName(), o1.getDisplayName())));
			//mods.sort((o1, o2) -> this.mc.fontRendererObj.getStringWidth(o2.getDisplayName().isEmpty() ? o2.getName() : String.format("%s %s", o2.getName(), o2.getDisplayName())) - this.mc.fontRendererObj.getStringWidth(o1.getDisplayName().isEmpty() ? o1.getName() : String.format("%s %s", o1.getName(), o1.getDisplayName())));
			int yStart = 0;
			int right = sr.getScaledWidth();
			
			for(Mod module : mods) {
			String name = module.getDisplayName().isEmpty() ? module.getName() : String.format("%s %s", module.getName(),module.getDisplayName());

			if(this.Array_mode.isCurrentMode("BLC")) {
			
				Gui.drawRect(sr.getScaledWidth() - 1, yStart - 2, sr.getScaledWidth(), yStart + 9,  RenderUtil.rainbow(counter[0] * rainbow));
	          
				if(module.getDisplayName() != "") {
					Gui.drawRect(sr.getScaledWidth() - font.getStringWidth( module.getDisplayName() + module.getName()) - 3, yStart, sr.getScaledWidth() - 1, yStart + 9, new Color(0, 0, 0,bg).getRGB());
				} else {
					Gui.drawRect(sr.getScaledWidth() - font.getStringWidth(module.getName()) - 3, yStart, sr.getScaledWidth() - 1, yStart + 9, new Color(0, 0, 0,bg).getRGB());

				}
				font.drawStringWithShadow(name, right - font.getStringWidth(module.getName() + module.getDisplayName() )- 2, (float)((double)yStart + 2.5),  RenderUtil.rainbow(counter[0] * rainbow));
			}
			

			if(this.Array_mode.isCurrentMode("SIGMA")) {	
				
				 RenderUtils.drawBorderRect2(sr.getScaledWidth() - Sigma.getStringWidth(String.valueOf(module.getName()) + module.getDisplayName()) - 4, yStart, sr.getScaledWidth(), yStart + 10, 1, new Color(40, 40, 40).getRGB(), RenderUtil.rainbow(counter[0] * rainbow));
				
				 Gui.drawRect(sr.getScaledWidth() - Sigma.getStringWidth(String.valueOf(module.getName()) + module.getDisplayName()) - 3, yStart-1, sr.getScaledWidth() - 1, yStart + 1, new Color(41, 40, 40).getRGB());
				 
				 Sigma.drawStringWithShadow(name, right - Sigma.getStringWidth(module.getName() + module.getDisplayName() )- 2, (float)((double)yStart + 2.0),  RenderUtil.rainbow(counter[0] * rainbow));
			}
			
			
			if(this.Array_mode.isCurrentMode("Backs")) {
				
				Gui.drawRect(sr.getScaledWidth() - BLC.getStringWidth( module.getDisplayName() + module.getName()) - 4, yStart - 0, sr.getScaledWidth()- BLC.getStringWidth( module.getDisplayName() + module.getName()) - 3, yStart + 9,  RenderUtil.rainbow(counter[0] * rainbow));
				
				if(module.getDisplayName() != "") {
					Gui.drawRect(sr.getScaledWidth() - BLC.getStringWidth( module.getDisplayName() + module.getName()) - 3, yStart, sr.getScaledWidth() + 2, yStart + 9, new Color(0, 0, 0,bg).getRGB());
				} else {
					Gui.drawRect(sr.getScaledWidth() - BLC.getStringWidth(module.getName()) - 3, yStart, sr.getScaledWidth() + 2, yStart + 9, new Color(0, 0, 0,bg).getRGB());

				}
				BLC.drawStringWithShadow( name, right - BLC.getStringWidth(module.getName() + module.getDisplayName() )-1.0, (float)((double)yStart + 1.0),  RenderUtil.rainbow(counter[0] * rainbow));

			}
			
			
				
					//  fr.drawStringWithShadow(String.valueOf(module.getName()) + "§7 " + module.getDisplayName(), sr.getScaledWidth() - this.mc.fontRendererObj.getStringWidth(String.valueOf(module.getName()) + module.getDisplayName()) - 2, y + 2, RenderUtil.rainbow(counter[0] * 200));
				//}
			
				
				yStart += 9;
				counter[0]++;
			}
			}
		}
	
	
	
public  void renderTabgui(){
		
        int[] var12 = new int[1];
        byte var14;
        var14 = 0;
        int var15 = logo_mode.isCurrentMode("Judgment") ? 8 : 2;
        int var16 = (int)YPort + 2;
        RenderUtils.drawBorderedRect((float)var15, (float)var16, (float)(var15 + this.getWidestCategory() + 3), (float)(var16 + this.categoryValues.size() * 11), 0.1F, Integer.MIN_VALUE, (new Color(0, 0, 0, 255)).getRGB());
        for(Iterator var19 = this.categoryValues.iterator(); var19.hasNext(); ++var12[0]) {
       	 Category var17 = (Category)var19.next();
       	 int var13;
            var13 = -1;

       	 if(this.getCurrentCategorry().equals(var17)) {
       		 RenderUtils.drawGradientSideways((float)var15+0.3, (float)var16+0.3, (float)(var15 + this.getWidestCategory() + 3)-0.3, (float)(var16 + 9 + 2)-0.3,new Color(30, 29, 30  ,200).getRGB(), var13);
       	 }

       	 String var21 = var17.name();
       	 Client.instance.fontMgrs.kiona16.drawStringWithShadow1(var21.substring(0, 1).toUpperCase() + var21.substring(1, var21.length()).toLowerCase(), (float)(var15 + 2), (float)var16 + (float)var14 * 1.5F+3, var13);
            var16 += 11;
        }
        
        if(this.screen == 1 || this.screen == 2) {
            int var18 = var15 + this.getWidestCategory() + 6 ;
            int var20 = 21 + this.currentCategoryIndex * 11;
            
            RenderUtils.drawRect((float)var18, (float)var20, (float)(var18 + this.getWidestMod() + 14), (float)(var20 + this.getModsForCurrentCategory().size() * 11), Integer.MIN_VALUE);

            for(Iterator var23 = this.getModsForCurrentCategory().iterator(); var23.hasNext(); var20 += 11) {
           	 Mod var22 = (Mod)var23.next();
           	 if(this.getCurrentModule().equals(var22)) {
           		 RenderUtils.drawGradientSideways((float)var18+0.3, (float)var20+0.3, (float)(var18 + this.getWidestMod() + 14)-0.3, (float)(var20 + 9 + 2)-0.3,new Color(30, 29, 30  ,200).getRGB(), var14);
           		
           	 }
           	 Client.instance.fontMgrs.kiona16.drawStringWithShadow1(var22.getName(), (float)(var18 + 1), (float)var20 + (float)var14 * 1.5F+3, var22.isEnabled()?-1:11184810);//Color.GRAY.getRGB());
            }

        }

	}
	@EventTarget
    public void onKey(EventKeyboard e) {
        switch (e.getKey()) {
            case Keyboard.KEY_UP:
                this.up();
                break;
            case Keyboard.KEY_DOWN:
                this.down();
                break;
            case Keyboard.KEY_RIGHT:
                this.right(Keyboard.KEY_RIGHT);
                break;
            case Keyboard.KEY_LEFT:
                this.left();
                break;
            case Keyboard.KEY_RETURN:
                this.ok(Keyboard.KEY_RETURN);
                break;
        }
    }
	
    
	public void up() {
	      if(this.currentCategoryIndex > 0 && this.screen == 0) {
	         --this.currentCategoryIndex;
	      } else if(this.currentCategoryIndex == 0 && this.screen == 0) {
	         this.currentCategoryIndex = this.categoryValues.size() - 1;
	      } else if(this.currentModIndex > 0 && this.screen == 1) {
	         --this.currentModIndex;
	      } else if(this.currentModIndex == 0 && this.screen == 1) {
	         this.currentModIndex = this.getModsForCurrentCategory().size() - 1;
	      } else if(this.currentSettingIndex > 0 && this.screen == 2) {
	         --this.currentSettingIndex;
	      }

	   }

	   public void down() {
	      if(this.currentCategoryIndex < this.categoryValues.size() - 1 && this.screen == 0) {
	         ++this.currentCategoryIndex;
	      } else if(this.currentCategoryIndex == this.categoryValues.size() - 1 && this.screen == 0) {
	         this.currentCategoryIndex = 0;
	      } else if(this.currentModIndex < this.getModsForCurrentCategory().size() - 1 && this.screen == 1) {
	         ++this.currentModIndex;
	      } else if(this.currentModIndex == this.getModsForCurrentCategory().size() - 1 && this.screen == 1) {
	         this.currentModIndex = 0;
	      }

	   }

	   public void right(int var1) {
	      if(this.screen == 0) {
	         this.screen = 1;
	      } 
	   }

	   public void ok(int var1) {
		   if(this.screen == 1 && this.getCurrentModule() != null) {
		         this.getCurrentModule().toggle();
		      }

		   }
	   
	   public void left() {
	      if(this.screen == 1) {
	         this.screen = 0;
	         this.currentModIndex = 0;
	      } else if(this.screen == 2) {
	         this.screen = 1;
	         this.currentSettingIndex = 0;
	      }

	   }

	   
	   public Category getCurrentCategorry() {
		   return (Category)this.categoryValues.get(this.currentCategoryIndex);
	   }
	   
	   public Mod getCurrentModule() {
		   return (Mod)this.getModsForCurrentCategory().get(this.currentModIndex);
	   }
	   
	   public ArrayList getModsForCurrentCategory() {
		      ArrayList var1 = new ArrayList();
		      Category  var2 = this.getCurrentCategorry();
		      Iterator var4 = ModManager.getModList().iterator();
		      while(var4.hasNext()) {
		    	  Mod var3 = (Mod)var4.next();
		    	  if(var3.getCategory().equals(var2)) {
		              var1.add(var3);
		           }
		      }
		      return var1;
	   }
	   
	   public int getWidestCategory() {
		      int var1 = 0;
		      Iterator var3 = this.categoryValues.iterator();

		      while(var3.hasNext()) {
		         Category var2 = (Category)var3.next();
		         String var4 = var2.name();
		         String p = ">";
	             if(this.screen == 1 || this.screen == 2) {
	            	 p = "<";
	             } else {
	            	 p = ">";
	             }
		         int var5 = Client.instance.fontMgrs.kiona16.getStringWidth(var4.substring(0, 1).toUpperCase() + var4.substring(1, var4.length()).toLowerCase()) + 3;
		         if(var5 > var1) {
		            var1 = var5;
		         }
		      }

		      return var1 + 2;
		   }



	   public int getWidestMod() {
		   int var1 = 0;
		   Iterator var3 = ModManager.getModList().iterator();
		   
		   while(var3.hasNext()) {
			   Mod var2 = (Mod)var3.next();
			   int var4 = Client.instance.fontMgrs.Backs16.getStringWidth(var2.getName());
		         if(var4 > var1) {
		            var1 = var4;
		         }
		   }
		   
		   return var1;
	   }
			
//药水显示

	 @EventTarget
	    public void o(EventRender2D e) {

		 
		 
		 if(this.ArmorHUD.getValueState().booleanValue()) {
				ScaledResolution sr = new ScaledResolution(mc);
				renderStuffStatus3(sr);
				
			}
		
	            ScaledResolution sr = new ScaledResolution(mc);
	            renderPotionStatus(sr);
	            renderPotionStatus1(sr);

	            
	    }
	 
	   public void renderPotionStatus1(ScaledResolution sr2) {
		   if(!this.Effect_mode.isCurrentMode("NONE")) {
			if(this.Effect_mode.isCurrentMode("Backs")) {
	            return;
	        }
	        this.x = 0;
	        for (PotionEffect effect : Minecraft.thePlayer.getActivePotionEffects()) {
	            Potion potion = Potion.potionTypes[effect.getPotionID()];
	            String PType = I18n.format(potion.getName(), new Object[0]);
	            String d2 = "";
	            switch (effect.getAmplifier()) {
	                case 1: {
	                    PType = String.valueOf(PType) + (EnumChatFormatting.DARK_AQUA) + " II";
	                    break;
	                }
	                case 2: {
	                    PType = String.valueOf(PType) + (EnumChatFormatting.BLUE) + " III";
	                    break;
	                }
	                case 3: {
	                    PType = String.valueOf(PType) + (EnumChatFormatting.DARK_PURPLE) + " IV";
	                    break;
	                }
	            }
	            if (effect.getDuration() < 600 && effect.getDuration() > 300) {
	                d2 = (EnumChatFormatting.YELLOW) + Potion.getDurationString(effect);
	            } else if (effect.getDuration() < 300) {
	                d2 = (EnumChatFormatting.RED) + Potion.getDurationString(effect);
	            } else if (effect.getDuration() > 600) {
	                d2 = (EnumChatFormatting.WHITE) + Potion.getDurationString(effect);
	            }
	            final int ychat = -2;
	            int y2 = sr2.getScaledHeight() - this.mc.fontRendererObj.FONT_HEIGHT + this.x - 5;
	            int m2 = 30;
	            this.mc.fontRendererObj.drawStringWithShadow(PType+" : "+d2, sr2.getScaledWidth() - m2 - this.mc.fontRendererObj.getStringWidth(PType) - 1, y2 - this.mc.fontRendererObj.FONT_HEIGHT - 12 - ychat, Colors.BLUE.c);
	            
	            this.x -= 12;
	        }
		   }
	    }
	   
	 
	  
	   
	    public void renderPotionStatus(ScaledResolution sr) {

	    	 if(!this.Effect_mode.isCurrentMode("NONE")) {
	    	if(this.Effect_mode.isCurrentMode("BLC")) {	
				return;
			}
	        int var1 = 1;
	        int var2 = 1;
	        Collection c = this.mc.thePlayer.getActivePotionEffects();
	        if (!c.isEmpty()) {
	            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	            GL11.glDisable(2896);
	            int var6 = -10;
	            int xcount = 0;
	            int ycount = 0;
	            Iterator i = this.mc.thePlayer.getActivePotionEffects().iterator();
	            while (i.hasNext()) {
	                if (xcount >= 3) {
	                    ++ycount;
	                    xcount = 0;
	                }
	                PotionEffect pe = (PotionEffect) i.next();
	                Potion var9 = Potion.potionTypes[pe.getPotionID()];
	                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
	                String thingy = "no";
	                double minutes = -1.0;
	                double seconds = -2.0;
	                try {
	                    minutes = Integer.parseInt((String)Potion.getDurationString((PotionEffect)pe).split(":")[0]);
	                    seconds = Integer.parseInt((String)Potion.getDurationString((PotionEffect)pe).split(":")[1]);
	                } catch (Exception e) {
	                    thingy = "**:**";
	                }
	                double total = minutes * 60.0 + seconds;
	                if (var9.maxtimer == 0 || total > (double)var9.maxtimer) {
	                    var9.maxtimer = (int)total;
	                }
	                double max = 0.0;
	                double percentofmax = 0.0;
	                double percentof360 = 360.0;
	                if (total >= 0.0) {
	                    max = var9.maxtimer;
	                    percentofmax = total / max * 100.0;
	                    percentof360 = 360.0;
	                    if (percentofmax < 100.0) {
	                        percentof360 = 3.6 * percentofmax;
	                    }
	                }
	                if (var9.hasStatusIcon()) {
	                    int n = var9.getStatusIconIndex();
	                }
	                String var12 = StatCollector.translateToLocal((String)var9.getName());
	                String amount = "";
	                if (pe.getAmplifier() == 1) {
	                    var12 = String.valueOf((Object)var12) + " II";
	                    amount = "II";
	                } else if (pe.getAmplifier() == 2) {
	                    var12 = String.valueOf((Object)var12) + " III";
	                    amount = "III";
	                } else if (pe.getAmplifier() == 3) {
	                    var12 = String.valueOf((Object)var12) + " IV";
	                    amount = "IV";
	                } else if(pe.getAmplifier() == 4) {
	                    var12 = String.valueOf((Object)var12) + " V";
	                    amount = "V";
	                } else if(pe.getAmplifier() == 5) {
	                    var12 = String.valueOf((Object)var12) + " VI";
	                    amount = "VI";
	                } else if(pe.getAmplifier() == 6) {
	                    var12 = String.valueOf((Object)var12) + " VII";
	                    amount = "VII";
	                } else if(pe.getAmplifier() == 7) {
	                    var12 = String.valueOf((Object)var12) + " VIII";
	                    amount = "VIII";
	                } else if(pe.getAmplifier() == 8) {
	                    var12 = String.valueOf((Object)var12) + " VIIII";
	                    amount = "VIIII";
	                } else if(pe.getAmplifier() == 9) {
	                    var12 = String.valueOf((Object)var12) + " X";
	                    amount = "X";
	                } else if(pe.getAmplifier() >= 10) {
	                    var12 = String.valueOf((Object)var12) + " X+";
	                    amount = "X+";
	                }
	                int var14 = this.x - this.mc.fontRendererObj.getStringWidth(var12) / 2 - 21;
	                String var11 = Potion.getDurationString((PotionEffect)pe);
	                String name = String.valueOf((Object)var12) + " ?(?" + var11 + "?)";
	                this.s = this.transitionTo(this.s, 11);
	                int color = new Color(0,255,255).getRGB();
	                int xpe = 41 * xcount;
	                int ype = 41 * ycount;
	                if(total <= 10) {
	                    color = new Color(255,0,0).getRGB();
	                } else if (total <= 0.0) {
	                    var9.maxtimer = 0;
	                    color = new Color(255,0,0).getRGB();
	                }
	                if (thingy == "**:**") {
	                    color = new Color(0,255,0).getRGB();
	                }
	                drawFullCircle(sr.getScaledWidth() - 20 - xpe, sr.getScaledHeight() - 45 - (this.s - 10) - ype, 17.5, Integer.MIN_VALUE);
	                drawArc(sr.getScaledWidth() - 20 - xpe, sr.getScaledHeight() - 45 - (this.s - 10) - ype, 18.0, color, 180, 180 + (int)percentof360, 3);
	                GL11.glPushMatrix();
	                GL11.glScaled((double)0.8, (double)0.8, (double)0.8);
	                mc.fontRendererObj.drawStringWithShadow(Potion.getDurationString(pe), (float)(sr.getScaledWidth() - 27 - xpe) / 0.8f, (float)(sr.getScaledHeight() - 47 - (this.s - 10) - ype + 4) / 0.8f, -1);
	                GL11.glPopMatrix();
	                GL11.glPushMatrix();
	                GL11.glScaled((double)0.6, (double)0.6, (double)0.6);
	                mc.fontRendererObj.drawStringWithShadow(amount, ((float)sr.getScaledWidth() - 22f - (float)xpe) / 0.6f, (float)(sr.getScaledHeight() - 32 - (this.s - 10) - ype - 4) / 0.6f, -1);
	                GL11.glPopMatrix();
	                if (var9.hasStatusIcon()) {
	                    int var10 = var9.getStatusIconIndex();
	                    int x1 = sr.getScaledWidth() - 20 - xpe;
	                    int y1 = sr.getScaledHeight() - 45 - (this.s - 10) - ype;
	                    ResourceLocation effect = new ResourceLocation("textures/gui/container/inventory.png");
	                    mc.getTextureManager().bindTexture(effect);
	                    mc.ingameGUI.drawTexturedModalRect(x1 - 9, y1 - 16, 0 + var10 % 8 * 18, 198 + var10 / 8 * 18, 18, 18);
	                }
	                ++xcount;
	                var2 += var6;
	            }
	        }
	        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
	    }
			
	   }
	    //盔甲显示
	    private void renderStuffStatus3(ScaledResolution scaledRes){
	   		int yOffset = mc.thePlayer.isInsideOfMaterial(Material.water) ? 0 : 0;
	   		for (int slot = 3, xOffset = 0; slot >= 0; slot--) {
	   			ItemStack stack = mc.thePlayer.inventory.armorItemInSlot(slot);
	   			
	   			GuiIngame gi = new GuiIngame(mc);
	   			if (stack != null) {
	   				mc.getRenderItem().renderItemIntoGUI(stack, scaledRes.getScaledWidth() / 2 + 10 - xOffset, scaledRes.getScaledHeight() - 70 - (yOffset / 2) + 15);
	   				GL11.glDisable(GL11.GL_DEPTH_TEST);
	   				GL11.glScalef(0.5F, 0.5F, 0.5F);
	   				mc.fontRendererObj.drawStringWithShadow(stack.getMaxDamage() - stack.getItemDamage() + "", scaledRes.getScaledWidth() + 29 - xOffset * 2 + (stack.getMaxDamage() - stack.getItemDamage() >= 100 ? 4 : (stack.getMaxDamage() - stack.getItemDamage() <= 100 && stack.getMaxDamage() - stack.getItemDamage() >= 10  ? 7 : 11)), scaledRes.getScaledHeight() * 2 - 112 - yOffset + 28, 0xFFFFFF);
	   				GL11.glScalef(2F, 2F, 2F);
	   				GL11.glEnable(GL11.GL_DEPTH_TEST);
	   				xOffset -= 18;
	   			}
	   			

	   			
	   		}
	   		/*ItemStack item = mc.thePlayer.getHeldItem();
	   		int xOffset = 0;
	   		if (item != null) {
   				Object renderStack;
				xOffset -= 8;
				renderStack = item.copy();
				if ((((ItemStack) renderStack).hasEffect())
						&& (((((ItemStack) renderStack).getItem() instanceof ItemTool))
								|| ((((ItemStack) renderStack).getItem() instanceof ItemArmor))))
					((ItemStack) renderStack).stackSize = 1;
				mc.getRenderItem().renderItemIntoGUI((ItemStack) renderStack,scaledRes.getScaledWidth() / 2 - 15 - xOffset, scaledRes.getScaledHeight() - 60 - (yOffset / 2) + 15);
				
				xOffset += 20;
			}*/

	   	}

//分割线
	    public void drawFullCircle(int cx, int cy, double r, int c) {
	    	
	        r *= 2.0;
	        cx *= 2;
	        cy *= 2;
	        float f = (float)(c >> 24 & 255) / 255.0f;
	        float f1 = (float)(c >> 16 & 255) / 255.0f;
	        float f2 = (float)(c >> 8 & 255) / 255.0f;
	        float f3 = (float)(c & 255) / 255.0f;
	        RenderUtils.R2DUtils.enableGL2D();
	        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
	        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
	        GL11.glBegin((int)6);
	        int i = 0;
	        while (i <= 360) {
	            double x = Math.sin((double)((double)i * 3.141592653589793 / 180.0)) * r;
	            double y = Math.cos((double)((double)i * 3.141592653589793 / 180.0)) * r;
	            GL11.glVertex2d((double)((double)cx + x), (double)((double)cy + y));
	            ++i;
	        }
	        GL11.glEnd();
	        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
	        RenderUtils.R2DUtils.disableGL2D();
	    }

	    public void drawArc(float cx, float cy, double r, int c, int startpoint, double arc, int linewidth) {
	    
	        r *= 2.0;
	        cx *= 2.0f;
	        cy *= 2.0f;
	        float f = (float)(c >> 24 & 255) / 255.0f;
	        float f1 = (float)(c >> 16 & 255) / 255.0f;
	        float f2 = (float)(c >> 8 & 255) / 255.0f;
	        float f3 = (float)(c & 255) / 255.0f;
	        RenderUtils.R2DUtils.enableGL2D();
	        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
	        GL11.glLineWidth((float)linewidth);
	        GL11.glEnable((int)2848);
	        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
	        GL11.glBegin((int)3);
	        int i = startpoint;
	        while ((double)i <= arc) {
	            double x = Math.sin((double)((double)i * 3.141592653589793 / 180.0)) * r;
	            double y = Math.cos((double)((double)i * 3.141592653589793 / 180.0)) * r;
	            GL11.glVertex2d((double)((double)cx + x), (double)((double)cy + y));
	            ++i;
	        }
	        GL11.glEnd();
	        GL11.glDisable((int)2848);
	        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
	        RenderUtils.R2DUtils.disableGL2D();
	    }

	    public int transitionTo(int from, int to) {
	        int i;
	        if (from < to && Minecraft.getDebugFPS() >= 60) {
	            i = 0;
	            while (i < 3) {
	                ++from;
	                ++i;
	            }
	        }
	        if (from > to && Minecraft.getDebugFPS() >= 60) {
	            i = 0;
	            while (i < 3) {
	                --from;
	                ++i;
	            }
	        }
	        if (from < to && Minecraft.getDebugFPS() >= 40 && Minecraft.getDebugFPS() <= 59) {
	            i = 0;
	            while (i < 4) {
	                ++from;
	                ++i;
	            }
	        }
	        if (from > to && Minecraft.getDebugFPS() >= 40 && Minecraft.getDebugFPS() <= 59) {
	            i = 0;
	            while (i < 4) {
	                --from;
	                ++i;
	            }
	        }
	        if (from < to && Minecraft.getDebugFPS() >= 0 && Minecraft.getDebugFPS() <= 39) {
	            i = 0;
	            while (i < 6) {
	                ++from;
	                ++i;
	            }
	        }
	        if (from > to && Minecraft.getDebugFPS() >= 0 && Minecraft.getDebugFPS() <= 39) {
	            i = 0;
	            while (i < 6) {
	                --from;
	                ++i;
	            }
	        }
	        return from;
	    }
	
	
	public enum Direction {
        S("S", 0), 
        SW("SW", 1), 
        W("W", 2), 
        NW("NW", 3), 
        N("N", 4), 
        NE("NE", 5), 
        E("E", 6), 
        SE("SE", 7);
        private Direction(final String s, final int n) {}
    }
    
    private int getRainbow(int speed, int offset) {
        float hue = (System.currentTimeMillis() + offset) % speed;
        hue /= speed;
        return Color.getHSBColor(hue, 1f, 1f).getRGB();
        
    }

    
    
  
    
    
    
    
    
}
