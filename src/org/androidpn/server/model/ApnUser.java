/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.androidpn.server.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/** 
 * This class represents the basic user object.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
@Entity
@Table(name = "apn_user")
public class ApnUser implements Serializable {

    private static final long serialVersionUID = 4733464888738356502L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "client_id", nullable = false, length = 64, unique = true)
    private String clientId;

    @Column(name = "password", length = 64)
    private String password;
    
    @Column(name = "account", length = 64)
    private String account;

//    @Column(name = "email", length = 64)
//    private String email;
//
//    @Column(name = "name", length = 64)
//    private String name;
//
    @Column(name = "created_date", updatable = false)
    private Date createdDate = new Date();

//    @Column(name = "updated_date")
//    private Date updatedDate;

    @Transient
    private boolean online;

    public ApnUser() {
    }

    public ApnUser(final String clientId) {
        this.clientId = clientId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    


//    @Override
//    public boolean equals(Object o) {
//        if (!(o instanceof ApnUser)) {
//            return false;
//        }
//
//        final ApnUser obj = (ApnUser) o;
//        if (clientId != null ? !clientId.equals(obj.clientId)
//                : obj.clientId != null) {
//            return false;
//        }
//        if (!(createdDate.getTime() == obj.createdDate.getTime())) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = 0;
//        result = 29 * result + (clientId != null ? clientId.hashCode() : 0);
//        result = 29 * result
//                + (createdDate != null ? createdDate.hashCode() : 0);
//        return result;
//    }

    public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }

}
