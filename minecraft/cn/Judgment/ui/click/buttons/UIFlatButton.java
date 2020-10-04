package cn.Judgment.ui.click.buttons;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import cn.Judgment.Client;
import cn.Judgment.util.RenderUtil;
import cn.Judgment.util.fontRenderer.UnicodeFontRenderer;
import cn.Judgment.util.timeUtils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

public class UIFlatButton extends GuiButton {
	private TimeHelper time = new TimeHelper();
    public String displayString;
    public int id;
    public boolean enabled;
    public boolean visible;
    protected boolean hovered;
    private int color;
    private float opacity;
    private UnicodeFontRenderer font;

    public UIFlatButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, int color) {
        super(buttonId, x, y, 10, 12, buttonText);
        this.width = widthIn;
        this.height = heightIn;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.displayString = buttonText;
        this.color = color;
        this.font = Client.instance.fontMgr.sansation15;
    }

    public UIFlatButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, int color, UnicodeFontRenderer font) {
        super(buttonId, x, y, 10, 12, buttonText);
        this.width = widthIn;
        this.height = heightIn;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.displayString = buttonText;
        this.color = color;
        this.font = font;
    }

    @Override
    protected int getHoverState(boolean mouseOver) {
        int i = 1;
        if (!this.enabled) {
            i = 0;
        } else if (mouseOver) {
            i = 2;
        }
        return i;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int var5 = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            if (!this.hovered) {
                this.time.reset();
                this.opacity = 0.0f;
            }
            if (this.hovered) {
                this.opacity += 0.5f;
                if (this.opacity > 1.0f) {
                    this.opacity = 1.0f;
                }
            }
            float radius = (float)this.height / 2.0f;
            RenderUtil.drawRoundedRect((float)this.xPosition - this.opacity * 0.1f, (float)this.yPosition - this.opacity, (float)(this.xPosition + this.width) + this.opacity * 0.1f, (float)this.yPosition + radius * 2.0f + this.opacity, 1.0f, this.color);
            GL11.glColor3f((float)2.55f, (float)2.55f, (float)2.55f);
            this.mouseDragged(mc, mouseX, mouseY);
            GL11.glPushMatrix();
            GL11.glPushAttrib((int)1048575);
            GL11.glScaled((double)1.0, (double)1.0, (double)1.0);
            int var6 = -1;
            float textHeight = this.font.getStringHeight(StringUtils.stripControlCodes(this.displayString));
            this.font.drawCenteredString(this.displayString, this.xPosition + this.width / 2, (float)this.yPosition + (float)(this.height - this.font.FONT_HEIGHT) / 2.0f, this.hovered ? -1 : -3487030);
            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
    }

    private Color darkerColor(Color c, int step) {
        int red = c.getRed();
        int blue = c.getBlue();
        int green = c.getGreen();
        if (red >= step) {
            red -= step;
        }
        if (blue >= step) {
            blue -= step;
        }
        if (green >= step) {
            green -= step;
        }
        return c.darker();
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isMouseOver() {
        return this.hovered;
    }

    @Override
    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0f));
    }

    @Override
    public int getButtonWidth() {
        return this.width;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }
}
