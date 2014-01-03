/*
 * A Java client library to interact with the Sugar CRM REST API.
 * Copyright (C) 2013-2014 Tim Stephenson (tim@knowprocess.com)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.knowprocess.sugarcrm.api;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import com.knowprocess.crm.CrmSession;

public class SugarSession implements Serializable, CrmSession {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6958803585274323020L;

	private String sessionId;
	private String username;
	private String password;
	private String sugarUrl;

	public SugarSession() {
		;
	}

	public SugarSession(String username, String password, String sugarUrl) {
		this();
		this.username = username;
		this.password = password;
		this.sugarUrl = sugarUrl;
		if (!isValid()) {
			throw new IllegalArgumentException(
					"Please check Username, Password and URL supplied.");
		}
	}

	/**
	 * @see com.knowprocess.sugarcrm.api.CrmSession#getSessionId()
	 */
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @see com.knowprocess.sugarcrm.api.CrmSession#getUsername()
	 */
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @see com.knowprocess.sugarcrm.api.CrmSession#getSugarUrl()
	 */
	public String getSugarUrl() {
		return sugarUrl;
	}

	public void setSugarUrl(String sugarUrl) {
		this.sugarUrl = sugarUrl;
	}

	/**
	 * @see com.knowprocess.sugarcrm.api.CrmSession#isValid()
	 */
	public boolean isValid() {
		if (username == null || password == null || sugarUrl == null ) {
			return false; 
		}
		try {
			new URL(sugarUrl); 
		} catch (MalformedURLException e) {
			return false;
		}
		return true;
	}
}
