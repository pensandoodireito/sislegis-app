package br.gov.mj.sislegis.app.parser;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.parser.senado.ParserPlenarioSenado;
import br.gov.mj.sislegis.app.parser.senado.ParserProposicaoSenado;

public class TestSenadoWSParser {

	@Test
	public void testParserProposicaoSenado() {
		ParserProposicaoSenado parser = new ParserProposicaoSenado();
		Long idProposicao = 24257L;
		try {
			Proposicao proposicaoSenado = parser.getProposicao(idProposicao);
			Assert.assertNotNull("Autor nulo", proposicaoSenado.getAutor());
			Assert.assertFalse("Autor vazio", proposicaoSenado.getAutor()
					.isEmpty());

		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}

	@Test
	public void testParserPlenarioSenado() {
		ParserPlenarioSenado parser = new ParserPlenarioSenado();

		String datIni = "20140801";

		try {
			List<Proposicao> proposicoes = parser.getProposicoes(datIni);

			Assert.assertNotNull("Nenhuma proposicao encontrada", proposicoes);
			Assert.assertEquals("Total de proposicoes errado", 28,
					proposicoes.size());

			// TODO Deve haver um método que faça a validação de uma proposição
			// que deve ser utilizado pelo testParserProposicaoSenado e aqui.

		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());

		}
	}

	@Test
	public void testParserComissoesSenado() {
		// TODO testar ParserComissoesSenado
	}

	@Test
	public void testParserPautaSenado() {
		// TODO testar ParserPautaSenado
	}
}
