package me.odium.simplechatchannels.commands;

import me.odium.simplechatchannels.Loader;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class scc implements CommandExecutor {   

  public Loader plugin;
  public scc(Loader plugin)  {
    this.plugin = plugin;
  }


  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)  {
    Player player = null;
    if (sender instanceof Player) {
      player = (Player) sender;
    }    


    if (args.length == 0) {
      sender.sendMessage(ChatColor.GOLD + "[ "+ChatColor.WHITE+"SimpleChatChannels " + plugin.getDescription().getVersion() +ChatColor.GOLD+" ]");
      sender.sendMessage(ChatColor.BLUE + "/addchan [-l] <channel>" + ChatColor.WHITE + "- Create and join a channel");
      sender.sendMessage(ChatColor.BLUE + "/delchan <channel> " + ChatColor.WHITE + "- Delete a channel you own");

      sender.sendMessage(ChatColor.BLUE + "/joinchan <channel> " + ChatColor.WHITE + "- Join a channel");
      sender.sendMessage(ChatColor.BLUE + "/partchan <channel> " + ChatColor.WHITE + "- Part a channel");

      sender.sendMessage(ChatColor.AQUA + "/addowner <channel> <player> " + ChatColor.WHITE + "- Add an owner");
      sender.sendMessage(ChatColor.AQUA + "/delowner <channel> <player> " + ChatColor.WHITE + "- Remove an owner");

      sender.sendMessage(ChatColor.GREEN + "/chanlist " + ChatColor.WHITE + "- List channels");
      sender.sendMessage(ChatColor.GREEN + "/chanlist <channel> " + ChatColor.WHITE + "- List channel users");
      sender.sendMessage(ChatColor.GREEN + "/chanlist -o <channel> " + ChatColor.WHITE + "- List channel owners");
      sender.sendMessage(ChatColor.GREEN + "/chanlist -a <channel> " + ChatColor.WHITE + "- List channel access list");

      sender.sendMessage(ChatColor.YELLOW + "/kuser <channel> <player> " + ChatColor.WHITE + "- Kick user from a chan");

      sender.sendMessage(ChatColor.GOLD + "/adduser <channel> <player> " + ChatColor.WHITE + "- Add user to a locked chan's Access List");
      sender.sendMessage(ChatColor.GOLD + "/deluser <channel> <player> " + ChatColor.WHITE + "- Remove user from a locked chan's Access List");
      
      if(player == null || player.hasPermission("scc.reload")) {
        sender.sendMessage(ChatColor.RED + "/scc reload " + ChatColor.WHITE + "- Reload the config");
      }


      return true;
    } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
      if(player == null || player.hasPermission("scc.reload")) {
        plugin.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Config Reloaded");
        return true;
      }        
    }


    return true;
  }
}