package cn.Judgment.mod.mods.COMBAT;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.events.EventUpdate;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.util.InventoryUtils;
import cn.Judgment.util.TimeHelper;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class AutoSword extends Mod {
   private ItemStack bestSword;
   private ItemStack prevBestSword;
   private boolean shouldSwitch = false;
   public TimeHelper timer = new TimeHelper();

   public AutoSword() {
      super("AutoSword", Category.COMBAT);
   }

   @EventTarget
   private void onUpdate(EventUpdate event) {
      Minecraft var10000 = mc;
      if(Minecraft.thePlayer.ticksExisted % 7 == 0) {
         var10000 = mc;
         if(!Minecraft.thePlayer.capabilities.isCreativeMode) {
            var10000 = mc;
            if(Minecraft.thePlayer.openContainer != null) {
               var10000 = mc;
               if(Minecraft.thePlayer.openContainer.windowId != 0) {
                  return;
               }
            }

            this.bestSword = this.getBestItem(ItemSword.class, Comparator.comparingDouble(this::getSwordDamage));
            if(this.bestSword != null) {
               boolean isInHBSlot = InventoryUtils.hotbarHas(this.bestSword.getItem(), 0);
               if(isInHBSlot) {
                  if(InventoryUtils.getItemBySlotID(0) != null) {
                     if(InventoryUtils.getItemBySlotID(0).getItem() instanceof ItemSword) {
                        isInHBSlot = this.getSwordDamage(InventoryUtils.getItemBySlotID(0)) >= this.getSwordDamage(this.bestSword);
                     }
                  } else {
                     isInHBSlot = false;
                  }
               }

               if(this.prevBestSword != null && this.prevBestSword.equals(this.bestSword) && isInHBSlot) {
                  this.shouldSwitch = false;
               } else {
                  this.shouldSwitch = true;
                  this.prevBestSword = this.bestSword;
               }

               if(this.shouldSwitch && this.timer.isDelayComplete(1000L)) {
                  int slotHB = InventoryUtils.getBestSwordSlotID(this.bestSword, this.getSwordDamage(this.bestSword));
                  switch(slotHB) {
                  case 0:
                     slotHB = 36;
                     break;
                  case 1:
                     slotHB = 37;
                     break;
                  case 2:
                     slotHB = 38;
                     break;
                  case 3:
                     slotHB = 39;
                     break;
                  case 4:
                     slotHB = 40;
                     break;
                  case 5:
                     slotHB = 41;
                     break;
                  case 6:
                     slotHB = 42;
                     break;
                  case 7:
                     slotHB = 43;
                     break;
                  case 8:
                     slotHB = 44;
                  }

                  var10000 = mc;
                  Minecraft var10005 = mc;
                  Minecraft.playerController.windowClick(0, slotHB, 0, 2, Minecraft.thePlayer);
                  this.timer.reset();
               }

            }
         }
      }
   }

   private ItemStack getBestItem(Class itemType, Comparator comparator) {
      Minecraft var10000 = mc;
      Optional bestItem = Minecraft.thePlayer.inventoryContainer.inventorySlots.stream().map(Slot::getStack).filter(Objects::nonNull).filter((itemStack) -> {
         return itemStack.getItem().getClass().equals(itemType);
      }).max(comparator);
      return (ItemStack)bestItem.orElse((Object)null);
   }

   private double getSwordDamage(ItemStack itemStack) {
      double damage = 0.0D;
      Optional attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();
      if(attributeModifier.isPresent()) {
         damage = ((AttributeModifier)attributeModifier.get()).getAmount();
      }

      return damage + (double)EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED);
   }
}
