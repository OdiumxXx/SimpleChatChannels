package me.odium.simplechatchannels.commands;

import java.util.List;

import me.odium.simplechatchannels.Loader;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class kuser implements CommandExecutor {   

  public Loader plugin;
  public kuser(Loader plugin)  {
    this.plugin = plugin;
  }


  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)  {
    Player player = null;
    if (sender instanceof Player) {
      player = (Player) sender;
    }
    Player[] players = Bukkit.getOnlinePlayers();
    String PlayerName;

    if(args.length != 2){
      sender.sendMessage("/kuser <ChannelName> <PlayerName>");
      return true;
    }

    String ChanName = args[0].toLowerCase();
    if (player != null) {
      PlayerName = player.getName().toLowerCase();  
    } else {
      PlayerName = "Console";
    }
    String AddPlayName = plugin.myGetPlayerName(args[1]).toLowerCase();
    Player target = Bukkit.getPlayer(AddPlayName);
    List<String> ChowList = plugin.getStorageConfig().getStringList(ChanName+".owner");
    if (player == null || ChowList.contains(PlayerName) ) {
      boolean ChanTemp = plugin.getStorageConfig().contains(ChanName);
      if(ChanTemp == false) {
        plugin.NotExist(sender, ChanName);
        return true;
      } else {
        List<String> ChList = plugin.getStorageConfig().getStringList(ChanName+".list"); // get the player list
        List<String> InChatList = plugin.getStorageConfig().getStringList("InChatList"); // get the player list
        if (!ChList.contains(AddPlayName)) {
          sender.sendMessage(plugin.DARK_RED+"[SCC] "+plugin.GOLD + AddPlayName + plugin.DARK_RED+ " is not in " + plugin.GOLD+  ChanName);
          return true;
        } else {
          ChList.remove(AddPlayName);  // remove the player from the list
          plugin.getStorageConfig().set(ChanName+".list", ChList); // set the new list
          InChatList.remove(AddPlayName);  // add the player to the list
          plugin.getStorageConfig().set("InChatList", InChatList); // set the new list
          plugin.saveStorageConfig();

          sender.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.GOLD + AddPlayName + plugin.DARK_GREEN + " kicked from " + plugin.GOLD +  ChanName);
          if(target != null) {
            target.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.GOLD +  plugin.DARK_GREEN + "You have been kicked from " + plugin.GOLD +  ChanName);
            if(plugin.InChannel.containsKey(target)){
              if(plugin.InChannel.get(target)){
                plugin.InChannel.put(target, false);
                plugin.InChannel.remove(target);
              }
            }
          }
          List<String> ChanList = plugin.getStorageConfig().getStringList(ChanName+".list");
          for(Player op: players){
            if(ChanList.contains(op.getName())) {
              op.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.GOLD +  AddPlayName + plugin.DARK_GREEN + " has been kicked from" + plugin.GOLD + ChanName + plugin.DARK_GREEN + " by " +plugin.GOLD+ PlayerName);              
            }
          }
          return true;
        }
      }
    } else {
      plugin.NotOwner(sender, ChanName);
      return true;
    }
  }
}