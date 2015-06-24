package org.androidpn.server.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "notification_history")
public class NotificationHistory {
	
	@Column(name = "account", nullable = false ,length = 64)
	private String account;
	
	@Column(name = "notification_id", nullable = false, length =64)
	private Long notificationId;
	
	@Column(name = "group_id", nullable = false, length =64)
	private Long groupId;
	
	@Column(name = "created_date",updatable = false)
	private Date createdDate = new Date();

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createDate) {
		this.createdDate = createDate;
	}
	
	

}
