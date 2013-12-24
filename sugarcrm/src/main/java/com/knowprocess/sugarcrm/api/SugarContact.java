package com.knowprocess.sugarcrm.api;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents a Contact in Sugar.
 * 
 * @author timstephenson
 * 
 */
public class SugarContact extends AbstractSugarRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1453955943706835232L;

	public String getModifiedByName() {
		return (String) properties.get("modified_by_name");
	}

	public void setModifiedByName(String modifiedByName) {
		properties.put("modified_by_name", modifiedByName);
	}

	public String getName() {
		return (String) properties.get("name");
	}

	public void setName(String name) {
		properties.put("name", name);
	}

	public Date getDateEntered() {
		return (Date) properties.get("date_entered");
	}

	public void setDateEntered(Date dateEntered) {
		properties.put("date_entered", dateEntered);
	}

	public Date getDateModified() {
		return (Date) properties.get("date_modified");
	}

	public void setDateModified(Date dateModified) {
		properties.put("date_modified", dateModified);
	}

	public String getModifiedUserId() {
		return (String) properties.get("modified_by_name");
	}

	public void setModifiedUserId(String modifiedUserId) {
		properties.put("modified_user_id", modifiedUserId);
	}

	public String getCreatedBy() {
		return (String) properties.get("created_by");
	}

	public void setCreatedBy(String createdBy) {
		properties.put("created_by", createdBy);
	}

	public boolean isDeleted() {
		return ((Boolean) properties.get("deleted")).booleanValue();
	}

	public void setDeleted(boolean deleted) {
		properties.put("deleted", Boolean.valueOf(deleted));
	}

	public String getFullName() {
		return (String) properties.get("full_name");
	}

	public void setFullName(String fullName) {
		properties.put("full_name", fullName);
	}

	public boolean isDoNotCall() {
		return ((Boolean) properties.get("do_not_call")).booleanValue();
	}

	public void setDoNotCall(boolean doNotCall) {
		properties.put("do_not_call", Boolean.valueOf(doNotCall));
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

	public String getRealSource() {
		return (String) properties.get("real_source_c");
	}

	public void setRealSource(String realSource) {
		properties.put("real_source_c", realSource);
	}

}
