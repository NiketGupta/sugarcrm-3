package com.knowprocess.sugarcrm.api;

import com.knowprocess.crm.CrmRecord;

public class ArchivedEmail extends CrmRecord {
	public ArchivedEmail(String from, String to, String subject, String body) {
		super();
		setFrom(from);
		setTo(to);
		setSubject(subject);
		setBody(body);
	}

	private void setBody(String body) {
		properties.put("description", body);
	}

	public String getBody() {
		return (String) properties.get("description");
	}

	public void setBodyHtml(String body) {
		properties.put("description_html", body);
	}

	public String getBodyHtml() {
		return (String) properties.get("description_html");
	}

	public void setSubject(String subject) {
		properties.put("name", subject);
	}

	public String getSubject() {
		return (String) properties.get("name");
	}

	public void setTo(String to) {
		properties.put("to_addrs", to);
	}

	public String getTo() {
		return (String) properties.get("to_addrs");
	}

	public void setFrom(String from) {
		properties.put("from_addr", from);
	}

	public String getFrom() {
		return (String) properties.get("from_addr");
	}
}
