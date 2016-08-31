package br.gov.mj.sislegis.app.service;

import java.util.List;

import javax.ejb.Local;

import br.gov.mj.sislegis.app.model.Tag;

@Local
public interface TagService extends Service<Tag> {

    Tag findById(String id);

    List<Tag> listarTodasTags();

    List<Tag> buscaPorSufixo(String sufixo);

}
