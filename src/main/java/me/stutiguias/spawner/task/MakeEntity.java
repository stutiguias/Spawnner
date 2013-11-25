/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.spawner.task;

import java.util.Random;
import me.stutiguias.spawner.init.Spawner;
import me.stutiguias.spawner.model.SpawnerControl;
import org.bukkit.Location;
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
        Entity ent;
        if(spawnerControl.getLocationX() == null) {
            ent = spawnerControl.getLocation().getWorld().spawnEntity(spawnerControl.getLocation(), spawnerControl.getType());
        }else{
            
            double xx = spawnerControl.getLocationX().getX();
            double yx = spawnerControl.getLocationY().getX();
            double xz = spawnerControl.getLocationX().getZ();
            double yz = spawnerControl.getLocationY().getZ();
      
            double x;
            if(xx > yx)
                 x = Random(xx,yx);
            else
                 x = Random(yx,xx);
            
            SendDebug(String.valueOf(x));         
            
            double z;
            
            if(yz > xz)
               z = Random(yz,xz);
            else
               z = Random(xz,yz);    
            
            SendDebug(String.valueOf(z));
             
            Location location = new Location(spawnerControl.getLocationX().getWorld(), x, spawnerControl.getLocationX().getY(), z);
            
            ent = location.getWorld().spawnEntity(location, spawnerControl.getType());
        }
        spawnerControl.addMob(ent.getUniqueId());
    }
    
    
    public double Random(double start,double end){
        long range = (long)end - (long)start + 1;
        long fraction = (long)(range * Spawner.r.nextDouble());
        return  (int)(fraction + start);    
    }
    
    public void SendDebug(String msg) {
        Spawner.logger.info(msg);
    }
}
