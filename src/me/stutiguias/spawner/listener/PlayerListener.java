/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.spawner.listener;

import me.stutiguias.spawner.db.YAML.PlayerYmlDb;
import me.stutiguias.spawner.init.Spawner;
import me.stutiguias.spawner.model.PlayerProfile;
import me.stutiguias.spawner.model.SpawnerAreaCreating;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author Daniel
 */
public class PlayerListener implements Listener {
    
    private final Spawner plugin;
    private final PlayerYmlDb playerYmlDb;
    
    public PlayerListener(Spawner plugin) {
        this.plugin = plugin;
        playerYmlDb = new PlayerYmlDb(plugin);
    }
        
    @EventHandler(priority= EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
 
        if(plugin.config.UpdaterNotify && plugin.hasPermission(player,"tsp.update") && Spawner.update)
        {
          player.sendMessage(plugin.parseColor("&6An update is available: " + Spawner.name + ", a " + Spawner.type + " for " + Spawner.version + " available at " + Spawner.link));
          player.sendMessage(plugin.parseColor("&6Type /sp update if you would like to automatically update."));
        }
        
        if(!playerYmlDb.Exist(player.getName()))
        {
           playerYmlDb.Create(new PlayerProfile(player.getName(), Boolean.FALSE, 0 ));
        }
        
        Spawner.PlayerProfiles.put(player.getName(),playerYmlDb.LoadPlayer(player.getName()));
    }
        
    @EventHandler(priority= EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Spawner.PlayerProfiles.remove(event.getPlayer().getName());
    }
    
    @EventHandler
    public void PlayerInteract(PlayerInteractEvent event){
        if( !event.hasItem() 
         || !event.getItem().hasItemMeta() 
         || !event.getItem().getItemMeta().hasDisplayName()
         || !event.getItem().getItemMeta().getDisplayName().equals("TimeSpawner Wand")) return;
        
        Player player = event.getPlayer();
        if(!Spawner.SpawnerCreating.containsKey(player))
            Spawner.SpawnerCreating.put(player, new SpawnerAreaCreating());
        
        if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Spawner.SpawnerCreating.get(player).locationLeft = event.getClickedBlock().getLocation();
            player.sendMessage(plugin.parseColor("&6First Spot Set!"));
            event.setCancelled(true);
        }
        
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Spawner.SpawnerCreating.get(player).locationRight = event.getClickedBlock().getLocation();
            player.sendMessage(plugin.parseColor("&6Second Spot Set!"));
            event.setCancelled(true);            
        }
        
    }
}
