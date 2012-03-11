package me.odium.simplechatchannels;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Loader extends JavaPlugin {
  public static Loader plugin;
    Logger log = Logger.getLogger("Minecraft");
  ChatColor WHITE = ChatColor.WHITE;
  ChatColor RED = ChatColor.RED;
  ChatColor AQUA = ChatColor.AQUA;
  ChatColor BLUE = ChatColor.BLUE;
  ChatColor GREEN = ChatColor.GREEN;
  ChatColor GRAY = ChatColor.GRAY;
    String fnlOut = "";
  public Map<Player, String> ChannelThing = new HashMap<Player, String>();
    public Map<Player, Boolean> pluginEnabled = new HashMap<Player, Boolean>();
  public Map<Player, String> userAttached = new HashMap<Player, String>();
  public Map<Player, Boolean> smChat = new HashMap<Player, Boolean>();
  public final ServerChatPlayerListener playerListener = new ServerChatPlayerListener(this);
  public final PlayerLeave leaveListener = new PlayerLeave(this);
  int overRide = 0;
  String fnlMsg0 = "";
  Player pmt;
  String name = null;

  // Custom Config  
  private FileConfiguration StorageConfig = null;
  private File StorageConfigFile = null;

  public void reloadStorageConfig() {
    if (StorageConfigFile == null) {
      StorageConfigFile = new File(getDataFolder(), "StorageConfig.yml");
    }
    StorageConfig = YamlConfiguration.loadConfiguration(StorageConfigFile);

    // Look for defaults in the jar
    InputStream defConfigStream = getResource("StorageConfig.yml");
    if (defConfigStream != null) {
      YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
      StorageConfig.setDefaults(defConfig);
    }
  }
  public FileConfiguration getStorageConfig() {
    if (StorageConfig == null) {
      reloadStorageConfig();
    }
    return StorageConfig;
  }
  public void saveStorageConfig() {
    if (StorageConfig == null || StorageConfigFile == null) {
      return;
    }
    try {
      StorageConfig.save(StorageConfigFile);
    } catch (IOException ex) {
      Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Could not save config to " + StorageConfigFile, ex);
    }
  }
  // End Custom Config

  public String myGetPlayerName(String name) { 
    Player caddPlayer = getServer().getPlayerExact(name);
    String pName;
    if(caddPlayer == null) {
      caddPlayer = getServer().getPlayer(name);
      if(caddPlayer == null) {
        pName = name;
      } else {
        pName = caddPlayer.getName();
      }
    } else {
      pName = caddPlayer.getName();
    }
    return pName;
  }

  public void onEnable() {
    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(playerListener, this);    
    // Load Custom Config
    FileConfiguration ccfg = getStorageConfig();
    FileConfigurationOptions ccfgOptions = ccfg.options();
    ccfgOptions.copyDefaults(true);
    ccfgOptions.copyHeader(true);
    saveStorageConfig();
    log.info("[" + getDescription().getName() + "] " + getDescription().getVersion() + " enabled.");
  }

  public void onDisable() {
    PluginDescriptionFile pdfFile = this.getDescription();
    log.info(pdfFile.getName() + " is now disabled.");
  }

  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
    Player[] players = Bukkit.getOnlinePlayers();
    for(String str: args){
      fnlOut += " " + str;
    }

    Player player = null;
    if (sender instanceof Player) {
      player = (Player) sender;
    }

//    if(cmd.getName().equalsIgnoreCase("ch")){
//      if(args[0] == "create"){
//        String name = args[1];
//        if(player != null)
//          channel.createChannel(name, player);
//      }
//    }
    
    if(cmd.getName().equalsIgnoreCase("scc")){
      sender.sendMessage(ChatColor.GOLD + "--- SimpleChatChannels " + getDescription().getVersion() + " ---");
      sender.sendMessage(ChatColor.BLUE + "/addchan <ChannelName> " + ChatColor.WHITE + "create a channel");
      sender.sendMessage(ChatColor.BLUE + "/delchan <ChannelName> " + ChatColor.WHITE + "delete a channel");
      sender.sendMessage(ChatColor.BLUE + "/adduser <ChannelName> <PlayerName> " + ChatColor.WHITE + "Add a user to channel");
      sender.sendMessage(ChatColor.BLUE + "/deluser <ChannelName> <PlayerName> " + ChatColor.WHITE + "Remove a user");
      sender.sendMessage(ChatColor.BLUE + "/addowner <ChannelName> <PlayerName> " + ChatColor.WHITE + "Add an owner");
      sender.sendMessage(ChatColor.BLUE + "/delowner <ChannelName> <PlayerName> " + ChatColor.WHITE + "Remove an owner");
      sender.sendMessage(ChatColor.BLUE + "/chanlist <channelname> [owner] " + ChatColor.WHITE + "List channel users");
      sender.sendMessage(ChatColor.BLUE + "/schat <channelname>" + ChatColor.WHITE + "Join/Leave a channel you've access to");
      sender.sendMessage(ChatColor.BLUE + "/schat <channelname> <message> " + ChatColor.WHITE + "Message channel users");
      return true;      
    }
    
// CHANNEL MANIPULATION
    if(cmd.getName().equalsIgnoreCase("addchan")){      
      String ChanName = args[0];
      if (getStorageConfig().contains(ChanName)) {
        sender.sendMessage(RED + ChanName + GRAY + " already exists");
        return true;
      }
      getStorageConfig().createSection(ChanName); // create the 'channel'
      List<String> ChList = getStorageConfig().getStringList(ChanName+".list"); // create/get the player list
      List<String> OwList = getStorageConfig().getStringList(ChanName+".list"); // create/get the owner list
      List<String> ChannelsList = getStorageConfig().getStringList("Channels"); // create/get the owner list
      ChList.add(player.getName());  // add the player to the list
      OwList.add(player.getName());  // add the player to the owner list
      ChannelsList.add(ChanName);
      getStorageConfig().set(ChanName+".list.", ChList); // set the new list
      getStorageConfig().set(ChanName+".owner.", OwList); // set the new list
      getStorageConfig().set("Channels", ChannelsList); // set the new list   
      saveStorageConfig();
      sender.sendMessage(GRAY + "Channel " + GREEN + "#" + ChanName + GRAY + " Created");
      return true;
    }

    if(cmd.getName().equalsIgnoreCase("delchan")){      
      String ChanName = args[0];
      List<String> ChowList = getStorageConfig().getStringList(ChanName+".owner");
      List<String> ChannelsList = getStorageConfig().getStringList("Channels"); // create/get the channel list
      if (!ChowList.contains(player.getName()) && !player.hasPermission("scc.admin")) {
        sender.sendMessage(GRAY + "You are not an owner of " + RED + "#" + ChanName);
        return true;
      }
      if (!getStorageConfig().contains(ChanName)) {
        sender.sendMessage(RED + ChanName + GRAY + " does not exist");
        return true;
      }
      getStorageConfig().set(ChanName, null); // delete the channel
      ChannelsList.remove(ChanName);
      getStorageConfig().set("Channels", ChannelsList); // set the new list
      saveStorageConfig();
      sender.sendMessage(GRAY + "Channel " + RED + "#" + ChanName + GRAY + " Deleted");
      return true;
    }

    if(cmd.getName().equalsIgnoreCase("chanlist")){
      if(!player.hasPermission("scc.list")) {
        sender.sendMessage(RED + "No Permission");
      }
      if(args.length == 0) {
        List<String> ChannelsList = getStorageConfig().getStringList("Channels"); // create/get the channel list
        sender.sendMessage(ChatColor.GOLD + "--- Channel List ---");
        for(int i = 0; i < ChannelsList.size(); ++i) {
          String ChannelNames = ChannelsList.get(i);
          sender.sendMessage(ChatColor.GOLD+"- "+WHITE + ChannelNames);          
        }
        return true;
      } else if(args.length == 1) {
        String ChanName = args[0];
        if (!getStorageConfig().contains(ChanName)) {
          sender.sendMessage(RED + ChanName + GRAY + " does not exist");
          return true;
        }      
        java.util.List<String> ChList = getStorageConfig().getStringList(ChanName+".list"); // get the player list
        sender.sendMessage(ChatColor.GOLD + "Channel " + ChanName + "'s" + " User List");
        for(int i = 0; i < ChList.size(); ++i) {
          String ChPlayers = ChList.get(i);
          sender.sendMessage(ChatColor.GOLD+"- "+WHITE + ChPlayers);          
        }
        return true;
      }  else if(args.length == 2 && args[1].contains("owner")) {
        String ChanName = args[0];
        if (!getStorageConfig().contains(ChanName)) {
          sender.sendMessage(RED + ChanName + GRAY + " does not exist");
          return true;
        }      
        List<String> OwList = getStorageConfig().getStringList(ChanName+".owner"); // create/get the owner list
        sender.sendMessage(ChatColor.GOLD + "Channel " + ChanName + "'s" + " Owner List");
        for(int i = 0; i < OwList.size(); ++i) {
          String ChOwners = OwList.get(i);              
          sender.sendMessage(ChatColor.GOLD+"- "+WHITE + ChOwners);          
        }
        return true;
      }       
        return true;
      }
    

// USER MANIPULATION    
    if(cmd.getName().equalsIgnoreCase("adduser")){
      if(args.length != 2){
        sender.sendMessage("/adduser <ChannelName> <PlayerName>");
        return true;
      }      
      String ChanName = args[0];
      List<String> ChowList = getStorageConfig().getStringList(ChanName+".owner");
      if (!ChowList.contains(player.getName()) && !player.hasPermission("scc.admin")) {
        sender.sendMessage(GRAY + "You are not an owner of " + RED + "#" + ChanName);
        return true;
      }
      String AddPlayName = myGetPlayerName(args[1]);
      boolean ChanTemp = getStorageConfig().contains(ChanName);
      if(ChanTemp == false) {
        sender.sendMessage(RED + ChanName + GRAY + " does not exist");
        return true;
      } else {
        List<String> ChList = getStorageConfig().getStringList(ChanName+".list"); // get the player list
        if (ChList.contains(AddPlayName)) {
          sender.sendMessage(RED + AddPlayName + GRAY + " already in " + RED + "#" + ChanName);
          return true;
        } else {
          ChList.add(AddPlayName);  // add the player to the list
          getStorageConfig().set(ChanName+".list.", ChList); // set the new list
          sender.sendMessage(GREEN + AddPlayName + GRAY + " added to " + GREEN + "#" + ChanName + "'s" + GRAY + " user list");
          Player target = this.getServer().getPlayer(AddPlayName);
          if(target != null) { target.sendMessage(GREEN + "* " + GRAY + "You have been added to " + GREEN + "#" + ChanName + "'s" + GRAY + " user list"); }
          saveStorageConfig();
          return true;
        }
      }
    }
 
    if(cmd.getName().equalsIgnoreCase("deluser")){
      if(args.length != 2){
        sender.sendMessage("/deluser <ChannelName> <PlayerName>");
        return true;
      }
      String ChanName = args[0];
      String AddPlayName = myGetPlayerName(args[1]);
      List<String> ChowList = getStorageConfig().getStringList(ChanName+".owner");
      if (!ChowList.contains(player.getName()) && !player.hasPermission("scc.admin") && AddPlayName != args[1]) {
        sender.sendMessage(GRAY + "You are not an owner of " + RED + "#" + ChanName);
        return true;
      }
      boolean ChanTemp = getStorageConfig().contains(ChanName);
      if(ChanTemp == false) {
        sender.sendMessage(RED + ChanName + GRAY + " does not exist");
        return true;
      } else {
        List<String> ChList = getStorageConfig().getStringList(ChanName+".list"); // get the player list
        if (!ChList.contains(AddPlayName)) {
          sender.sendMessage(RED + AddPlayName + GRAY + " is not in" + RED + "#" + ChanName);
          return true;
        } else {
          ChList.remove(AddPlayName);  // add the player to the list
          getStorageConfig().set(ChanName+".list.", ChList); // set the new list
          sender.sendMessage(GREEN + AddPlayName + GRAY + " removed From " + GREEN + "#" + ChanName + "'s" + GRAY + " user list");
          Player target = this.getServer().getPlayer(AddPlayName);
          if(target != null) { target.sendMessage(GREEN + "* " + GRAY + "You have been removed from " + GREEN + "#" + ChanName + "'s" + GRAY + " user list"); }
          saveStorageConfig();
          return true;
        }
      }
    }
 
// OWNER MANIPULATION    
    if(cmd.getName().equalsIgnoreCase("addowner")){
      if(args.length != 2){
        sender.sendMessage("/addowner <ChannelName> <PlayerName>");
        return true;
      }      
      String ChanName = args[0];
      List<String> ChowList = getStorageConfig().getStringList(ChanName+".owner");
      if (!ChowList.contains(player.getName()) && !player.hasPermission("scc.admin")) {
        sender.sendMessage(GRAY + "You are not an owner of " + RED + "#" + ChanName);
        return true;
      }
      String AddPlayName = myGetPlayerName(args[1]);
      boolean ChanTemp = getStorageConfig().contains(ChanName);
      if(ChanTemp == false) {
        sender.sendMessage(RED + ChanName + GRAY + " does not exist");
        return true;
      } else {        
        if (ChowList.contains(AddPlayName)) {
          sender.sendMessage(RED + AddPlayName + GRAY + " is already an owner of " + RED + "#" + ChanName);
          return true;
        } else {
          ChowList.add(AddPlayName);  // add the player to the list
          getStorageConfig().set(ChanName+".owner.", ChowList); // set the new list
          sender.sendMessage(GREEN + AddPlayName + GRAY + " added to " + GREEN + "#" + ChanName + "'s" + GRAY + " owner list");
          Player target = this.getServer().getPlayer(AddPlayName);
          if(target != null) { target.sendMessage(GREEN + "* " + GRAY + "You have been added to " + GREEN + "#" + ChanName + "'s" + GRAY + " owner list"); }
          saveStorageConfig();
          return true;
        }
      }
    }
    
    if(cmd.getName().equalsIgnoreCase("delowner")){
      if(args.length != 2){
        sender.sendMessage("/delowner <ChannelName> <PlayerName>");
        return true;
      }
      String ChanName = args[0];
      List<String> ChowList = getStorageConfig().getStringList(ChanName+".owner");
      if (!ChowList.contains(player.getName()) && !player.hasPermission("scc.admin")) {
        sender.sendMessage(GRAY + "You are not an owner of " + RED + "#" + ChanName);
        return true;
      }
      String AddPlayName = myGetPlayerName(args[1]);
      boolean ChanTemp = getStorageConfig().contains(ChanName);
      if(ChanTemp == false) {
        sender.sendMessage(RED + ChanName + GRAY + " does not exist");
        return true;
      } else {        
        if (!ChowList.contains(AddPlayName)) {
          sender.sendMessage(RED + AddPlayName + GRAY + " is not an owner of " + RED + "#" + ChanName);
          return true;
        } else {
          ChowList.remove(AddPlayName);  // remove the player from the list
          getStorageConfig().set(ChanName+".owner.", ChowList); // set the new list          
          sender.sendMessage(GREEN + AddPlayName + GRAY + " removed From " + GREEN + "#" + ChanName + "'s" + GRAY + " owner list");
          Player target = this.getServer().getPlayer(AddPlayName);
          target.sendMessage(GREEN + "* " + GRAY + "You have been removed from " + GREEN + "#" + ChanName + "'s" + GRAY + " owner list");
          saveStorageConfig();
          return true;
        }
      }
    }
    
    
// MESSAGING
    if(cmd.getName().equalsIgnoreCase("schat")){
      if(args.length == 0){
        return false;
      }
      if(args.length == 1){
        togglePluginState(player, args[0]);
        return true;
      }
      String ChanName = args[0];
      boolean ChanTemp = getStorageConfig().contains(ChanName);
      if(ChanTemp == false) {
        sender.sendMessage(RED + ChanName + GRAY + " does not exist");
        return true;
      } else {
        List<String> ChanList = getStorageConfig().getStringList(ChanName+".list");
        if(ChanList.contains(player.getName())) {
          StringBuilder sb = new StringBuilder();
          for (String arg : args)
            sb.append(arg + " ");
              String[] temp = sb.toString().split(" ");
              String[] temp2 = Arrays.copyOfRange(temp, 1, temp.length);
              sb.delete(0, sb.length());
              for (String message : temp2)
              {
                sb.append(message);
                sb.append(" ");
              }
              String message = sb.toString();          
              for(Player op: players){
                if(ChanList.contains(op.getName())) {
                  op.sendMessage(AQUA + "[" + GREEN + ChanName + AQUA + "/" + player.getDisplayName() + AQUA + "]" + ChatColor.GREEN + " " + message);              
                }
              }
              return true;
        } else {
          sender.sendMessage(RED + "You do not have access to this channel");
          return true;
        }
      }      
    }
    return true;
  }

  public void togglePluginState(Player player, String channelk){
    
    if(pluginEnabled.containsKey(player)){
      if(pluginEnabled.get(player)){
        pluginEnabled.put(player, false);
        pluginEnabled.remove(player);
        player.sendMessage(ChatColor.GOLD + "* " + RED + "Left Channel");
      } else {
        ChannelThing.put(player, channelk);
        String Chan = plugin.ChannelThing.get(player);
        pluginEnabled.put(player, true);
        player.sendMessage(ChatColor.GOLD + "* " + GREEN + "Joined #" + Chan);
      }
    } else {
      ChannelThing.put(player, channelk);
      String Chan = ChannelThing.get(player);
      log.info(Chan);
      pluginEnabled.put(player, true);
      player.sendMessage(ChatColor.GOLD + "* " + GREEN + "Joined #" + Chan);
    }		
  }

}
