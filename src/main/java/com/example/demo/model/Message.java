package com.example.demo.model;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;

@Entity
@Table(name = "message")
@TypeDef(
	    name = "enum_msg_type",
	    typeClass = PostgreSQLEnumType.class
	)
public class Message {
	public static enum enum_msg_type {INFO, DEBUG}
	@Id
	@SequenceGenerator(name="msg_generator",
    sequenceName="push_notif.message_msg_id_seq",
    allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
	generator="msg_generator")
	@Column(name="msg_id")
	private Integer msgId;
/*	@Column(name="app_id")
	private Integer appId;*/
	
	@Column(name="msg_type")
	@Enumerated(EnumType.STRING)
	@Type(type = "enum_msg_type")
	private enum_msg_type msgType;
	
	
	//@Generated(GenerationTime.INSERT)

    //@Temporal(TemporalType.TIMESTAMP)
	//@Convert(converter = LocalDateTimeConverter.class)
	
	//@Generated(GenerationTime.INSERT)
	//@Temporal(TemporalType.TIMESTAMP)
	@Column(name="send_time" ,columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	//@Generated(GenerationTime.INSERT)
	@CreationTimestamp
	private Timestamp sendTime;
	@Column(name="priority")
	private String priority;
	@Column(name="time_to_live")
	private int timeToLive;
	@Column(name="title")
	private String  title;
	@Column(name="body")
	private String body;
	@Column(name="sound")
	private String sound;
	@Column(name="badge")
	private String badge;
	@Column(name="click_action")
	private String clickAction;
	@Column(name="is_broadcast")
	private boolean isBroadcast;
	@Column(name="is_queue")
	private boolean isQueue;
	@Column(name="message_status")
	private String messageStatus;
	@ManyToOne
	@JoinColumn(name="appId")
	//@JoinColumn
	@JsonBackReference
	//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Application application;
	
	/*@OneToMany(mappedBy="message" , cascade = CascadeType.ALL )
	@JsonManagedReference
	private  Set<Notif> notif; 
	*/
	
//	@OneToMany(fetch = FetchType.LAZY ,mappedBy="message" , cascade = CascadeType.ALL  )
//	@JsonManagedReference
//	private  Set<Receiver> receiver; 
	
	

//	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "message")
//	@JsonManagedReference
//    private Set<CustomField> customField;
	
	
	public Integer getMsgId() {
		return msgId;
	}
	public void setMsgId(Integer msgId) {
		this.msgId = msgId;
	}

	
	public Application getApplication() {
		return application;
	}
	public void setApplication(Application application) {
		this.application = application;
	}
	public enum_msg_type getMsgType() {
		return msgType;
	}
	public void setMsgType(enum_msg_type msgType) {
		this.msgType = msgType;
	}
	
	
	public Timestamp getSendTime() {
		return sendTime;
	}
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	public int getTimeToLive() {
		return timeToLive;
	}
	public void setTimeToLive(int timeToLive) {
		this.timeToLive = timeToLive;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getSound() {
		return sound;
	}
	public void setSound(String sound) {
		this.sound = sound;
	}
	public String getBadge() {
		return badge;
	}
	public void setBadge(String badge) {
		this.badge = badge;
	}
	public String getClickAction() {
		return clickAction;
	}
	public void setClickAction(String clickAction) {
		this.clickAction = clickAction;
	}
	public boolean isBroadcast() {
		return isBroadcast;
	}
	public void setBroadcast(boolean isBroadcast) {
		this.isBroadcast = isBroadcast;
	}
	public boolean isQueue() {
		return isQueue;
	}
	public void setQueue(boolean isQueue) {
		this.isQueue = isQueue;
	}
	public String getMessageStatus() {
		return messageStatus;
	}
	public void setMessageStatus(String messageStatus) {
		this.messageStatus = messageStatus;
	}
	
	
	/*public Set<Notif> getNotif() {
		return notif;
	}
	public void setNotif(Set<Notif> notif) {
		this.notif = notif;
	}*/
	
	
/*	public Set<ReceiverId> getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(Set<ReceiverId> receiverId) {
		this.receiverId = receiverId;
	}*/

/*	public Integer getAppId() {
		return appId;
	}
	public void setAppId(Integer appId) {
		this.appId = appId;
	}*/
/*	public Set<CustomField> getCustomField() {
		return customField;
	}
	public void setCustomField(Set<CustomField> customField) {
		this.customField = customField;
	}
	public Set<Receiver> getReceiver() {
		return receiver;
	}
	public void setReceiver(Set<Receiver> receiver) {
		this.receiver = receiver;
	}*/
	
/*	@Override
	public String toString() {
		return "Message [msgId=" + msgId + ", msgType=" + msgType + ", sendTime=" + sendTime + ", priority=" + priority
				+ ", timeToLive=" + timeToLive + ", title=" + title + ", body=" + body + ", sound=" + sound + ", badge="
				+ badge + ", clickAction=" + clickAction + ", isBroadcast=" + isBroadcast + ", application="
				+ application + "]";
	}*/
	
	/* @Override
	    public String toString() {
	        String result = String.format(
	                "Category[msgId=%d, msgType='%s']%n",
	                msgId, msgType);
	        if (customField != null) {
	            for(CustomField custom : customField) {
	                result += String.format(
	                        "CustomField[msgId=%d, keyname='%s ,keyvalue='%s']%n",
	                        "", custom.getKeyName(), custom.getKeyValue());
	            }
	        }

	        return result;
	    }*/
	
}
