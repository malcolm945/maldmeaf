package cn.Judgment.util;

import java.awt.Color;
import java.awt.Font;
import java.io.InputStream;
import java.util.ArrayList;

import cn.Judgment.Client;
import cn.Judgment.mod.mods.ClickGui;
import cn.Judgment.ui.ClientNotification;
import cn.Judgment.ui.ClientNotification.Type;
import cn.Judgment.util.fontRenderer.UnicodeFontRenderer;
import cn.Judgment.util.fontRenderer.CFont.CFont;
import cn.Judgment.util.fontRenderer.CFont.CFontRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

public enum ClientUtil {
	
	INSTANCE;
	protected static Minecraft mc = Minecraft.getMinecraft();
	private static final UnicodeFontRenderer clientFont = Client.fontManager.tahoma18;
	private static ArrayList<ClientNotification> notifications = new ArrayList<>();
	public CFontRenderer sansation19 = new CFontRenderer(this.getSansation(19), true, true);
	public CFontRenderer tahoma19 = new CFontRenderer(this.getTahoma(19), true, true);
	
	public Font getTahoma(float size) {
		Font f;
		try {
			InputStream is = UnicodeFontRenderer.class.getResourceAsStream("fonts/tahoma.ttf");
			f = Font.createFont(0, is);
            f = f.deriveFont(0, size);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error loading font");
			f = new Font("Arial", 0, (int)size);
        }
		return f;
	}
	
	   public static UnicodeFontRenderer getClientfont() {
		      return clientFont;
		   }
	public Font getSansation(float size) {
		Font f;
		try {
			InputStream is = UnicodeFontRenderer.class.getResourceAsStream("fonts/sansation.ttf");
			f = Font.createFont(0, is);
            f = f.deriveFont(0, size);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error loading font");
			f = new Font("Arial", 0, (int)size);
        }
		return f;
	}
	
	public void drawNotifications() {
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		double startY = res.getScaledHeight() - 55;
		final double lastY = startY;
		for (int i = 0; i < notifications.size(); i++) {
			ClientNotification not = notifications.get(i);
			if (not.shouldDelete())
				notifications.remove(i);
			if(ClickGui.Open.getValueState()) {
				not.draw(startY, lastY);
			}
			startY -= not.getHeight() + 1;
		}
	}
	
	public static void sendClientMessage(String message, Type type) {
		notifications.add(new ClientNotification(message, type));
	}
	
	public static int reAlpha(int color, float alpha) {
		Color c = new Color(color);
		float r = ((float) 1 / 255) * c.getRed();
		float g = ((float) 1 / 255) * c.getGreen();
		float b = ((float) 1 / 255) * c.getBlue();
		return new Color(r, g, b, alpha).getRGB();
	}
	
	public static String removeColorCode(String text) {
	      String finalText = text;
	      if (text.contains("¡ì")) {
	         for(int i = 0; i < finalText.length(); ++i) {
	            if (Character.toString(finalText.charAt(i)).equals("¡ì")) {
	               try {
	                  String part1 = finalText.substring(0, i);
	                  String part2 = finalText.substring(Math.min(i + 2, finalText.length()), finalText.length());
	                  finalText = part1 + part2;
	               } catch (Exception var5) {
	                  ;
	               }
	            }
	         }
	      }

	      return finalText;
	   }
	
	public static void sendChatMessage(String message,ChatType type) {
    	if(type == ChatType.INFO) {
    		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\247c\247l[" + Client.CLIENT_name + "]\247r\2477\247l " + message));
    	} else if(type == ChatType.WARN) {
    		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\247c\247l[" + Client.CLIENT_name + "]\247r\247e\247l " + message));
    	} else if(type == ChatType.ERROR) {
    		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\247b\247l[" + Client.CLIENT_name + "]\247r\2474\247l " + message));
    	}
    }

	public static boolean isBlockBetween(BlockPos start, BlockPos end) {
	      int startX = start.getX();
	      int startY = start.getY();
	      int startZ = start.getZ();
	      int endX = end.getX();
	      int endY = end.getY();
	      int endZ = end.getZ();
	      double diffX = (double)(endX - startX);
	      double diffY = (double)(endY - startY);
	      double diffZ = (double)(endZ - startZ);
	      double x = (double)startX;
	      double y = (double)startY;
	      double z = (double)startZ;
	      double STEP = 0.1D;
	      int STEPS = (int)Math.max(Math.abs(diffX), Math.max(Math.abs(diffY), Math.abs(diffZ))) * 4;

	      for(int i = 0; i < STEPS - 1; ++i) {
	         x += diffX / (double)STEPS;
	         y += diffY / (double)STEPS;
	         z += diffZ / (double)STEPS;
	         if (x != (double)endX || y != (double)endY || z != (double)endZ) {
	            BlockPos pos = new BlockPos(x, y, z);
	            Block block = mc.theWorld.getBlockState(pos).getBlock();
	            if (block.getMaterial() != Material.air && block.getMaterial() != Material.water && !(block instanceof BlockVine) && !(block instanceof BlockLadder)) {
	               return true;
	            }
	         }
	      }

	      return false;
	   }
}
