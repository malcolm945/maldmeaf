package cn.Judgment.command.commands;

import cn.Judgment.Client;
import cn.Judgment.command.Command;
import cn.Judgment.ui.ClientNotification;
import cn.Judgment.util.ClientUtil;

public class CommandVersion extends Command {

	public CommandVersion(String[] commands) {
		super(commands);
	}

	@Override
	public void onCmd(String[] args) {
		ClientUtil.sendClientMessage(Client.CLIENT_name + " v" + Client.CLIENT_VER , ClientNotification.Type.INFO);
	}
	
}
