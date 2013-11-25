package me.stutiguias.spawner.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class SpawnerControl  implements Serializable
{
  private Double z;
  private Double y;
  private Double x;
  private Float pitch;
  private Float yaw;
  private String world;
  private Set<UUID> moblist;
  private String type;
  private Integer qtd;
  private Integer time;
  private String name;
  
  private Location locationx;
  private Location locationy;
  
  public SpawnerControl(String name, Location locationx,Location locationy, EntityType type, Integer quantd, Integer tempo)
  {
    this.moblist = new HashSet();
    this.name = name;
    this.locationx = locationx;
    this.locationy = locationy;
    this.type = type.name();
    this.qtd = quantd;
    this.time = tempo;
  }
  
  public SpawnerControl(String name, Location location, EntityType type, Integer quantd, Integer tempo)
  {
    this.moblist = new HashSet();
    this.name = name;
    this.z = Double.valueOf(location.getZ());
    this.y = Double.valueOf(location.getY());
    this.x = Double.valueOf(location.getX());
    this.pitch = Float.valueOf(location.getPitch());
    this.yaw = Float.valueOf(location.getYaw());
    this.world = location.getWorld().getName();
    this.type = type.name();
    this.qtd = quantd;
    this.time = tempo;
  }

  public Location getLocation() {
    return new Location(Bukkit.getWorld(this.world), this.x.doubleValue(), this.y.doubleValue(), this.z.doubleValue(), this.yaw.floatValue(), this.pitch.floatValue());
  }

  public Location getLocationX() {
    return locationx;
  }
  
  public Location getLocationY() {
    return locationy;
  }
  
  public Set<UUID> getMobs() {
    return this.moblist;
  }

  public EntityType getType() {
    return EntityType.fromName(this.type);
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
    this.type = type.getName();
  }

  public void setLocation(Location location) {
    this.z = Double.valueOf(location.getZ());
    this.y = Double.valueOf(location.getY());
    this.x = Double.valueOf(location.getX());
    this.pitch = Float.valueOf(location.getPitch());
    this.yaw = Float.valueOf(location.getYaw());
    this.world = location.getWorld().getName();
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
}
