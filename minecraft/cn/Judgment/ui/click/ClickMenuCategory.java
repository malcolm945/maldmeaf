package cn.Judgment.ui.click;

import java.awt.Color;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cn.Judgment.Client;
import cn.Judgment.mod.Category;
import cn.Judgment.util.ClientUtil;
import cn.Judgment.util.Colors;
import cn.Judgment.util.RenderUtil;
import cn.Judgment.util.fontRenderer.UnicodeFontRenderer;
import cn.Judgment.util.handler.MouseInputHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ClickMenuCategory {
	public Category c;
    ClickMenuMods uiMenuMods;
    private MouseInputHandler handler;
    public boolean open;
    public int x;
    public int y;
    public int width;
    public int tab_height;
    public int x2;
    public int y2;
    public boolean drag = true;
    private double arrowAngle = 0.0;

    public ClickMenuCategory(Category c, int x, int y, int width, int tab_height, MouseInputHandler handler) {
        this.c = c;
        this.x = x;
        this.y = y;
        this.width = width;
        this.tab_height = tab_height;
        this.uiMenuMods = new ClickMenuMods(c, handler);
        this.handler = handler;
    }

    public void draw(int mouseX, int mouseY) {
        boolean hoverArrow;
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        UnicodeFontRenderer font = Client.instance.fontMgr.tahoma15;
        UnicodeFontRenderer font1 = Client.instance.fontMgr.tahoma20;
        UnicodeFontRenderer font2 = Client.instance.fontMgr.tahoma25;
        Gui.drawBorderedRect(this.x-2, this.y, this.x + this.width +2, this.y + this.tab_height -2, 1,new Color(64,141,255).getRGB(), new Color(30,34,33).getRGB());
    	RenderUtil.drawRect(this.x+1, this.y+17, this.x + this.width-1 , this.y + this.tab_height -2, new Color(30,34,33).getRGB());
       // RenderUtil.drawRoundedRect(this.x, this.y, this.x + this.width +0.5f, this.y+0.5f + this.tab_height -2, 0.0f, ClientUtil.reAlpha(Colors.BLACK.c, 0.4F));
        //RenderUtil.drawRoundedRect(this.x, this.y, this.x + this.width, this.y + this.tab_height - 1, 1.5f, new Color(66,165,245).getRGB());
        String name = "";
        name = this.c.name().substring(0, 1) + this.c.name().toLowerCase().substring(1, this.c.name().length()) + "   " ;
        font.drawString(name, (float)this.x + (this.width - font.getStringWidth(name)) / 3, this.y + (this.tab_height - font.FONT_HEIGHT) / 3, ClientUtil.reAlpha(Colors.WHITE.c, 1F));
        double xMid = this.x + this.width - 6;
        double yMid = this.y + 10;
        //this.arrowAngle = RenderUtil.getAnimationState(this.arrowAngle, this.uiMenuMods.open ? -90 : -90, 1000.0);
       
        GL11.glPushMatrix();
        GL11.glTranslated((double)xMid, (double)yMid, (double)0.0);
       // GL11.glRotated((double)this.arrowAngle, (double)0.0, (double)0.0, (double)1.0);
        GL11.glTranslated((double)(-xMid), (double)(-yMid), (double)0.0);
        boolean bl = hoverArrow = mouseX >= this.x + this.width - 15 && mouseX <= this.x + this.width - 5 && mouseY >= this.y + 7 && mouseY <= this.y + 17;
        if (hoverArrow) {
        	//RenderUtil.drawImage(new ResourceLocation("Kyra/icons/arrow-down.png"), this.x + this.width - 9, this.y + 7, 6, 6, new Color(0.7058824f, 0.7058824f, 0.7058824f));
        	if(this.uiMenuMods.open) {
        	font2.drawString("-", (float)this.x + (this.width-0.5  - font.getStringHeight(name)) / 1, this.y-2 + (this.tab_height - font.FONT_HEIGHT) / 4, ClientUtil.reAlpha(Colors.GREY.c, 1F));
        	}else{
        		font1.drawString("+", (float)this.x + (this.width-2  - font.getStringHeight(name)) / 1, this.y + (this.tab_height - font.FONT_HEIGHT) / 4, ClientUtil.reAlpha(Colors.GREY.c, 1F));
        	}
        	} else {
           // RenderUtil.drawImage(new ResourceLocation("Kyra/icons/arrow-down.png"), this.x + this.width - 9, this.y + 7, 6, 6);
        		if(this.uiMenuMods.open) {
        		font2.drawString("-", (float)this.x + (this.width-0.5  - font.getStringHeight(name)) / 1, this.y-2 + (this.tab_height - font.FONT_HEIGHT) / 4, ClientUtil.reAlpha(Colors.WHITE.c, 1F));
        		}else{
        			font1.drawString("+", (float)this.x + (this.width-2  - font.getStringHeight(name)) / 1, this.y + (this.tab_height - font.FONT_HEIGHT) / 4, ClientUtil.reAlpha(Colors.WHITE.c, 1F));
        		}
        		}
        GL11.glPopMatrix();
        
        
        if(c == Category.COMBAT) {
           	RenderUtil.drawImage((ResourceLocation)new ResourceLocation("Client/ico/combat.png"), (int)(this.x - 3), (int)(this.y - 1), (int)20, (int)20);
            } else if(c == Category.MOVEMENT) {
            	RenderUtil.drawImage((ResourceLocation)new ResourceLocation("Client/ico/movement.png"), (int)(this.x - 2), (int)(this.y - 1), (int)20, (int)20);
            } else if(c == Category.RENDER) {
            	RenderUtil.drawImage((ResourceLocation)new ResourceLocation("Client/ico/render.png"), (int)(this.x - 2), (int)(this.y - 1), (int)20, (int)20);
            } else if(c == Category.PLAYER) {
            	RenderUtil.drawImage((ResourceLocation)new ResourceLocation("Client/ico/player.png"), (int)(this.x - 2), (int)(this.y - 2), (int)20, (int)20);
            } else if(c == Category.WORLD) {
            	RenderUtil.drawImage((ResourceLocation)new ResourceLocation("Client/ico/world.png"), (int)(this.x - 2), (int)(this.y - 1), (int)20, (int)20);
            }
        this.upateUIMenuMods();
        this.width = font.getStringWidth(name) + 55; 
       // this.width = /*font.getStringWidth(name)*/ + 90;
        this.uiMenuMods.draw(mouseX, mouseY);
        this.move(mouseX, mouseY);
    }

    private void move(int mouseX, int mouseY) {
        boolean hoverArrow;
        boolean bl = hoverArrow = mouseX >= this.x + this.width - 15 && mouseX <= this.x + this.width - 5 && mouseY >= this.y + 7 && mouseY <= this.y + 17;
        if (!hoverArrow && this.isHovering(mouseX, mouseY) && this.handler.canExcecute()) {
            this.drag = true;
            this.x2 = mouseX - this.x;
            this.y2 = mouseY - this.y;
        }
        if (hoverArrow && this.handler.canExcecute()) {
            boolean bl2 = this.uiMenuMods.open = !this.uiMenuMods.open;
        }
        if (!Mouse.isButtonDown((int)0)) {
            this.drag = false;
        }
        if (this.drag) {
            this.x = mouseX - this.x2;
            this.y = mouseY - this.y2;
        }
    }

    private boolean isHovering(int mouseX, int mouseY) {
        if (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.tab_height) {
            return true;
        }
        return false;
    }

    private void upateUIMenuMods() {
        this.uiMenuMods.x = this.x;
        this.uiMenuMods.y = this.y;
        this.uiMenuMods.tab_height = this.tab_height;
        this.uiMenuMods.width = this.width;
    }

    public void mouseClick(int mouseX, int mouseY) {
        this.uiMenuMods.mouseClick(mouseX, mouseY);
    }

    public void mouseRelease(int mouseX, int mouseY) {
        this.uiMenuMods.mouseRelease(mouseX, mouseY);
    }
}
