package br.gov.mj.sislegis.app.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import br.gov.mj.sislegis.app.model.AreaDeMeritoRevisao;
import br.gov.mj.sislegis.app.model.Comissao;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.service.AreaDeMeritoService;
import br.gov.mj.sislegis.app.service.ComissaoService;
import br.gov.mj.sislegis.app.service.ProposicaoService;

@WebServlet("/template")
public class TemplateDownloadServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6083668243975013801L;
	@Inject
	private ProposicaoService proposicaoService;
	@Inject
	private ComissaoService comissaoService;

	@Inject
	private AreaDeMeritoService areaMeritoService;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		try {
			Long id = Long.parseLong(req.getParameter("id"));
			Proposicao prop = proposicaoService.findById(id);
			String type = req.getParameter("type");
			if ("briefing".equals(type)) {

				geraBriefing(prop, response);

			} else if ("nota".equals(type)) {
				geraNota(prop, response);
			} else if ("emenda".equals(type)) {
				geraEmenda(prop, response);
			} else {
				throw new IllegalAccessException("Acesso incorreto. Tipo desconhecido: '" + type);
			}

			response.flushBuffer();
		} catch (Exception ex) {

			throw new RuntimeException("Error criando modelo", ex);
		}

	}

	private XWPFDocument geraBriefing(Proposicao prop, HttpServletResponse response) throws Exception {
		InputStream fis = null;
		try {
			String filename = "ResumoAnalitico_" + prop.getIdProposicao() + ".docx";

			response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

			Map<String, String> substituicoes = getSubstituicoes(prop);
			// Open the Word document file and instantiate the XWPFDocument
			// class.
			fis = this.getClass().getClassLoader().getResourceAsStream("Briefing.docx");
			XWPFDocument doc = executaSubstituicoes(fis, substituicoes);
			doc.write(response.getOutputStream());
			return doc;
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
			}
		}

	}

	private XWPFDocument executaSubstituicoes(InputStream fis, Map<String, String> substituicoes) throws IOException {
		XWPFDocument doc = new XWPFDocument(fis);
		for (XWPFParagraph p : doc.getParagraphs()) {
			List<XWPFRun> runs = p.getRuns();
			if (runs != null) {
				for (XWPFRun r : runs) {
					String text = r.getText(0);
					for (Iterator<String> iterator = substituicoes.keySet().iterator(); iterator.hasNext();) {
						String chave = (String) iterator.next();
						if (text != null && text.contains(chave)) {
							text = text.replace(chave, substituicoes.get(chave));
							r.setText(text, 0);
						}
					}
				}
			}
		}
		for (XWPFTable tbl : doc.getTables()) {
			for (XWPFTableRow row : tbl.getRows()) {
				for (XWPFTableCell cell : row.getTableCells()) {
					for (XWPFParagraph p : cell.getParagraphs()) {
						for (XWPFRun r : p.getRuns()) {
							String text = r.getText(0);
							for (Iterator<String> iterator = substituicoes.keySet().iterator(); iterator.hasNext();) {
								String chave = (String) iterator.next();
								if (text != null && text.contains(chave)) {
									text = text.replace(chave, substituicoes.get(chave));
									r.setText(text, 0);
								}
							}
						}
					}
				}
			}
		}
		return doc;
	}

	private Map<String, String> getSubstituicoes(Proposicao prop) throws Exception {

		Map<String, String> siglasProjeto = new HashMap<String, String>();

		siglasProjeto.put("PL", "Projeto de Lei");
		siglasProjeto.put("MPV", "Medida Provisória");
		siglasProjeto.put("PEC", "Proposta de Emenda à Constituição");
		siglasProjeto.put("PLC", "Projeto de Lei da Câmara");
		siglasProjeto.put("PLS", "Projeto de Lei do Senado");

		siglasProjeto.put("EMD", "Emenda");
		siglasProjeto.put("MSC", "Mensagem");
		siglasProjeto.put("INC", "Indicação");
		siglasProjeto.put("PLP", "Projeto de Lei complementar (na Câmara)");
		siglasProjeto.put("PLV", "Projeto de Lei de Conversão");
		siglasProjeto.put("PDC", "Projeto de Decreto Legislativo (na Câmara)");
		siglasProjeto.put("PDS", "Projeto de Decreto Legislativo (no Senado)");
		siglasProjeto.put("PRC", "Projeto de Resolução (na Câmara)");
		siglasProjeto.put("PRS", "Projeto de Resolução (no Senado)");
		siglasProjeto.put("REQ", "Requerimento (na Câmara)");
		siglasProjeto.put("RQS", "Requerimento (no Senado)");
		siglasProjeto.put("RIC", "Requerimento de Informação");
		siglasProjeto.put("RCP", "Requerimento de Instituição de CPI");
		siglasProjeto.put("ECD", "Emendas da Câmara dos Deputados a projeto de lei do Senado");
		siglasProjeto.put("EDS", "Emendas da Câmara dos Deputados a projeto de Decreto Legislativo do Senado");
		siglasProjeto.put("PDN", "Projeto de Decreto Legislativo do Congresso Nacional");
		siglasProjeto.put("PLN", "Projeto de Lei do Congresso Nacional");
		Map<String, String> substituicoes = new HashMap<String, String>();

		String relator = "[RELATOR NÃO IDENTIFICADO]";
		if (prop.getPautaComissaoAtual() != null) {
			relator = prop.getPautaComissaoAtual().getRelator();
		}

		substituicoes.put("[P.RELATOR]", relator);
		substituicoes.put("[P.SIGLA]", prop.getSigla());

		String tipo = prop.getTipo().toUpperCase().trim();
		if (siglasProjeto.get(tipo) != null) {
			tipo = siglasProjeto.get(tipo);
		}
		substituicoes.put("[P.TIPO]", tipo);
		substituicoes.put("[P.TIPO_ABR]", prop.getTipo());
		substituicoes.put("[P.AUTOR]", prop.getAutor());
		substituicoes.put("[P.NUM]", prop.getNumero());
		substituicoes.put("[P.ANO]", prop.getAno());

		substituicoes.put("[P.DATADIA]", "" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		substituicoes.put("[P.DATAMES]", meses[Calendar.getInstance().get(Calendar.MONTH)]);
		substituicoes.put("[P.DATAANO]", Calendar.getInstance().get(Calendar.YEAR) + "");
		substituicoes.put("[P.AUTOR]", prop.getAutor());

		String posicionamento = "Sem posicionamento";
		if (prop.getPosicionamentoAtual() != null && prop.getPosicionamentoAtual().getPosicionamento() != null) {
			posicionamento = prop.getPosicionamentoAtual().getPosicionamento().getNome();
		}
		substituicoes.put("[P.POSICIONAMENTO]", posicionamento);

		substituicoes.put("[P.POSICIONAMENTO_UPPER]", posicionamento.toUpperCase());
		String explicacao = prop.getParecerSAL();
		if (explicacao == null) {
			explicacao = "";
		}
		substituicoes.put("[P.EXPLICACAO]", explicacao);
		String comissaoPorExtenso = prop.getComissao();
		Comissao comissao = comissaoService.getComissao(prop.getOrigem(), prop.getComissao());
		if (comissao != null) {
			comissaoPorExtenso = comissao.getNome();
		}
		substituicoes.put("P.COMISSAO", prop.getComissao());
		substituicoes.put("P.COMISSAOEXTENSO", comissaoPorExtenso);

		StringBuilder posicionamentoAreas = new StringBuilder();
		StringBuilder posicionamentoAreasFull = new StringBuilder();
		for (Iterator<AreaDeMeritoRevisao> iterator = areaMeritoService.listRevisoesProposicao(prop.getId()).iterator(); iterator.hasNext();) {
			AreaDeMeritoRevisao revisao = (AreaDeMeritoRevisao) iterator.next();
			if (revisao.getPosicionamento() != null) {
				StringBuilder area = new StringBuilder().append(revisao.getAreaMerito().getNome()).append(": ").append(revisao.getPosicionamento().getNome()).append("\n");
				posicionamentoAreasFull.append(area);
				if (revisao.getParecer() != null && !revisao.getParecer().isEmpty()) {
					posicionamentoAreasFull.append("\n\t").append(revisao.getParecer()).append("\n");
				}
				posicionamentoAreas.append(area);
			}

		}
		substituicoes.put("[P.POSICIONAMENTO_AREAS]", posicionamentoAreas.toString());
		substituicoes.put("[P.POSICIONAMENTO_AREAS_FULL]", posicionamentoAreasFull.toString());

		String ementa = prop.getEmenta();
		if (prop.getExplicacao() != null && !prop.getExplicacao().isEmpty()) {
			ementa = prop.getExplicacao();
		}
		substituicoes.put("[P.TEMA]", prop.getExplicacao());
		substituicoes.put("[P.EMENTA]", ementa);

		String tramitacao = prop.getTramitacao();
		if (tramitacao == null || tramitacao.isEmpty()) {
			substituicoes.put("[P.TRAMITACAO]", "[SEM INFORMAÇÃO DE TRAMITAÇÃO]");
		} else {
			substituicoes.put("[P.TRAMITACAO]", tramitacao);
		}

		return substituicoes;
	}

	String[] meses = { "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro" };

	private XWPFDocument geraNota(Proposicao prop, HttpServletResponse response) throws Exception {
		InputStream fis = null;
		try {
			String filename = "NotaTecnica_" + prop.getIdProposicao() + ".docx";

			response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

			String ementa = prop.getEmenta();
			if (prop.getExplicacao() != null && !prop.getExplicacao().isEmpty()) {
				ementa = prop.getExplicacao();
			}

			// Open the Word document file and instantiate the XWPFDocument
			// class.
			fis = this.getClass().getClassLoader().getResourceAsStream("NotaTecnica.docx");
			XWPFDocument doc = executaSubstituicoes(fis, getSubstituicoes(prop));
			doc.write(response.getOutputStream());
			return doc;
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
			}
		}

	}

	private XWPFDocument geraEmenda(Proposicao prop, HttpServletResponse response) throws Exception {
		InputStream fis = null;
		try {
			String filename = "Emenda_" + prop.getIdProposicao() + ".docx";

			response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

			// Open the Word document file and instantiate the XWPFDocument
			// class.
			fis = this.getClass().getClassLoader().getResourceAsStream("Emenda.docx");
			XWPFDocument doc = executaSubstituicoes(fis, getSubstituicoes(prop));
			doc.write(response.getOutputStream());
			return doc;
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
			}
		}

	}

}
