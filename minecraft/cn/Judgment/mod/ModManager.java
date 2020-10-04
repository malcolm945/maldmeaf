package cn.Judgment.mod;

import java.util.ArrayList;

import cn.Judgment.mod.mods.ClickGui;
import cn.Judgment.mod.mods.COMBAT.AntiBot;
import cn.Judgment.mod.mods.COMBAT.AutoClicker;
import cn.Judgment.mod.mods.COMBAT.AutoPot;
import cn.Judgment.mod.mods.COMBAT.AutoSoup;
import cn.Judgment.mod.mods.COMBAT.AutoSword;
import cn.Judgment.mod.mods.COMBAT.BowAimbot;
import cn.Judgment.mod.mods.COMBAT.Criticals;
import cn.Judgment.mod.mods.COMBAT.FastBow;
import cn.Judgment.mod.mods.COMBAT.Hitbox;
import cn.Judgment.mod.mods.COMBAT.KeepSprint;
import cn.Judgment.mod.mods.COMBAT.Killaura;
import cn.Judgment.mod.mods.COMBAT.Reach;
import cn.Judgment.mod.mods.COMBAT.Velocity;
import cn.Judgment.mod.mods.MOVEMENT.AntiFall;
import cn.Judgment.mod.mods.MOVEMENT.Fly;
import cn.Judgment.mod.mods.MOVEMENT.InvMove;
import cn.Judgment.mod.mods.MOVEMENT.LongJump;
import cn.Judgment.mod.mods.MOVEMENT.NoFall;
import cn.Judgment.mod.mods.MOVEMENT.NoSlow;
import cn.Judgment.mod.mods.MOVEMENT.Safewalk;
import cn.Judgment.mod.mods.MOVEMENT.Scaffold;
import cn.Judgment.mod.mods.MOVEMENT.Speed;
import cn.Judgment.mod.mods.MOVEMENT.Sprint;
import cn.Judgment.mod.mods.MOVEMENT.Strafe;
import cn.Judgment.mod.mods.MOVEMENT.TargetStrafe;
import cn.Judgment.mod.mods.MOVEMENT.ZoomFly;
import cn.Judgment.mod.mods.PLAYER.AntiObf;
import cn.Judgment.mod.mods.PLAYER.AutoArmor;
import cn.Judgment.mod.mods.PLAYER.AutoRejoin;
import cn.Judgment.mod.mods.PLAYER.AutoTool;
import cn.Judgment.mod.mods.PLAYER.Blink;
import cn.Judgment.mod.mods.PLAYER.Freecam;
import cn.Judgment.mod.mods.PLAYER.Fucker;
import cn.Judgment.mod.mods.PLAYER.Jesus;
import cn.Judgment.mod.mods.PLAYER.Lobby;
import cn.Judgment.mod.mods.PLAYER.NoRotate;
import cn.Judgment.mod.mods.PLAYER.Regen;
import cn.Judgment.mod.mods.PLAYER.Respawn;
import cn.Judgment.mod.mods.PLAYER.Step;
import cn.Judgment.mod.mods.PLAYER.Timer;
import cn.Judgment.mod.mods.RENDER.Animation;
import cn.Judgment.mod.mods.RENDER.BlockESP;
import cn.Judgment.mod.mods.RENDER.BlockOverlay;
import cn.Judgment.mod.mods.RENDER.Chams;
import cn.Judgment.mod.mods.RENDER.ChestESP;
import cn.Judgment.mod.mods.RENDER.Crosshair;
import cn.Judgment.mod.mods.RENDER.DMGParticle;
import cn.Judgment.mod.mods.RENDER.ESP;
import cn.Judgment.mod.mods.RENDER.Emote;
import cn.Judgment.mod.mods.RENDER.EnchantEffect;
import cn.Judgment.mod.mods.RENDER.FullBright;
import cn.Judgment.mod.mods.RENDER.HUD;
import cn.Judgment.mod.mods.RENDER.Health;
import cn.Judgment.mod.mods.RENDER.ItemESP;
import cn.Judgment.mod.mods.RENDER.ItemPhysic;
import cn.Judgment.mod.mods.RENDER.NameTag;
import cn.Judgment.mod.mods.RENDER.NoFov;
import cn.Judgment.mod.mods.RENDER.Projectiles;
import cn.Judgment.mod.mods.RENDER.Radar;
import cn.Judgment.mod.mods.RENDER.Skeletal;
import cn.Judgment.mod.mods.RENDER.ViewClip;
import cn.Judgment.mod.mods.RENDER.Xray;
import cn.Judgment.mod.mods.WORLD.AutoL;
import cn.Judgment.mod.mods.WORLD.ChestStealer;
import cn.Judgment.mod.mods.WORLD.Eagle;
import cn.Judgment.mod.mods.WORLD.FastPlace;
import cn.Judgment.mod.mods.WORLD.HideAndSeek;
import cn.Judgment.mod.mods.WORLD.InvCleaner;
import cn.Judgment.mod.mods.WORLD.ModCheck;
import cn.Judgment.mod.mods.WORLD.NameProtect;
import cn.Judgment.mod.mods.WORLD.NoHurtcam;
import cn.Judgment.mod.mods.WORLD.Phase;
import cn.Judgment.mod.mods.WORLD.Spammer;
import cn.Judgment.mod.mods.WORLD.SpeedMine;
import cn.Judgment.mod.mods.WORLD.Teams;
import cn.Judgment.mod.mods.WORLD.Tracers;

public class ModManager {
	public static ArrayList<Mod> modList = new ArrayList();
    public static ArrayList<Mod> sortedModList = new ArrayList();
    
    public ModManager() {
    	//NOTHING
    	this.addMod(new ClickGui());
    	
    	//COMBAT
    	this.addMod(new Reach());
    	this.addMod(new Hitbox());
    	this.addMod(new FastBow());
    	this.addMod(new AntiBot());
    	this.addMod(new AutoPot());
    	this.addMod(new AutoSoup());
    	this.addMod(new Killaura());
    	this.addMod(new Velocity());
    	this.addMod(new Criticals());
    	this.addMod(new BowAimbot());
    	this.addMod(new AutoSword());
    	this.addMod(new KeepSprint());
    	this.addMod(new AutoClicker());
    	
    	//MOVEMENT
    	this.addMod(new Fly());
    	this.addMod(new Speed());
    	this.addMod(new TargetStrafe());
    	this.addMod(new Jesus()); 
    	this.addMod(new Strafe());
    	this.addMod(new NoFall());
    	this.addMod(new Sprint());
    	this.addMod(new NoSlow());
    	this.addMod(new Scaffold());
    	this.addMod(new ZoomFly());
    	this.addMod(new InvMove());
    	this.addMod(new AntiFall());
    	this.addMod(new LongJump());
    	this.addMod(new Safewalk());
    	
    	//RENDER
    	this.addMod(new HUD());
    	this.addMod(new ESP());
    	this.addMod(new Emote());
    	this.addMod(new Xray());
    	this.addMod(new Chams());
    	this.addMod(new NoFov());
    	this.addMod(new Radar());  
    	this.addMod(new Health());
    	this.addMod(new ItemESP());
    	this.addMod(new NameTag());
    	this.addMod(new Tracers());
    	this.addMod(new Skeletal());
    	this.addMod(new ViewClip());
    	this.addMod(new BlockESP());
    	this.addMod(new DMGParticle());
    	this.addMod(new ChestESP()); 
    	this.addMod(new NoHurtcam());
    	this.addMod(new Animation());
    	this.addMod(new FullBright());
    	this.addMod(new ItemPhysic());
    	this.addMod(new Projectiles());
    	this.addMod(new BlockOverlay());
    	this.addMod(new EnchantEffect());
    	this.addMod(new Crosshair());
    	
    	//PLAYER
   // 	this.addMod(new Phase());
    	this.addMod(new Step());
     	//this.addMod(new Lobby());
    	this.addMod(new Blink());
    	this.addMod(new Timer());
    	this.addMod(new Regen());
    	this.addMod(new Fucker());
    	this.addMod(new AutoRejoin());
    	this.addMod(new Freecam());
        this.addMod(new Respawn());
        this.addMod(new AntiObf());
    	this.addMod(new AutoTool());
    	this.addMod(new NoRotate());
    	this.addMod(new AutoArmor());

        
        
    	//WORLD
    	this.addMod(new Eagle());
    	this.addMod(new Teams());  	
    	this.addMod(new AutoL());
    	this.addMod(new Phase());
    	this.addMod(new Spammer());
    	this.addMod(new HideAndSeek());
    	this.addMod(new ModCheck());
    	this.addMod(new FastPlace());	
    	this.addMod(new SpeedMine());
    	this.addMod(new InvCleaner());
    	this.addMod(new NameProtect());
    	this.addMod(new ChestStealer());

    }
    
    public void addMod(Mod m) {
    	modList.add(m);
    }
    
    public ArrayList<Mod> getToggled() {
		ArrayList<Mod> toggled = new ArrayList();
		for(Mod m : this.modList) {
			if(m.isEnabled()) {
				toggled.add(m);
			}
		}
		return toggled;
	}
    
    public static Mod getModuleByClass(Class<? extends Mod> cls) {
        for (Mod m : modList) {
            if (m.getClass() != cls) continue;
            return m;
        }
        return null;
    }
    
    public static Mod getModByName(String mod) {
        for (Mod m : modList) {
            if (!m.getName().equalsIgnoreCase(mod)) continue;
            return m;
        }
        return null;
    }

	public static ArrayList<Mod> getModList() {
		return modList;
	}
}
