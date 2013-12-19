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
}
