package cn.Judgment.mod.mods.RENDER;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventRender;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.mod.ModManager;
import cn.Judgment.mod.mods.COMBAT.AntiBot;
import cn.Judgment.mod.mods.COMBAT.Killaura;
import cn.Judgment.mod.mods.WORLD.Teams;
import cn.Judgment.ui.ClientNotification;
import cn.Judgment.util.ClientUtil;
import cn.Judgment.util.Colors;
import cn.Judgment.util.FlatColors;
import cn.Judgment.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class ESP extends Mod {
	
	public static Value<String> mode = new Value("ESP", "Mode", 0);
	public Value<Boolean> player = new Value("ESP_player", true);
	public Value<Boolean> mobs = new Value("ESP_mobs", false);
	public Value<Boolean> invis = new Value("ESP_Invisibles", true);
	

	public ESP() {
		super("ESP", Category.RENDER);
		ArrayList<String> settings = new ArrayList();
		this.mode.mode.add("2D");
		this.mode.mode.add("Box");
		this.mode.mode.add("Candy");
		this.mode.mode.add("Other2D");
		this.mode.mode.add("Outline");
		this.mode.mode.add("Twinkle");
	}
	
	public static boolean isOutline = false;
	
	@Override
	public void onDisable() {
		isOutline = false;
		super.onDisable();
	}
	
	@EventTarget
	public void onRender(EventRender event) {
		if(this.mode.isCurrentMode("2D")) {
			this.setDisplayName("2D");
			  this.doCornerESP();
		}
		if(this.mode.isCurrentMode("Box")) {
			this.setDisplayName("Box");
			this.doBoxESP(event);
		}
		if(this.mode.isCurrentMode("Candy")) {
			this.setDisplayName("Candy");
			  this.doCandyESP();
		}
		if(this.mode.isCurrentMode("Other2D")) {
			this.setDisplayName("Other2D");
			this.doOther2DESP();
		}
		if(this.mode.isCurrentMode("Outline")) {
			this.setDisplayName("Outline");
			isOutline = true;
		} else {
			isOutline = false;
		}
		if(this.mode.isCurrentMode("Twinkle") ) {
			this.setDisplayName("Twinkle");
			if( this.mc.gameSettings.ofFastRender) {
			 this.set(false);
			 ClientUtil.sendClientMessage("Options->Video Settings->Performance->Fast Render->Off", ClientNotification.Type.ERROR);
			 return;
		}
	}
	}
	
	private void doBoxESP(EventRender event) {
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        for (Object o : this.mc.theWorld.loadedEntityList) {
            if(o instanceof EntityPlayer && o != mc.thePlayer) {
            	EntityPlayer ent = (EntityPlayer)o;
            	 if (((Entity) o).isInvisible() && !this.invis.getValueState().booleanValue()) {
	                    continue;
	                }
            	if(Teams.isOnSameTeam(ent)) {
            		RenderUtil.entityESPBox(ent, new Color(0,255,0), event);
            	} else if(ent.hurtTime > 0) {
            		RenderUtil.entityESPBox(ent, new Color(255,0,0), event);
            	} else if(ent.isInvisible()) {
            		RenderUtil.entityESPBox(ent, new Color(255,255,0), event);
            	} else {
            		RenderUtil.entityESPBox(ent, new Color(255,255,255), event);
            	}
            }
            
            
        }
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
    }
	
	 
	   
	   private Color getBoxColor(EntityLivingBase entity) {
		      if (entity.hurtTime > 0) {
		         return new Color(192, 57, 43);
		      } else {
		         return entity != Killaura.target  ? new Color(80, 255, 150) : new Color(255, 255, 0);
		      }
		   }
	   
	private boolean isValid(EntityLivingBase entity) {
		if(entity instanceof EntityPlayer && entity.getHealth() >= 0.0f && entity != mc.thePlayer) {
			return true;
		}
		return false;
	}
	
	
	private void doCornerESP() {
	      Iterator var2 = this.mc.theWorld.playerEntities.iterator();

	      while(true) {
	         EntityPlayer entity;
	         do {
	            if (!var2.hasNext()) {
	               return;
	            }
	            entity = (EntityPlayer)var2.next();
	         } while(entity == this.mc.thePlayer);
	         if ((entity).isInvisible() && !this.invis.getValueState().booleanValue()) {
                 continue;
             }

	         if (!this.isValid(entity)) {
	            return;
	         }

	         GL11.glPushMatrix();
	         GL11.glEnable(3042);
	         GL11.glDisable(2929);
	         GL11.glNormal3f(0.0F, 1.0F, 0.0F);
	         GlStateManager.enableBlend();
	         GL11.glBlendFunc(770, 771);
	         GL11.glDisable(3553);
	         float partialTicks = this.mc.timer.renderPartialTicks;
	         this.mc.getRenderManager();
	         double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - RenderManager.renderPosX;
	         this.mc.getRenderManager();
	         double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - RenderManager.renderPosY;
	         this.mc.getRenderManager();
	         double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - RenderManager.renderPosZ;
	         float DISTANCE = this.mc.thePlayer.getDistanceToEntity(entity);
	         float DISTANCE_SCALE = Math.min(DISTANCE * 0.15F, 2.5F);
	         float SCALE = 0.035F;
	         SCALE /= 2.0F;
	         GlStateManager.translate((float)x, (float)y + entity.height + 0.5F - (entity.isChild() ? entity.height / 2.0F : 0.0F), (float)z);
	         GL11.glNormal3f(0.0F, 1.0F, 0.0F);
	         GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
	         GL11.glScalef(-SCALE, -SCALE, -SCALE);
	         Tessellator tesselator = Tessellator.getInstance();
	         WorldRenderer worldRenderer = tesselator.getWorldRenderer();
	         Color color = new Color(FlatColors.WHITE.c);
	         if (entity.hurtTime > 0) {
	            color = new Color(255, 0, 0);
	         } else if(Teams.isOnSameTeam(entity)) {
             	color = new Color(0, 255, 0);
	         } else if(entity.isInvisible()) {
             	color = new Color(255,255,0);
	         }else   if(AntiBot.isBot(entity)) {
	        	 color = new Color(40,40,40);
	         } else if (entity == Killaura.target && ModManager.getModByName("KillAura").isEnabled()) {
	            color = new Color(0, 0, 255);
	            
	         }

	         Color gray = new Color(0, 0, 0);
	         double thickness = (double)(8.0F + DISTANCE * 0.08F);
	         double xLeft = -40.0D;
	         double xRight = 40.0D;
	         double yUp = 15.0D;
	         double yDown = 140.0D;
	         double size = 20.0D;
	         drawVerticalLine(xLeft + size / 2.0D -4.2D, yUp -1.0D, size -9.2, thickness, gray);
	         this.drawHorizontalLine(xLeft + 1.0D - 2.0D, yUp-15.0 + size, size - 11.0D, thickness, gray);
	         
	         drawVerticalLine(xLeft + size / 3.0D, yUp, size / 2.0D, thickness, color);
	         this.drawHorizontalLine(xLeft, yUp-15 + size, size-11, thickness, color);
	         
	         drawVerticalLine(xRight - size / 2.0D + 4.2D, yUp -1.0D, size - 9.2D, thickness, gray);
	         this.drawHorizontalLine(xRight + 1.0D, yUp-15.0 + size, size - 11.0D, thickness, gray);
	        
	         drawVerticalLine(xRight - size / 3.0D, yUp, size / 2.0D, thickness, color);
	         this.drawHorizontalLine(xRight, yUp-15 + size, size-11, thickness, color);
	        
	         drawVerticalLine(xLeft + size / 2.0D + -4.2D, yDown + 1.0D, size - 9.2D, thickness, gray);
	         this.drawHorizontalLine(xLeft + 1.0D - 2.0, yDown + 15.0D - size, size-11D, thickness, gray);
	        
	         drawVerticalLine(xLeft + size / 3.0D, yDown, size / 2.0D, thickness, color);
	         this.drawHorizontalLine(xLeft, yDown+15 - size, size-11, thickness, color);
	        
	         drawVerticalLine(xRight - size / 2.0D + 4.2D, yDown + 1.0D, size - 9.2D, thickness, gray);
	         this.drawHorizontalLine(xRight + 1.0D, yDown + 15.0D - size, size-11D, thickness, gray);
	       
	         drawVerticalLine(xRight - size / 3.0D, yDown, size / 2.0D, thickness, color);
	         this.drawHorizontalLine(xRight, yDown+15 - size, size-11, thickness, color);
	         
	         GL11.glEnable(3553);
	         GL11.glEnable(2929);
	         GlStateManager.disableBlend();
	         GL11.glDisable(3042);
	         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	         GL11.glNormal3f(1.0F, 1.0F, 1.0F);
	         GL11.glPopMatrix();
	      }
	   }
	private static void drawVerticalLine(double xPos, double yPos, double xSize, double thickness, Color color) {
	      Tessellator tesselator = Tessellator.getInstance();
	      WorldRenderer worldRenderer = tesselator.getWorldRenderer();
	      worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
	      worldRenderer.pos(xPos - xSize, yPos - thickness / 2.0D, 0.0D).color((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F).endVertex();
	      worldRenderer.pos(xPos - xSize, yPos + thickness / 2.0D, 0.0D).color((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F).endVertex();
	      worldRenderer.pos(xPos + xSize, yPos + thickness / 2.0D, 0.0D).color((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F).endVertex();
	      worldRenderer.pos(xPos + xSize, yPos - thickness / 2.0D, 0.0D).color((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F).endVertex();
	      tesselator.draw();
	   }

	   private void drawHorizontalLine(double xPos, double yPos, double ySize, double thickness, Color color) {
	      Tessellator tesselator = Tessellator.getInstance();
	      WorldRenderer worldRenderer = tesselator.getWorldRenderer();
	      worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
	      worldRenderer.pos(xPos - thickness / 2.0D, yPos - ySize, 0.0D).color((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F).endVertex();
	      worldRenderer.pos(xPos - thickness / 2.0D, yPos + ySize, 0.0D).color((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F).endVertex();
	      worldRenderer.pos(xPos + thickness / 2.0D, yPos + ySize, 0.0D).color((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F).endVertex();
	      worldRenderer.pos(xPos + thickness / 2.0D, yPos - ySize, 0.0D).color((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F).endVertex();
	      tesselator.draw();
	   }
	
	   
	   private void doCandyESP() {
		      Iterator var2 = this.mc.theWorld.playerEntities.iterator();
		      while(true) {
		         EntityPlayer entity;
		         do {
		            do {
		               if (!var2.hasNext()) {
		                  return;
		               }
		               entity = (EntityPlayer)var2.next();
		            } while(entity == this.mc.thePlayer);
		            if ((entity).isInvisible() && !this.invis.getValueState().booleanValue()) {
		                 continue;
		             }
		         } while(!this.isValid(entity));
		         

		         GL11.glPushMatrix();
		         GL11.glEnable(3042);
		         GL11.glDisable(2929);
		         GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		         GlStateManager.enableBlend();
		         GL11.glBlendFunc(770, 771);
		         GL11.glDisable(3553);
		         float partialTicks = this.mc.timer.renderPartialTicks;
		         this.mc.getRenderManager();
		         double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - RenderManager.renderPosX;
		         this.mc.getRenderManager();
		         double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - RenderManager.renderPosY;
		         this.mc.getRenderManager();
		         double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - RenderManager.renderPosZ;
		         float DISTANCE = this.mc.thePlayer.getDistanceToEntity(entity);
		         float DISTANCE_SCALE = Math.min(DISTANCE * 0.15F, 0.15F);
		         float SCALE = 0.035F;
		         SCALE /= 2.0F;
		         float xMid = (float)x;
		         float yMid = (float)y + entity.height + 0.5F - (entity.isChild() ? entity.height / 2.0F : 0.0F);
		         float zMid = (float)z;
		         GlStateManager.translate((float)x, (float)y + entity.height + 0.5F - (entity.isChild() ? entity.height / 2.0F : 0.0F), (float)z);
		         GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		         GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
		         GL11.glScalef(-SCALE, -SCALE, -SCALE);
		         Tessellator tesselator = Tessellator.getInstance();
		         WorldRenderer worldRenderer = tesselator.getWorldRenderer();
		         new Color(0, 0, 0);
		         double thickness = 1.5D;
		         double xLeft = -30.0D;
		         double xRight = 30.0D;
		         double yUp = 15.0D;
		         double yDown = 140.0D;
		         double size = 10.0D;
		         Color color = new Color(255, 255, 255);
		         
		         if (entity.hurtTime > 0) {
			            color = new Color(255, 0, 0);
			         } else if(Teams.isOnSameTeam(entity)) {
		             	color = new Color(0, 255, 0);
			         } else if(entity.isInvisible()) {
		             	color = new Color(255,255,0);
			         }else   if(AntiBot.isBot(entity)) {
			        	 color = new Color(100,100,100);
			         } else if (entity == Killaura.target && ModManager.getModByName("KillAura").isEnabled()) {
			            color = new Color(0, 0, 255);
			            
			         }

		         RenderHelper.drawBorderedRect((float)xLeft, (float)yUp, (float)xRight, (float)yDown, (float)thickness + 3.0F, Colors.BLACK.c, 0);
		         RenderHelper.drawBorderedRect((float)xLeft, (float)yUp, (float)xRight, (float)yDown, (float)thickness + 1.0F, color.getRGB(), 0);
		         GL11.glEnable(3553);
		         GL11.glEnable(2929);
		         GlStateManager.disableBlend();
		         GL11.glDisable(3042);
		         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		         GL11.glNormal3f(1.0F, 1.0F, 1.0F);
		         GL11.glPopMatrix();
		      }
		   }
	   
	private void doOther2DESP() {
        for (EntityPlayer entity : this.mc.theWorld.playerEntities) {
        	   if ((entity).isInvisible() && !this.invis.getValueState().booleanValue()) {
                   continue;
               }
            if (isValid(entity)) {
                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glDisable(2929);
                GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                GlStateManager.enableBlend();
                GL11.glBlendFunc(770, 771);
                GL11.glDisable(3553);
                float partialTicks = mc.timer.renderPartialTicks;
                this.mc.getRenderManager();
                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - RenderManager.renderPosX;
                this.mc.getRenderManager();
                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - RenderManager.renderPosY;
                this.mc.getRenderManager();
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - RenderManager.renderPosZ;
                float DISTANCE = this.mc.thePlayer.getDistanceToEntity(entity);
                float DISTANCE_SCALE = Math.min(DISTANCE * 0.15f, 0.15f);
                float SCALE = 0.035f;
                SCALE /= 2.0f;
                float xMid = (float)x;
                float yMid = (float)y + entity.height + 0.5f - (entity.isChild() ? (entity.height / 2.0f) : 0.0f);
                float zMid = (float)z;
                GlStateManager.translate((float)x, (float)y + entity.height + 0.5f - (entity.isChild() ? (entity.height / 2.0f) : 0.0f), (float)z);
                GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                GL11.glScalef(-SCALE, -SCALE, -SCALE);
                Tessellator tesselator = Tessellator.getInstance();
                WorldRenderer worldRenderer = tesselator.getWorldRenderer();
                float HEALTH = entity.getHealth();
                int COLOR = -1;
                if (HEALTH > 20.0) {
                    COLOR = -65292;
                }
                else if (HEALTH >= 10.0) {
                    COLOR = -16711936;
                }
                else if (HEALTH >= 3.0) {
                    COLOR = -23296;
                }
                else {
                    COLOR = -65536;
                }
                Color gray = new Color(0, 0, 0);
                double thickness = 1.5f + DISTANCE * 0.01f;
                double xLeft = -20.0;
                double xRight = 20.0;
                double yUp = 27.0;
                double yDown = 130.0;
                double size = 10.0;
                Color color = new Color(255, 255, 255);
                if (entity.hurtTime > 0) {
                    color = new Color(255, 0, 0);
                } else if(Teams.isOnSameTeam(entity)) {
                	color = new Color(0, 255, 0);
                } else if(entity.isInvisible()) {
                	color = new Color(255,255,0);
                }
                drawBorderedRect((float)xLeft, (float)yUp, (float)xRight, (float)yDown, (float)thickness + 0.5f, Colors.BLACK.c, 0);
                drawBorderedRect((float)xLeft, (float)yUp, (float)xRight, (float)yDown, (float)thickness, color.getRGB(), 0);
                drawBorderedRect((float)xLeft - 3.0f - DISTANCE * 0.2f, (float)yDown - (float)(yDown - yUp), (float)xLeft - 2.0f, (float)yDown, 0.15f, Colors.BLACK.c, new Color(100, 100, 100).getRGB());
                drawBorderedRect((float)xLeft - 3.0f - DISTANCE * 0.2f, (float)yDown - (float)(yDown - yUp) * Math.min(1.0f, entity.getHealth() / 20.0f), (float)xLeft - 2.0f, (float)yDown, 0.15f, Colors.BLACK.c, COLOR);
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GlStateManager.disableBlend();
                GL11.glDisable(3042);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glNormal3f(1.0f, 1.0f, 1.0f);
                GL11.glPopMatrix();
            }
        }
    }
	
	public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, int col1, int col2) {
	      drawRect(x, y, x2, y2, col2);
	      float f = (float)(col1 >> 24 & 255) / 255.0F;
	      float f1 = (float)(col1 >> 16 & 255) / 255.0F;
	      float f2 = (float)(col1 >> 8 & 255) / 255.0F;
	      float f3 = (float)(col1 & 255) / 255.0F;
	      GL11.glEnable(3042);
	      GL11.glDisable(3553);
	      GL11.glBlendFunc(770, 771);
	      GL11.glEnable(2848);
	      GL11.glPushMatrix();
	      GL11.glColor4f(f1, f2, f3, f);
	      GL11.glLineWidth(l1);
	      GL11.glBegin(1);
	      GL11.glVertex2d((double)x, (double)y);
	      GL11.glVertex2d((double)x, (double)y2);
	      GL11.glVertex2d((double)x2, (double)y2);
	      GL11.glVertex2d((double)x2, (double)y);
	      GL11.glVertex2d((double)x, (double)y);
	      GL11.glVertex2d((double)x2, (double)y);
	      GL11.glVertex2d((double)x, (double)y2);
	      GL11.glVertex2d((double)x2, (double)y2);
	      GL11.glEnd();
	      GL11.glPopMatrix();
	      GL11.glEnable(3553);
	      GL11.glDisable(3042);
	      GL11.glDisable(2848);
	   }
	
	public static void drawRect(float g, float h, float i, float j, int col1) {
	      float f = (float)(col1 >> 24 & 255) / 255.0F;
	      float f1 = (float)(col1 >> 16 & 255) / 255.0F;
	      float f2 = (float)(col1 >> 8 & 255) / 255.0F;
	      float f3 = (float)(col1 & 255) / 255.0F;
	      GL11.glEnable(3042);
	      GL11.glDisable(3553);
	      GL11.glBlendFunc(770, 771);
	      GL11.glEnable(2848);
	      GL11.glPushMatrix();
	      GL11.glColor4f(f1, f2, f3, f);
	      GL11.glBegin(7);
	      GL11.glVertex2d((double)i, (double)h);
	      GL11.glVertex2d((double)g, (double)h);
	      GL11.glVertex2d((double)g, (double)j);
	      GL11.glVertex2d((double)i, (double)j);
	      GL11.glEnd();
	      GL11.glPopMatrix();
	      GL11.glEnable(3553);
	      GL11.glDisable(3042);
	      GL11.glDisable(2848);
	}
	
	public void pre() {
        GL11.glDisable((int)2929);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
    }

    public void post() {
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glColor3d((double)1.0, (double)1.0, (double)1.0);
    }
	
}