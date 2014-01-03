package com.knowprocess.sugarcrm.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.Test;

public class SugarAccountTest {

	@Test
	public void testJavaScriptForParsingContact() {
		String SCRIPT = "importPackage(Packages.com.knowprocess.sugarcrm.api);"
				+ "var acct = new SugarAccount();"
				+ "acct.setCustom('companyNumber','12345678');";

		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");

		// evaluate JavaScript code
		try {
			engine.eval(SCRIPT);
			SugarAccount acct = (SugarAccount) engine.get("acct");
			System.out.println("acct parsed: " + acct);
			assertEquals("12345678", acct.getCustom("companyNumber"));
		} catch (ClassCastException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (ScriptException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}


}
