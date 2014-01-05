/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.spawner.db;

import java.util.HashMap;
import me.stutiguias.spawner.db.connection.WALConnection;
import java.util.List;
import java.util.UUID;
import me.stutiguias.spawner.model.SpawnerControl;
import org.bukkit.Location;

/**
 *
 * @author Daniel
 */
public interface IDataQueries {

        void initTables(); // Init Tables
        Integer getFound(); // Found On Last Search
        WALConnection getConnection();
        
        boolean InsertSpawner(SpawnerControl area);
        List<SpawnerControl> getAreas();
        HashMap<String,Location> getSigns();
        List<UUID> LoadUUIDsTmp(SpawnerControl spawner);
        boolean RemoveSpawnerControl(String name);
        boolean RemoveSpawnerControlTmp(String name);
        boolean Delete(SpawnerControl area);
        boolean InsertTmpMob(String areaName,String uuid);
}
