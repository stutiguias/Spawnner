package me.stutiguias.spawner.init;

import me.stutiguias.spawner.configs.EnderConfig;
import java.io.File;
import java.io.IOException;
import me.stutiguias.spawner.model.SpawnerControl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.stutiguias.spawner.commands.SpawnerCommands;
import me.stutiguias.spawner.configs.SkeletonConfig;
import me.stutiguias.spawner.configs.ZombieConfig;
import me.stutiguias.spawner.listener.MobListener;
import me.stutiguias.spawner.listener.PlayerListener;
import me.stutiguias.spawner.listener.SignListener;
import me.stutiguias.spawner.metrics.Metrics;
import me.stutiguias.spawner.db.SignYmlDb;
import me.stutiguias.spawner.model.SpawnerAreaCreating;
import me.stutiguias.spawner.db.SpawnerYmlDb;
import me.stutiguias.spawner.db.TmpYmlDb;
import me.stutiguias.spawner.listener.EnderDragonListener;
import me.stutiguias.spawner.model.PlayerProfile;
import me.stutiguias.spawner.task.SignUpdate;
import me.stutiguias.spawner.task.SpawnLocation;
import me.stutiguias.spawner.task.SpawnWork;
import me.stutiguias.updater.Updater;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Spawner extends JavaPlugin {
    
    public String prefix = "[TimeSpawner] ";
    public static final Logger logger = Logger.getLogger("Minecraft");
    
    public static final String PluginDir = "plugins" + File.separator + "TimeSpawner";
    public static String SpawnerDir = PluginDir + File.separator + "spawners";
    public static String SignDir = PluginDir + File.separator + "sign";
    public static String PlayerDir = PluginDir + File.separator + "players";
    public static String TmpDir = PluginDir + File.separator + "tmp";
    
    private final MobListener mobListener = new MobListener(this);
    private final PlayerListener playerListener = new PlayerListener(this);
    private final SignListener SignListener = new SignListener(this);
    private final EnderDragonListener enderDragonListener = new EnderDragonListener(this);
    
    public static List<SpawnerControl> SpawnerList;
    public static HashMap<Player,SpawnerAreaCreating> SpawnerCreating;
    public static HashMap<String,Location> SignLocation;
    public static HashMap<String,PlayerProfile> PlayerProfiles;
    
    public final SignYmlDb signYmlDb = new SignYmlDb(this);
    private final TmpYmlDb tmpYmlDb = new TmpYmlDb(this);
    
    public Permission permission = null;
    public Economy economy = null;
    
    public static Random r = new Random();  
    
    private ConfigAccessor config;
    
    public EnderConfig enderConfig;
    public SkeletonConfig skeletonConfig;
    public ZombieConfig zombieConfig;
    
    public boolean ShowDebug;
    public boolean UpdaterNotify;
    public boolean EnablePulliFFarAway;
    public int PulliFFarAwayTime;
    public int PulliFFarAwayLimit;
    
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
        
        File fspawnerdir = new File(SpawnerDir);
        if (!fspawnerdir.exists()) {
            logger.log(Level.WARNING, "{0} Spawners folder does not exist. Creating !", new Object[]{prefix});
            fspawnerdir.mkdirs();
        }
                
        File fsigndir = new File(SignDir);
        if (!fsigndir.exists()) {
            logger.log(Level.WARNING, "{0} Signs folder does not exist. Creating !", new Object[]{prefix});
            fsigndir.mkdirs();
        }
                
        File fplayerdir = new File(PlayerDir);
        if (!fplayerdir.exists()) {
            logger.log(Level.WARNING, "{0} Players folder does not exist. Creating !", new Object[]{prefix});
            fplayerdir.mkdirs();
        }
                
        File ftmpdir = new File(TmpDir);
        if (!ftmpdir.exists()) {
            logger.log(Level.WARNING, "{0} Tmp folder does not exist. Creating !", new Object[]{prefix});
            ftmpdir.mkdirs();
        }
        
        SpawnerList = new ArrayList();
        SpawnerCreating = new HashMap<>();
        SignLocation = new HashMap<>();
        PlayerProfiles = new HashMap<>();
        
        Load();
        ReloadMobs();
        
        getCommand("sp").setExecutor(new SpawnerCommands(this));
        
        PluginManager pm = getServer().getPluginManager();
        
        pm.registerEvents(mobListener, this);
        pm.registerEvents(playerListener, this);
        pm.registerEvents(SignListener,this);
        pm.registerEvents(enderDragonListener, this);
        
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
        
        for(SpawnerControl spawner:SpawnerList){
            if(!spawner.hasMobs()) continue;
            tmpYmlDb.Create(spawner);
        }
        
        getServer().getPluginManager().disablePlugin(this);
    }
    
    public void OnReload() {
        for(SpawnerControl spawner:SpawnerList){
            if(!spawner.hasMobs()) continue;
            tmpYmlDb.Create(spawner);
        }
        config.reloadConfig();
        getServer().getPluginManager().disablePlugin(this);
        getServer().getPluginManager().enablePlugin(this);
    }
    
    private void Load(){
        getLogger().log(Level.INFO, "Loading YML Data...");
        LoadDataFromYML();
        CheckExistMobs();
        RemoveLostSign();
        getLogger().log(Level.INFO, "...loaded with sucess.");
        
        try {
            config = new ConfigAccessor(this, "config.yml");
            config.setupConfig();
            FileConfiguration fc = config.getConfig();   
                        
            if(!fc.isSet("configversion") || fc.getInt("configversion") != 2){ 
                config.MakeOld();
                config.setupConfig();
                fc = config.getConfig();
            }
            
            ShowDebug = fc.getBoolean("ShowDebug");
            UpdaterNotify =fc.getBoolean("UpdaterNotify");
            EnablePulliFFarAway =fc.getBoolean("EnablePulliFFarAway");
            PulliFFarAwayTime =fc.getInt("PulliFFarAwayTime");
            PulliFFarAwayLimit = fc.getInt("PulliFFarAwayLimit");
            
        }catch(IOException ex){
            getLogger().log(Level.WARNING, "Erro Loading Config");
        }
        
        enderConfig = new EnderConfig(this);
        skeletonConfig = new SkeletonConfig(this);
        zombieConfig = new ZombieConfig(this);
        
        if(EnablePulliFFarAway)
        Bukkit.getScheduler().runTaskTimer(this, new SpawnLocation(this), PulliFFarAwayTime * 20, PulliFFarAwayTime * 20);
        
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
        if(spawnner.hasMobs()) return;
        Bukkit.getScheduler().runTaskLater(this, new SignUpdate(this,spawnner),1 * 20L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(this,new SpawnWork(this,spawnner),spawnner.getTime().intValue() * 20L);
    }

    private void ReloadMobs() {
        for (SpawnerControl spawnner : SpawnerList) {
            if(spawnner.hasMobs()) continue;
            Spawn(spawnner);
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
    
    private void RemoveLostSign() {
        HashMap<String,Location> tmpSignLocation = new HashMap<>(SignLocation);
        
        for(Map.Entry<String, Location> signlocation:tmpSignLocation.entrySet()) {
            String worldname = signlocation.getValue().getWorld().getName();
            Block block = getServer().getWorld(worldname).getBlockAt(signlocation.getValue());
            BlockState blockState = null;
            if(!block.isEmpty()) blockState = block.getState();
            if(blockState == null || !(blockState instanceof Sign)){
                SignLocation.remove(signlocation.getKey());
                new SignYmlDb(this).Remove(signlocation.getKey());
            }
        }
    }
    
    private void LoadDataFromYML() {
        
        File folder = new File(SpawnerDir);
        File[] listOfFiles = folder.listFiles();
        
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                SpawnerList.add(new SpawnerYmlDb(this).LoadSpawnerControl(listOfFile.getName()));
            }
        }
               
        folder = new File(SignDir);
        listOfFiles = folder.listFiles();
        
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                SignLocation.put( listOfFile.getName().replace(".yml","") , new SignYmlDb(this).LoadSign(listOfFile.getName()));
            }
        }
    }
    
    public void CheckExistMobs() {
        for(SpawnerControl spawner:SpawnerList) {
            if(spawner.getWorld() == null) { 
                getLogger().log(Level.WARNING, "Erro parsing world of spawn");
                new SpawnerYmlDb(this).RemoveSpawnerControl(spawner.getName());
                continue;
            }
            
            String worldname = spawner.getWorld().getName();

            for(LivingEntity entity:Bukkit.getServer().getWorld(worldname).getLivingEntities()) {
                if(!tmpYmlDb.Exist(spawner)) continue;
                
                List<UUID> mobs = tmpYmlDb.LoadPlayer(spawner);
                
                for(UUID mob:mobs) {
                   if(entity.getUniqueId().equals(mob)) spawner.addMob(mob);
                }
                
            }    
            
            tmpYmlDb.RemoveSpawnerControl(spawner.getName());
        }
    }
    
    public SpawnerControl FindSpawn(String name) {
        for(SpawnerControl spawnerControl:Spawner.SpawnerList) {
            if(!spawnerControl.getName().equalsIgnoreCase(name)) continue;
            return spawnerControl;
        }
        return null;
    }
    
    public boolean isTimeSpawnerMob(UUID monster) {
        for(SpawnerControl spawnerControl:Spawner.SpawnerList) {
            if(spawnerControl.containsMob(monster)) return true;
        }
        return false;
    }
            
    
    public long getCurrentMilli() {
            return System.currentTimeMillis();
    }
}