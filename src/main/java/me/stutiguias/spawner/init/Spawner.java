package me.stutiguias.spawner.init;

import java.io.File;
import java.io.IOException;
import me.stutiguias.spawner.model.SpawnerControl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.stutiguias.spawner.commands.SpawnerCommands;
import me.stutiguias.spawner.listener.MobListener;
import me.stutiguias.spawner.listener.PlayerListener;
import me.stutiguias.spawner.listener.SignListener;
import me.stutiguias.spawner.metrics.Metrics;
import me.stutiguias.spawner.model.SpawnerAreaCreating;
import me.stutiguias.spawner.model.SpawnerProfile;
import me.stutiguias.spawner.task.SignUpdate;
import me.stutiguias.spawner.task.SpawnWork;
import me.stutiguias.updater.Updater;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Spawner extends JavaPlugin {
    
    public String prefix = "[TimeSpawner] ";
    public static final Logger logger = Logger.getLogger("Minecraft");
    
    public static final String PluginDir = "plugins" + File.separator + "TimeSpawner";
    public static String PluginPlayerDir = PluginDir + File.separator + "spawners";
    
    private final MobListener mobListener = new MobListener(this);
    private final PlayerListener playerListener = new PlayerListener(this);
    private final SignListener SignListener = new SignListener(this);
    
    public static List<SpawnerControl> SpawnerList;
    public static HashMap<Player,SpawnerAreaCreating> SpawnerCreating;
    public static HashMap<SpawnerControl,Location> SpawnerSignLocation;
    
    public Permission permission = null;
    public Economy economy = null;
    
    public static Random r = new Random();  
    
    private ConfigAccessor config;
    
    public boolean ShowDebug;
    public boolean UpdaterNotify;
    
    public static boolean update = false;
    public static String name = "";
    public static String type = "";
    public static String version = "";
    public static String link = "";
    
    @Override
    public void onEnable() {
        
        File dir = getDataFolder();
        if (!dir.exists()) {
          dir.mkdirs();
        }
        
        File fuserdata = new File(PluginPlayerDir);
        if (!fuserdata.exists()) {
            logger.log(Level.WARNING, "{0} Spawners folder does not exist. Creating 'spawners' Folder", new Object[]{prefix});
            fuserdata.mkdirs();
        }
        
        SpawnerList = new ArrayList();
        SpawnerCreating = new HashMap<>();
        SpawnerSignLocation = new HashMap<>();
        
        Load();
        ReloadMobs();

        
        getCommand("sp").setExecutor(new SpawnerCommands(this));
        
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(mobListener, this);
        pm.registerEvents(playerListener, this);
        pm.registerEvents(SignListener,this);
        
        setupPermissions();
        setupEconomy();
        // Metrics 
        try {
         logger.log(Level.INFO, "{0} {1} - Sending Metrics, Thank You!", new Object[]{prefix, "[Metrics]"});
         Metrics metrics = new Metrics(this);
         metrics.start();
        } catch (IOException e) {
         logger.log(Level.WARNING, "{0} {1} !! Failed to submit the stats !! ", new Object[]{prefix, "[Metrics]"});
        }
       
        if(UpdaterNotify){
            Updater updater = new Updater(this, 49809, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false); // Start Updater but just do a version check
            
            update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE; // Determine if there is an update ready for us
            name = updater.getLatestName(); // Get the latest name
            version = updater.getLatestGameVersion(); // Get the latest game version
            type = updater.getLatestType(); // Get the latest game version
            link = updater.getLatestFileLink(); // Get the latest link
        }
    }

    @Override
    public void onDisable() {

    }
    
    public void OnReload() {
        config.reloadConfig();
        getServer().getPluginManager().disablePlugin(this);
        getServer().getPluginManager().enablePlugin(this);
    }
    
    private void Load(){
        getLogger().log(Level.INFO, "Loading Spawns...");
        File folder = new File(PluginPlayerDir);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile()) {
              getLogger().log(Level.INFO, "Loading Spawner {0}", listOfFiles[i].getName());
              SpawnerList.add(new SpawnerProfile(this).LoadSpawnerControl(listOfFiles[i].getName()));
          }
        }
        getLogger().log(Level.INFO, "Spawns loaded with sucess.");
        
        try {
            config = new ConfigAccessor(this, "config.yml");
            config.setupConfig();
            FileConfiguration fc = config.getConfig();   
                        
            if(!fc.isSet("configversion") || fc.getInt("configversion") != 1){ 
                config.MakeOld();
                config.setupConfig();
                fc = config.getConfig();
            }
            
            ShowDebug = fc.getBoolean("ShowDebug");
            UpdaterNotify =fc.getBoolean("UpdaterNotify");
            
        }catch(IOException ex){
            getLogger().log(Level.WARNING, "Erro Loading Config");
        }
    }
    
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permission = rsp.getProvider();
        return permission != null;
    }

    private Boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
                economy = economyProvider.getProvider();
        }
        return (economy != null);
    }
    
    public void Spawn(SpawnerControl spawnner) {
        Bukkit.getScheduler().runTaskLater(this, new SignUpdate(this,spawnner),1 * 20L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(this,new SpawnWork(this,spawnner),spawnner.getTime().intValue() * 20L);
    }
    
    // TODO : Better Handle Reload - First Save Exist Mobs ( TODO )
    private void ReloadMobs() {
        for (SpawnerControl spawnner : SpawnerList) {
           // for (LivingEntity ent : spawnner.getLocation().getWorld().getLivingEntities()) {
                //if (!spawnner.containsMob(ent.getUniqueId())) continue;
                //spawnner.removeMob(ent.getUniqueId());
                //ent.remove();
                //if (!spawnner.hasMobs()) {
                    Spawn(spawnner);
                    //break;
                //}
           // }
        }
    }
    
    public String parseColor(String message) {
         for (ChatColor color : ChatColor.values()) {
            message = message.replaceAll(String.format("&%c", color.getChar()), color.toString());
        }
        return message;
    }
     
    public void Update() {
        Updater updater = new Updater(this, 49809, this.getFile(), Updater.UpdateType.NO_VERSION_CHECK, true);
    }
    
    public boolean hasPermission(String PlayerName,String Permission) {
       return permission.has(getServer().getPlayer(PlayerName).getWorld(),PlayerName,Permission);
    }
    
    public boolean hasPermission(Player player, String Permission) {
        return permission.has(player.getWorld(), player.getName(), Permission.toLowerCase());
    }
}