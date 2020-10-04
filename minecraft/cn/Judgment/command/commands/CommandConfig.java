package cn.Judgment.command.commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import cn.Judgment.Client;
import cn.Judgment.Value;
import cn.Judgment.command.Command;
import cn.Judgment.mod.Mod;
import cn.Judgment.mod.ModManager;
import cn.Judgment.ui.ClientNotification;
import cn.Judgment.util.ClientUtil;
import cn.Judgment.util.FileUtil;
import cn.Judgment.util.timeUtils.TimeHelper;
import net.minecraft.client.Minecraft;

public class CommandConfig extends Command{
	 public TimeHelper timer = new TimeHelper();
	 
	 public String mods = "https://gitee.com/HanFia/Kyra/raw/master/mod.txt";
	 public String value = "https://gitee.com/HanFia/Kyra/raw/master/values.txt";
	public CommandConfig(String[] commands) {
		super(commands);
		this.setArgs("-Config <Hypixel>");
	}
	
	@Override
	public void onCmd(String[] args) {

		if(args.length < 2) {
			ClientUtil.sendClientMessage(this.getArgs(), ClientNotification.Type.WARNING);
		} else if (args.length > 1 && args[1].equalsIgnoreCase("hypixel")) {
			Mod.TS = true;
			ModManager.getModByName("HUD").set(false);
			
		URL url;
		try {
			url = new URL(mods);
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(
		                       new InputStreamReader(conn.getInputStream()));
			String inputLine;
			File file = new File(String.valueOf((Object)FileUtil.fileDir) + "/Hypixelmods.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			while ((inputLine = br.readLine()) != null) {
				bw.write(inputLine+"\n");
			}
			bw.close();
			br.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		URL url2;
		try {
			url2 = new URL(value);
			URLConnection conn = url2.openConnection();
			BufferedReader br = new BufferedReader(
		                       new InputStreamReader(conn.getInputStream()));
			String inputLine;
			File file = new File(String.valueOf((Object)FileUtil.fileDir) + "/Hypixelvaluse.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			while ((inputLine = br.readLine()) != null) {
				bw.write(inputLine+"\n");
			}
			bw.close();
			br.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			this.loadMods();
			this.loadValues();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		Mod.TS = false;
		}else {
			ClientUtil.sendClientMessage(this.getArgs(), ClientNotification.Type.WARNING);
		}
	}

    
	public void loadMods() throws IOException {
		   File f = new File(String.valueOf((Object)FileUtil.fileDir) + "/Hypixelmods.txt");
        if (!f.exists()) {
            f.createNewFile();
        } else {
            String line;
            @SuppressWarnings("resource")
			BufferedReader br = new BufferedReader((Reader)new FileReader(f));
            while ((line = br.readLine()) != null) {
                if (!line.contains((CharSequence)":")) continue;
                String[] split = line.split(":");
                Mod m = ModManager.getModByName((String)split[0]);
                boolean state = Boolean.parseBoolean((String)split[1]);
                if (m == null) continue;
                m.set(state, false);
            }
        }
    }
	
	public void loadValues() throws IOException {
		File f = new File(String.valueOf((Object)FileUtil.fileDir) + "/Hypixelvaluse.txt");
        if (!f.exists()) {
            f.createNewFile();
        } else {
            String line;
            @SuppressWarnings("resource")
			BufferedReader br = new BufferedReader((Reader)new FileReader(f));
            while ((line = br.readLine()) != null) {
                if (!line.contains((CharSequence)":")) continue;
                String[] split = line.split(":");
                for (Value value : Value.list) {
                    if (!split[0].equalsIgnoreCase(value.getValueName())) continue;
                    if (value.isValueBoolean && split[1].equalsIgnoreCase("b")) {
                        value.setValueState((Object)Boolean.parseBoolean((String)split[2]));
                        continue;
                    }
                    if (value.isValueDouble && split[1].equalsIgnoreCase("d")) {
                        value.setValueState((Object)Double.parseDouble((String)split[2]));
                        continue;
                    }
                    if (!value.isValueMode || !split[1].equalsIgnoreCase("s") || !split[2].equalsIgnoreCase(value.getModeTitle())) continue;
                    value.setCurrentMode(Integer.parseInt((String)split[3]));
                }
            }
        }
    }
}
