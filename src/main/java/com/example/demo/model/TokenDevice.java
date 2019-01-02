package com.example.demo.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;


@Entity
@Table(name = "token")
@TypeDef(
	    name = "enum_os",
	    typeClass = PostgreSQLEnumType.class
	)
public class TokenDevice {
	
	public static enum enum_os {IOS, ANDROID}
	/*
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;*/
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="token_id")
	private Integer tokenId;
	@Column(name="device_det")
	private String deviceDet;
	
	//@Enumerated(EnumType.STRING)
	//@Enumerated(EnumType.STRING)
	//@Column(name="os_type" ,columnDefinition="enum_os")
	@Column(name="os_type")
	@Enumerated(EnumType.STRING)
	@Type( type = "enum_os" )
	private enum_os osType;
	
	
	@Column(name="app_id")
	private int appId;
	@Column(name="user_ref")
	private String userRef;
	@Column(name="token")
	private String token;
	@Column(name="expire_time")
	private Timestamp expireTime;
	@Column(name="is_active")
	private boolean isActive;
	
	

	
	
	public enum_os getOsType() {
		return osType;
	}
	public void setOsType(enum_os osType) {
		this.osType = osType;
	}
	public Integer getTokenId() {
		return tokenId;
	}
	public void setTokenId(Integer tokenId) {
		this.tokenId = tokenId;
	}
	public String getDeviceDet() {
		return deviceDet;
	}
	public void setDeviceDet(String deviceDet) {
		this.deviceDet = deviceDet;
	}


	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public String getUserRef() {
		return userRef;
	}
	public void setUserRef(String userRef) {
		this.userRef = userRef;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Timestamp getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Timestamp expireTime) {
		this.expireTime = expireTime;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	@Override
	public String toString() {
		return "TokenDevice [tokenId=" + tokenId + ", deviceDet=" + deviceDet + ", osType=" + osType + ", appId="
				+ appId + ", userRef=" + userRef + ", token=" + token + ", expireTime=" + expireTime + ", isActive="
				+ isActive + "]";
	}
	
	
	
	
}
