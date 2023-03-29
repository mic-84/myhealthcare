package it.unipi.lsmsd.myhealthcare.model;

public class StructureService extends Service{
	private Float rate;
	private boolean active;

	public StructureService(){}

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
