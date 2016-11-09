package br.gov.mj.sislegis.app.service.ejbs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.naming.CommunicationException;
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

import br.gov.mj.sislegis.app.model.AreaDeMeritoRevisao;
import br.gov.mj.sislegis.app.model.Comentario;
import br.gov.mj.sislegis.app.model.Documento;
import br.gov.mj.sislegis.app.model.EncaminhamentoProposicao;
import br.gov.mj.sislegis.app.model.Equipe;
import br.gov.mj.sislegis.app.model.Notificacao;
import br.gov.mj.sislegis.app.model.Papel;
import br.gov.mj.sislegis.app.model.PosicionamentoProposicao;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.Tarefa;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.model.documentos.Briefing;
import br.gov.mj.sislegis.app.model.documentos.Emenda;
import br.gov.mj.sislegis.app.model.documentos.NotaTecnica;
import br.gov.mj.sislegis.app.model.pautacomissao.AgendaComissao;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.EncaminhamentoProposicaoService;
import br.gov.mj.sislegis.app.service.ProposicaoService;
import br.gov.mj.sislegis.app.service.TarefaService;
import br.gov.mj.sislegis.app.service.UsuarioService;
import br.gov.mj.sislegis.app.util.SislegisUtil;

@Stateless
public class UsuarioServiceEjb extends AbstractPersistence<Usuario, Long> implements UsuarioService, EJBUnitTestable {

	@PersistenceContext
	private EntityManager em;
	private SearchControls controls = null;

	public UsuarioServiceEjb() {
		super(Usuario.class);
		controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		controls.setTimeLimit(1000);// maximo 1 segundo de espera
		controls.setCountLimit(20); // maximo 20 resultados
		controls.setReturningAttributes(new String[] { "cn", "userPrincipalName", "displayName", "department", "sAMAccountName" });
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Usuario findByEmail(String email) {
		TypedQuery<Usuario> findByIdQuery = em.createQuery("SELECT u FROM Usuario u WHERE upper(u.email) like upper(:email) ORDER BY u.email ASC", Usuario.class);
		findByIdQuery.setParameter("email", email);

		try {
			Usuario user = findByIdQuery.getSingleResult();
			return user;
		} catch (javax.persistence.NoResultException e) {
			// no execption just return null
			return null;
		}

	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Usuario findOrCreateByEmail(String name, String email) {
		Usuario user = findByEmail(email);
		if (user == null) {
			user = new Usuario();
			user.setEmail(email);
			user.setNome(name);
			save(user);
		}
		return user;
	}

	@Override
	public List<Usuario> listUsuariosSeguidoresDeComissao(AgendaComissao agenda) {
		TypedQuery<Usuario> findByIdQuery = em.createQuery("SELECT u FROM Usuario u join u.agendasSeguidas agendas where agendas.id=:idAgenda", Usuario.class);

		findByIdQuery.setParameter("idAgenda", agenda.getId());
		return findByIdQuery.getResultList();

	}

	@Override
	public List<Usuario> listUsuariosSeguidoresDeProposicao(Proposicao proposicao) {
		TypedQuery<Usuario> findByIdQuery = em.createQuery("SELECT u FROM Usuario u join u.proposicoesSeguidas prop where prop.id=:idProp", Usuario.class);

		findByIdQuery.setParameter("idProp", proposicao.getId());
		return findByIdQuery.getResultList();

	}

	@Override
	public List<Usuario> findByNome(String nome) {
		TypedQuery<Usuario> findByIdQuery = em.createQuery("SELECT u FROM Usuario u WHERE upper(u.nome) like upper(:nome) ORDER BY u.nome ASC", Usuario.class);
		findByIdQuery.setParameter("nome", "%" + nome + "%");
		return findByIdQuery.getResultList();
	}

	@Override
	public List<Usuario> findByIdEquipe(Long idEquipe) {

		Query query = em.createNativeQuery("SELECT u.* FROM Usuario u " + " inner join equipe_usuario eu on u.id = eu.usuario_id " + " inner join Equipe e on e.id = eu.equipe_id " + "	WHERE e.id = :idEquipe ORDER BY u.nome ASC", Usuario.class);
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
	// @Resource(lookup = "java:global/federation/ldap/mjldap")
	private javax.naming.directory.InitialDirContext ldapContext;

	@Override
	public List<Usuario> findByNomeOnLDAP(String nome) {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		try {
			ldapContext = (InitialDirContext) InitialContext.doLookup("java:global/federation/ldap/mjldap");

			NamingEnumeration<SearchResult> results = ldapContext.search("OU=SISLEGIS", "(&(objectclass=person)(cn=" + nome + "*))", controls);
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

			try {
				if (e.getRootCause().getCause().getCause().getCause() instanceof CommunicationException) {
					if (Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).isLoggable(Level.FINE)) {
						Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE, "Não foi possível carregar o recurso do LDAP. Sua rede pode acessar o LDAP do MJ?");
					} else {
						Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE, "Não foi possível carregar o recurso do LDAP. Sua rede pode acessar o LDAP do MJ?", e);
					}
				} else {
					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE, "Houve um erro consultando o LDAP", e);
				}
			} catch (Exception e1) {
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE, "Houve um erro consultando o LDAP", e);
			}

		}
		return usuarios;
	}

	@Override
	public Usuario loadComAgendasSeguidas(Long id) {
		Usuario user = findById(id);
		user.getAgendasSeguidas().size();
		return user;
	}

	@Override
	public Collection<Proposicao> proposicoesSeguidas(Long id) {
		Collection<Proposicao> props = (findById(id)).getProposicoesSeguidas();
		props.size();
		return props;
	}

	@Override
	public void setInjectedEntities(Object... injections) {
		this.em = (EntityManager) injections[0];

	}

	@Override
	public Set<Usuario> listUsuariosPorPapel(Papel secretario) {

		return new HashSet<Usuario>(getEntityManager().createNativeQuery("select u.* from usuario_papel up, usuario u where up.usuario_id=u.id and up.papel=:papel", Usuario.class).setParameter("papel", secretario.name()).getResultList());
	}

	@Override
	public Set<Usuario> listUsuariosPorPapelDeEquipe(Papel papel, Equipe equipe) {
		return new HashSet<Usuario>(getEntityManager().createNativeQuery("select u.* from usuario_papel up, usuario u where up.usuario_id=u.id and up.papel=:papel and u.idequipe=:equipe", Usuario.class).setParameter("papel", papel.name()).setParameter("equipe", equipe.getId()).getResultList());

	}

	@Inject
	EncaminhamentoProposicaoService encSvc;
	@Inject
	TarefaService tarefaSvc;
	@Inject
	ProposicaoService propSvc;

	@Override
	public void deleteByIdForce(Long id) {
		List<EncaminhamentoProposicao> listaEncaminhamentoProposicao = getEntityManager().createNamedQuery("getAllEncaminhamentoProposicao4Usuario", EncaminhamentoProposicao.class).setParameter("userId", id).getResultList();
		for (Iterator iterator = listaEncaminhamentoProposicao.iterator(); iterator.hasNext();) {
			EncaminhamentoProposicao encaminhamentoProposicao = (EncaminhamentoProposicao) iterator.next();
			encSvc.deleteById(encaminhamentoProposicao.getId());
		}
		List<Tarefa> listaTarefas = getEntityManager().createNamedQuery("getAllTarefa4Usuario", Tarefa.class).setParameter("userId", id).getResultList();
		for (Iterator iterator = listaTarefas.iterator(); iterator.hasNext();) {
			Tarefa tarefa = (Tarefa) iterator.next();
			tarefaSvc.deleteById(tarefa.getId());
		}
		List<Notificacao> listaNotificacao = getEntityManager().createNamedQuery("getAllNotificacaoByUsuario", Notificacao.class).setParameter("idUsuario", id).getResultList();
		for (Iterator iterator = listaNotificacao.iterator(); iterator.hasNext();) {
			Notificacao notificacao = (Notificacao) iterator.next();
			em.remove(notificacao);

		}
		List<Comentario> listaComentarios = getEntityManager().createNamedQuery("getAllComentarios4Usuario", Comentario.class).setParameter("userId", id).getResultList();
		for (Iterator iterator = listaComentarios.iterator(); iterator.hasNext();) {
			Comentario comentario = (Comentario) iterator.next();
			em.remove(comentario);
		}

		List<Emenda> listaEmendas = getEntityManager().createNamedQuery("listEmendasByUser", Emenda.class).setParameter("userId", id).getResultList();
		for (Iterator iterator = listaEmendas.iterator(); iterator.hasNext();) {
			Emenda emenda = (Emenda) iterator.next();
			propSvc.deleteDocRelated(emenda.getId().longValue(), Emenda.class);
		}

		List<NotaTecnica> listaNotas = getEntityManager().createNamedQuery("listNotaTecnicaByUser", NotaTecnica.class).setParameter("userId", id).getResultList();
		for (Iterator iterator = listaNotas.iterator(); iterator.hasNext();) {
			NotaTecnica emenda = (NotaTecnica) iterator.next();
			propSvc.deleteDocRelated(emenda.getId().longValue(), NotaTecnica.class);
		}

		List<Briefing> listaBriefings = getEntityManager().createNamedQuery("listBriefingByUser", Briefing.class).setParameter("userId", id).getResultList();
		for (Iterator iterator = listaBriefings.iterator(); iterator.hasNext();) {
			Briefing emenda = (Briefing) iterator.next();
			propSvc.deleteDocRelated(emenda.getId().longValue(), Briefing.class);
		}
		List<AreaDeMeritoRevisao> listaAreaDeMeritoRevisao = getEntityManager().createNamedQuery("listRevisaoByUser", AreaDeMeritoRevisao.class).setParameter("userId", id).getResultList();
		for (Iterator iterator = listaAreaDeMeritoRevisao.iterator(); iterator.hasNext();) {
			AreaDeMeritoRevisao emenda = (AreaDeMeritoRevisao) iterator.next();
			emenda.setDocumento(null);
			em.persist(emenda);
		}

		List<Documento> listaDocs = getEntityManager().createNamedQuery("getAllDocumentos4Usuario", Documento.class).setParameter("userId", id).getResultList();
		for (Iterator iterator = listaDocs.iterator(); iterator.hasNext();) {
			Documento emenda = (Documento) iterator.next();

			em.remove(emenda);
		}

		List<PosicionamentoProposicao> listaPosicionamentoProposicao = getEntityManager().createNamedQuery("getAllPosicionamentoProposicao4Usuario", PosicionamentoProposicao.class).setParameter("userId", id).getResultList();
		for (Iterator iterator = listaPosicionamentoProposicao.iterator(); iterator.hasNext();) {
			PosicionamentoProposicao posicionamentoProposicao = (PosicionamentoProposicao) iterator.next();
			em.remove(posicionamentoProposicao);
		}
		List<Proposicao> listaProposicaoPosicionada = getEntityManager().createNamedQuery("getAllProposicaoPosicionada4Usuario", Proposicao.class).setParameter("userId", id).getResultList();
		for (Iterator iterator = listaProposicaoPosicionada.iterator(); iterator.hasNext();) {
			Proposicao proposicao = (Proposicao) iterator.next();
			PosicionamentoProposicao posicionamentoProposicao = proposicao.getPosicionamentoAtual();
			em.remove(posicionamentoProposicao);
			proposicao.setPosicionamentoAtual(null);
			
			propSvc.save(proposicao, null);
		}
		
		List<Proposicao> listaProposicao = getEntityManager().createNamedQuery("getAllProposicao4Usuario", Proposicao.class).setParameter("userId", id).getResultList();
		for (Iterator iterator = listaProposicao.iterator(); iterator.hasNext();) {
			Proposicao proposicao = (Proposicao) iterator.next();
			proposicao.setResponsavel(null);
			propSvc.save(proposicao, null);
		}
		deleteById(id);

	}

}
