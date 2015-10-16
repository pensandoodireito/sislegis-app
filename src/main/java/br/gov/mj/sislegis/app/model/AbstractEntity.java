package br.gov.mj.sislegis.app.model;

import java.io.Serializable;

/**
 * Estipula um contrato base para as entidades persistentes da aplicacao.
 * 
 * @author raphael.santos
 *
 */
public abstract class AbstractEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8805686324493542628L;

	/**
	 * @return A referencia para a chave primaria (Primary Key) de cada objeto
	 *         persistido. Caso o objeto ainda não tenha sido persistido, deve
	 *         retornar <code>null</code>.
	 */
	public abstract Number getId();

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj.getClass().equals(this.getClass())) || !(obj instanceof AbstractEntity)) {
			return false;
		}
		AbstractEntity other = (AbstractEntity) obj;
		if (getId() != null) {
			if (!getId().equals(other.getId())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

}
