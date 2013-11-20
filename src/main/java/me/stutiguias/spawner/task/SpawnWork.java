/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.spawner.task;

import me.stutiguias.spawner.init.Spawner;
import me.stutiguias.spawner.model.SpawnerClass;
import org.bukkit.entity.LivingEntity;

/**
 *
 * @author Daniel
 */
public class SpawnWork implements Runnable {

    private SpawnerClass mbs;
    
    public SpawnWork(SpawnerClass mbs) {
        this.mbs = mbs;
    }
    
    @Override
    public void run() {
         SpawnerClass mobs = mbs;
         for (int i = 1; i <= mobs.getQuantd().intValue(); i++) {
             LivingEntity ent = mobs.getLocation().getWorld().spawnCreature(mobs.getLocation(), mobs.getType());
             mobs.addMob(ent.getUniqueId());
         }
         Spawner.mobList.remove(mbs);
         Spawner.mobList.add(mobs);
    }
    
}
