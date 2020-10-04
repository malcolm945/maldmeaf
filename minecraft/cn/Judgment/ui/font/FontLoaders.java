package cn.Judgment.ui.font;

import java.awt.Font;
import java.io.InputStream;
import java.io.PrintStream;

import cn.Judgment.util.fontRenderer.CFont.CFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
public abstract class FontLoaders {
	public static CFontRenderer kiona6 = new CFontRenderer(FontLoaders.getKiona(6), true, true);
	public static CFontRenderer kiona7 = new CFontRenderer(FontLoaders.getKiona(7), true, true);
	public static CFontRenderer kiona8 = new CFontRenderer(FontLoaders.getKiona(8), true, true);
	public static CFontRenderer kiona10 = new CFontRenderer(FontLoaders.getKiona(10), true, true);
	public static CFontRenderer kiona12 = new CFontRenderer(FontLoaders.getKiona(12), true, true);
	public static CFontRenderer kiona14 = new CFontRenderer(FontLoaders.getKiona(14), true, true);
    public static CFontRenderer kiona16 = new CFontRenderer(FontLoaders.getKiona(16), true, true);
    public static CFontRenderer kiona18 = new CFontRenderer(FontLoaders.getKiona(18), true, true);
    public static CFontRenderer kiona20 = new CFontRenderer(FontLoaders.getKiona(20), true, true);
    public static CFontRenderer kiona22 = new CFontRenderer(FontLoaders.getKiona(22), true, true);
    public static CFontRenderer kiona24 = new CFontRenderer(FontLoaders.getKiona(24), true, true);
    public static CFontRenderer kiona26 = new CFontRenderer(FontLoaders.getKiona(26), true, true);
    public static CFontRenderer kiona28 = new CFontRenderer(FontLoaders.getKiona(28), true, true);

    public static CFontRenderer Sigma6 = new CFontRenderer(FontLoaders.Sigma(6), true, true);
	public static CFontRenderer Sigma7 = new CFontRenderer(FontLoaders.Sigma(7), true, true);
	public static CFontRenderer Sigma8 = new CFontRenderer(FontLoaders.Sigma(8), true, true);
	public static CFontRenderer Sigma10 = new CFontRenderer(FontLoaders.Sigma(10), true, true);
	public static CFontRenderer Sigma12 = new CFontRenderer(FontLoaders.Sigma(12), true, true);
	public static CFontRenderer Sigma14 = new CFontRenderer(FontLoaders.Sigma(14), true, true);
    public static CFontRenderer Sigma16 = new CFontRenderer(FontLoaders.Sigma(16), true, true);
    public static CFontRenderer Sigma18 = new CFontRenderer(FontLoaders.Sigma(18), true, true);
    public static CFontRenderer Sigma20 = new CFontRenderer(FontLoaders.Sigma(20), true, true);
    public static CFontRenderer Sigma22 = new CFontRenderer(FontLoaders.Sigma(22), true, true);
    public static CFontRenderer Sigma24 = new CFontRenderer(FontLoaders.Sigma(24), true, true);
    public static CFontRenderer Sigma26 = new CFontRenderer(FontLoaders.Sigma(26), true, true);
    public static CFontRenderer Sigma28 = new CFontRenderer(FontLoaders.Sigma(28), true, true);
    
    public static CFontRenderer Backs6 = new CFontRenderer(FontLoaders.Backs(6), true, true);
	public static CFontRenderer Backs7 = new CFontRenderer(FontLoaders.Backs(7), true, true);
	public static CFontRenderer Backs8 = new CFontRenderer(FontLoaders.Backs(8), true, true);
	public static CFontRenderer Backs10 = new CFontRenderer(FontLoaders.Backs(10), true, true);
	public static CFontRenderer Backs12 = new CFontRenderer(FontLoaders.Backs(12), true, true);
	public static CFontRenderer Backs14 = new CFontRenderer(FontLoaders.Backs(14), true, true);
    public static CFontRenderer Backs16 = new CFontRenderer(FontLoaders.Backs(16), true, true);
    public static CFontRenderer Backs18 = new CFontRenderer(FontLoaders.Backs(18), true, true);
    public static CFontRenderer Backs20 = new CFontRenderer(FontLoaders.Backs(20), true, true);
    public static CFontRenderer Backs22 = new CFontRenderer(FontLoaders.Backs(22), true, true);
    public static CFontRenderer Backs24 = new CFontRenderer(FontLoaders.Backs(24), true, true);
    public static CFontRenderer Backs26 = new CFontRenderer(FontLoaders.Backs(26), true, true);
    public static CFontRenderer Backs28 = new CFontRenderer(FontLoaders.Backs(28), true, true);
    
    
    private static Font getKiona(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Kyra/raleway.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    private static Font Backs(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Kyra/BLC.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    private static Font Sigma(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Kyra/Sigma.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
}