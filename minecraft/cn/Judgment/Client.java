package cn.Judgment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.lwjgl.opengl.Display;

import cn.Judgment.command.CommandManager;
import cn.Judgment.mod.ModManager;
import cn.Judgment.ui.click.UIClick;
import cn.Judgment.ui.font.FontLoaders;
import cn.Judgment.util.FileUtil;
import cn.Judgment.util.fontRenderer.FontManager;
import net.minecraft.client.Minecraft;

public class Client {
	
	public static final boolean INSTANCE = false;
	public static String CLIENT_name = "Judgment";
	public static String CLIENT_VER = "1.2";
	public static String CLIENT_Bulid = "200420";
	public static String Server = "Vanilla";
	public static String playerName;
    public static String playingGame = "NoGames";
	public static boolean isClientLoading;
    public static String UserName;
	public static Client instance;
    public static boolean paiduser;
	public static Logger LOGGER = Logger.getLogger(CLIENT_name);
	

	public FileUtil fileUtil;
	public ModManager modMgr;
	public FileUtil fileMgr;
	public FontManager fontMgr;
	public static FontLoaders fontMgrs;
	public static FontManager fontManager;
	public static Minecraft mc;
	public UIClick clickface;
	public CommandManager cmdMgr;
	
	public Client() {
		instance = this;
		isClientLoading = true;
		
	}
	
	public void onClientStart() {
		modMgr = new ModManager();
		fontManager = fontMgr = new FontManager();
		fileMgr = new FileUtil();
		clickface = new UIClick();
		cmdMgr = new CommandManager();
		//new Tiplan();
		Display.setTitle(CLIENT_name + " v"+CLIENT_VER);
		isClientLoading = false;
	}
	
	public void onClientStop() {
		
	}
	
    public static Client getInstance() {
        return instance;
    }
	
	public UIClick showClickGui() {
		return this.clickface;
	}

	public static String sendGet(String url) throws MalformedURLException, IOException {
        String inputLine;
        HttpURLConnection con = (HttpURLConnection)new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        @SuppressWarnings("resource")
		BufferedReader in = new BufferedReader((Reader)new InputStreamReader(con.getInputStream()));
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
            response.append("\n");
        }
        in.close();
        return response.toString();
    }


	public FileUtil getFileUtil() {
        return this.fileUtil;
    }
}
