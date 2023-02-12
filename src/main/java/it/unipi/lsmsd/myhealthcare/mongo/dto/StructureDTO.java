package it.unipi.lsmsd.myhealthcare.mongo.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection="Structure")
public class StructureDTO {
	@Id
	private String id;
	private String aslCode, structureCode, name, address;
	private String city, region;
	private Set<ServiceDTO> services;

	public StructureDTO(){
		services = new HashSet<ServiceDTO>();
	}

	public StructureDTO(String aslCode, String structureCode, String name, String address, String city, String region) {
		this.aslCode = aslCode;
		this.structureCode = structureCode;
		this.name = name;
		this.address = address;
		this.city = city;
		this.region = region;

		services = new HashSet<ServiceDTO>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAslCode() {
		return aslCode;
	}

	public void setAslCode(String aslCode) {
		this.aslCode = aslCode;
	}

	public String getStructureCode() {
		return structureCode;
	}

	public void setStructureCode(String structureCode) {
		this.structureCode = structureCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Set<ServiceDTO> getServices() {
		return services;
	}

	public void setServices(Set<ServiceDTO> services) {
		this.services = services;
	}
	public void addService(ServiceDTO service) {
		services.add(service);
	}

	@Override
	public String toString() {
		return "StructureDTO{" +
				"id='" + id + '\'' +
				", aslCode='" + aslCode + '\'' +
				", structureCode='" + structureCode + '\'' +
				", name='" + name + '\'' +
				", address='" + address + '\'' +
				", city='" + city + '\'' +
				", region='" + region + '\'' +
				", services=" + services +
				'}';
	}

	public String completeToString(){
		String ret = toString() + "\n    id: " + id
				+ "\n    city: " + city + "\n    services: ";
		for(ServiceDTO service:services) {
			ret += "\n        - " + service.getId() + ", " + service.getRate();
			if(service.isActive())
				ret += " - active";
			else
				ret += " - not active";
		}

		return ret;
	}
}
