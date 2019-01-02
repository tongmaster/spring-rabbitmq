package com.example.demo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Embeddable
public class ReceiverId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name="msg_id")
	private Integer msgId;
	
	@Column(name="user_ref")
	private String userRef;
	public Integer getMsgId() {
		return msgId;
	}
	public void setMsgId(Integer msgId) {
		this.msgId = msgId;
	}
	public String getUserRef() {
		return userRef;
	}
	public void setUserRef(String userRef) {
		this.userRef = userRef;
	}
	
	
	

}
