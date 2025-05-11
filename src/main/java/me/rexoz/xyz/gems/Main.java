package me.rexoz.xyz.gems;

import me.rexoz.xyz.armorevent.EventAnalyser;
import me.rexoz.xyz.gems.abilities.Armor;
import me.rexoz.xyz.gems.abilities.Effect;
import me.rexoz.xyz.gems.abilities.EntityDamage;
import me.rexoz.xyz.gems.abilities.ItemDamage;
import me.rexoz.xyz.gems.abilities.PlayerDamage;
import me.rexoz.xyz.gems.abilities.PlayerDeath;
import me.rexoz.xyz.gems.gui.Gemgui;
import me.rexoz.xyz.gems.gui.Gemgui2;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
   public static Main instance;
   private static Main instanceE;

   public void onEnable() {;
      instance = this;
      instanceE = this;
      this.getServer().getPluginManager().registerEvents(new Gemgui2(), this);
      this.getServer().getPluginManager().registerEvents(new Gemgui(), this);
      this.getServer().getPluginManager().registerEvents(new EventAnalyser(), this);
      this.getServer().getPluginManager().registerEvents(new Armor(), this);
      this.getServer().getPluginManager().registerEvents(new EntityDamage(), this);
      this.getServer().getPluginManager().registerEvents(new ItemDamage(), this);
      this.getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
      this.getServer().getPluginManager().registerEvents(new PlayerDamage(), this);
      this.getServer().getPluginManager().registerEvents(new Effect(), this);
      (new Effect()).init(this);
   }

   public static Main getPlugin() {
      return instanceE;
   }
}
