package br.gov.mj.sislegis.app.parser;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Comentario;
import br.gov.mj.sislegis.app.model.EncaminhamentoProposicao;
import br.gov.mj.sislegis.app.model.Equipe;
import br.gov.mj.sislegis.app.model.EstadoProposicao;
import br.gov.mj.sislegis.app.model.Papel;
import br.gov.mj.sislegis.app.model.Posicionamento;
import br.gov.mj.sislegis.app.model.PosicionamentoProposicao;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.Tag;
import br.gov.mj.sislegis.app.model.TipoEncaminhamento;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.parser.camara.ParserPautaCamara;
import br.gov.mj.sislegis.app.parser.camara.ParserProposicaoCamara;
import br.gov.mj.sislegis.app.parser.senado.ParserPautaSenado;
import br.gov.mj.sislegis.app.parser.senado.ParserProposicaoSenado;
import br.gov.mj.sislegis.app.service.ComentarioService;
import br.gov.mj.sislegis.app.service.ComissaoService;
import br.gov.mj.sislegis.app.service.EncaminhamentoProposicaoService;
import br.gov.mj.sislegis.app.service.EquipeService;
import br.gov.mj.sislegis.app.service.NotificacaoService;
import br.gov.mj.sislegis.app.service.PosicionamentoService;
import br.gov.mj.sislegis.app.service.ProposicaoService;
import br.gov.mj.sislegis.app.service.TagService;
import br.gov.mj.sislegis.app.service.TarefaService;
import br.gov.mj.sislegis.app.service.TipoEncaminhamentoService;
import br.gov.mj.sislegis.app.service.UsuarioService;
import br.gov.mj.sislegis.app.service.ejbs.ComentarioServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.ComissaoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.EJBDataCacherImpl;
import br.gov.mj.sislegis.app.service.ejbs.EJBUnitTestable;
import br.gov.mj.sislegis.app.service.ejbs.EncaminhamentoProposicaoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.EquipeServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.NotificacaoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.PosicionamentoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.ProposicaoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.ReuniaoProposicaoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.ReuniaoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.TagServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.TarefaServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.TipoEncaminhamentoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.UsuarioServiceEjb;
import br.gov.mj.sislegis.app.util.SislegisUtil;

public class ImporterTests {
	private static final String EMAIL_USUARIO_PADRAO = "ana.couto@mj.gov.br";
	PosicionamentoService posicionamentoSvc;
	ProposicaoService proposicaoService;
	ComissaoService comissaoService;
	TipoEncaminhamentoService tipoEncaminhamentoService;
	EncaminhamentoProposicaoService encaminhamentoService;
	TarefaService tarefaService;
	EquipeService equipeService;
	NotificacaoService notiService;
	ComentarioService comentarioService;
	private UsuarioService userSvc;
	EntityManager entityManager;
	TagService tagService;
	private static EntityManagerFactory emf = null;
	EntityManager em;
	private ReuniaoServiceEjb reuniaoEJB;
	private ReuniaoProposicaoServiceEjb reuniaoProposicaoEJB;

	static Map<String, Usuario> atribuidoToResponsavel = new HashMap<String, Usuario>();
	static {

	}
	Map<String, Posicionamento> posicionamentoCache = new HashMap<String, Posicionamento>();
	private TipoEncaminhamento tipoEnc;

	public static void closeEntityManager() {
		emf.close();
	}

	@After
	public void tearDown() {
		deInitEJBS();
		closeEntityManager();
	}

	@Before
	public void setUp() {
		try {
			emf = Persistence.createEntityManagerFactory("sislegis-persistence-unit-test");
			initEJBS();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deInitEJBS() {
		if (em != null && em.isOpen()) {
			em.close();
		}
	}

	private void initEJBS() {
		em = emf.createEntityManager();
		proposicaoService = new ProposicaoServiceEjb();
		userSvc = new UsuarioServiceEjb();
		posicionamentoSvc = new PosicionamentoServiceEjb();
		reuniaoEJB = new ReuniaoServiceEjb();
		reuniaoEJB.setInjectedEntities(em);
		tagService = new TagServiceEjb();
		encaminhamentoService = new EncaminhamentoProposicaoServiceEjb();
		tarefaService = new TarefaServiceEjb();
		notiService = new NotificacaoServiceEjb();
		equipeService = new EquipeServiceEjb();

		reuniaoProposicaoEJB = new ReuniaoProposicaoServiceEjb();
		reuniaoProposicaoEJB.setInjectedEntities(em);
		comissaoService = new ComissaoServiceEjb();
		EJBDataCacherImpl ejbCacher = new EJBDataCacherImpl();
		ejbCacher.initialize();
		((EJBUnitTestable) comissaoService).setInjectedEntities(em, ejbCacher);
		((EJBUnitTestable) notiService).setInjectedEntities(em);
		comentarioService = new ComentarioServiceEjb();
		((EJBUnitTestable) comentarioService).setInjectedEntities(em);
		((EJBUnitTestable) tarefaService).setInjectedEntities(em, notiService);
		((EJBUnitTestable) encaminhamentoService).setInjectedEntities(em, tarefaService);
		((EJBUnitTestable) proposicaoService).setInjectedEntities(em, new ParserProposicaoCamara(), reuniaoEJB,
				reuniaoProposicaoEJB, comissaoService, comentarioService);
		((EJBUnitTestable) userSvc).setInjectedEntities(em);
		((EJBUnitTestable) posicionamentoSvc).setInjectedEntities(em);
		((EJBUnitTestable) tagService).setInjectedEntities(em);
		tipoEncaminhamentoService = new TipoEncaminhamentoServiceEjb();
		((EJBUnitTestable) tipoEncaminhamentoService).setInjectedEntities(em);

		((EJBUnitTestable) equipeService).setInjectedEntities(em, userSvc);

		initPosicionamentos();
		List<Posicionamento> posicoes = posicionamentoSvc.listAll();
		for (Iterator iterator = posicoes.iterator(); iterator.hasNext();) {
			Posicionamento posicionamento = (Posicionamento) iterator.next();
			System.out.println("Posicionamentos: '" + posicionamento.getNome() + "'");
			posicionamentoCache.put(posicionamento.getNome().trim().toLowerCase(), posicionamento);
		}
		posicionamentoCache.put("Pela rejeição".toLowerCase(), posicionamentoSvc.getByName("Contrário"));
		posicionamentoCache.put("Pela rejeição.".toLowerCase(), posicionamentoSvc.getByName("Contrário"));
		posicionamentoCache.put("Pela rejeição (16/09/16)".toLowerCase(), posicionamentoSvc.getByName("Contrário"));
		posicionamentoCache.put("Previamente contrário".toLowerCase(), posicionamentoSvc.getByName("Contrário"));

		posicionamentoCache.put("SUPAR: Liberar (19/09/16)".toLowerCase(), posicionamentoSvc.getByName("Favorável"));
		posicionamentoCache.put("Liberar".toLowerCase(), posicionamentoSvc.getByName("Favorável"));
		posicionamentoCache.put("Supar orientando por liberar".toLowerCase(), posicionamentoSvc.getByName("Favorável"));
		posicionamentoCache.put("SUPAR: Liberar".toLowerCase(), posicionamentoSvc.getByName("Favorável"));

		posicionamentoCache.put("Favorável com emendas".toLowerCase(),
				posicionamentoSvc.getByName("Favorável com emendas"));
		posicionamentoCache.put("Favorável com emendas".toLowerCase(),
				posicionamentoSvc.getByName("Favorável com emendas"));
		posicionamentoCache.put("Favorável com emenda".toLowerCase(),
				posicionamentoSvc.getByName("Favorável com emendas"));

		for (Iterator iterator = tipoEncaminhamentoService.listAll().iterator(); iterator.hasNext();) {
			TipoEncaminhamento enc = (TipoEncaminhamento) iterator.next();
			if (enc.getNome().equals("Elaborar Nota Técnica")) {
				this.tipoEnc = enc;
			}
		}
		if (tipoEnc == null) {
			tipoEnc = new TipoEncaminhamento();
			tipoEnc.setNome("Elaborar Nota Técnica");
			EntityTransaction trans = em.getTransaction();
			trans.begin();
			tipoEncaminhamentoService.save(tipoEnc);
			trans.commit();
		}

		initUsers();

	}

	private void initPosicionamentos() {
		// favoravel
		// favoravel com emendas
		// contrario
		// monitorar
		// nada a opor
		String[] existentes = { "Favorável", "Favorável com emendas", "Contrário", "Monitorar", "Nada a opor" };
		for (int i = 0; i < existentes.length; i++) {
			Posicionamento p = posicionamentoSvc.getByName(existentes[i]);
			if (p == null) {
				p = new Posicionamento();
				p.setNome(existentes[i]);
				EntityTransaction t = em.getTransaction();
				t.begin();
				posicionamentoSvc.save(p);
				t.commit();
			}
		}

	}

	boolean debug = false;

	private void checkCreate(String[] useralias, String[] usernames, String[] userEmails, Equipe equipe, Papel p) {

		for (int i = 0; i < useralias.length; i++) {

			String nome = useralias[i];
			String email = userEmails[i];
			String nomeCompletoStr = usernames[i];

			if (email != null) {
				Usuario u = userSvc.findByEmail(email);
				if (u == null) {
					EntityTransaction trans = em.getTransaction();
					trans.begin();
					u = new Usuario();
					u.setEmail(email);
					u.setNome(nomeCompletoStr);
					u.setEquipe(equipe);
					if (email.equals(EMAIL_USUARIO_PADRAO)) {
						u.addPapel(Papel.ADMIN);
					} else {
						u.addPapel(p);
					}

					userSvc.save(u);
					trans.commit();
				}

				atribuidoToResponsavel.put(nome, u);
			}

		}
	}

	Equipe atual;
	Equipe pessoa;
	Equipe penal;
	Equipe estado;

	private void initUsers() {
		String[] userUp = { "Ana Carla Couto", "Afonso", "Marcelo D. Varella" };
		String[] userCompletoUp = { "Ana Carla Couto de Miranda Castro", "Afonso Almeida", "Marcelo D. Varella" };
		String[] userEmailUp = { "ana.couto@mj.gov.br", "afonso.almeida@mj.gov.br", "marcelo.varella@mj.gov.br" };

		checkCreate(userUp, userCompletoUp, userEmailUp, null, Papel.SECRETARIO);

		String[] userASPAR = { "leandrog", "pl", "gb", "nr", "nn", "fs" };
		String[] userComAspar = { "Leandro Guedes", "Paula Lacerda", "Gabriel Borges", "Natália Reis", "Nayara Nunes",
				"Fernanda Soares" };
		String[] userEmailAspar = { "leandro.guedes@mj.gov.br", "paula.lacerda@mj.gov.br", "gabriel.borges@mj.gov.br",
				"natalia.reis.estagio@mj.gov.br", "nayara.nunes@mj.gov.br", "fernanda.msoares.terceirizado@mj.gov.br" };

		checkCreate(userASPAR, userComAspar, userEmailAspar, getEquipe("ASPAR"), Papel.ASPAR);

		String[] userEstadp = { "Eduarda", "Guilherme", "Leonardo", "Marcelo", "Natalia", "Paula", "Sem atribuição" };
		String[] userCompletoEstado = { "Eduarda Cintra", "Guilherme Moraes Rego", "Leonardo Povoa", "Marcelo Bastos",
				"Natalia Langenegger", "Paula Leal", "Sem atribuição" };
		String[] userEmailEstado = { "eduarda.cintra@mj.gov.br", "guilherme.moraesrego@mj.gov.br",
				"leonardo.povoa@mj.gov.br", "marcelo.bastos@mj.gov.br", "natalia.langenegger@mj.gov.br",
				"paula.leal@mj.gov.br", null };
		estado = getEquipe("Politica Legislativa e Organização do Estado");

		checkCreate(userEstadp, userCompletoEstado, userEmailEstado, estado, Papel.EQUIPE);
		setPapel("guilherme.moraesrego@mj.gov.br", Papel.DIRETOR);

		String[] userPPessoa = { "Adriana", "Mariana", "Rodrigo", "Clarice", "Bernardo", "Fernando", "Frederico",
				"Sem atribuição" };
		String[] userCompPPessoa = { "Adriana Ligeiro", "Mariana Carvalho", "Rodrigo Mercante", "Clarice Oliveira",
				"Bernardo Andrade", "Fernando Couto", "Frederico Moesch", "Sem atribuição" };
		String[] userEmailPPessoa = { "adriana.ligeiro@mj.gov.br", "mariana.carvalho@mj.gov.br",
				"rodrigo.mercante@mj.gov.br", "clarice.oliveira@mj.gov.br", "bernardo.andrade.estagio@mj.gov.br",
				"fernando.couto@mj.gov.br", "frederico.moesch@mj.gov.br", null };
		pessoa = getEquipe("Equipe Politica Legislativa e Proteção da Pessoa");
		checkCreate(userPPessoa, userCompPPessoa, userEmailPPessoa, pessoa, Papel.EQUIPE);
		setPapel("clarice.oliveira@mj.gov.br", Papel.DIRETOR);

		String[] userPenal = { "Karise", "Cláudio", "Melina", "Laura", "Silvana" };
		String[] userCompletoPenal = { "Jocyane Figueroa", "Cláudio Teixeira", "Melina Siqueira", "Laura Souza",
				"Silvana Nunes" };
		String[] userEmailPenal = { "jocyane.figueroa@mj.gov.br", "claudio.teixeira@mj.gov.br",
				"melina.siqueira@mj.gov.br", "laura.souza@mj.gov.br", "silvana.nunes@mj.gov.br" };

		penal = getEquipe("Politica Processo e Controle Penal");

		checkCreate(userPenal, userCompletoPenal, userEmailPenal, penal, Papel.EQUIPE);
		setPapel("claudio.teixeira@mj.gov.br", Papel.DIRETOR);

	}

	private void setPapel(String s, Papel p) {
		EntityTransaction trans = em.getTransaction();
		trans.begin();

		Usuario diretorestado = userSvc.findByEmail(s);
		diretorestado.addPapel(p);
		userSvc.save(diretorestado);
		trans.commit();
	}

	private Equipe getEquipe(String nomeEquipe) {
		Equipe estado = equipeService.getByName(nomeEquipe);
		if (estado == null) {
			EntityTransaction trans = em.getTransaction();
			trans.begin();
			estado = new Equipe();
			estado.setNome(nomeEquipe);
			em.persist(estado);
			trans.commit();

		}
		return estado;
	}

	static int PENAL = 2;
	static int PESSOA = 1;
	static int ESTADO = 3;
	static int CodigoEquipe = ESTADO;

	private void processaExcel() throws IOException {
		String file = null;
		if (CodigoEquipe == PENAL) {
			file = "/home/sislegis/workspace/b/src/main/resources/Acompanhamento PLs Penal.xlsx";
			atual = penal;
		} else if (CodigoEquipe == PESSOA) {
			file = "/home/sislegis/workspace/b/src/main/resources/Acompanhamento PLs Pessoa.xlsx";
			atual = pessoa;
		} else if (CodigoEquipe == ESTADO) {
			file = "/home/sislegis/workspace/b/src/main/resources/Acompanhamento PLs Estado.xlsx";
			atual = estado;
		}

		System.out.println("** PROCESSANDO PLANILHA DA EQUIPE " + atual.getNome() + " *************");
		XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(file)));

		List<ProposicalXLS> list = new ArrayList<ProposicalXLS>();
		// for (int i = 0; i < wb.getNumberOfSheets(); i++) {
		// Sheet sheet = wb.getSheetAt(i);
		{
			Sheet sheet = wb.getSheetAt(0);// heet("Projetos");
			int k = 0;
			for (Row row : sheet) {
				ParserPautaCamara pautaCamara = new ParserPautaCamara();
				ParserPautaSenado pautaSenado = new ParserPautaSenado();
				if (row.getCell(0) != null) {

					String origem = "";
					try {
						origem = row.getCell(0).getStringCellValue();
					} catch (Exception e) {

					}

					if ("Câmara".equals(origem) || "Senado".equals(origem)) {
						ProposicalXLS p = new ProposicalXLS(row, CodigoEquipe);

						// if (!p.numero.equals("30") || !p.ano.equals("2015"))
						// {
						// continue;
						// }
						ProposicaoSearcher parserPropCamara = new ParserProposicaoCamara();

						if ("Senado".equals(origem)) {
							parserPropCamara = new ParserProposicaoSenado();
						}
						EntityTransaction trans = em.getTransaction();
						Proposicao prop = null;
						try {
							if (p.tipo == null || p.numero == null || p.ano == null) {
								System.err.println("Entrada invalida:");
								System.err.println(p);
								continue;
							}
							// if(!p.numero.equals("5202")){
							// continue;
							// }
							Collection<Proposicao> proposicoesWS = parserPropCamara.searchProposicao(p.tipo, p.numero,
									Integer.parseInt(p.ano));
							if (proposicoesWS.isEmpty()) {
								if (p.hasMore()) {
									proposicoesWS = parserPropCamara.searchProposicao(p.tipo, p.numero,
											Integer.parseInt(p.ano));
								} else {
									throw new Exception("Nao conseguiu encontrar " + p);
								}

							}
							prop = proposicoesWS.iterator().next();
							Proposicao propdb = proposicaoService.buscarPorIdProposicao(prop.getIdProposicao());
							boolean existente = false;
							if (propdb != null) {
								existente = true;
								prop = propdb;
							}
							if (debug) {
								trans = em.getTransaction();
								trans.begin();
								proposicaoService.save(prop);
								trans.commit();
							}
							Usuario responsavel = atribuidoToResponsavel.get(p.responsavel);

							if (p.tema.length() > 5000) {
								p.tema = p.tema.substring(0, 5000);
							}
							if (debug) {
								trans = em.getTransaction();
								trans.begin();
							}
							prop.setExplicacao(p.tema);
							prop.setEquipe(atual);
							prop.setResponsavel(responsavel);
							prop.setParecerSAL(p.providencias);
							if (debug) {
								proposicaoService.save(prop);
								trans.commit();
								trans = em.getTransaction();
								trans.begin();
							}
							prop.setResultadoASPAR(p.asparTxt);
							prop.setFavorita(p.prioritario);
							if (debug) {
								proposicaoService.save(prop);
								trans.commit();
								trans = em.getTransaction();
							}

							if (p.despachado) {
								prop.setEstado(EstadoProposicao.DESPACHADA);
							} else {

								if (p.situacao != null && p.situacao.toLowerCase().contains("feita")) {
									prop.setEstado(EstadoProposicao.ADESPACHAR);
								} else if (p.drive != null && !p.drive.isEmpty()) {
									prop.setEstado(EstadoProposicao.ADESPACHAR);
								} else {
									prop.setEstado(EstadoProposicao.EMANALISE);
								}
							}
							trans.begin();// verdadeiro
							if (p.macrotema != null && p.macrotema.trim().length() > 0) {
								List<Tag> tags = tagService.buscaPorSufixo(p.macrotema.trim());
								Tag t = new Tag();
								if (tags.isEmpty()) {
									if (p.macrotema != null && p.macrotema.trim().length() > 0) {
										if (p.macrotema.length() > 255) {
											System.err.println("macrotema muito longo " + p.macrotema + " " + p);
											p.macrotema = p.macrotema.substring(0, 254);
										}
										t.setTag(p.macrotema.trim());

										t = tagService.save(t);

										tags.add(t);
									}
								} else {

									t = tags.iterator().next();
								}
								prop.setTags(tags);
							}
							if (debug) {
								proposicaoService.save(prop);
								trans.commit();
								trans = em.getTransaction();
								trans.begin();
							}
							if (p.comissao != null && p.comissao.length() > 0
									&& p.comissao.length() < "COMISSÃO ESPECIAL".length()) {
								prop.setComissao(p.comissao);
							}

							proposicaoService.save(prop);
							if (debug) {
								trans.commit();
								trans = em.getTransaction();
								trans.begin();
							}
							if (existente == false && p.drive != null && p.drive.length() > 0) {
								EncaminhamentoProposicao ep = new EncaminhamentoProposicao();
								ep.setDetalhes("Buscar do Drive: " + p.drive);
								if (responsavel != null) {
									ep.setResponsavel(responsavel);
								}
								ep.setProposicao(prop);
								ep.setTipoEncaminhamento(this.tipoEnc);
								encaminhamentoService.salvarEncaminhamentoProposicao(ep);

							}
							if (debug) {
								trans.commit();
								trans = em.getTransaction();
								trans.begin();
							}
							if (p.areaDeMerito != null && p.areaDeMerito.length() > 0
									&& !"Não há".equals(p.areaDeMerito)) {
								Comentario c = new Comentario();
								if (responsavel != null) {
									c.setAutor(responsavel);
								} else {
									c.setAutor(userSvc.findByEmail(EMAIL_USUARIO_PADRAO));
								}
								if (p.areaDeMerito.length() > 255) {
									p.areaDeMerito = p.areaDeMerito.substring(0, 252) + "..";
								}
								c.setDescricao(p.areaDeMerito);

								c.setProposicao(prop);
								c.setDataCriacao(new Date());
								em.persist(c);
							}
							trans.commit();

							if (p.posicaoSAL != null && p.posicaoSAL.length() > 0) {
								p.posicaoSAL = p.posicaoSAL.trim();

								Posicionamento posicionamento = posicionamentoCache.get(p.posicaoSAL.toLowerCase());
								if (posicionamento != null) {

									trans = em.getTransaction();
									trans.begin();
									PosicionamentoProposicao pp = new PosicionamentoProposicao();
									pp.setDataCriacao(new Date());
									pp.setProposicao(prop);
									if (responsavel == null) {
										pp.setUsuario(userSvc.findByEmail(EMAIL_USUARIO_PADRAO));
									} else {
										pp.setUsuario(responsavel);
									}
									pp.setPosicionamento(posicionamento);
									em.persist(pp);
									prop.setPosicionamentoAtual(pp);
									trans.commit();

								} else {
									if ("".equals(p.posicaoSAL)) {

									} else {
										String msg = "Posicionamento SAL da planilha não reconhecido: " + p.posicaoSAL;
										criaComentario(prop, responsavel, msg);
										System.err.println("Posicionamento inválido '" + p.posicaoSAL + "' para " + p);
										// System.err.println("Posicionametno novo "
										// + p.posicaoSAL);
									}
								}

							}

							if (p.supar != null && p.supar.trim().length() > 0) {
								p.supar = p.supar.trim();
								Posicionamento posicionamento = posicionamentoCache.get(p.supar.toLowerCase());
								if (posicionamento != null) {
									trans = em.getTransaction();
									trans.begin();
									prop.setPosicionamentoSupar(posicionamento);
									proposicaoService.save(prop);
									trans.commit();
								} else {
									if ("".equals(p.supar)) {

									} else {
										String msg = "Posicionamento SUPAR da planilha não reconhecido: " + p.supar;
										criaComentario(prop, responsavel, msg);
										System.err.println("Posicionamento inválido supar '" + p.supar + "' para " + p);
									}
								}

							}

							if (p.pauta.length() > 0) {
								trans = em.getTransaction();
								trans.begin();
								prop = proposicaoService.buscarPorIdProposicao(prop.getIdProposicao());
								proposicaoService.syncDadosPautaProposicao(prop.getId());
								trans.commit();
							}

						} catch (Exception e) {
							e.printStackTrace();
							System.err.println("Falhou ao processar " + p + " " + p.comissao.length() + " "
									+ p.situacao.length());
//							System.out.println(prop.getComissao().length() + " n:" + prop.getNumero().length() + " l:"
//									+ prop.getLinkProposicao().length() + " sit:" + prop.getSituacao().length() + " a:"
//									+ prop.getAutor().length());

							p.printRow();

							Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE,
									"Falhou ao processar " + p, e);
							if (trans.isActive()) {
								trans.rollback();
							}
						} finally {

						}
						list.add(p);
					}
				}
			}
		}

	}

	private void criaComentario(Proposicao prop, Usuario responsavel, String msg) {
		EntityTransaction trans = em.getTransaction();
		trans.begin();
		// posicionamento = new
		// Posicionamento();
		// posicionamento.setNome(p.posicaoSAL);
		// em.persist(posicionamento);
		// em.flush();
		// posicionamentoCache.put(posicionamento.getNome().trim().toLowerCase(),
		// posicionamento);
		Comentario c = new Comentario();
		if (responsavel != null) {
			c.setAutor(responsavel);
		} else {
			c.setAutor(userSvc.findByEmail(EMAIL_USUARIO_PADRAO));
		}

		c.setDescricao(msg);

		c.setProposicao(prop);
		c.setDataCriacao(new Date());
		em.persist(c);
		trans.commit();
	}

	// // @Test
	// public void equipe() {
	//
	// List<Usuario> all = userSvc.listAll();
	// for (Iterator iterator = all.iterator(); iterator.hasNext();) {
	// Usuario usuario = (Usuario) iterator.next();
	// System.out.println(usuario);
	// Set<EquipeUsuario> equipes = usuario.getEquipes();
	// for (Iterator iterator2 = equipes.iterator(); iterator2.hasNext();) {
	// EquipeUsuario equipeUsuario = (EquipeUsuario) iterator2.next();
	// System.out.println(equipeUsuario.getEquipe());
	// Map<String, String> filtros = new HashMap<String, String>();
	// filtros.put("idEquipe", equipeUsuario.getEquipe().getId().toString());
	// List l = proposicaoService.consultar(filtros, 0, 10);
	// System.out.println(l.size());
	// }
	// }
	// }

	// @Test
	public void propCheck() {
		try {
			ProposicaoSearcher parserPropCamara = new ParserProposicaoCamara();
			Collection<Proposicao> proposicoesWS = parserPropCamara.searchProposicao("PL", "4941", 2016);
			Proposicao prop = proposicoesWS.iterator().next();
			Proposicao propdb = proposicaoService.buscarPorIdProposicao(prop.getIdProposicao());
			boolean existente = false;
			if (propdb != null) {
				existente = true;
				prop = propdb;
				System.err.println(existente);
			}
			EntityTransaction trans = em.getTransaction();
			trans = em.getTransaction();
			trans.begin();
			proposicaoService.save(prop);
			trans.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testA() {

		try {
			List l = em.createQuery("SELECT p FROM Proposicao p WHERE :tag in elements(p.tags)  ", Proposicao.class)
					.setParameter("tag", "DH - mulheres").getResultList();
			System.out.println(l.size());

		} catch (Exception e) {

			e.printStackTrace();

			fail();
		}
	}

	@Test
	public void testImportPenal() {

		try {
			CodigoEquipe = PENAL;
			processaExcel();

		} catch (Exception e) {

			e.printStackTrace();

			fail();
		}
	}

	@Test
	public void testImportEstado() {

		try {
			CodigoEquipe = ESTADO;
			processaExcel();

		} catch (Exception e) {

			e.printStackTrace();

			fail();
		}
	}

	@Test
	public void testImportPessoa() {

		try {
			CodigoEquipe = PESSOA;
			processaExcel();

		} catch (Exception e) {

			e.printStackTrace();

			fail();
		}
	}
}

class ProposicalXLS {
	Origem origem;
	String sigla;
	String autoria;
	String tema;
	String areaDeMerito;
	String posicaoSAL;
	boolean prioritario = false;
	boolean despachado = false;
	String comissao;
	String providencias;
	String estagio;
	String despachoInicial;
	String responsavel;
	String macrotema;
	String pauta;
	String asparTxt;
	String situacao;
	String tipo;
	String numero;
	String ano;
	String drive;
	static Pattern p = Pattern.compile("(\\w+)\\s+(\\d+.*?)/?\\s?(\\d+)\\s*(\\((\\w+)\\s+(\\d+)/(\\d+)\\))?");

	boolean hasMore() {

		// System.out.println(sigla);
		if (m.find()) {
			tipo = m.group(1);
			numero = m.group(2);
			numero = numero.replace("-B", "");
			ano = m.group(3);
			if (ano.length() == 2) {
				ano = "20" + ano;
			}
			return true;

		} else {
			// System.err.println("IX " + sigla);
		}
		return false;
	}

	public void printRow() {
		int i = 0;
		for (Iterator<Cell> iterator = r.cellIterator(); iterator.hasNext();) {
			Cell type = (Cell) iterator.next();
			try {
				String col = "?";
				for (Iterator iterator2 = colunas.keySet().iterator(); iterator2.hasNext();) {
					String c = (String) iterator2.next();
					Integer pos = colunas.get(c);
					if (pos == i) {
						col = c;
						break;
					}

				}
				// System.out.println(i + ":'" + col + "':" +
				// type.getStringCellValue().length() + ":" +
				// type.getStringCellValue());
			} catch (Exception e) {
				System.err.println(i + ":" + e.getMessage());
			}
			i++;

		}

	}

	int tipoExecucao = -1;
	Matcher m = null;
	int rowNumber = -1;
	private Row r;
	String supar;
	Map<String, Integer> colunas = new HashMap<String, Integer>();

	void initColunas() {
		if (tipoExecucao == ImporterTests.ESTADO) {
			// Estado
			colunas.put("origem", 0);
			colunas.put("sigla", 1);
			colunas.put("autoria", 2);
			colunas.put("tema", 3);
			colunas.put("areaDeMerito", 4);
			colunas.put("posicaoSAL", 5);
			colunas.put("prioritario", 6);
			colunas.put("despachado", 7);
			colunas.put("comissao", 8);
			colunas.put("providencias", 9);
			colunas.put("estagio", 10);
			colunas.put("despachoInicial", 11);
			colunas.put("responsavel", 12);
			colunas.put("drive", 13);
			colunas.put("macrotema", 14);
			colunas.put("pauta", 15);
			colunas.put("asparTxt", 16);
			colunas.put("supar", 17);
			colunas.put("situacao", 18);
		} else if (tipoExecucao == ImporterTests.PENAL) {
			// // Penal
			colunas.put("origem", 0);
			colunas.put("sigla", 1);
			colunas.put("autoria", 2);
			colunas.put("tema", 3);
			colunas.put("areaDeMerito", 4);
			colunas.put("posicaoSAL", 5);
			colunas.put("prioritario", 6);
			colunas.put("despachado", 7);
			colunas.put("comissao", 8);

			colunas.put("situacao", 9);
			colunas.put("responsavel", 10);
			colunas.put("providencias", 11);
			colunas.put("pauta", 12);
			colunas.put("asparTxt", 15);

			colunas.put("despachoInicial", 20);
			colunas.put("drive", 20);
			colunas.put("macrotema", 20);
			colunas.put("estagio", 20);
			colunas.put("supar", 20);
		} else if (tipoExecucao == ImporterTests.PESSOA) {
			// Pessoa
			colunas.put("origem", 0);
			colunas.put("sigla", 1);
			colunas.put("autoria", 2);
			colunas.put("tema", 3);
			colunas.put("areaDeMerito", 4);
			colunas.put("posicaoSAL", 5);
			colunas.put("prioritario", 6);
			colunas.put("despachado", 7);
			colunas.put("comissao", 8);
			colunas.put("macrotema", 9);
			colunas.put("estagio", 10);
			colunas.put("responsavel", 11);
			colunas.put("providencias", 12);
			colunas.put("pauta", 13);
			colunas.put("asparTxt", 15);
			colunas.put("supar", 16);

			colunas.put("despachoInicial", 20);
			colunas.put("drive", 20);

			colunas.put("situacao", 17);
		}

	}

	ProposicalXLS(Row r, int tipoExec) {
		this.tipoExecucao = tipoExec;
		initColunas();
		this.r = r;
		rowNumber = r.getRowNum();

		if ("Câmara".equals(r.getCell(colunas.get("origem")).getStringCellValue())) {
			origem = Origem.CAMARA;
		} else if ("Senado".equals(r.getCell(colunas.get("origem")).getStringCellValue())) {
			origem = Origem.SENADO;
		} else {
			throw new IllegalArgumentException("Origem errada " + r.getCell(colunas.get("origem")));
		}

		sigla = r.getCell(colunas.get("sigla")).getStringCellValue();
		m = p.matcher(sigla);
		hasMore();

		autoria = r.getCell(colunas.get("autoria")).getStringCellValue();
		tema = r.getCell(colunas.get("tema")).getStringCellValue();
		areaDeMerito = r.getCell(colunas.get("areaDeMerito")).getStringCellValue();
		posicaoSAL = r.getCell(colunas.get("posicaoSAL")).getStringCellValue();
		String prioStr = r.getCell(colunas.get("prioritario")).getStringCellValue();

		if ("Sim".equals(prioStr)) {
			prioritario = true;
		} else if ("Não".equals(prioStr) || prioStr.isEmpty()) {
			prioritario = false;
		} else {
			throw new IllegalArgumentException("prioritario errada " + prioStr);
		}

		String desp = r.getCell(colunas.get("despachado")).getStringCellValue();

		if ("ok".equalsIgnoreCase(desp)) {
			despachado = true;
		} else if (desp.isEmpty()) {
			despachado = false;
		} else {
			System.err.println("despachado errada " + desp + " " + r.getRowNum());
		}

		comissao = r.getCell(colunas.get("comissao")).getStringCellValue();
		providencias = r.getCell(colunas.get("providencias")).getStringCellValue();
		if (r.getCell(colunas.get("estagio")) != null) {
			estagio = r.getCell(colunas.get("estagio")).getStringCellValue();
		}
		if (r.getCell(colunas.get("despachoInicial")) != null) {
			despachoInicial = r.getCell(colunas.get("despachoInicial")).getStringCellValue();
		}
		responsavel = r.getCell(colunas.get("responsavel")).getStringCellValue();
		if (r.getCell(colunas.get("drive")) != null) {
			Hyperlink link = r.getCell(colunas.get("drive")).getHyperlink();
			if (link != null) {
				this.drive = link.getAddress();
			}
		}
		if (r.getCell(colunas.get("macrotema")) != null) {
			macrotema = r.getCell(colunas.get("macrotema")).getStringCellValue();
		}
		pauta = r.getCell(colunas.get("pauta")).getStringCellValue();
		if (r.getCell(colunas.get("asparTxt")) != null) {
			asparTxt = r.getCell(colunas.get("asparTxt")).getStringCellValue();
		}
		if (r.getCell(colunas.get("situacao")) != null) {
			situacao = r.getCell(colunas.get("situacao")).getStringCellValue();
		}

		if (r.getCell(colunas.get("supar")) != null) {
			supar = r.getCell(colunas.get("supar")).getStringCellValue();
		}
		// System.out.println(this);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return rowNumber + ":" + origem.name() + " " + sigla + " (" + numero + "/" + ano + ")" + " sit:'" + situacao;
	}
}