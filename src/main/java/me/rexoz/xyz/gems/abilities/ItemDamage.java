package me.rexoz.xyz.gems.abilities;

import java.util.Iterator;
import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemDamage implements Listener {
   @EventHandler
   public void on(PlayerItemDamageEvent e) {
      ItemStack item = e.getItem();
      if (item.hasItemMeta()) {
         ItemMeta meta = item.getItemMeta();
         List<String> lore = meta.getLore();
         if (lore != null && !lore.isEmpty()) {
            boolean has = false;
            Iterator var6 = lore.iterator();

            while(var6.hasNext()) {
               String var = (String)var6.next();
               if (var.contains("<Item#438#1#")) {
                  has = true;
                  break;
               }
            }

            if (has && Math.random() < 0.2D) {
               e.setCancelled(true);
            }
         }
      }

   }
}
