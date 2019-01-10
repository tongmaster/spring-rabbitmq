package com.example.demo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;

@Entity
@Table(name = "custom_field")
@IdClass(CustomField.class)
@TypeDef(
	    name = "enum_msg_type",
	    typeClass = PostgreSQLEnumType.class
	)
public class CustomField implements Serializable {


	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "msgId")
	private Integer msgId;

	@Id
	@Column(name = "key_name")
	private String keyName;

	@Id
	@Column(name = "key_value")
	private String keyValue;
	
//	@Id
//	@ManyToOne
//	//@JoinColumn(name="msgId")
//	@JoinColumn
//	@JsonBackReference
//	private Message message;




	public Integer getMsgId() {
		return msgId;
	}

	public void setMsgId(Integer msgId) {
		this.msgId = msgId;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	/*public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}*/

}
