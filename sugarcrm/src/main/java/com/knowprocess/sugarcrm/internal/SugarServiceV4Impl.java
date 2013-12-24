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

import com.knowprocess.sugarcrm.api.SugarAuthenticationException;
import com.knowprocess.sugarcrm.api.SugarService;
import com.knowprocess.sugarcrm.api.SugarSession;

public class SugarServiceV4Impl {

	private static final String ID_MARKER = "\"id\":\"";
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
					"Session is not incompletely specified");
		}
		URL url = new URL(getServiceUrl(session.getSugarUrl()) + "?"
				+ getLoginPayload(session));
		return getIdFromGet(url);
	}

	protected String getIdFromGet(URL url) throws IOException {
		InputStream is = null;
		StringBuffer response = new StringBuffer();
		try {
			byte[] b = new byte[1024];
			is = (InputStream) url.getContent();
			while (is.read(b) != -1) {
				response.append(new String(b));
			}
		} finally {
			is.close();
		}
		return parseId(response.toString());
	}

	private String parseId(String s) {
		System.out.println("response: " + s);
		if (s.indexOf("Invalid Login") != -1) {
			throw new SugarAuthenticationException();
		}
		int start = s.indexOf(ID_MARKER) + ID_MARKER.length();
		String id = s.substring(start, s.indexOf('"', start));
		return id;
	}

	protected String doPost(URL url, String urlParameters)
			throws IOException {
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
				response.append(new String(b));
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

	public String setEntry(SugarSession session, String module,
			String nameValueList) throws IOException {
		URL url = new URL(getServiceUrl(session.getSugarUrl()));
		return getIdFromPost(url,
				getSetEntryPayload(session, module, nameValueList));
	}

	public String setRelationship(SugarSession session, String moduleName,
			String moduleId, String linkField, String linkId)
			throws IOException {
		URL url = new URL(getServiceUrl(session.getSugarUrl()));
		return doPost(
				url,
				getSetRelationshipPayload(session, moduleName, moduleId,
						linkField, linkId));
	}

	public String getEntry(SugarSession session, String entryId) {
		return "TODO";
	}

	protected String getLoginPayload(SugarSession session)
			throws UnsupportedEncodingException {
		String query = queries.getProperty("login");
		return String.format(query, session.getUsername(),
				session.getUsername(),
				hash(session.getPassword()),
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

	protected String getSetEntryPayload(SugarSession session, String module,
			String nameValueList)
			throws UnsupportedEncodingException {
		String query = queries.getProperty("set_entry");
		return String.format(query, session.getSessionId(), module,
				nameValueList);
	}

	protected String getSetRelationshipPayload(SugarSession session,
			String moduleName, String parentId, String linkField, String linkId) {
		String query = queries.getProperty("set_relationship");
		return String.format(query, session.getSessionId(), moduleName,
				parentId, linkField, linkId);
	}
}
