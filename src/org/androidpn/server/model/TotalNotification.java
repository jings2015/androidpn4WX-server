package org.androidpn.server.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "total_notification")
public class TotalNotification {
	@Id
	@Column(name = "notification_id")
	private Long notificationId;

	@Column(name = "receiver",length = 64)
	private String receiver;

	@Column(name = "sender", nullable = false, length = 64)
	private String sender;

	@Column(name = "group_id", nullable = false, length = 64)
	private String groupId;

	@Column(name = "title", length = 64)
	private String title;

	@Column(name = "message", length = 1000)
	private String message;

	// @Column(name = "uri")
	// private String uri;

	@Column(name = "created_date", updatable = false)
	private Date createdDate = new Date();

	public TotalNotification() {

	};

	public TotalNotification(String receiver, String sender,
			String groupId, String title, String message) {
		this.receiver = receiver;
		this.sender = sender;
		this.groupId = groupId;
		this.title = title;
		this.message = message;
	}
	
	public TotalNotification(String sender,String message,String groupId){
		this.sender = sender;
		this.message = message;
		this.groupId = groupId;
		this.title = "";
		this.receiver = "";
	}

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	
}
