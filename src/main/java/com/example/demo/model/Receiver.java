package com.example.demo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "receiver")
public class Receiver implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private ReceiverId receiverId;
	
	
	public ReceiverId getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(ReceiverId receiverId) {
		this.receiverId = receiverId;
	}
	
	
	

}
