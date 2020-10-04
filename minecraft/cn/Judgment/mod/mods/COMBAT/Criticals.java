package cn.Judgment.mod.mods.COMBAT;

import java.util.Random;

import org.apache.commons.lang3.RandomUtils;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventUpdate;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.util.timeUtils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Criticals extends Mod
{
    public Value<String> mode;
    public Value<Double> Delay;
    private TimeHelper timer;
    private static Random random;
    int stage;
    int count;
    double y;
    private int groundTicks;
    
    static {
        Criticals.random = new Random();
    }
    
    public Criticals() {
        super("Criticals", Category.COMBAT);
        this.mode = (Value<String>)new Value("Criticals", "Mode", 0);
        this.Delay = (Value<Double>)new Value("Criticals_Delay", (Object)333.0, (Object)0.0, (Object)1000.0, 1.0);
        this.timer = new TimeHelper();
        this.mode.mode.add("Packet");
        this.mode.mode.add("Packet2");
        this.mode.mode.add("Edit");
        this.mode.mode.add("Cracking");
        this.mode.mode.add("Hypixel");
        this.mode.mode.add("Hypixel2");
        this.mode.mode.add("HVH");
    }
    
    private boolean canCrit(final EntityLivingBase e) {
        final EntityLivingBase target = e;
        if (target.hurtTime > 8) {
            final Minecraft mc = Criticals.mc;
            if (Minecraft.thePlayer.isCollidedVertically) {
                final Minecraft mc2 = Criticals.mc;
                if (Minecraft.thePlayer.onGround) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @EventTarget
    private void onPacket(final EventUpdate e10) {
        super.setDisplayName(this.mode.getModeAt(this.mode.getCurrentMode()));
    }
    
    public void onDisable() {
    }
    
    boolean autoCrit(final EntityLivingBase e) {
        if (!this.isEnabled()) {
            return false;
        }
        final Minecraft mc = Criticals.mc;
        Minecraft.thePlayer.onCriticalHit((Entity)e);
        if (!(e instanceof EntityPlayer)) {
            return false;
        }
        if (this.canCrit(e) && this.timer.hasReached(((Double)this.Delay.getValueState()).longValue())) {
            this.timer.reset();
            final String mode;
            switch (mode = this.mode.getModeAt(this.mode.getCurrentMode())) {
                case "Packet": {
                    Crit(new Double[] { 0.625, -RandomUtils.nextDouble(0.0, 0.625) });
                    break;
                }
                case "Hypixel": {
                    Crit2(new Double[] { 0.051, 0.0125 });
                    break;
                }
                case "Hypixel2": {
                    Crit2(new Double[] { 0.0412622959183674, 0.01, 0.0412622959183674, 0.01, 0.001 });
                    break;
                }
                case "HVH": {
                    Crit2(new Double[] { 0.06250999867916107, -9.999999747378752E-6, 0.0010999999940395355 });
                    break;
                }
                case "Edit": {
                    Crit(new Double[] { 0.0, 0.419999986886978, 0.3331999936342235, 0.2481359985909455, 0.164773281826067, 0.083077817806467, 0.0, -0.078400001525879, -0.155232004516602, -0.230527368912964, -0.304316827457544, -0.376630498238655, -0.104080378093037 });
                    break;
                }
                case "Packet2": {
                    Crit2(new Double[] { 0.626, 0.0 });
                    break;
                }
                case "Cracking": {
                    break;
                }
                default:
                    break;
            }
            return true;
        }
        return false;
    }
    
    public static void Crit2(final Double[] value) {
        final Minecraft mc = Criticals.mc;
        final NetworkManager var1 = Minecraft.thePlayer.sendQueue.getNetworkManager();
        final Minecraft mc2 = Criticals.mc;
        final Double curX = Minecraft.thePlayer.posX;
        final Minecraft mc3 = Criticals.mc;
        final Double curY = Minecraft.thePlayer.posY;
        final Minecraft mc4 = Criticals.mc;
        final Double curZ = Minecraft.thePlayer.posZ;
        for (final Double offset : value) {
            var1.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition((double)curX, curY + offset, (double)curZ, false));
        }
    }
    
    public static void Crit(final Double[] value) {
        final Minecraft mc = Criticals.mc;
        final NetworkManager var1 = Minecraft.thePlayer.sendQueue.getNetworkManager();
        final Minecraft mc2 = Criticals.mc;
        final Double curX = Minecraft.thePlayer.posX;
        final Minecraft mc3 = Criticals.mc;
        Double curY = Minecraft.thePlayer.posY;
        final Minecraft mc4 = Criticals.mc;
        final Double curZ = Minecraft.thePlayer.posZ;
        final Double RandomY = 0.0;
        for (final Double offset : value) {
            curY += offset;
            var1.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition((double)curX, curY + RandomY, (double)curZ, false));
        }
    }
    
    public static void CritAppointRandom(final Double[] value) {
        final Minecraft mc = Criticals.mc;
        final NetworkManager var1 = Minecraft.thePlayer.sendQueue.getNetworkManager();
        final Minecraft mc2 = Criticals.mc;
        final Double curX = Minecraft.thePlayer.posX;
        final Minecraft mc3 = Criticals.mc;
        Double curY = Minecraft.thePlayer.posY;
        final Minecraft mc4 = Criticals.mc;
        final Double curZ = Minecraft.thePlayer.posZ;
        final Double RandomY = 0.0;
        for (final Double offset : value) {
            curY += offset;
            var1.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition((double)curX, curY + RandomY, (double)curZ, false));
        }
    }
}
