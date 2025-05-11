package me.rexoz.xyz.gems.abilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDamage implements Listener {
   @EventHandler(
      priority = EventPriority.HIGH,
      ignoreCancelled = true
   )
   public void on(EntityDamageByEntityEvent e) {
      if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
         ArrayList<ItemStack> list = new ArrayList(Arrays.asList(((Player)e.getEntity()).getInventory().getArmorContents()));
         int damage = 0;
         Iterator var4 = list.iterator();

         while(true) {
            while(true) {
               ArrayList lore;
               ItemStack hand;
               do {
                  do {
                     if (!var4.hasNext()) {
                        if (damage == 0) {
                           return;
                        }

                        hand = ((Player)e.getDamager()).getItemInHand();
                        if (hand == null) {
                           return;
                        }

                        if (hand.getType().equals(Material.AIR)) {
                           return;
                        }

                        hand.setDurability((short)(hand.getDurability() + damage));
                        return;
                     }

                     hand = (ItemStack)var4.next();
                  } while(!hand.hasItemMeta());
               } while((lore = hand.getItemMeta().getLore() != null ? new ArrayList(hand.getItemMeta().getLore()) : new ArrayList()).isEmpty());

               Iterator var7 = lore.iterator();

               while(var7.hasNext()) {
                  if (((String)var7.next()).contains("<Item#438#4#")) {
                     ++damage;
                     break;
                  }
               }
            }
         }
      }
   }
}
