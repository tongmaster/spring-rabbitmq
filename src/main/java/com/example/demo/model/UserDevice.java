package com.example.demo.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_device")
public class UserDevice {
	@Id
	@Column(name="device_id")
	private Integer deviceID;
	@Column(name="application_id")
	private Integer applicationId;
	@Column(name="device_detail")
	private String deviceDetail;
	@Column(name="device_token")
	private String deviceToken;
	@Column(name="device_enable")
	private boolean deviceIsEnable;
	@Column(name="os_type")
	private Integer osType;
	@Column(name="last_active")
	private Timestamp  lastActive;
	@Column(name="receiver_type")
	private String receiverType;
	@Column(name="receiver_reference")
	private String receiverReference;
	public Integer getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(Integer deviceID) {
		this.deviceID = deviceID;
	}
	
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	public String getDeviceDetail() {
		return deviceDetail;
	}
	public void setDeviceDetail(String deviceDetail) {
		this.deviceDetail = deviceDetail;
	}
	public String getDeviceToken() {
		return deviceToken;
	}
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	public boolean isDeviceIsEnable() {
		return deviceIsEnable;
	}
	public void setDeviceIsEnable(boolean deviceIsEnable) {
		this.deviceIsEnable = deviceIsEnable;
	}
	public Integer getOsType() {
		return osType;
	}
	public void setOsType(Integer osType) {
		this.osType = osType;
	}
	public Timestamp getLastActive() {
		return lastActive;
	}
	public void setLastActive(Timestamp lastActive) {
		this.lastActive = lastActive;
	}
	public String getReceiverType() {
		return receiverType;
	}
	public void setReceiverType(String receiverType) {
		this.receiverType = receiverType;
	}
	public String getReceiverReference() {
		return receiverReference;
	}
	public void setReceiverReference(String receiverReference) {
		this.receiverReference = receiverReference;
	}
	
	
}
