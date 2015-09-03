package br.gov.mj.sislegis.app.parser;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.parser.senado.ParserPlenarioSenado;
import br.gov.mj.sislegis.app.parser.senado.ParserProposicaoSenado;

public class TestSenado {

	@Test
	public void testParserProposicaoSenado() {
		ParserProposicaoSenado parser = new ParserProposicaoSenado();
		Long idProposicao = 24257L;
		try {
			Proposicao proposicaoSenado = parser.getProposicao(idProposicao);
			Assert.assertNotNull("Autor nulo", proposicaoSenado.getAutor());
			Assert.assertTrue("Autor vazio", proposicaoSenado.getAutor()
					.length() == 0);

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

		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());

		}
	}

}
