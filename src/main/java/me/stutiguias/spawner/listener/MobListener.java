/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.spawner.listener;

import java.util.ArrayList;
import me.stutiguias.spawner.init.Spawner;
import static me.stutiguias.spawner.init.Spawner.SpawnerList;
import me.stutiguias.spawner.model.PlayerProfile;
import me.stutiguias.spawner.model.SpawnerControl;
import org.bukkit.Chunk;
import org.bukkit.entity.Animals;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

/**
 *
 * @author Daniel
 */
public class MobListener implements Listener {
    
    private final Spawner plugin;
    private EnderDragonListener enderDragonListener;
    
    public MobListener(Spawner plugin) {
        this.plugin = plugin;
        enderDragonListener = new EnderDragonListener(plugin);
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        
        Chunk chunk = event.getChunk();
        Entity[] entities = chunk.getEntities();

        for (Entity entity : entities) {
            if (entity == null || !(entity instanceof Monster ||  entity instanceof Animals)) continue;

            EntityDeathEvent newEvent = new EntityDeathEvent((LivingEntity) entity, new ArrayList());
            plugin.getServer().getPluginManager().callEvent(newEvent);
            entity.remove();
        }
        
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void MobDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        
        if (entity instanceof EnderDragon) {
            enderDragonListener.onDeath(event);
        }
        
        if (SpawnerList.isEmpty()) return;
        
        for (SpawnerControl mobs : SpawnerList) {
            if (!mobs.containsMob(event.getEntity().getUniqueId())) continue;
                
            if(event.getEntity().getKiller() != null){
                PlayerProfile playerProfile = Spawner.PlayerProfiles.get(event.getEntity().getKiller().getName());

                if (playerProfile != null && playerProfile.getBan()) {
                    event.setDroppedExp(0);
                }
            }
            
            mobs.removeMob(event.getEntity().getUniqueId());

            if (!mobs.hasMobs()) {
                plugin.Spawn(mobs);
            }

            return;
        }
        
    }
}
