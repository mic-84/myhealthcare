package it.unipi.lsmsd.myhealthcare.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection="City")
public class CityDTO extends AbstractCityDTO {
	@Id
	private String id;
	private List<CityNoDetailsDTO> neighbours;

	public CityDTO(){
		neighbours = new ArrayList<CityNoDetailsDTO>();
	}
	public CityDTO(String code, String name, String province, String region) {
		this.code = code;
		this.name = name;
		this.province = province;
		this.region = region;
		neighbours = new ArrayList<CityNoDetailsDTO>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public List<CityNoDetailsDTO> getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(List<CityNoDetailsDTO> neighbours) {
		this.neighbours = neighbours;
	}

	public void addNeighbour(CityNoDetailsDTO neighbour) {
		neighbours.add(neighbour);
	}

	@Override
	public String toString() {
		return "City{" +
				"id='" + id + '\'' +
				", code='" + code + '\'' +
				", name='" + name + '\'' +
				", province='" + province + '\'' +
				", region='" + region + '\'' +
				", neighbours=" + neighbours.size() +
				'}';
	}
}
