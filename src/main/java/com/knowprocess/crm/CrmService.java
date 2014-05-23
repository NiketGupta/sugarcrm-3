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

import java.io.IOException;
import java.util.List;

import com.knowprocess.sugarcrm.api.SugarLead;
import com.knowprocess.sugarcrm.api.SugarNote;
import com.knowprocess.sugarcrm.api.SugarSession;

public interface CrmService {

	CrmSession login(CrmSession session);

	CrmRecord createAccount(CrmSession session, CrmRecord acct);

	CrmRecord createContact(CrmSession session, CrmRecord contact);

	CrmRecord createAccountWithPrimeContact(CrmSession session,
			CrmRecord contact, CrmRecord acct);

	CrmRecord createLead(CrmSession session, CrmRecord lead);

	CrmRecord getContact(CrmSession session, String contactId)
			throws IOException;

	List<CrmRecord> searchContacts(CrmSession session, CrmRecord query,
			int offset, int maxResults) throws IOException;

	List<SugarLead> searchLeads(CrmSession session, CrmRecord query,
			int offset, int maxResults) throws IOException;

	List<SugarNote> searchNotes(SugarSession session, CrmRecord query,
			int offset, int maxResults) throws IOException;

	String toJson(List<CrmRecord> list);

	//CrmRecord archiveLeadEmail(CrmSession session, String leadId,
	//		ArchivedEmail email) throws IOException;

	CrmRecord addNoteToLead(CrmSession session, String leadId, CrmRecord note);

    List<CrmRecord> getReferenceData(SugarSession session, String string);

}