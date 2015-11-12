package br.gov.mj.sislegis.app.json;

import java.io.Serializable;

public class TagJSON implements Serializable {

	private static final long serialVersionUID = -5488024476236712816L;

	public TagJSON(String tag){
		this.id = tag.hashCode();
		this.tag =tag;
	}
	
	private Number id;
	
	public TagJSON(){}
	
	private String tag;
	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Number getId() {
		return id;
	}

	public void setId(Number id) {
		this.id = id;
	}

}
