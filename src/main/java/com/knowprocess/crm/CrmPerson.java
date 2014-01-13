package com.knowprocess.crm;

public class CrmPerson extends CrmRecord {

	public CrmPerson() {
		super();
	}

	public String getFirstName() {
		return (String) properties.get("first_name");
	}

	public void setFirstName(String firstName) {
		properties.put("first_name", firstName);
	}

	public String getLastName() {
		return (String) properties.get("last_name");
	}

	public void setLastName(String lastName) {
		properties.put("last_name", lastName);
	}

	public String getTitle() {
		return (String) properties.get("title");
	}

	public void setTitle(String title) {
		properties.put("title", title);
	}

	public String getPhoneWork() {
		return (String) properties.get("phone_work");
	}

	public void setPhoneWork(String phoneWork) {
		properties.put("phone_work", phoneWork);
	}

	public String getPhoneOther() {
		return (String) properties.get("phone_other");
	}

	public void setPhoneOther(String phoneOther) {
		properties.put("phone_other", phoneOther);
	}

	public String getEmail1() {
		return (String) properties.get("email1");
	}

	public void setEmail1(String email1) {
		properties.put("email1", email1);
	}

}
