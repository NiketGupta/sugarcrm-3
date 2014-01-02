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
package com.knowprocess.sugarcrm.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.knowprocess.crm.CrmRecord;
import com.knowprocess.sugarcrm.api.SugarSession;

public class SugarServiceV4ImplTest {

	private static final String SUGAR_BASE_URL = "http://localhost/sugarcrm/";

	private static SugarSession session;

	private static SugarServiceV4Impl svc;

	private static String pwd;

	private static String url;

	@BeforeClass
	public static void setUpClass() {
		String usr = System.getProperty("sugar.username");
		pwd = System.getProperty("sugar.password");
		url = System.getProperty("sugar.url");
		session = new SugarSession(usr == null ? "admin" : usr,
				pwd == null ? "sugar" : pwd,
				url == null ? SUGAR_BASE_URL : url);
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
			assertEquals("ada15bd1a5ddf0b790ae1dcfd05a1e70", svc.hash("sugar"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			fail(e.getClass().getName() + ":" + e.getMessage());
		}
	}

	@Test
	public void testGetServiceUrl() {
		assertEquals(url + "service/v4/rest.php",
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
							+ "\"password\":\""
							+ svc.hash(pwd)
							+ "\"},\"application_name\":\"com.knowprocess.sugarcrm.api.SugarService\"}",
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
					+ "rest_data={\"session\":\"" + session.getSessionId()
					+ "\",\"module_name\":\"Accounts\","
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
							+ "&rest_data={\"session\":\""
							+ session.getSessionId()
							+ "\",\"module_name\":\"Accounts\","
							+ "\"module_id\": \"acctId\","
							+ "\"link_field_name\": \"contacts\","
							+ "\"related_ids\": [\"contactId\"]}", payload);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getClass().getName() + ":" + e.getMessage());
		}
	}

	@Test
	public void testParseRecordFromJson() {
		try {
			String json = "{\"name\":\"first_name\",\"value\":\"John\"},"
					+ "{\"name\":\"title\",\"value\":\"Mr\"},"
					+ "{\"name\":\"last_name\",\"value\":\"Braithwaite\"}";
			CrmRecord record = svc.parseRecordFromJson(json);
			assertNotNull(record);
			assertEquals("John", record.getCustom("first_name"));
			assertEquals("Braithwaite", record.getCustom("last_name"));
			assertEquals("Mr", record.getCustom("title"));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getClass().getName() + ":" + e.getMessage());
		}
	}

	@Test
	public void testGetModuleFields() {
		try {
			session.setSessionId(svc.login(session));
			System.out.println("session id: " + session.getSessionId());
			String payload = svc.getModuleFields(session, "industry");
			System.out.println("payload: " + payload);
			// TODO assertions
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getClass().getName() + ":" + e.getMessage());
		}
	}

}
