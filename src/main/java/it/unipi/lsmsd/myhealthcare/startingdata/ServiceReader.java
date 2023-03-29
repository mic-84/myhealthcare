package it.unipi.lsmsd.myhealthcare.startingdata;

import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.dao.ServiceDao;
import it.unipi.lsmsd.myhealthcare.dto.ServiceDTO;
import it.unipi.lsmsd.myhealthcare.repository.ServiceRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServiceReader {
	private static String line = "";
	private static String splitBy = ";";

	private static List<ServiceDTO> services;
	
	public static void read() {
		services = new ArrayList<ServiceDTO>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					new File(MyHealthCareApplication.properties.ministerialServices)));
			br.readLine();
			
			while ((line = br.readLine()) != null){
				line = line.replace("\"","");
				String[] lineSplit = line.split(splitBy);
				ServiceDTO service = new ServiceDTO(lineSplit[1],lineSplit[2]);
				services.add(service);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static void populateService(ServiceRepository serviceRepository) {
		serviceRepository.deleteAll();
		read();
		System.out.println("read services from starting data: " + services.size());
		for (ServiceDTO service : services)
			ServiceDao.create(service,serviceRepository);
	}

	public static ServiceDTO getServiceByCode(List<ServiceDTO> services, String code) {
		if(code != null)
			for (ServiceDTO service: services)
				if(service.getCode().replace(".","").contains(code.replace(".","")))
					return service;

		return null;
	};
}
