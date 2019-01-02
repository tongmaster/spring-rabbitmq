package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


public class Payload {
	
	
	private Message message;
	private List<String> userRef;
	private Map<String,String> data;
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public List<String> getUserRef() {
		return userRef;
	}
	public void setUserRef(List<String> userRef) {
		this.userRef = userRef;
	}
	public Map<String, String> getData() {
		return data;
	}
	public void setData(Map<String, String> data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "Payload [message=" + message + ", userRef=" + userRef + ", data=" + data + "]";
	}
	
	
	
	
	
	
/*
	private String to;
	private String priority;
	private String restricted;
	private Notification notification;
	private Data data;

	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getRestricted() {
		return restricted;
	}
	public void setRestricted(String restricted) {
		this.restricted = restricted;
	}
	public Notification getNotification() {
		return notification;
	}
	public void setNotification(Notification notification) {
		this.notification = notification;
	}
	
	*/
	
}
