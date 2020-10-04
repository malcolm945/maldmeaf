package cn.Judgment.command.commands;

import java.util.Iterator;

import cn.Judgment.Client;
import cn.Judgment.command.Command;
import cn.Judgment.mod.mods.RENDER.BlockESP;
import cn.Judgment.ui.ClientNotification;
import cn.Judgment.util.ClientUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;

public class CommandBlock extends Command {
	
	public CommandBlock(String[] commands) {
        super(commands);
        this.setArgs("-block (add | remove) <id>, (clear | list)");
    }
	
	@Override
    public void onCmd(String[] args) {
		if (args.length > 1 && args[1].equalsIgnoreCase("clear")) {
			BlockESP.getBlockIds().clear();
            ClientUtil.sendClientMessage("Cleared list!", ClientNotification.Type.SUCCESS);
            Minecraft.getMinecraft().renderGlobal.loadRenderers();
		} else if (args.length > 1 && args[1].equalsIgnoreCase("list")) {
			String all = "Empty";
            Iterator<Integer> iterator = BlockESP.getBlockIds().iterator();
            while (iterator.hasNext()) {
                int id = iterator.next();
                String name = Block.getBlockById(id).getLocalizedName();
                all = all.equals("Empty") ? name : String.valueOf(all) + ", " + name;
            }
            ClientUtil.sendClientMessage(all, ClientNotification.Type.INFO);
		} else if (args.length > 2 && args[1].equalsIgnoreCase("add")) {
			try {
                int id = Integer.valueOf(args[2]);
                if ((Block.getBlockById(id).isFullBlock() || id == 144 || id == 166) && !BlockESP.getBlockIds().contains(new Integer(id))) {
                    BlockESP.getBlockIds().add(new Integer(id));
                    ClientUtil.sendClientMessage("Added " + Block.getBlockById(id).getLocalizedName(), ClientNotification.Type.SUCCESS);
                }
                ClientUtil.sendClientMessage("Invalid Id", ClientNotification.Type.WARNING);
            } catch (Exception e) {
                try {
                    Block block = Block.getBlockFromName(args[2]);
                    String name = block.getLocalizedName();
                    if (BlockESP.getBlockIds().contains(new Integer(Block.getIdFromBlock(block)))) {
                        return;
                    }
                    ClientUtil.sendClientMessage("Added " + name, ClientNotification.Type.SUCCESS);
                    BlockESP.getBlockIds().add(new Integer(Block.getIdFromBlock(block)));
                }
                catch (Exception e1) {
                    ClientUtil.sendClientMessage("Invalid Id", ClientNotification.Type.WARNING);
                }
            }
		} else if (args.length > 2 && args[1].equalsIgnoreCase("remove")) {
			try {
                int id = Integer.valueOf(args[2]);
                this.removeId(id);
                ClientUtil.sendClientMessage("Removed " + Block.getBlockById(id).getLocalizedName(), ClientNotification.Type.ERROR);
            }
            catch (Exception e) {
                try {
                    Block block = Block.getBlockFromName(args[2]);
                    String name = block.getLocalizedName();
                    this.removeId(Block.getIdFromBlock(block));
                    ClientUtil.sendClientMessage("Removed " + name, ClientNotification.Type.ERROR);
                }
                catch (Exception e1) {
                    ClientUtil.sendClientMessage("Invalid Id", ClientNotification.Type.WARNING);
                }
            }
        } else {
            ClientUtil.sendClientMessage(this.getArgs(), ClientNotification.Type.INFO);
		}
		Minecraft.getMinecraft().renderGlobal.loadRenderers();
		Client.instance.fileMgr.saveBlocks();
	}
	
	private void removeId(int number) {
        int i = 0;
        while (i < BlockESP.getBlockIds().size()) {
            int id = BlockESP.getBlockIds().get(i);
            if (id == number) {
                BlockESP.getBlockIds().remove(i);
            }
            ++i;
        }
    }
}
