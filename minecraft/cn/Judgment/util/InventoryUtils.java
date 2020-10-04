package cn.Judgment.util;

import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;

public class InventoryUtils {
   public static Minecraft mc = Minecraft.getMinecraft();

   public void dropSlot(int slot) {
      int windowId = (new GuiInventory(Minecraft.thePlayer)).inventorySlots.windowId;
      Minecraft.playerController.windowClick(windowId, slot, 1, 4, Minecraft.thePlayer);
   }

   public static ItemStack getStackInSlot(int slot) {
      return Minecraft.thePlayer.inventory.getStackInSlot(slot);
   }

   public static boolean isBestArmorOfTypeInInv(ItemStack is) {
      try {
         if(is == null) {
            return false;
         }

         if(is.getItem() == null) {
            return false;
         }

         if(is.getItem() != null && !(is.getItem() instanceof ItemArmor)) {
            return false;
         }

         ItemArmor ia = (ItemArmor)is.getItem();
         int prot = getArmorProt(is);

         ItemStack stack;
         ItemArmor otherArmor;
         int i;
         for(i = 0; i < 4; ++i) {
            stack = Minecraft.thePlayer.inventory.armorInventory[i];
            if(stack != null) {
               otherArmor = (ItemArmor)stack.getItem();
               if(otherArmor.armorType == ia.armorType && getArmorProt(stack) >= prot) {
                  return false;
               }
            }
         }

         for(i = 0; i < Minecraft.thePlayer.inventory.getSizeInventory() - 4; ++i) {
            stack = Minecraft.thePlayer.inventory.getStackInSlot(i);
            if(stack != null && stack.getItem() instanceof ItemArmor) {
               otherArmor = (ItemArmor)stack.getItem();
               if(otherArmor.armorType == ia.armorType && otherArmor != ia && getArmorProt(stack) >= prot) {
                  return false;
               }
            }
         }
      } catch (Exception var7) {
         ;
      }

      return true;
   }

   public static boolean hotbarHas(Item item) {
      for(int index = 0; index <= 36; ++index) {
         Minecraft.getMinecraft();
         ItemStack stack = Minecraft.thePlayer.inventory.getStackInSlot(index);
         if(stack != null && stack.getItem() == item) {
            return true;
         }
      }

      return false;
   }

   public static boolean hotbarHas(Item item, int slotID) {
      for(int index = 0; index <= 36; ++index) {
         Minecraft.getMinecraft();
         ItemStack stack = Minecraft.thePlayer.inventory.getStackInSlot(index);
         if(stack != null && stack.getItem() == item && getSlotID(stack.getItem()) == slotID) {
            return true;
         }
      }

      return false;
   }

   public static int getSlotID(Item item) {
      for(int index = 0; index <= 36; ++index) {
         Minecraft.getMinecraft();
         ItemStack stack = Minecraft.thePlayer.inventory.getStackInSlot(index);
         if(stack != null && stack.getItem() == item) {
            return index;
         }
      }

      return -1;
   }

   public static ItemStack getItemBySlotID(int slotID) {
      for(int index = 0; index <= 36; ++index) {
         Minecraft.getMinecraft();
         ItemStack stack = Minecraft.thePlayer.inventory.getStackInSlot(index);
         if(stack != null && getSlotID(stack.getItem()) == slotID) {
            return stack;
         }
      }

      return null;
   }

   public static int getArmorProt(ItemStack i) {
      int armorprot = -1;
      if(i != null && i.getItem() != null && i.getItem() instanceof ItemArmor) {
         armorprot = ((ItemArmor)i.getItem()).getArmorMaterial().getDamageReductionAmount(getItemType(i)) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{i}, DamageSource.generic);
      }

      return armorprot;
   }

   public static int getBestSwordSlotID(ItemStack item, double damage) {
      for(int index = 0; index <= 36; ++index) {
         Minecraft.getMinecraft();
         ItemStack stack = Minecraft.thePlayer.inventory.getStackInSlot(index);
         if(stack != null && stack == item && getSwordDamage(stack) == getSwordDamage(item)) {
            return index;
         }
      }

      return -1;
   }

   private static double getSwordDamage(ItemStack itemStack) {
      double damage = 0.0D;
      Optional attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();
      if(attributeModifier.isPresent()) {
         damage = ((AttributeModifier)attributeModifier.get()).getAmount();
      }

      return damage + (double)EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED);
   }

   public boolean isBestChest(int slot) {
      if(getStackInSlot(slot) != null && getStackInSlot(slot).getItem() != null && getStackInSlot(slot).getItem() instanceof ItemArmor) {
         int slotProtection = ((ItemArmor)Minecraft.thePlayer.inventory.getStackInSlot(slot).getItem()).getArmorMaterial().getDamageReductionAmount(getItemType(Minecraft.thePlayer.inventory.getStackInSlot(slot))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{Minecraft.thePlayer.inventory.getStackInSlot(slot)}, DamageSource.generic);
         ItemArmor ia1;
         if(Minecraft.thePlayer.inventory.armorInventory[2] != null) {
            ItemArmor i = (ItemArmor)Minecraft.thePlayer.inventory.armorInventory[2].getItem();
            ItemStack otherProtection = Minecraft.thePlayer.inventory.armorInventory[2];
            ia1 = (ItemArmor)getStackInSlot(slot).getItem();
            int ia2 = ((ItemArmor)otherProtection.getItem()).getArmorMaterial().getDamageReductionAmount(getItemType(otherProtection)) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{otherProtection}, DamageSource.generic);
            if(ia2 > slotProtection || ia2 == slotProtection) {
               return false;
            }
         }

         for(int var7 = 0; var7 < Minecraft.thePlayer.inventory.getSizeInventory(); ++var7) {
            if(getStackInSlot(var7) != null && Minecraft.thePlayer.inventory.getStackInSlot(var7).getItem() instanceof ItemArmor) {
               int var8 = ((ItemArmor)Minecraft.thePlayer.inventory.getStackInSlot(var7).getItem()).getArmorMaterial().getDamageReductionAmount(getItemType(Minecraft.thePlayer.inventory.getStackInSlot(var7))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{Minecraft.thePlayer.inventory.getStackInSlot(var7)}, DamageSource.generic);
               ia1 = (ItemArmor)getStackInSlot(slot).getItem();
               ItemArmor var9 = (ItemArmor)getStackInSlot(var7).getItem();
               if(ia1.armorType == 1 && var9.armorType == 1 && var8 > slotProtection) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   public boolean isBestHelmet(int slot) {
      if(getStackInSlot(slot) != null && getStackInSlot(slot).getItem() != null && getStackInSlot(slot).getItem() instanceof ItemArmor) {
         int slotProtection = ((ItemArmor)Minecraft.thePlayer.inventory.getStackInSlot(slot).getItem()).getArmorMaterial().getDamageReductionAmount(getItemType(Minecraft.thePlayer.inventory.getStackInSlot(slot))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{Minecraft.thePlayer.inventory.getStackInSlot(slot)}, DamageSource.generic);
         ItemArmor ia1;
         if(Minecraft.thePlayer.inventory.armorInventory[3] != null) {
            ItemArmor i = (ItemArmor)Minecraft.thePlayer.inventory.armorInventory[3].getItem();
            ItemStack otherProtection = Minecraft.thePlayer.inventory.armorInventory[3];
            ia1 = (ItemArmor)getStackInSlot(slot).getItem();
            int ia2 = ((ItemArmor)otherProtection.getItem()).getArmorMaterial().getDamageReductionAmount(getItemType(otherProtection)) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{otherProtection}, DamageSource.generic);
            if(ia2 > slotProtection || ia2 == slotProtection) {
               return false;
            }
         }

         for(int var7 = 0; var7 < Minecraft.thePlayer.inventory.getSizeInventory(); ++var7) {
            if(getStackInSlot(var7) != null && Minecraft.thePlayer.inventory.getStackInSlot(var7).getItem() instanceof ItemArmor) {
               int var8 = ((ItemArmor)Minecraft.thePlayer.inventory.getStackInSlot(var7).getItem()).getArmorMaterial().getDamageReductionAmount(getItemType(Minecraft.thePlayer.inventory.getStackInSlot(var7))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{Minecraft.thePlayer.inventory.getStackInSlot(var7)}, DamageSource.generic);
               ia1 = (ItemArmor)getStackInSlot(slot).getItem();
               ItemArmor var9 = (ItemArmor)getStackInSlot(var7).getItem();
               if(ia1.armorType == 0 && var9.armorType == 0 && var8 > slotProtection) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   public boolean isBestLeggings(int slot) {
      if(getStackInSlot(slot) != null && getStackInSlot(slot).getItem() != null && getStackInSlot(slot).getItem() instanceof ItemArmor) {
         int slotProtection = ((ItemArmor)Minecraft.thePlayer.inventory.getStackInSlot(slot).getItem()).getArmorMaterial().getDamageReductionAmount(getItemType(Minecraft.thePlayer.inventory.getStackInSlot(slot))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{Minecraft.thePlayer.inventory.getStackInSlot(slot)}, DamageSource.generic);
         ItemArmor ia1;
         if(Minecraft.thePlayer.inventory.armorInventory[1] != null) {
            ItemArmor i = (ItemArmor)Minecraft.thePlayer.inventory.armorInventory[1].getItem();
            ItemStack otherProtection = Minecraft.thePlayer.inventory.armorInventory[1];
            ia1 = (ItemArmor)getStackInSlot(slot).getItem();
            int ia2 = ((ItemArmor)otherProtection.getItem()).getArmorMaterial().getDamageReductionAmount(getItemType(otherProtection)) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{otherProtection}, DamageSource.generic);
            if(ia2 > slotProtection || ia2 == slotProtection) {
               return false;
            }
         }

         for(int var7 = 0; var7 < Minecraft.thePlayer.inventory.getSizeInventory(); ++var7) {
            if(getStackInSlot(var7) != null && Minecraft.thePlayer.inventory.getStackInSlot(var7).getItem() instanceof ItemArmor) {
               int var8 = ((ItemArmor)Minecraft.thePlayer.inventory.getStackInSlot(var7).getItem()).getArmorMaterial().getDamageReductionAmount(getItemType(Minecraft.thePlayer.inventory.getStackInSlot(var7))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{Minecraft.thePlayer.inventory.getStackInSlot(var7)}, DamageSource.generic);
               ia1 = (ItemArmor)getStackInSlot(slot).getItem();
               ItemArmor var9 = (ItemArmor)getStackInSlot(var7).getItem();
               if(ia1.armorType == 2 && var9.armorType == 2 && var8 > slotProtection) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   public boolean isBestBoots(int slot) {
      if(getStackInSlot(slot) != null && getStackInSlot(slot).getItem() != null && getStackInSlot(slot).getItem() instanceof ItemArmor) {
         int slotProtection = ((ItemArmor)Minecraft.thePlayer.inventory.getStackInSlot(slot).getItem()).getArmorMaterial().getDamageReductionAmount(getItemType(Minecraft.thePlayer.inventory.getStackInSlot(slot))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{Minecraft.thePlayer.inventory.getStackInSlot(slot)}, DamageSource.generic);
         ItemArmor ia1;
         if(Minecraft.thePlayer.inventory.armorInventory[0] != null) {
            ItemArmor i = (ItemArmor)Minecraft.thePlayer.inventory.armorInventory[0].getItem();
            ItemStack otherProtection = Minecraft.thePlayer.inventory.armorInventory[0];
            ia1 = (ItemArmor)getStackInSlot(slot).getItem();
            int ia2 = ((ItemArmor)otherProtection.getItem()).getArmorMaterial().getDamageReductionAmount(getItemType(otherProtection)) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{otherProtection}, DamageSource.generic);
            if(ia2 > slotProtection || ia2 == slotProtection) {
               return false;
            }
         }

         for(int var7 = 0; var7 < Minecraft.thePlayer.inventory.getSizeInventory(); ++var7) {
            if(getStackInSlot(var7) != null && Minecraft.thePlayer.inventory.getStackInSlot(var7).getItem() instanceof ItemArmor) {
               int var8 = ((ItemArmor)Minecraft.thePlayer.inventory.getStackInSlot(var7).getItem()).getArmorMaterial().getDamageReductionAmount(getItemType(Minecraft.thePlayer.inventory.getStackInSlot(var7))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{Minecraft.thePlayer.inventory.getStackInSlot(var7)}, DamageSource.generic);
               ia1 = (ItemArmor)getStackInSlot(slot).getItem();
               ItemArmor var9 = (ItemArmor)getStackInSlot(var7).getItem();
               if(ia1.armorType == 3 && var9.armorType == 3 && var8 > slotProtection) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   public boolean isBestSword(int slotIn) {
      return this.getBestWeapon() == slotIn;
   }

   public static int getItemType(ItemStack itemStack) {
      if(itemStack.getItem() instanceof ItemArmor) {
         ItemArmor armor = (ItemArmor)itemStack.getItem();
         return armor.armorType;
      } else {
         return -1;
      }
   }

   public static float getItemDamage(ItemStack itemStack) {
      Multimap multimap = itemStack.getAttributeModifiers();
      Iterator iterator;
      if(!multimap.isEmpty() && (iterator = multimap.entries().iterator()).hasNext()) {
         Entry entry = (Entry)iterator.next();
         AttributeModifier attributeModifier = (AttributeModifier)entry.getValue();
         double damage = attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2?attributeModifier.getAmount():attributeModifier.getAmount() * 100.0D;
         return attributeModifier.getAmount() > 1.0D?1.0F + (float)damage:1.0F;
      } else {
         return 1.0F;
      }
   }

   public boolean hasItemMoreTimes(int slotIn) {
      boolean has = false;
      ArrayList stacks = new ArrayList();
      stacks.clear();

      for(int i = 0; i < Minecraft.thePlayer.inventory.getSizeInventory(); ++i) {
         if(!stacks.contains(getStackInSlot(i))) {
            stacks.add(getStackInSlot(i));
         } else if(getStackInSlot(i) == getStackInSlot(slotIn)) {
            return true;
         }
      }

      return false;
   }

   public int getBestWeaponInHotbar() {
      int originalSlot = Minecraft.thePlayer.inventory.currentItem;
      byte weaponSlot = -1;
      float weaponDamage = 1.0F;

      for(byte slot = 0; slot < 9; ++slot) {
         ItemStack itemStack = Minecraft.thePlayer.inventory.getStackInSlot(slot);
         if(itemStack != null) {
            float damage = getItemDamage(itemStack);
            if((damage += EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED)) > weaponDamage) {
               weaponDamage = damage;
               weaponSlot = slot;
            }
         }
      }

      if(weaponSlot != -1) {
         return weaponSlot;
      } else {
         return originalSlot;
      }
   }

   public int getBestWeapon() {
      int originalSlot = Minecraft.thePlayer.inventory.currentItem;
      byte weaponSlot = -1;
      float weaponDamage = 1.0F;

      for(byte slot = 0; slot < Minecraft.thePlayer.inventory.getSizeInventory(); ++slot) {
         ItemStack itemStack;
         if(getStackInSlot(slot) != null && (itemStack = getStackInSlot(slot)) != null && itemStack.getItem() != null && itemStack.getItem() instanceof ItemSword) {
            float damage = getItemDamage(itemStack);
            if((damage += EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED)) > weaponDamage) {
               weaponDamage = damage;
               weaponSlot = slot;
            }
         }
      }

      if(weaponSlot != -1) {
         return weaponSlot;
      } else {
         return originalSlot;
      }
   }

   public int getArmorProt(int i) {
      int armorprot = -1;
      if(getStackInSlot(i) != null && getStackInSlot(i).getItem() != null && getStackInSlot(i).getItem() instanceof ItemArmor) {
         armorprot = ((ItemArmor)Minecraft.thePlayer.inventory.getStackInSlot(i).getItem()).getArmorMaterial().getDamageReductionAmount(getItemType(Minecraft.thePlayer.inventory.getStackInSlot(i))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{Minecraft.thePlayer.inventory.getStackInSlot(i)}, DamageSource.generic);
      }

      return armorprot;
   }

   public static int getFirstItem(Item i1) {
      for(int i = 0; i < Minecraft.thePlayer.inventory.getSizeInventory(); ++i) {
         if(getStackInSlot(i) != null && getStackInSlot(i).getItem() != null && getStackInSlot(i).getItem() == i1) {
            return i;
         }
      }

      return -1;
   }

   public static boolean isBestSword(ItemStack itemSword, int slot) {
      if(itemSword != null && itemSword.getItem() instanceof ItemSword) {
         for(int i = 0; i < Minecraft.thePlayer.inventory.getSizeInventory(); ++i) {
            ItemStack iStack = Minecraft.thePlayer.inventory.getStackInSlot(i);
            if(iStack != null && iStack.getItem() instanceof ItemSword && getItemDamage(iStack) >= getItemDamage(itemSword) && slot != i) {
               return false;
            }
         }
      }

      return true;
   }
}
