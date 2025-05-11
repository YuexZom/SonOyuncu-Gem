package me.rexoz.xyz.gems.abilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerDeath implements Listener {
   @EventHandler
   public void on(PlayerDeathEvent e) {
      ItemStack hand = null;
      Player killer = e.getEntity().getKiller();
      if (killer != null && (hand = killer.getItemInHand()) != null && hand.hasItemMeta() && hand.getItemMeta().getLore() != null) {
         List<String> lore = hand.getItemMeta().getLore();
         if (!lore.isEmpty()) {
            ArrayList<String> lore1 = new ArrayList();
            Iterator var6 = lore.iterator();

            while(var6.hasNext()) {
               String var = (String)var6.next();
               if (!var.contains("<Item#438#14#")) {
                  lore1.add(var);
               } else {
                  int a = Integer.parseInt(var.replace("<Item#438#14#§eTopaz§r#Öldürülen: ", "").replace(">", ""));
                  StringBuilder var10001 = (new StringBuilder()).append("<Item#438#14#§eTopaz§r#Öldürülen: ");
                  ++a;
                  lore1.add(var10001.append(a).append(">").toString());
               }
            }

            ItemMeta meta = hand.getItemMeta();
            meta.setLore(lore1);
            hand.setItemMeta(meta);
         }
      }

   }
}
