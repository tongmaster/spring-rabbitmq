package com.example.demo.model;

public class DeviceDetail {
	private String token;
	private String perid;
	private String cordova;
	private String model;
	private String platform;
	private String uuid;

	private String version;
	private String manufacturer;
	private String isVirtual;
	private String serial;
	private int appId;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getPerid() {
		return perid;
	}
	public void setPerid(String perid) {
		this.perid = perid;
	}
	public String getCordova() {
		return cordova;
	}
	public void setCordova(String cordova) {
		this.cordova = cordova;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getIsVirtual() {
		return isVirtual;
	}
	public void setIsVirtual(String isVirtual) {
		this.isVirtual = isVirtual;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	@Override
	public String toString() {
		return "DeviceDetail [token=" + token + ", perid=" + perid + ", cordova=" + cordova + ", model=" + model
				+ ", platform=" + platform + ", uuid=" + uuid + ", version=" + version + ", manufacturer="
				+ manufacturer + ", isVirtual=" + isVirtual + ", serial=" + serial + ", appId=" + appId + "]";
	}

	
}
