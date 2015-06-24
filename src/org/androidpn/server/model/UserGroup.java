package org.androidpn.server.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_group")
public class UserGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "group_id")
	private Long groupId;
	
	@Column(name="group_name",nullable = false, length = 64)
	private String groupName;
	
	@Column(name="owner",nullable = false, length = 64)
	private String owner;
	
	@Column(name="info", length = 1000)
	private String info;
	
	@Column(name = "created_date", updatable = false)
    private Date createdDate = new Date();
	
	@Column(name="flag",length = 64, nullable = false, unique =true)
	private String flag;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	
	
}
