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

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.AreaDeMeritoRevisao;
import br.gov.mj.sislegis.app.model.Comissao;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.service.AreaDeMeritoService;
import br.gov.mj.sislegis.app.service.ComissaoService;
import br.gov.mj.sislegis.app.service.ProposicaoService;

@WebServlet("/template")
public class TemplateDownloadServlet extends HttpServlet {
	@Inject
	private ProposicaoService proposicaoService;
	@Inject
	private ComissaoService comissao;

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
			}

			// response.
			// get your file as InputStream
			// is = new FileInputStream(generated);
			// // copy it to response's OutputStream
			// org.apache.commons.io.IOUtils.copy(is,
			// response.getOutputStream());
			response.flushBuffer();
		} catch (Exception ex) {

			throw new RuntimeException("Error criando modelo", ex);
		}

	}

	private XWPFDocument geraBriefing(Proposicao prop, HttpServletResponse response) throws IOException {
		InputStream fis = null;
		try {
			String filename = "Briefing_" + prop.getIdProposicao() + ".docx";

			response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

			Map<String, String> substituicoes = new HashMap<String, String>();
			substituicoes.put("[P.SIGLA]", prop.getSigla());
			substituicoes.put("[P.AUTOR]", prop.getAutor());
			String posicionamento = "Sem posicionamento";
			if (prop.getPosicionamentoAtual() != null && prop.getPosicionamentoAtual().getPosicionamento() != null) {
				posicionamento = prop.getPosicionamentoAtual().getPosicionamento().getNome();
			}
			substituicoes.put("[P.POSICIONAMENTO]", posicionamento);
			String explicacao = prop.getParecerSAL();
			if (explicacao == null) {
				explicacao = "";
			}
			substituicoes.put("[P.EXPLICACAO]", explicacao);
			substituicoes.put("[P.SITUACAO]", prop.getSituacao());

			String ementa = prop.getEmenta();
			if (prop.getExplicacao() != null && !prop.getExplicacao().isEmpty()) {
				ementa = prop.getExplicacao();
			}
			substituicoes.put("[P.EMENTA]", ementa);
			// Open the Word document file and instantiate the XWPFDocument
			// class.
			fis = this.getClass().getClassLoader().getResourceAsStream("Briefing.docx");
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

	String[] meses = { "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro",
			"Outubro", "Novembro", "Dezembro" };

	private XWPFDocument geraNota(Proposicao prop, HttpServletResponse response) throws Exception {
		InputStream fis = null;
		try {
			String filename = "NotaTecnica_" + prop.getIdProposicao() + ".docx";

			response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

			Map<String, String> substituicoes = new HashMap<String, String>();
			substituicoes.put("[P.SIGLA]", prop.getSigla());
			substituicoes.put("[P.NUM]", prop.getNumero());
			substituicoes.put("[P.NUMERO]", prop.getNumero());
			substituicoes.put("[P.ANO]", prop.getAno());
			String comissaoPorExtenso = prop.getComissao();
			if (Origem.CAMARA.equals(prop.getOrigem())) {
				for (Iterator iterator = comissao.listarComissoesCamara().iterator(); iterator.hasNext();) {
					Comissao type = (Comissao) iterator.next();
					if (type.getSigla().equals(comissaoPorExtenso)) {
						comissaoPorExtenso = type.getNome();
						break;
					}

				}

			} else {
				for (Iterator iterator = comissao.listarComissoesSenado().iterator(); iterator.hasNext();) {
					Comissao type = (Comissao) iterator.next();
					if (type.getSigla().equals(comissaoPorExtenso)) {
						comissaoPorExtenso = type.getNome();
						break;
					}

				}
			}
			substituicoes.put("[P.DATADIA]", "" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
			substituicoes.put("[P.DATAMES]", meses[Calendar.getInstance().get(Calendar.MONTH)]);
			substituicoes.put("[P.DATAANO]", Calendar.getInstance().get(Calendar.YEAR) + "");
			substituicoes.put("[P.COMISSAO]", comissaoPorExtenso);
			substituicoes.put("[P.AUTOR]", prop.getAutor());

			String relator = "";
			if (prop.getPautaComissaoAtual() != null) {
				relator = prop.getPautaComissaoAtual().getRelator();
			}
			substituicoes.put("[P.RELATOR]", relator);
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
			substituicoes.put("[P.SITUACAO]", prop.getSituacao());
			String posicionamentoAreas = "";
			for (Iterator<AreaDeMeritoRevisao> iterator = areaMeritoService.listRevisoesProposicao(prop.getId())
					.iterator(); iterator.hasNext();) {
				AreaDeMeritoRevisao revisao = (AreaDeMeritoRevisao) iterator.next();
				if (revisao.getPosicionamento() != null) {
					posicionamentoAreas += revisao.getAreaMerito().getNome() + " "
							+ revisao.getPosicionamento().getNome() + "\n";
				}

			}
			substituicoes.put("[P.POSICIONAMENTO_AREAS]", posicionamentoAreas);

			String ementa = prop.getEmenta();
			if (prop.getExplicacao() != null && !prop.getExplicacao().isEmpty()) {
				ementa = prop.getExplicacao();
			}
			substituicoes.put("[P.TEMA]", prop.getExplicacao());
			substituicoes.put("[P.EMENTA]", ementa);
			// Open the Word document file and instantiate the XWPFDocument
			// class.
			fis = this.getClass().getClassLoader().getResourceAsStream("NotaTecnica.docx");
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

			Map<String, String> substituicoes = new HashMap<String, String>();
			substituicoes.put("[P.NUM]", prop.getNumero());
			substituicoes.put("[P.ANO]", prop.getAno());
			String comissaoPorExtenso = prop.getComissao();
			if (Origem.CAMARA.equals(prop.getOrigem())) {
				for (Iterator iterator = comissao.listarComissoesCamara().iterator(); iterator.hasNext();) {
					Comissao type = (Comissao) iterator.next();
					if (type.getSigla().equals(comissaoPorExtenso)) {
						comissaoPorExtenso = type.getNome();
						break;
					}

				}

			} else {
				for (Iterator iterator = comissao.listarComissoesSenado().iterator(); iterator.hasNext();) {
					Comissao type = (Comissao) iterator.next();
					if (type.getSigla().equals(comissaoPorExtenso)) {
						comissaoPorExtenso = type.getNome();
						break;
					}

				}
			}
			substituicoes.put("[P.DATADIA]", "" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
			substituicoes.put("[P.DATAMES]", meses[Calendar.getInstance().get(Calendar.MONTH)]);
			substituicoes.put("[P.DATAANO]", Calendar.getInstance().get(Calendar.YEAR) + "");
			substituicoes.put("[P.COMISSAO]", comissaoPorExtenso);
			substituicoes.put("[P.SIGLA]", prop.getSigla());
			substituicoes.put("[P.AUTOR]", prop.getAutor());
			String posicionamento = "Sem posicionamento";
			if (prop.getPosicionamentoAtual() != null && prop.getPosicionamentoAtual().getPosicionamento() != null) {
				posicionamento = prop.getPosicionamentoAtual().getPosicionamento().getNome();
			}
			substituicoes.put("[P.POSICIONAMENTO]", posicionamento);
			String explicacao = prop.getParecerSAL();
			if (explicacao == null) {
				explicacao = "";
			}
			substituicoes.put("[P.EXPLICACAO]", explicacao);
			substituicoes.put("[P.SITUACAO]", prop.getSituacao());

			String ementa = prop.getEmenta();
			if (prop.getExplicacao() != null && !prop.getExplicacao().isEmpty()) {
				ementa = prop.getExplicacao();
			}
			substituicoes.put("[P.EMENTA]", ementa);
			// Open the Word document file and instantiate the XWPFDocument
			// class.
			fis = this.getClass().getClassLoader().getResourceAsStream("Emenda.docx");
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
