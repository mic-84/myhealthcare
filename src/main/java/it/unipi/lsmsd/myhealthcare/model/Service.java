package it.unipi.lsmsd.myhealthcare.model;

public class Service {
	private String id;
	private String code, name;
	private Float rate;
	private boolean active;

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

	public String toString() {
		return code + " - " + name + " (euro " + rate + ")";
	}

	public String getGraphicActive(){
		if(active) return "<img src='/resources/img/check.png'  height='20' width='20'>";
		else return " ";
	}
}
