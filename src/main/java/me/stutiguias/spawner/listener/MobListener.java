/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.spawner.listener;

import me.stutiguias.spawner.listener.mobs.SkeletonListener;
import me.stutiguias.spawner.listener.mobs.ZombieListener;
import me.stutiguias.spawner.listener.mobs.EnderDragonListener;
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
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

/**
 *
 * @author Daniel
 */
public class MobListener implements Listener {
    
    private final Spawner plugin;
    private final EnderDragonListener enderDragonListener;
    private final SkeletonListener skeletonListener;
    private final ZombieListener zombieListener;
    
    public MobListener(Spawner plugin) {
        this.plugin = plugin;
        enderDragonListener = new EnderDragonListener(plugin);
        skeletonListener = new SkeletonListener(plugin);
        zombieListener = new ZombieListener(plugin);
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        
        Chunk chunk = event.getChunk();
        Entity[] entities = chunk.getEntities();

        for (Entity entity : entities) {
            if (entity == null || !(entity instanceof Monster ||  entity instanceof Animals)) continue;

            if (!handleTrackedMobDeath(entity, null)) continue;

            entity.remove();
        }
        
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onCombustEvent(EntityCombustEvent event) {
        if(!plugin.isTimeSpawnerMob(event.getEntity().getUniqueId())) return;
        if(event.getEntity() instanceof Skeleton) skeletonListener.onCombust(event);
        if(event.getEntity() instanceof Zombie) zombieListener.onCombust(event);
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void MobDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof EnderDragon && !plugin.config.DisableControlOverEnderDragon) {
            enderDragonListener.onDeath(event);
        }

        handleTrackedMobDeath(entity, event);

    }

    private boolean handleTrackedMobDeath(Entity entity, EntityDeathEvent event) {
        if (SpawnerList.isEmpty()) return false;

        for (SpawnerControl mobs : SpawnerList) {
            if (!mobs.containsMob(entity.getUniqueId())) continue;
            mobs.removeMob(entity.getUniqueId());

            if (event != null && event.getEntity().getKiller() != null) {
                PlayerProfile playerProfile = Spawner.PlayerProfiles.get(event.getEntity().getKiller().getName());

                if (playerProfile != null && playerProfile.getBan()) {
                    event.setDroppedExp(0);
                }
            }

            if (!mobs.hasMobs()) {
                plugin.Spawn(mobs);
            }

            return true;
        }

        return false;
    }
}
