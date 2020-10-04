package cn.Judgment.mod.mods.COMBAT;

import com.darkmagician6.eventapi.EventTarget;
//mport com.sun.xml.internal.ws.api.config.management.policy.ManagementAssertion.Setting;

import cn.Judgment.Value;
import cn.Judgment.events.EventPreMotion;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.mod.ModManager;
import cn.Judgment.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;

public class AutoSoup
extends Mod {
    private Value<Double> health = new Value<Double>("AutoSoup_Health", 6.0, 0.5, 20.5, 0.5);
    private Value<Double> delay = new Value<Double>("AutoSoup_Delay", 50.0, 0.0, 1000.0, 10.0);
    private Value<Double> SLOT = new Value<Double>("AutoSoup_Slot", 7.0, 1.0, 9.0, 1.0);
    private Value<Boolean> aura = new Value<Boolean>("AutoSoup_AuraOpen", true);
    private Value<Boolean> DROP = new Value<Boolean>("AutoSoup_Heads", true);
    Timer timer = new Timer();
    public AutoSoup() {
    	super("AutoSoup", Category.COMBAT);
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
       	int Slot = SLOT.getValueState().intValue()-1;
    	if(DROP.getValueState().booleanValue()) {
    		this.setDisplayName("Head");
    	}
        int soupSlot = getSoupFromInventory();
        if(!ModManager.getModuleByClass(Killaura.class).isEnabled()&& aura.getValueState()){
     	   return;
         }        
        
        if (soupSlot != -1 && mc.thePlayer.getHealth() < (health.getValueState()).floatValue()
                && timer.delay(delay.getValueState().floatValue())) {
            swap(getSoupFromInventory(), Slot);
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(Slot));
            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            timer.reset();
        }
        
    	/*if(Killaura.target != null && mc.thePlayer.isPotionActive(Potion.regeneration) && !(mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword)) {
    		 mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem = 0));
	            mc.playerController.updateController();
		}*/
    }

    protected void swap(int slot, int hotbarNum) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
    }

    private int getSoupFromInventory() {
        Minecraft mc = Minecraft.getMinecraft();
        int soup = -1;
        int counter = 0;

        for(int i = 1; i < 45; ++i) {
           if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
              ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
              Item item = is.getItem();
              boolean shouldApple = DROP.getValueState() && (Item.getIdFromItem(item) == Item.getIdFromItem(Items.skull) || Item.getIdFromItem(item) == Item.getIdFromItem(Items.baked_potato)) && (!mc.thePlayer.isPotionActive(Potion.regeneration) || mc.thePlayer.isPotionActive(Potion.regeneration) && mc.thePlayer.getActivePotionEffect(Potion.regeneration).getDuration() <= 1 || !mc.thePlayer.isPotionActive(Potion.absorption) && is.stackSize > 1);
              if (Item.getIdFromItem(item) == 282 || shouldApple) {
                 ++counter;
                 soup = i;
              }
           }
        }

        return soup;
     }
}

