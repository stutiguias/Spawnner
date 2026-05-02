/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.spawner.db;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.stutiguias.spawner.db.YAML.SignYmlDb;
import me.stutiguias.spawner.db.YAML.SpawnerYmlDb;
import me.stutiguias.spawner.db.connection.WALConnection;
import me.stutiguias.spawner.init.Spawner;
import static me.stutiguias.spawner.init.Spawner.SignDir;
import static me.stutiguias.spawner.init.Spawner.SignLocation;
import static me.stutiguias.spawner.init.Spawner.SpawnerDir;
import static me.stutiguias.spawner.init.Spawner.SpawnerList;
import me.stutiguias.spawner.init.Util;
import me.stutiguias.spawner.model.SpawnerControl;
import org.bukkit.Location;

/**
 *
 * @author Daniel
 */
public class YamlDataQueries extends Util implements IDataQueries {

    public YamlDataQueries(Spawner plugin) {
        super(plugin);
    }

    @Override
    public void initTables() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer getFound() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public WALConnection getConnection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean InsertSpawner(SpawnerControl area) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SpawnerControl> getAreas() {
        List<SpawnerControl> spawnerControls = new ArrayList();
        File folder = new File(SpawnerDir);
        File[] listOfFiles = folder.listFiles();
        
        for (File file : listOfFiles) {
            if (file.isFile()) {
                SpawnerControl spawner = new SpawnerYmlDb(plugin).LoadSpawnerControl(file.getName());
                if(spawner != null) {
                    spawnerControls.add(spawner);
                }else{
                    file.delete();
                }
            }
        }
        
        return spawnerControls;
    }

    @Override
    public HashMap<String, Location> getSigns() {
        HashMap<String, Location> signs = new HashMap<>();

        File folder = new File(SignDir);
        File[] listOfFiles = folder.listFiles();
        
        for (File file : listOfFiles) {
            if (file.isFile()) {
                signs.put( file.getName().replace(".yml","") , new SignYmlDb(plugin).LoadSign(file.getName()));
            }
        }
        
        return signs;
    }

    @Override
    public List<UUID> LoadUUIDsTmp(SpawnerControl spawner) {
        if(!plugin.tmpYmlDb.Exist(spawner)) return null;
        return plugin.tmpYmlDb.LoadUUIDs(spawner);
    }

    @Override
    public boolean RemoveSpawnerControl(String name) {
       return plugin.spawnerYmlDb.RemoveSpawnerControl(name);
    }

    @Override
    public boolean RemoveSpawnerControlTmp(String name) {
        return plugin.tmpYmlDb.RemoveSpawnerControl(name);
    }

    @Override
    public boolean Delete(SpawnerControl area) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean InsertTmpMob(String areaName, String uuid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
