package cn.Judgment.mod.mods.COMBAT;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

import javax.vecmath.Vector3d;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Client;
import cn.Judgment.Value;
import cn.Judgment.events.EventAttack;
import cn.Judgment.events.EventPacket;
import cn.Judgment.events.EventPostMotion;
import cn.Judgment.events.EventPreMotion;
import cn.Judgment.events.EventRender;
import cn.Judgment.events.EventRender2D;
import cn.Judgment.events.EventWorldLoaded;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.ui.ClientNotification;
import cn.Judgment.mod.ModManager;
import cn.Judgment.mod.mods.WORLD.Teams;
import cn.Judgment.util.ClientUtil;
import cn.Judgment.util.PlayerUtil;
import cn.Judgment.util.RenderUtil;
import cn.Judgment.util.RotationUtil;
import cn.Judgment.util.fontRenderer.UnicodeFontRenderer;
import cn.Judgment.util.timeUtils.TimeHelper;
import cn.Judgment.util.utilities.angle.Angle;
import cn.Judgment.util.utilities.angle.AngleUtility;
import cn.Judgment.util.utilities.vector.impl.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

public class Killaura extends Mod {
   public static Value autoBlock = new Value("KillAura_AutoBlock", Boolean.valueOf(true));
   public Value priority = new Value("KillAura", "Priority", 1);
   public Value rotMode = new Value("KillAura", "RotationMode", 0);
   public Value hurttime = new Value("KillAura_HurtTime", Double.valueOf(10.0D), Double.valueOf(1.0D), Double.valueOf(10.0D), 1.0D);
   public Value mistake = new Value("KillAura_Mistakes", Double.valueOf(0.0D), Double.valueOf(0.0D), Double.valueOf(20.0D), 1.0D);
   public Value blockMode = new Value("KillAura", "BlockMode", 0);
   public static Value reach = new Value("KillAura_Range", Double.valueOf(4.2D), Double.valueOf(3.0D), Double.valueOf(6.0D), 0.1D);
   public Value blockReach = new Value("KillAura_BlockRange", Double.valueOf(0.5D), Double.valueOf(0.0D), Double.valueOf(3.0D), 0.1D);
   public Value cps = new Value("KillAura_CPS", Double.valueOf(10.0D), Double.valueOf(1.0D), Double.valueOf(20.0D), 1.0D);
   public Value turn = new Value("KillAura_TurnHeadSpeed", Double.valueOf(15.0D), Double.valueOf(5.0D), Double.valueOf(120.0D), 1.0D);
   public static Value switchsize = new Value("KillAura_MaxTargets", Double.valueOf(1.0D), Double.valueOf(1.0D), Double.valueOf(5.0D), 1.0D);
   public Value switchDelay = new Value("KillAura_SwitchDelay", Double.valueOf(50.0D), Double.valueOf(0.0D), Double.valueOf(2000.0D), 10.0D);
   public Value yawDiff = new Value("KillAura_YawDifference", Double.valueOf(15.0D), Double.valueOf(5.0D), Double.valueOf(90.0D), 1.0D);
   public Value throughblock = new Value("KillAura_ThroughBlock", Boolean.valueOf(true));
   public Value rotations = new Value("KillAura_HeadRotations", Boolean.valueOf(true));
   public Value autodisable = new Value("KillAura_AutoDisable", Boolean.valueOf(true));
   public Value attackPlayers = new Value("KillAura_Players", Boolean.valueOf(true));
   public Value attackAnimals = new Value("KillAura_Animals", Boolean.valueOf(false));
   public Value attackMobs = new Value("KillAura_Mobs", Boolean.valueOf(false));
   public Value invisible = new Value("KillAura_Invisibles", Boolean.valueOf(false));
   public Value targetHUD = new Value("KillAura_ShowTarget", Boolean.valueOf(true));
   public Value esp = new Value("KillAura_ESP", Boolean.valueOf(true));
   public static boolean isBlocking = false;
   public static ArrayList targets = new ArrayList();
   public Random random = new Random();
   public static ArrayList attacked = new ArrayList();
   public boolean needBlock = false;
   public boolean needUnBlock = false;
   public int index;
   public static EntityLivingBase target = null;
   public static EntityLivingBase needHitBot = null;
   public TimeHelper switchTimer = new TimeHelper();
   public TimeHelper attacktimer = new TimeHelper();
   public TimeHelper TickexistCharge = new TimeHelper();
   private AngleUtility angleUtility = new AngleUtility(110.0F, 120.0F, 30.0F, 40.0F);
   AxisAlignedBB axisAlignedBB;
   float shouldAddYaw;
   float[] lastRotation = new float[]{0.0F, 0.0F};
   private float rotationYawHead;
   private float[] lastRotations;
   boolean Crit = false;
   float curHealthX = 0.0F;
   float curAbsorptionAmountX = 0.0F;
   float curY;
   private float yaw;
   private float pitch;
   int attackSpeed;

   public Killaura() {
      super("KillAura", Category.COMBAT);
      this.curY = (float)(new ScaledResolution(mc)).getScaledHeight();
      this.priority.mode.add("Angle");
      this.priority.mode.add("Range");
      this.priority.mode.add("Fov");
      this.rotMode.mode.add("Hypixel");
      this.rotMode.mode.add("Hypixel-F");
      this.rotMode.mode.add("Hypixel-AA");
      this.rotMode.mode.add("Hypixel-FA");
      this.blockMode.addValue("Hypixel");
      this.blockMode.addValue("Old");
      System.out.println("KillAura");
      this.rotMode.mode.add("Smooth");
      attacked = new ArrayList();
   }
   @EventTarget
   public void onWorldLoaded(EventWorldLoaded e) {
	   if (((Boolean)this.autodisable.getValueState()).booleanValue()) {
		   ClientUtil.sendClientMessage("KillAura Disabled", ClientNotification.Type.INFO);
		    set(false);
	   }
   }
   @EventTarget
   public void targetHud(EventRender2D event) {
      ScaledResolution res = new ScaledResolution(mc);
      UnicodeFontRenderer font = Client.instance.fontManager.consolasbold20;
      if(((Boolean)this.targetHUD.getValueState()).booleanValue()) {
         float Y = (float)res.getScaledHeight();
         float X = (float)res.getScaledWidth() / 1.8F;
         String Text = "Do not have target";
         int TextLength = font.getStringWidth(Text);
         float MaxHealth = 0.0F;
         float Health = 0.0F;
         float AbsorptionAmount = 0.0F;
         if(target != null) {
            Y = (float)res.getScaledHeight() / 1.8F;
            Text = target.getName();
            TextLength = font.getStringWidth(Text);
            MaxHealth = (float)((int)target.getMaxHealth());
            Health = (float)((int)target.getHealth());
            AbsorptionAmount = (float)((int)target.getAbsorptionAmount());
         }

         if(this.curY > Y) {
            this.curY -= 2.0F;
            if(this.curY < Y) {
               ++this.curY;
            }
         }

         if(this.curY < Y) {
            this.curY += 2.0F;
            if(this.curY > Y) {
               --this.curY;
            }
         }

         if(this.curHealthX > Health) {
            this.curHealthX = (float)((double)this.curHealthX - 0.2D);
         }

         if(this.curHealthX < Health) {
            this.curHealthX = (float)((double)this.curHealthX + 0.2D);
         }

         if(this.curAbsorptionAmountX > AbsorptionAmount) {
            this.curAbsorptionAmountX = (float)((double)this.curAbsorptionAmountX - 0.2D);
         }

         if(this.curAbsorptionAmountX < AbsorptionAmount) {
            this.curAbsorptionAmountX = (float)((double)this.curAbsorptionAmountX + 0.2D);
         }

         byte nextX = 28;
         int var22;
         if((float)TextLength > (MaxHealth + AbsorptionAmount) * 5.0F) {
            if(TextLength > 160) {
               var22 = nextX + TextLength;
            } else {
               var22 = nextX + 160;
            }
         } else if((MaxHealth + AbsorptionAmount) * 5.0F > 160.0F) {
            var22 = (int)((float)nextX + (MaxHealth + AbsorptionAmount) * 5.0F);
         } else {
            var22 = nextX + 160;
         }

         RenderUtil.drawImage(new ResourceLocation("Client/Background/background.png"), (int)X, (int)this.curY, var22, 100);
         RenderUtil.drawImage(new ResourceLocation("Client/Killaura/Head.png"), (int)X + 4, (int)this.curY + 4, 20, 20);
         font.drawString(Text, X + 20.0F + 4.0F, this.curY + 7.0F, (new Color(255, 255, 255, 255)).getRGB());
         RenderUtil.drawImage(new ResourceLocation("Client/Killaura//Heart.png"), (int)X + 6, (int)this.curY + 6 + 20, 16, 16);
         int color = (new Color(250 - (int)(this.curHealthX / MaxHealth * 10.0F * 25.0F), 0 + (int)(this.curHealthX / MaxHealth * 10.0F * 25.0F), 0, 255)).getRGB();
         if(this.curHealthX > 0.5F) {
            RenderUtil.drawFastRoundedRect(X + 20.0F + 4.0F, this.curY + 20.0F + 9.0F, X + 20.0F + 4.0F + this.curHealthX * 5.0F, this.curY + 20.0F + 8.0F + 10.0F, 2.0F, color);
         } else if(Health < 0.5F && Health != 0.0F) {
            this.curHealthX = 0.0F;
         }

         if(this.curAbsorptionAmountX > 0.5F) {
            RenderUtil.drawRect(X + 20.0F + 4.0F + this.curHealthX * 5.0F, this.curY + 20.0F + 9.0F, X + 20.0F + 4.0F + this.curHealthX * 5.0F - 4.0F, this.curY + 20.0F + 8.0F + 10.0F, color);
            RenderUtil.drawRect(X + 20.0F + 4.0F + this.curHealthX * 5.0F, this.curY + 20.0F + 9.0F, X + 20.0F + 4.0F + this.curHealthX * 5.0F + 4.0F, this.curY + 20.0F + 8.0F + 10.0F, (new Color(255, 225, 100, 255)).getRGB());
            RenderUtil.drawFastRoundedRect(X + 20.0F + 4.0F + this.curHealthX * 5.0F, this.curY + 20.0F + 9.0F, X + 20.0F + 4.0F + (this.curHealthX + this.curAbsorptionAmountX) * 5.0F, this.curY + 20.0F + 8.0F + 10.0F, 2.0F, (new Color(255, 225, 100, 255)).getRGB());
         } else if(AbsorptionAmount < 0.5F && AbsorptionAmount != 0.0F) {
            this.curAbsorptionAmountX = 0.0F;
         }

         RenderUtil.drawRect((float)((int)X + 6), (float)((int)this.curY + 4 + 20 + 20 + 3), (float)((int)X + var22 - 6), (float)((int)this.curY + 4 + 20 + 20 + 4), (new Color(255, 255, 255, 160)).getRGB());
         RenderUtil.drawImage(new ResourceLocation("Client/Killaura/Half_Heart.png"), (int)X + 6, (int)this.curY + 6 + 20 + 20 + 7, 16, 16);
         Minecraft var10000;
         String var24;
         if(this.Crit) {
            var24 = "Criticals";
         } else {
            label340: {
               var10000 = mc;
               if(!Minecraft.thePlayer.onGround) {
                  var10000 = mc;
                  if(Minecraft.thePlayer.fallDistance > 0.0F) {
                     var24 = "Criticals";
                     break label340;
                  }
               }

               var24 = "Normal";
            }
         }

         Text = var24;
         font.getStringWidth(Text);
         if(Text == "Normal") {
            color = Color.GREEN.getRGB();
         } else {
            color = Color.ORANGE.getRGB();
         }

         font.drawString(Text, (int)X + 6 + 20, (int)this.curY + 6 + 20 + 20 + 7, color);
         RenderUtil.drawImage(new ResourceLocation("Client/Killaura/Sword.png"), (int)X + 4, (int)this.curY + 4 + 20 + 20 + 20 + 7, 20, 20);
         color = Color.GREEN.getRGB();
         String LoseOrWin = "Finding Player...";
         int SelfScore = 0;
         int TargetScore = 0;
         ItemStack armourStack;
         int var18;
         int var19;
         ItemStack[] var20;
         ItemStack renderStack1;
         if(target instanceof EntityPlayer) {
            EntityPlayer entity = (EntityPlayer)target;
            var20 = entity.inventory.armorInventory;
            var19 = entity.inventory.armorInventory.length;

            for(var18 = 0; var18 < var19; ++var18) {
               armourStack = var20[var18];
               if(armourStack != null) {
                  renderStack1 = armourStack.copy();
                  if(Item.getIdFromItem(renderStack1.getItem()) == 298) {
                     ++TargetScore;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 299) {
                     ++TargetScore;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 300) {
                     ++TargetScore;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 301) {
                     ++TargetScore;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 302) {
                     TargetScore += 3;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 303) {
                     TargetScore += 3;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 304) {
                     TargetScore += 3;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 305) {
                     TargetScore += 3;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 306) {
                     TargetScore += 4;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 307) {
                     TargetScore += 4;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 308) {
                     TargetScore += 4;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 309) {
                     TargetScore += 4;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 310) {
                     TargetScore += 5;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 311) {
                     TargetScore += 5;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 312) {
                     TargetScore += 5;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 313) {
                     TargetScore += 5;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 314) {
                     TargetScore += 2;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 315) {
                     TargetScore += 2;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 316) {
                     TargetScore += 2;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 317) {
                     TargetScore += 2;
                  }
               }
            }

            if(entity.getHeldItem() != null) {
               TargetScore += entity.getHeldItem().getMaxDamage();
            }

            TargetScore = (int)((float)TargetScore + entity.getHealth());
            TargetScore += entity.isBlocking()?1:0;
            TargetScore += !entity.onGround && entity.fallDistance > 0.0F?1:0;
         }

         if(target instanceof EntityPlayer) {
            var10000 = mc;
            EntityPlayerSP var23 = Minecraft.thePlayer;
            var20 = var23.inventory.armorInventory;
            var19 = var23.inventory.armorInventory.length;

            for(var18 = 0; var18 < var19; ++var18) {
               armourStack = var20[var18];
               if(armourStack != null) {
                  renderStack1 = armourStack.copy();
                  if(Item.getIdFromItem(renderStack1.getItem()) == 298) {
                     ++SelfScore;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 299) {
                     ++SelfScore;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 300) {
                     ++SelfScore;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 301) {
                     ++SelfScore;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 302) {
                     SelfScore += 3;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 303) {
                     SelfScore += 3;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 304) {
                     SelfScore += 3;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 305) {
                     SelfScore += 3;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 306) {
                     SelfScore += 4;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 307) {
                     SelfScore += 4;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 308) {
                     SelfScore += 4;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 309) {
                     SelfScore += 4;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 310) {
                     SelfScore += 5;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 311) {
                     SelfScore += 5;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 312) {
                     SelfScore += 5;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 313) {
                     SelfScore += 5;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 314) {
                     SelfScore += 2;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 315) {
                     SelfScore += 2;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 316) {
                     SelfScore += 2;
                  }

                  if(Item.getIdFromItem(renderStack1.getItem()) == 317) {
                     SelfScore += 2;
                  }
               }
            }

            if(var23.getHeldItem() != null) {
               SelfScore += var23.getHeldItem().getMaxDamage();
            }

            SelfScore = (int)((float)SelfScore + var23.getHealth());
            SelfScore += var23.isBlocking()?1:0;
            SelfScore += !var23.onGround && var23.fallDistance > 0.0F?1:0;
         }

         if(SelfScore > TargetScore) {
            color = Color.GREEN.getRGB();
            LoseOrWin = "I think you will win.";
         }

         if(SelfScore < TargetScore) {
            LoseOrWin = "I think you will lose.";
            color = Color.RED.getRGB();
         }

         if(SelfScore - TargetScore < 2 && SelfScore - TargetScore > -2) {
            LoseOrWin = "WTF? I DONT THINK ANY.";
            color = Color.YELLOW.getRGB();
         }

         font.getStringWidth(LoseOrWin);
         font.drawString(LoseOrWin, (int)X + 6 + 20, (int)this.curY + 6 + 20 + 20 + 20 + 10, color);
      }

   }

   @EventTarget
   public void onRender(EventRender render) {
      if(target != null && ((Boolean)this.esp.getValueState()).booleanValue()) {
         mc.getRenderManager();
         double x = target.lastTickPosX + (target.posX - target.lastTickPosX) * (double)mc.timer.renderPartialTicks - RenderManager.renderPosX;
         mc.getRenderManager();
         double y = target.lastTickPosY + (target.posY - target.lastTickPosY) * (double)mc.timer.renderPartialTicks - RenderManager.renderPosY;
         mc.getRenderManager();
         double z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * (double)mc.timer.renderPartialTicks - RenderManager.renderPosZ;
         double width = target.getEntityBoundingBox().maxX - target.getEntityBoundingBox().minX;
         double height = target.getEntityBoundingBox().maxY - target.getEntityBoundingBox().minY + 0.25D;
         RenderUtil.drawEntityESP(x, y, z, width, height, target.hurtTime > 1?1.0F:0.0F, target.hurtTime > 1?0.0F:1.0F, 0.0F, 0.2F, target.hurtTime > 1?1.0F:0.0F, target.hurtTime > 1?0.0F:1.0F, 0.0F, 1.0F, 2.0F);
      }
   }

   public static double getRandomDoubleInRange(double minDouble, double maxDouble) {
      return minDouble >= maxDouble?minDouble:(new Random()).nextDouble() * (maxDouble - minDouble) + minDouble;
   }

   @EventTarget
   public void onPre(EventPreMotion event) {
      Minecraft var10001 = mc;
      this.rotationYawHead = Minecraft.thePlayer.rotationYawHead;
      needHitBot = null;
      if(!targets.isEmpty() && this.index >= targets.size()) {
         this.index = 0;
      }

      Iterator var4 = targets.iterator();

      while(var4.hasNext()) {
         EntityLivingBase diff = (EntityLivingBase)var4.next();
         if(!this.isValidEntity(diff)) {
            targets.remove(diff);
         }
      }

      this.getTarget(event);
      Minecraft var10000;
      if(targets.size() == 0) {
         target = null;
         this.attackSpeed = 0;
      } else {
         target = (EntityLivingBase)targets.get(this.index);
         this.axisAlignedBB = null;
         var10000 = mc;
         if((double)Minecraft.thePlayer.getDistanceToEntity(target) > ((Double)reach.getValueState()).doubleValue()) {
            target = (EntityLivingBase)targets.get(0);
         }
      }

      if(ModManager.getModByName("Scaffold").isEnabled()) {
         target = null;
      } else {
         Minecraft var10002;
         if(target != null) {
            if(target.hurtTime == 10 && this.switchTimer.isDelayComplete(((Double)this.switchDelay.getValueState()).doubleValue()) && targets.size() > 1) {
               this.switchTimer.reset();
               ++this.index;
            }

            float var11 = Math.abs(Math.abs(MathHelper.wrapAngleTo180_float(this.rotationYawHead)) - Math.abs(MathHelper.wrapAngleTo180_float(RotationUtil.getRotations(target)[0])));
            if(((Boolean)this.rotations.getValueState()).booleanValue()) {
               float[] var12;
               if(this.rotMode.isCurrentMode("Hypixel-AA")) {
                  var12 = getEntityRotations(target, this.lastRotations, false, ((Double)this.turn.getValueState()).intValue());
                  this.lastRotations = new float[]{var12[0], var12[1]};
                  event.setYaw(var12[0]);
                  var10000 = mc;
                  Minecraft.thePlayer.renderYawOffset = event.getYaw();
                  var10000 = mc;
                  Minecraft.thePlayer.renderArmPitch = event.pitch;
                  event.setPitch(var12[1]);
                  this.rotationYawHead = event.getYaw();
               } else if(this.rotMode.isCurrentMode("Smooth")) {
                  var10001 = mc;
                  double var18;
                  if(Math.abs(target.posY - Minecraft.thePlayer.posY) > 1.8D) {
                     var10001 = mc;
                     var18 = Math.abs(target.posY - Minecraft.thePlayer.posY);
                     var10002 = mc;
                     var18 = var18 / Math.abs(target.posY - Minecraft.thePlayer.posY) / 2.0D;
                  } else {
                     var10001 = mc;
                     var18 = Math.abs(target.posY - Minecraft.thePlayer.posY);
                  }

                  double var13 = var18;
                  Vector3 enemyCoords = new Vector3(Double.valueOf(target.getEntityBoundingBox().minX + (target.getEntityBoundingBox().maxX - target.getEntityBoundingBox().minX) / 2.0D), Double.valueOf((!(target instanceof EntityPig) && !(target instanceof EntitySpider)?target.posY:target.getEntityBoundingBox().minY - (double)target.getEyeHeight() * 1.2D) - var13), Double.valueOf(target.getEntityBoundingBox().minZ + (target.getEntityBoundingBox().maxZ - target.getEntityBoundingBox().minZ) / 2.0D));
                  var10002 = mc;
                  Minecraft var10003 = mc;
                  Minecraft var10004 = mc;
                  Double var15 = Double.valueOf(Minecraft.thePlayer.getEntityBoundingBox().minX + (Minecraft.thePlayer.getEntityBoundingBox().maxX - Minecraft.thePlayer.getEntityBoundingBox().minX) / 2.0D);
                  var10003 = mc;
                  Double var17 = Double.valueOf(Minecraft.thePlayer.posY);
                  var10004 = mc;
                  Minecraft var10005 = mc;
                  Minecraft var10006 = mc;
                  Vector3 myCoords = new Vector3(var15, var17, Double.valueOf(Minecraft.thePlayer.getEntityBoundingBox().minZ + (Minecraft.thePlayer.getEntityBoundingBox().maxZ - Minecraft.thePlayer.getEntityBoundingBox().minZ) / 2.0D));
                  Angle srcAngle = new Angle(Float.valueOf(this.lastRotation[0]), Float.valueOf(this.lastRotation[1]));
                  Angle dstAngle = AngleUtility.calculateAngle(enemyCoords, myCoords);
                  Angle smoothedAngle = AngleUtility.smoothAngle(dstAngle, srcAngle, ((Double)this.turn.getValueState()).floatValue() * 8.0F, ((Double)this.turn.getValueState()).floatValue() * 7.5F);
                  new Random();
                  event.setYaw(smoothedAngle.getYaw().floatValue() + (float)this.randomNumber(-2, 2));
                  event.setPitch(smoothedAngle.getPitch().floatValue() + (float)this.randomNumber(-3, 3));
                  this.lastRotation[0] = event.getYaw();
                  var10000 = mc;
                  Minecraft.thePlayer.renderYawOffset = event.getYaw();
                  var10000 = mc;
                  Minecraft.thePlayer.renderArmPitch = event.pitch;
                  this.lastRotation[1] = event.getPitch();
                  this.rotationYawHead = event.getYaw();
               } else if(this.rotMode.isCurrentMode("Hypixel-FA")) {
                  var12 = RotationUtil.getRotations(target);
                  Random var14 = new Random();
                  event.setYaw(var12[0] + (float)var14.nextInt(10) - 5.0F);
                  event.setPitch(var12[1] + (float)var14.nextInt(3) - 2.0F);
                  this.rotationYawHead = event.getYaw();
               } else {
                  float[] facing;
                  if(((Boolean)this.rotations.getValueState()).booleanValue() && this.rotMode.isCurrentMode("Hypixel-F")) {
                     var10000 = mc;
                     var4 = Minecraft.theWorld.loadedEntityList.iterator();
                     facing = getAnglesIgnoringNull(target, this.yaw, this.pitch);
                     this.lastRotations[0] = facing[0];
                     this.lastRotations[1] = facing[1];
                     event.setYaw(facing[0]);
                     var10000 = mc;
                     Minecraft.thePlayer.rotationYawHead = event.getYaw();
                     var10000 = mc;
                     Minecraft.thePlayer.renderYawOffset = event.getYaw();
                     var10000 = mc;
                     Minecraft.thePlayer.renderArmPitch = event.pitch;
                     var10000 = mc;
                     Minecraft.thePlayer.renderYawOffset = event.getYaw();
                     event.setPitch(facing[1]);
                     this.rotationYawHead = event.getYaw();
                  } else if(((Boolean)this.rotations.getValueState()).booleanValue() && this.rotMode.isCurrentMode("Hypixel")) {
                     var10000 = mc;
                     var4 = Minecraft.theWorld.loadedEntityList.iterator();
                     facing = RotUtil.getRotations(target);
                     this.lastRotations[0] = facing[0];
                     this.lastRotations[1] = facing[1];
                     event.setYaw(facing[0]);
                     var10000 = mc;
                     Minecraft.thePlayer.rotationYawHead = event.getYaw();
                     var10000 = mc;
                     Minecraft.thePlayer.renderYawOffset = event.getYaw();
                     var10000 = mc;
                     Minecraft.thePlayer.renderArmPitch = event.pitch;
                     var10000 = mc;
                     Minecraft.thePlayer.renderYawOffset = event.getYaw();
                     event.setPitch(facing[1]);
                     this.rotationYawHead = event.getYaw();
                  } else if(((Boolean)this.rotations.getValueState()).booleanValue() && this.rotMode.isCurrentMode("Hypixel-F")) {
                     var10000 = mc;
                     var4 = Minecraft.theWorld.loadedEntityList.iterator();
                     facing = RotUtil.getRotationNeededHypixelBetter(target);
                     this.lastRotations[0] = facing[0];
                     this.lastRotations[1] = facing[1];
                     event.setYaw(facing[0]);
                     var10000 = mc;
                     Minecraft.thePlayer.rotationYawHead = event.getYaw();
                     var10000 = mc;
                     Minecraft.thePlayer.renderYawOffset = event.getYaw();
                     var10000 = mc;
                     Minecraft.thePlayer.renderArmPitch = event.pitch;
                     var10000 = mc;
                     Minecraft.thePlayer.renderYawOffset = event.getYaw();
                     event.setPitch(facing[1]);
                     this.rotationYawHead = event.getYaw();
                  }
               }
            }

            var10000 = mc;
            if(!Minecraft.thePlayer.isBlocking()) {
               var10000 = mc;
               if(Minecraft.thePlayer.getHeldItem() == null) {
                  return;
               }

               var10000 = mc;
               if(!(Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemSword) || !((Boolean)autoBlock.getValueState()).booleanValue() || !isBlocking || !this.blockMode.isCurrentMode("Hypixel")) {
                  return;
               }
            }

            boolean var16;
            label86: {
               var10001 = mc;
               if(!Minecraft.thePlayer.isBlocking() && !((Boolean)autoBlock.getValueState()).booleanValue()) {
                  var10001 = mc;
                  if(Minecraft.thePlayer.getItemInUseCount() > 0) {
                     var16 = true;
                     break label86;
                  }
               }

               var16 = false;
            }

            this.unBlock(var16);
         } else {
            var10002 = mc;
            this.lastRotation[0] = Minecraft.thePlayer.rotationYaw;
            targets.clear();
            var10000 = mc;
            if(Minecraft.thePlayer.getHeldItem() != null) {
               var10000 = mc;
               if(Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemSword && ((Boolean)autoBlock.getValueState()).booleanValue() && isBlocking) {
                  this.unBlock(true);
               }
            }
         }

      }
   }

   private void doBlock(boolean setItemUseInCount) {
      Minecraft var10000;
      if(setItemUseInCount) {
         var10000 = mc;
         Minecraft var10001 = mc;
         Minecraft.thePlayer.itemInUseCount = Minecraft.thePlayer.getHeldItem().getMaxItemUseDuration();
      }

      var10000 = mc;
      NetworkManager var2 = Minecraft.thePlayer.sendQueue.getNetworkManager();
      BlockPos var10003 = this.blockMode.isCurrentMode("Hypixel")?new BlockPos(-1, -1, -1):BlockPos.ORIGIN;
      Minecraft var10005 = mc;
      var2.sendPacket(new C08PacketPlayerBlockPlacement(var10003, 255, Minecraft.thePlayer.getHeldItem(), 0.0F, 0.0F, 0.0F));
      isBlocking = true;
   }

   private void unBlock(boolean setItemUseInCount) {
      Minecraft var10000;
      if(setItemUseInCount) {
         var10000 = mc;
         Minecraft.thePlayer.itemInUseCount = 0;
      }

      var10000 = mc;
      Minecraft.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, this.blockMode.isCurrentMode("Hypixel")?new BlockPos(-1, -1, -1):BlockPos.ORIGIN, EnumFacing.DOWN));
      isBlocking = false;
   }

   public static float[] getEntityRotations(EntityLivingBase target, float[] lastrotation, boolean aac, int smooth) {
      myAngleUtility angleUtility = new myAngleUtility(aac, (float)smooth);
      Vector3d enemyCoords = new Vector3d(target.posX, target.posY + (double)target.getEyeHeight(), target.posZ);
      Minecraft var10002 = mc;
      double var13 = Minecraft.thePlayer.posX;
      Minecraft var10003 = mc;
      Minecraft var10004 = mc;
      double var14 = Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight();
      var10004 = mc;
      Vector3d myCoords = new Vector3d(var13, var14, Minecraft.thePlayer.posZ);
      myAngle dstAngle = angleUtility.calculateAngle(enemyCoords, myCoords);
      myAngle srcAngle = new myAngle(lastrotation[0], lastrotation[1]);
      myAngle smoothedAngle = angleUtility.smoothAngle(dstAngle, srcAngle);
      float yaw = smoothedAngle.getYaw();
      float pitch = smoothedAngle.getPitch();
      Minecraft var10001 = mc;
      float yaw2 = MathHelper.wrapAngleTo180_float(yaw - Minecraft.thePlayer.rotationYaw);
      Minecraft var10000 = mc;
      yaw = Minecraft.thePlayer.rotationYaw + yaw2;
      return new float[]{yaw, pitch};
   }

   private int randomNumber(int max, int min) {
      return (int)(Math.random() * (double)(max - min)) + min;
   }

   private void doAttack() {
      int aps = ((Double)this.cps.getValueState()).intValue();
      int delayValue = 1000 / aps + this.random.nextInt(50) - 30;
      if(this.attacktimer.isDelayComplete((long)(delayValue - 20 + this.random.nextInt(50)))) {
         boolean miss = false;
         Minecraft var10000 = mc;
         boolean isInRange = (double)Minecraft.thePlayer.getDistanceToEntity(target) <= ((Double)reach.getValueState()).doubleValue();
         if(isInRange) {
            this.attacktimer.reset();
            if((double)target.hurtTime > ((Double)this.hurttime.getValueState()).doubleValue() || this.random.nextInt(100) < ((Double)this.mistake.getValueState()).intValue()) {
               miss = true;
            }

            float diff = Math.abs(Math.abs(MathHelper.wrapAngleTo180_float(this.rotationYawHead)) - Math.abs(MathHelper.wrapAngleTo180_float(RotationUtil.getRotations(target)[0])));
            if((double)diff > ((Double)this.yawDiff.getValueState()).doubleValue() && !ModManager.getModByName("Scaffold").isEnabled()) {
               miss = true;
            }
         }

         label61: {
            var10000 = mc;
            if(!Minecraft.thePlayer.isBlocking()) {
               var10000 = mc;
               if(Minecraft.thePlayer.getHeldItem() == null) {
                  break label61;
               }

               var10000 = mc;
               if(!(Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemSword) || !((Boolean)autoBlock.getValueState()).booleanValue()) {
                  break label61;
               }
            }

            boolean var6;
            label43: {
               Minecraft var10001 = mc;
               if(!Minecraft.thePlayer.isBlocking() && !((Boolean)autoBlock.getValueState()).booleanValue()) {
                  var10001 = mc;
                  if(Minecraft.thePlayer.getItemInUseCount() > 0) {
                     var6 = true;
                     break label43;
                  }
               }

               var6 = false;
            }

            this.unBlock(var6);
         }

         if(isInRange) {
            this.attack(miss);
         }
      }

   }

   @EventTarget
   public void onPost(EventPostMotion event) {
      if(target != null) {
         this.doAttack();
      }

      if(target != null) {
         label22: {
            Minecraft var10000 = mc;
            if(Minecraft.thePlayer.getHeldItem() != null) {
               var10000 = mc;
               if(Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemSword && ((Boolean)autoBlock.getValueState()).booleanValue()) {
                  break label22;
               }
            }

            var10000 = mc;
            if(!Minecraft.thePlayer.isBlocking()) {
               return;
            }
         }

         if(!isBlocking) {
            this.doBlock(true);
         }
      }

   }

   @EventTarget
   public void onBlockPacket(EventPacket e) {
   }

   private void attack(boolean mistake) {
      this.Crit = false;
      Minecraft var10000;
      if(!mistake) {
         this.needBlock = true;
         ArrayList list = new ArrayList();
         var10000 = mc;
         Iterator var4 = Minecraft.theWorld.loadedEntityList.iterator();

         while(true) {
            Entity Crit;
            do {
               float diff;
               do {
                  do {
                     if(!var4.hasNext()) {
                        if(list.size() == 0) {
                           list.add(target);
                        }

                        needHitBot = (EntityLivingBase)list.get(this.random.nextInt(list.size()));
                        EventManager.call(new EventAttack(target));
                        Criticals Crit1 = (Criticals)ModManager.getModByName("Criticals");
                        if(Crit1.autoCrit(target)) {
                           PlayerUtil.tellPlayer("Do Criticals");
                           this.Crit = true;
                           this.attackSpeed = 0;
                        }

                        ++this.attackSpeed;
                        var10000 = mc;
                        Minecraft.thePlayer.swingItem();
                        var10000 = mc;
                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, net.minecraft.network.play.client.C02PacketUseEntity.Action.ATTACK));
                        if(!attacked.contains(target) && target instanceof EntityPlayer) {
                           attacked.add(target);
                        }

                        needHitBot = null;
                        return;
                     }

                     Crit = (Entity)var4.next();
                     diff = Math.abs(Math.abs(MathHelper.wrapAngleTo180_float(this.rotationYawHead)) - Math.abs(MathHelper.wrapAngleTo180_float(RotationUtil.getRotations(Crit)[0])));
                  } while(!(Crit instanceof EntityZombie));
               } while(!Crit.isInvisible());

               if((double)diff < ((Double)this.yawDiff.getValueState()).doubleValue()) {
                  break;
               }

               var10000 = mc;
            } while(Minecraft.thePlayer.getDistanceToEntity(target) >= 1.0F);

            var10000 = mc;
            if((double)Minecraft.thePlayer.getDistanceToEntity(Crit) < ((Double)reach.getValueState()).doubleValue()) {
               Minecraft var10001 = mc;
               if(Crit != Minecraft.thePlayer) {
                  list.add((EntityLivingBase)Crit);
               }
            }
         }
      } else {
         var10000 = mc;
         Minecraft.thePlayer.swingItem();
      }
   }

   private void getTarget(EventPreMotion event) {
      int maxSize = ((Double)switchsize.getValueState()).intValue();
      if(maxSize > 1) {
         this.setDisplayName("Switch");
      } else {
         this.setDisplayName("Single");
      }

      Minecraft var10000 = mc;
      Iterator var4 = Minecraft.theWorld.loadedEntityList.iterator();

      while(var4.hasNext()) {
         Entity o3 = (Entity)var4.next();
         EntityLivingBase curEnt;
         if(o3 instanceof EntityLivingBase && this.isValidEntity(curEnt = (EntityLivingBase)o3) && !targets.contains(curEnt)) {
            targets.add(curEnt);
         }

         if(targets.size() >= maxSize) {
            break;
         }
      }

      if (this.priority.isCurrentMode("Range")) {
          targets.sort((o1, o2) -> (int)(((Entity) o1).getDistanceToEntity((Entity)Minecraft.thePlayer) - ((Entity) o2).getDistanceToEntity((Entity)Minecraft.thePlayer)));
      }

      if (this.priority.isCurrentMode("Fov")) {
          targets.sort(Comparator.comparingDouble(o -> RotationUtil.getDistanceBetweenAngles((float)Minecraft.thePlayer.rotationPitch, (float)RotationUtil.getRotations((Entity)o)[0])));
      }

      if (this.priority.isCurrentMode("Angle")) {
          targets.sort((o1, o2) -> {
              float[] rot1 = RotationUtil.getRotations((Entity)o1);
              float[] rot2 = RotationUtil.getRotations((Entity)o2);
              return (int)(Minecraft.thePlayer.rotationYaw - rot1[0] - (Minecraft.thePlayer.rotationYaw - rot2[0]));
          });
      }

   }

   @EventTarget
   private void onPacket(EventPacket e) {
      if(e.getPacket() instanceof S08PacketPlayerPosLook) {
         S08PacketPlayerPosLook look = (S08PacketPlayerPosLook)e.getPacket();
         Minecraft var10001 = mc;
         look.yaw = Minecraft.thePlayer.rotationYaw;
         var10001 = mc;
         look.pitch = Minecraft.thePlayer.rotationPitch;
      }

   }

   private boolean isValidEntity(Entity entity) {
      if(entity != null && entity instanceof EntityLivingBase) {
         if(entity.isDead || ((EntityLivingBase)entity).getHealth() <= 0.0F) {
            return false;
         }

         Minecraft var10000 = mc;
         if((double)Minecraft.thePlayer.getDistanceToEntity(entity) < ((Double)reach.getValueState()).doubleValue() + ((Double)this.blockReach.getValueState()).doubleValue()) {
            Minecraft var10001 = mc;
            if(entity != Minecraft.thePlayer) {
               var10000 = mc;
               if(!Minecraft.thePlayer.isDead && !(entity instanceof EntityArmorStand) && !(entity instanceof EntitySnowman)) {
                  if(entity instanceof EntityPlayer && ((Boolean)this.attackPlayers.getValueState()).booleanValue()) {
                     if(entity.ticksExisted < 30) {
                        return false;
                     }

                     var10000 = mc;
                     if(!Minecraft.thePlayer.canEntityBeSeen(entity) && !((Boolean)this.throughblock.getValueState()).booleanValue()) {
                        return false;
                     }

                     if(entity.isInvisible() && !((Boolean)this.invisible.getValueState()).booleanValue()) {
                        return false;
                     }

                     if(!AntiBot.isBot(entity) && !Teams.isOnSameTeam(entity)) {
                        return true;
                     }

                     return false;
                  }

                  if(entity instanceof EntityMob && ((Boolean)this.attackMobs.getValueState()).booleanValue()) {
                     return !AntiBot.isBot(entity);
                  }

                  if((entity instanceof EntityAnimal || entity instanceof EntityVillager) && ((Boolean)this.attackAnimals.getValueState()).booleanValue()) {
                     return !AntiBot.isBot(entity);
                  }
               }
            }
         }
      }

      return false;
   }

   public void onEnable() {
      this.curY = (float)(new ScaledResolution(mc)).getScaledHeight();
      this.Crit = false;
      this.shouldAddYaw = 0.0F;
      attacked = new ArrayList();
      this.axisAlignedBB = null;
      Minecraft var10000 = mc;
      if(Minecraft.thePlayer != null) {
         Minecraft var10002 = mc;
         this.lastRotation[0] = Minecraft.thePlayer.rotationYaw;
         float[] var10001 = new float[2];
         Minecraft var10004 = mc;
         var10001[0] = Minecraft.thePlayer.rotationYaw;
         var10004 = mc;
         var10001[1] = Minecraft.thePlayer.rotationPitch;
         this.lastRotations = var10001;
      }

      this.index = 0;
      super.onEnable();
   }

   public void onDisable() {
      this.curY = (float)(new ScaledResolution(mc)).getScaledHeight();
      this.Crit = false;
      this.axisAlignedBB = null;
      Minecraft var10000 = mc;
      if(Minecraft.thePlayer != null) {
         Minecraft var10002 = mc;
         this.lastRotation[0] = Minecraft.thePlayer.rotationYaw;
      }

      targets.clear();
      target = null;
      this.unBlock(true);
      super.onDisable();
   }

   public static double isInFov(float var0, float var1, double var2, double var4, double var6) {
      Vec3 var8 = new Vec3((double)var0, (double)var1, 0.0D);
      Minecraft var10002 = mc;
      Minecraft var10003 = mc;
      Minecraft var10004 = mc;
      float[] var9 = getAngleBetweenVecs(new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ), new Vec3(var2, var4, var6));
      double var10 = MathHelper.wrapAngleTo180_double(var8.xCoord - (double)var9[0]);
      return Math.abs(var10) * 2.0D;
   }

   public static float[] getAngleBetweenVecs(Vec3 var0, Vec3 var1) {
      double var2 = var1.xCoord - var0.xCoord;
      double var4 = var1.yCoord - var0.yCoord;
      double var6 = var1.zCoord - var0.zCoord;
      double var8 = Math.sqrt(var2 * var2 + var6 * var6);
      float var10 = (float)(Math.atan2(var6, var2) * 180.0D / 3.141592653589793D) - 90.0F;
      float var11 = (float)(-(Math.atan2(var4, var8) * 180.0D / 3.141592653589793D));
      return new float[]{var10, var11};
   }

   public static float[] getAnglesIgnoringNull(Entity var0, float var1, float var2) {
      float[] var3 = getAngles(var0);
      if(var3 == null) {
         return new float[]{0.0F, 0.0F};
      } else {
         float var4 = var3[0];
         float var5 = var3[1];
         return new float[]{var1 + MathHelper.wrapAngleTo180_float(var4 - var1), var2 + MathHelper.wrapAngleTo180_float(var5 - var2) + 5.0F};
      }
   }

   public static float[] getAngles(Entity entity) {
      if(entity == null) {
         return null;
      } else {
         Minecraft var10001 = mc;
         double var1 = entity.posX - Minecraft.thePlayer.posX;
         var10001 = mc;
         double var3 = entity.posZ - Minecraft.thePlayer.posZ;
         double var10000;
         Minecraft var10002;
         double var5;
         if(entity instanceof EntityLivingBase) {
            EntityLivingBase var11 = (EntityLivingBase)entity;
            var10000 = var11.posY + ((double)var11.getEyeHeight() - 0.4D);
            var10001 = mc;
            var10002 = mc;
            var5 = var10000 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
         } else {
            var10000 = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0D;
            var10001 = mc;
            var10002 = mc;
            var5 = var10000 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
         }

         double var111 = (double)MathHelper.sqrt_double(var1 * var1 + var3 * var3);
         float var9 = (float)(Math.atan2(var3, var1) * 180.0D / 3.141592653589793D) - 90.0F;
         float var10 = (float)(-(Math.atan2(var5, var111) * 180.0D / 3.141592653589793D));
         return new float[]{var9, var10};
      }
   }

   public static boolean isValidToRotate(double var0, double var2) {
      Minecraft var10000 = mc;
      if(Minecraft.thePlayer != null) {
         var10000 = mc;
         if(Minecraft.theWorld != null) {
            var10000 = mc;
            if(Minecraft.thePlayer.getEntityWorld() != null) {
               var10000 = mc;
               Iterator var4 = Minecraft.thePlayer.getEntityWorld().loadedEntityList.iterator();

               while(var4.hasNext()) {
                  Entity var5 = (Entity)var4.next();
                  if(var5 instanceof EntityPlayer) {
                     Minecraft var10001 = mc;
                     if(var5 != Minecraft.thePlayer) {
                        var10000 = mc;
                        if((double)Minecraft.thePlayer.getDistanceToEntity(var5) < var0) {
                           var10000 = mc;
                           var10001 = mc;
                           if(isInFov(Minecraft.thePlayer.rotationYaw, Minecraft.thePlayer.rotationPitch, var5.posX, var5.posY, var5.posZ) < var2) {
                              return true;
                           }
                        }
                     }
                  }
               }

               return false;
            }
         }
      }

      return false;
   }

   public static double normalizeAngle(double var0, double var2) {
      double var4 = Math.abs(var0 % 360.0D - var2 % 360.0D);
      var4 = Math.min(360.0D - var4, var4);
      return Math.abs(var4);
   }

   private double getAngleYaw(EntityLivingBase var1) {
      Minecraft var10001 = mc;
      Minecraft var10002 = mc;
      return (double)getAnglesIgnoringNull(var1, Minecraft.thePlayer.rotationYaw, Minecraft.thePlayer.rotationPitch)[0];
   }
}
