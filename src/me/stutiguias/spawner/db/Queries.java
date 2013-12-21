/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.spawner.db;

import me.stutiguias.spawner.db.connection.WALConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import me.stutiguias.spawner.init.Spawner;
import me.stutiguias.spawner.init.Util;
import me.stutiguias.spawner.model.SpawnerControl;

/**
 *
 * @author Daniel
 */
public class Queries extends Util implements IDataQueries {
        
    protected WALConnection connection;
    protected Integer found;
    
    public Queries(Spawner plugin) {
        super(plugin);
    }

    @Override
    public void initTables() {
        throw new UnsupportedOperationException("Implement On Children.");
    }

    @Override
    public Integer getFound() {
        return found;
    }
        
    @Override
    public WALConnection getConnection() {
        throw new UnsupportedOperationException("Implement On Children.");
    }
    
    public void closeResources(WALConnection conn, Statement st, ResultSet rs) {
        if (null != rs) {
            try {
                rs.close();
            } catch (SQLException e) {
            }
        }
        if (null != st) {
            try {
                st.close();
            } catch (SQLException e) {
            }
        }
        if (null != conn) conn.close();
    }
        
    public int tableVersion() {
        int version = 0;
        WALConnection conn = getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
                st = conn.prepareStatement("SELECT dbversion FROM TS_Spawners");
                rs = st.executeQuery();
                while (rs.next()) {
                        version = rs.getInt("dbversion");
                }
        } catch (SQLException e) {
                Spawner.logger.log(Level.WARNING, "{0} Unable to check if table version ", plugin.prefix);
                Spawner.logger.warning(e.getMessage());
        } finally {
                closeResources(conn, st, rs);
        }
        return version;
    }
    
    public void executeRawSQL(String sql) {
        WALConnection conn = getConnection();
        Statement st = null;
        ResultSet rs = null;

        try {
            st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {
            Spawner.logger.log(Level.WARNING, "{0} Exception executing raw SQL {1}", new Object[]{plugin.prefix, sql});
            Spawner.logger.warning(e.getMessage());
        } finally {
            closeResources(conn, st, rs);
        }
    }

    @Override
    public boolean InsertSpawner(SpawnerControl area) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SpawnerControl> getAreas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
