package it.unipi.lsmsd.myhealthcare.startingdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.dao.CityDao;
import it.unipi.lsmsd.myhealthcare.dao.ServiceDao;
import it.unipi.lsmsd.myhealthcare.dao.StructureDao;
import it.unipi.lsmsd.myhealthcare.dto.ServiceDTO;
import it.unipi.lsmsd.myhealthcare.dto.StructureServiceDTO;
import it.unipi.lsmsd.myhealthcare.dto.StructureDTO;
import it.unipi.lsmsd.myhealthcare.model.City;
import it.unipi.lsmsd.myhealthcare.repository.ServiceRepository;
import it.unipi.lsmsd.myhealthcare.repository.StructureRepository;
import it.unipi.lsmsd.myhealthcare.service.Utility;

public class UmbriaReader {
	private static String line = "";
	private static String splitBy = ";";

	private static Map<String, StructureDTO> structures;
	
	public static void read() {
		structures = new HashMap<String, StructureDTO>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					new File(MyHealthCareApplication.properties.umbriaStructures)));
			br.readLine();
			
			while ((line = br.readLine()) != null){
				line = line.replace("\"","");
				String[] lineSplit = line.split(splitBy);
				City city = null;
				try {
					city = Utility.getCityByCode(lineSplit[5]);
				}
				catch (Exception e){
					city = null;
				}
				StructureDTO structure = new StructureDTO(lineSplit[2],lineSplit[3],lineSplit[4].toUpperCase(),
						lineSplit[6], CityDao.toDTONoDetails(city),"Umbria","Centre");
				structures.put(lineSplit[2]+lineSplit[3],structure);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println("read Umbria structures from starting data: " + structures.size());
	}

	public static Set<StructureServiceDTO> randomServices(ServiceRepository serviceRepository){
		Set<StructureServiceDTO> services = new HashSet<StructureServiceDTO>();
		List<ServiceDTO> storedServices = ServiceDao.readAllDTO(serviceRepository);
		int size = storedServices.size()-1;
		int quantity = Utility.getRandomInt(5,200);
		for(int i=0; i<quantity; i++){
			ServiceDTO storedService = storedServices.get(Utility.getRandomInt(0,size));
			StructureServiceDTO service = new StructureServiceDTO();
			service.setId(storedService.getId());
			service.setCode(storedService.getCode());
			service.setName(storedService.getName());
			service.setRate(Utility.roundFloat(Utility.getRandomFloat(10f,100f)));
			service.setActive(true);
			services.add(service);
		}

		return services;
	}

	public static void populateStructure(StructureRepository structureRepository,
                                         ServiceRepository serviceRepository){
		read();
		List<StructureDTO> structuresToSave = new ArrayList<StructureDTO>();
		for(Map.Entry<String, StructureDTO> set:structures.entrySet()) {
			set.getValue().setServices(randomServices(serviceRepository));
			structuresToSave.add(set.getValue());
		}
		StructureDao.createMany(structuresToSave,structureRepository);
	}
}
