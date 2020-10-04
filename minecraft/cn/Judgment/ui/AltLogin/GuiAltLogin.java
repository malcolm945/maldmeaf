package cn.Judgment.ui.AltLogin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;

import org.lwjgl.input.Keyboard;

import cn.Judgment.mod.mods.ClickGui;
import cn.Judgment.util.FlatColors;
import cn.Judgment.util.RenderUtil;
import cn.Judgment.util.WebUtils;
import cn.Judgment.util.RenderUtil;

import javax.swing.*;


public final class GuiAltLogin
extends GuiScreen {
    private PasswordField password;
    private final GuiScreen previousScreen;
    private AltLoginThread thread;
    private GuiTextField username;
    private GuiButton loginButton;
    private GuiButton backButton;
    private GuiButton importbutton;
    private GuiButton getbutton;
    private GuiButton get10button;
    private GuiButton get20button;
    
    public GuiAltLogin(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1: {
                this.mc.displayGuiScreen(this.previousScreen);
                break;
            }
            case 2:
    			String data = null;
    			try {
    				data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
    			} catch (Exception ignored) {
    				break;
    			}
    			if (data.contains(":")) {
    				String[] credentials = data.split(":");
    				this.username.setText(credentials[0]);
    				this.password.setText(credentials[1]);
    			}
    			break;
            case 3:{
                    String GET = null;
                    try {
                        GET = WebUtils.get("http://AzlipsClient.top/NFAS/Get.php");
                        GET = GET.substring(0,GET.length()-1);
                    } catch (Exception ignored) {
                        break;
                    }
                    if (GET.contains(":")) {
                        String[] credentials = GET.split(":");
                        this.username.setText(credentials[0]);
                        this.password.setText(credentials[1]);
                    }else{
                        JOptionPane.showMessageDialog(null,"Ã»ºÅ×ÓÀ­:"+GET);
                    }
                    break;
            }
            case 0: {
                this.thread = new AltLoginThread(this.username.getText(), this.password.getText());
                this.thread.start();
            }
            }
        
    }




    @Override
    public void drawScreen(int x2, int y2, float z2) {
    	ScaledResolution s1 = new ScaledResolution(this.mc);
    	ScaledResolution res = new ScaledResolution(this.mc);
    	FontRenderer font = Minecraft.getMinecraft().fontRendererObj;

    	RenderUtil.drawRect(0,0,new ScaledResolution(mc).getScaledWidth(),new ScaledResolution(mc).getScaledHeight(),new Color(0,0,0,255).getRGB());
    	 this.username.drawTextBox();
        this.password.drawTextBox();
        
        this.drawCenteredString(font, "Alt Login", width / 2, 20, -1);
        this.drawCenteredString(font, this.thread == null ? "Idle..." : this.thread.getStatus(), width / 2, 29, -1);
        if (this.username.getText().isEmpty()) {
            this.drawString(font, "Username / E-Mail", width / 2 - 96, 66, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            this.drawString(font, "Password", width / 2 - 96, 106, -7829368);
        }
        super.drawScreen(x2, y2, z2);
    }

    @Override
    public void initGui() {
    	FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
        int var3 = height / 4 + 24;
        this.loginButton = new GuiButton(0, width / 2 - 100, var3 + 72 + 12, 65, 15, "Login");
        this.backButton = new GuiButton(1, width / 2 - 30, var3 + 72 + 12, 65, 15, "Back");
        this.importbutton = new GuiButton(2, width / 2 + 40, var3 + 72 + 12, 65, 15, "User:Pass");
        this.buttonList.add(this.loginButton);
        this.buttonList.add(this.backButton);
        this.buttonList.add(this.importbutton);
        this.username = new GuiTextField(var3, font, width / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(font, width / 2 - 100, 100, 200, 20);
        this.username.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (character == '\t') {
            if (!this.username.isFocused() && !this.password.isFocused()) {
                this.username.setFocused(true);
            } else {
                this.username.setFocused(this.password.isFocused());
                this.password.setFocused(!this.username.isFocused());
            }
        }
        if (character == '\r') {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
        this.username.textboxKeyTyped(character, key);
        this.password.textboxKeyTyped(character, key);
    }

    @Override
    protected void mouseClicked(int x2, int y2, int button) {
        try {
            super.mouseClicked(x2, y2, button);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.username.mouseClicked(x2, y2, button);
        this.password.mouseClicked(x2, y2, button);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
    }
}