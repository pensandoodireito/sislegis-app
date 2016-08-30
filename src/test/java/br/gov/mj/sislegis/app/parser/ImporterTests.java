package br.gov.mj.sislegis.app.parser;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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
import br.gov.mj.sislegis.app.model.EstadoProposicao;
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
import br.gov.mj.sislegis.app.service.ejbs.NotificacaoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.PosicionamentoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.ProposicaoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.ReuniaoProposicaoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.ReuniaoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.TagServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.TarefaServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.TipoEncaminhamentoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.UsuarioServiceEjb;

public class ImporterTests {
	private static final String EMAIL_USUARIO_PADRAO = "rafael.coutinho@gmail.com";
	PosicionamentoService posicionamentoSvc;
	ProposicaoService proposicaoService;
	ComissaoService comissaoService;
	TipoEncaminhamentoService tipoEncaminhamentoService;
	EncaminhamentoProposicaoService encaminhamentoService;
	TarefaService tarefaService;
	NotificacaoService notiService;
	ComentarioService comentarioService;
	private UsuarioService userSvc;
	EntityManager entityManager;
	TagService tagService;
	private static EntityManagerFactory emf = null;
	EntityManager em;
	private ReuniaoServiceEjb reuniaoEJB;
	private ReuniaoProposicaoServiceEjb reuniaoProposicaoEJB;

	static Map<String, String> atribuidoToResponsavel = new HashMap<String, String>();
	static {
		String[] user = { "Eduarda", "Guilherme", "Leonardo", "Marcelo", "Natalia", "Paula", "Sem atribuição", "Afonso" };
		String[] userEmail = { "eduarda.cintra@mj.gov.br", "guialmeida@gmail.com", "Leonardo", "Marcelo",
				"natalia.langenegger@mj.gov.br", "Paula", null, "Afonso" };

		for (int i = 0; i < userEmail.length; i++) {
			atribuidoToResponsavel.put(user[i], userEmail[i]);
		}

	}
	Map<String, Posicionamento> posicionamentos = new HashMap<String, Posicionamento>();
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
		List<Posicionamento> posicoes = posicionamentoSvc.listAll();
		for (Iterator iterator = posicoes.iterator(); iterator.hasNext();) {
			Posicionamento posicionamento = (Posicionamento) iterator.next();
			posicionamentos.put(posicionamento.getNome().toLowerCase(), posicionamento);
		}
		for (Iterator iterator = tipoEncaminhamentoService.listAll().iterator(); iterator.hasNext();) {
			TipoEncaminhamento enc = (TipoEncaminhamento) iterator.next();
			if (enc.getNome().equals("Elaborar Nota Técnica")) {
				this.tipoEnc = enc;
			}
		}

	}

	private void processaExcel() throws IOException {
		XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(
				"/home/sislegis/workspace/b/src/main/resources/Acompanhamento PLs Estado.xlsx")));

		ParserPautaCamara pautaCamara = new ParserPautaCamara();
		ParserPautaSenado pautaSenado = new ParserPautaSenado();
		List<ProposicalXLS> list = new ArrayList<ProposicalXLS>();
		// for (int i = 0; i < wb.getNumberOfSheets(); i++) {
		// Sheet sheet = wb.getSheetAt(i);
		{
			Sheet sheet = wb.getSheet("Projetos");
			int k = 0;
			for (Row row : sheet) {
				// if (k++ > 5) {
				// return;
				// }
				if (row.getCell(0) != null) {
					String origem = row.getCell(0).getStringCellValue();
					if ("Câmara".equals(origem) || "Senado".equals(origem)) {

						ProposicalXLS p = new ProposicalXLS(row);
						System.out.println(p);
						ProposicaoSearcher parserPropCamara = new ParserProposicaoCamara();

						if ("Senado".equals(origem)) {
							parserPropCamara = new ParserProposicaoSenado();
						}
						EntityTransaction trans = em.getTransaction();

						try {
							Proposicao prop = parserPropCamara
									.searchProposicao(p.tipo, Integer.parseInt(p.numero), Integer.parseInt(p.ano))
									.iterator().next();
							Proposicao propdb = proposicaoService.buscarPorIdProposicao(prop.getIdProposicao());
							if (propdb != null) {
								prop = propdb;
							}

							Usuario responsavel = userSvc.findByEmail(atribuidoToResponsavel.get(p.responsavel));
							System.out.println(p.responsavel + " " + responsavel);
							prop.setExplicacao(p.tema);
							prop.setResponsavel(responsavel);
							prop.setParecerSAL(p.providencias);

							prop.setResultadoASPAR(p.asparTxt);
							prop.setFavorita(p.prioritario);

							if (p.despachado) {
								prop.setEstado(EstadoProposicao.DESPACHADA);
							} else {
								prop.setEstado(EstadoProposicao.EMANALISE);
							}
							trans.begin();
							List<Tag> tags = tagService.buscaPorSufixo(p.macrotema);
							Tag t = new Tag();
							if (tags.isEmpty()) {
								if (p.macrotema != null && p.macrotema.length() > 0) {
									System.err.println("** Nenhuma tag para " + p.macrotema);

									t.setTag(p.macrotema.trim());

									t = tagService.save(t);
									// System.out.println(t.getId());
									// tags =
									// tagService.buscaPorSufixo(p.macrotema);
									// System.out.println(tags.size());
									tags.add(t);
								}
							} else {
								System.err.println("Encontrou tag para " + p.macrotema + " = " + tags.size());
								t = tags.iterator().next();
								for (Iterator iterator = tags.iterator(); iterator.hasNext();) {
									Tag tag = (Tag) iterator.next();
									System.out.println(tag.getTag());

								}
							}
							prop.setTags(tags);
							if (p.comissao != null && p.comissao.length() > 0) {
								prop.setComissao(p.comissao);
							}

							proposicaoService.save(prop);
							if (p.drive != null && p.drive.length() > 0) {
								EncaminhamentoProposicao ep = new EncaminhamentoProposicao();
								Comentario ce = new Comentario();
								ce.setAutor(userSvc.findByEmail(EMAIL_USUARIO_PADRAO));
								ce.setProposicao(prop);
								ce.setDescricao("Buscar do Drive: " + p.drive);
								ce.setDataCriacao(new Date());
								ep.setComentario(ce);
								ep.setDetalhes("Buscar do Drive: " + p.drive);
								ep.setResponsavel(userSvc.findByEmail(EMAIL_USUARIO_PADRAO));
								ep.setProposicao(prop);
								ep.setTipoEncaminhamento(this.tipoEnc);
								encaminhamentoService.salvarEncaminhamentoProposicao(ep);

							}

							if (p.areaDeMerito != null && p.areaDeMerito.length() > 0) {
								Comentario c = new Comentario();
								if (responsavel != null) {
									c.setAutor(responsavel);
								} else {
									c.setAutor(userSvc.findByEmail(EMAIL_USUARIO_PADRAO));
								}
								c.setDescricao(p.areaDeMerito);
								System.out.println(p.areaDeMerito + " " + prop.getNumero());
								c.setProposicao(prop);
								c.setDataCriacao(new Date());
								em.persist(c);
							}

							trans.commit();

							if (p.posicaoSAL != null && p.posicaoSAL.length() > 0) {
								trans = em.getTransaction();
								trans.begin();
								Posicionamento posicionamento = posicionamentos.get(p.posicaoSAL.toLowerCase());
								if (posicionamento != null) {
									System.out.println("Achou " + posicionamento);
								} else {
									if ("".equals(p.posicaoSAL)) {

									} else {
										posicionamento = new Posicionamento();
										posicionamento.setNome(p.posicaoSAL.trim());
										em.persist(posicionamento);
										System.err.println("Posicionametno novo " + p.posicaoSAL);
									}
								}
								if (posicionamentos != null) {
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
								}
								trans.commit();
							}
							if (p.pauta.length() > 0) {
								trans = em.getTransaction();
								trans.begin();
								System.out.println("PAUTA " + p.pauta);
								prop = proposicaoService.buscarPorIdProposicao(prop.getIdProposicao());
								proposicaoService.syncDadosPautaProposicao(prop.getId());
								trans.commit();
							}

						} catch (Exception e) {
							e.printStackTrace();
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

	@Test
	public void testDBAccess() {
		try {

			processaExcel();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
	static Pattern p = Pattern.compile("(\\w+)\\s+(\\d+.*?)/(\\d+)\\s*(\\((\\w+)\\s+(\\d+)/(\\d+)\\))?");

	ProposicalXLS(Row r) {
		int c = 0;
		if ("Câmara".equals(r.getCell(c).getStringCellValue())) {
			origem = Origem.CAMARA;
		} else if ("Senado".equals(r.getCell(c).getStringCellValue())) {
			origem = Origem.SENADO;
		} else {
			throw new IllegalArgumentException("Origem errada " + r.getCell(c));
		}

		sigla = r.getCell(++c).getStringCellValue();
		Matcher m = p.matcher(sigla);
		System.out.println(sigla);
		if (m.find()) {

			tipo = m.group(1);
			numero = m.group(2);

			ano = m.group(3);

		} else {
			System.err.println("IX " + sigla);
		}

		autoria = r.getCell(++c).getStringCellValue();
		tema = r.getCell(++c).getStringCellValue();
		areaDeMerito = r.getCell(++c).getStringCellValue();
		posicaoSAL = r.getCell(++c).getStringCellValue();
		String prioStr = r.getCell(++c).getStringCellValue();

		if ("Sim".equals(prioStr)) {
			prioritario = true;
		} else if ("Não".equals(prioStr) || prioStr.isEmpty()) {
			prioritario = false;
		} else {
			throw new IllegalArgumentException("prioritario errada " + prioStr);
		}

		String desp = r.getCell(++c).getStringCellValue();

		if ("ok".equalsIgnoreCase(desp)) {
			despachado = true;
		} else if (desp.isEmpty()) {
			despachado = false;
		} else {
			System.err.println("despachado errada " + desp + " " + r.getRowNum());
		}

		comissao = r.getCell(++c).getStringCellValue();
		providencias = r.getCell(++c).getStringCellValue();
		estagio = r.getCell(++c).getStringCellValue();
		despachoInicial = r.getCell(++c).getStringCellValue();
		responsavel = r.getCell(++c).getStringCellValue();
		Hyperlink link = r.getCell(++c).getHyperlink();
		if (link != null) {
			this.drive = link.getAddress();
		}

		macrotema = r.getCell(++c).getStringCellValue();
		pauta = r.getCell(++c).getStringCellValue();
		asparTxt = r.getCell(++c).getStringCellValue();
		situacao = r.getCell(++c).getStringCellValue();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return origem.name() + " " + sigla;
	}
}