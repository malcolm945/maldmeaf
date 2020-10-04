package cn.Judgment.mod.mods.PLAYER;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.events.EventPacket;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;

public class AutoTool extends Mod {
	public AutoTool() {
		super("AutoTool", Category.PLAYER);
    }
	public Class type() {
        return EventPacket.class;
    }

	@EventTarget
    public void handle(EventPacket event) {
        if (!(event.getPacket() instanceof C07PacketPlayerDigging) || event.getType() != 0) {
            return;
        }
        C07PacketPlayerDigging packet = (C07PacketPlayerDigging)event.getPacket();
        if (packet.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
            AutoTool.autotool(packet.getPosition());
        }
    }

    private static void autotool(BlockPos position) {
        Block block = Minecraft.theWorld.getBlockState(position).getBlock();
        int itemIndex = AutoTool.getStrongestItem(block);
        if (itemIndex < 0) {
            return;
        }
        float itemStrength = AutoTool.getStrengthAgainstBlock(block, Minecraft.thePlayer.inventory.mainInventory[itemIndex]);
        if (Minecraft.thePlayer.getHeldItem() != null && AutoTool.getStrengthAgainstBlock(block, Minecraft.thePlayer.getHeldItem()) >= itemStrength) {
            return;
        }
        Minecraft.thePlayer.inventory.currentItem = itemIndex;
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(itemIndex));
    }

    private static int getStrongestItem(Block block) {
        float strength = Float.NEGATIVE_INFINITY;
        int strongest = -1;
        int i = 0;
        while (i < 9) {
            float itemStrength;
            ItemStack itemStack = Minecraft.thePlayer.inventory.mainInventory[i];
            if (itemStack != null && itemStack.getItem() != null && (itemStrength = AutoTool.getStrengthAgainstBlock(block, itemStack)) > strength && itemStrength != 1.0f) {
                strongest = i;
                strength = itemStrength;
            }
            ++i;
        }
        return strongest;
    }

    public static float getStrengthAgainstBlock(Block block, ItemStack item) {
        float strength = item.getStrVsBlock(block);
        if (!EnchantmentHelper.getEnchantments(item).containsKey(Enchantment.efficiency.effectId) || strength == 1.0f) {
            return strength;
        }
        int enchantLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, item);
        return strength + (float)(enchantLevel * enchantLevel + 1);
    }

}
