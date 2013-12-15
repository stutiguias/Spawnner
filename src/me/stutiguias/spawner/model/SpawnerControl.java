package me.stutiguias.spawner.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

public class SpawnerControl  implements Serializable
{
  private final Set<UUID> moblist;
  private String type;
  private Integer qtd;
  private Integer time;
  private final String name;
  
  private Location location;
  private Location locationx;
  private Location locationz;
  
  public SpawnerControl(String name, Location locationx,Location locationz, EntityType type, Integer quantd, Integer tempo)
  {
    this.moblist = new HashSet();
    this.name = name;
    this.locationx = locationx;
    this.locationz = locationz;
    this.type = type.name();
    this.qtd = quantd;
    this.time = tempo;
    this.location = null;
  }
  
  public SpawnerControl(String name, Location location, EntityType type, Integer quantd, Integer tempo)
  {
    this.moblist = new HashSet();
    this.name = name;
    this.location = location;
    this.type = type.name();
    this.qtd = quantd;
    this.time = tempo;
  }

  public Location getLocation() {
    return location;
  }

  public Location getLocationX() {
    return locationx;
  }
  
  public Location getLocationZ() {
    return locationz;
  }
  
  public Set<UUID> getMobs() {
    return this.moblist;
  }
  
  public void setMobs(Set<UUID> mobs) {
     this.moblist.addAll(mobs);
  }

  public EntityType getType() {
    return EntityType.valueOf(this.type);
  }

  public Integer getQuantd() {
    return this.qtd;
  }
  public void addMob(UUID id) {
    this.moblist.add(id);
  }
  public void removeMob(UUID id) {
    this.moblist.remove(id);
  }
  public boolean containsMob(UUID id) {
    return this.moblist.contains(id);
  }
  public boolean hasMobs() {
    return !this.moblist.isEmpty();
  }

  public String getName() {
    return this.name;
  }

  public void setQuantd(Integer quantd) {
    this.qtd = quantd;
  }

  public void setType(EntityType type) {
    this.type = type.name();
  }

  public void setLocation(Location location) {
    this.location = location;
  }
  public void cleanMobs() {
    this.moblist.clear();
  }

  public void setTime(Integer tempo) {
    this.time = tempo;
  }
  
  public Integer getTime() {
    return this.time;
  }
  
  public World getWorld() {
      if(this.location == null){
          return locationx.getWorld();
      }else {
          return location.getWorld();
      }
  }
}
