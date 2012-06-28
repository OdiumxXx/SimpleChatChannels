package me.odium.simplechatchannels;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.odium.simplechatchannels.commands.addchan;
import me.odium.simplechatchannels.commands.addowner;
import me.odium.simplechatchannels.commands.adduser;
import me.odium.simplechatchannels.commands.chanlist;
import me.odium.simplechatchannels.commands.delchan;
import me.odium.simplechatchannels.commands.delowner;
import me.odium.simplechatchannels.commands.deluser;
import me.odium.simplechatchannels.commands.joinchan;
import me.odium.simplechatchannels.commands.kuser;
import me.odium.simplechatchannels.commands.partchan;
import me.odium.simplechatchannels.commands.scc;
import me.odium.simplechatchannels.listeners.PlayerLeave;
import me.odium.simplechatchannels.listeners.ServerChatPlayerListener;

import org.bukkit.ChatColor;
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

  //ColorMacros
  public ChatColor WHITE = ChatColor.WHITE;
  public ChatColor RED = ChatColor.RED;
  public ChatColor AQUA = ChatColor.AQUA;
  public ChatColor BLUE = ChatColor.BLUE;
  public ChatColor GREEN = ChatColor.GREEN;
  public ChatColor DARK_GREEN = ChatColor.DARK_GREEN;
  public ChatColor DARK_RED = ChatColor.DARK_RED;
  public ChatColor GRAY = ChatColor.GRAY;
  public ChatColor GOLD = ChatColor.GOLD;


  public Map<Player, String> ChannelThing = new HashMap<Player, String>();
  public Map<Player, Boolean> pluginEnabled = new HashMap<Player, Boolean>();
  public final ServerChatPlayerListener playerListener = new ServerChatPlayerListener(this);
  public final PlayerLeave leaveListener = new PlayerLeave(this);
  public Map<Player, String> userAttached = new HashMap<Player, String>();
    public Map<Player, Boolean> smChat = new HashMap<Player, Boolean>();

  public int overRide = 0;
  String fnlMsg0 = "";
  String fnlOut = "";
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
    // Load Config.yml
    FileConfiguration cfg = getConfig();
    FileConfigurationOptions cfgOptions = cfg.options();
    cfgOptions.copyDefaults(true);
    cfgOptions.copyHeader(true);
    saveConfig(); 
    // Load Custom Config
    FileConfiguration ccfg = getStorageConfig();
    FileConfigurationOptions ccfgOptions = ccfg.options();
    ccfgOptions.copyDefaults(true);
    ccfgOptions.copyHeader(true);
    saveStorageConfig();
    // Load command executors
    this.getCommand("addchan").setExecutor(new addchan(this));
    this.getCommand("addowner").setExecutor(new addowner(this));
    this.getCommand("adduser").setExecutor(new adduser(this));
    this.getCommand("chanlist").setExecutor(new chanlist(this));
    this.getCommand("delchan").setExecutor(new delchan(this));
    this.getCommand("delowner").setExecutor(new delowner(this));
    this.getCommand("deluser").setExecutor(new deluser(this));
    this.getCommand("joinchan").setExecutor(new joinchan(this));
    this.getCommand("kuser").setExecutor(new kuser(this));
    this.getCommand("partchan").setExecutor(new partchan(this));
    this.getCommand("scc").setExecutor(new scc(this));
    log.info("[" + getDescription().getName() + "] " + getDescription().getVersion() + " enabled.");
  }

  public void onDisable() {
    List<String> ChansList = getStorageConfig().getStringList("Channels"); // get the channels list    
    for(String ch: ChansList){
      List<String> PList = getStorageConfig().getStringList(ch+".list"); // get the player list
      PList.removeAll(PList);
      getStorageConfig().set(ch+".list", PList); // set the new list
    }
    List<String> InChatList = getStorageConfig().getStringList("InChatList");
    InChatList.clear();
    log.info("" + InChatList);
    getStorageConfig().set("InChatList", InChatList); // set the new list
    saveStorageConfig();
    PluginDescriptionFile pdfFile = this.getDescription();
    log.info(pdfFile.getName() + " is now disabled.");
  }

  public void togglePluginState(Player player, String channelk){
    if(pluginEnabled.containsKey(player)){
      if(pluginEnabled.get(player)){
        String Chan = plugin.ChannelThing.get(player);
        pluginEnabled.put(player, false);
        pluginEnabled.remove(player);
        player.sendMessage(plugin.DARK_GREEN+"[SCC] "+ ChatColor.GOLD + player.getName() + ChatColor.DARK_GREEN+" left "+ Chan);
      } else {
        ChannelThing.put(player, channelk);
        String Chan = plugin.ChannelThing.get(player);
        pluginEnabled.put(player, true);
        player.sendMessage(plugin.DARK_GREEN+"[SCC] "+ ChatColor.GOLD + player.getName() + ChatColor.DARK_GREEN+" joined "+ Chan);
      }
    } else {
      ChannelThing.put(player, channelk);
      String Chan = ChannelThing.get(player);
      log.info(Chan);
      pluginEnabled.put(player, true);
      player.sendMessage(plugin.DARK_GREEN+"[SCC] "+ ChatColor.GOLD + player.getName() + ChatColor.DARK_GREEN+" joined "+ Chan);
    }   
  }
}
//    if(cmd.getName().equalsIgnoreCase("scc")){
//      if (args.length == 0) {
//        sender.sendMessage(ChatColor.GOLD + "--- SimpleChatChannels " + getDescription().getVersion() + " ---");
//        sender.sendMessage(ChatColor.BLUE + "/addchan <ChannelName> [locked] " + ChatColor.WHITE + "- Create and join a channel");
//        sender.sendMessage(ChatColor.BLUE + "/delchan <ChannelName> " + ChatColor.WHITE + "- Delete a channel you own");
//
//        sender.sendMessage(ChatColor.BLUE + "/joinchan <ChannelName> " + ChatColor.WHITE + "- Join a channel");
//        sender.sendMessage(ChatColor.BLUE + "/partchan <ChannelName> " + ChatColor.WHITE + "- Part a channel");
//
//        sender.sendMessage(ChatColor.AQUA + "/addowner <ChannelName> <PlayerName> " + ChatColor.WHITE + "- Add an owner");
//        sender.sendMessage(ChatColor.AQUA + "/delowner <ChannelName> <PlayerName> " + ChatColor.WHITE + "- Remove an owner");
//
//        sender.sendMessage(ChatColor.GREEN + "/chanlist <ChannelName> " + ChatColor.WHITE + "- List channel users");
//        sender.sendMessage(ChatColor.GREEN + "/chanlist <ChannelName> owner " + ChatColor.WHITE + "- List channel owners");
//        sender.sendMessage(ChatColor.GREEN + "/chanlist <ChannelName> access " + ChatColor.WHITE + "- List channel access list");
//
//        sender.sendMessage(ChatColor.YELLOW + "/kuser <ChannelName> <PlayerName> " + ChatColor.WHITE + "- Kick user from a chan");
//
//        sender.sendMessage(ChatColor.GOLD + "/adduser <ChannelName> <PlayerName> " + ChatColor.WHITE + "- Add user to a locked chan's Access List");
//        sender.sendMessage(ChatColor.GOLD + "/deluser <ChannelName> <PlayerName> " + ChatColor.WHITE + "- Remove user from a locked chan's Access List");
//        if(player == null || player.hasPermission("scc.reload")) {
//          sender.sendMessage(ChatColor.RED + "/scc reload " + ChatColor.WHITE + "- Reload the config");
//          sender.sendMessage(ChatColor.RED + "/fixchans " + ChatColor.WHITE + "- Fix ChannelNames (Upgrading from earlier version)");
//        }
//        
//
//        return true;
//      } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
//        if(player == null || player.hasPermission("scc.reload")) {
//          reloadConfig();
//          sender.sendMessage(ChatColor.GREEN + "Config Reloaded");
//          return true;
//        }        
//      }
//    }

//// CHANNEL MANIPULATION
//    if(cmd.getName().equalsIgnoreCase("addchan")){
//      if (player == null) {
//        sender.sendMessage("This command can only be run by a player");
//        return true;
//      }
//      if (args.length == 1) {     
//      String ChanName = args[0].toLowerCase();      
//      if (getStorageConfig().contains(ChanName)) {
//        sender.sendMessage(RED + ChanName + GRAY + " already exists");
//        return true;
//      }
//      getStorageConfig().createSection(ChanName); // create the 'channel'
//      List<String> ChList = getStorageConfig().getStringList(ChanName+".list"); // create/get the player list
//      List<String> OwList = getStorageConfig().getStringList(ChanName+".owner"); // create/get the owner list
//      List<String> AccList = getStorageConfig().getStringList(ChanName+".AccList"); // create/get the owner list
//      List<String> ChannelsList = getStorageConfig().getStringList("Channels"); // create/get the owner list
//      List<String> InChatList = getStorageConfig().getStringList("InChatList"); // get the player list
//      ChList.add(player.getName());  // add the player to the list
//      OwList.add(player.getName());  // add the player to the owner list
//      AccList.add(player.getName());  // add the player to the access list
//      ChannelsList.add(ChanName);
//      InChatList.add(player.getName());  // add the player to the list      
//      getStorageConfig().set(ChanName+".list", ChList); // set the new list
//      getStorageConfig().set(ChanName+".owner", OwList); // set the new list
//      getStorageConfig().set(ChanName+".AccList", AccList); // set the new list
//      getStorageConfig().set(ChanName+".Locked", false); // set the new list
//      getStorageConfig().set("Channels", ChannelsList); // set the new list  
//      getStorageConfig().set("InChatList", InChatList); // set the new list
//      saveStorageConfig();
//      sender.sendMessage(GRAY + "Channel " + GREEN + "#" + ChanName + GRAY + " Created");
//      ChannelThing.put(player, args[0]);
//      pluginEnabled.put(player, true);
//      List<String> ChanList = getStorageConfig().getStringList(ChanName+".list");
//      for(Player op: players){
//        if(ChanList.contains(op.getName())) {
//          op.sendMessage(ChatColor.GOLD + "* " + GREEN + player.getName() + " Joined Channel");
//        }
//      }
//      return true;
//      } else if (args.length == 2 && args[1].equalsIgnoreCase("locked")) {
//        String ChanName = args[0].toLowerCase();
//        Boolean bool = true;
//        if (getStorageConfig().contains(ChanName)) {
//          sender.sendMessage(RED + ChanName + GRAY + " already exists");
//          return true;
//        }
//        getStorageConfig().createSection(ChanName); // create the 'channel'
//        List<String> ChList = getStorageConfig().getStringList(ChanName+".list"); // create/get the player list
//        List<String> OwList = getStorageConfig().getStringList(ChanName+".owner"); // create/get the owner list
//        List<String> AccList = getStorageConfig().getStringList(ChanName+".AccList"); // create/get the owner list
//        List<String> ChannelsList = getStorageConfig().getStringList("Channels"); // create/get the owner list
//        List<String> InChatList = getStorageConfig().getStringList("InChatList"); // get the player list
//        ChList.add(player.getName());  // add the player to the list
//        OwList.add(player.getName());  // add the player to the owner list
//        AccList.add(player.getName());  // add the player to the access list
//        ChannelsList.add(ChanName);
//        InChatList.add(player.getName());  // add the player to the list
//        getStorageConfig().set(ChanName+".list", ChList); // set the new list
//        getStorageConfig().set(ChanName+".owner", OwList); // set the new list
//        getStorageConfig().set(ChanName+".AccList", AccList); // set the new list
//        getStorageConfig().set(ChanName+".locked", bool); // set the new list
//        getStorageConfig().set("Channels", ChannelsList); // set the new list
//        getStorageConfig().set("InChatList", InChatList); // set the new list
//        saveStorageConfig();
//        sender.sendMessage(GRAY + "Locked Channel " + GREEN + "#" + ChanName + GRAY + " Created");
//        ChannelThing.put(player, args[0]);
//        pluginEnabled.put(player, true);
//        List<String> ChanList = getStorageConfig().getStringList(ChanName+".list");
//        for(Player op: players){
//          if(ChanList.contains(op.getName())) {
//            op.sendMessage(ChatColor.GOLD + "* " + GREEN + player.getName() + " Joined Channel");
//          }
//        }
//
//        return true;       
//      }
//    }


//    if(cmd.getName().equalsIgnoreCase("delchan")){      
//      String ChanName = args[0].toLowerCase();
//      List<String> ChowList = getStorageConfig().getStringList(ChanName+".owner");
//      List<String> ChannelsList = getStorageConfig().getStringList("Channels"); // create/get the channel list
//      List<String> InChatList = getStorageConfig().getStringList("InChatList"); // get the player list
//      if (player == null || ChowList.contains(player.getName()) || player.hasPermission("scc.admin")) {
//        if (!getStorageConfig().contains(ChanName)) {
//          sender.sendMessage(RED + ChanName + GRAY + " does not exist");
//          return true;
//        }
//        List<String> ChanList = getStorageConfig().getStringList(ChanName+".list");
//        for(Player op: players){
//          if(ChanList.contains(op.getName())) {
//            if(player == null) {
//              op.sendMessage(RED + "* " + ChanName + GRAY + " has been deleted by " + RED + "Console" );
//            } else {
//            op.sendMessage(RED + "* " + ChanName + GRAY + " has been deleted by " + RED + player.getName() );              
//          }
//          }
//        }
//        getStorageConfig().set(ChanName, null); // delete the channel
//        ChannelsList.remove(ChanName);
//        getStorageConfig().set("Channels", ChannelsList); // set the new list
//        saveStorageConfig();
//        sender.sendMessage(GRAY + "Channel " + RED + "#" + ChanName + GRAY + " Deleted");
//
//        for(Player op: players){
//          if(ChanList.contains(op.getName())) {
//            if(pluginEnabled.containsKey(op)){
//              if(pluginEnabled.get(op)){
//                pluginEnabled.put(op, false);
//                pluginEnabled.remove(op);
//              }
//            }                
//          }
//          if(InChatList.contains(op.getName())) {
//            InChatList.remove(op.getName());
//            getStorageConfig().set("InChatList", InChatList); // set the new list
//            saveStorageConfig();
//          }
//        }
//        return true;
//      } else {
//        sender.sendMessage(GRAY + "You are not an owner of " + RED + "#" + ChanName);
//        return true;
//      }
//    }    


//    if(cmd.getName().equalsIgnoreCase("chanlist")){
//      if(args.length == 0) {
//        List<String> ChannelsList = getStorageConfig().getStringList("Channels"); // create/get the channel list
//        sender.sendMessage(ChatColor.GOLD + "--- Channel List ---");
//        for(int i = 0; i < ChannelsList.size(); ++i) {
//          String ChannelNames = ChannelsList.get(i);
//          if (getStorageConfig().getBoolean(ChannelNames+".locked") == true) {
//            sender.sendMessage(ChatColor.GOLD + "- " + WHITE + ChannelNames + ChatColor.GOLD + " [Locked]");
//          } else {
//            sender.sendMessage(ChatColor.GOLD + "- " + WHITE + ChannelNames);
//          }
//        }
//        return true;
//      } else if(args.length == 1) {
//        String ChanName = args[0].toLowerCase();
//        if (!getStorageConfig().contains(ChanName)) {
//          sender.sendMessage(RED + ChanName + GRAY + " does not exist");
//          return true;
//        }      
//        java.util.List<String> ChList = getStorageConfig().getStringList(ChanName+".list"); // get the player list
//        sender.sendMessage(ChatColor.GOLD + "Channel " + ChanName + "'s" + " User List");
//        for(int i = 0; i < ChList.size(); ++i) {
//          String ChPlayers = ChList.get(i);
//          sender.sendMessage(ChatColor.GOLD+"- "+WHITE + ChPlayers);          
//        }
//        return true;
//      } else if(args.length == 2 && args[1].contains("owner")) {
//        String ChanName = args[0].toLowerCase();
//        if (!getStorageConfig().contains(ChanName)) {
//          sender.sendMessage(RED + ChanName + GRAY + " does not exist");
//          return true;
//        }      
//        List<String> OwList = getStorageConfig().getStringList(ChanName+".owner"); // create/get the owner list
//        sender.sendMessage(ChatColor.GOLD + "Channel " + ChanName + "'s" + " Owner List");
//        for(int i = 0; i < OwList.size(); ++i) {
//          String ChOwners = OwList.get(i);              
//          sender.sendMessage(ChatColor.GOLD+"- "+WHITE + ChOwners);          
//        }
//        return true;
//      } else if(args.length == 2 && args[1].contains("access")) {
//        String ChanName = args[0].toLowerCase();
//        if (!getStorageConfig().contains(ChanName)) {
//          sender.sendMessage(RED + ChanName + GRAY + " does not exist");
//          return true;
//        }      
//        List<String> AccList = getStorageConfig().getStringList(ChanName+".access"); // create/get the owner list
//        sender.sendMessage(ChatColor.GOLD + "Channel " + ChanName + "'s" + " Access List");
//        for(int i = 0; i < AccList.size(); ++i) {
//          String ChAccess = AccList.get(i);              
//          sender.sendMessage(ChatColor.GOLD+"- "+WHITE + ChAccess);          
//        }
//        return true;
//      }       
//        return true;
//      }


// USER MANIPULATION    
//    if(cmd.getName().equalsIgnoreCase("adduser")){
//      if(args.length != 2){
//        sender.sendMessage("/adduser <ChannelName> <PlayerName>");
//        return true;
//      }      
//      String ChanName = args[0].toLowerCase();
//      List<String> ChowList = getStorageConfig().getStringList(ChanName+".owner");
//      if (!ChowList.contains(player.getName()) && !player.hasPermission("scc.admin")) {
//        sender.sendMessage(GRAY + "You are not an owner of " + RED + "#" + ChanName);
//        return true;
//      }
//      String AddPlayName = myGetPlayerName(args[1]);
//      boolean ChanTemp = getStorageConfig().contains(ChanName);
//      if(ChanTemp == false) {
//        sender.sendMessage(GRAY + "Channel " + RED + ChanName + GRAY + " does not exist");
//        return true;
//      } else {
//        List<String> ChList = getStorageConfig().getStringList(ChanName+".AccList"); // get the player access list
//        if (ChList.contains(AddPlayName)) {
//          sender.sendMessage(RED + AddPlayName + GRAY + " already in " + RED + "#" + ChanName + " Access List");
//          return true;
//        } else {
//          ChList.add(AddPlayName);  // add the player to the access list
//          getStorageConfig().set(ChanName+".AccList", ChList); // set the new list
//          saveStorageConfig();
//          sender.sendMessage(GREEN + AddPlayName + GRAY + " added to " + GREEN + "#" + ChanName + "'s" + GRAY + " access list");
//          List<String> ChanList = getStorageConfig().getStringList(ChanName+".list");
//          for(Player op: players){
//            if(ChanList.contains(op.getName())) {
//              op.sendMessage(GREEN + "* " + AddPlayName + GRAY + " has been added to" + GREEN + " #" + ChanName + "'s " + GRAY + "Access List by " + player.getName());              
//            }
//          }
//          
//          return true;
//        }
//      }
//    }

//    if(cmd.getName().equalsIgnoreCase("fixchans")){
//      List<String> ChannelsList = getStorageConfig().getStringList("Channels");      
//      for(int i = 0; i < ChannelsList.size(); ++i) {
//        String ChannelName = ChannelsList.get(0);        
//        List<String> list = getStorageConfig().getStringList(ChannelName+".list");
//        List<String> owner = getStorageConfig().getStringList(ChannelName+".owner");
//        List<String> AccList = getStorageConfig().getStringList(ChannelName+".AccList");
//        List<String> Locked = getStorageConfig().getStringList(ChannelName+".Locked");
//        
//        getStorageConfig().set(ChannelName, null);
//        String ChannelNameLC = ChannelName.toLowerCase();
//        ChannelsList.add(ChannelNameLC);
//        ChannelsList.remove(ChannelName);
//        
//        getStorageConfig().createSection(ChannelNameLC); // create the 'channel'
//        
//        List<String> Newlist = getStorageConfig().getStringList(ChannelNameLC+".list");
//        List<String> Newowner = getStorageConfig().getStringList(ChannelNameLC+".owner");
//        List<String> NewAccList = getStorageConfig().getStringList(ChannelNameLC+".AccList");
//        List<String> NewLocked = getStorageConfig().getStringList(ChannelNameLC+".Locked");
//        Newlist.addAll(list);
//        Newowner.addAll(owner);
//        NewAccList.addAll(AccList);
//        NewLocked.addAll(Locked);
//        getStorageConfig().set(ChannelNameLC+".list", Newlist); // set the new list
//        getStorageConfig().set(ChannelNameLC+".owner", Newowner); // set the new list
//        getStorageConfig().set(ChannelNameLC+".AccList", NewAccList); // set the new list
//        getStorageConfig().set(ChannelNameLC+".locked", NewLocked); // set the new list
//        getStorageConfig().set("Channels", ChannelsList); // set the new list        
//        
//        sender.sendMessage("Setting: " + ChannelName +" to "+ ChannelNameLC);
//      }
//      saveStorageConfig();
//      return true;
//    }
//    

//  if(cmd.getName().equalsIgnoreCase("deluser")){
//    if(args.length != 2){
//      sender.sendMessage("/deluser <ChannelName> <PlayerName>");
//      return true;
//    }
//    String ChanName = args[0].toLowerCase();
//    String AddPlayName = myGetPlayerName(args[1]);
//    List<String> ChowList = getStorageConfig().getStringList(ChanName+".owner");
//    if (!ChowList.contains(player.getName()) && !player.hasPermission("scc.admin") && AddPlayName != args[1]) {
//      sender.sendMessage(GRAY + "You are not an owner of " + RED + "#" + ChanName);
//      return true;
//    }
//    boolean ChanTemp = getStorageConfig().contains(ChanName);
//    if(ChanTemp == false) {
//      sender.sendMessage(RED + ChanName + GRAY + " does not exist");
//      return true;
//    } else {
//      List<String> ChList = getStorageConfig().getStringList(ChanName+".AccList"); // get the player list
//      if (!ChList.contains(AddPlayName)) {
//        sender.sendMessage(RED + AddPlayName + GRAY + " is not in " + RED + "#" + ChanName);
//        return true;
//      } else {
//        ChList.remove(AddPlayName);  // remove the player from the access list
//        getStorageConfig().set(ChanName+".AccList", ChList); // set the new access list
//        saveStorageConfig();
//        sender.sendMessage(RED + AddPlayName + GRAY + " removed from " + RED + "#" + ChanName + "'s" + GRAY + " access list");
//        Player target = this.getServer().getPlayer(AddPlayName);
//        if(target != null) { target.sendMessage(RED + "* " + GRAY + "You have been removed from " + RED + "#" + ChanName + "'s" + GRAY + " access list"); }
//        List<String> ChanList = getStorageConfig().getStringList(ChanName+".AccList");
//        for(Player op: players){
//          if(ChanList.contains(op.getName())) {
//            op.sendMessage(RED + "* " + AddPlayName + GRAY + " has been removed from" + RED + " #" + ChanName + "'s " + GRAY + "acces list by " + player.getName());              
//          }
//        }
//        return true;
//      }
//    }
//  }


//  if(cmd.getName().equalsIgnoreCase("kuser")){
//    if(args.length != 2){
//      sender.sendMessage("/kuser <ChannelName> <PlayerName>");
//      return true;
//    }
//    String ChanName = args[0].toLowerCase();
//    String AddPlayName = myGetPlayerName(args[1]);
//    List<String> ChowList = getStorageConfig().getStringList(ChanName+".owner");
//    if (player == null || ChowList.contains(player.getName()) && player.hasPermission("scc.admin") && AddPlayName == args[1]) {
//      boolean ChanTemp = getStorageConfig().contains(ChanName);
//      if(ChanTemp == false) {
//        sender.sendMessage(RED + "#" + ChanName + GRAY + " does not exist");
//        return true;
//      } else {
//        List<String> ChList = getStorageConfig().getStringList(ChanName+".list"); // get the player list
//        List<String> InChatList = getStorageConfig().getStringList("InChatList"); // get the player list
//        if (!ChList.contains(AddPlayName)) {
//          sender.sendMessage(RED + AddPlayName + GRAY + " is not in " + RED + "#" + ChanName);
//          return true;
//        } else {
//          ChList.remove(AddPlayName);  // remove the player from the list
//          getStorageConfig().set(ChanName+".list", ChList); // set the new list
//          InChatList.remove(player.getName());  // add the player to the list
//          getStorageConfig().set("InChatList", InChatList); // set the new list
//          saveStorageConfig();
//          sender.sendMessage(RED + AddPlayName + GRAY + " removed from " + RED + "#" + ChanName + "'s" + GRAY + " user list");
//          Player target = this.getServer().getPlayer(AddPlayName);
//          if(target != null) { target.sendMessage(RED + "* " + GRAY + "You have been removed from " + RED + "#" + ChanName + "'s" + GRAY + " user list"); }
//          List<String> ChanList = getStorageConfig().getStringList(ChanName+".list");
//          for(Player op: players){
//            if(ChanList.contains(op.getName())) {
//              op.sendMessage(RED + "* " + AddPlayName + GRAY + " has been removed from" + RED + " #" + ChanName + GRAY + " by " + player.getName());              
//            }
//          }
//          return true;
//        }
//      }
//    } else {
//      sender.sendMessage(GRAY + "You are not an owner of " + RED + "#" + ChanName);
//      return true;
//    }
//  }

//    if(cmd.getName().equalsIgnoreCase("joinchan")){
//      if (player == null) {
//        sender.sendMessage("This command can only be run by a player");
//        return true;
//      }
//      String ChanName = args[0].toLowerCase();
//      boolean ChanTemp = getStorageConfig().contains(ChanName);
//      if(ChanTemp == false) {
//        sender.sendMessage(GRAY + "Channel " + RED + ChanName + GRAY + " does not exist");
//        return true;
//      } 
//      if(getStorageConfig().getBoolean(ChanName+".Locked") == true) {
//        if(getStorageConfig().getStringList(ChanName+".AccList.").contains(player.getName())) {
//          List<String> ChList = getStorageConfig().getStringList(ChanName+".list"); // get the player list
//          List<String> InChatList = getStorageConfig().getStringList("InChatList"); // get the player list
//          if (ChList.contains(player.getName())) {
//            player.sendMessage(ChatColor.GOLD + "* " + GREEN + "Already in #" + ChanName);
//            //            togglePluginState(player, args[0]);
//          } else {
//            ChList.add(player.getName());  // add the player to the list
//            getStorageConfig().set(ChanName+".list", ChList); // set the new list
//            InChatList.add(player.getName());  // add the player to the list
//            getStorageConfig().set("InChatList", InChatList); // set the new list
//            saveStorageConfig();
//            ChannelThing.put(player, args[0]);
//            pluginEnabled.put(player, true);
//            List<String> ChanList = getStorageConfig().getStringList(ChanName+".list");
//            for(Player op: players){
//              if(ChanList.contains(op.getName())) {
//                op.sendMessage(ChatColor.GOLD + "* " + GREEN + player.getName() + " Joined Channel");
//              }
//            }
//
//          }
//        } else {
//          sender.sendMessage(RED + "You must be added to this channel's access list");
//          return true;
//        }
//      } else {
//        List<String> ChList = getStorageConfig().getStringList(ChanName+".list"); // get the player list
//        List<String> InChatList = getStorageConfig().getStringList("InChatList"); // get the player list
//        if (ChList.contains(player.getName())) {
//          player.sendMessage(ChatColor.GOLD + "* " + GREEN + "Already in #" + ChanName);
//        } else {
//          ChList.add(player.getName());  // add the player to the list
//          getStorageConfig().set(ChanName+".list", ChList); // set the new list
//          InChatList.add(player.getName());  // add the player to the list
//          getStorageConfig().set("InChatList", InChatList); // set the new list
//          saveStorageConfig();
//          ChannelThing.put(player, args[0]);
//          pluginEnabled.put(player, true);
//          List<String> ChanList = getStorageConfig().getStringList(ChanName+".list");
//          for(Player op: players){
//            if(ChanList.contains(op.getName())) {
//              op.sendMessage(ChatColor.GOLD + "* " + GREEN + player.getName() + " Joined " + ChanName);
//            }
//          }
//        }
//      }
//    }

//    if(cmd.getName().equalsIgnoreCase("partchan")){
//      if (player == null) {
//        sender.sendMessage("This command can only be run by a player");
//        return true;
//      }
//      if (args.length == 0) {
//        sender.sendMessage("/partchan <ChannelName>");
//        return true;
//      } else {
//        String ChanName = args[0].toLowerCase();
//        boolean ChanTemp = getStorageConfig().contains(ChanName);
//        if(ChanTemp == false) {
//          sender.sendMessage(GRAY + "Channel " + RED + ChanName + GRAY + " does not exist");
//          return true;
//        } else {
//          List<String> ChList = getStorageConfig().getStringList(ChanName+".list"); // get the player list
//          List<String> InChatList = getStorageConfig().getStringList("InChatList"); // get the player list
//          if (!ChList.contains(player.getName())) {
//            sender.sendMessage(RED + player.getName() + GRAY + " is not in " + RED + "#" + ChanName);
//            return true;
//          } else {
//            ChList.remove(player.getName());  // remove the player from the list
//            getStorageConfig().set(ChanName+".list", ChList); // set the new list
//            InChatList.remove(player.getName());  // add the player to the list
//            getStorageConfig().set("InChatList", InChatList); // set the new list
//            saveStorageConfig();
//            //          togglePluginState(player, args[0]);
//            if(pluginEnabled.containsKey(player)){
//              if(pluginEnabled.get(player)){
//                pluginEnabled.put(player, false);
//                pluginEnabled.remove(player);
//              }
//            }
//            player.sendMessage(ChatColor.GOLD + "* " + RED + "Left Channel");
//            List<String> ChanList = getStorageConfig().getStringList(ChanName+".list");
//            for(Player op: players){
//              if(ChanList.contains(op.getName())) {
//                op.sendMessage(ChatColor.GOLD + "* " + RED + player.getName() + " Left Channel");
//              }
//            }
//            return true;
//
//          }
//        }
//      }
//    }


// OWNER MANIPULATION    
//    if(cmd.getName().equalsIgnoreCase("addowner")){
//      if(args.length != 2){
//        sender.sendMessage("/addowner <ChannelName> <PlayerName>");
//        return true;
//      }      
//      String ChanName = args[0].toLowerCase();
//      List<String> ChowList = getStorageConfig().getStringList(ChanName+".owner");
//      if (player == null || ChowList.contains(player.getName()) && player.hasPermission("scc.admin")) {
//        String AddPlayName = myGetPlayerName(args[1]);
//        boolean ChanTemp = getStorageConfig().contains(ChanName);
//        if(ChanTemp == false) {
//          sender.sendMessage(RED + ChanName + GRAY + " does not exist");
//          return true;
//        } else {        
//          if (ChowList.contains(AddPlayName)) {
//            sender.sendMessage(RED + AddPlayName + GRAY + " is already an owner of " + RED + "#" + ChanName);
//            return true;
//          } else {
//            ChowList.add(AddPlayName);  // add the player to the list
//            getStorageConfig().set(ChanName+".owner", ChowList); // set the new list
//            sender.sendMessage(GREEN + AddPlayName + GRAY + " added to " + GREEN + "#" + ChanName + "'s" + GRAY + " owner list");
//            Player target = this.getServer().getPlayer(AddPlayName);
//            if(target != null) { target.sendMessage(GREEN + "* " + GRAY + "You have been added to " + GREEN + "#" + ChanName + "'s" + GRAY + " owner list"); }
//            saveStorageConfig();
//            return true;
//          }
//        }
//      } else {
//        sender.sendMessage(GRAY + "You are not an owner of " + RED + "#" + ChanName);
//        return true;
//      }
//    }

//    if(cmd.getName().equalsIgnoreCase("delowner")){
//      if(args.length != 2){
//        sender.sendMessage("/delowner <ChannelName> <PlayerName>");
//        return true;
//      }
//      String ChanName = args[0].toLowerCase();
//      List<String> ChowList = getStorageConfig().getStringList(ChanName+".owner");
//      if (player == null || ChowList.contains(player.getName()) && player.hasPermission("scc.admin")) {
//        String AddPlayName = myGetPlayerName(args[1]);
//        boolean ChanTemp = getStorageConfig().contains(ChanName);
//        if(ChanTemp == false) {
//          sender.sendMessage(RED + ChanName + GRAY + " does not exist");
//          return true;
//        } else {        
//          if (!ChowList.contains(AddPlayName)) {
//            sender.sendMessage(RED + AddPlayName + GRAY + " is not an owner of " + RED + "#" + ChanName);
//            return true;
//          } else {
//            ChowList.remove(AddPlayName);  // remove the player from the list
//            getStorageConfig().set(ChanName+".owner", ChowList); // set the new list          
//            sender.sendMessage(RED + AddPlayName + GRAY + " removed From " + RED + "#" + ChanName + "'s" + GRAY + " owner list");
//            Player target = this.getServer().getPlayer(AddPlayName);
//            target.sendMessage(RED + "* " + GRAY + "You have been removed from " + RED + "#" + ChanName + "'s" + GRAY + " owner list");
//            saveStorageConfig();
//            return true;
//          }
//        }
//      } else {
//        sender.sendMessage(GRAY + "You are not an owner of " + RED + "#" + ChanName);
//        return true;
//      }
//    }
//    
//    
//// MESSAGING
//    if(cmd.getName().equalsIgnoreCase("schat")){
//      if(args.length == 0){
//        return false;
//      }
//      if(args.length == 1){
//        togglePluginState(player, args[0]);
//        return true;
//      }
//      String ChanName = args[0].toLowerCase();
//      boolean ChanTemp = getStorageConfig().contains(ChanName);
//      if(ChanTemp == false) {
//        sender.sendMessage(RED + ChanName + GRAY + " does not exist");
//        return true;
//      } else {
//        List<String> ChanList = getStorageConfig().getStringList(ChanName+".list");
//        if(ChanList.contains(player.getName())) {
//          StringBuilder sb = new StringBuilder();
//          for (String arg : args)
//            sb.append(arg + " ");
//              String[] temp = sb.toString().split(" ");
//              String[] temp2 = Arrays.copyOfRange(temp, 1, temp.length);
//              sb.delete(0, sb.length());
//              for (String message : temp2)
//              {
//                sb.append(message);
//                sb.append(" ");
//              }
//              String message = sb.toString();          
//              for(Player op: players){
//                if(ChanList.contains(op.getName())) {
//                  op.sendMessage(GRAY + "[" + GREEN + ChanName + GRAY + "/" + AQUA + player.getName() + GRAY + "]" + ChatColor.GREEN + " " + message);              
//                }
//              }
//              return true;
//        } else {
//          sender.sendMessage(RED + "You do not have access to this channel");
//          return true;
//        }
//      }      
//    }
//    return true;
//  }
//
//
//
//}
