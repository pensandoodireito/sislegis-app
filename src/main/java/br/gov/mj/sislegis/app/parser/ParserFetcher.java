package br.gov.mj.sislegis.app.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.gov.mj.sislegis.app.util.SislegisUtil;

import com.thoughtworks.xstream.XStream;

/**
 * Ferramentas comuns aos fetchers de URL externos.
 * 
 * @author coutinho
 *
 */
public class ParserFetcher {
	private static final int WS_TIMEOUT = 15 * 1000;

	/**
	 * Retorna um inputstream para um webservice URL.
	 * 
	 * @param wsURLStr
	 * @return
	 * @throws IOException
	 */
	public static InputStream getWebServiceInputStream(String wsURLStr) throws IOException {
		URL url = new URL(wsURLStr);
		HttpURLConnection huc = (HttpURLConnection) url.openConnection();
		huc.setConnectTimeout(WS_TIMEOUT);
		huc.setRequestMethod("GET");
		huc.connect();
		InputStream input = huc.getInputStream();
		return input;
	}

	public static void fetchXStream(String wsURL, XStream xstream, Object object) throws IOException {
		Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.ALL, wsURL);
		InputStream in = null;

		try {
			in = ParserFetcher.getWebServiceInputStream(wsURL);
			xstream.fromXML(in, object);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					// pode ignorar
				}
			}
		}

	}
}
