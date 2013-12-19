package com.knowprocess.sugarcrm.api;

import java.io.IOException;

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

	public SugarAccount createAccount(SugarSession session, SugarAccount acct) {
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
		// TODO call set_entry
		return contact;
	}

	public SugarContact createAccountWithPrimeContact(SugarSession session,
			SugarContact contact, SugarAccount acct) {
		createContact(session, contact);
		createAccount(session, acct);

		// TODO link contact and account.
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
}