package com.knowprocess.sugarcrm.api;

public class SugarAccount {

	private String id;
	private String name;

	public SugarAccount() {
		;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameValueListAsJson() {
		return String.format("{ \"name\":\"name\", \"value\":\"%1$s\" }", name);
	}

}
