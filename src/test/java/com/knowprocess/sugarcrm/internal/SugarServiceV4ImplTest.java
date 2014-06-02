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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.Assume;
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
        assertEquals(session.getSugarUrl()
                + SugarServiceV4Impl.SVC_URL_FRAGMENT,
				svc.getServiceUrl(session.getSugarUrl()));
	}

	@Test
	public void testGetLoginPayload() {
		try {
			String payload = svc.getLoginPayload(session);
			System.out.println("payload: " + payload);
			assertEquals(
					"method=login&input_type=json&response_type=json&rest_data={\""
                            + "user_auth\":{\"user_name\":\""
                            + session.getUsername()
                            + "\",\"version\":\".01\",\"username\":\""
                            + session.getUsername()
                            + "\",\"password\":\""
							+ svc.hash(session.getPassword())
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
    public void testSvcParseRecordFromJson() {
		try {
			String json = "{\"name\":\"first_name\",\"value\":\"John\"},"
					+ "{\"name\":\"title\",\"value\":\"Mr\"},"
					+ "{\"name\":\"last_name\",\"value\":\"Braithwaite\"}";
			CrmRecord record = svc.parseRecordFromSugarRepresentation(json);
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
    public void testParseContactFromSugarSerialisation() {
		try {
			String json = "{\"entry_list\":["
					+ "{\"id\":\"52cd2f26-6497-6ac6-8389-52d69314b6cc\","
					+ "\"module_name\":\"Contacts\",\"name_value_list\":{"
					+ "\"id\":{\"name\":\"id\",\"value\":\"52cd2f26-6497-6ac6-8389-52d69314b6cc\"},"
					+ "\"first_name\":{\"name\":\"first_name\",\"value\":\"John\"},"
					+ "\"last_name\":{\"name\":\"last_name\",\"value\":\"Braithwaite\"},"
                    + "\"salutation\":{\"name\":\"salutation\",\"value\":\"Mr\"},"
                    + "\"company_no_c\":{\"name\":\"company_no_c\",\"value\":\"\"}}"
					+ "}],"
					+ "\"relationship_list\":[]}";
			CrmRecord record = svc.parseRecordFromSugarRepresentation(json);
			assertNotNull(record);
			assertEquals("John", record.getCustom("first_name"));
			assertEquals("Braithwaite", record.getCustom("last_name"));
			assertEquals("Mr", record.getCustom("salutation"));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getClass().getName() + ":" + e.getMessage());
		}
	}

    @Test
    public void testParseAccountFromSugarSerialisation() {
        try {
            String json = "{\"entry_list\":["
                    + "{\"id\":\"52cd2f26-6497-6ac6-8389-52d69314b6cc\","
                    + "\"module_name\":\"Accounts\",\"name_value_list\":{"
                    + "\"id\":{\"name\":\"id\",\"value\":\"52cd2f26-6497-6ac6-8389-52d69314b6cc\"},"
                    + "\"name\":{\"name\":\"name\",\"value\":\"Ergo Digital Ltd\"},"
                    + "\"year_established_c\":{\"name\":\"year_established_c\",\"value\":\"2008\"},"
                    + "\"employees\":{\"name\":\"employees\",\"value\":\"5\"},"
                    + "\"business_website_c\":{\"name\":\"business_website_c\",\"value\":\"http:\\/\\/www.ergodigital.com\\/\"},"
                    + "\"company_no_c\":{\"name\":\"company_no_c\",\"value\":\"\"}}"
                    + "}]," + "\"relationship_list\":[]}";
            CrmRecord record = svc.parseRecordFromSugarRepresentation(json);
            assertNotNull(record);
            assertEquals("Ergo Digital Ltd", record.getCustom("name"));
            assertEquals(5, record.getCustom("employees"));
            assertEquals(2008, record.getCustom("year_established_c"));
            assertEquals("http://www.ergodigital.com/",
                    record.getCustom("business_website_c"));
            assertEquals(null, record.getCustom("company_no_c"));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getClass().getName() + ":" + e.getMessage());
        }
    }

    @Test
	public void testParseRecordsFromJsonArray() {
		try {
			String json = "{\"result_count\":20,\"total_count\":\"111\",\"next_offset\":40,"
					+ "\"entry_list\":["
					+ "{\"id\":\"4992b0c1-c058-cfd3-845c-52c55a433d0c\","
					+ "\"module_name\":\"Contacts\",\"name_value_list\":{"
					+ "\"assigned_user_name\":{\"name\":\"assigned_user_name\",\"value\":\"\"},"
					+ "\"modified_by_name\":{\"name\":\"modified_by_name\",\"value\":\"Tim Stephenson\"},"
					+ "\"created_by_name\":{\"name\":\"created_by_name\",\"value\":\"Tim Stephenson\"},"
					+ "\"id\":{\"name\":\"id\",\"value\":\"4992b0c1-c058-cfd3-845c-52c55a433d0c\"},"
					+ "\"name\":{\"name\":\"name\",\"value\":\"John Braithwaite\"},"
					+ "\"date_entered\":{\"name\":\"date_entered\",\"value\":\"2014-01-02 12:24:34\"},"
					+ "\"date_modified\":{\"name\":\"date_modified\",\"value\":\"2014-01-02 12:24:34\"},"
					+ "\"modified_user_id\":{\"name\":\"modified_user_id\",\"value\":\"1\"},"
					+ "\"created_by\":{\"name\":\"created_by\",\"value\":\"1\"},"
					+ "\"description\":{\"name\":\"description\",\"value\":\"\"},"
					+ "\"deleted\":{\"name\":\"deleted\",\"value\":\"0\"},"
					+ "\"assigned_user_id\":{\"name\":\"assigned_user_id\",\"value\":\"\"},"
					+ "\"salutation\":{\"name\":\"salutation\",\"value\":\"Mr\"},"
					+ "\"first_name\":{\"name\":\"first_name\",\"value\":\"John\"},"
					+ "\"last_name\":{\"name\":\"last_name\",\"value\":\"Braithwaite\"},"
					+ "\"full_name\":{\"name\":\"full_name\",\"value\":\"John Braithwaite\"}}},"
					+ "{\"id\":\"id-the-second\"}" + "]}";
//					,"title":{"name":"title","value":"Mr"},"department":{"name":"department","value":""},"do_not_call":{"name":"do_not_call","value":"0"},"phone_home":{"name":"phone_home","value":""},"email":{"name":"email","value":""},"phone_mobile":{"name":"phone_mobile","value":""},"phone_work":{"name":"phone_work","value":""},"phone_other":{"name":"phone_other","value":""},"phone_fax":{"name":"phone_fax","value":""},"email1":{"name":"email1","value":""},"email2":{"name":"email2","value":""},"invalid_email":{"name":"invalid_email","value":""},"email_opt_out":{"name":"email_opt_out","value":""},"primary_address_street":{"name":"primary_address_street","value":""},"primary_address_street_2":{"name":"primary_address_street_2","value":""},"primary_address_street_3":{"name":"primary_address_street_3","value":""},"primary_address_city":{"name":"primary_address_city","value":""},"primary_address_state":{"name":"primary_address_state","value":""},"primary_address_postalcode":{"name":"primary_address_postalcode","value":""},"primary_address_country":{"name":"primary_address_country","value":""},"alt_address_street":{"name":"alt_address_street","value":""},"alt_address_street_2":{"name":"alt_address_street_2","value":""},"alt_address_street_3":{"name":"alt_address_street_3","value":""},"alt_address_city":{"name":"alt_address_city","value":""},"alt_address_state":{"name":"alt_address_state","value":""},"alt_address_postalcode":{"name":"alt_address_postalcode","value":""},"alt_address_country":{"name":"alt_address_country","value":""},"assistant":{"name":"assistant","value":""},"assistant_phone":{"name":"assistant_phone","value":""},"email_addresses_non_primary":{"name":"email_addresses_non_primary","value":""},"email_and_name1":{"name":"email_and_name1","value":"John Braithwaite &lt;&gt;"},"lead_source":{"name":"lead_source","value":""},"account_name":{"name":"account_name","value":"Ergo Digital"},"account_id":{"name":"account_id","value":"8b97f06a-2cea-8b26-0a17-52c55a4a1be9"},"opportunity_role_fields":{"name":"opportunity_role_fields","value":"                                                                                                                                                                                                                                                              "},"opportunity_role_id":{"name":"opportunity_role_id","value":""},"opportunity_role":{"name":"opportunity_role","value":""},"reports_to_id":{"name":"reports_to_id","value":""},"report_to_name":{"name":"report_to_name","value":""},"birthdate":{"name":"birthdate","value":false},"campaign_id":{"name":"campaign_id","value":""},"campaign_name":{"name":"campaign_name","value":""}}]";
			List<CrmRecord> records = svc.parseRecordsFromJson(json);
			assertTrue("Wrong number of records parsed", records.size() == 2);
			CrmRecord record = records.get(0);
			assertNotNull(record);
			assertEquals("John", record.getCustom("first_name"));
			assertEquals("Braithwaite", record.getCustom("last_name"));
			assertEquals("Mr", record.getCustom("salutation"));
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
            String payload = svc.getModuleFields(session, "Accounts");
			System.out.println("payload: " + payload);
			// TODO assertions
		} catch (IllegalStateException e) {
			// Assume dev server not available
            e.printStackTrace();
			Assume.assumeTrue(e.getMessage(), true);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getClass().getName() + ":" + e.getMessage());
		}
	}

    @Test
    public void testAvailableModules() {
        try {
            session.setSessionId(svc.login(session));
            System.out.println("session id: " + session.getSessionId());
            String payload = svc.getAvailableModules(session, null);
            System.out.println("payload: " + payload);
            // TODO assertions
        } catch (IllegalStateException e) {
            // Assume dev server not available
            e.printStackTrace();
            Assume.assumeTrue(e.getMessage(), true);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getClass().getName() + ":" + e.getMessage());
        }
    }
}
