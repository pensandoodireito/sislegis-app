package br.gov.mj.sislegis.app.web;

import java.io.IOException;
import java.io.InputStream;
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

import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.service.ProposicaoService;

@WebServlet("/template")
public class TemplateDownloadServlet extends HttpServlet {
	@Inject
	private ProposicaoService proposicaoService;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		InputStream fis = null;
		try {
			Long id = Long.parseLong(req.getParameter("id"));
			Proposicao prop = proposicaoService.findById(id);
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
			// response.
			// get your file as InputStream
			// is = new FileInputStream(generated);
			// // copy it to response's OutputStream
			// org.apache.commons.io.IOUtils.copy(is,
			// response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex) {

			throw new RuntimeException("IOError writing file to output stream", ex);
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
