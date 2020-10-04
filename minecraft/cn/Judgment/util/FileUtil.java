package cn.Judgment.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Iterator;

import org.lwjgl.input.Keyboard;

import cn.Judgment.Client;
import cn.Judgment.Value;
import cn.Judgment.mod.Mod;
import cn.Judgment.mod.ModManager;
import cn.Judgment.mod.mods.RENDER.BlockESP;
import net.minecraft.client.Minecraft;

public class FileUtil {
	private Minecraft mc = Minecraft.getMinecraft();
    public static String fileDir;
    
    public FileUtil() {
        this.fileDir = String.valueOf((Object)this.mc.mcDataDir.getAbsolutePath()) + "/" + Client.CLIENT_name;
        File fileFolder = new File(this.fileDir);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        try {
            this.loadKeys();
            this.loadValues();
            this.loadMods();
            this.loadBlocks();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void saveBlocks() {
        File f = new File(String.valueOf(this.fileDir) + "/blocks.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            PrintWriter pw = new PrintWriter(f);
            Iterator<Integer> iterator = BlockESP.getBlockIds().iterator();
            while (iterator.hasNext()) {
                int id = iterator.next();
                pw.print(String.valueOf(String.valueOf(id)) + "\n");
            }
            pw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadBlocks() throws IOException {
        File f = new File(String.valueOf(this.fileDir) + "/blocks.txt");
        if (!f.exists()) {
            f.createNewFile();
        } else {
            String line;
            @SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(f));
            while ((line = br.readLine()) != null) {
                try {
                    int id = Integer.valueOf(line);
                    BlockESP.getBlockIds().add(id);
                }
                catch (Exception id) {
                }
            }
        }
    }
    
    public void saveKeys() {
        File f = new File(String.valueOf((Object)this.fileDir) + "/keys.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            PrintWriter pw = new PrintWriter(f);
            for (Mod m : ModManager.getModList()) {
                String keyName = m.getKey() < 0 ? "None" : Keyboard.getKeyName((int)m.getKey());
                pw.write(String.valueOf((Object)m.getName()) + ":" + keyName + "\n");
            }
            pw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadKeys() throws IOException {
        File f = new File(String.valueOf((Object)this.fileDir) + "/keys.txt");
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
                int key = Keyboard.getKeyIndex((String)split[1]);
                if (m == null || key == -1) continue;
                m.setKey(key);
            }
            
        }
    }
    
    public void saveMods() {
        File f = new File(String.valueOf((Object)this.fileDir) + "/mods.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            PrintWriter pw = new PrintWriter(f);
            for (Mod m : ModManager.getModList()) {
                pw.print(String.valueOf((Object)m.getName()) + ":" + m.isEnabled() + "\n");
            }
            pw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMods() throws IOException {
        File f = new File(String.valueOf((Object)this.fileDir) + "/mods.txt");
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
    
    public void saveValues() {
        File f = new File(String.valueOf((Object)this.fileDir) + "/values.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            PrintWriter pw = new PrintWriter(f);
            for (Value value : Value.list) {
                String valueName = value.getValueName();
                if (value.isValueBoolean) {
                    pw.print(String.valueOf((Object)valueName) + ":b:" + value.getValueState() + "\n");
                    continue;
                }
                if (value.isValueDouble) {
                    pw.print(String.valueOf((Object)valueName) + ":d:" + value.getValueState() + "\n");
                    continue;
                }
                if (!value.isValueMode) continue;
                pw.print(String.valueOf((Object)valueName) + ":s:" + value.getModeTitle() + ":" + value.getCurrentMode() + "\n");
            }
            pw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadValues() throws IOException {
        File f = new File(String.valueOf((Object)this.fileDir) + "/values.txt");
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
