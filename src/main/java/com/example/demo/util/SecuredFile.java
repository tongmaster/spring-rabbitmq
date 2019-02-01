package com.example.demo.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class SecuredFile {
	private static File securedDirectory;
	private static String encryptedConnectionFileName = "dbconfig.cfg";
	private static String encryptKeyFileName = "dbkey.cfg";
	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES";
	static {
		String securedPath = System.getProperty("DB_CONFIG_PATH");
		if (securedPath == null) {
			//securedPath = M.classPath("resources") + "/db";
			securedPath = "/c/resources/db";
		}
		securedDirectory = new File(securedPath);
	}

	protected static File getSecuredDirectory() {
		return securedDirectory;
	}

	protected File getEncryptedConfigFile() {
		File cfgFile = new File(securedDirectory, encryptedConnectionFileName);
		return cfgFile;
	}

	protected File getEncryptKeyFile() {
		File keyFile = new File(securedDirectory, encryptKeyFileName);
		return keyFile;
	}

	protected String getDecryptedConnectionFile() throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		byte[] encryptedKey = readBytes(getEncryptKeyFile());
		byte[] encryptedConnection = readBytes(getEncryptedConfigFile());
		byte[] decryptedConnection = doEncrypt(Cipher.DECRYPT_MODE, new String(encryptedKey), encryptedConnection);
		return new String(decryptedConnection);
	}

	protected void writeBytes(File file, byte[] outputBytes, boolean append) throws IOException {

		try (FileOutputStream outputStream = new FileOutputStream(file, append)) {
			outputStream.write(outputBytes);
		}
	}

	protected byte[] readBytes(File file) throws IOException {
		try (FileInputStream inputStream = new FileInputStream(file)) {
			byte[] inputBytes = new byte[(int) file.length()];
			inputStream.read(inputBytes);
			return inputBytes;
		}

	}
	protected String getDecryptedConfig() throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		byte[] fileContent = readBytes(getEncryptedConfigFile());
		byte[] encryptedFirstKey = readBytes(getEncryptKeyFile());		
		byte[] secondKey = Arrays.copyOfRange(fileContent, 0, 16);		
		byte[] encryptedConfig = Arrays.copyOfRange(fileContent, 16,fileContent.length);
		
		byte[] decryptedFirstKey = doEncrypt(Cipher.DECRYPT_MODE, new String(secondKey), encryptedFirstKey);	
		
		byte[] decryptedConfig = doEncrypt(Cipher.DECRYPT_MODE, new String(decryptedFirstKey), encryptedConfig);
		return new String(decryptedConfig);
	}
	
	protected  String getDecryptedConfigFile(File fileEncrypted , File EncryptKey) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		byte[] fileContent = readBytes(fileEncrypted);
		byte[] encryptedFirstKey = readBytes(EncryptKey);		
		byte[] secondKey = Arrays.copyOfRange(fileContent, 0, 16);		
		byte[] encryptedConfig = Arrays.copyOfRange(fileContent, 16,fileContent.length);
		
		byte[] decryptedFirstKey = doEncrypt(Cipher.DECRYPT_MODE, new String(secondKey), encryptedFirstKey);	
		
		byte[] decryptedConfig = doEncrypt(Cipher.DECRYPT_MODE, new String(decryptedFirstKey), encryptedConfig);
		return new String(decryptedConfig);
	}
	
	
	protected byte[] doEncrypt(int cipherMode, String key, byte[] inputBytes)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException,
			IllegalBlockSizeException, BadPaddingException {		
		Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(cipherMode, secretKey);
		byte[] outputBytes = cipher.doFinal(inputBytes);
		return outputBytes;
	}
	private static String getRandom(int randomLength) {
		String str = "";
		for (int i = 1; i <= randomLength; i++) {
			Random rand = new Random();
			int mod = i % 3;
			if (mod == 0) {
				str +=  (char) (rand.nextInt(26) + (byte)'a');
			} else if (mod == 1) {
				str +=  (char) (rand.nextInt(26) + (byte)'A');
			} else {
				str +=  (char) (rand.nextInt(9) + (byte)'0');	
			}									
		}
		return str;
	}
	public static void main(String[] args) {
		SecuredFile securedFile = new SecuredFile();
		String clearTextFileName = args[0];
		String firstKey = getRandom(16);
		String secondKey = getRandom(16);
		
		File clearTextFile = new File(clearTextFileName);
		try {
			byte[] clearByte = securedFile.readBytes(clearTextFile);
			byte[] encryptedByte = securedFile.doEncrypt(Cipher.ENCRYPT_MODE, firstKey, clearByte);			
			byte[] encryptedFirstKey = securedFile.doEncrypt(Cipher.ENCRYPT_MODE, secondKey, firstKey.getBytes());
			securedFile.writeBytes(securedFile.getEncryptedConfigFile(), secondKey.getBytes(), false);
			securedFile.writeBytes(securedFile.getEncryptedConfigFile(), encryptedByte, true);
			securedFile.writeBytes(securedFile.getEncryptKeyFile(), encryptedFirstKey, false);
			
		} catch (IOException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
