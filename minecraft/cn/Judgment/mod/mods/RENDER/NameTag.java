package cn.Judgment.mod.mods.RENDER;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventRender;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.mod.mods.COMBAT.AntiBot;
import cn.Judgment.mod.mods.WORLD.Teams;
import cn.Judgment.util.ClientUtil;
import cn.Judgment.util.Colors;
import cn.Judgment.util.GLUtil;
import cn.Judgment.util.RenderUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

public class NameTag extends Mod {
	
	public Value<Boolean> Armor = new Value("NameTag_Armor", true);
	public Value<Boolean> Health = new Value("NameTag_Health", true);
	public Value<Boolean> invis = new Value("NameTag_Invisibles", true);
	private Value<Double> size = new Value<Double>("NameTag_Size", Double.valueOf(1D), Double.valueOf(1D), Double.valueOf(5D), 0.1D);
	
	public NameTag() {
		super("NameTag", Category.RENDER);
	}
	
	@EventTarget
	public void onRender(EventRender event) {
		for (Object o : mc.theWorld.playerEntities) {
			EntityPlayer p = (EntityPlayer) o;
			if(p != mc.thePlayer && p.isEntityAlive()) {
				 if (((Entity) o).isInvisible() && !this.invis.getValueState().booleanValue()) {
	                    continue;
	                }
				double pX = p.lastTickPosX + (p.posX - p.lastTickPosX) * mc.timer.renderPartialTicks - RenderManager.renderPosX;
                double pY = p.lastTickPosY + (p.posY - p.lastTickPosY) * mc.timer.renderPartialTicks - RenderManager.renderPosY;
                double pZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * mc.timer.renderPartialTicks - RenderManager.renderPosZ;
                renderNameTag(p, p.getName(), pX, pY, pZ);
			}
		}
	}

	private void renderNameTag(EntityPlayer entity, String tag, double pX, double pY, double pZ) {
		 ScaledResolution sr = new ScaledResolution(this.mc);
		 
		 
        FontRenderer fr = mc.fontRendererObj;
        float size = mc.thePlayer.getDistanceToEntity(entity) / 6.0f;
		if(size < 1.1f) {
			size = 1.1f;
		}
		pY += (entity.isSneaking() ? 0.5D : 0.7D);
		float scale = (float) (size * this.size.getValueState().doubleValue());
		scale /= 100f;
        tag =  entity.getDisplayName().getFormattedText();
        
        String bot = "";
        if(AntiBot.isBot(entity)) {
           bot = "§9[BOT]";
        } else {
           bot = "";
        }

		
		String team = "";
		if(Teams.isOnSameTeam(entity)) {
			team = "\247a[Team]";
		} else {
			team = "";
		}
		String lol = team + bot + tag;
		
		double plyHeal = entity.getHealth();
		BigDecimal bigDecimal = new BigDecimal((double)entity.getHealth());
		bigDecimal = bigDecimal.setScale(1, RoundingMode.HALF_UP);
		double HEALTH = bigDecimal.doubleValue();
		

        String COLOR1;
        if (HEALTH > 20.0D) {
           COLOR1 = " \2479";
        } else if (HEALTH >= 10.0D) {
           COLOR1 = " \247a";
        } else if (HEALTH >= 3.0D) {
           COLOR1 = " \247e";
        } else {
           COLOR1 = " \2474";
        }
        
		String hp = "";
		if(this.Health.getValueState().booleanValue()) {	
			 
			hp = COLOR1 + String.valueOf(HEALTH);
		} else {
			hp = "";
		}
		
		
		String dt = "\247c" +(int)entity.getDistanceToEntity(mc.thePlayer) + "m";
		
        GL11.glPushMatrix();
        GL11.glTranslatef((float) pX, (float) pY + 1.4F, (float) pZ);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-scale, -scale, scale);
        GLUtil.setGLCap(2896, false);
        GLUtil.setGLCap(2929, false);
        int width = sr.getScaledHeight()/2;
        int Height = sr.getScaledHeight()/2;
        GLUtil.setGLCap(3042, true);
        GL11.glBlendFunc(770, 771);
 
        String USERNAME = lol + hp + " ";
        int STRING_WIDTH = (int)(fr.getStringWidth(USERNAME) / 2.0F);
        RenderHelper.drawBorderedRect(-STRING_WIDTH - 1.0f, -16.0f, STRING_WIDTH , -3.0f, 1.0f, Colors.BLACK.c, ClientUtil.reAlpha(Colors.BLACK.c, 0.4f));
        fr.drawStringWithShadow(USERNAME, -fr.getStringWidth(USERNAME) / 2.0F + 2 ,  fr.FONT_HEIGHT-22, 16777215);
        
        GL11.glColor3f(1,1,1);
		GL11.glScaled(0.6f, 0.6f, 0.6f);
		GL11.glScaled(1, 1, 1);
		int COLOR = new Color(188,0,0).getRGB();
		if(entity.getHealth() > 20) {
			COLOR = -65292;
		}

	//	RenderUtil.drawRect(-STRING_WIDTH - 1.0f + 149.2f * Math.min(1.0f, entity.getHealth() / 20.0f), -5f, -STRING_WIDTH , -8f, COLOR);
		
		
        GL11.glPushMatrix();
        GL11.glScaled(1.5d, 1.5d, 1.5d);
			if(this.Armor.getValueState().booleanValue()) {	
			int xOffset = 0;
			for (ItemStack armourStack : entity.inventory.armorInventory) {
				if (armourStack != null)
					xOffset -= 11;
			}
			Object renderStack;
			if (entity.getHeldItem() != null) {
				xOffset -= 8;
				renderStack = entity.getHeldItem().copy();
				if ((((ItemStack) renderStack).hasEffect())
						&& (((((ItemStack) renderStack).getItem() instanceof ItemTool))
								|| ((((ItemStack) renderStack).getItem() instanceof ItemArmor))))
					((ItemStack) renderStack).stackSize = 1;
				renderItemStack((ItemStack) renderStack, xOffset, -37);
				xOffset += 20;
			}
			for (ItemStack armourStack : entity.inventory.armorInventory)
				if (armourStack != null) {
					ItemStack renderStack1 = armourStack.copy();
					if ((renderStack1.hasEffect()) && (((renderStack1.getItem() instanceof ItemTool))
							|| ((renderStack1.getItem() instanceof ItemArmor))))
						renderStack1.stackSize = 1;
					renderItemStack(renderStack1, xOffset, -36);
					xOffset += 20;
				}
		}
        GL11.glPopMatrix();
        GLUtil.revertAllCaps();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }
	
	

	public void renderItemStack(ItemStack stack, int x, int y) {
		GL11.glPushMatrix();
		GL11.glDepthMask(true);
		GlStateManager.clear(256);

		net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
		this.mc.getRenderItem().zLevel = -150.0F;
		whatTheFuckOpenGLThisFixesItemGlint();
		this.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
		this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, stack, x, y);
		this.mc.getRenderItem().zLevel = 0.0F;
		net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
		this.renderEnchantText(stack, x, y);
		GlStateManager.disableCull();
		GlStateManager.enableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.disableLighting();
		GlStateManager.scale(0.5D, 0.5D, 0.5D);
		GlStateManager.disableDepth();
		GlStateManager.enableDepth();
		GlStateManager.scale(2.0F, 2.0F, 2.0F);
		GL11.glPopMatrix();

		







	}
	
	private void whatTheFuckOpenGLThisFixesItemGlint() {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
    }
	
	public static void drawBorderedRectNameTag(final float x, final float y, final float x2, final float y2, final float l1, final int col1, final int col2) {
        RenderUtil.drawRect(x, y, x2, y2, col2);
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d((double) x, (double) y);
        GL11.glVertex2d((double) x, (double) y2);
        GL11.glVertex2d((double) x2, (double) y2);
        GL11.glVertex2d((double) x2, (double) y);
        GL11.glVertex2d((double) x, (double) y);
        GL11.glVertex2d((double) x2, (double) y);
        GL11.glVertex2d((double) x, (double) y2);
        GL11.glVertex2d((double) x2, (double) y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

	 public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, int col1, int col2) {
        NameTag.drawRect(x, y, x2, y2, col2);
        float f = (float)(col1 >> 24 & 255) / 255.0f;
        float f1 = (float)(col1 >> 16 & 255) / 255.0f;
        float f2 = (float)(col1 >> 8 & 255) / 255.0f;
        float f3 = (float)(col1 & 255) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glLineWidth((float)l1);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }
	 public static void drawRect(float g, float h, float i, float j, int col1) {
	        float f = (float)(col1 >> 24 & 255) / 255.0f;
	        float f1 = (float)(col1 >> 16 & 255) / 255.0f;
	        float f2 = (float)(col1 >> 8 & 255) / 255.0f;
	        float f3 = (float)(col1 & 255) / 255.0f;
	        GL11.glEnable((int)3042);
	        GL11.glDisable((int)3553);
	        GL11.glBlendFunc((int)770, (int)771);
	        GL11.glEnable((int)2848);
	        GL11.glPushMatrix();
	        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
	        GL11.glBegin((int)7);
	        GL11.glVertex2d((double)i, (double)h);
	        GL11.glVertex2d((double)g, (double)h);
	        GL11.glVertex2d((double)g, (double)j);
	        GL11.glVertex2d((double)i, (double)j);
	        GL11.glEnd();
	        GL11.glPopMatrix();
	        GL11.glEnable((int)3553);
	        GL11.glDisable((int)3042);
	        GL11.glDisable((int)2848);
	    }
	 
	 
		private void renderEnchantText(ItemStack stack, int x, int y) {
			int unbreakingLevel2;
			int enchantmentY = y -8;
			if (stack.getEnchantmentTagList() != null && stack.getEnchantmentTagList().tagCount() >= 6) {
				mc.fontRendererObj.drawStringWithShadow("god", x * 2, enchantmentY, 16711680);
				return;
			}
			if (stack.getItem() instanceof ItemArmor) {
				int protectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
				int projectileProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack);
				int blastProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack);
				int fireProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack);
				int thornsLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
				int unbreakingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
				if (protectionLevel > 0) {
					mc.fontRendererObj.drawStringWithShadow("pr" + protectionLevel, x * 1, enchantmentY, 52479);
					enchantmentY += 8;
				}
				if (projectileProtectionLevel > 0) {
					mc.fontRendererObj.drawStringWithShadow("pp" + projectileProtectionLevel, x * 1, enchantmentY, 52479);
					enchantmentY += 8;
				}
				if (blastProtectionLevel > 0) {
					mc.fontRendererObj.drawStringWithShadow("bp" + blastProtectionLevel, x * 1, enchantmentY, 52479);
					enchantmentY += 8;
				}
				if (fireProtectionLevel > 0) {
					mc.fontRendererObj.drawStringWithShadow("fp" + fireProtectionLevel, x * 1, enchantmentY, 52479);
					enchantmentY += 8;
				}
				if (thornsLevel > 0) {
					mc.fontRendererObj.drawStringWithShadow("t" + thornsLevel, x * 1, enchantmentY, 52479);
					enchantmentY += 8;
				}
				if (unbreakingLevel > 0) {
					mc.fontRendererObj.drawStringWithShadow("u" + unbreakingLevel, x * 1, enchantmentY, 52479);
					enchantmentY += 8;
				}
			}
			if (stack.getItem() instanceof ItemBow) {
				int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
				int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
				int flameLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
				unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
				if (powerLevel > 0) {
					mc.fontRendererObj.drawStringWithShadow("po" + powerLevel, x * 1, enchantmentY, 65535);
					enchantmentY += 8;
				}
				if (punchLevel > 0) {
					mc.fontRendererObj.drawStringWithShadow("pu" + punchLevel, x * 1, enchantmentY, 65535);
					enchantmentY += 8;
				}
				if (flameLevel > 0) {
					mc.fontRendererObj.drawStringWithShadow("f" + flameLevel, x * 1, enchantmentY, 65535);
					enchantmentY += 8;
				}
				if (unbreakingLevel2 > 0) {
					mc.fontRendererObj.drawStringWithShadow("u" + unbreakingLevel2, x * 1, enchantmentY, 65535);
					enchantmentY += 8;
				}
			}
			if (stack.getItem() instanceof ItemSword) {
				int sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
				int knockbackLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack);
				int fireAspectLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
				unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
				if (sharpnessLevel > 0) {
					mc.fontRendererObj.drawStringWithShadow("sh" + sharpnessLevel, x * 1, enchantmentY, 65535);
					enchantmentY += 8;
				}
				if (knockbackLevel > 0) {
					mc.fontRendererObj.drawStringWithShadow("kn" + knockbackLevel, x * 1, enchantmentY, 65535);
					enchantmentY += 8;
				}
				if (fireAspectLevel > 0) {
					mc.fontRendererObj.drawStringWithShadow("f" + fireAspectLevel, x * 1, enchantmentY, 65535);
					enchantmentY += 8;
				}
				if (unbreakingLevel2 > 0) {
					mc.fontRendererObj.drawStringWithShadow("ub" + unbreakingLevel2, x * 1, enchantmentY, 65535);
				}
			}
			if (stack.getItem() instanceof ItemTool) {
				int unbreakingLevel22 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
				int efficiencyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack);
				int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack);
				int silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, stack);
				if (efficiencyLevel > 0) {
					mc.fontRendererObj.drawStringWithShadow("eff" + efficiencyLevel, x * 1, enchantmentY, 65535);
					enchantmentY += 8;
				}
				if (fortuneLevel > 0) {
					mc.fontRendererObj.drawStringWithShadow("fo" + fortuneLevel, x * 1, enchantmentY, 65535);
					enchantmentY += 8;
				}
				if (silkTouch > 0) {
					mc.fontRendererObj.drawStringWithShadow("st" + silkTouch, x * 1, enchantmentY, 65535);
					enchantmentY += 8;
				}
				if (unbreakingLevel22 > 0) {
					mc.fontRendererObj.drawStringWithShadow("ub" + unbreakingLevel22, x * 1, enchantmentY, 65535);
				}
			}
			if (stack.getItem() == Items.golden_apple && stack.hasEffect()) {
				mc.fontRendererObj.drawStringWithShadow("god", x * 2, enchantmentY, 52479);
			}
		}

	
}
