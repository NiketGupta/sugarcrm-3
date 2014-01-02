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
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.knowprocess.crm.CrmRecord;
import com.knowprocess.crm.CrmService;
import com.knowprocess.crm.CrmSession;
import com.knowprocess.sugarcrm.internal.SugarServiceV4Impl;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knowprocess.sugarcrm.api.CrmService#login(com.knowprocess.sugarcrm
	 * .api.SugarSession)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knowprocess.sugarcrm.api.CrmService#createAccount(com.knowprocess
	 * .sugarcrm.api.SugarSession, com.knowprocess.sugarcrm.api.SugarAccount)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knowprocess.sugarcrm.api.CrmService#createContact(com.knowprocess
	 * .sugarcrm.api.SugarSession, com.knowprocess.sugarcrm.api.SugarContact)
	 */
	public CrmRecord createContact(CrmSession session, CrmRecord contact) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knowprocess.sugarcrm.api.CrmService#createAccountWithPrimeContact
	 * (com.knowprocess.sugarcrm.api.SugarSession,
	 * com.knowprocess.sugarcrm.api.SugarContact,
	 * com.knowprocess.sugarcrm.api.SugarAccount)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knowprocess.sugarcrm.api.CrmService#createLead(com.knowprocess.sugarcrm
	 * .api.SugarSession, com.knowprocess.sugarcrm.api.SugarLead)
	 */
	public CrmRecord createLead(CrmSession session, CrmRecord lead) {
		// TODO set_entry
		return lead;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knowprocess.sugarcrm.api.CrmService#getContact(com.knowprocess.sugarcrm
	 * .api.SugarSession, java.lang.String)
	 */
	public CrmRecord getContact(CrmSession session, String contactId) {
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