package org.androidpn.server.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class User {
	@Id
	@Column(name = "account", nullable = false, length = 64, unique = true)
	private String account;
	
	@Column(name = "password", nullable = false, length = 64)
	private String password;
	
	@Column(name = "alias", length = 64)
	private String alias;
	
	@Column(name = "client_id", length = 64, unique = true)
	private String clientId;
	
	@Column(name = "created_date", updatable = false)
    private Date createdDate = new Date();

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	
	
}
