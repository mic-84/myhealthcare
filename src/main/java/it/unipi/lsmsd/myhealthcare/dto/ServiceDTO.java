package it.unipi.lsmsd.myhealthcare.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Service")
public class ServiceDTO extends AbstractServiceDTO {
	@Id
	private String id;

	public ServiceDTO(){}

	public ServiceDTO(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Service{" +
				"id='" + id + '\'' +
				", code='" + code + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
