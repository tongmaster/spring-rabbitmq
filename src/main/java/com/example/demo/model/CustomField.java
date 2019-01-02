package com.example.demo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "custom_field")
public class CustomField implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private CustomFieldId customFieldId; 
	
	@Column(name="key_value")
	private String keyValue;

	public CustomFieldId getCustomFieldId() {
		return customFieldId;
	}

	public void setCustomFieldId(CustomFieldId customFieldId) {
		this.customFieldId = customFieldId;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}
	
	
	
	
}
