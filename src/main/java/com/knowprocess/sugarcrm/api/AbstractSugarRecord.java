package com.knowprocess.sugarcrm.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Common abstract ancestor for all Sugar records.
 * 
 * @author timstephenson
 * 
 */
public abstract class AbstractSugarRecord {

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
