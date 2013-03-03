package cz.emo4d.zen;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.AuthenticationException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

import cz.emo4d.zen.Zen.BossPerson;

/**
 * Parsovani predmetu z WISu
 * @author Vojta
 *
 */
public class WisImport {
	private String loginName;
	private String loginPass;

	private final String ptrSubjects1 = "(IJC|IMS|ICP|SNT|IPP|FLP|PDB|ITY|IUS|IIS|PIS|WAP)";
	private final String ptrSubjects2 = "[^0-9]+id=\\d+[^0-9]+(\\d+)";

	/**
	 * Prihlasi se pomoci jmena a hesla
	 * @param name
	 * @param pass
	 * @throws AuthenticationException
	 */
	public void login(String name, String pass) throws AuthenticationException {
		loginName = name;
		loginPass = pass;

		bypassInvalidCertificate();

		try {
			HttpsURLConnection conn = createConnection("https://wis.fit.vutbr.cz/FIT/st/");
			Gdx.app.log("WIS", "Conn created");

			if (conn.getResponseCode() == 401)
				throw new AuthenticationException("Spatne heslo");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Vrati seznam vybranych predmetu z WISu serazeny vzestupne podle bodu.
	 *
	 * @return String, kde body = [0:2], zkratka predmetu = [3:5]
	 */
	public Array<String> getHatedSubjects() {
		String data = null;
		Array<String> subjects = new Array<String>();

		getPageContent("https://wis.fit.vutbr.cz/FIT/st/index.php?cist=1");
		data = getPageContent("https://wis.fit.vutbr.cz/FIT/st/study-a.php");
		parseSubjects(data, subjects);

		getPageContent("https://wis.fit.vutbr.cz/FIT/st/index.php?cist=2");
		data = getPageContent("https://wis.fit.vutbr.cz/FIT/st/study-a.php");
		parseSubjects(data, subjects);

		subjects.sort();

		return subjects;
	}

	public BossPerson getMostHatedBoss() {
		Array<String> subjects = getHatedSubjects();
		String subj = subjects.first();
		String abbr = subj.substring(3);

		return Zen.bossTypes.get(abbr);
	}

	private void parseSubjects(String pageData, Array<String> subjects) {
		Pattern pattern = Pattern.compile(ptrSubjects1 + ptrSubjects2);
		Matcher matcher = pattern.matcher(pageData);

		while (matcher.find()) {
			String abbr = matcher.group(1);
			String points = matcher.group(2);

			if (points.length() == 1)
				points = "00" + points;
			else if (points.length() == 2)
				points = "0" + points;

			String item = points + abbr;

			if (!subjects.contains(item, false)) {
				subjects.add(item);
			}
		}
	}

	private String getPageContent(String pageUrl) {
		try {
			HttpsURLConnection conn = createConnection(pageUrl);
			InputStream istream = conn.getInputStream();

			return new Scanner(istream, "iso-8859-2").useDelimiter("\\A").next();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}

	private HttpsURLConnection createConnection(String pageUrl) {
		HttpsURLConnection conn = null;

		try {
			URL url = new URL(pageUrl);
			conn = (HttpsURLConnection) url.openConnection();

			String userpass = loginName + ":" + loginPass;
			String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
			conn.setRequestProperty("Authorization", basicAuth);
		} catch (MalformedURLException e) {
			// URL jsou pevne dana
		} catch (SSLHandshakeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return conn;
	}


	/**
	 * Obejde neplatny certifikat FITu.
	 */
	private void bypassInvalidCertificate() {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}