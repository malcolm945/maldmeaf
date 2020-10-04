package cn.Judgment.ui.click;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import cn.Judgment.Client;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.util.fontRenderer.UnicodeFontRenderer;
import cn.Judgment.util.handler.MouseInputHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class ClickMenu {
	private ArrayList<ClickMenuCategory> categories;
    public static int WIDTH = 100;
    public static int TAB_HEIGHT = 20;
    private MouseInputHandler handler = new MouseInputHandler(0);
    private Minecraft mc = Minecraft.getMinecraft();
    private String fileDir;

    public ClickMenu() {
        this.fileDir = String.valueOf(this.mc.mcDataDir.getAbsolutePath()) + "/" + Client.CLIENT_name;
        this.categories = new ArrayList();
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        UnicodeFontRenderer font = Client.instance.fontMgr.sansation18;
        this.addCategorys();
        try {
            this.loadClickGui();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(int mouseX, int mouseY) {
        for (ClickMenuCategory c : this.categories) {
            c.draw(mouseX, mouseY);
        }
    }

    private void addCategorys() {
        int xAxis = 10;
        Category[] arrcategory = Category.values();
        int n = arrcategory.length;
        int n2 = 0;
        while (n2 < n) {
            Category c = arrcategory[n2];
            this.categories.add(new ClickMenuCategory(c, xAxis, 90, WIDTH, TAB_HEIGHT, this.handler));
            xAxis += 115;
            ++n2;
        }
    }

    public void mouseClick(int mouseX, int mouseY) {
        for (ClickMenuCategory cat : this.categories) {
            cat.mouseClick(mouseX, mouseY);
        }
    }

    public void mouseRelease(int mouseX, int mouseY) {
        for (ClickMenuCategory cat : this.categories) {
            cat.mouseRelease(mouseX, mouseY);
        }
        Client.instance.fileMgr.saveValues();
        this.saveClickGui();
    }

    public ArrayList<ClickMenuCategory> getCategories() {
        return this.categories;
    }

    public void saveClickGui() {
        File f = new File(String.valueOf(this.fileDir) + "/gui.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            PrintWriter pw = new PrintWriter(f);
            for (ClickMenuCategory cat : this.getCategories()) {
                String name = cat.c.name();
                String x = String.valueOf(cat.x);
                String y = String.valueOf(cat.y);
                String open = String.valueOf(cat.uiMenuMods.open);
                pw.print(String.valueOf(name) + ":" + x + ":" + y + ":" + open + "\n");
            }
            pw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadClickGui() throws IOException {
        File f = new File(String.valueOf(this.fileDir) + "/gui.txt");
        if (!f.exists()) {
            f.createNewFile();
        } else {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(f));
            while ((line = br.readLine()) != null) {
                try {
                    String[] s = line.split(":");
                    if (s.length < 4) continue;
                    String name = s[0];
                    int x = Integer.valueOf(s[1]);
                    int y = Integer.valueOf(s[2]);
                    boolean open = Boolean.valueOf(s[3]);
                    for (ClickMenuCategory cat : this.getCategories()) {
                        String name2 = cat.c.name();
                        if (!name2.equals(name)) continue;
                        cat.x = x;
                        cat.y = y;
                        cat.uiMenuMods.open = open;
                    }
                }
                catch (Exception s) {}
            }
            br.close();
        }
    }
}
