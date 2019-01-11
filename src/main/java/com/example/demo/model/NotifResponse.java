package com.example.demo.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "notif_response")
@IdClass(NotifResponse.class)
public class NotifResponse implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="notif_uuid")
	private UUID notifUuid;
	
	@Id
	@Column(name="token_id")
	private Integer tokenId;
	@Column(name="response_id")
	private Integer responseId;

	public UUID getNotifUuid() {
		return notifUuid;
	}
	public void setNotifUuid(UUID notifUuid) {
		this.notifUuid = notifUuid;
	}
	public Integer getTokenId() {
		return tokenId;
	}
	public void setTokenId(Integer tokenId) {
		this.tokenId = tokenId;
	}
	public Integer getResponseId() {
		return responseId;
	}
	public void setResponseId(Integer responseId) {
		this.responseId = responseId;
	}
	
	
}
