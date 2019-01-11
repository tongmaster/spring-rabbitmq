package com.example.demo.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;


@Entity
@Table(name = "token")
@TypeDef(
	    name = "enum_os",
	    typeClass = PostgreSQLEnumType.class
	)
public class TokenDevice implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static enum enum_os {IOS, ANDROID}
	/*
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;*/
	@Id
	@SequenceGenerator(name="token_generator",
    sequenceName="push_notif.token_token_id_seq",
    allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
	generator="token_generator")
	@Column(name="token_id")
	private Integer tokenId;
	@Column(name="device_det")
	private String deviceDet;
	@Column(name="device_uuid")
	private String deviceUuid;
	//@Enumerated(EnumType.STRING)
	//@Enumerated(EnumType.STRING)
	//@Column(name="os_type" ,columnDefinition="enum_os")
	@Column(name="os_type")
	@Enumerated(EnumType.STRING)
	@Type( type = "enum_os" )
	private enum_os osType;
	
	
/*	@Column(name="app_id")
	private Integer appId;*/
	
	
	@Column(name="user_ref")
	private String userRef;
	@Column(name="token")
	private String token;
	
	@Column(name="last_login" ,columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	@CreationTimestamp
	private Timestamp lastLogin;

	
	@ManyToOne
	@JoinColumn(name="appId")
	@JsonBackReference
	//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Application application;
	
	

	
	public Application getApplication() {
		return application;
	}
	public void setApplication(Application application) {
		this.application = application;
	}
	public enum_os getOsType() {
		return osType;
	}
	public void setOsType(enum_os osType) {
		this.osType = osType;
	}

	public String getDeviceDet() {
		return deviceDet;
	}
	public void setDeviceDet(String deviceDet) {
		this.deviceDet = deviceDet;
	}



	public Integer getTokenId() {
		return tokenId;
	}
	public void setTokenId(Integer tokenId) {
		this.tokenId = tokenId;
	}
	public String getDeviceUuid() {
		return deviceUuid;
	}
	public void setDeviceUuid(String deviceUuid) {
		this.deviceUuid = deviceUuid;
	}
	public Timestamp getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}
	/*	public Integer getAppId() {
		return appId;
	}
	public void setAppId(Integer appId) {
		this.appId = appId;
	}*/
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
	@Override
	public String toString() {
		return "TokenDevice [deviceDet=" + deviceDet + ", deviceUuid=" + deviceUuid + ", osType=" + osType
				+ ", userRef=" + userRef + ", token=" + token + ", lastLogin=" + lastLogin + ", application="
				+ application + "]";
	}




	
	
	
	
}
