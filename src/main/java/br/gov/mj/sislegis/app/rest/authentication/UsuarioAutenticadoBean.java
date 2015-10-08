package br.gov.mj.sislegis.app.rest.authentication;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.service.UsuarioService;

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

	private Usuario criaUsuario(JSONObject jsonUser) {
		// Criá-lo
		Usuario novoUsuario = new Usuario();
		novoUsuario.setEmail(jsonUser.getString("email"));
		novoUsuario.setNome(jsonUser.getString("name"));

		usuarioService.save(novoUsuario);
		return novoUsuario;
	}

	public Usuario carregaUsuarioAutenticado(String authorization) throws IOException {
		JSONObject jsonUser = null;

		jsonUser = buscaDadosUsuarioDoKeycloak(authorization);
		Usuario authenticatedUser = usuarioService.findByEmail(jsonUser.getString("email"));
		if (authenticatedUser == null) {
			authenticatedUser = criaUsuario(jsonUser);
		}

		return authenticatedUser;
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
				throw new IOException("Não foi possível obter dados do usuáro logado. Http Status: "
						+ response.getStatusLine().getStatusCode());
			}
			HttpEntity httpEntity = response.getEntity();

			InputStream is = httpEntity.getContent();
			try {
				jsonUser = new JSONObject(IOUtils.toString(is, "UTF-8"));

			} finally {
				is.close();
			}

		} finally {
			client.getConnectionManager().shutdown();
		}
		return jsonUser;
	}
}
