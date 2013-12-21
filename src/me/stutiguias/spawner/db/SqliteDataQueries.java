/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.spawner.db;

import me.stutiguias.spawner.db.connection.WALDriver;
import me.stutiguias.spawner.db.connection.WALConnection;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import me.stutiguias.spawner.init.Spawner;

/**
 *
 * @author Daniel
 */
public class SqliteDataQueries extends Queries {

    public SqliteDataQueries(Spawner plugin) {
        super(plugin);
        initTables();
    }

    @Override
    public WALConnection getConnection() {
            try {
                    Driver driver = (Driver) Class.forName("org.sqlite.JDBC").newInstance();
                    WALDriver jDriver = new WALDriver(driver);
                    DriverManager.registerDriver(jDriver);
                    connection = new WALConnection(DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + File.separator + "data.db"));
                    return connection;
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException e) {
                    Spawner.logger.log(Level.SEVERE, "{0} Exception getting SQLite WALConnection", plugin.prefix);
                    Spawner.logger.warning(e.getMessage());
            }
            return null;
    }
	
    private boolean tableExists(String tableName) {
        boolean exists = false;
        WALConnection conn = getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
                st = conn.prepareStatement("SELECT name FROM sqlite_master WHERE type = 'table' and name LIKE ?");
                st.setString(1, tableName);
                rs = st.executeQuery();
                while (rs.next()) {
                        exists = true;
                }
        } catch (SQLException e) {
                Spawner.logger.log(Level.WARNING, "{0} Unable to check if table exists: {1}", new Object[]{plugin.prefix, tableName});
                Spawner.logger.warning(e.getMessage());
        } finally {
                closeResources(conn, st, rs);
        }
        return exists;
    }

    @Override
    public final void initTables() {
                File dbFile = new File(plugin.getDataFolder() + File.separator +  "data.db");
                if(!dbFile.exists()) {
                    try {
                        dbFile.createNewFile();
                    } catch (IOException ex) {
                        Spawner.logger.log(Level.WARNING,"{0} Can`t create file db", plugin.prefix);
                    }
                }
                if (!tableExists("TS_Players")) {
			Spawner.logger.log(Level.INFO, "{0} Creating table TS_Players", plugin.prefix);
			executeRawSQL("CREATE TABLE TS_Players (id INTEGER PRIMARY KEY, name VARCHAR(255), banned INTEGER);");
		}
                if (!tableExists("TS_TmpMob")) {
			Spawner.logger.log(Level.INFO, "{0} Creating table TS_TmpMob", plugin.prefix);
			executeRawSQL("CREATE TABLE TS_TmpMob (id INTEGER PRIMARY KEY, name VARCHAR(255), uuid VARCHAR(255));");
		}
		if (!tableExists("TS_Spawners")) {
			Spawner.logger.log(Level.INFO, "{0} Creating table TS_Spawners", plugin.prefix);
			executeRawSQL("CREATE TABLE TS_Spawners (id INTEGER PRIMARY KEY, name VARCHAR(255), location VARCHAR(255), locationx VARCHAR(255), locationz VARCHAR(255), type VARCHAR(255), qtd INTEGER, time INTEGER);");
		}
                if (!tableExists("TS_DbVersion")) {
                        Spawner.logger.log(Level.INFO, "{0} Creating table TS_DbVersion", plugin.prefix);
                        executeRawSQL("CREATE TABLE TS_DbVersion (id INTEGER PRIMARY KEY, dbversion INTEGER);");
                        executeRawSQL("INSERT INTO TS_DbVersion (dbversion) VALUES (1)");
                }
    }
    
}
