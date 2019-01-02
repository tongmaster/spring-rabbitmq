package com.example.demo.model;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "notif")
public class Notif {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="notif_uuid")
	private UUID notifUuid;
	@Column(name="msg_id")
	private Integer msgId;
	@Column(name="token_id")
	private Integer tokenId;
	@Column(name="queue_status")
	private String queueStatus;
	@Column(name="send_status")
	private String sendStatus;
	@Column(name="request_time")
	//@CreationTimestamp
	private Timestamp requestTime;
	@Column(name="response_time")
	private Timestamp responseTime;
	@Column(name="request_body")
	private String requestBody;
	@Column(name="response_body")
	private String responseBody;

	public UUID getNotifUuid() {
		return notifUuid;
	}
	public void setNotifUuid(UUID notifUuid) {
		this.notifUuid = notifUuid;
	}
	public Integer getMsgId() {
		return msgId;
	}
	public void setMsgId(Integer msgId) {
		this.msgId = msgId;
	}
	public Integer getTokenId() {
		return tokenId;
	}
	public void setTokenId(Integer tokenId) {
		this.tokenId = tokenId;
	}
	public String getQueueStatus() {
		return queueStatus;
	}
	public void setQueueStatus(String queueStatus) {
		this.queueStatus = queueStatus;
	}
	public String getSendStatus() {
		return sendStatus;
	}
	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}
	public Timestamp getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Timestamp requestTime) {
		this.requestTime = requestTime;
	}
	public Timestamp getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Timestamp responseTime) {
		this.responseTime = responseTime;
	}
	public String getRequestBody() {
		return requestBody;
	}
	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}
	public String getResponseBody() {
		return responseBody;
	}
	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}
/*
	@PrePersist
	public void autofill() {
		this.setNotifUuid(UUID.randomUUID().toString());
	}*/
	

	
}
