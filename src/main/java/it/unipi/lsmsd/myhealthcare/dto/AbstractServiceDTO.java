package it.unipi.lsmsd.myhealthcare.dto;

public abstract class AbstractServiceDTO {
	protected String code, name;

	public AbstractServiceDTO(){}

	public AbstractServiceDTO(String code, String name) {
		this.code = code;
		this.name = name;
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

	@Override
	public String toString() {
		return "Service{" +
				", code='" + code + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
