package com.example.demo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class ConnectionBean {
	private String applicationName;
	private String protocol;
	private String vendor;
	private String hostName;
	private String portNo;
	private String databaseName;
	private String encoding;
	private String zeroDateTime;
	private Boolean requireSSL;
	private String userName;
	private String password;
	private final String urlPatternStr = "(?<protocol>\\w+):(?<vendor>\\w+)://(?<hostName>.*):(?<portNo>\\d*)/?(?<databaseName>\\w+)?(\\?characterEncoding=(?<characterEncoding>\\w+))?.*";

	protected ConnectionBean(File propertyFile) throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(propertyFile));
		setApplicationName(propertyFile.getName().replaceAll(".properties", ""));
		Matcher matcher = Pattern.compile(urlPatternStr).matcher(prop.getProperty("DB_URL"));
		if (matcher.find()) {
			setProtocol(defaultValue(matcher.group("protocol"), "jdbc"));
			setVendor(defaultValue(matcher.group("vendor"), prop.getProperty("DB_TYPE")));
			setHostName(defaultValue(matcher.group("hostName"), prop.getProperty("DB_HOST")));
			setPortNo(defaultValue(matcher.group("portNo"), prop.getProperty("DB_PORT")));
			setDatabaseName(defaultValue(matcher.group("databaseName"), prop.getProperty("DB_NAME")));
			setEncoding(defaultValue(matcher.group("characterEncoding"), prop.getProperty("DB_ENCODE")));
			setZeroDateTime(prop.getProperty("ZERO_DATETIME"));
			if (prop.getProperty("DB_SSL") != null) {
				setRequireSSL(prop.getProperty("DB_SSL").equalsIgnoreCase("TRUE"));
			} else if (prop.getProperty("LDAP") != null) {
				setRequireSSL(prop.getProperty("LDAP").equalsIgnoreCase("TRUE"));
			}
			setUserName(prop.getProperty("DB_USER"));
			setPassword(prop.getProperty("DB_PASSWORD"));
		}
	}

	protected ConnectionBean(Node connectionNode) {
		NodeList nodeList = connectionNode.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				
				String nodeName = node.getNodeName();
				String nodeValue = node.getTextContent();
				
				switch (nodeName) {
				case "connection_name":
						
					setApplicationName(nodeValue);
					break;
				case "protocol":
					setProtocol(nodeValue);
					break;
				case "vendor":
					setVendor(nodeValue);
					break;
				case "host_name":
					setHostName(nodeValue);
					break;
				case "port_no":
					setPortNo(nodeValue);
					break;
				case "database_name":
					setDatabaseName(nodeValue);
					break;
				case "encoding":
					setEncoding(nodeValue);
					break;
				case "SSL":
					setRequireSSL(nodeValue != null && nodeValue.equalsIgnoreCase("true"));
					break;
				case "user_name":
					setUserName(nodeValue);
					break;
				case "password":
					setPassword(nodeValue);
					break;
				case "zero_datetime":
					setZeroDateTime(nodeValue);
					break;

				}
			}
		}		
	}

	protected ConnectionBean(String[] strContent) {
		setApplicationName(strContent[0]);
		setProtocol(strContent[1]);
		setVendor(strContent[2]);
		setHostName(strContent[3]);
		setPortNo(strContent[4]);
		setDatabaseName(strContent[5]);
		setEncoding(strContent[6]);
		setZeroDateTime(strContent[7]);
		setRequireSSL(strContent[8].equalsIgnoreCase("TRUE"));
		setUserName(strContent[9]);
		setPassword(strContent[10]);
	}
	protected ConnectionBean() {
		
	}
	private String defaultValue(String str, String defaultStr) {
		return str == null || str.isEmpty() ? defaultStr : str;
	}

	protected String getConnectionString() {
		StringBuilder stb = new StringBuilder();
		stb = stb.append(protocol).append(":").append(vendor).append("://").append(hostName).append(":").append(portNo)
				.append("/").append(databaseName);
		StringBuilder opt = new StringBuilder();
		if (encoding != null && !encoding.isEmpty()) {
			opt = opt.append("?characterEncoding=").append(encoding);
		}
		if (zeroDateTime != null && !zeroDateTime.isEmpty()) {
			if (opt.length() > 0) {
				opt = opt.append("&");
			}
			opt = opt.append("zeroDateTimeBehavior=").append(zeroDateTime);
		}
		if (requireSSL != null && requireSSL.booleanValue()) {
			if (opt.length() > 0) {
				opt = opt.append("&");
			}
			opt = opt.append("useSSL=true&requireSSL=true");
		}
		return stb.append(opt).toString();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getPortNo() {
		return portNo;
	}

	public void setPortNo(String portNo) {
		this.portNo = portNo;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getZeroDateTime() {
		return zeroDateTime;
	}

	public void setZeroDateTime(String zeroDateTime) {
		this.zeroDateTime = zeroDateTime;
	}

	public Boolean getRequireSSL() {
		return requireSSL;
	}

	public void setRequireSSL(Boolean requireSSL) {
		this.requireSSL = requireSSL;
	}

	public String getUrlPatternStr() {
		return urlPatternStr;
	}
	protected ConnectionBean copyBean() {
	ConnectionBean copy = new ConnectionBean();
	copy.setApplicationName(applicationName);
	copy.setDatabaseName(databaseName);
	copy.setEncoding(encoding);
	copy.setHostName(hostName);
	copy.setPassword(password);
	copy.setPortNo(portNo);
	copy.setProtocol(protocol);
	copy.setRequireSSL(requireSSL);
	copy.setUserName(userName);
	copy.setVendor(vendor);
	copy.setZeroDateTime(zeroDateTime);
	return copy;
	}
	@Override
	public String toString() {
		return "ConnectionBean [applicationName=" + applicationName + ", protocol=" + protocol + ", vendor=" + vendor
				+ ", hostName=" + hostName + ", portNo=" + portNo + ", databaseName=" + databaseName + ", encoding="
				+ encoding + ", zeroDateTime=" + zeroDateTime + ", requireSSL=" + requireSSL + ", userName=" + userName
				+ ", password=" + password + "]";
	}

}
