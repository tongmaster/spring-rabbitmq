package com.example.demo.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "Application")
public class Application implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*
	@Id
	@Column(name="application_id")
	private Integer applicationId;
	@Column(name="application_detail")
	private String applicationDetail;
	@Column(name="application_enable")
	private boolean applicationEnable;
	
	
	
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getApplicationDetail() {
		return applicationDetail;
	}
	public void setApplicationDetail(String applicationDetail) {
		this.applicationDetail = applicationDetail;
	}
	public boolean isApplicationEnable() {
		return applicationEnable;
	}
	public void setApplicationEnable(boolean applicationEnable) {
		this.applicationEnable = applicationEnable;
	}
	*/
	@Id
	@Column(name="app_id")
	private Integer appId;
	@Column(name="app_name")
	private String appName;
	@Column(name="is_active")
	private boolean isActive;
	@Column(name="max_inactive_day")
	private int maxInactiveDay;
	
	@OneToMany(mappedBy="application" , cascade = CascadeType.ALL /*,orphanRemoval = true*/)
	@JsonManagedReference
	private  Set<TokenDevice> token; 
	
	@OneToMany(mappedBy="application" , cascade = CascadeType.ALL /*,orphanRemoval = true*/)
	@JsonManagedReference
	private  Set<Message> message; 
	
	
	public Integer getAppId() {
		return appId;
	}
	public void setAppId(Integer appId) {
		this.appId = appId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public int getMaxInactiveDay() {
		return maxInactiveDay;
	}
	public void setMaxInactiveDay(int maxInactiveDay) {
		this.maxInactiveDay = maxInactiveDay;
	}
	public Set<TokenDevice> getToken() {
		return token;
	}
	public void setToken(Set<TokenDevice> token) {
		this.token = token;
	}
	public Set<Message> getMessage() {
		return message;
	}
	public void setMessage(Set<Message> message) {
		this.message = message;
	}
	
	

	/*@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="app_id", referencedColumnName="app_id")
	private  Set<TokenDevice> tokenDevice;
	

	
	
	public Set<TokenDevice> getTokenDevice() {
		return tokenDevice;
	}
	public void setTokenDevice(Set<TokenDevice> tokenDevice) {
		this.tokenDevice = tokenDevice;
	}*/
	
	
	
	
	
	
	
}
