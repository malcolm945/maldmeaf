package cn.Judgment.command.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import cn.Judgment.command.Command;
import cn.Judgment.mod.mods.WORLD.Spammer;
import cn.Judgment.ui.ClientNotification;
import cn.Judgment.util.ClientUtil;
import net.minecraft.client.Minecraft;

public class CommandSpammer extends Command {
	
	private static String fileDir = String.valueOf(Minecraft.getMinecraft().mcDataDir.getAbsolutePath()) + "/" + "Kyra";

    public CommandSpammer(String[] commands) {
        super(commands);
        this.setArgs("-spammer <Text>");
    }

    @Override
    public void onCmd(String[] args) {
        if (args.length != 2) {
        	ClientUtil.sendClientMessage(this.getArgs(), ClientNotification.Type.WARNING);
            return;
        }
        Spammer.message = args[1];
        CommandSpammer.saveMessage();
        ClientUtil.sendClientMessage("Changed message " + args[1], ClientNotification.Type.SUCCESS);
        
        super.onCmd(args);
    }

    public static void saveMessage() {
        File f = new File(String.valueOf(fileDir) + "/spammer.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            PrintWriter pw = new PrintWriter(f);
            pw.print(Spammer.message);
            pw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadMessage() throws IOException {
        File f = new File(String.valueOf(fileDir) + "/spammer.txt");
        if (!f.exists()) {
            f.createNewFile();
        } else {
            String line;
            @SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(f));
            while ((line = br.readLine()) != null) {
                try {
                    String message;
                    Spammer.message = message = String.valueOf(line);
                }
                catch (Exception message) {
                }
            }
        }
    }

}
