package it.unipi.lsmsd.myhealthcare.startingdata;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PugliaReader {
	private static String line = "";
	private static String splitBy = ";";

	private static Map<String, StructureDTO> structures;
	
	public static void read() {
		structures = new HashMap<String, StructureDTO>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					new File(MyHealthCareApplication.properties.pugliaStructures)));
			br.readLine();
			
			while ((line = br.readLine()) != null){
				line = line.replace("\"","");
				String[] lineSplit = line.split(splitBy);
				System.out.println("reading " + lineSplit[3] + ": city " + lineSplit[8]);
				City city = null;
				try {
					city = Utility.getCityByCode(lineSplit[8]);

					// aslCode, structCode, name, address, city, region
					StructureDTO structure = new StructureDTO(lineSplit[0],lineSplit[2],lineSplit[3].toUpperCase(),
							lineSplit[7], CityDao.toDTONoDetails(city),"Puglia","South");
					structures.put(lineSplit[0]+lineSplit[2],structure);
				}
				catch (Exception e){}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println("read Puglia structures from starting data: " + structures.size());
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
