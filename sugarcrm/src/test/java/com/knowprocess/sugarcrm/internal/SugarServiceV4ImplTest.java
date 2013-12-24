package com.knowprocess.sugarcrm.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.knowprocess.sugarcrm.api.SugarSession;

public class SugarServiceV4ImplTest {

	private static final String PASSWORD = "sugar";

	private static final String SUGAR_BASE_URL = "http://localhost/sugarcrm/";

	private static SugarSession session;

	private static SugarServiceV4Impl svc;

	@BeforeClass
	public static void setUpClass() {
		session = new SugarSession("admin", PASSWORD, SUGAR_BASE_URL);
		try {
			svc = new SugarServiceV4Impl();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getClass().getName() + ":" + e.getMessage());
		}
	}

	@Test
	public void testHashedPassword() {
		try {
			assertEquals("ada15bd1a5ddf0b790ae1dcfd05a1e70", svc.hash(PASSWORD));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			fail(e.getClass().getName() + ":" + e.getMessage());
		}
	}

	@Test
	public void testGetServiceUrl() {
		assertEquals(SUGAR_BASE_URL + "service/v4/rest.php",
				svc.getServiceUrl(session.getSugarUrl()));
	}

	@Test
	public void testGetLoginPayload() {
		try {
			String payload = svc.getLoginPayload(session);
			System.out.println("payload: " + payload);
			assertEquals(
					"method=login&input_type=json&response_type=json&rest_data={\""
							+ "user_auth\":{\"user_name\":\"admin\","
							+ "\"version\":\".01\","
							+ "\"username\":\"admin\","
							+ "\"password\":\"ada15bd1a5ddf0b790ae1dcfd05a1e70\"},"
							+ "\"application_name\":\"com.knowprocess.sugarcrm.api.SugarService\"}",
					payload);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getClass().getName() + ":" + e.getMessage());
		}
	}

	@Test
	public void testSetEntryPayload() {
		try {
			String nameValueList = "{ \"name\":\"name\", \"value\":\"ACME Inc.\" }";
			String payload = svc.getSetEntryPayload(session, "Accounts",
					nameValueList);
			System.out.println("payload: " + payload);
			assertEquals("method=set_entry&input_type=json&response_type=json&"
					+ "rest_data={\"session\":\"null\","
					+ "\"module_name\":\"Accounts\","
					+ "\"name_value_list\":[{ \"name\":\"name\", "
					+ "\"value\":\"ACME Inc.\" }]}", payload);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getClass().getName() + ":" + e.getMessage());
		}
	}

	@Test
	public void testSetRelationshipPayload() {
		try {
			String contactId = "contactId";
			String acctId = "acctId";
			String payload = svc.getSetRelationshipPayload(session, "Accounts",
					acctId, "contacts", contactId);
			System.out.println("payload: " + payload);
			assertEquals(
					"method=set_relationship&input_type=json&response_type=json"
					+ "&rest_data={\"session\":\"null\","
					+ "\"module_name\":\"Accounts\",\"module_id\": \"acctId\","
					+ "\"link_field_name\": \"contacts\","
					+ "\"related_ids\": [\"contactId\"]}",
					payload);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getClass().getName() + ":" + e.getMessage());
		}
	}

}
