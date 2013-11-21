package me.stutiguias.spawner.init;

import me.stutiguias.spawner.model.SpawnerClass;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.stutiguias.spawner.commands.SpawnerCommands;
import me.stutiguias.spawner.listener.MobListener;
import me.stutiguias.spawner.task.SpawnWork;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Spawner extends JavaPlugin {
    
    public String prefix = "[Spawner] ";
    public static final Logger logger = Logger.getLogger("Minecraft");
    
    private final MobListener mobListener = new MobListener(this);
    
    public static List<SpawnerClass> mobList;

    @Override
    public void onEnable() {
        mobList = new ArrayList();
        try {
            Load();
        } catch (IOException ex) {
            Logger.getLogger(Spawner.class.getName()).log(Level.SEVERE, null, ex);
        }
        ReloadMobs();
        
        getCommand("sp").setExecutor(new SpawnerCommands(this));
        
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(mobListener, this);
        
    }

    @Override
    public void onDisable() {
        Save();
    }

    private void Load() throws IOException {
        
            getLogger().log(Level.INFO, "Nothing to load.");
            getLogger().log(Level.INFO, "Loading Spawns...");
            getLogger().log(Level.INFO, "Spawns loaded with sucess.");

    }

    private void Save() {
        if (mobList.isEmpty()) {
            getLogger().log(Level.INFO, "Nothing to save.");
            return;
        }
            getLogger().log(Level.INFO, "Salving Spawns...");
            getLogger().log(Level.INFO, "Spawns Save with sucess.");

    }

    public void Spawn(SpawnerClass mbs) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(this,new SpawnWork(mbs),mbs.getTime().intValue() * 20L);
    }

    private void ReloadMobs() {
        SpawnerClass mobs;
        Iterator it;
        for (Iterator i = mobList.iterator(); i.hasNext();) {
            mobs = (SpawnerClass) i.next();
            for (it = mobs.getLocation().getWorld().getLivingEntities().iterator(); it.hasNext();) {
                LivingEntity ent = (LivingEntity) it.next();
                if (mobs.containsMob(ent.getUniqueId())) {
                    mobs.removeMob(ent.getUniqueId());
                    ent.remove();
                    if (!mobs.hasMobs()) {
                        Spawn(mobs);
                        break;
                    }
                }
            }
        }
    }
}