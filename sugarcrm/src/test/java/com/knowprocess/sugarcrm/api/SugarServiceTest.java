package com.knowprocess.sugarcrm.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class SugarServiceTest {

	private static SugarSession session;

	private static SugarService svc;

	@BeforeClass
	public static void setUpClass() {
		session = new SugarSession("admin", "sugar",
				"http://localhost/sugarcrm");
		svc = new SugarService();
	}

	@Test
	public void testLogin() {
		svc.login(session);
		System.out.println("session id: " + session.getSessionId());
		assertNotNull(session.getSessionId());
		assertTrue(session.getSessionId().matches("[a-z,0-9]{16,32}"));
	}

	@Test
	public void testCreateAccount() {
		svc.login(session);
		System.out.println("session id: " + session.getSessionId());
		assertNotNull(session.getSessionId());
		assertTrue(session.getSessionId().matches("[a-z,0-9]{16,32}"));

		SugarAccount acct = new SugarAccount();
		acct.setName("TIMCO");
		svc.createAccount(session, acct);
		System.out.println("acct:" + acct.getNameValueListAsJson());
		assertNotNull(acct.getId());
	}

	@Test
	public void testCreateContact() {
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
	}

	@Test
	public void testCreateContactAndLinkedAccount() {
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
	}
}
