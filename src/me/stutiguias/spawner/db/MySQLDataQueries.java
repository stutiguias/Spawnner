package me.stutiguias.spawner.db;

import me.stutiguias.spawner.db.connection.WALConnectionPool;
import me.stutiguias.spawner.db.connection.WALConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import me.stutiguias.spawner.init.Spawner;

public class MySQLDataQueries extends Queries {

        private WALConnectionPool pool;
        
	public MySQLDataQueries(Spawner plugin, String dbHost, String dbPort, String dbUser, String dbPass, String dbName) {
		super(plugin);
                try {
                        Spawner.logger.log(Level.INFO, "{0} Starting pool....", plugin.prefix);
                        pool = new WALConnectionPool("com.mysql.jdbc.Driver", "jdbc:mysql://"+ dbHost +":"+ dbPort +"/"+ dbName, dbUser, dbPass);
                }catch(InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
                        Spawner.logger.log(Level.WARNING, "{0} Exception getting mySQL WALConnection", plugin.prefix);
			Spawner.logger.warning(e.getMessage());
                }
	}

        @Override
	public WALConnection getConnection() {
		try {
			return pool.getConnection();
		} catch (SQLException e) {
			Spawner.logger.log(Level.WARNING, "{0} Exception getting mySQL WALConnection", plugin.prefix);
			Spawner.logger.warning(e.getMessage());
		}
		return null;
	}
        
	public boolean tableExists(String tableName) {
		boolean exists = false;
		WALConnection conn = getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SHOW TABLES LIKE ?");
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
	public void initTables() {
		if (!tableExists("TS_Players")) {
			Spawner.logger.log(Level.INFO, "{0} Creating table TS_Players", plugin.prefix);
			executeRawSQL("CREATE TABLE TS_Players (id INT NOT NULL AUTO_INCREMENT, PRIMARY KEY(id), name VARCHAR(255), banned INT);");
		}
                if (!tableExists("TS_TmpMob")) {
			Spawner.logger.log(Level.INFO, "{0} Creating table TS_TmpMob", plugin.prefix);
			executeRawSQL("CREATE TABLE TS_TmpMob (id INT NOT NULL AUTO_INCREMENT, PRIMARY KEY(id), name VARCHAR(255), uuid VARCHAR(255));");
		}
		if (!tableExists("TS_Spawners")) {
			Spawner.logger.log(Level.INFO, "{0} Creating table TS_Spawners", plugin.prefix);
			executeRawSQL("CREATE TABLE TS_Spawners (id INT NOT NULL AUTO_INCREMENT, PRIMARY KEY(id), name VARCHAR(255), location VARCHAR(255), locationx VARCHAR(255), locationz VARCHAR(255), type VARCHAR(255), qtd INT, time INT );");
		}
                if (!tableExists("TS_DbVersion")) {
                        Spawner.logger.log(Level.INFO, "{0} Creating table TS_DbVersion", plugin.prefix);
                        executeRawSQL("CREATE TABLE TS_DbVersion (id INT NOT NULL AUTO_INCREMENT, PRIMARY KEY(id), dbversion INT);");
                        executeRawSQL("INSERT INTO TS_DbVersion (dbversion) VALUES (1)");
                }                
	}
    
}
