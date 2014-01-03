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
import java.util.Date;

import com.knowprocess.crm.CrmPerson;

/**
 * Represents a Contact in Sugar.
 * 
 * @author timstephenson
 * 
 */
public class SugarContact extends CrmPerson implements Serializable {

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

}
