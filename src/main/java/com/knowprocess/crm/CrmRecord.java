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
package com.knowprocess.crm;

import java.util.HashMap;
import java.util.Map;

import com.knowprocess.sugarcrm.api.SugarService;

/**
 * Common ancestor model for all CRM records.
 * 
 * @author timstephenson
 * 
 */
public class CrmRecord {

	protected String id;

	protected Map<String, Object> properties = new HashMap<String, Object>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Object getCustom(String name) {
		return properties.get(name);
	}

	public void setCustom(String name, Object value) {
		properties.put(name, value);
	}

	public String getNameValueListAsJson() {
		return SugarService.getNameValueListAsJson(properties);
	}

}
