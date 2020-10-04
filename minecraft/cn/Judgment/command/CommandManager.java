package cn.Judgment.command;

import java.util.ArrayList;

import cn.Judgment.command.commands.CommandBind;
import cn.Judgment.command.commands.CommandBlock;
import cn.Judgment.command.commands.CommandConfig;
import cn.Judgment.command.commands.CommandHelp;
import cn.Judgment.command.commands.CommandSay;
import cn.Judgment.command.commands.CommandSpammer;
import cn.Judgment.command.commands.CommandTiplan;
import cn.Judgment.command.commands.CommandToggle;
import cn.Judgment.command.commands.CommandVersion;
import cn.Judgment.command.commands.CommandWdr;

public class CommandManager {
	private static ArrayList<Command> commands = new ArrayList();
	
	public CommandManager() {
		add(new CommandBind(new String[] {"bind"}));
		add(new CommandVersion(new String[] {"version", "v"}));
		add(new CommandToggle(new String[] {"toggle", "t"}));
		add(new CommandWdr(new String[] {"wdr"}));
		add(new CommandBlock(new String[] {"block"}));
		add(new CommandTiplan(new String[] {"tiplan"}));
		add(new CommandSpammer(new String[] {"spammer"}));
		add(new CommandSay(new String[] {"say"}));
		add(new CommandHelp(new String[] {"help"}));
		add(new CommandConfig(new String[] {"Config","c"}));
	}
	
	public void add(Command c) {
		this.commands.add(c);
	}
	
	public static ArrayList<Command> getCommands() {
        return commands;
    }
	
	public static String removeSpaces(String message) {
        String space = " ";
        String doubleSpace = "  ";
        while (message.contains(doubleSpace)) {
            message = message.replace(doubleSpace, space);
        }
        return message;
    }
}
