/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.spawner.task;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import me.stutiguias.spawner.init.Spawner;
import me.stutiguias.spawner.model.SpawnerControl;
import org.bukkit.entity.Entity;

/**
 *
 * @author Daniel
 */
public class SpawnCheck implements Runnable {

    private final Spawner plugin;
    
    public SpawnCheck(Spawner plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void run() {

        Spawner.logger.log(Level.INFO,"{0} : Starting to check Spawners...",new Object[] { plugin.prefix });
        for (SpawnerControl spawnerControl:Spawner.SpawnerList) {
            
            Set<UUID> mobsAlive = new HashSet<>();
            if(!spawnerControl.hasMobs()) continue;
            
            for(Entity entity:spawnerControl.getWorld().getEntities()) {

                for (UUID uuid : spawnerControl.getMobs()) {
                    if(entity.getUniqueId().equals(uuid)) mobsAlive.add(uuid);
                }
                
            }
                            
            if(mobsAlive.isEmpty()) {
                Spawner.logger.log(Level.INFO,"{0} : Found mob spawner to fix {1} !",new Object[] { plugin.prefix , spawnerControl.getName() });
                spawnerControl.cleanMobs();
                plugin.Spawn(spawnerControl);
            }

            spawnerControl.setMobs(mobsAlive);

        }
        Spawner.logger.log(Level.INFO,"{0} : End check Spawners...",new Object[] { plugin.prefix });
        
    }
    
}
