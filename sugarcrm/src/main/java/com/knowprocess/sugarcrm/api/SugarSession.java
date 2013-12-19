package com.knowprocess.sugarcrm.api;

public class SugarSession {

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

}
