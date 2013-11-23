/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.spawner.task;

import java.util.logging.Level;
import me.stutiguias.spawner.init.Spawner;
import me.stutiguias.spawner.model.SpawnerControl;
import org.bukkit.entity.LivingEntity;

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
            SpawnerControl spanner = Spawnner;
            
            if(plugin.ShowDebug) {
                Spawner.logger.log(Level.INFO, "Spawning {0}", spanner.getName());
                Spawner.logger.log(Level.INFO, "Qtd {0}", spanner.getQuantd().toString());
                Spawner.logger.log(Level.INFO, "Time {0}", spanner.getTime().toString());
                Spawner.logger.log(Level.INFO, "X Y Z {0} {1} {2}",new Object[] { spanner.getLocation().getX(), spanner.getLocation().getY(),spanner.getLocation().getZ() } );
            }
            
            for (int i = 1; i <= spanner.getQuantd().intValue(); i++) {
                LivingEntity ent = spanner.getLocation().getWorld().spawnCreature(spanner.getLocation(), spanner.getType());
                spanner.addMob(ent.getUniqueId());
            }
            
            if(plugin.ShowDebug) {
                Spawner.logger.log(Level.INFO, "Mobs spawnned {0}", String.valueOf(spanner.getMobs().size()));
            }
            
            Spawner.spawnerList.remove(Spawnner);
            Spawner.spawnerList.add(spanner);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
}
