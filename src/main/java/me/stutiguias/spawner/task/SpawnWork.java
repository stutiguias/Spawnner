/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.spawner.task;

import me.stutiguias.spawner.init.Spawner;
import me.stutiguias.spawner.model.SpawnerControl;
import org.bukkit.entity.LivingEntity;

/**
 *
 * @author Daniel
 */
public class SpawnWork implements Runnable {

    private SpawnerControl Spawnner;
    
    public SpawnWork(SpawnerControl spanner) {
        this.Spawnner = spanner;
    }
    
    @Override
    public void run() {
        try {
            SpawnerControl spanner = Spawnner;
            
            Spawner.logger.info(spanner.getName());
            Spawner.logger.info(spanner.getQuantd().toString());
            Spawner.logger.info(spanner.getTime().toString());
            Spawner.logger.info(String.valueOf(spanner.getLocation().getX()));
            Spawner.logger.info(String.valueOf(spanner.getLocation().getY()));
            Spawner.logger.info(String.valueOf(spanner.getLocation().getZ()));
            Spawner.logger.info(String.valueOf(spanner.getLocation().getPitch()));
            Spawner.logger.info(String.valueOf(spanner.getLocation().getYaw()));
            
            for (int i = 1; i <= spanner.getQuantd().intValue(); i++) {
                LivingEntity ent = spanner.getLocation().getWorld().spawnCreature(spanner.getLocation(), spanner.getType());
                spanner.addMob(ent.getUniqueId());
            }
            
            Spawner.logger.info(String.valueOf(spanner.getMobs().size()));
            
            Spawner.spawnerList.remove(Spawnner);
            Spawner.spawnerList.add(spanner);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
}
