package br.gov.mj.sislegis.app.parser.senado.xstream;

/*
 * wrapper para o nó /Autoria/Autor
 */
class Autor {
	String Nome;
	String Tratamento;
	String Foto;

	public String getDescricao() {
		String desc = "";
		if (Tratamento != null && Tratamento.length() > 0) {
			desc += Tratamento + " ";
		}
		if (Nome != null && Nome.length() > 0) {
			desc += Nome;
		}
		return desc;
	}
}