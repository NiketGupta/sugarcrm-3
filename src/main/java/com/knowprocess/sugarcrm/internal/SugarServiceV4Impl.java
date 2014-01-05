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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import com.knowprocess.crm.CrmRecord;
import com.knowprocess.crm.CrmSession;
import com.knowprocess.sugarcrm.api.SugarAuthenticationException;
import com.knowprocess.sugarcrm.api.SugarException;
import com.knowprocess.sugarcrm.api.SugarService;
import com.knowprocess.sugarcrm.api.SugarSession;

public class SugarServiceV4Impl {

	private static final String NAME_MARKER = "\"name\":\"";
	private static final String ID_MARKER = "\"id\":\"";
	private static final String VALUE_MARKER = "\"value\":\"";
	private Properties queries;
	private MessageDigest md;

	public SugarServiceV4Impl() throws IOException, NoSuchAlgorithmException {
		queries = new Properties();
		queries.load(getClass().getResourceAsStream("/queries.properties"));
		md = MessageDigest.getInstance("MD5");
	}

	public String login(SugarSession session) throws IOException {
		if (!session.isValid()) {
			throw new IllegalArgumentException(
					"Session is incompletely specified");
		}
		URL url = new URL(getServiceUrl(session.getSugarUrl()) + "?"
				+ getLoginPayload(session));
		return getIdFromGet(url);
	}

	protected String doGet(URL url) throws IOException {
		InputStream is = null;
		StringBuffer response = new StringBuffer();
		try {
			byte[] b = new byte[1024];
			is = (InputStream) url.getContent();
			while (is.read(b) != -1) {
				response.append(new String(b).trim());
			}
		} finally {
			try {
				is.close();
			} catch (NullPointerException e) {
				// Good chance this is a Jenkins environment calling localhost
				throw new IllegalStateException("Cannot connect to Sugar at: "
						+ url, e);
			}
		}
		return response.toString();
	}

	protected String getIdFromGet(URL url) throws IOException {
		return parseId(doGet(url));
	}

	private String parseId(String s) {
		System.out.println("response: " + s);
		if (s.indexOf("Invalid Login") != -1) {
			throw new SugarAuthenticationException();
		} else if (s.trim().equals("null")) {
			throw new SugarException("No response received.");
		}
		int start = s.indexOf(ID_MARKER) + ID_MARKER.length();
		String id = s.substring(start, s.indexOf('"', start));
		return id;
	}

	protected String doPost(URL url, String urlParameters) throws IOException {
		System.out.println("POST to " + url + "\n  with " + urlParameters);
		InputStream is = null;
		StringBuffer response = new StringBuffer();
		try {
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(urlParameters.getBytes().length));
			connection.setUseCaches(false);

			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			byte[] b = new byte[1024];
			is = (InputStream) connection.getContent();
			while (is.read(b) != -1) {
				response.append(new String(b).trim());
			}
			connection.disconnect();
		} finally {
			is.close();
		}
		return response.toString();
	}

	protected String getIdFromPost(URL url, String urlParameters)
			throws IOException {
		return parseId(doPost(url, urlParameters));
	}

	public String setEntry(CrmSession session, String module,
			String nameValueList) throws IOException {
		URL url = new URL(getServiceUrl(session.getSugarUrl()));
		return getIdFromPost(url,
				getSetEntryPayload(session, module, nameValueList));
	}

	public String setRelationship(CrmSession session, String moduleName,
			String moduleId, String linkField, String linkId)
			throws IOException {
		return doPost(
				new URL(getServiceUrl(session.getSugarUrl())),
				getSetRelationshipPayload(session, moduleName, moduleId,
						linkField, linkId));
	}

	public CrmRecord getEntry(CrmSession session, String moduleName,
			String contactId) throws IOException {
		URL url = new URL(getServiceUrl(session.getSugarUrl())
				+ getGetEntryPayload(session, moduleName, contactId, ""));
		String entry = doGet(url);
		return parseRecordFromJson(entry);
	}

	CrmRecord parseRecordFromJson(String entry) {
		CrmRecord record = new CrmRecord();
		String[] nameValues = entry.split("\\{");
		for (String nameValue : nameValues) {
			if (nameValue.length() > 0) {
				int nStart = nameValue.indexOf(NAME_MARKER)
						+ NAME_MARKER.length();
				int vStart = nameValue.indexOf(VALUE_MARKER)
						+ VALUE_MARKER.length();
				record.setCustom(
						nameValue.substring(nStart,
								nameValue.indexOf("\"", nStart)),
						nameValue.substring(vStart,
								nameValue.indexOf("\"", vStart)));
			}
		}
		return record;
	}

	protected String getLoginPayload(SugarSession session)
			throws UnsupportedEncodingException {
		String query = queries.getProperty("login");
		return String.format(query, session.getUsername(),
				session.getUsername(), hash(session.getPassword()),
				SugarService.class.getName());
	}

	public String hash(String plainPassword)
			throws UnsupportedEncodingException {
		md.reset();
		md.update(plainPassword.getBytes("UTF-8"));
		BigInteger bigInt = new BigInteger(1, md.digest());
		String hashtext = bigInt.toString(16);
		return hashtext;
	}

	public String getServiceUrl(String sugarUrl) {
		if (sugarUrl.endsWith("/")) {
			return sugarUrl + "service/v4/rest.php";
		} else {
			return sugarUrl + "/" + "service/v4/rest.php";
		}
	}

	protected String getSetEntryPayload(CrmSession session, String module,
			String nameValueList) throws UnsupportedEncodingException {
		String query = queries.getProperty("set_entry");
		return String.format(query, session.getSessionId(), module,
				nameValueList);
	}

	protected String getGetEntryPayload(CrmSession session, String module,
			String entryId, String nameValueList)
			throws UnsupportedEncodingException {
		String query = queries.getProperty("get_entry");
		return String.format(query, session.getSessionId(), module, entryId,
				nameValueList);
	}

	protected String getSetRelationshipPayload(CrmSession session,
			String moduleName, String parentId, String linkField, String linkId) {
		String query = queries.getProperty("set_relationship");
		return String.format(query, session.getSessionId(), moduleName,
				parentId, linkField, linkId);
	}

	public String getModuleFields(CrmSession session, String moduleName)
			throws IOException {
		return doPost(new URL(getServiceUrl(session.getSugarUrl())),
				getModuleFieldsPayload(session, moduleName));
	}

	protected String getModuleFieldsPayload(CrmSession session,
			String moduleName) {
		String query = queries.getProperty("get_module_fields");
		return String.format(query, session.getSessionId(), moduleName);
	}
}
