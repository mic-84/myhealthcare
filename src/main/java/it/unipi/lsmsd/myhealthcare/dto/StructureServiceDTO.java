package it.unipi.lsmsd.myhealthcare.dto;

import org.bson.Document;
import org.bson.types.ObjectId;

public class StructureServiceDTO extends AbstractServiceDTO {
	private String id;
	private Float rate;
	private boolean active;

	public StructureServiceDTO(){}

	public StructureServiceDTO(String code, String name, Float rate) {
		this.code = code;
		this.name = name;
		this.rate = rate;
		active = true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Float getRate() {
		return rate;
	}

	public void setRate(Float rate) {
		this.rate = rate;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "Service{" +
				"id='" + id + '\'' +
				", code='" + code + '\'' +
				", name='" + name + '\'' +
				", rate=" + rate +
				", active=" + active +
				'}';
	}

	public Document toBsonDocument(){
		Document document = new Document();
		document.append("_id", new ObjectId(id));
		document.append("code", code);
		document.append("name", name);
		document.append("rate", rate);
		document.append("active", active);

		return document;
	}
}
