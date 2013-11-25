/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.spawner.task;

import java.util.logging.Level;
import me.stutiguias.spawner.init.Spawner;
import me.stutiguias.spawner.model.SpawnerControl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;

/**
 *
 * @author Daniel
 */
public class SpawnWork implements Runnable {

    private SpawnerControl Spawnner;
    private Spawner plugin;
    
    public SpawnWork(Spawner plugin,SpawnerControl spanner) {
        this.Spawnner = spanner;
        this.plugin = plugin;
    }
    
    @Override
    public void run() {
        try {
            SpawnerControl spawner = Spawnner;
            
            for (int i = 1; i <= spawner.getQuantd().intValue(); i++) {
                Entity ent = spawner.getLocation().getWorld().spawnEntity(spawner.getLocation(), spawner.getType());
                spawner.addMob(ent.getUniqueId());
            }
            
            if(plugin.ShowDebug) {
                Spawner.logger.log(Level.INFO, "Spawning {0}", spawner.getName());
            }
            
            Spawner.SpawnerList.remove(Spawnner);
            Spawner.SpawnerList.add(spawner);
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
}
