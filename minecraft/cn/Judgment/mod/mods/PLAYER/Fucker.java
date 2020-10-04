package cn.Judgment.mod.mods.PLAYER;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.util.vector.Vector3f;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventPreMotion;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.util.Colors;
import cn.Judgment.util.CombatUtil;
import cn.Judgment.util.timeUtils.TimeHelper;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockCake;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;

public class Fucker
extends Mod {
    ArrayList<Vector3f> positions = null;
    private TimeHelper timer = new TimeHelper();
    public static Value<String> mode = new Value("Fucker", "Mode", 0);
    private Value<Double> reach = new Value<Double>("Fucker_Reach", 6.0, 1.0, 6.0, 0.1);
    private Value<Double> delay = new Value<Double>("Fucker_Delay", 120.0, 0.0, 1000.0, 10.0);
    private Value<Boolean> teleport = new Value<Boolean>("Fucker_Teleport", false);

    public Fucker() {
        super("Fucker", Category.PLAYER);
        Fucker.mode.mode.add("Bed");
        Fucker.mode.mode.add("Egg");
        Fucker.mode.mode.add("Cake");
    }
    
   

    @EventTarget
    public void onPre(EventPreMotion event) {
        this.setColor(Colors.GREY.c);
        this.standartDestroyer(event);
    }

    private void standartDestroyer(EventPreMotion event) {
        Iterator<BlockPos> positions = BlockPos.getAllInBox(this.mc.thePlayer.getPosition().subtract(new Vec3i(this.reach.getValueState(), this.reach.getValueState(), this.reach.getValueState())), this.mc.thePlayer.getPosition().add(new Vec3i(this.reach.getValueState(), this.reach.getValueState(), this.reach.getValueState()))).iterator();
        BlockPos bedPos = null;
        while ((bedPos = positions.next()) != null) {
            if (this.mc.theWorld.getBlockState(bedPos).getBlock() instanceof BlockBed && mode.isCurrentMode("Bed") || this.mc.theWorld.getBlockState(bedPos).getBlock() instanceof BlockDragonEgg && mode.isCurrentMode("Egg") || this.mc.theWorld.getBlockState(bedPos).getBlock() instanceof BlockCake && mode.isCurrentMode("Cake")) break;
        }
        if (!(bedPos instanceof BlockPos)) {
            return;
        }
        float[] rot = CombatUtil.getRotationsNeededBlock(bedPos.getX(), bedPos.getY(), bedPos.getZ());
        event.yaw = rot[0];
        event.pitch = rot[1];
        if (this.timer.isDelayComplete(this.delay.getValueState().intValue())) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, bedPos, EnumFacing.DOWN));
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, bedPos, EnumFacing.DOWN));
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, bedPos, EnumFacing.DOWN));
            this.mc.thePlayer.swingItem();
            if (this.teleport.getValueState().booleanValue()) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(bedPos.getX(), bedPos.getY() + 1, bedPos.getZ(), true));
            }
            this.timer.reset();
        }
    }
}
