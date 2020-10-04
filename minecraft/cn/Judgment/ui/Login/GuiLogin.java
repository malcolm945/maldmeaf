package cn.Judgment.ui.Login;

import java.io.IOException;

import javax.swing.JOptionPane;

import org.lwjgl.input.Keyboard;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import cn.Judgment.Client;
import cn.Judgment.ui.AltLogin.PasswordField;

public class GuiLogin extends GuiScreen {

    public static PasswordField password;
    public static GuiTextField username;
    public GuiButton loginButton;
    private static final ResourceLocation minecraftTitleTextures = new ResourceLocation("Client/2");
    public static boolean Checked = false;
    public static boolean NMSL = false;

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1:
                try {
                    if (!this.username.getText().isEmpty() || !this.password.getText().isEmpty()) {
                        if (Verify.AddVerify(Verify.Login(this.username.getText(), this.password.getText()), Addon.QaQ())) {
                            Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
                            Client.UserName = this.username.getText();
                            Client.paiduser = true;
                            Checked = true;
                            NMSL = true;
                        }
                    }
                } catch (Throwable var4) {
                    var4.printStackTrace();
                }
                break;
            case 3:
                Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
                Client.paiduser = false;
        }
    }

    @Override
    public void drawScreen(int var1, int var2, float var3) {
        FontRenderer var4 = this.mc.fontRendererObj;
        final ScaledResolution s1 = new ScaledResolution(this.mc);
        ScaledResolution res = new ScaledResolution(this.mc);
        this.drawDefaultBackground();
        this.username.drawTextBox();
        this.password.drawTextBox();
        this.drawCenteredString(var4, Client.CLIENT_name+" Login", this.width / 2, this.height / 2 - 90, -1);
        if (this.username.getText().isEmpty()) {
            this.drawString(var4, "ÓÃ»§Ãû", this.width / 2 - 96, this.height / 2 - 44, -7829368);
        }

        if (this.password.getText().isEmpty()) {
            this.drawString(var4, "ÃÜÂë", this.width / 2 - 96, this.height / 2 - 4, -7829368);
        }

        super.drawScreen(var1, var2, var3);
    }

    @Override
    public void initGui() {
        FontRenderer var1 = this.mc.fontRendererObj;
        int var2 = this.height / 2;
        super.initGui();
        this.loginButton = new GuiButton(1, this.width / 2 - 50, var2 + 50, 100, 20, "µÇÂ¼");
        this.buttonList.add(this.loginButton);
        this.username = new GuiTextField(var2, var1, this.width / 2 - 100, this.height / 2 - 50, 200, 20);
        this.password = new PasswordField(var1, this.width / 2 - 100, this.height / 2 - 10, 200, 20);
        this.username.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    protected void keyTyped(char var1, int var2) {
        try {
            super.keyTyped(var1, var2);
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        if (var1 == 9) {
            if (!this.username.isFocused() && !this.password.isFocused()) {
                this.username.setFocused(true);
            } else {
                this.username.setFocused(this.password.isFocused());
                this.password.setFocused(!this.username.isFocused());
            }
        }

        if (var1 == 13) {
            this.actionPerformed((GuiButton) this.buttonList.get(0));
        }

        if (var1 == 27) {
            System.exit(0);
        }

        this.username.textboxKeyTyped(var1, var2);
        this.password.textboxKeyTyped(var1, var2);
    }

    @Override
    protected void mouseClicked(int var1, int var2, int var3) {
        try {
            super.mouseClicked(var1, var2, var3);
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        this.username.mouseClicked(var1, var2, var3);
        this.password.mouseClicked(var1, var2, var3);
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
