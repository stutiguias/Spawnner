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
  private UUID ownerUuid;
  private String ownerName;
  
  public SpawnerControl(String name, Location locationx,Location locationz, EntityType type, Integer quantd, Integer tempo)
  {
    this(name, locationx, locationz, type, quantd, tempo, null, null);
  }

  public SpawnerControl(String name, Location locationx,Location locationz, EntityType type, Integer quantd, Integer tempo, UUID ownerUuid, String ownerName)
  {
    this.moblist = new HashSet();
    this.name = name;
    this.locationx = locationx;
    this.locationz = locationz;
    this.type = type.name();
    this.qtd = quantd;
    this.time = tempo;
    this.location = null;
    this.ownerUuid = ownerUuid;
    this.ownerName = ownerName;
  }
  
  public SpawnerControl(String name, Location location, EntityType type, Integer quantd, Integer tempo)
  {
    this(name, location, type, quantd, tempo, null, null);
  }

  public SpawnerControl(String name, Location location, EntityType type, Integer quantd, Integer tempo, UUID ownerUuid, String ownerName)
  {
    this.moblist = new HashSet();
    this.name = name;
    this.location = location;
    this.type = type.name();
    this.qtd = quantd;
    this.time = tempo;
    this.ownerUuid = ownerUuid;
    this.ownerName = ownerName;
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
    return parseEntityType(this.type);
  }

  public static EntityType parseEntityType(String rawType) {
    String normalized = rawType.toUpperCase();
    if ("PIG_ZOMBIE".equals(normalized)) {
      normalized = "ZOMBIFIED_PIGLIN";
    }
    return EntityType.valueOf(normalized);
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

  public UUID getOwnerUuid() {
    return ownerUuid;
  }

  public String getOwnerName() {
    return ownerName;
  }

  public boolean hasOwner() {
    return ownerUuid != null;
  }

  public boolean isOwner(UUID playerUuid) {
    return ownerUuid != null && ownerUuid.equals(playerUuid);
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
