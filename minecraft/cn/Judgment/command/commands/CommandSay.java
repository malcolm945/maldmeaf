package cn.Judgment.command.commands;

import cn.Judgment.command.Command;
import cn.Judgment.ui.ClientNotification;
import cn.Judgment.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class CommandSay
extends Command {
    public static boolean blockedmsg = false;

    public CommandSay(String[] commands) {
        super(commands);
        this.setArgs("-say <text>");
    }

    @Override
    public void onCmd(String[] args) {
        String msg = "";
        if (args.length <= 1) {
            ClientUtil.sendClientMessage(this.getArgs(), ClientNotification.Type.WARNING);
            return;
        }
        int i = 1;
        while (i < args.length) {
            msg = String.valueOf(String.valueOf(msg)) + args[i] + " ";
            ++i;
        }
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage(msg));
        super.onCmd(args);
    }
}
