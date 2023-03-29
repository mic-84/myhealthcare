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

public class LombardiaReader {
	private static String line = "";
	private static String splitBy = ";";

	private static Map<String, StructureDTO> structures;

	public static void read(ServiceRepository serviceRepository){
		structures = new HashMap<String, StructureDTO>();
		readStructures();
		System.out.println("read Lombardia structures from starting data: " + structures.size());

		readServices(serviceRepository);
		int count = 0;
		for(Map.Entry<String, StructureDTO> set: structures.entrySet())
			if(set.getValue().getServices().size() > 0)
				count++;
		System.out.println("read services of " + count + " structures from starting data");
	}
	
	public static void readStructures() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					new File(MyHealthCareApplication.properties.lombardiaStructures)));
			br.readLine();

			while ((line = br.readLine()) != null){
				line = line.replace("\"","");
				String[] lineSplit = line.split(splitBy);
				City city = null;
				try {
					city = Utility.getCityByName(lineSplit[9]);
				}
				catch (Exception e){}
				StructureDTO structure = new StructureDTO(lineSplit[1],lineSplit[5],lineSplit[6],lineSplit[8],
						CityDao.toDTONoDetails(city),"Lombardia","North");
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
			List<ServiceDTO> services = ServiceDao.readAllDTO(serviceRepository);
			int count = 1;
			while ((line = br.readLine()) != null){
				count++;
				line = line.replace("\"","");
				String[] lineSplit = line.split(splitBy);
				for(Map.Entry<String, StructureDTO> set: structures.entrySet())
					if(set.getKey().contains(lineSplit[1]+lineSplit[5])) {
						ServiceDTO storedService = ServiceReader.getServiceByCode(services,lineSplit[17]);
						if(storedService != null) {
							StructureServiceDTO service = new StructureServiceDTO();
							service.setId(storedService.getId());
							service.setCode(storedService.getCode());
							service.setName(storedService.getName());
							service.setActive(true);
							try {
								service.setRate(Float.parseFloat(lineSplit[20]));
							}
							catch(NumberFormatException e) {}
							if(service.getRate() == null)
								service.setRate(10f);
							service.setRate(Utility.roundFloat(service.getRate()));
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
                                         ServiceRepository serviceRepository){
		structureRepository.deleteAll();
		read(serviceRepository);
		List<StructureDTO> structuresToSave = new ArrayList<StructureDTO>();
		for(Map.Entry<String, StructureDTO> set: structures.entrySet())
			if(set.getValue().getServices().size() > 0)
				structuresToSave.add(set.getValue());
		StructureDao.createMany(structuresToSave,structureRepository);
	}
}
