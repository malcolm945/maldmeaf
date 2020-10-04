package cn.Judgment.ui.click;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cn.Judgment.Client;
import cn.Judgment.Value;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.mod.ModManager;
import cn.Judgment.ui.click.options.UIMode;
import cn.Judgment.ui.click.options.UISlider;
import cn.Judgment.ui.click.options.UIToggleButton;
import cn.Judgment.util.ClientUtil;
import cn.Judgment.util.Colors;
import cn.Judgment.util.RenderUtil;
import cn.Judgment.util.fontRenderer.UnicodeFontRenderer;
import cn.Judgment.util.handler.MouseInputHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class ClickMenuMods {
	private ArrayList<Mod> modList;
    private MouseInputHandler handler;
    public boolean open;
    public int x;
    public int y;
    public int width;
    public int tab_height;
    private Category c;
    public double yPos;
    private boolean opened;
    private boolean closed;
    private HashMap<Value, UISlider> sliderList;
    private HashMap<Value, UIMode> valueModeList;
    private HashMap<Value, UIToggleButton> toggleButtonList;
    private int valueYAdd;
    private float scrollY;
    private float scrollAmount;
    
    public ClickMenuMods(final Category c, final MouseInputHandler handler) {
        this.modList = new ArrayList<Mod>();
        this.sliderList = new HashMap<Value, UISlider>();
        this.valueModeList = new HashMap<Value, UIMode>();
        this.toggleButtonList = new HashMap<Value, UIToggleButton>();
        this.valueYAdd = 0;
        this.c = c;
        this.handler = handler;
        this.addMods();
        this.addValues();
        this.yPos = -(this.y + this.tab_height + this.modList.size() * 20 + 10);
    }
    
    public void draw(final int mouseX, int mouseY) {
        final int MAX_HEIGHT = 248;
        if (mouseY > this.y + MAX_HEIGHT) {
            mouseY = Integer.MAX_VALUE;
        }
        final UnicodeFontRenderer font = Client.instance.fontMgr.tahoma14;
        final String name = "Panel" + this.c.name().substring(0, 1) + this.c.name().toLowerCase().substring(1, this.c.name().length());
        if (this.opened) {
            this.yPos = this.y + this.tab_height - 2;
        }
        if (this.closed) {
            this.yPos = this.y - this.modList.size() * 20 - this.valueYAdd;
        }
        if (this.yPos > this.y + this.tab_height - 2) {
            this.yPos = this.y + this.tab_height - 2;
        }
        if (this.open) {
            this.yPos = RenderUtil.getAnimationState(this.yPos, (double)(this.y + this.tab_height - 2), Math.max(50.0, Math.abs(this.yPos - (this.y + this.tab_height - 2)) * 5.0));
            if (this.yPos == this.y + this.tab_height - 2) {
                this.opened = true;
            }
            this.closed = false;
        }
        else {
            this.yPos = RenderUtil.getAnimationState(this.yPos, (double)(this.y - this.modList.size() * 20 - this.valueYAdd), Math.max(1.0, Math.abs(this.yPos - (this.y - this.modList.size() * 20 - this.valueYAdd) - 2.0) * 4.0));
            this.opened = false;
            if (this.yPos == this.y - this.modList.size() * 20 - this.valueYAdd) {
                this.closed = true;
            }
        }
        int yAxis = (int)this.yPos;
        final int height = 20;
        final ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glPushMatrix();
        GL11.glEnable(3089);
        RenderUtil.doGlScissor(this.x, this.y + this.tab_height + 3, this.width, res.getScaledHeight());
        float bottomY = (float)(this.modList.size() * height + yAxis + this.valueYAdd);
        RenderUtil.doGlScissor(this.x, this.y + this.tab_height - 2, this.width, Math.min(MAX_HEIGHT - (this.tab_height -2 ), this.modList.size() * height + this.valueYAdd));
        GL11.glTranslated(0.0, (double)this.scrollY, 0.0);
        mouseY -= (int)this.scrollY;
        this.valueYAdd = 0;
        for (final Mod m : this.modList) {
        	 Gui.drawRect(this.x, yAxis, this.x + this.width, yAxis + height, new Color(30,34,33).getRGB());
   		  Gui.drawRect(this.x, yAxis, this.x + this.width, yAxis + height, new Color(30,34,33).getRGB());
   		  Gui.drawRect(this.x, yAxis, this.x+1, yAxis + height, new Color(64,141,255).getRGB());
   		  Gui.drawRect(this.x+ this.width, yAxis, this.x+ this.width-1, yAxis + height, new Color(64,141,255).getRGB());

           // Gui.drawRect(this.x, yAxis, this.x + this.width, yAxis + height,ClientUtil.reAlpha(Colors.BLUE.c, 1F));
            if (m.isEnabled()) {
            	Gui.drawRect(this.x+1.8f, yAxis+0.5F, this.x + this.width-1.8F, yAxis + height-0.5F,ClientUtil.reAlpha(Colors.WHITE.c, 0.5F));
           // Gui.drawRect(this.x, yAxis, this.x + this.width, yAxis + height,ClientUtil.reAlpha(Colors.RED.c, 0.8F));
            }
          //  net.minecraft.client.gui.Gui.drawRect(this.x, yAxis, this.x + this.width , yAxis + height, ClientUtil.reAlpha(Colors.WHITE.c, 0.5F)  -13223618);
            final boolean arrowHover = this.yPos == this.y + this.tab_height - 2 && mouseX >= this.x + this.width - 11 && mouseX <= this.x + this.width - 2 && mouseY >= yAxis && mouseY < yAxis + height && mouseY + this.scrollY >= this.y + this.tab_height;
            final boolean hover = !arrowHover && this.yPos == this.y + this.tab_height - 2 && mouseX >= this.x && mouseX <= this.x + this.width - 12 && mouseY >= yAxis && mouseY < yAxis + height && mouseY + this.scrollY >= this.y + this.tab_height;
            if (hover) {
                m.hoverOpacity = RenderUtil.getAnimationState(m.hoverOpacity, 0.25, 1.0);
            }
            else {
                m.hoverOpacity = RenderUtil.getAnimationState(m.hoverOpacity, 0.09, 1.5);
            }
            if (hover && this.handler.canExcecute()) {
                m.set(!m.isEnabled());
            }
            if (arrowHover && this.handler.canExcecute() && m.hasValues()) {
                m.openValues = !m.openValues;
            }
            if (m.hasValues()) {
                m.arrowAnlge = RenderUtil.getAnimationState(m.arrowAnlge, (double)(m.openValues ? 0 : -90), 1000.0);
                final int size = 5;
                final double xMid = this.x + this.width - 8 + 2;
                final double yMid = yAxis + (height - size) / 2 + 1 + 2;
                GL11.glPushMatrix();
                GL11.glTranslated(xMid, yMid, 0.0);
                GL11.glRotated(m.arrowAnlge, 0.0, 0.0, 1.0);
                GL11.glTranslated(-xMid, -yMid, 0.0);
                if (arrowHover) {
                    RenderUtil.drawImage(new ResourceLocation("Client/icons/arrow_down.png"), this.x + this.width - 8, yAxis + (height - size) / 2 + 1, size, size, new Color(0.7058824f, 0.7058824f, 0.7058824f));
                } else {
                    RenderUtil.drawImage(new ResourceLocation("Client/icons/arrow_down.png"), this.x + this.width - 8, yAxis + (height - size) / 2 + 1, size, size, new Color(255,255,255));
                }
                GL11.glPopMatrix();
            }
            net.minecraft.client.gui.Gui.drawRect(this.x, yAxis, this.x + this.width, yAxis + height, ClientUtil.reAlpha(Colors.BLACK.c, (float)m.hoverOpacity));
            font.drawString(m.getName(), (float)(this.x + (this.width - font.getStringWidth(name)) / 2) - 15, (float)(yAxis + (height - font.FONT_HEIGHT) / 2), m.isEnabled() ? 0 : Colors.WHITE.c);
            if (m.openValues) {
                final int oldY = yAxis;
                for (final Value value : this.sliderList.keySet()) {
                    if (value.getValueName().split("_")[0].equalsIgnoreCase(m.getName())) {
                        yAxis += height;
                        this.valueYAdd += height;
                        Gui.drawRect(this.x, yAxis, this.x + this.width, yAxis + height,new Color(30,34,33).getRGB());
                        Gui.drawRect(this.x, yAxis, this.x+1, yAxis + height, new Color(64,141,255).getRGB());
                 		  Gui.drawRect(this.x+ this.width, yAxis, this.x+ this.width-1, yAxis + height, new Color(64,141,255).getRGB());


                        final double val = (double)value.getValueState();
                        final UISlider slider = this.sliderList.get(value);
                        slider.width = this.width - 3;
                        final double newVal = slider.draw((float)val, mouseX, mouseY, this.x + 1, yAxis + height / 2 - 5);
                        value.setValueState((Object)newVal);
                    }
                }
                for (final Value value : this.valueModeList.keySet()) {
                    if (value.getValueName().split("_")[0].equalsIgnoreCase(m.getName())) {
                        yAxis += height;
                        this.valueYAdd += height;
                        Gui.drawRect(this.x, yAxis, this.x + this.width, yAxis + height,new Color(30,34,33).getRGB());
                        Gui.drawRect(this.x, yAxis, this.x+1, yAxis + height, new Color(64,141,255).getRGB());
                		  Gui.drawRect(this.x+ this.width, yAxis, this.x+ this.width-1, yAxis + height, new Color(64,141,255).getRGB());
                        final UIMode mode = this.valueModeList.get(value);
                        mode.width = this.width;
                        mode.draw(mouseX, mouseY, this.x, yAxis);
                    }
                }
                for (final Value value : this.toggleButtonList.keySet()) {
                    if (value.getValueName().split("_")[0].equalsIgnoreCase(m.getName())) {
                        yAxis += height;
                        this.valueYAdd += height;
                        Gui.drawRect(this.x, yAxis, this.x + this.width, yAxis + height, new Color(30,34,33).getRGB());
                        Gui.drawRect(this.x, yAxis, this.x+1, yAxis + height, new Color(64,141,255).getRGB());
                		  Gui.drawRect(this.x+ this.width, yAxis, this.x+ this.width-1, yAxis + height, new Color(64,141,255).getRGB());
                        final UIToggleButton button = this.toggleButtonList.get(value);
                        button.width = this.width;
                        button.draw(mouseX, mouseY, this.x, yAxis);
                    }
                }
                Gui.drawRect(this.x, oldY + height, this.x + this.width, oldY + height + 1, new Color(64,141,255).getRGB());
                Gui.drawRect(this.x, yAxis + height - 1, this.x + this.width, yAxis + height, new Color(64,141,255).getRGB());
            }
            yAxis += height;
        }
        GL11.glDisable(3089);
        GL11.glPopMatrix();
        if (mouseX >= this.x && mouseX <= this.x + this.width && mouseY + this.scrollY >= this.y && mouseY + this.scrollY <= yAxis) {
            final float scroll = (float)Mouse.getDWheel();
            this.scrollY += scroll / 10.0f;
        }
        if (yAxis - height - this.tab_height >= MAX_HEIGHT) {
            final double test = yAxis - this.y + this.scrollY;
            if (test < MAX_HEIGHT) {
                this.scrollY = MAX_HEIGHT - (float)yAxis + this.y;
            }
        }
        if (this.scrollY > 0.0f || yAxis - height - this.tab_height < MAX_HEIGHT) {
            this.scrollY = 0.0f;
        }
    }
    
    public void mouseClick(final int mouseX, int mouseY) {
        mouseY -= (int)this.scrollY;
        for (final Mod m : this.modList) {
            if (m.openValues) {
                for (final Value value : Value.list) {
                    if (value.getValueName().split("_")[0].equalsIgnoreCase(m.getName()) && value.isValueDouble) {
                        final UISlider slider = this.sliderList.get(value);
                        if (!slider.mouseClick(mouseX, mouseY)) {
                            continue;
                        }
                        this.handler.clicked = true;
                    }
                }
            }
        }
    }
    
    public void mouseRelease(final int mouseX, final int mouseY) {
        for (final Mod m : this.modList) {
            if (m.openValues) {
                for (final Value value : Value.list) {
                    if (value.getValueName().split("_")[0].equalsIgnoreCase(m.getName()) && value.isValueDouble) {
                        final UISlider slider = this.sliderList.get(value);
                        slider.mouseRelease();
                    }
                }
            }
        }
    }
    
    private void addSliders() {
        for (final Mod m : this.modList) {
            for (final Value value : Value.list) {
                if (value.getValueName().split("_")[0].equalsIgnoreCase(m.getName()) && value.isValueDouble) {
                    final UISlider slider = new UISlider(value.getValueName().split("_")[1], (double)value.getValueMin(), (double)value.getValueMax(), value.getSteps(), this.width - 3);
                    this.sliderList.put(value, slider);
                }
            }
        }
    }
    
    private void addModes() {
        final int height = 20;
        for (final Mod m : this.modList) {
            for (final Value value : Value.list) {
                if (value.getValueName().split("_")[0].equalsIgnoreCase(m.getName()) && value.isValueMode) {
                    final UIMode mode = new UIMode(value, this.handler, this.width, height);
                    this.valueModeList.put(value, mode);
                }
            }
        }
    }
    
    private void addToggleButtons() {
        final int height = 20;
        for (final Mod m : this.modList) {
            for (final Value value : Value.list) {
                if (value.getValueName().split("_")[0].equalsIgnoreCase(m.getName()) && value.isValueBoolean) {
                    final UIToggleButton button = new UIToggleButton(value, this.handler, this.width, height);
                    this.toggleButtonList.put(value, button);
                }
            }
        }
    }
    
    private void addValues() {
        this.addSliders();
        this.addModes();
        this.addToggleButtons();
    }
    
    private void addMods() {
        for (final Mod m : ModManager.getModList()) {
            if (m.getCategory() == this.c) {
                this.modList.add(m);
            }
        }
    }
}
