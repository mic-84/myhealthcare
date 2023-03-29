package it.unipi.lsmsd.myhealthcare.model;

import java.util.ArrayList;
import java.util.List;

public class City {
	private String id;
	private String code, name, province, region;
	private List<City> neighbours;

	public City(){
		neighbours = new ArrayList<City>();
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

	public List<City> getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(List<City> neighbours) {
		this.neighbours = neighbours;
	}
	
	public void addNeighbour(City neighbour) {
		neighbours.add(neighbour);
	}
	
	public String toString() {
		return name + " (" + province + ")";
	}
}
