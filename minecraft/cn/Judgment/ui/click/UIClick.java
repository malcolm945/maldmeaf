package cn.Judgment.ui.click;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import cn.Judgment.mod.ModManager;
import cn.Judgment.mod.mods.ClickGui;
import cn.Judgment.mod.mods.RENDER.Animation;
import cn.Judgment.ui.Particle;
import cn.Judgment.ui.click.buttons.UIPopUPButton;
import cn.Judgment.util.handler.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class UIClick extends GuiScreen {
	private ClientEventHandler mouseClickedPopUpMenu = new ClientEventHandler();
    private UIPopUPButton uiPopUPButton;
    private ScaledResolution res;
    public boolean initialized;
    private ArrayList<Particle> particles;
    Random random = new Random();
    @Override
    public void initGui() {
    	
    	this.particles = new ArrayList();
    	ScaledResolution resolution = new ScaledResolution(this.mc);
    	int i = 0;
        while (i < 150) {
            this.particles.add(new Particle(this.random.nextInt(resolution.getScaledWidth()) + 10, this.random.nextInt(resolution.getScaledHeight())));
            ++i;
        }
    	
        this.res = new ScaledResolution(this.mc);
        this.mouseClickedPopUpMenu = new ClientEventHandler();
        if (!this.initialized) {
            this.uiPopUPButton = new UIPopUPButton(10.0f, this.res.getScaledHeight() - 10, 6.0f, 14.0f);
            this.initialized = true;
        }
    }

    public void load() {
        if (!this.initialized) {
            Runnable run = new Runnable(){

                @Override
                public void run() {
                    UIClick.access(UIClick.this, new UIPopUPButton(10.0f, Minecraft.getMinecraft().displayHeight - 10, 6.0f, 14.0f));
                    UIClick.this.initialized = true;
                }
            };
            new Thread(run).start();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.uiPopUPButton.draw(mouseX, mouseY);
        if(ClickGui.snowflake.getValueState().booleanValue()) {
        for (Particle particle : this.particles) {
            particle.drawScreen(mouseX, mouseY, res.getScaledHeight());
         }
    }
    }
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.uiPopUPButton.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        this.uiPopUPButton.mouseReleased(mouseX, mouseY);
    }

    private boolean isHovering(int mouseX, int mouseY, int x, int y, int x2, int y2) {
        if (mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2) {
            return true;
        }
        return false;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    static void access(UIClick uIClick, UIPopUPButton uIPopUPButton) {
        uIClick.uiPopUPButton = uIPopUPButton;
    }
}
