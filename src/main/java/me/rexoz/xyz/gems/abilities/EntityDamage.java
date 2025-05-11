package me.rexoz.xyz.gems.abilities;

import java.util.Iterator;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EntityDamage implements Listener {
   @EventHandler(
      priority = EventPriority.HIGH,
      ignoreCancelled = true
   )
   public void on(EntityDamageByEntityEvent e) {
      ItemStack hand;
      if (e.getDamager() instanceof Player && e.getEntity() instanceof Player && e.getDamager().getVelocity().getY() >= 0.0D && !e.getDamager().isOnGround() && (hand = ((Player)e.getDamager()).getItemInHand()) != null && hand.hasItemMeta() && hand.getItemMeta().getLore() != null) {
         List<String> lore = hand.getItemMeta().getLore();
         if (!lore.isEmpty()) {
            boolean critical = false;
            Iterator var5 = lore.iterator();

            while(var5.hasNext()) {
               String var = (String)var5.next();
               if (var.contains("<Item#438#0#")) {
                  critical = true;
                  break;
               }
            }

            if (critical) {
               double damage = e.getDamage();
               damage += damage / 100.0D * 25.0D;
               e.setDamage(damage);
            }
         }
      }

   }

   @EventHandler
   public void onMeteorit(EntityDamageByEntityEvent e) {
      if (e.getDamager() instanceof Player) {
         Player saldiran = (Player)e.getDamager();
         ItemStack hnd = saldiran.getInventory().getItemInHand();
         if (hnd != null && hnd.getType() != Material.AIR) {
            if (hnd.hasItemMeta() && hnd.getItemMeta().hasLore()) {
               Iterator var4 = hnd.getItemMeta().getLore().iterator();

               while(var4.hasNext()) {
                  String l = (String)var4.next();
                  if (l.contains("<Item#438#6#§eMeteorit§r#%10 Şansla körlük verir>") && Math.random() < 0.03D && e.getEntity() instanceof Player) {
                     Player saldirilan = (Player)e.getEntity();
                     saldirilan.sendMessage("§a" + saldiran.getName() + " §cadlı oyuncu sana §aMeteorit §cuyguladı!");
                     saldiran.sendMessage("§a" + saldirilan.getName() + " §cadlı oyuncuya §aMeteorit §cuyguladın!");
                     saldirilan.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 160, 2));
                  }
               }
            }

         }
      }
   }
}
