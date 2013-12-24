package com.knowprocess.sugarcrm.api;

import java.io.Serializable;

public class SugarAccount extends AbstractSugarRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 190044867985297262L;

	private String website;

	private int employees;

	public SugarAccount() {
		;
	}

	public String getName() {
		return (String) properties.get("name");
	}

	public void setName(String name) {
		properties.put("name", name);
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		properties.put("website", website);
	}

	public int getEmployees() {
		return employees;
	}

	public void setEmployees(int employees) {
		properties.put("employees", Integer.valueOf(employees));
	}
}
