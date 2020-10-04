package cn.Judgment.mod.mods.MOVEMENT;

import java.util.Arrays;
import java.util.List;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventPacket;
import cn.Judgment.events.EventPostMotion;
import cn.Judgment.events.EventPreMotion;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.util.PlaceInfo;
import cn.Judgment.util.PlaceRotation;
import cn.Judgment.util.PlayerUtil;
import cn.Judgment.util.Rotation;
import cn.Judgment.util.RotationUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;

public class Scaffold extends Mod {
   public static final List BLOCK_BLACKLIST;
   public static Value tower;
   private final Value autoBlockValue = new Value("Scaffold_Slient", Boolean.valueOf(true));
   private final Value swingValue = new Value("Scaffold_Swing", Boolean.valueOf(true));
   private final Value searchValue = new Value("Scaffold_Search", Boolean.valueOf(true));
   private final Value KeepRotationValue = new Value("Scaffold_KeepRotation", Boolean.valueOf(true));
   private final Value safeWalkValue = new Value("Scaffold_SafeWalk", Boolean.valueOf(true));
   private String[] directions = new String[]{"S", "SW", "W", "NW", "N", "NE", "E", "SE"};
   private Rotation lockRotation;
   private PlaceInfo targetPlace;
   private int slot;
   int curY = 0;

   static {
      BLOCK_BLACKLIST = Arrays.asList(new Block[]{Blocks.enchanting_table, Blocks.chest, Blocks.ender_chest, Blocks.trapped_chest, Blocks.anvil, Blocks.sand, Blocks.web, Blocks.torch, Blocks.crafting_table, Blocks.furnace, Blocks.waterlily, Blocks.dispenser, Blocks.stone_pressure_plate, Blocks.wooden_pressure_plate, Blocks.noteblock, Blocks.dropper, Blocks.tnt, Blocks.standing_banner, Blocks.wall_banner});
      tower = new Value("Scaffold_Tower", Boolean.valueOf(true));
   }

   public Scaffold() {
      super("Scaffold", Category.WORLD);
   }

   public void onEnable() {
      this.curY = 0;
      super.onEnable();
   }

   public void onDisable() {
      Minecraft var10000 = mc;
      if(Minecraft.thePlayer != null) {
         Minecraft var10001 = mc;
         if(this.slot != Minecraft.thePlayer.inventory.currentItem) {
            var10000 = mc;
            NetHandlerPlayClient var1 = Minecraft.getNetHandler();
            Minecraft var10003 = mc;
            var1.addToSendQueue(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem));
         }

         super.onDisable();
      }
   }

  

   @EventTarget
   public void onPacket(EventPacket event) {
      Minecraft var10000 = mc;
      if(Minecraft.thePlayer != null) {
         Packet packet = event.getPacket();
         if(packet instanceof C09PacketHeldItemChange) {
            C09PacketHeldItemChange packetHeldItemChange = (C09PacketHeldItemChange)packet;
            this.slot = packetHeldItemChange.getSlotId();
         }

      }
   }

   @EventTarget
   public void onMotion(EventPreMotion event) {
      if(((Boolean)this.autoBlockValue.getValueState()).booleanValue()) {
         if(findAutoBlockBlock() == -1) {
            return;
         }
      } else {
         Minecraft var10000 = mc;
         if(Minecraft.thePlayer.getHeldItem() == null) {
            return;
         }

         var10000 = mc;
         if(!(Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemBlock)) {
            return;
         }
      }

      this.findBlock(event);
      event.setPitch(90.0F);
   }

   @EventTarget
   public void onMotion(EventPostMotion event) {
      Minecraft var10000 = mc;
      double x = Minecraft.thePlayer.posX;
      var10000 = mc;
      double y = Minecraft.thePlayer.posY - 1.0D;
      var10000 = mc;
      double z = Minecraft.thePlayer.posZ;
      new BlockPos(x, y, z);
      if(((Boolean)tower.getValueState()).booleanValue() && mc.gameSettings.keyBindJump.isKeyDown()) {
         var10000 = mc;
         if(Minecraft.thePlayer.isMoving()) {
            Minecraft var10001;
            if(PlayerUtil.isOnGround(0.76D) && !PlayerUtil.isOnGround(0.75D)) {
               var10000 = mc;
               if(Minecraft.thePlayer.motionY > 0.23D) {
                  var10000 = mc;
                  if(Minecraft.thePlayer.motionY < 0.25D) {
                     var10000 = mc;
                     EntityPlayerSP var10 = Minecraft.thePlayer;
                     var10001 = mc;
                     double var9 = (double)Math.round(Minecraft.thePlayer.posY);
                     Minecraft var10002 = mc;
                     var10.motionY = var9 - Minecraft.thePlayer.posY;
                  }
               }
            }

            if(PlayerUtil.isOnGround(1.0E-4D)) {
               var10000 = mc;
               Minecraft.thePlayer.motionY = 0.42D;
               var10000 = mc;
               Minecraft.thePlayer.motionX *= 0.9D;
               var10000 = mc;
               Minecraft.thePlayer.motionZ *= 0.9D;
            } else {
               var10000 = mc;
               var10001 = mc;
               if(Minecraft.thePlayer.posY >= (double)Math.round(Minecraft.thePlayer.posY) - 1.0E-4D) {
                  var10000 = mc;
                  var10001 = mc;
                  if(Minecraft.thePlayer.posY <= (double)Math.round(Minecraft.thePlayer.posY) + 1.0E-4D) {
                     var10000 = mc;
                     Minecraft.thePlayer.motionY = 0.0D;
                  }
               }
            }
         } else {
            var10000 = mc;
            Minecraft.thePlayer.motionX = 0.0D;
            var10000 = mc;
            Minecraft.thePlayer.motionZ = 0.0D;
            var10000 = mc;
            Minecraft.thePlayer.jumpMovementFactor = 0.0F;
            BlockPos blockBelow = new BlockPos(x, y, z);
            var10000 = mc;
            if(Minecraft.theWorld.getBlockState(blockBelow).getBlock() == Blocks.air && this.targetPlace != null) {
               var10000 = mc;
               Minecraft.thePlayer.motionY = 0.4196D;
               var10000 = mc;
               Minecraft.thePlayer.motionX *= 0.75D;
               var10000 = mc;
               Minecraft.thePlayer.motionZ *= 0.75D;
            }
         }
      }

      this.place();
   }

   public static int getBlockCount() {
      int blockCount = 0;

      for(int i = 0; i < 45; ++i) {
         Minecraft var10000 = mc;
         if(Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
            var10000 = mc;
            ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if(is.getItem() instanceof ItemBlock && isValid(item)) {
               blockCount += is.stackSize;
            }
         }
      }

      return blockCount;
   }

   private static boolean isValid(Item item) {
      if(!(item instanceof ItemBlock)) {
         return false;
      } else {
         ItemBlock iBlock = (ItemBlock)item;
         Block block = iBlock.getBlock();
         return !BLOCK_BLACKLIST.contains(block);
      }
   }

   public static final Block getBlock(BlockPos blockPos) {
      Minecraft.getMinecraft();
      WorldClient var10000 = Minecraft.theWorld;
      Block var2;
      if(var10000 != null) {
         IBlockState var1 = var10000.getBlockState(blockPos);
         if(var1 != null) {
            var2 = var1.getBlock();
            return var2;
         }
      }

      var2 = null;
      return var2;
   }

   public static final Material getMaterial(BlockPos blockPos) {
      Block var10000 = getBlock(blockPos);
      return var10000 != null?var10000.getMaterial():null;
   }

   public static final boolean isReplaceable(BlockPos blockPos) {
      Material var10000 = getMaterial(blockPos);
      return var10000 != null?var10000.isReplaceable():false;
   }

   public static final IBlockState getState(BlockPos blockPos) {
      Minecraft.getMinecraft();
      IBlockState var10000 = Minecraft.theWorld.getBlockState(blockPos);
      return var10000;
   }

   public static final boolean canBeClicked(BlockPos blockPos) {
      Block var10000 = getBlock(blockPos);
      boolean var2;
      if(var10000 != null && var10000.canCollideCheck(getState(blockPos), false)) {
         Minecraft.getMinecraft();
         WorldClient var1 = Minecraft.theWorld;
         if(var1.getWorldBorder().contains(blockPos)) {
            var2 = true;
            return var2;
         }
      }

      var2 = false;
      return var2;
   }

   private void findBlock(EventPreMotion e) {
      Minecraft var10000 = mc;
      Minecraft var10001 = mc;
      BlockPos var5;
      Minecraft var10002;
      if(Minecraft.thePlayer.posY == (double)((int)Minecraft.thePlayer.posY) + 0.5D) {
         var10002 = mc;
         var5 = new BlockPos(Minecraft.thePlayer);
      } else {
         var10002 = mc;
         double var6 = Minecraft.thePlayer.posX;
         Minecraft var10003 = mc;
         double var7 = Minecraft.thePlayer.posY - (double)(mc.gameSettings.keyBindSneak.isKeyDown()?1:0);
         Minecraft var10004 = mc;
         var5 = (new BlockPos(var6, var7, Minecraft.thePlayer.posZ)).down();
      }

      BlockPos blockPosition = var5;
      if(isReplaceable(blockPosition) && !this.search(blockPosition, true, e)) {
         if(((Boolean)this.searchValue.getValueState()).booleanValue()) {
            for(int x = -1; x <= 1; ++x) {
               for(int z = -1; z <= 1; ++z) {
                  if(this.search(blockPosition.add(x, 0, z), true, e)) {
                     return;
                  }
               }
            }
         }

      }
   }

   private boolean search(BlockPos blockPosition, boolean checks, EventPreMotion e) {
      if(!isReplaceable(blockPosition)) {
         return false;
      } else {
         Minecraft var10002 = mc;
         double var35 = Minecraft.thePlayer.posX;
         Minecraft var10003 = mc;
         Minecraft var10004 = mc;
         double var36 = Minecraft.thePlayer.getEntityBoundingBox().minY + (double)Minecraft.thePlayer.getEyeHeight();
         var10004 = mc;
         Vec3 eyesPos = new Vec3(var35, var36, Minecraft.thePlayer.posZ);
         PlaceRotation placeRotation = null;
         EnumFacing[] var9;
         int var8 = (var9 = EnumFacing.values()).length;

         for(int var7 = 0; var7 < var8; ++var7) {
            EnumFacing side = var9[var7];
            BlockPos neighbor = blockPosition.offset(side);
            if(canBeClicked(neighbor)) {
               Vec3 dirVec = new Vec3(side.getDirectionVec());

               for(double xSearch = 0.1D; xSearch < 0.9D; xSearch += 0.1D) {
                  for(double ySearch = 0.1D; ySearch < 0.9D; ySearch += 0.1D) {
                     for(double zSearch = 0.1D; zSearch < 0.9D; zSearch += 0.1D) {
                        Vec3 posVec = (new Vec3(blockPosition)).addVector(xSearch, ySearch, zSearch);
                        double distanceSqPosVec = eyesPos.squareDistanceTo(posVec);
                        Vec3 hitVec = posVec.add(new Vec3(dirVec.xCoord * 0.5D, dirVec.yCoord * 0.5D, dirVec.zCoord * 0.5D));
                        Minecraft var10000;
                        if(checks) {
                           if(eyesPos.squareDistanceTo(hitVec) > 18.0D || distanceSqPosVec > eyesPos.squareDistanceTo(posVec.add(dirVec))) {
                              continue;
                           }

                           var10000 = mc;
                           if(Minecraft.theWorld.rayTraceBlocks(eyesPos, hitVec, false, true, false) != null) {
                              continue;
                           }
                        }

                        double diffX = hitVec.xCoord - eyesPos.xCoord;
                        double diffY = hitVec.yCoord - eyesPos.yCoord;
                        double diffZ = hitVec.zCoord - eyesPos.zCoord;
                        double diffXZ = (double)MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
                        Rotation rotation = new Rotation(MathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F), MathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)))));
                        Vec3 rotationVector = RotationUtil.getVectorForRotation(rotation);
                        Vec3 vector = eyesPos.addVector(rotationVector.xCoord * 4.0D, rotationVector.yCoord * 4.0D, rotationVector.zCoord * 4.0D);
                        var10000 = mc;
                        MovingObjectPosition obj = Minecraft.theWorld.rayTraceBlocks(eyesPos, vector, false, false, true);
                        if(obj.typeOfHit == MovingObjectType.BLOCK && obj.getBlockPos().equals(neighbor)) {
                           if(placeRotation != null) {
                              var10003 = mc;
                              var10004 = mc;
                              double var34 = RotationUtil.getRotationDifference(rotation, new Rotation(Minecraft.thePlayer.rotationYaw, Minecraft.thePlayer.rotationPitch));
                              Rotation var10001 = placeRotation.getRotation();
                              var10004 = mc;
                              Minecraft var10005 = mc;
                              if(var34 >= RotationUtil.getRotationDifference(var10001, new Rotation(Minecraft.thePlayer.rotationYaw, Minecraft.thePlayer.rotationPitch))) {
                                 continue;
                              }
                           }

                           placeRotation = new PlaceRotation(new PlaceInfo(neighbor, side.getOpposite(), hitVec), rotation);
                        }
                     }
                  }
               }
            }
         }

         if(placeRotation == null) {
            return false;
         } else {
            this.lockRotation = placeRotation.getRotation();
            this.targetPlace = placeRotation.getPlaceInfo();
            return true;
         }
      }
   }

   private void place() {
      if(this.targetPlace != null) {
         Minecraft var10000;
         ItemStack itemStack;
         label33: {
            boolean blockSlot = true;
            var10000 = mc;
            itemStack = Minecraft.thePlayer.getHeldItem();
            var10000 = mc;
            if(Minecraft.thePlayer.getHeldItem() != null) {
               var10000 = mc;
               if(Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemBlock) {
                  break label33;
               }
            }

            if(!((Boolean)this.autoBlockValue.getValueState()).booleanValue()) {
               return;
            }

            int blockSlot1 = findAutoBlockBlock();
            if(blockSlot1 == -1) {
               return;
            }

            var10000 = mc;
            Minecraft.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(blockSlot1 - 36));
            var10000 = mc;
            itemStack = Minecraft.thePlayer.inventoryContainer.getSlot(blockSlot1).getStack();
         }

         var10000 = mc;
         Minecraft var10001 = mc;
         Minecraft var10002 = mc;
         if(Minecraft.playerController.onPlayerRightClick(Minecraft.thePlayer, Minecraft.theWorld, itemStack, this.targetPlace.getBlockPos(), this.targetPlace.getEnumFacing(), this.targetPlace.getVec3())) {
            if(((Boolean)this.swingValue.getValueState()).booleanValue()) {
               var10000 = mc;
               Minecraft.thePlayer.swingItem();
            } else {
               var10000 = mc;
               Minecraft.getNetHandler().addToSendQueue(new C0APacketAnimation());
            }
         }

         this.targetPlace = null;
      }
   }

   public static int findAutoBlockBlock() {
      Minecraft var10000;
      int i;
      ItemStack itemStack;
      ItemBlock itemBlock;
      Block block;
      for(i = 36; i < 45; ++i) {
         var10000 = mc;
         itemStack = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
         if(itemStack != null && itemStack.getItem() instanceof ItemBlock) {
            itemBlock = (ItemBlock)itemStack.getItem();
            block = itemBlock.getBlock();
            if(itemStack.stackSize > 0 && block.isFullCube() && !BLOCK_BLACKLIST.contains(block)) {
               return i;
            }
         }
      }

      for(i = 36; i < 45; ++i) {
         var10000 = mc;
         itemStack = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
         if(itemStack != null && itemStack.getItem() instanceof ItemBlock) {
            itemBlock = (ItemBlock)itemStack.getItem();
            block = itemBlock.getBlock();
            if(itemStack.stackSize > 0 && !BLOCK_BLACKLIST.contains(block)) {
               return i;
            }
         }
      }

      return -1;
   }
}
