package br.gov.mj.sislegis.app.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRelation;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STUnderline;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.EstadoProposicao;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.service.ProposicaoService;
import br.gov.mj.sislegis.app.util.SislegisUtil;

@WebServlet("/relatorio")
public class ReportDownloadServlet extends HttpServlet {
	@Inject
	private ProposicaoService proposicaoService;

	/**
	 * Locate a string in a PDF and replace it with a new string.
	 *
	 * @param inputFile
	 *            The PDF to open.
	 * @param outputFile
	 *            The PDF to write to.
	 * @param strToFind
	 *            The string to find in the PDF document.
	 * @param message
	 *            The message to write in the file.
	 *
	 * @throws IOException
	 *             If there is an error writing the data.
	 * @throws COSVisitorException
	 *             If there is an error writing the PDF.
	 */
	public static void appendExternalHyperlink(String url, String text, XWPFParagraph paragraph) {

		// Add the link as External relationship
		String id = paragraph.getDocument().getPackagePart()
				.addExternalRelationship(url, XWPFRelation.HYPERLINK.getRelation()).getId();

		// Append the link and bind it to the relationship
		CTHyperlink cLink = paragraph.getCTP().addNewHyperlink();
		cLink.setId(id);

		// Create the linked text
		CTText ctText = CTText.Factory.newInstance();
		ctText.setStringValue(text);
		CTR ctr = CTR.Factory.newInstance();
		ctr.setTArray(new CTText[] { ctText });
		CTRPr rpr = ctr.addNewRPr();
		CTColor colour = CTColor.Factory.newInstance();
		colour.setVal("0000FF");
		rpr.setColor(colour);
		CTRPr rpr1 = ctr.addNewRPr();
		rpr1.addNewU().setVal(STUnderline.SINGLE);
		// Insert the linked text into the link
		cLink.setRArray(new CTR[] { ctr });
	}

	public XWPFDocument gerarRelatorio() throws IOException, ParserConfigurationException {
		try {

			// Open the Word document file and instantiate the XWPFDocument
			// class.
			XWPFDocument doc = new XWPFDocument(this.getClass().getClassLoader().getResourceAsStream("relatorio.docx"));
			XWPFTable tableTemplate = doc.getTables().get(0);
			XWPFTable camaraTable = cloneTable(tableTemplate, doc);

			Map<String, Object> filtros = new HashMap<String, Object>();
			filtros.put("origem", Origem.CAMARA.name());
			filtros.put("estado", EstadoProposicao.DESPACHADA.name());

			camaraTable.removeRow(1);

			Calendar sunday = Calendar.getInstance();
			sunday.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			sunday.set(Calendar.HOUR_OF_DAY, 0);
			sunday.set(Calendar.MINUTE, 0);
			sunday.set(Calendar.SECOND, 0);
			sunday.set(Calendar.MILLISECOND, 0);

			List<Proposicao> props = proposicaoService.consultar(filtros, 0, null);

			Collections.sort(props, new ProposicaoPautadasPrimeiro());
			populaProposicoesTabela(tableTemplate, camaraTable, props, sunday.getTime());

			XWPFTable tableTemplateSenado = doc.getTables().get(1);

			XWPFTable senadoTable = cloneTable(tableTemplateSenado, doc);
			senadoTable.removeRow(1);
			filtros.put("origem", Origem.SENADO.name());
			List<Proposicao> propsSenado = proposicaoService.consultar(filtros, 0, null);
			Collections.sort(propsSenado, new ProposicaoPautadasPrimeiro());
			populaProposicoesTabela(tableTemplateSenado, senadoTable, propsSenado, sunday.getTime());

			doc.setTable(0, camaraTable);

			doc.setTable(1, senadoTable);
			// doc.write(new
			// FileOutputStream("/home/sislegis/workspace/b/output2.docx"));
			return doc;

		} finally {

		}

	}

	private void populaProposicoesTabela(XWPFTable tableTemplate, XWPFTable camaraTable, List<Proposicao> props,
			Date ref) {
		for (Iterator iterator = props.iterator(); iterator.hasNext();) {
			Proposicao proposicao = (Proposicao) iterator.next();
			XWPFTableRow row = createTableRow(camaraTable, tableTemplate.getRow(1));

			if (!proposicao.getLinkProposicao().isEmpty()) {
				replaceText(row.getCell(0), "[PL]", "");
				appendExternalHyperlink(proposicao.getLinkProposicao(), proposicao.getSigla(), row.getCell(0)
						.getParagraphs().get(0));
			} else {
				replaceText(row.getCell(0), "[PL]", proposicao.getSigla());
			}

			replaceText(row.getCell(1), "[AUTOR]", proposicao.getAutor());
			if (proposicao.getExplicacao() != null) {
				replaceText(row.getCell(2), "[EMENTA]", proposicao.getExplicacao());
			} else {
				replaceText(row.getCell(2), "[EMENTA]", "Sem tema");
			}
			if (proposicao.getPosicionamentoAtual() != null
					&& proposicao.getPosicionamentoAtual().getPosicionamento() != null) {
				replaceText(row.getCell(3), "[POSICIONAMENTO]", proposicao.getPosicionamentoAtual().getPosicionamento()
						.getNome());
			} else {
				replaceText(row.getCell(3), "[POSICIONAMENTO]", "SEM POSICIONAMENTO");
			}
			replaceText(row.getCell(4), "[PRIORITARIO]", proposicao.isFavorita() ? "Sim" : "Não");
			if (proposicao.getComissao() == null) {
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.WARNING, "Proposicao "+proposicao.getSigla()+" não possui dados de comissão");
				replaceText(row.getCell(5), "[COMISSAO]", "Comissão não identificada");
			} else {
				replaceText(row.getCell(5), "[COMISSAO]", proposicao.getComissao());
			}

			if (proposicao.getPautaComissaoAtual() != null) {

				if (proposicao.getPautaComissaoAtual().getPautaReuniaoComissao() != null) {
					if (proposicao.getPautaComissaoAtual().getPautaReuniaoComissao().getData().after(ref)) {
						replaceText(row.getCell(6), "[PAUTA]", "Pautada");

						// replaceText(row.getCell(6), "[PAUTA]",
						// "Pautada para ("
						// + proposicao.getPautaComissaoAtual().getOrdemPauta()
						// + ") "
						// +
						// proposicao.getPautaComissaoAtual().getPautaReuniaoComissao().getData());
					} else {
						replaceText(row.getCell(6), "[PAUTA]", "Não Pautada");
					}
				} else {
					replaceText(row.getCell(6), "[PAUTA]", "Não Pautada");
				}

			} else {
				replaceText(row.getCell(6), "[PAUTA]", "Não Pautada");
			}
			camaraTable.addRow(row);
		}
	}

	private void replaceText(XWPFTableCell cell, String chave, String replacement) {
		for (XWPFParagraph p : cell.getParagraphs()) {

			for (XWPFRun r : p.getRuns()) {

				String text = r.getText(0);
				if (text != null) {
					text = text.replace(chave, replacement);
				} else {
					text = replacement;
				}
				r.setText(text, 0);
			}
		}
	}

	private static XWPFTableRow createTableRow(XWPFTable camaraTable, XWPFTableRow row) {
		// Copying a existing table row‍
		CTRow ctRow = CTRow.Factory.newInstance();
		ctRow.set(row.getCtRow());

		XWPFTableRow row2 = new XWPFTableRow(ctRow, camaraTable);
		if (camaraTable.getRows().size() % 2 == 0) {
			for (Iterator iterator = row2.getTableCells().iterator(); iterator.hasNext();) {
				XWPFTableCell type = (XWPFTableCell) iterator.next();
				type.setColor("ffffff");
			}
		}

		return row2;
	}

	private static XWPFTable cloneTable(XWPFTable table, XWPFDocument doc) {
		// Copying a existing table
		CTTbl ctTbl = CTTbl.Factory.newInstance();
		ctTbl.set(table.getCTTbl());
		XWPFTable table2 = new XWPFTable(ctTbl, doc);

		doc.createParagraph();
		doc.createTable(); // Create a empty table in the document
		// doc.setTable(1, table2); // Replace the empty table to table2
		return table2;
	}

	SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		try {
			String filename = "Relatorio_" + sdf.format(new Date()) + ".docx";

			response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

			gerarRelatorio().write(response.getOutputStream());
			// response.
			// get your file as InputStream
			// is = new FileInputStream(generated);
			// // copy it to response's OutputStream
			// org.apache.commons.io.IOUtils.copy(is,
			// response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex) {

			throw new RuntimeException("IOError writing file to output stream", ex);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("IOError writing file to output stream", e);
		} finally {

		}
	}

}
