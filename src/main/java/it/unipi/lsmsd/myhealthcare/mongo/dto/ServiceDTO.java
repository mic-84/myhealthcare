package it.unipi.lsmsd.myhealthcare.mongo.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Service")
public class ServiceDTO {
	@Id
	private String id;
	private String code, name;
	private Float rate;
	private boolean active;

	public ServiceDTO(){}

	public ServiceDTO(String code, String name, Float rate) {
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		return "ServiceMongo{" +
				"id='" + id + '\'' +
				", code='" + code + '\'' +
				", name='" + name + '\'' +
				", rate=" + rate +
				", active=" + active +
				'}';
	}
}
