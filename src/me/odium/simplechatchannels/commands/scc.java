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


    if (args.length == 0 || args.length == 1 && args[0].equalsIgnoreCase("help")) {
      sender.sendMessage(ChatColor.GOLD + "[ "+ChatColor.WHITE+"SimpleChatChannels " + plugin.getDescription().getVersion() +ChatColor.GOLD+" ]");
      sender.sendMessage(ChatColor.GREEN + " /scc help channels " + ChatColor.WHITE + "- Channels help menu");
      sender.sendMessage(ChatColor.GREEN + " /scc help users " + ChatColor.WHITE + "- Users help menu");
      sender.sendMessage(ChatColor.GREEN + " /scc help list " + ChatColor.WHITE + "- Channel list help menu");      
      if(player == null || player.hasPermission("scc.reload")) {
        sender.sendMessage(ChatColor.RED + " /scc reload " + ChatColor.WHITE + "- Reload the config");
      }
      return true;
    } else if (args.length == 2 && args[0].equalsIgnoreCase("help") && args[1].equalsIgnoreCase("channels")) {
      sender.sendMessage(ChatColor.GOLD + "[ "+ChatColor.WHITE+"Channel Commands"+ChatColor.GOLD+" ]");
      sender.sendMessage(ChatColor.GREEN + " /addchan [-l] <channel> " + ChatColor.WHITE + "- Create and join a channel");      
      sender.sendMessage(ChatColor.GREEN + " /delchan <channel> " + ChatColor.WHITE + "- Delete a channel you own");
      sender.sendMessage(ChatColor.GREEN + " /joinchan <channel> " + ChatColor.WHITE + "- Join a channel");
      sender.sendMessage(ChatColor.GREEN + " /partchan <channel> " + ChatColor.WHITE + "- Leave a channel");
      sender.sendMessage(ChatColor.GREEN + " /topic <channel> <topic> " + ChatColor.WHITE + "- Set a channel topic");
      if (sender.hasPermission("scc.spychan")) {
        sender.sendMessage(ChatColor.GREEN + " /spychan [channel] " + ChatColor.WHITE + "- Spy on all or a specific channel");
      }
      return true;
    } else if (args.length == 2 && args[0].equalsIgnoreCase("help") && args[1].equalsIgnoreCase("users")) {
      sender.sendMessage(ChatColor.GOLD + "[ "+ChatColor.WHITE+"User Commands"+ChatColor.GOLD+" ]");
      sender.sendMessage(ChatColor.GREEN + " /kuser <channel> <player> " + ChatColor.WHITE + "- Kick user from a chan");
      sender.sendMessage(ChatColor.GREEN + " /addowner <channel> <player> " + ChatColor.WHITE + "- Add an owner");
      sender.sendMessage(ChatColor.GREEN + " /delowner <channel> <player> " + ChatColor.WHITE + "- Remove an owner");   
      sender.sendMessage(ChatColor.GREEN + " /adduser <channel> <player> " + ChatColor.WHITE + "- Add user to a locked chan's Access List");
      sender.sendMessage(ChatColor.GREEN + " /deluser <channel> <player> " + ChatColor.WHITE + "- Remove user from a locked chan's Access List");
      return true;
    } else if (args.length == 2 && args[0].equalsIgnoreCase("help") && args[1].equalsIgnoreCase("list")) {
      sender.sendMessage(ChatColor.GOLD + "[ "+ChatColor.WHITE+"List Commands"+ChatColor.GOLD+" ]");
      sender.sendMessage(ChatColor.GREEN + " /chanlist " + ChatColor.WHITE + "- List channels");
      sender.sendMessage(ChatColor.GREEN + " /chanlist <channel> " + ChatColor.WHITE + "- List channel users");
      sender.sendMessage(ChatColor.GREEN + " /chanlist -o <channel> " + ChatColor.WHITE + "- List channel owners");
      sender.sendMessage(ChatColor.GREEN + " /chanlist -a <channel> " + ChatColor.WHITE + "- List channel access list");
      sender.sendMessage(ChatColor.GREEN + " /chanlist -p <player> " + ChatColor.WHITE + "- List which channel a user is in");
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