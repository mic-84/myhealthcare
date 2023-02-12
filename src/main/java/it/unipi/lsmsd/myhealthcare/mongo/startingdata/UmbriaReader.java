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

public class UmbriaReader {
	private static String line = "";
	private static String splitBy = ";";

	private static Map<String, StructureDTO> structures;
	
	public static void read(CityRepository cityRepository) {
		structures = new HashMap<String, StructureDTO>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					new File(MyHealthCareApplication.properties.umbriaStructures)));
			br.readLine();
			
			while ((line = br.readLine()) != null){
				line = line.replace("\"","");
				String[] lineSplit = line.split(splitBy);
				String cityId = null;
				try {
					cityId = Utility.getCityByCode(lineSplit[5]).getId();
				}
				catch (Exception e){
					cityId = null;
				}
				StructureDTO structure = new StructureDTO(lineSplit[2],lineSplit[3],lineSplit[4].toUpperCase(),
						lineSplit[6],cityId,"Umbria");
				structures.put(lineSplit[2]+lineSplit[3],structure);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println("read Umbria structures from starting data: " + structures.size());
	}

	public static Set<ServiceDTO> randomServices(ServiceRepository serviceRepository,
												 CityRepository cityRepository){
		Set<ServiceDTO> services = new HashSet<ServiceDTO>();
		List<ServiceDTO> storedServices = ServiceDao.readAllMongo(serviceRepository);
		int size = storedServices.size()-1;
		int quantity = Utility.getRandomInt(5,200);
		for(int i=0; i<quantity; i++){
			ServiceDTO service = storedServices.get(Utility.getRandomInt(0,size));
			service.setName(null);
			if(service.getRate()<1)
				service.setRate(1f);
			service.setRate(Utility.getRandomFloat(1.0f,service.getRate()));
			service.setActive(true);
			services.add(service);
		}

		return services;
	}

	public static void populateStructure(StructureRepository structureRepository,
										 ServiceRepository serviceRepository, CityRepository cityRepository){
		read(cityRepository);
		List<StructureDTO> structuresToSave = new ArrayList<StructureDTO>();
		for(Map.Entry<String, StructureDTO> set:structures.entrySet()) {
			set.getValue().setServices(randomServices(serviceRepository, cityRepository));
			structuresToSave.add(set.getValue());
		}
		StructureDao.createMany(structuresToSave,structureRepository);
	}
}
