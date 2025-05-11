package me.rexoz.xyz.gems.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import me.rexoz.xyz.gems.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class Gemgui2 implements Listener {
   private HashMap<Byte, Boolean> gemID2 = new HashMap();

   public Gemgui2() {
      this.gemID2.put((byte)12, true);
      this.gemID2.put((byte)1, true);
      this.gemID2.put((byte)11, true);
   }

   @EventHandler
   public void onInventoryClick(final InventoryClickEvent e) {
      if (e.getView().getBottomInventory().equals(e.getWhoClicked().getInventory())) {
         final ItemStack gem = e.getCursor();
         final ItemStack armor = e.getCurrentItem();

         if (gem == null || gem.getTypeId() != 438) {
            return;
         }

         if (gem.getAmount() > 1 && armor != null && isArmor(armor)) {
            e.getWhoClicked().sendMessage("§dGem §7§l> §cBirden fazla gem ile işlem yapamazsınız. Lütfen sadece 1 tane gem ile tıklayın.");
            return;
         }

         byte gemDataID = gem.getData().getData();
         List<Byte> allowedGemIDs = Arrays.asList((byte) 12, (byte) 1, (byte) 11);

         if (!allowedGemIDs.contains(gemDataID)) {
            return;
         }

         if (armor != null && isArmor(armor)) {
            ItemMeta itemMeta = armor.getItemMeta();

            if (itemMeta == null || !itemMeta.hasLore()) {
               e.getWhoClicked().sendMessage("§dGem §7§l> §cZırhda karabüyü bulunamadı.");
               return;
            }

            List<String> lore = itemMeta.getLore();
            String yeniGemLore = "";

            switch (gemDataID) {
               case 12:
                  yeniGemLore = "<Item#438#12#§cSpinel§r#+1 Kalp>";
                  break;
               case 1:
                  yeniGemLore = "<Item#438#1#§bTurkuaz§r#Kırılmazlık %10>";
                  break;
               case 11:
                  yeniGemLore = "<Item#438#11#§9Safir§r#Kötü etkiler -%10>";
                  break;
            }

            if (lore.contains(yeniGemLore)) {
               e.getWhoClicked().sendMessage("§dGem §7> §cZırhda zaten §c" + yeniGemLore.split("#")[3].replace("§5", "").replace("§b", "").replace("§4", "") + " §cgemi var!");
               return;
            }

            e.setCancelled(true);
            e.getWhoClicked().setItemOnCursor(null);

            new BukkitRunnable() {
               public void run() {
                  openGemGui((Player) e.getWhoClicked(), armor, gem);
               }
            }.runTaskLater(Main.getPlugin(), 1L);
         }
      }
   }

   private boolean isArmor(ItemStack item) {
      return item.getType().toString().equalsIgnoreCase("TITANIUM_HELMET") ||
              item.getType().toString().equalsIgnoreCase("TITANIUM_CHESTPLATE") ||
              item.getType().toString().equalsIgnoreCase("TITANIUM_BOOTS") ||
              item.getType().toString().equalsIgnoreCase("TITANIUM_LEGGINGS");
   }

   private void openGemGui(Player p, ItemStack armor, ItemStack gem) {
      Inventory menu = Bukkit.createSingleInventory(null, "Zırh'a Gem Yerleştir", 36);

      ItemStack gemClone = gem.clone();
      ItemStack armorClone = armor.clone();

      menu.setItem(13, gemClone);
      menu.setItem(19, armorClone);

      ItemStack bilgi = new ItemStack(Material.BOOK);
      ItemMeta bilgiMeta = bilgi.getItemMeta();
      bilgiMeta.setDisplayName("§eBilgi");
      List<String> bilgiLore = new ArrayList<>();
      bilgiLore.add("§7Başarısız olursan");
      bilgiLore.add("§7gem kırıktaşa dönüşür.");
      bilgiLore.add("§r");
      bilgiLore.add("§7Başarı Şansı: §a%80");
      bilgiMeta.setLore(bilgiLore);
      bilgi.setItemMeta(bilgiMeta);

      ItemStack iptal = new ItemStack(Material.STAINED_CLAY, 1, (short) 1);
      ItemMeta iptalMeta = iptal.getItemMeta();
      iptalMeta.setDisplayName("§cVazgeç");
      List<String> iptalLore = new ArrayList<>();
      iptalLore.add("§6Vazgeçmek için tıkla");
      iptalMeta.setLore(iptalLore);
      iptal.setItemMeta(iptalMeta);

      ItemStack onay = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
      ItemMeta onayMeta = onay.getItemMeta();
      onayMeta.setDisplayName("§aOnaylıyorum");
      List<String> onayLore = new ArrayList<>();
      onayLore.add("§6Onaylamak için tıkla");
      onayMeta.setLore(onayLore);
      onay.setItemMeta(onayMeta);

      ItemStack cam = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
      for (int i = 0; i < menu.getSize(); ++i) {
         if (menu.getItem(i) == null || menu.getItem(i).getType() == Material.AIR) {
            menu.setItem(i, cam);
         }
      }

      menu.setItem(5, bilgi);
      menu.setItem(22, iptal);
      menu.setItem(32, onay);

      p.openInventory(menu);
   }

   @EventHandler
   public void gemyerlestirclick(InventoryClickEvent e) {
      if (e.getView().getTitle().equals("Zırh'a Gem Yerleştir")) {
         e.setCancelled(true);
         Player p = (Player) e.getWhoClicked();

         if (e.getSlot() == 32) {
            ItemStack gem = e.getView().getItem(13);
            if (gem != null && gem.getType() != Material.AIR) {
               ItemStack zirh = e.getView().getItem(19);
               if (zirh != null && zirh.getType() != Material.AIR) {
                  for (int i = 0; i < p.getInventory().getSize(); ++i) {
                     ItemStack item = p.getInventory().getItem(i);
                     if (item != null && item.isSimilar(zirh)) {
                        ItemMeta itemMeta = item.getItemMeta();
                        List<String> lore2 = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();

                        short id = gem.getDurability();
                        String yeni;
                        switch (id) {
                           case 1:
                              yeni = "<Item#438#1#§bTurkuaz§r#Kırılmazlık %10>";
                              break;
                           case 11:
                              yeni = "<Item#438#11#§9Safir§r#Kötü etkiler -%10>";
                              break;
                           case 12:
                              yeni = "<Item#438#12#§cSpinel§r#+1 Kalp>";
                              break;
                           default:
                              yeni = "<Item#809#0#§fKırık Taş§r#Bu gem yuvası kullanılamaz>";
                        }

                        String gemism = yeni.split("#")[3].replace("§5", "").replace("§b", "").replace("§e", "").replace("§9", "");

                        if (lore2.contains(yeni)) {
                           p.sendMessage("§6RN §7> §cZırhta zaten §c" + gemism + " §cvar!");
                           p.closeInventory();
                           return;
                        }
                        boolean hasGem = lore2.stream().anyMatch(lore -> lore.contains("<Item#438#"));
                        int emptySlot = -1;

                        for (int j = 0; j < lore2.size(); ++j) {
                           String lore = lore2.get(j);
                           if (lore.contains("Boş Gem Yuvası") && emptySlot == -1) {
                              emptySlot = j;
                           }
                        }
                        if (hasGem) {
                           if (emptySlot == -1) {
                              p.sendMessage("§6RN §7> §cYeni gem ekleyemezsin, boş yuva yok!");
                              p.closeInventory();
                              return;
                           }
                           lore2.set(emptySlot, yeni);
                        } else {
                           lore2.add("");
                           lore2.add(yeni);
                           lore2.add("<Item#0#0#Boş Gem Yuvası#Buraya gem yerleştirilebilir>");
                           lore2.add("<Item#0#0#Boş Gem Yuvası#Buraya gem yerleştirilebilir>");
                        }

                        itemMeta.setLore(lore2);
                        item.setItemMeta(itemMeta);
                        p.closeInventory();
                     }
                  }
               } else {
                  p.sendMessage("§6Gem §7> §cZırh bulunamadı.");
               }
            }
         }
         else if (e.getSlot() == 22) {
            ItemStack gem = e.getView().getItem(13);
            if (gem != null && gem.getType() != Material.AIR) {
               p.getInventory().addItem(gem);
            }
            p.closeInventory();
         }
         else {
            e.setCancelled(true);
         }
      }
   }
}