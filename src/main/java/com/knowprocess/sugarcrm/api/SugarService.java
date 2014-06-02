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
 * @author Tim Stephenson
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
     */
    public CrmRecord updateAccount(CrmSession session, CrmRecord acct) {
        if (acct.getId() == null) {
            throw new SugarException("Account id must not be null");
        }

        return createAccount(session, acct);
    }

    /**
     * 
     * @see com.knowprocess.crm.api.CrmService#createContact(com.knowprocess
     *      .crm.api.CrmSession, com.knowprocess.crm.api.CrmRecord)
     */
	public CrmRecord createContact(CrmSession session, CrmRecord contact) {
		return setEntry(session, contact, "Contacts");
	}

    /**
     * 
     */
    public CrmRecord updateContact(CrmSession session, CrmRecord contact) {
        if (contact.getId() == null) {
            throw new SugarException("Contact id must not be null");
        }

        return createContact(session, contact);
    }

    protected CrmRecord setEntry(CrmSession session, CrmRecord record,
			String moduleName) {
		try {
			record.setId(impl.setEntry(session, moduleName,
					record.getNameValueListAsJson()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SugarException(e.getMessage(), e);
		}
		return record;
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

		createRelationship(session, "Accounts", acct.getId(), "contacts",
				contact.getId());
		return contact;
	}

    /**
     * 
     */
    public CrmRecord updateAccountWithPrimeContact(CrmSession session,
            CrmRecord contact, CrmRecord acct) {
        if (contact.getId() == null) {
            throw new SugarException("Contact id must not be null");
        }
        if (acct.getId() == null) {
            throw new SugarException("Account id must not be null");
        }

        return createAccountWithPrimeContact(session, contact, acct);
    }

    private void createRelationship(CrmSession session, String moduleA,
			String idA, String moduleB, String idB) {
		try {
			String response = impl.setRelationship(session, moduleA, idA,
					moduleB, idB);
			System.out.println("Response: " + response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SugarException(e.getMessage(), e);
		}
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
     */
    public CrmRecord updateLead(CrmSession session, CrmRecord lead) {
        if (lead.getId() == null) {
            throw new SugarException("Lead id must not be null");
        }

        return createAccount(session, lead);
    }

    /**
     * 
     * @throws IOException
     * @see com.knowprocess.crm.api.CrmService#getContact(com.knowprocess.crm
     *      .api.CrmSession, java.lang.String)
     */
	public CrmRecord getContact(CrmSession session, String contactId)
			throws IOException {
		SugarContact contact = new SugarContact(impl.getEntry(session, "Contacts", contactId, ""));
        if (contact.getAccountId() != null) {
            contact.setAccount(new SugarAccount(impl.getEntry(session,
                    "Accounts", contact.getAccountId(), "")));
        }
        return contact;
	}

    /**
     * 
     * @throws IOException
     * @see com.knowprocess.crm.api.CrmService#getAccount(com.knowprocess.crm
     *      .api.CrmSession, java.lang.String)
     */
    public CrmRecord getAccount(CrmSession session, String acctId)
            throws IOException {
        return new SugarAccount(impl.getEntry(session, "Accounts", acctId, ""));
    }

    /**
     * 
     * @throws IOException
     * @see com.knowprocess.crm.api.CrmService#getLead(com.knowprocess.crm
     *      .api.CrmSession, java.lang.String)
     */
    public CrmRecord getLead(CrmSession session, String leadId)
            throws IOException {
        return new SugarLead(impl.getEntry(session, "Leads", leadId, ""));
    }

    public List<CrmRecord> getRelationships(CrmSession session,
            String moduleName, String id, String linkedModule)
            throws IOException {
        return impl.getRelationships(session, moduleName, id, linkedModule);
    }

	protected List<CrmRecord> search(CrmSession session, String moduleName,
			CrmRecord query, String orderBy, int offset, int maxResults)
			throws IOException {
		// Note that calling getEntryList with id='x' seems not to return work,
		// hence this workaround

		// Note 2: No success getting any result from search_by_module
		List<CrmRecord> list = null;
		if (query.getId() == null) {
			list = impl.getEntryList(session, moduleName, query, orderBy,
					offset, maxResults);
		} else {
			list = new ArrayList<CrmRecord>();
			list.add(impl.getEntry(session, moduleName, query.getId(), ""));
		}

		return list;
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
        String moduleName = "Contacts";
        List<CrmRecord> list = search(session, moduleName, query,
				" contacts.last_name ", offset, maxResults);
		List<CrmRecord> typedList = new ArrayList<CrmRecord>();
		for (CrmRecord crmRecord : list) {
			SugarContact contact = new SugarContact(crmRecord);
            typedList.add(contact);
		}
		return typedList;
	}

	/**
	 * @param offset
	 * @param maxResults
	 * @throws IOException
	 * @see com.knowprocess.crm.api.CrmService#searchLeads(com.knowprocess.crm
	 *      .api.CrmSession, com.knowprocess.crm .api.CrmRecord, int, int)
	 */
	public List<SugarLead> searchLeads(SugarSession session, CrmRecord query,
			int offset, int maxResults) throws IOException {
		List<CrmRecord> list = search(session, "Leads", query, " ", offset,
				maxResults);
		List<SugarLead> typedList = new ArrayList<SugarLead>();
		for (CrmRecord crmRecord : list) {
			typedList.add(new SugarLead(crmRecord));
		}
		return typedList;
	}

	/**
	 * @param offset
	 * @param maxResults
	 * @throws IOException
	 * @see com.knowprocess.crm.api.CrmService#searchLeads(com.knowprocess.crm
	 *      .api.CrmSession, com.knowprocess.crm .api.CrmRecord, int, int)
	 */
	public List<SugarNote> searchNotes(SugarSession session, CrmRecord query,
			int offset, int maxResults) throws IOException {
		List<CrmRecord> list = search(session, "Notes", query, " ", offset,
				maxResults);
		List<SugarNote> typedList = new ArrayList<SugarNote>();
		for (CrmRecord crmRecord : list) {
			typedList.add(new SugarNote(crmRecord));
		}
		return typedList;
	}

    /**
     * Sugar REST API uses a non-standard way of serializing. It is parseable as
     * JSON but not in the usual way. For example:
     * 
     * <pre>
     *   {"name":"Foo","value":"Bar"}
     * </pre>
     * 
     * @param properties
     * @return
     */
    public static String getNameValueListAsJson(String id,
            Map<String, Object> properties) {
        Object o = properties.put("id", id);
        String tmp = getNameValueListAsJson(properties);
        properties.remove(o);
        return tmp;
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

	public List<SugarLead> searchLeads(CrmSession session, CrmRecord query,
			int offset, int maxResults) throws IOException {
		System.out.println("searchLeads called, not yet implemented");
		throw new RuntimeException("Not yet implemented");
	}

	public CrmRecord archiveLeadEmail(CrmSession session, String leadId,
			ArchivedEmail email) throws IOException {
		// CrmRecord entry = setEntry(session, email, "Emails");
		// createRelationship(session, "Leads", leadId, "Emails",
		// entry.getId());

		impl.archiveEmail(session, email);

		return email;
	}


	public CrmRecord addNoteToLead(CrmSession session, String leadId,
			CrmRecord note) {
		CrmRecord entry = setEntry(session, note, "Notes");
		this.createRelationship(session, "Leads", leadId, "notes",
				entry.getId());
		return entry;
	}

    public List<CrmRecord> getReferenceData(SugarSession session, String string) {
        // TODO Auto-generated method stub
        return null;
    }

}
