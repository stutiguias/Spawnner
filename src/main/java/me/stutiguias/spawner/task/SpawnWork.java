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

/**
 *
 * @author Daniel
 */
public class SpawnWork implements Runnable {

    private SpawnerControl Spawnner;
    private Spawner plugin;
    private MakeEntity makeEntity;
    
    public SpawnWork(Spawner plugin,SpawnerControl spanner) {
        this.Spawnner = spanner;
        this.plugin = plugin;
    }
    
    @Override
    public void run() {
        try {
            SpawnerControl spawner = Spawnner;
            makeEntity = new MakeEntity(spawner);
            
            for (int i = 1; i <= spawner.getQuantd().intValue(); i++) {
                Bukkit.getScheduler().runTask(plugin,makeEntity);
            }
            
            if(plugin.ShowDebug) {
                Spawner.logger.log(Level.INFO, "Spawning {0}", spawner.getName());
            }
            
            Spawner.SpawnerList.remove(Spawnner);
            Spawner.SpawnerList.add(makeEntity.spawnerControl);
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
}
