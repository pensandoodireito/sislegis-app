package br.gov.mj.sislegis.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Reuniao extends AbstractEntity implements Comparable<Reuniao> {

	private static final long serialVersionUID = -3187796439185752162L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(nullable = false, unique = true)
	@Temporal(TemporalType.DATE)
	private Date data;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (id != null)
			result += "id: " + id;
		if (data != null)
			result += ", data: " + data;
		return result;
	}

	@Override
	public int compareTo(Reuniao o) {
		if (o.data.before(data)) {
			return 1;
		} else if (o.data.after(data)) {
			return -1;
		}
		return 0;
	}

}