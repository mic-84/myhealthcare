package it.unipi.lsmsd.myhealthcare.startingdata;

import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.dao.CityDao;
import it.unipi.lsmsd.myhealthcare.dto.CityDTO;
import it.unipi.lsmsd.myhealthcare.dto.CityNoDetailsDTO;
import it.unipi.lsmsd.myhealthcare.repository.CityRepository;
import it.unipi.lsmsd.myhealthcare.service.Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CityReader {
	private static String line = "";
	private static String splitBy = ";";

	private static List<CityDTO> cities;

	public static void readCities() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					new File(MyHealthCareApplication.properties.cityFilename)));
			br.readLine();
			br.readLine();
			br.readLine();
			
			while ((line = br.readLine()) != null){
				String[] lineSplit = line.split(splitBy);
				if(Utility.regionExists(lineSplit[0]))
					cities.add(new CityDTO(
							lineSplit[4],lineSplit[6].toUpperCase(),lineSplit[14],
							Utility.getRegion(Integer.valueOf(lineSplit[0]))));
			}			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void readNeighbours() {	
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					new File(MyHealthCareApplication.properties.cityNeigboursFilename)));
			br.readLine();
			
			while ((line = br.readLine()) != null){
				String[] lineSplit = line.split(splitBy);

				if(Utility.regionExists(lineSplit[5]))
					for(int i=0; i<cities.size(); i++)
						if(cities.get(i).getCode().contains(lineSplit[1])) {
							CityNoDetailsDTO neighbour = getCityByCode(lineSplit[3]);
							if(neighbour != null)
								cities.get(i).addNeighbour(neighbour);
						}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static void populateCity(CityRepository cityRepository) {
		CityDao.deleteMany(cityRepository);

		cities = new ArrayList<CityDTO>();
		readCities();
		System.out.println("read cities from starting data: " + cities.size());
		CityDao.createMany(cities, cityRepository);

		cities = CityDao.readAllDTO(cityRepository);
		readNeighbours();
		for(CityDTO city : cities)
			if(city.getNeighbours().size() > 0)
				CityDao.update(city, cityRepository);
	}

	public static CityNoDetailsDTO getCityByCode(String code) {
		for (CityDTO city: cities)
			if(city.getCode().contains(code))
				return new CityNoDetailsDTO(
						city.getId(), city.getCode(), city.getName(), city.getProvince(), city.getRegion());

		return null;
	}
}
