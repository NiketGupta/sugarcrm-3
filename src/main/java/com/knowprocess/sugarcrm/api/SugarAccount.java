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

import com.knowprocess.crm.CrmRecord;

public class SugarAccount extends CrmRecord implements Serializable {

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
