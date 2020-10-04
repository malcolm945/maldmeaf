package cn.Judgment.mod.mods.COMBAT;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventPreMotion;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.util.ClientUtil;
import cn.Judgment.util.timeUtils.TimeHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;

public class AutoPot
extends Mod {
    private Value<Double> delay = new Value<Double>("AutoPot_Delay", 50.0, 0.0, 1000.0, 10.0);
    private Value<Double> health = new Value<Double>("AutoPot_Health", 6.0, 0.5, 9.5, 0.5);
    private Value<Boolean> throwInInv = new Value<Boolean>("AutoPot_OnlyInv", true);
    private TimeHelper timer = new TimeHelper();
    private TimeHelper throwTimer = new TimeHelper();
    private boolean nextTick = false;

    public AutoPot() {
    	super("AutoPot", Category.COMBAT);
    }
    @Override
    public void onDisable() {
        super.onDisable();

    }
    public void onEnable() {
    	super.isEnabled();

    }
    @EventTarget
    public void onPre(EventPreMotion event) {
        if ((double)(this.mc.thePlayer.getHealth() / 2.0f) <= this.health.getValueState()) {
            event.pitch = 90.0f;
            this.getPotion();
            this.nextTick = true;
        }
        if (this.nextTick) {
            event.pitch = 90.0f;
            this.throwPotion();
            this.nextTick = false;
        }
    }
    

    private void getPotion() {
        int slotId = this.getFreeSlot();
        if (slotId != -1) {
            int id = 9;
            while (id <= 35) {
                ItemStack currentItem;
                Slot currentSlot = this.mc.thePlayer.inventoryContainer.getSlot(id);
                if (currentSlot.getHasStack() && (currentItem = currentSlot.getStack()).getItem() instanceof ItemPotion && this.isSplashPotion(currentItem) && this.timer.isDelayComplete(this.delay.getValueState().intValue())) {
                    this.mc.playerController.windowClick(0, id, 0, 1, this.mc.thePlayer);
                    slotId = this.getFreeSlot();
                    this.timer.reset();
                }
                ++id;
            }
        }
    }

    private void throwPotion() {
        int slotId = this.getFreeSlot();
        if (slotId != -1) {
            int id = 36;
            while (id <= 44) {
                ItemStack currentItem;
                Slot currentSlot = this.mc.thePlayer.inventoryContainer.getSlot(id);
                if (currentSlot.getHasStack() && (currentItem = currentSlot.getStack()).getItem() instanceof ItemPotion && this.isSplashPotion(currentItem) && this.throwTimer.isDelayComplete(this.delay.getValueState().intValue())) {
                    int old = this.mc.thePlayer.inventory.currentItem;
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(id - 36));
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), -1, this.mc.thePlayer.inventoryContainer.getSlot(id).getStack(), 0.0f, 0.0f, 0.0f));
                    this.mc.thePlayer.inventory.currentItem = id - 36;
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getCurrentItem()));
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(old));
                    this.mc.thePlayer.inventory.currentItem = old;
                    this.throwTimer.reset();
                }
                ++id;
            }
        }
    }

    private boolean isSplashPotion(ItemStack itemStack) {
        return ItemPotion.isSplash(itemStack.getMetadata());
    }

    private int getFreeSlot() {
        int id = 36;
        while (id < 45) {
            Slot currentSlot = this.mc.thePlayer.inventoryContainer.getSlot(id);
            if (!currentSlot.getHasStack()) {
                return 1;
            }
            ++id;
        }
        return -1;
    }
}

