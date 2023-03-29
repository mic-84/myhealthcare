package it.unipi.lsmsd.myhealthcare.dto;

public abstract class AbstractStructureDTO {
	protected String aslCode, structureCode, name, address;
	protected CityNoDetailsDTO city;
	protected String region, area;

	public AbstractStructureDTO(){}

	public AbstractStructureDTO(String aslCode, String structureCode, String name, String address,
                                CityNoDetailsDTO city, String region, String area) {
		this.aslCode = aslCode;
		this.structureCode = structureCode;
		this.name = name;
		this.address = address;
		this.city = city;
		this.region = region;
		this.area = area;
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

	public CityNoDetailsDTO getCity() {
		return city;
	}

	public void setCity(CityNoDetailsDTO city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
}
