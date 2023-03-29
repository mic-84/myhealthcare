package it.unipi.lsmsd.myhealthcare.model;

public class Service {
	protected String id;
	protected String code, name;

	public Service(){}

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

	public String toString() {
		return code + " - " + name;
	}
}
