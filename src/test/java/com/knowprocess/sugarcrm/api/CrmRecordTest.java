package com.knowprocess.sugarcrm.api;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.knowprocess.crm.CrmRecord;

public class CrmRecordTest {

	@Test
	public void testToJson() {
		SugarContact contact = new SugarContact();
		contact.setFirstName("Nick");
		contact.setLastName("Cave");
		String json = contact.toJson();
		System.out.println("JSON created: " + json);
		assertTrue(json.contains("\"first_name\":\"Nick\""));
		assertTrue(json.contains("\"last_name\":\"Cave\""));
	}

	@Test
	public void testToJsonArray() {
		SugarContact guitar = new SugarContact();
		guitar.setFirstName("Bernard");
		guitar.setLastName("Sumner");
		SugarContact bass = new SugarContact();
		bass.setFirstName("Peter");
		bass.setLastName("Hook");

		List<CrmRecord> list = new ArrayList<CrmRecord>();
		list.add(guitar);
		list.add(bass);

		String json = new SugarService().toJson(list);
		System.out.println("JSON created: " + json);
		assertTrue(json.contains("\"first_name\":\"Bernard\""));
		assertTrue(json.contains("\"last_name\":\"Sumner\""));
	}

}
