   package me.rexoz.xyz.gems.gui;
   
   import java.util.*;
   
   import me.rexoz.xyz.gems.Main;
   import org.bukkit.Bukkit;
   import org.bukkit.Material;
   import org.bukkit.entity.Player;
   import org.bukkit.event.EventHandler;
   import org.bukkit.event.Listener;
   import org.bukkit.event.inventory.InventoryClickEvent;
   import org.bukkit.inventory.Inventory;
   import org.bukkit.inventory.InventoryHolder;
   import org.bukkit.inventory.ItemStack;
   import org.bukkit.inventory.meta.ItemMeta;
   import org.bukkit.scheduler.BukkitRunnable;
   
   public class Gemgui implements Listener {
      private HashMap<Byte, Boolean> gemID = new HashMap();
   
      public Gemgui() {
         this.gemID.put((byte)1, true);
         this.gemID.put((byte)0, true);
         this.gemID.put((byte)14, true);
      }

      @EventHandler
      public void onInventoryClick(final InventoryClickEvent e) {
         if (e.getView().getBottomInventory().equals(e.getWhoClicked().getInventory())) {
            final ItemStack gem = e.getCursor();
            final ItemStack kilic = e.getCurrentItem();

            if (gem == null || gem.getTypeId() != 438) {
               return;
            }

            if (gem.getAmount() > 1 && kilic != null && isSword(kilic)) {
               e.getWhoClicked().sendMessage("§dGem §7§l> §cBirden fazla gem ile işlem yapamazsınız. Lütfen sadece 1 tane gem ile tıklayın.");
               return;
            }

            byte gemDataID = gem.getData().getData();
            List<Byte> allowedGemIDs = Arrays.asList((byte) 0, (byte) 1, (byte) 14);

            if (!allowedGemIDs.contains(gemDataID)) {
               return;
            }

            if (kilic != null && isSword(kilic)) {
               ItemMeta itemMeta = kilic.getItemMeta();

               if (itemMeta == null || !itemMeta.hasLore()) {
                  e.getWhoClicked().sendMessage("§dGem §7§l> §cKılıçta karabüyü bulunamadı.");
                  return;
               }

               List<String> lore = itemMeta.getLore();
               String yeniGemLore = "";

               switch (gemDataID) {
                  case 0:
                     yeniGemLore = "<Item#438#0#§5Ametist§r#Kritik hasarına %25>";
                     break;
                  case 1:
                     yeniGemLore = "<Item#438#1#§bTurkuaz§r#Kırılmazlık %10>";
                     break;
                  case 14:
                     yeniGemLore = "<Item#438#14#§eTopaz§r#Öldürülen: 0>";
                     break;
               }

               if (lore.contains(yeniGemLore)) {
                  e.getWhoClicked().sendMessage("§dGem §7> §cKılıçta zaten §c" + yeniGemLore.split("#")[3].replace("§5", "").replace("§b", "").replace("§4", "") + " §cgemi var!");
                  return;
               }

               if (e.getSlot() == 32) {
                  e.getWhoClicked().sendMessage("§dGem §7§l> §cGem işlemi iptal edildi, gem geri verildi.");
                  e.setCancelled(true);
                  e.getWhoClicked().setItemOnCursor(null);
                  return;
               }

               e.setCancelled(true);
               e.getWhoClicked().setItemOnCursor(null);

               new BukkitRunnable() {
                  public void run() {
                     openGemGui((Player) e.getWhoClicked(), kilic, gem);
                  }
               }.runTaskLater(Main.getPlugin(), 1L);
            }
         }
      }


      private boolean isSword(ItemStack item) {
         return item.getType().toString().equalsIgnoreCase("SWORD_BUZPERISI") ||
                 item.getType().toString().equalsIgnoreCase("SWORD_KANKILICI") ||
                 item.getType().toString().equalsIgnoreCase("SWORD_MAVIGOKYUZUKILICI") ||
                 item.getType().toString().equalsIgnoreCase("SWORD_ZEHIRLIKESKINLIK") ||
                 item.getType().toString().equalsIgnoreCase("TITANIUM_WARAXE");
      }

      public void openGemGui(Player player, ItemStack kilic, ItemStack gem) {
         Inventory menu = Bukkit.createSingleInventory((InventoryHolder)null, "Gem Yerleştir", 36);
   
         ItemStack c2 = kilic.clone();
         ItemStack c = gem.clone();
         menu.setItem(13, c);
         menu.setItem(19, c2);
   
         ItemStack bilgi = new ItemStack(Material.BOOK);
         ItemMeta bilgiMeta = bilgi.getItemMeta();
         bilgiMeta.setDisplayName("§eBilgi");
         List<String> bilgiLore = Arrays.asList(
                 "§7Başarısız olursan",
                 "§7gem kırıktaşa dönüşür.",
                 "§r",
                 "§7Başarı Şansı: §a%80"
         );
         bilgiMeta.setLore(bilgiLore);
         bilgi.setItemMeta(bilgiMeta);
   
         ItemStack iptal = new ItemStack(Material.STAINED_CLAY, 1, (short) 1);
         ItemMeta iptalMeta = iptal.getItemMeta();
         iptalMeta.setDisplayName("§cVazgeç");
         iptalMeta.setLore(Collections.singletonList("§6Vazgeçmek için tıkla"));
         iptal.setItemMeta(iptalMeta);
   
         ItemStack onay = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
         ItemMeta onayMeta = onay.getItemMeta();
         onayMeta.setDisplayName("§aOnaylıyorum");
         onayMeta.setLore(Collections.singletonList("§6Onaylamak için tıkla"));
         onay.setItemMeta(onayMeta);
   
         ItemStack cam = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
         for (int i = 0; i < menu.getSize(); i++) {
            if (menu.getItem(i) == null || menu.getItem(i).getType() == Material.AIR) {
               menu.setItem(i, cam);
            }
         }
   
         menu.setItem(5, bilgi);
         menu.setItem(22, iptal);
         menu.setItem(32, onay);
   
         player.openInventory(menu);
      }

      @EventHandler
      public void gemyerlestirclick(InventoryClickEvent e) {
         if (e.getView().getTitle().equals("Gem Yerleştir")) {
            e.setCancelled(true);

            Player p = (Player) e.getWhoClicked();

            if (e.getSlot() == 22) {
               ItemStack gem = e.getView().getItem(13);
               if (gem != null && gem.getType() != Material.AIR) {
                  p.getInventory().addItem(gem);
               }
               p.closeInventory();
            }
            else if (e.getSlot() == 32) {
               ItemStack gem = e.getView().getItem(13);
               if (gem != null && gem.getType() != Material.AIR) {
                  ItemStack kilic = e.getView().getItem(19);
                  if (kilic != null && kilic.getType() != Material.AIR) {
                     for (int i = 0; i < p.getInventory().getSize(); ++i) {
                        ItemStack item = p.getInventory().getItem(i);
                        if (item != null && item.isSimilar(kilic)) {
                           ItemMeta itemMeta = item.getItemMeta();
                           List<String> lore2 = (itemMeta.hasLore()) ? itemMeta.getLore() : new ArrayList<>();

                           short id = gem.getDurability();
                           String yeni;
                           switch (id) {
                              case 0:
                                 yeni = "<Item#438#0#§5Ametist§r#Kritik hasarına %25>";
                                 break;
                              case 1:
                                 yeni = "<Item#438#1#§bTurkuaz§r#Kırılmazlık %10>";
                                 break;
                              case 4:
                                 yeni = "<Item#438#4#§4Garnet§r#Rakip kılıcına hasar>";
                                 break;
                              case 14:
                                 yeni = "<Item#438#14#§eTopaz§r#Öldürülen: 0>";
                                 break;
                              default:
                                 yeni = "<Item#809#0#§fKırık Taş§r#Bu gem yuvası kullanılamaz>";
                           }

                           String gemism = yeni.split("#")[3].replace("§5", "").replace("§b", "").replace("§e", "").replace("§9", "");

                           boolean hasGem = lore2.stream().anyMatch(lorex -> lorex.contains(gemism));
                           if (hasGem) {
                              p.sendMessage("§6Gem §7§l> §cKılıçta zaten §c" + gemism + " §cvar!");
                              p.closeInventory();
                              return;
                           }

                           int in = -1;
                           boolean has = false;

                           for (int j = 0; j < lore2.size(); ++j) {
                              String lore = lore2.get(j);
                              if (lore.contains("<Item#438#")) {
                                 has = true;
                              }

                              if (lore.contains("Boş Gem Yuvası") && in == -1) {
                                 in = j;
                              }
                           }

                           if (has) {
                              if (in == -1) {
                                 p.sendMessage("§6Gem §7§l> §cYeni gem ekleyemezsin, boş yuva yok!");
                                 p.closeInventory();
                                 return;
                              }

                              lore2.set(in, yeni);
                           } else {
                              lore2.add("");
                              lore2.add(yeni);
                              lore2.add("<Item#0#0#Boş Gem Yuvası#Buraya gem yerleştirilebilir>");
                              lore2.add("<Item#0#0#Boş Gem Yuvası#Buraya gem yerleştirilebilir>");
                           }

                           itemMeta.setLore(lore2);
                           item.setItemMeta(itemMeta);
                           p.sendMessage("§6Gem §7§l> §aBaşarılı bir şekilde gem eklendi");
                           p.closeInventory();
                           break;
                        }
                     }
                  }
               }
            } else {
               e.setCancelled(true);
            }
         }
      }
   }