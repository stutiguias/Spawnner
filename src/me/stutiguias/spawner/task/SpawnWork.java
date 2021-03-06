/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.spawner.task;

import java.util.List;
import java.util.logging.Level;
import me.stutiguias.spawner.init.Spawner;
import me.stutiguias.spawner.model.SpawnerControl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Daniel
 */
public class SpawnWork implements Runnable {

    private final SpawnerControl spawnerControl;
    private final Spawner plugin;
    
    public SpawnWork(Spawner plugin,SpawnerControl spawner) {
        this.spawnerControl = spawner;
        this.plugin = plugin;
    }
    
    @Override
    public void run() {
        try {
            Spawner.SpawnerList.remove(spawnerControl);
            
            if(plugin.config.ShowDebug) {
                Spawner.logger.log(Level.INFO, "{0} Spawning {1}",new Object[]{ plugin.prefix,spawnerControl.getName() });
            }
            
            for (int i = 1; i <= spawnerControl.getQuantd(); i++) {
                if(!MakeEntity()) break;
            }

            Spawner.SpawnerList.add(spawnerControl);
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    
    public boolean MakeEntity() {
        Entity ent;
        String worldname;
        World world;
        
        if(spawnerControl.getLocationZ() == null || spawnerControl.getLocationX() == null) {
            worldname = spawnerControl.getLocation().getWorld().getName();
            
            if(worldname == null) {
                Spawner.logger.log(Level.WARNING, "{0} World Not found", plugin.prefix);
                return false;
            }   
          
            world = Bukkit.getWorld(worldname);
            
            if(!isPlayerNear(world, spawnerControl.getLocation())) return false;
            
            ent = plugin.getWorld(worldname).spawnEntity(spawnerControl.getLocation(), spawnerControl.getType());
        }else{
            
            double xx = spawnerControl.getLocationX().getX();
            double yx = spawnerControl.getLocationZ().getX();
            double xz = spawnerControl.getLocationX().getZ();
            double yz = spawnerControl.getLocationZ().getZ();
      
            double x,z;
            
            if(xx > yx)
                 x = Random(xx,yx);
            else
                 x = Random(yx,xx);    
            
            if(yz > xz)
               z = Random(yz,xz);
            else
               z = Random(xz,yz);    
             
            Location location = new Location(spawnerControl.getLocationX().getWorld(), x, spawnerControl.getLocationX().getY() + 1, z);
            
            worldname = location.getWorld().getName();
                               
            if(worldname == null) {
                Spawner.logger.log(Level.WARNING, "{0} World Not found",new Object[] { plugin.prefix });
                return false;
            }  
            
            world = plugin.getWorld(worldname);
            
            if(!isPlayerNear(world, spawnerControl.getLocationX())) return false;

            ent = world.spawnEntity(location, spawnerControl.getType());
            
        }
        FixEntity((LivingEntity)ent);
        spawnerControl.addMob(ent.getUniqueId());
        return true;
    }
    
        
    public double Random(double start,double end){
        long range = (long)end - (long)start + 1;
        long fraction = (long)(range * Spawner.r.nextDouble());
        return  (int)(fraction + start);    
    }
    
    public boolean isPlayerNear(World world,Location spawnLocation) {
        
        boolean nearbyPlayer = false;
        
        List<Player> players = world.getPlayers();
        if (players.size() <= 0) nearbyPlayer = false;

        for (Player player : players) {

            Location playerLocation = player.getLocation();

            int x = playerLocation.getBlockX();
            int z = playerLocation.getBlockZ();

            Location relativeLocation = new Location(world, x, spawnLocation.getY(), z);

            if (spawnLocation.distance(relativeLocation) > 160) continue;

            nearbyPlayer = true;
            break;
        }
        
        if(nearbyPlayer == false) {
            plugin.Spawn(spawnerControl);
            if(plugin.config.ShowDebug)
            Spawner.logger.log(Level.INFO, "{0} Stop spawn don't find any player near ", new Object[]{ plugin.prefix });
        }
        
        return nearbyPlayer;
    }
    
    public void FixEntity(LivingEntity livingEntity) {
        livingEntity.setRemoveWhenFarAway(false);
        if(livingEntity instanceof Skeleton) Skeleton((Skeleton)livingEntity);
        if(livingEntity instanceof PigZombie) PigMan((PigZombie)livingEntity);
        if(livingEntity instanceof Zombie) Zombie((Zombie)livingEntity);
    }
    
    public void Skeleton(Skeleton skeleton) {
        skeleton.getEquipment().setItemInMainHand(new ItemStack(Material.BOW));
        if(!plugin.config.DisableCustomName) skeleton.setCustomName(plugin.parseColor(plugin.skeletonConfig.name));
    }
    
    public void PigMan(PigZombie pigzombie) {
        pigzombie.getEquipment().setItemInMainHand(new ItemStack(Material.GOLD_SWORD));
        if(plugin.pigZombieConfig.Aggressive) pigzombie.setAngry(true);
    }    
    
    public void Zombie(Zombie zombie) {
       if(!plugin.config.DisableCustomName) zombie.setCustomName(plugin.parseColor(plugin.zombieConfig.name));
    }
}
