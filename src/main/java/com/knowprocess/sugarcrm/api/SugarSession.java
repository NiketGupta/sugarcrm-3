package com.knowprocess.sugarcrm.api;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

public class SugarSession implements Serializable {

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
		super();
		this.username = username;
		this.password = password;
		this.sugarUrl = sugarUrl;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

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

	public String getSugarUrl() {
		return sugarUrl;
	}

	public void setSugarUrl(String sugarUrl) {
		this.sugarUrl = sugarUrl;
	}

	/**
	 * 
	 * @return true if 'valid' session. Note this does not connect to a server
	 *         so is a 'necessary but not sufficient' test.
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
