package cn.Judgment.command.commands;

import cn.Judgment.command.Command;
import cn.Judgment.ui.ClientNotification;
import cn.Judgment.util.ClientUtil;
import net.minecraft.client.Minecraft;

public class CommandWdr extends Command {

	public CommandWdr(String[] commands) {
		super(commands);
		this.setArgs("-wdr <Playername>");
	}
	
	@Override
	public void onCmd(String[] args) {
		if(args.length < 2) {
			ClientUtil.sendClientMessage(this.getArgs(), ClientNotification.Type.WARNING);
		} else {
			ClientUtil.sendClientMessage("Reported " + args[1], ClientNotification.Type.SUCCESS);
			Minecraft.getMinecraft().thePlayer.sendChatMessage("/wdr " + args[1] + " Fly KillAura AutoClicker Speed AntiKnockBack Reach Dolphin");
		}
	}
	

}
