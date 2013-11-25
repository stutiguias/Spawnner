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

    private SpawnerControl spawnerControl;
    private Spawner plugin;
    private MakeEntity makeEntity;
    
    public SpawnWork(Spawner plugin,SpawnerControl spawner) {
        this.spawnerControl = spawner;
        this.plugin = plugin;
    }
    
    @Override
    public void run() {
        try {
            makeEntity = new MakeEntity(spawnerControl);
            
            for (int i = 1; i <= spawnerControl.getQuantd().intValue(); i++) {
                Bukkit.getScheduler().runTask(plugin,makeEntity);
            }
            
            if(plugin.ShowDebug) {
                Spawner.logger.log(Level.INFO, "Spawning {0}", spawnerControl.getName());
            }
            
            Spawner.SpawnerList.remove(spawnerControl);
            Spawner.SpawnerList.add(makeEntity.spawnerControl);
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
}
