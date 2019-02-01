package com.example.demo.util;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DBConnectionFile {
	private ArrayList<ConnectionBean> beanList;
	private ConnectionBean bean;
	protected DBConnectionFile() {
		beanList = new ArrayList<>();
		bean = new ConnectionBean();
		SecuredFile securedFile = new SecuredFile();
		try {
			String cfgContent = securedFile.getDecryptedConfig();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(cfgContent)));
			NodeList nodeList = doc.getElementsByTagName("connection");
			int length = nodeList.getLength();
			for (int i = 0; i < length; i++) {
				Node connection = nodeList.item(i);
			
				ConnectionBean bean = new ConnectionBean(connection);
				//System.out.println(">>> "+bean.getPassword());
				beanList.add(bean);
			}
			/*
			 * String[] strLines = cfgContent.split("\n"); for (String line :
			 * strLines) { String[] str = line.split("\\|"); ConnectionBean bean
			 * = new ConnectionBean(str); beanList.add(bean); }
			 */

		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | IOException | ParserConfigurationException | SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
	protected DBConnectionFile(File fileEncrypted,File EncryptKey, String dbName) {
		beanList = new ArrayList<>();
		SecuredFile securedFile = new SecuredFile();
		try {
			String cfgContent = securedFile.getDecryptedConfigFile(fileEncrypted, EncryptKey);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(cfgContent)));
			NodeList nodeList = doc.getElementsByTagName("connection");
			int length = nodeList.getLength();
			for (int i = 0; i < length; i++) {
				Node connection = nodeList.item(i);
				//System.out.println(">>>>"+connection.getChildNodes());
					ConnectionBean bean = new ConnectionBean(connection);
					beanList.add(bean);
			}
			/*
			 * String[] strLines = cfgContent.split("\n"); for (String line :
			 * strLines) { String[] str = line.split("\\|"); ConnectionBean bean
			 * = new ConnectionBean(str); beanList.add(bean); }
			 */

		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | IOException | ParserConfigurationException | SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
	public DBConnectionFile(File fileEncrypted,File EncryptKey) {
		beanList = new ArrayList<>();
		SecuredFile securedFile = new SecuredFile();
		try {
			String cfgContent = securedFile.getDecryptedConfigFile(fileEncrypted, EncryptKey);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(cfgContent)));
			NodeList nodeList = doc.getElementsByTagName("connection");
			int length = nodeList.getLength();
			if(length == 1) {
			Node connection = nodeList.item(0);
				//System.out.println(">>>>"+connection.getChildNodes());
				bean = new ConnectionBean(connection);
			}
			/*
			 * String[] strLines = cfgContent.split("\n"); for (String line :
			 * strLines) { String[] str = line.split("\\|"); ConnectionBean bean
			 * = new ConnectionBean(str); beanList.add(bean); }
			 */

		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | IOException | ParserConfigurationException | SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	protected ArrayList<ConnectionBean> getList() {
		return beanList;
	}
	
	
	public ConnectionBean getConnectionBean() {
		return bean;
	}
}
// String str = String.format(format,
// protocol,vendor,hostName,portNo,dbName,encode, zeroDateTime, ldap, userName,
// password,fileName);
