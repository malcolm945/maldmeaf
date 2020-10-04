package cn.Judgment.command.commands;

import cn.Judgment.Tiplan;
import cn.Judgment.command.Command;
import cn.Judgment.util.ChatType;
import cn.Judgment.util.ClientUtil;

public class CommandTiplan extends Command {

	public CommandTiplan(String[] commands) {
		super(commands);
	}

	@Override
	public void onCmd(String[] args) {
		ClientUtil.sendChatMessage("Changing tiplan...", ChatType.INFO);
		new Tiplan();
	}
}
