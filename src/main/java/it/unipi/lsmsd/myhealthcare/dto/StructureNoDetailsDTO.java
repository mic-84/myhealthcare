package it.unipi.lsmsd.myhealthcare.dto;

import org.bson.Document;
import org.bson.types.ObjectId;

public class StructureNoDetailsDTO extends AbstractStructureDTO {
	private String id;

	public StructureNoDetailsDTO(){}

	public StructureNoDetailsDTO(String aslCode, String structureCode, String name, String address,
								 CityNoDetailsDTO city, String region, String area) {
		super(aslCode, structureCode, name, address, city, region, area);
	}

	public StructureNoDetailsDTO(StructureDTO structure) {
		this.aslCode = structure.getAslCode();
		this.structureCode = structure.getStructureCode();
		this.name = structure.getName();
		this.address = structure.getAddress();
		this.city = structure.getCity();
		this.region = structure.getRegion();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Structure{" +
				"id='" + id + '\'' +
				", aslCode='" + aslCode + '\'' +
				", structureCode='" + structureCode + '\'' +
				", name='" + name + '\'' +
				", address='" + address + '\'' +
				", city='" + city + '\'' +
				", region='" + region + '\'' +
				'}';
	}

	public Document toBsonDocument(){
		Document document = new Document();
		document.append("_id", new ObjectId(id));
		document.append("aslCode", aslCode);
		document.append("structureCode", structureCode);
		document.append("name", name);
		document.append("address", address);
		document.append("city", city.toBsonDocument());
		document.append("region", region);

		return document;
	}
}
