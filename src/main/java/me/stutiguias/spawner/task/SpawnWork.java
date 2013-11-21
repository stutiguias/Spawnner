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
         SpawnerControl spanner = Spawnner;
         for (int i = 1; i <= spanner.getQuantd().intValue(); i++) {
             LivingEntity ent = spanner.getLocation().getWorld().spawnCreature(spanner.getLocation(), spanner.getType());
             spanner.addMob(ent.getUniqueId());
         }
         Spawner.spawnerList.remove(Spawnner);
         Spawner.spawnerList.add(spanner);
    }
    
}
