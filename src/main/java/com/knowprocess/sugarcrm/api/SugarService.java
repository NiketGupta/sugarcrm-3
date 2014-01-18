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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.knowprocess.crm.CrmRecord;
import com.knowprocess.crm.CrmService;
import com.knowprocess.crm.CrmSession;
import com.knowprocess.sugarcrm.internal.SugarServiceV4Impl;

/**
 * Exposes a more semantically rich API wrapping the setX, getX API of Sugar.
 * 
 * @author timstephenson
 */
public class SugarService implements CrmService {

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

	/**
	 * 
	 * @see com.knowprocess.crm.api.CrmService#login(com.knowprocess.crm
	 *      .api.CrmSession)
	 */
	public CrmSession login(CrmSession session) {
		if (!(session instanceof SugarSession)) {
			throw new IllegalArgumentException(
					"session parameter must be instance of "
							+ SugarSession.class.getName());
		}
		try {
			((SugarSession) session).setSessionId(impl
					.login((SugarSession) session));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SugarException(e.getMessage(), e);
		}
		return session;
	}

	/**
	 * 
	 * @see com.knowprocess.crm.api.CrmService#createAccount(com.knowprocess
	 *      .crm.api.CrmSession, com.knowprocess.crm.api.CrmRecord)
	 */
	public CrmRecord createAccount(CrmSession session, CrmRecord acct) {
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

	/**
	 * 
	 * @see com.knowprocess.crm.api.CrmService#createContact(com.knowprocess
	 *      .crm.api.CrmSession, com.knowprocess.crm.api.CrmRecord)
	 */
	public CrmRecord createContact(CrmSession session, CrmRecord contact) {
		return setEntry(session, contact, "Contacts");
	}

	private CrmRecord setEntry(CrmSession session, CrmRecord contact,
			String moduleName) {
		try {
			contact.setId(impl.setEntry(session, moduleName,
					contact.getNameValueListAsJson()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SugarException(e.getMessage(), e);
		}
		return contact;
	}

	/**
	 * 
	 * @see com.knowprocess.crm.api.CrmService#createAccountWithPrimeContact
	 *      (com.knowprocess.crm.api.CrmSession,
	 *      com.knowprocess.crm.api.CrmRecord,
	 *      com.knowprocess.crm.api.CrmRecord)
	 */
	public CrmRecord createAccountWithPrimeContact(CrmSession session,
			CrmRecord contact, CrmRecord acct) {
		createContact(session, contact);
		createAccount(session, acct);

		try {
			String response = impl.setRelationship(session, "Accounts",
					acct.getId(), "contacts", contact.getId());
			System.out.println("Response: " + response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SugarException(e.getMessage(), e);
		}
		return contact;
	}

	/**
	 * 
	 * @see com.knowprocess.crm.api.CrmService#createLead(com.knowprocess.crm
	 *      .api.CrmSession, com.knowprocess.crm.api.CrmRecord)
	 */
	public CrmRecord createLead(CrmSession session, CrmRecord lead) {
		return setEntry(session, lead, "Leads");
	}

	/**
	 * 
	 * @throws IOException
	 * @see com.knowprocess.crm.api.CrmService#getContact(com.knowprocess.crm
	 *      .api.CrmSession, java.lang.String)
	 */
	public CrmRecord getContact(CrmSession session, String contactId)
			throws IOException {
		return new SugarContact(impl.getEntry(session, "Contacts", contactId, ""));
	}

	/**
	 * 
	 * @param offset
	 * @param maxResults
	 * @throws IOException
	 * @see com.knowprocess.crm.api.CrmService#searchContacts(com.knowprocess.crm
	 *      .api.CrmSession, com.knowprocess.crm .api.CrmRecord, int, int)
	 */
	public List<CrmRecord> searchContacts(CrmSession session, CrmRecord query,
			int offset, int maxResults) throws IOException {
		// Note that calling getEntryList with id='x' seems not to return work,
		// hence this workaround

		// Note 2: No success getting any result from search_by_module
		List<CrmRecord> list = null;
		if (query.getId() == null) {
			list = impl.getEntryList(session, "Contacts", query, 
					/* order by */" contacts.last_name ", offset, maxResults);
		} else {
			list = new ArrayList<CrmRecord>();
			list.add(impl.getEntry(session, "Contacts", query.getId(), ""));
		}
		List<CrmRecord> typedList = new ArrayList<CrmRecord>();
		for (CrmRecord crmRecord : list) {
			typedList.add(new SugarContact(crmRecord));
		}
		return typedList;
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

	public String toJson(List<CrmRecord> list) {
		return impl.toJson(list);
	}
}
