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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.knowprocess.crm.CrmPerson;
import com.knowprocess.crm.CrmRecord;
import com.knowprocess.crm.CrmService;

public class SugarServiceTest {

    private static final int D_EBITDA = 54800;

    private static final String D_TITLE = "Managing Director";

    private static final String D_LAST_NAME = "Braithwaite";

    private static final String D_FIRST_NAME = "John TEST";

    private static SugarSession session;

	private static CrmService svc;

	private CrmPerson contact;

	private SugarLead lead;

	@BeforeClass
	public static void setUpClass() {
		String usr = System.getProperty("sugar.username");
		String pwd = System.getProperty("sugar.password");
		String url = System.getProperty("sugar.url");
		session = new SugarSession(usr == null ? "admin" : usr,
				pwd == null ? "sugar" : pwd,
				url == null ? "http://localhost/sugarcrm" : url);
		// session = new SugarSession(usr == null ? "admin" : usr,
		// pwd == null ? "gfjusmcnniqj" : pwd,
		// url == null ? "http://www.trakeocorp.com" : url);
		// session = new SugarSession(usr == null ? "TimS" : usr,
		// pwd == null ? "RnFh31BJ%f#" : pwd,
		// url == null ? "http://sugar.syncapt.com" : url);
		svc = new SugarService();
	}

	@Before
	public void setUp() {
		contact = new SugarContact();
        contact.setFirstName(D_FIRST_NAME);
        contact.setLastName(D_LAST_NAME);
        contact.setTitle(D_TITLE);
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

			CrmPerson contact = new SugarContact();
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
    public void testCreateContactAndLinkedAccountThenUpdate() {
		try {
			svc.login(session);

			SugarAccount acct = new SugarAccount();
            acct.setName("Ergo Digital TEST");

			svc.createAccountWithPrimeContact(session, contact, acct);

			System.out.println("contact created with id:" + contact.getId());
			assertNotNull(contact.getId());

			// Check can find contact by its id
            SugarContact contact2 = new SugarContact(svc.getContact(session,
                    contact.getId()));
            assertContacts(contact, contact2);
            System.out.println(contact2.getCustom("account_id"));
            assertNotNull(contact2.getCustom("account_id"));
            assertEquals(contact2.getCustom("account_id"),
                    contact2.getAccountId());

			// Check can find contact by newly created id
			SugarContact queryObject = new SugarContact();
			queryObject.setId(contact.getId());
			List<CrmRecord> results = svc.searchContacts(session, queryObject,
					0, 10);
			assertTrue(results.size() >= 1);
            SugarContact newlyCreatedContact = new SugarContact(results.get(0));
            assertContacts(contact, newlyCreatedContact);

            // Now update ....
            contact.setFirstName(contact.getFirstName()
                    + " UPDATED");
            acct.setCustom("val_quote_low_c", D_EBITDA * 6);
            acct.setCustom("val_quote_mid_c", D_EBITDA * 10);
            acct.setCustom("val_quote_high_c", D_EBITDA * 14);
            newlyCreatedContact.setFirstName(contact.getFirstName());
            CrmRecord updatedContact = svc.updateAccountWithPrimeContact(
                    session, newlyCreatedContact, acct);
            assertEquals(newlyCreatedContact, updatedContact);
            assertContacts(contact, newlyCreatedContact);
            
            // check can fetch associated account
            CrmRecord updatedAccount = svc.getAccount(session, acct.getId());
            assertEquals(acct.getCustom("val_quote_low_c"),
                    updatedAccount.getCustom("val_quote_low_c"));
            assertEquals(acct.getCustom("val_quote_med_c"),
                    updatedAccount.getCustom("val_quote_med_c"));
            assertEquals(acct.getCustom("val_quote_high_c"),
                    updatedAccount.getCustom("val_quote_high_c"));
        } catch (IllegalStateException e) {
			System.out.println(e.getMessage());
			Assume.assumeTrue(e.getMessage(), true);
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	@Ignore
	/*
	 * This fails with 500 on local and on prod with: Unsupported operand types
	 * in <b>/var/sites
	 * /s/sugar.syncapt.com/public_html/service/v4/SugarWebServiceImplv4.php</b>
	 * on line <b>315</b>
	 */
	public void searchContactsByNameTest() {
		try {
			svc.login(session);

			// Check can find contact by its properties
			SugarContact queryObject = new SugarContact();
			queryObject.setFirstName("John");
			// queryObject.setLastName("Braithwaite");
			List<CrmRecord> results = svc.searchContacts(session, queryObject,
					0, 10);

			assertTrue(results.size() >= 1);

			for (CrmRecord crmRecord : results) {
				System.out.println("Checking: " + crmRecord.toJson());
				assertContacts(contact, new SugarContact(crmRecord));
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	private void assertContacts(CrmPerson expected, SugarContact crmRecord) {
		System.out.println("returned: " + crmRecord.toJson());
		// This is what we get back
		// {"name":"opportunity_role_id","value":""},{"name":"primary_address_city","value":""},{"name":"assigned_user_id","value":""},{"name":"sync_contact","value":""},{"name":"primary_address_postalcode","value":""},{"name":"first_name","value":"John"},{"name":"phone_fax","value":""},{"name":"assistant","value":""},{"name":"invalid_email","value":""},{"name":"description","value":""},{"name":"ed_user_name","value":"d_user_name"},{"name":"accept_status_id","value":""},{"name":"created_by","value":"1"},{"name":"assistant_phone","value":""},{"name":"account_id","value":"1fc672a4-3d13-2b94-d94b-52cb1490d2e3"},{"name":"alt_address_state","value":""},{"name":"campaign_name","value":""},{"name":"modified_user_id","value":"1"},{"name":"deleted","value":"0"},{"name":"campaign_id","value":""},{"name":"report_to_name","value":""},{"name":"account_name","value":"Ergo Digital"},{"name":"phone_home","value":""},{"name":"alt_address_street","value":""},{"name":"lead_source","value":""},{"name":"email","value":""},{"name":"last_name","value":"Braithwaite"},{"name":"do_not_call","value":"0"},{"name":"alt_address_country","value":""},{"name":"assigned_user_name","value":""},{"name":"primary_address_street_2","value":""},{"name":"primary_address_street_3","value":""},{"name":"alt_address_city","value":""},{"name":"date_entered","value":"2014-01-06 20:37:37"},{"name":"department","value":""},{"name":"reports_to_id","value":""},{"name":"primary_address_country","value":""},{"name":"created_by_name","value":"Tim Stephenson"},{"name":"email_opt_out","value":""},{"name":"accept_status_name","value":""},{"name":"id","value":"c9a4ae4a-05a0-273f-50ef-52cb1478b1a5"},{"name":"title","value":"Mr"},{"name":"alt_address_postalcode","value":""},{"name":"name","value":"John Braithwaite"},{"name":"birthdate","value":"birthdate"},{"name":"opportunity_role","value":""},{"name":"primary_address_state","value":""},{"name":"alt_address_street_3","value":""},{"name":"c_accept_status_fields","value":""},{"name":"email_addresses_non_primary","value":""},{"name":"date_modified","value":"2014-01-06 20:37:37"},{"name":"modified_by_name","value":"Tim Stephenson"},{"name":"phone_work","value":""},{"name":"list","value":"ist"},{"name":"email1","value":""},{"name":"opportunity_role_fields","value":""},{"name":"email2","value":""},{"name":"primary_address_street","value":""},{"name":"alt_address_street_2","value":""},{"name":"m_accept_status_fields","value":""},{"name":"phone_other","value":""},{"name":"email_and_name1","value":""},{"name":"salutation","value":""},{"name":"9a4ae4a-05a0-273f-50ef-52cb1478b1a5","value":"a4ae4a-05a0-273f-50ef-52cb1478b1a5"},{"name":"phone_mobile","value":""},{"name":"full_name","value":"John Braithwaite"}

		assertNotNull(crmRecord);
        assertEquals(expected.getId(), crmRecord.getId());
		assertEquals(expected.getFirstName(), crmRecord.getFirstName());
		assertEquals(expected.getLastName(), crmRecord.getLastName());
		assertEquals(expected.getTitle(), crmRecord.getTitle());
	}

	@Test
	public void testCreateLead() {
		try {
			svc.login(session);

			lead = new SugarLead();
			lead.setFirstName("Tim");
			lead.setLastName("Stephenson");
			lead.setTitle("Mr");
			svc.createLead(session, lead);
			System.out.println("lead:" + lead.getNameValueListAsJson());
			assertNotNull(lead.getId());

			// Check can find contact by newly created id
			SugarLead queryObject = new SugarLead();
			queryObject.setId(lead.getId());
			// List<SugarLead> results = svc.searchLeads(session, queryObject,
			// 0, 10);
			// assertTrue(results.size() == 1);
			// assertLeads(lead, new SugarLead(results.get(0)));

			// Search on broader range of criteria
			// queryObject = new SugarLead();
			// queryObject.setFirstName(lead.getFirstName());
			// results = svc.searchLeads(session, queryObject, 0, 10);
			// assertTrue(results.size() == 1);
			// assertLeads(lead, new SugarLead(results.get(0)));

			// add an email to this lead
			// ArchivedEmail email = new ArchivedEmail(session.getUsername(),
			// "fred@example.com", "Hello Fred!",
			// "It's been ages, we really need to get together for a round of golf. Call me!");
			// CrmRecord archivedEmail = svc.archiveLeadEmail(session,
			// lead.getId(), email);
			// assertNotNull(archivedEmail.getId());
			// System.out.println("archived email: " + archivedEmail.getId());

			CrmRecord note = new CrmRecord();
			note.setCustom("name", "Hello Fred!");
			note.setCustom("description",
					"It's been ages, we really need to get together for a round of golf. Call me!");

			CrmRecord noteCreated = svc.addNoteToLead(session, lead.getId(),
					note);
			assertEquals(note.getCustom("name"), noteCreated.getCustom("name"));
			assertEquals(note.getCustom("description"),
					noteCreated.getCustom("description"));

			CrmRecord query = new CrmRecord();
			query.setId(noteCreated.getId());
			List<SugarNote> notes = svc.searchNotes(session, query, 0, 1);
			System.out.println(String.format("Found %1$s notes", notes.size()));
			assertTrue(notes.size() >= 1);
			System.out.println(String.format("Note %1$s: %2$s", notes.get(0)
					.getId(), notes.get(0).toString()));
			assertEquals(noteCreated.getId(), notes.get(0).getId());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IllegalStateException e) {
			Assume.assumeTrue(e.getMessage(), true);
		}
	}

	private void assertLeads(SugarLead expected, SugarLead crmRecord) {
		System.out.println("returned: " + crmRecord.toJson());
		// This is what we get back
		// {"name":"opportunity_role_id","value":""},{"name":"primary_address_city","value":""},{"name":"assigned_user_id","value":""},{"name":"sync_contact","value":""},{"name":"primary_address_postalcode","value":""},{"name":"first_name","value":"John"},{"name":"phone_fax","value":""},{"name":"assistant","value":""},{"name":"invalid_email","value":""},{"name":"description","value":""},{"name":"ed_user_name","value":"d_user_name"},{"name":"accept_status_id","value":""},{"name":"created_by","value":"1"},{"name":"assistant_phone","value":""},{"name":"account_id","value":"1fc672a4-3d13-2b94-d94b-52cb1490d2e3"},{"name":"alt_address_state","value":""},{"name":"campaign_name","value":""},{"name":"modified_user_id","value":"1"},{"name":"deleted","value":"0"},{"name":"campaign_id","value":""},{"name":"report_to_name","value":""},{"name":"account_name","value":"Ergo Digital"},{"name":"phone_home","value":""},{"name":"alt_address_street","value":""},{"name":"lead_source","value":""},{"name":"email","value":""},{"name":"last_name","value":"Braithwaite"},{"name":"do_not_call","value":"0"},{"name":"alt_address_country","value":""},{"name":"assigned_user_name","value":""},{"name":"primary_address_street_2","value":""},{"name":"primary_address_street_3","value":""},{"name":"alt_address_city","value":""},{"name":"date_entered","value":"2014-01-06 20:37:37"},{"name":"department","value":""},{"name":"reports_to_id","value":""},{"name":"primary_address_country","value":""},{"name":"created_by_name","value":"Tim Stephenson"},{"name":"email_opt_out","value":""},{"name":"accept_status_name","value":""},{"name":"id","value":"c9a4ae4a-05a0-273f-50ef-52cb1478b1a5"},{"name":"title","value":"Mr"},{"name":"alt_address_postalcode","value":""},{"name":"name","value":"John Braithwaite"},{"name":"birthdate","value":"birthdate"},{"name":"opportunity_role","value":""},{"name":"primary_address_state","value":""},{"name":"alt_address_street_3","value":""},{"name":"c_accept_status_fields","value":""},{"name":"email_addresses_non_primary","value":""},{"name":"date_modified","value":"2014-01-06 20:37:37"},{"name":"modified_by_name","value":"Tim Stephenson"},{"name":"phone_work","value":""},{"name":"list","value":"ist"},{"name":"email1","value":""},{"name":"opportunity_role_fields","value":""},{"name":"email2","value":""},{"name":"primary_address_street","value":""},{"name":"alt_address_street_2","value":""},{"name":"m_accept_status_fields","value":""},{"name":"phone_other","value":""},{"name":"email_and_name1","value":""},{"name":"salutation","value":""},{"name":"9a4ae4a-05a0-273f-50ef-52cb1478b1a5","value":"a4ae4a-05a0-273f-50ef-52cb1478b1a5"},{"name":"phone_mobile","value":""},{"name":"full_name","value":"John Braithwaite"}

		assertNotNull(crmRecord);
		assertEquals("Tim", crmRecord.getCustom("first_name"));
		assertEquals(expected.getFirstName(), crmRecord.getFirstName());
		assertEquals("Stephenson", crmRecord.getCustom("last_name"));
		assertEquals(expected.getLastName(), crmRecord.getLastName());
		assertEquals("Mr", crmRecord.getCustom("title"));
		assertEquals(expected.getTitle(), crmRecord.getTitle());
	}

	@Test
	@Ignore
	// Not yet implemented
	public void searchLeadsByNameTest() {
		try {
			svc.login(session);

			// Check can find contact by its properties
			SugarLead queryObject = new SugarLead();
			queryObject.setFirstName("Tim");
			// queryObject.setLastName("Stephenson");
			List<SugarLead> results = svc.searchLeads(session, queryObject, 0,
					10);

			assertTrue(results.size() >= 1);

			for (SugarLead crmRecord : results) {
				System.out.println("Checking: " + crmRecord.toJson());
				assertLeads(lead, crmRecord);
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

    @Test
    @Ignore
    public void getReferenceDataTest() {
        try {
            svc.login(session);

            List<CrmRecord> results = svc.getReferenceData(session, "Contacts");

            assertTrue(results.size() >= 1);

            for (CrmRecord crmRecord : results) {
                System.out.println("Checking: " + crmRecord.toJson());
                // assertLeads(lead, crmRecord);
            }
        } catch (IllegalStateException e) {
            System.err
                    .println("No credentials to connect to Sugar, assume test ok");
            Assume.assumeTrue(e.getMessage(), true);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
