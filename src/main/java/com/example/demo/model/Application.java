package com.example.demo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
	private Integer applicationId;
	@Column(name="app_name")
	private String applicationDetail;
	@Column(name="is_active")
	private boolean applicationEnable;
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
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
	
	
	
	
}
