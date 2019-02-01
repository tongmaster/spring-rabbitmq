package com.example.demo.util;
/**
 * Define supported DBMS to be use in database connection 
 * @author Manisa Makaphon (11/06/2014)
 * @See utility.database.DBConnect
 */
public enum DatabaseType {
	MySQL("com.mysql.jdbc.Driver", "jdbc:mysql:", 3306),
	POSTGRESQL("org.postgresql.Driver","jdbc:postgresql:", 5432);
	/** Class Name of database connection driver */
	private String driverClass;
	/** Protocol to be use in database connection */
	private String protocol;
	/** TCP/IP Port to be use in database connection */
	private int port;
	
	private DatabaseType(String driverClass, String protocol, int port) {
		this.driverClass = driverClass;
		this.protocol = protocol;
		this.port = port;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public String getProtocol() {
		return protocol;
	}

	public int getPort() {
		return port;
	}
	public static DatabaseType getDatabaseType(String typeName) {
		for (DatabaseType dbType : values()) {
			if (dbType.name().equalsIgnoreCase(typeName)) {
				return dbType;
			}
		}
		return null;
	}
}
