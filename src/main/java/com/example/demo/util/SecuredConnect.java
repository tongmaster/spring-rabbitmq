package com.example.demo.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SecuredConnect {
	private static final File ROOT;

	private static final HashMap<String, ConnectionBean> connectionMap;
	private static final HashMap<String, ConnectionBean> connectionUserMap;
	static {
		ROOT = SecuredFile.getSecuredDirectory();
		connectionMap = new HashMap<>();
		connectionUserMap = new HashMap<>();
		// Read from encrypted file
		ArrayList<ConnectionBean> beanList = (new DBConnectionFile()).getList();
		for (ConnectionBean bean : beanList) {
			String userStr = bean.getUserName() + "@" + bean.getHostName();
			connectionMap.put(bean.getApplicationName(), bean);
			if (bean.getUserName() != null && bean.getUserName().trim().length() > 0) {
				connectionUserMap.put(userStr, bean);
			}
		}
		File[] propertyFileList = ROOT.listFiles();
		for (File propertyFile : propertyFileList) {
			if (!propertyFile.getAbsolutePath().endsWith(".properties")) {
				continue;
			}

			try {
				ConnectionBean bean = new ConnectionBean(propertyFile);
				String userStr = bean.getUserName() + "@" + bean.getHostName();
				if (connectionMap.get(bean.getApplicationName()) == null) {
					connectionMap.put(bean.getApplicationName(), bean);
				}
				if (bean.getUserName() != null && bean.getUserName().trim().length() > 0) {
					if (connectionUserMap.get(userStr) == null) {
						connectionUserMap.put(userStr, bean);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		String keyPass = null;
		String storePass = null;

		File zipPassFile = new File(ROOT, "SSL/StorePass");
		if (zipPassFile.exists()) {
			try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zipPassFile))) {
				
				ZipEntry entry = zin.getNextEntry();
				byte[] b = new byte[(int) entry.getSize()];
				zin.read(b);
				ByteArrayInputStream byteIn = new ByteArrayInputStream(b);
				Properties tmpProp = new Properties();
				tmpProp.load(byteIn);
				keyPass = tmpProp.getProperty("KEY_STORE").trim();
				storePass = tmpProp.getProperty("TRUST_STORE").trim();
				System.setProperty("javax.net.ssl.keyStore", ROOT.getAbsolutePath() + "/SSL/MySQLKey");
				System.setProperty("javax.net.ssl.keyStorePassword", keyPass);
				System.setProperty("javax.net.ssl.trustStore", ROOT.getAbsolutePath() + "/SSL/MyTrustStore");
				System.setProperty("javax.net.ssl.trustStorePassword", storePass);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * Create {@link java.sql.Connection} by properties specified in properties
	 * file
	 * 
	 * @param applicationName
	 *            Assigned application name
	 * @return an instance of database connection
	 * @throws DBException
	 */

	public static Connection createConnection(String applicationName) throws DBException {

		SecuredConnect secure;
		try {
			secure = new SecuredConnect(applicationName, null, null, null);
		} catch (ClassNotFoundException | SQLException e) {
			throw DBException.newInstance(e.getMessage());
		}

		return secure.getConnection();
	}

	/**
	 * Create {@link java.sql.Connection} by properties specified in properties
	 * file
	 * 
	 * @param applicationName
	 *            Assigned application name
	 * @param charSet
	 *            Character Encoding
	 * @return an instance of database connection
	 * @throws DBException
	 */

	public static Connection createConnection(String applicationName, CharacterSet charSet) throws DBException {

		SecuredConnect secure;
		try {
			secure = new SecuredConnect(applicationName, null, null, charSet);
		} catch (ClassNotFoundException | SQLException e) {
			throw DBException.newInstance(e.getMessage());
		}

		return secure.getConnection();
	}

	/**
	 * Create {@link java.sql.Connection} by properties specified in properties
	 * file with user name and password parsed to the method instead of default
	 * user name and password
	 * 
	 * @param applicationName
	 * @param userName
	 * @param password
	 * @param charSet
	 * @return
	 * @throws DBException
	 */
	public static Connection createConnection(String applicationName, String userName, String password,
			CharacterSet charSet) throws DBException {
		SecuredConnect secure;
		try {
			secure = new SecuredConnect(applicationName, userName, password, charSet);
		} catch (ClassNotFoundException | SQLException e) {
			throw DBException.newInstance(e.getMessage());
		}
		return secure.getConnection();

	}

	/**
	 * Create {@link java.sql.Connection} by properties specified in properties
	 * file with user name and password parsed to the method instead of default
	 * user name and password
	 * 
	 * @param applicationName
	 *            Assigned application name
	 * @param userName
	 * @param password
	 * @return an instance of database connection
	 * @throws DBException
	 */
	public static Connection createConnection(String applicationName, String userName, String password)
			throws DBException {

		SecuredConnect secure;
		try {
			secure = new SecuredConnect(applicationName, userName, password, null);
		} catch (ClassNotFoundException | SQLException e) {
			throw DBException.newInstance(e.getMessage());
		}
		return secure.getConnection();
	}
	
	/** 
	 * create by tongmaster
	 */
	public static ConnectionBean createConnection(String applicationName,boolean pwd)
			throws DBException {

		SecuredConnect secure;
		try {
			 secure = new SecuredConnect(applicationName, null, null, null);
		} catch (ClassNotFoundException | SQLException e) {
			throw DBException.newInstance(e.getMessage());
		}
		return secure.getConnectionBean();
	}
	
	private Connection commonConnection = null;
	private ConnectionBean commonConnectionBean = null;
	private SecuredConnect(String applicationName, String userName, String password, CharacterSet charSet)
			throws DBException, ClassNotFoundException, SQLException {
		ConnectionBean connBean = null;
		if (applicationName.indexOf("@") > 0) {
			connBean = connectionUserMap.get(applicationName);
			commonConnectionBean = connectionMap.get(applicationName);
			System.out.println(connBean.getPassword());
		} else {
			connBean = connectionMap.get(applicationName);
			commonConnectionBean = connectionMap.get(applicationName); 
			System.out.println(connBean.getPassword());
		}
		if (connBean == null) {
			throw DBException.newInstance(String.format("Connection property for %s not found", applicationName));
		}
		DatabaseType dbType = DatabaseType.getDatabaseType(connBean.getVendor());
		if (dbType == null) {
			throw DBException.newInstance(String.format("Databse vendor is unknown %s", connBean.getVendor()));
		}

		ConnectionBean copyBean = connBean.copyBean();

		if (charSet != null) {
			copyBean.setEncoding(charSet.name());
		}

		Class.forName(dbType.getDriverClass());
		String connectionString = copyBean.getConnectionString();
		// System.out.printf("Connection String=%s\n", connectionString);
		if (userName == null) {
			userName = copyBean.getUserName();
		}
		if (password == null) {
			password = copyBean.getPassword();
		}

		commonConnection = DriverManager.getConnection(connectionString, userName, password);
	}
	
	

	private Connection getConnection() {
		return commonConnection;
	}
	
	private ConnectionBean getConnectionBean() {
		return commonConnectionBean;
	}

}
