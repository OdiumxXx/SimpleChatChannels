package me.odium.simplechatchannels;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class ServerChatPlayerListener implements Listener {
  public static Loader plugin;
  Logger log = Logger.getLogger("Minecraft");

  public ServerChatPlayerListener(Loader instance) {
    plugin = instance;
  }

  @EventHandler
  public void onPlayerChat(PlayerChatEvent chat) {
    Player player = chat.getPlayer();
    if (plugin.getConfig().getBoolean("SilenceGeneralChat")) {
      if (!plugin.getStorageConfig().getStringList("InChatList").contains(player.getPlayer().getDisplayName())) {
        chat.getRecipients().clear();
        Player[] players = Bukkit.getOnlinePlayers();
        Set<Player> chatrecipients = chat.getRecipients();
        for (Player cake: players) {
          if (!plugin.getStorageConfig().getStringList("InChatList").contains(cake.getDisplayName())) {
            chatrecipients.add(cake);
          }
        }
      }
    }
    if(plugin.overRide == 1){
      plugin.overRide = 0;
    }  else{
      String message = chat.getMessage();
      if(plugin.pluginEnabled.containsKey(player)){
        String Chan = plugin.ChannelThing.get(player);
        Player[] players = Bukkit.getOnlinePlayers();
        log.info("[" + Chan + " / " + player.getName() + "]" + message);
        List<String> ChanList = plugin.getStorageConfig().getStringList(Chan+".list");
        for(Player op: players){
          if(ChanList.contains(op.getName())) {
            op.sendMessage(plugin.GRAY + "[" + plugin.GREEN + Chan + plugin.GRAY + "/" + plugin.AQUA + player.getDisplayName() + plugin.GRAY + "]" + ChatColor.WHITE + " " + message);
          }
        }
        chat.setCancelled(true);
      }
    }
  }
}
