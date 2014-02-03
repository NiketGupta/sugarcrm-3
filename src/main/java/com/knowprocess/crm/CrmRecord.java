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

import java.io.Serializable;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import com.knowprocess.sugarcrm.api.SugarService;

/**
 * Common ancestor model for all CRM records.
 * 
 * @author timstephenson
 * 
 */
public class CrmRecord implements Serializable {
	private static final long serialVersionUID = 1397645817430467867L;

	public static final DateFormat iso = new SimpleDateFormat(
			"yyyy-MM-dd'T'hh:mm:ss");

	protected String id;

	protected Map<String, Object> properties = new HashMap<String, Object>();

	public CrmRecord() {
		super();
	}

	public CrmRecord(CrmRecord crmRecord) {
		this();
		setId(crmRecord.getId());
		this.properties = crmRecord.getProperties();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public Object getCustom(String name) {
		return properties.get(name);
	}

	public void setCustom(String name, Object value) {
		// System.out.println("Setting '" + name + "' to '" + value + "'");
		if ("id".equals(name)) {
			System.out.println("*****************" + value);
			setId((String) value);
			System.out.println("&&&&&&&&&" + getId());
		} else {
			properties.put(name, value);
		}
	}

	public String getNameValueListAsJson() {
		return SugarService.getNameValueListAsJson(properties);
	}

	// This works fine in theory but passing to search_by_module API always
	// returns no matches. Hence using get_entry and get_entry_list instead
	public String getSearchString() {
		StringBuffer sb = new StringBuffer();
		for (Iterator<Entry<String, Object>> it = properties.entrySet()
				.iterator(); it.hasNext();) {
			Entry<String, Object> e = it.next();
			sb.append(" ");
			if (e.getValue() instanceof Date) {
				// sb.append(isoFormat.format(e.getValue()));
			} else {
				sb.append(e.getValue());
			}
		}
		return sb.toString().trim();
	}

	public String getWhereClause(String tableName) {
		StringBuffer sb = new StringBuffer();
		if (getId() != null) {
			sb.append(" ").append(tableName).append(".id = '").append(getId())
					.append("'");
		}
		for (Iterator<Entry<String, Object>> it = properties.entrySet()
				.iterator(); it.hasNext();) {
			Entry<String, Object> e = it.next();
			if (getId() != null) {
				sb.append(" AND ");
			} else {
				sb.append(" ");
			}
			if (e.getValue() instanceof Date) {
				// sb.append(isoFormat.format(e.getValue()));
			} else {
				sb.append(tableName).append(".").append(e.getKey());
				sb.append(" = '").append(e.getValue()).append("'");
				if (it.hasNext()) {
					sb.append(" AND");
				}
			}
		}
		sb.append(" ");
		return sb.toString();
	}

	@Override
	public String toString() {
		return "CrmRecord [id=" + id + ", properties=" + properties + "]";
	}

	public String toJson() {
		JsonObjectBuilder bldr = Json.createObjectBuilder();
		for (Entry<String, Object> entry : properties.entrySet()) {
			if (entry.getValue() == null) {
				// ignore
			} else if (entry.getValue() instanceof Date) {
				bldr.add(entry.getKey(), iso.format(entry.getValue()));
			} else {
				bldr.add(entry.getKey(), entry.getValue().toString());
			}
		}
		return bldr.build().toString();
	}

	public static CrmRecord parseFromJson(String json) {
		CrmRecord record = new CrmRecord();

		JsonParser parser = Json.createParser(new StringReader(json));
		while (parser.hasNext()) {
			Event e = parser.next();
			if (e == Event.KEY_NAME) {
				// System.out.println(parser.getString());
				// System.out.println(parser.getLocation());
				int start = json.indexOf('"', (int) parser.getLocation()
						.getStreamOffset() + 1);
				record.setCustom(parser.getString(),
						json.substring(start + 1, json.indexOf('"', start + 1)));
			}
		}
		// JsonReader reader = Json.createReader(new StringReader(json));
		// JsonObject obj = reader.readObject();
		// System.out.println("obj" + obj);

		return record;
	}

	public String getSelectFields() {
		StringBuilder sb = new StringBuilder();
		for (Iterator<Entry<String, Object>> it = properties.entrySet()
				.iterator(); it.hasNext();) {
			Entry<String, Object> e = it.next();
			sb.append("\"").append(e.getKey()).append("\"");
			if (it.hasNext()) {
				sb.append(",");
			}
		}
		return sb.toString();
	}
}
