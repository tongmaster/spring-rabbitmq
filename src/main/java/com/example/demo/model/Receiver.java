package com.example.demo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "receiver")
@IdClass(Receiver.class)
public class Receiver implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*@Id
	@ManyToOne
	@JoinColumn(name="msgId")
	@JsonBackReference
	private Message message;*/
	
	@Id
	@Column(name = "msg_id")
	private Integer msgId;
	
	@Id
	@Column(name = "user_ref")
	private String userRef;

	
	@Column(name = "notif_status")
	private String notifStatus;



	public String getUserRef() {
		return userRef;
	}

	public void setUserRef(String userRef) {
		this.userRef = userRef;
	}

	public String getNotifStatus() {
		return notifStatus;
	}

	public void setNotifStatus(String notifStatus) {
		this.notifStatus = notifStatus;
	}

	public Integer getMsgId() {
		return msgId;
	}

	public void setMsgId(Integer msgId) {
		this.msgId = msgId;
	}

	
	
	

}
