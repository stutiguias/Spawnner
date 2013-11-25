package me.stutiguias.spawner.init;

import java.io.File;
import java.io.IOException;
import me.stutiguias.spawner.model.SpawnerControl;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.stutiguias.spawner.commands.SpawnerCommands;
import me.stutiguias.spawner.listener.MobListener;
import me.stutiguias.spawner.model.SpawnerProfile;
import me.stutiguias.spawner.task.SpawnWork;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Spawner extends JavaPlugin {
    
    public String prefix = "[TimeSpawner] ";
    public static final Logger logger = Logger.getLogger("Minecraft");
    
    public static final String PluginDir = "plugins" + File.separator + "TimeSpawner";
    public static String PluginPlayerDir = PluginDir + File.separator + "spawners";
    
    private final MobListener mobListener = new MobListener(this);
    
    public static List<SpawnerControl> SpawnerList;
    
    public static List<Integer> SpawnerTasks;
            
    private ConfigAccessor config;
    
    public boolean ShowDebug;
    
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
        SpawnerTasks = new ArrayList();
        
        Load();
        ReloadMobs();
        
        getCommand("sp").setExecutor(new SpawnerCommands(this));
        
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(mobListener, this);
        
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
            
            ShowDebug = fc.getBoolean("ShowDebug");
            
        }catch(IOException ex){
            getLogger().log(Level.WARNING, "Erro Loading Config");
        }
    }

    public void Spawn(SpawnerControl spawnner) {
        SpawnerTasks.add(Bukkit.getScheduler().scheduleSyncDelayedTask(this,new SpawnWork(this,spawnner),spawnner.getTime().intValue() * 20L));
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
}