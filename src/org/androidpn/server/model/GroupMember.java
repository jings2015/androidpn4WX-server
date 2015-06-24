package org.androidpn.server.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "group_member")
public class GroupMember {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "group_id", nullable = false ,length = 64)
	private Long groupId;
	
	@Column(name = "account", nullable = false ,length = 64)
	private String account;
	
	@Column(name = "alias_in_group", length = 64)
	private String aliasInGroup;
	
	@Column(name = "pushable", nullable = false ,length = 64)
	private boolean pushable;
	
	@Column(name = "created_date", updatable = false)
	private Date createdDate = new Date();
	
	@Column(name = "owner", nullable = false ,length = 64)
	private String owner;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAliasInGroup() {
		return aliasInGroup;
	}

	public void setAliasInGroup(String aliasInGroup) {
		this.aliasInGroup = aliasInGroup;
	}

	public boolean isPushable() {
		return pushable;
	}

	public void setPushable(boolean pushable) {
		this.pushable = pushable;
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

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	
	
}
