package it.unipi.lsmsd.myhealthcare.model;

import it.unipi.lsmsd.myhealthcare.mongo.repository.CityRepository;
import it.unipi.lsmsd.myhealthcare.mongo.repository.ServiceRepository;

import java.util.ArrayList;
import java.util.List;

public class Structure {
	private String id;
	private String aslCode, structureCode, name, address, region;
	private City city;
	private List<Service> services;

	public Structure(){
		services = new ArrayList<Service>();
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

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public List<Service> getServices() {
		return services;
	}

	public List<Service> getActiveServices() {
		List<Service> activeServices = new ArrayList<Service>();
		for(Service service : services)
			if(service.isActive())
				activeServices.add(service);
		return activeServices;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}
	public void addService(Service service) {
		services.add(service);
	}

	public Integer getNumberOfServices(){
		return services.size();
	}

	public Integer getNumberOfActiveServices(){
		return getActiveServices().size();
	}

	public void changeServiceActivation(Service service) {
		services.add(service);
	}

	public Service getServiceById(String serviceId){
		for(Service service : getServices())
			if(service.getId().equals(serviceId))
				return service;
		return null;
	}

	public String toString() {
		return name + " - " + city.getName() + " (" + city.getProvince() + ")";
	}

	public String completeToString(CityRepository cityRepository, ServiceRepository serviceRepository){
		String ret = toString() + "\n    id: " + id
				+ "\n    city: " + city.getName() + "\n    services: ";
		for(Service service:services) {
			ret += "\n        - " + service;
			if(service.isActive())
				ret += " - active";
			else
				ret += " - not active";
		}

		return ret;
	}
}
