package it.unipi.lsmsd.myhealthcare.mongo.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection="City")
public class CityDTO {
	@Id
	private String id;
	private String code, name, province;
	private List<String> neighbours;

	public CityDTO(){
		neighbours = new ArrayList<String>();
	}
	public CityDTO(String code, String name, String province) {
		this.code = code;
		this.name = name;
		this.province = province;
		neighbours = new ArrayList<String>();
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

	public List<String> getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(List<String> neighbours) {
		this.neighbours = neighbours;
	}
	
	public void addNeighbour(String neighbour) {
		neighbours.add(neighbour);
	}
	
	public String toString() {
		return id + ": " + name + " (" + province + ")";
	}
}
