package cn.Judgment.command.commands;

import cn.Judgment.command.Command;
import cn.Judgment.mod.Mod;
import cn.Judgment.mod.ModManager;
import cn.Judgment.ui.ClientNotification;
import cn.Judgment.util.ClientUtil;

public class CommandToggle extends Command {

	public CommandToggle(String[] commands) {
		super(commands);
		this.setArgs("-toggle <module>");
	}

	@Override
	public void onCmd(String[] args) {
		if(args.length < 2) {
			ClientUtil.sendClientMessage(this.getArgs(), ClientNotification.Type.WARNING);
		} else {
			String mod = args[1];
			for (Mod m : ModManager.getModList()) {
				if(m.getName().equalsIgnoreCase(mod)) {
					m.set(m.isEnabled());
					ClientUtil.sendClientMessage("Module " + m.getName() + " toggled", ClientNotification.Type.SUCCESS);
					return;
				}
			}
		}
	}
}
