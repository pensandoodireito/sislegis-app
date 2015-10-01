package br.gov.mj.sislegis.app.service.ejbs;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.model.pautacomissao.AgendaComissao;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.UsuarioService;

@Stateless
public class UsuarioServiceEjb extends AbstractPersistence<Usuario, Long> implements UsuarioService {

	@PersistenceContext
	private EntityManager em;
	private SearchControls controls = null;

	public UsuarioServiceEjb() {
		super(Usuario.class);
		controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		controls.setTimeLimit(1000);// maximo 1 segundo de espera
		controls.setCountLimit(20); // maximo 20 resultados
		controls.setReturningAttributes(new String[] { "cn", "userPrincipalName", "displayName", "department",
				"sAMAccountName" });
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public Usuario findByEmail(String email) {
		TypedQuery<Usuario> findByIdQuery = em.createQuery(
				"SELECT u FROM Usuario u WHERE upper(u.email) like upper(:email) ORDER BY u.email ASC", Usuario.class);
		findByIdQuery.setParameter("email", email);

		try {
			return findByIdQuery.getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			// no execption just return null
			return null;
		}

	}

	@Override
	public List<Usuario> listUsuariosSeguidoresDeComissao(AgendaComissao agenda) {
		TypedQuery<Usuario> findByIdQuery = em.createQuery(
				"SELECT u FROM Usuario u join u.agendasSeguidas agendas where agendas.agendasseguidas_id=:idAgenda", Usuario.class);

		findByIdQuery.setParameter("idAgenda", agenda.getId());
		return findByIdQuery.getResultList();

	}

	@Override
	public List<Usuario> findByNome(String nome) {
		TypedQuery<Usuario> findByIdQuery = em.createQuery(
				"SELECT u FROM Usuario u WHERE upper(u.nome) like upper(:nome) ORDER BY u.nome ASC", Usuario.class);
		findByIdQuery.setParameter("nome", "%" + nome + "%");
		return findByIdQuery.getResultList();
	}

	@Override
	public List<Usuario> findByIdEquipe(Long idEquipe) {

		Query query = em.createNativeQuery("SELECT u.* FROM Usuario u "
				+ " inner join equipe_usuario eu on u.id = eu.usuario_id "
				+ " inner join Equipe e on e.id = eu.equipe_id " + "	WHERE e.id = :idEquipe ORDER BY u.nome ASC",
				Usuario.class);
		query.setParameter("idEquipe", idEquipe);
		List<Usuario> usuarios = query.getResultList();

		return usuarios;
	}

	/**
	 * Definido no xml de configuração do servidor. Da forma abaixo:<br>
	 * 
	 * <pre>
	 * <subsystem xmlns="urn:jboss:domain:naming:2.0">             
	 *      <bindings>
	 *          <external-context module="org.jboss.as.naming" name="java:global/federation/ldap/mjldap" class="javax.naming.directory.InitialDirC    ontext" cache="true">
	 *               <environment>
	 *                   <property name="java.naming.factory.initial" value="com.sun.jndi.ldap.LdapCtxFactory" />
	 *                   <property name="java.naming.provider.url" value="ldap://SERVIDOR:389/SUBTREE" />
	 *                   <property name="java.naming.security.authentication" value="simple" />
	 *                   <property name="java.naming.security.principal" value="PRINCIPAL" />
	 *                   <property name="java.naming.security.credentials" value="****" />
	 *               </environment>
	 *          </external-context>
	 *      </bindings>
	 * </subsystem>
	 * 
	 * </pre>
	 */
//	@Resource(lookup = "java:global/federation/ldap/mjldap")
	private javax.naming.directory.InitialDirContext ldapContext;

	@Override
	public List<Usuario> findByNomeOnLDAP(String nome) {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		try {
			ldapContext= (InitialDirContext) InitialContext.doLookup("java:global/federation/ldap/mjldap");

			NamingEnumeration<SearchResult> results = ldapContext.search("OU=SISLEGIS", "(&(objectclass=person)(cn="
					+ nome + "*))", controls);
			if (results.hasMoreElements()) {
				while (results.hasMoreElements()) {
					SearchResult searchResult = (SearchResult) results.nextElement();
					Usuario ldapUser = new Usuario();
					ldapUser.setEmail(String.valueOf(searchResult.getAttributes().get("userPrincipalName").get()));
					ldapUser.setNome(String.valueOf(searchResult.getAttributes().get("cn").get()));
					usuarios.add(ldapUser);
				}
			}

		} catch (NamingException e) {

			e.printStackTrace();
			// TODO checar quais excecoes notificamos
		}
		return usuarios;
	}

}
