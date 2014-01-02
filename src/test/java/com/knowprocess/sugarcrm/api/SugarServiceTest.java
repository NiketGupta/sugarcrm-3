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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import com.knowprocess.crm.CrmService;

public class SugarServiceTest {

	private static SugarSession session;

	private static CrmService svc;

	@BeforeClass
	public static void setUpClass() {
		session = new SugarSession("admin", "sugar",
				"http://localhost/sugarcrm");
		// session = new SugarSession("TimS", "OHO02G",
		// "http://sugar.syncapt.com/");
		svc = new SugarService();
	}

	@Test
	public void testLogin() {
		try {
			svc.login(session);
			System.out.println("session id: " + session.getSessionId());
			assertNotNull(session.getSessionId());
			assertTrue(session.getSessionId().matches("[a-z,0-9]{16,32}"));
		} catch (IllegalStateException e) {
			Assume.assumeTrue(e.getMessage(), true);
		}
	}

	@Test
	public void testCreateAccount() {
		try {
			svc.login(session);
			System.out.println("session id: " + session.getSessionId());
			assertNotNull(session.getSessionId());
			assertTrue(session.getSessionId().matches("[a-z,0-9]{16,32}"));

			SugarAccount acct = new SugarAccount();
			acct.setName("Tim Co.");
			svc.createAccount(session, acct);
			System.out.println("acct:" + acct.getNameValueListAsJson());
			assertNotNull(acct.getId());
		} catch (IllegalStateException e) {
			Assume.assumeTrue(e.getMessage(), true);
		}
	}

	@Test
	public void testCreateContact() {
		try {
			svc.login(session);
			System.out.println("session id: " + session.getSessionId());
			assertNotNull(session.getSessionId());
			assertTrue(session.getSessionId().matches("[a-z,0-9]{16,32}"));

			SugarContact contact = new SugarContact();
			contact.setFirstName("Tim");
			contact.setLastName("Stephenson");
			contact.setTitle("Mr");
			svc.createContact(session, contact);
			System.out.println("contact:" + contact.getNameValueListAsJson());
			assertNotNull(contact.getId());
		} catch (IllegalStateException e) {
			Assume.assumeTrue(e.getMessage(), true);
		}
	}

	@Test
	public void testCreateContactAndLinkedAccount() {
		try {
			svc.login(session);
			System.out.println("session id: " + session.getSessionId());
			assertNotNull(session.getSessionId());
			assertTrue(session.getSessionId().matches("[a-z,0-9]{16,32}"));

			SugarContact contact = new SugarContact();
			contact.setFirstName("John");
			contact.setLastName("Braithwaite");
			contact.setTitle("Mr");

			SugarAccount acct = new SugarAccount();
			acct.setName("Ergo Digital");

			svc.createAccountWithPrimeContact(session, contact, acct);

			System.out.println("contact:" + contact.getNameValueListAsJson());
			assertNotNull(contact.getId());
		} catch (IllegalStateException e) {
			Assume.assumeTrue(e.getMessage(), true);
		}
	}
}
