/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.spawner.task;

import me.stutiguias.spawner.model.SpawnerControl;
import org.bukkit.entity.Entity;

/**
 *
 * @author Daniel
 */
public class MakeEntity implements Runnable {

    public SpawnerControl spawnerControl;
    
    public MakeEntity(SpawnerControl spawnerControl) {
        this.spawnerControl = spawnerControl;
    }
    
    @Override
    public void run() {
        Entity ent = spawnerControl.getLocation().getWorld().spawnEntity(spawnerControl.getLocation(), spawnerControl.getType());
        spawnerControl.addMob(ent.getUniqueId());
    }
    
}
