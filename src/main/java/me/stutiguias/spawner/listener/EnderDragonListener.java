/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.spawner.listener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import me.stutiguias.spawner.init.Spawner;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.PortalType;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Daniel
 */
public class EnderDragonListener implements Listener {
    
    private Spawner plugin;
    
    public void EnderDragonListerner(Spawner plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDragonEggTeleport(BlockFromToEvent event) {
        if (event.getBlock().getType() != Material.DRAGON_EGG || plugin.enderConfig.teleportEgg) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (!(event.getEntity() instanceof EnderDragon) || plugin.enderConfig.destroyBlocks) {
            return;
        }
        event.blockList().clear();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityCreatePortal(EntityCreatePortalEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof EnderDragon)) {
            return;
        }

        List<BlockState> blocks = new ArrayList(event.getBlocks());

        for (BlockState block : event.getBlocks()) {
            if (block.getType() == Material.DRAGON_EGG && !plugin.enderConfig.spawnEgg) {
                blocks.remove(block);
            }

            if (plugin.enderConfig.spawnPortal) {
                continue;
            }

            if (block.getType().getId() == 7 || block.getType().getId() == 119) {
                blocks.remove(block);
            } else if (block.getType().getId() == 0 || block.getType().getId() == 50) {
                blocks.remove(block);
            } else if (block.getType().getId() == 122 && plugin.enderConfig.spawnEgg) {
                blocks.remove(block);

                Location location = entity.getLocation();
                ItemStack item = new ItemStack(block.getType());

                entity.getWorld().dropItemNaturally(location, item);
            }
        }

        if (blocks.size() != event.getBlocks().size()) {
            event.setCancelled(true);

            LivingEntity newEntity = (LivingEntity) entity;
            PortalType type = event.getPortalType();
            EntityCreatePortalEvent newEvent;
            newEvent = new EntityCreatePortalEvent(newEntity, blocks, type);

            plugin.getServer().getPluginManager().callEvent(newEvent);

            if (!newEvent.isCancelled()) {
                for (BlockState blockState : blocks) {
                    int id = blockState.getTypeId();
                    byte data = blockState.getRawData();
                    blockState.getBlock().setTypeIdAndData(id, data, false);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        int droppedEXP = event.getDroppedExp();
        
        if (!(entity instanceof EnderDragon) || droppedEXP <= 0) return;

        if (plugin.enderConfig.dropExp) {
            if (plugin.enderConfig.useCustomExp) {
                event.setDroppedExp(plugin.enderConfig.customExp);
            }

            return;
        }

        if (plugin.enderConfig.useCustomExp) {
            droppedEXP = plugin.enderConfig.customExp;
        }

        event.setDroppedExp(0);

        List<Player> players = entity.getWorld().getPlayers();

        Location enderDragonLocation = entity.getLocation();

        for (Player player : players) {
            Location playerLocation = player.getLocation();

            int distance = (int) enderDragonLocation.distance(playerLocation);

            if (distance > plugin.enderConfig.expMaxDistance) {
                continue;
            }

            String playerName = player.getName().toUpperCase().toLowerCase();

            //if (plugin.data.bannedPlayers.containsKey(playerName)) {
            //    continue;
            //}

            //Timestamp time = plugin.data.players.get(playerName);

            //long requiredTime = new Date().getTime();
            //requiredTime -= plugin.expResetMinutes * 60000;

            //if (time != null && (time.getTime() > requiredTime)) {
            //    continue;
            //}

            if (!plugin.hasPermission(player,"enderspawn.exp")) {
                continue;
            }

            player.giveExp(droppedEXP);

            if (plugin.hasPermission(player, "enderspawn.unlimitedexp")) {
                continue;
            }

            Timestamp now = new Timestamp(new Date().getTime());
            //plugin.data.players.put(playerName, now);
        }

    }

}
