/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.spawner.db;

import me.stutiguias.spawner.db.connection.WALConnection;
import java.util.List;
import me.stutiguias.spawner.model.SpawnerControl;

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
        boolean Delete(SpawnerControl area);
}
