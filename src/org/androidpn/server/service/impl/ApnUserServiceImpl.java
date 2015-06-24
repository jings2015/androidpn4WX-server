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
package org.androidpn.server.service.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityExistsException;

import org.androidpn.server.dao.ApnUserDao;
import org.androidpn.server.model.ApnUser;
import org.androidpn.server.service.ApnUserService;
import org.androidpn.server.service.exception.ApnUserExistsException;
import org.androidpn.server.service.exception.ApnUserNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataIntegrityViolationException;

/** 
 * This class is the implementation of UserService.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class ApnUserServiceImpl implements ApnUserService {

    protected final Log log = LogFactory.getLog(getClass());

    private ApnUserDao apnUserDao;

    public void setApnUserDao(ApnUserDao apnUserDao) {
        this.apnUserDao = apnUserDao;
    }

    public ApnUser getApnUser(String id) {
        return apnUserDao.getApnUser(new Long(id));
    }

    public List<ApnUser> getApnUsers() {
        return apnUserDao.getApnUsers();
    }
    
    public List<ApnUser> getApnUsersFromCreatedDate(Date createDate) {
    	 return apnUserDao.getApnUsersFromCreatedDate(createDate);
    }

    public ApnUser saveApnUser(ApnUser user) {
    	return apnUserDao.saveApnUser(user);
    }

    public void updateApnUser(ApnUser apnUser){
    	apnUserDao.updateApnUser(apnUser);
    }
    
    public ApnUser getApnUserByClientId(String clientId){
        return (ApnUser) apnUserDao.getApnUserByClientId(clientId);
    }

    public void removeApnUser(Long userId) {
        log.debug("removing user: " + userId);
        apnUserDao.removeApnUser(userId);
    }

}
