package it.unipi.lsmsd.myhealthcare.dto;

import org.bson.Document;
import org.bson.types.ObjectId;

public class CityNoDetailsDTO extends AbstractCityDTO {
	private String id;

	public CityNoDetailsDTO(){}

	public CityNoDetailsDTO(String id, String code, String name, String province, String region) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.province = province;
		this.region = region;
	}

	public CityNoDetailsDTO(CityDTO cityDTO) {
		this.id = cityDTO.getId();
		this.code = cityDTO.getCode();
		this.name = cityDTO.getName();
		this.province = cityDTO.getProvince();
		this.region = cityDTO.getRegion();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "City{" +
				"id='" + id + '\'' +
				", code='" + code + '\'' +
				", name='" + name + '\'' +
				", province='" + province + '\'' +
				", region='" + region + '\'' +
				'}';
	}

	public Document toBsonDocument(){
		Document document = new Document();
		document.append("_id", new ObjectId(id));
		document.append("code", code);
		document.append("name", name);
		document.append("province", province);
		document.append("region", region);

		return document;
	}
}
