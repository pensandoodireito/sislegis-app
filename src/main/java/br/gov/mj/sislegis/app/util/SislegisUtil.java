package br.gov.mj.sislegis.app.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.json.JSONArray;
import org.json.JSONObject;

public class SislegisUtil {

	public static void sendEmail(String toMail, String toName, String subject, String body) throws EmailException {
		final HtmlEmail htmlEmail = new HtmlEmail();

		try {
			String emailFrom = PropertiesUtil.getProperties().getProperty("email");

			htmlEmail.setHostName(PropertiesUtil.getProperties().getProperty("host"));
			htmlEmail.setSmtpPort(Integer.parseInt(PropertiesUtil.getProperties().getProperty("port")));
			htmlEmail.setTLS(true);

			htmlEmail.setAuthenticator(new DefaultAuthenticator(emailFrom, PropertiesUtil.getProperties().getProperty(
					"password")));

			htmlEmail.setFrom(emailFrom, PropertiesUtil.getProperties().getProperty("username"));

			htmlEmail.setSubject(subject);

			htmlEmail.addTo(toMail, toName);

			htmlEmail.setHtmlMsg(body);

			htmlEmail.setCharset("UTF-8");
			htmlEmail.setSocketTimeout(Integer.parseInt(PropertiesUtil.getProperties().getProperty("emailTimeout")));
			htmlEmail.send();

		} catch (EmailException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public static String getDataAtual() {
		Calendar calendar = Calendar.getInstance();
		return Conversores.dateToString(calendar.getTime());
	}

	public static Date getDate() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();
	}

	public static Date getFutureDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 1);
		return calendar.getTime();
	}

	public static List<String> jsonArrayToList(String jsonArray) {
		JSONArray array = new JSONArray("[" + jsonArray + "]");
		List<String> lista = new ArrayList<String>();
		for (int i = 0; i < array.length(); i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			if (jsonObject.length() > 0) {
				lista.add(jsonObject.get(String.valueOf(i)).toString());
			}
		}
		return lista;
	}

	public static final String SISLEGIS_LOGGER = "sislegis";
}
