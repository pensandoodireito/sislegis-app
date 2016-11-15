package br.gov.mj.sislegis.app.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.ws.rs.core.Response.Status;

import org.apache.http.entity.ContentType;
import org.json.JSONObject;

import br.gov.mj.sislegis.app.model.AreaDeMeritoRevisao;
import br.gov.mj.sislegis.app.model.Documento;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.model.documentos.Briefing;
import br.gov.mj.sislegis.app.model.documentos.Emenda;
import br.gov.mj.sislegis.app.model.documentos.NotaTecnica;
import br.gov.mj.sislegis.app.rest.authentication.UsuarioAutenticadoBean;
import br.gov.mj.sislegis.app.rest.authentication.UsuarioNaoLogado;
import br.gov.mj.sislegis.app.service.AreaDeMeritoService;
import br.gov.mj.sislegis.app.service.DocumentoService;
import br.gov.mj.sislegis.app.service.ProposicaoService;

/**
 * Servlet to manage ontologies files upload
 * 
 * @author rcoutinh@redhat.com
 *
 */

@WebServlet(name = "documentos", urlPatterns = { "/documentos", "/documentos/*" })
@MultipartConfig
public class FileUploadEndpoint extends HttpServlet {

	/**
	 * 
	 */
	@Inject
	private UsuarioAutenticadoBean controleUsuarioAutenticado;
	private static final long serialVersionUID = 1L;
	@Inject
	ProposicaoService propService;

	@Inject
	DocumentoService docService;
	@Inject
	AreaDeMeritoService areaService;
	private static final Logger logger = Logger.getLogger("sislegis");

	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);
	}

	/**
	 * Opens the form for uploading the model
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Principal principal = null;
		String authType = null;
		String remoteUser = null;

		// Get security principal
		principal = req.getUserPrincipal();
		// Get user name from login principal
		remoteUser = req.getRemoteUser();
		// Get authentication type
		authType = req.getAuthType();
		String str = (String) req.getSession().getAttribute("sislegisId");
		System.out.println("Session: " + str);
		System.out.println("principal: " + principal);

	}

	private void downloadFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String auth = req.getParameter("a");

			Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(auth);

			int pos = req.getRequestURI().indexOf("/documentos/");

			Long id = null;
			if (req.getParameter("id") != null) {
				id = Long.parseLong(req.getParameter("id"));
			} else {
				id = Long.parseLong(req.getRequestURI().substring(pos + "/documentos/".length()));
			}

			Documento doc = docService.findById(id);

			resp.setHeader("Content-Disposition", "inline; filename=\"" + doc.getNome() + "\"");

			org.apache.commons.io.IOUtils.copy(new FileInputStream(doc.getPath()), resp.getOutputStream());

			resp.flushBuffer();
		} catch (UsuarioNaoLogado e) {
			e.printStackTrace();
			resp.sendError(Status.FORBIDDEN.getStatusCode(), new JSONObject().put("error", "Usuário não autenticado").toString());
		}

	}

	private static String getSubmittedFileName(Part part) {

		for (String cd : part.getHeader("content-disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {
				String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
				return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE
																													// fix.
			}
		}
		return null;
	}

	/**
	 * Perform the file upload of the model.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		if ("v".equals(request.getParameter("v"))) {
			downloadFile(request, resp);
			return;
		}

		Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(request.getHeader("Authorization"));
		resp.setContentType(ContentType.APPLICATION_JSON.getMimeType());
		Part filePart = request.getPart("file");
		if (filePart == null || filePart.getSize() == 0) {
			resp.sendError(Status.BAD_REQUEST.getStatusCode(), new JSONObject().put("error", "Arquivo vazio").put("status", false).toString());
			return;
		}
		try {
			String str = (String) request.getSession().getAttribute("sislegisId");
			if (str == null) {
				request.getSession().setAttribute("sislegisId", user.getEmail());
			}
			String fileName = getSubmittedFileName(filePart);
			Integer typeOfUpload = Integer.parseInt(request.getParameter("type"));
			Documento documento = new Documento(fileName);
			documento.setUsuario(user);
			docService.save(documento, filePart);
			JSONObject respJson = new JSONObject();

			switch (typeOfUpload) {
			case 1: {
				Long proposicaoId = Long.parseLong(request.getParameter("idProposicao"));
				Proposicao p = propService.findById(proposicaoId);
				NotaTecnica nt = new NotaTecnica(p, user);
				nt.setDocumento(documento);
				propService.saveNotaTecnica(nt);
				JSONObject payload = new JSONObject();
				payload.put("id", nt.getId());
				payload.put("dataCriacao", nt.getDataCriacao());
				payload.put("documento", nt.getDocumento().toJson());
				payload.put("usuario", nt.getUsuario().toJson());

				respJson.put("payload", payload);
			}
				break;
			case 2: {
				Long proposicaoId = Long.parseLong(request.getParameter("idProposicao"));
				Proposicao p = propService.findById(proposicaoId);
				Briefing nt = new Briefing(p, user);
				nt.setDocumento(documento);
				propService.saveDocRelated(nt);
				JSONObject payload = new JSONObject();
				payload.put("id", nt.getId());
				payload.put("dataCriacao", nt.getDataCriacao());
				payload.put("documento", nt.getDocumento().toJson());
				payload.put("usuario", nt.getUsuario().toJson());

				respJson.put("payload", payload);
			}
				break;
			case 3: {
				Long proposicaoId = Long.parseLong(request.getParameter("idProposicao"));
				Proposicao p = propService.findById(proposicaoId);
				Emenda nt = new Emenda(p, user);
				nt.setDocumento(documento);
				propService.saveDocRelated(nt);
				JSONObject payload = new JSONObject();
				payload.put("id", nt.getId());
				payload.put("dataCriacao", nt.getDataCriacao());
				payload.put("documento", nt.getDocumento().toJson());
				payload.put("usuario", nt.getUsuario().toJson());

				respJson.put("payload", payload);
			}
				break;
			case 4: {
				Long idRevisao = Long.parseLong(request.getParameter("idRevisao"));
				System.out.println("id Revisao " + idRevisao);
				AreaDeMeritoRevisao p = areaService.findRevisao(idRevisao);
				p.setDocumento(documento);
				areaService.saveRevisao(p);

				JSONObject payload = new JSONObject();
				payload.put("id", p.getId());
				payload.put("dataCriacao", p.getDataCriacao().getTime());
				payload.put("documento", p.getDocumento().toJson());

				respJson.put("payload", payload);
			}
				break;
			default:

				break;
			}

			resp.getWriter().write(respJson.put("status", true).toString());

		} catch (UsuarioNaoLogado e) {
			e.printStackTrace();
			resp.sendError(Status.FORBIDDEN.getStatusCode(), new JSONObject().put("error", "Usuário não autenticado").toString());
		} catch (Exception e) {
			e.printStackTrace();
			resp.sendError(Status.INTERNAL_SERVER_ERROR.getStatusCode(), new JSONObject().put("error", "Internal error " + e.getMessage()).put("status", false).toString());
		}
	}
}
