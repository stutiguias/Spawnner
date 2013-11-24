/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.spawner.listener;

import me.stutiguias.spawner.init.Spawner;
import static me.stutiguias.spawner.init.Spawner.spawnerList;
import me.stutiguias.spawner.model.SpawnerControl;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 *
 * @author Daniel
 */
public class MobListener implements Listener {
    
    private Spawner plugin;
    
    public MobListener(Spawner plugin) {
        this.plugin = plugin;
    }
    
    
    @EventHandler
    public void MobDeath(EntityDeathEvent event) {
        if (spawnerList.isEmpty()) return;
        for (SpawnerControl mobs : spawnerList) {
            if (mobs.containsMob(event.getEntity().getUniqueId())) {
                mobs.removeMob(event.getEntity().getUniqueId());
                if (!mobs.hasMobs()) {
                    plugin.Spawn(mobs);
                }
                return;
            }
        }
    }
}
