package me.odium.simplechatchannels.listeners;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import me.odium.simplechatchannels.Loader;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ServerChatPlayerListener implements Listener {
  public static Loader plugin;
  Logger log = Logger.getLogger("Minecraft");

  public ServerChatPlayerListener(Loader instance) {
    plugin = instance;
  }

  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent chat) {
    Player player = chat.getPlayer();
    if (plugin.getConfig().getBoolean("SilenceGeneralChat")) {
      if (!plugin.getStorageConfig().getStringList("InChatList").contains(player.getName().toLowerCase())) {
        chat.getRecipients().clear();
        Player[] players = Bukkit.getOnlinePlayers();
        Set<Player> chatrecipients = chat.getRecipients();
        for (Player cake: players) {
          if (!plugin.getStorageConfig().getStringList("InChatList").contains(cake.getName().toLowerCase())) {
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
        log.info("[" + Chan + " / " + player.getDisplayName() + "]" + message);
        List<String> ChanList = plugin.getStorageConfig().getStringList(Chan+".list");
        for(Player op: players){
          if(ChanList.contains(op.getName().toLowerCase())) {
            op.sendMessage(plugin.GRAY + "[" + plugin.GREEN + Chan + plugin.GRAY + "/" + plugin.AQUA + player.getDisplayName() + plugin.GRAY + "]" + ChatColor.WHITE + " " + message);
          }
        }
        chat.setCancelled(true);
      }
    }
  }
}
