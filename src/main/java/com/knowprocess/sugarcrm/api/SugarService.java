package com.knowprocess.sugarcrm.api;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.knowprocess.sugarcrm.internal.SugarServiceV4Impl;

public class SugarService {

	// Only interested in one version for now.
	private SugarServiceV4Impl impl;

	public SugarService() {
		try {
			impl = new SugarServiceV4Impl();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SugarException(e.getMessage(), e);
		}
	}

	public SugarSession login(SugarSession session) {
		try {
			session.setSessionId(impl.login(session));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SugarException(e.getMessage(), e);
		}
		return session;
	}

	public AbstractSugarRecord createAccount(SugarSession session, SugarAccount acct) {
		try {
			acct.setId(impl.setEntry(session, "Accounts",
					acct.getNameValueListAsJson()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SugarException(e.getMessage(), e);
		}
		return acct;
	}

	public SugarContact createContact(SugarSession session, SugarContact contact) {
		try {
			contact.setId(impl.setEntry(session, "Contacts",
					contact.getNameValueListAsJson()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SugarException(e.getMessage(), e);
		}
		return contact;
	}

	public SugarContact createAccountWithPrimeContact(SugarSession session,
			SugarContact contact, SugarAccount acct) {
		createContact(session, contact);
		createAccount(session, acct);

		try {
			String response = impl.setRelationship(session, "Accounts",
					acct.getId(), "contacts",
					contact.getId());
			System.out.println("Response: " + response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SugarException(e.getMessage(), e);
		}
		return contact;
	}

	public SugarLead createLead(SugarSession session, SugarLead lead) {
		// TODO set_entry
		return lead;
	}

	public SugarContact getContact(SugarSession session, String contactId) {
		// TODO get_entry
		return null;
	}

	public static String getNameValueListAsJson(Map<String, Object> properties) {
		StringBuffer sb = new StringBuffer();
		for (Iterator<Entry<String, Object>> it = properties.entrySet()
				.iterator(); it.hasNext();) {
			Entry<String, Object> e = it.next();
			sb.append("{\"").append("name").append("\":\"").append(e.getKey())
					.append("\",");
			sb.append("\"").append("value").append("\":\"");
			if (e.getValue() instanceof Date) {
				// sb.append(isoFormat.format(e.getValue()));
			} else {
				sb.append(e.getValue());
			}
			sb.append("\"}");
			if (it.hasNext()) {
				sb.append(",");
			}
		}
		return sb.toString();
	}
}