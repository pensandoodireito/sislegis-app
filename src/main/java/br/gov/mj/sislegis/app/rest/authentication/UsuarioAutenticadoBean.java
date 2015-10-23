package br.gov.mj.sislegis.app.rest.authentication;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.service.UsuarioService;
import br.gov.mj.sislegis.app.util.SislegisUtil;

@Local
@Singleton
public class UsuarioAutenticadoBean {
	@Inject
	private UsuarioService usuarioService;
	String keycloakURL = "";

	@PostConstruct
	void init() {
		JSONObject keycloakConfig = new JSONObject(KeyCloakGenerated.conf);
		StringBuffer url = new StringBuffer();
		url.append(keycloakConfig.getString("auth-server-url-for-backend-requests"));
		url.append("/realms/").append(keycloakConfig.getString("realm")).append("/protocol/openid-connect/userinfo");
		keycloakURL = url.toString();

	}

	public synchronized Usuario carregaUsuarioAutenticado(String authorization) throws IOException {
		JSONObject jsonUser = null;

		jsonUser = buscaDadosUsuarioDoKeycloak(authorization);
		String email = jsonUser.getString("email");
		Usuario authenticatedUser = usuarioService.findOrCreateByEmail(jsonUser.getString("name"), email);

		return authenticatedUser;
	}

	private String getContent(HttpResponse response) throws IOException {
		InputStream is = null;

		try {
			HttpEntity httpEntity = response.getEntity();

			is = httpEntity.getContent();
			return IOUtils.toString(is, "UTF-8");
		} finally {
			if (is != null) {
				is.close();
			}
		}

	}

	/**
	 * Busca dados do usuário pelo rest service do keycloak baseado no parametro
	 * 'authorization'
	 * 
	 * @param authorization
	 * @return
	 * @throws IOException
	 */
	private JSONObject buscaDadosUsuarioDoKeycloak(String authorization) throws IOException {
		JSONObject jsonUser = null;
		HttpClient client = new DefaultHttpClient();
		try {
			HttpGet get = new HttpGet(keycloakURL);
			get.addHeader("Authorization", authorization);
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() != 200) {
				if (Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).isLoggable(Level.FINE)) {

					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine(
							"Falhou ao obter dados do usuario logado: '" + getContent(response));
				}

				throw new IOException("Não foi possível obter dados do usuáro logado. Http Status: "
						+ response.getStatusLine().getStatusCode());
			}

			jsonUser = new JSONObject(getContent(response));

		} finally {
			client.getConnectionManager().shutdown();
		}
		return jsonUser;
	}
}
