package cn.Judgment.util.fontRenderer;

import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cn.Judgment.util.fontRenderer.CFont.CFontRenderer;
import net.minecraft.client.Minecraft;

public class FontManager {
	private HashMap<String, HashMap<Float, UnicodeFontRenderer>> fonts = new HashMap();
	
	public UnicodeFontRenderer tahoma14;
	public UnicodeFontRenderer tahoma15;
	public UnicodeFontRenderer tahoma17;
	public UnicodeFontRenderer tahoma18;
	public UnicodeFontRenderer tahoma20;
	public UnicodeFontRenderer tahoma25;
	public UnicodeFontRenderer tahoma40;
	public UnicodeFontRenderer tahoma60;
	
	public UnicodeFontRenderer sansation15;
	public UnicodeFontRenderer sansation18;
	
    public UnicodeFontRenderer simpleton13;
    public UnicodeFontRenderer simpleton17;
    public UnicodeFontRenderer simpleton30;
    
    public UnicodeFontRenderer consolasbold14;
    public UnicodeFontRenderer consolasbold18;
    public UnicodeFontRenderer consolasbold20;
    
	
	public UnicodeFontRenderer Sigma7;
	public UnicodeFontRenderer Sigma;
	public UnicodeFontRenderer BLC55;
    
    public UnicodeFontRenderer verdana12;

    
	
	public FontManager() {
		tahoma14 = this.getFont("tahoma", 14f);
		tahoma15 = this.getFont("tahoma", 15f);
		tahoma17 = this.getFont("tahoma", 17f);
		tahoma18 = this.getFont("tahoma", 18f);
		tahoma20 = this.getFont("tahoma", 20f);
		tahoma25 = this.getFont("tahoma", 25f);
		tahoma40 = this.getFont("tahoma", 40f);
		tahoma60 = this.getFont("tahoma", 60f);

		sansation15 = this.getFont("sansation", 15f);
		sansation18 = this.getFont("sansation", 18f);
		
		Sigma7 = this.getFont("Sigma", 7f);
		Sigma = this.getFont("Sigma", 8f);
		
		BLC55 = this.getFont("BLC", 55f);
		verdana12 = this.getFont("verdana", 12f);
        this.simpleton13 = this.getFont("simpleton", 13.0f, true);
        this.simpleton17 = this.getFont("simpleton", 17.0f, true);
        this.simpleton30 = this.getFont("simpleton", 30.0f, true);
        this.consolasbold14 = this.getFont("consolasbold", 14.0f);
        this.consolasbold18 = this.getFont("consolasbold", 18.0f);
        this.consolasbold20 = this.getFont("consolasbold", 20.0f);

	}
	
	public UnicodeFontRenderer getFont(String name, float size) {
        UnicodeFontRenderer unicodeFont = null;
        try {
            if (this.fonts.containsKey(name) && this.fonts.get(name).containsKey(Float.valueOf(size))) {
                return this.fonts.get(name).get(Float.valueOf(size));
            }
            InputStream inputStream = this.getClass().getResourceAsStream("fonts/" + name + ".ttf");
            Font font = null;
            font = Font.createFont(0, inputStream);
            unicodeFont = new UnicodeFontRenderer(font.deriveFont(size));
            unicodeFont.setUnicodeFlag(true);
            unicodeFont.setBidiFlag(Minecraft.getMinecraft().mcLanguageManager.isCurrentLanguageBidirectional());
            HashMap<Float, UnicodeFontRenderer> map = new HashMap<Float, UnicodeFontRenderer>();
            if (this.fonts.containsKey(name)) {
                map.putAll((Map)this.fonts.get(name));
            }
            map.put(Float.valueOf(size), unicodeFont);
            this.fonts.put(name, map);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return unicodeFont;
    }

    public UnicodeFontRenderer getFont(String name, float size, boolean b) {
        UnicodeFontRenderer unicodeFont = null;
        try {
            if (this.fonts.containsKey(name) && this.fonts.get(name).containsKey(Float.valueOf(size))) {
                return this.fonts.get(name).get(Float.valueOf(size));
            }
            InputStream inputStream = this.getClass().getResourceAsStream("fonts/" + name + ".otf");
            Font font = null;
            font = Font.createFont(0, inputStream);
            unicodeFont = new UnicodeFontRenderer(font.deriveFont(size));
            unicodeFont.setUnicodeFlag(true);
            unicodeFont.setBidiFlag(Minecraft.getMinecraft().mcLanguageManager.isCurrentLanguageBidirectional());
            HashMap<Float, UnicodeFontRenderer> map = new HashMap<Float, UnicodeFontRenderer>();
            if (this.fonts.containsKey(name)) {
                map.putAll((Map)this.fonts.get(name));
            }
            map.put(Float.valueOf(size), unicodeFont);
            this.fonts.put(name, map);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return unicodeFont;
    }
}
