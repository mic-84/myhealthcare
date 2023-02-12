package it.unipi.lsmsd.myhealthcare.mongo.startingdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.dao.ServiceDao;
import it.unipi.lsmsd.myhealthcare.dao.StructureDao;
import it.unipi.lsmsd.myhealthcare.mongo.dto.ServiceDTO;
import it.unipi.lsmsd.myhealthcare.mongo.dto.StructureDTO;
import it.unipi.lsmsd.myhealthcare.mongo.repository.*;
import it.unipi.lsmsd.myhealthcare.utility.Utility;

public class LombardiaReader {
	private static String line = "";
	private static String splitBy = ";";

	private static Map<String, StructureDTO> structures;

	public static void read(ServiceRepository serviceRepository, CityRepository cityRepository){
		structures = new HashMap<String, StructureDTO>();
		readStructures(cityRepository);
		System.out.println("read Lombardia structures from starting data: " + structures.size());

		readServices(serviceRepository);
		int count = 0;
		for(Map.Entry<String, StructureDTO> set: structures.entrySet())
			if(set.getValue().getServices().size() > 0)
				count++;
		System.out.println("read services of " + count + " structures from starting data");
	}
	
	public static void readStructures(CityRepository cityRepository) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					new File(MyHealthCareApplication.properties.lombardiaStructures)));
			br.readLine();

			while ((line = br.readLine()) != null){
				line = line.replace("\"","");
				String[] lineSplit = line.split(splitBy);
				String cityId = null;
				try {
					cityId = Utility.getCityByName(lineSplit[9]).getId();
				}
				catch (Exception e){}
				StructureDTO structure = new StructureDTO(lineSplit[1],lineSplit[5],lineSplit[6],lineSplit[8],
						cityId,"Lombardia");
				structures.put(lineSplit[1]+lineSplit[5],structure);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void readServices(ServiceRepository serviceRepository) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					new File(MyHealthCareApplication.properties.lombardiaStructures)));
			br.readLine();
			List<ServiceDTO> services = ServiceDao.readAllMongo(serviceRepository);
			int count = 1;
			while ((line = br.readLine()) != null){
				count++;
				line = line.replace("\"","");
				String[] lineSplit = line.split(splitBy);
				for(Map.Entry<String, StructureDTO> set: structures.entrySet())
					if(set.getKey().contains(lineSplit[1]+lineSplit[5])) {
						ServiceDTO service = ServiceReader.getServiceByCode(services,lineSplit[17]);
						if(service != null) {
							service.setName(null);
							service.setRate(null);
							service.setActive(true);
							try {
								service.setRate(Float.parseFloat(lineSplit[20]));
							}
							catch(NumberFormatException e) {}
							set.getValue().addService(service);
						}
						break;
					}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static void populateStructure(StructureRepository structureRepository,
										 ServiceRepository serviceRepository, CityRepository cityRepository){
		structureRepository.deleteAll();
		read(serviceRepository, cityRepository);
		List<StructureDTO> structuresToSave = new ArrayList<StructureDTO>();
		for(Map.Entry<String, StructureDTO> set: structures.entrySet())
			if(set.getValue().getServices().size() > 0)
				structuresToSave.add(set.getValue());
		StructureDao.createMany(structuresToSave,structureRepository);
	}
}
