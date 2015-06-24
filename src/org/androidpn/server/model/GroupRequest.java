package org.androidpn.server.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "group_request")
public class GroupRequest {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "account", nullable = false, length = 64)
	private String account;
	
	@Column(name = "alias",length = 64)
	private String alias;
	
	@Column(name = "group_id", nullable = false)
	private Long groupId;
	
	@Column(name = "message",length = 256)
	private String message;
	
	@Column(name = "created_date", updatable = false)
	private Date createdDate = new Date();

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
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

	public void setCreatedDate(Date createDate) {
		this.createdDate = createDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
