package me.rexoz.xyz.gems.abilities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.rexoz.xyz.gems.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Effect implements Listener {
   private final Map<PotionEffectType, List<UUID>> handled = new HashMap();
   private final Map<UUID, Integer> cooldowns = new HashMap();

   public void init(Main main) {
      (new BukkitRunnable() {
         public void run() {
            Iterator var1 = Bukkit.getOnlinePlayers().iterator();

            label27:
            while(var1.hasNext()) {
               Player player = (Player)var1.next();
               Iterator var3 = player.getActivePotionEffects().iterator();

               while(true) {
                  while(true) {
                     if (!var3.hasNext()) {
                        continue label27;
                     }

                     PotionEffect effect = (PotionEffect)var3.next();
                     UUID playerUUID = player.getUniqueId();
                     int duration = effect.getDuration();
                     if (Effect.this.cooldowns.containsKey(playerUUID) && (Integer)Effect.this.cooldowns.get(playerUUID) > 0) {
                        Effect.this.cooldowns.put(playerUUID, (Integer)Effect.this.cooldowns.get(playerUUID) - 1);
                     } else {
                        player.addPotionEffect(new PotionEffect(effect.getType(), duration - 20, effect.getAmplifier()));
                        Effect.this.cooldowns.put(playerUUID, duration);
                     }
                  }
               }
            }

         }
      }).runTaskTimer(main, 20L, 20L);
   }
}
