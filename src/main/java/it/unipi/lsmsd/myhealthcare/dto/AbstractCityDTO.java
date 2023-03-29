package it.unipi.lsmsd.myhealthcare.dto;

public abstract class AbstractCityDTO {
	protected String code, name, province, region;

	public AbstractCityDTO(){}

	public AbstractCityDTO(String code, String name, String province, String region) {
		this.code = code;
		this.name = name;
		this.province = province;
		this.region = region;
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

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}
}
