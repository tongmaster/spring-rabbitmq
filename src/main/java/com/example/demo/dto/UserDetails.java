package com.example.demo.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDetails implements Serializable{
	private static final long serialVersionUID = 1L;

	@JsonProperty("supplierId")
    private String supplierId;
	
	@JsonProperty("supplierName")
    private String supplierName;
	
	@JsonProperty("supplierUrl")
	private String supplierUrl;

    // Default constructor is needed to de-serialize JSON
    public UserDetails() {
    }

    public UserDetails(String supplierId, String supplierName,  String supplierUrl) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.supplierUrl = supplierUrl;
    }

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSupplierUrl() {
		return supplierUrl;
	}

	public void setSupplierUrl(String supplierUrl) {
		this.supplierUrl = supplierUrl;
	}

	@Override
	public String toString() {
		return "CrawlSupplierData [supplierId=" + supplierId + ", supplierName=" + supplierName + ", supplierUrl=" + supplierUrl + "]";
	}

}
