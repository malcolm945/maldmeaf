package cn.Judgment.mod.mods.COMBAT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventUpdate;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.mod.ModManager;
import cn.Judgment.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;

public class AntiBot
extends Mod {
    public static Value<String> mode = new Value("AntiBot", "Mode", 0);
    public static List invalid = new ArrayList();
    public static ArrayList bots = new ArrayList();
    public AntiBot() {
        super("AntiBot", Category.COMBAT);
        mode.addValue("Hypixel");
        mode.addValue("MinePlex");
    }

    public List<EntityPlayer> getPlayerList() {
        Minecraft.getMinecraft();
        Collection<NetworkPlayerInfo> playerInfoMap = Minecraft.thePlayer.sendQueue.getPlayerInfoMap();
        ArrayList<EntityPlayer> list = new ArrayList<EntityPlayer>();
        for (NetworkPlayerInfo networkPlayerInfo : playerInfoMap) {
            list.add(Minecraft.getMinecraft().theWorld.getPlayerEntityByName(networkPlayerInfo.getGameProfile().getName()));
        }
        return list;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mode.isCurrentMode("MinePlex")) {
            this.setDisplayName("Mineplex");
            for (Entity var3 : this.mc.theWorld.loadedEntityList) {
                if (!(var3 instanceof EntityPlayer) || Minecraft.thePlayer.getDistanceToEntity(var3) > 5.0f || Float.isNaN(((EntityPlayer)var3).getHealth()) || var3 == Minecraft.thePlayer || (double)((EntityPlayer)var3).getHealth() >= 20.0 || (double)((EntityPlayer)var3).getHealth() == 1.0) continue;
                this.mc.theWorld.removeEntity(var3);
                PlayerUtil.tellPlayer("\u00a7d[Judgment]\u00a7fRemoved a bot: " + var3.getName() + " Health: " + ((EntityPlayer)var3).getHealth());
            }
        }
        if (mode.isCurrentMode("Hypixel")) {
            this.setDisplayName("Hypixel");
            for (Object list : this.mc.theWorld.playerEntities) {
                EntityPlayer entity = (EntityPlayer)list;
                if (list == Minecraft.thePlayer || entity.isDead || !entity.isInvisible() || !this.getPlayerList().contains(entity) || entity.getCustomNameTag().length() < 2) continue;
                for (PotionEffect potionEffect : entity.getActivePotionEffects()) {
                }
                this.mc.theWorld.removeEntity(entity);
            }
        }
    }

    public static boolean isInTablist(EntityPlayer player) {
        if (Minecraft.getMinecraft().isSingleplayer()) {
            return true;
        }
        for (NetworkPlayerInfo o : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
            NetworkPlayerInfo playerInfo = o;
            if (!playerInfo.getGameProfile().getName().equalsIgnoreCase(player.getName())) continue;
            return true;
        }
        return false;
    }

    public static boolean isBot(Entity entity) {
        if (ModManager.getModByName("AntiBot").isEnabled() && mode.isCurrentMode("Hypixel")) {
            if (entity.getDisplayName().getFormattedText().startsWith("\u00a7") && !entity.getDisplayName().getFormattedText().toLowerCase().contains("[npc")) {
                return false;
            }
            return true;
        }
        return false;
    }
}

