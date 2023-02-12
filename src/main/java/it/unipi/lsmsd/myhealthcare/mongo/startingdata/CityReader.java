package it.unipi.lsmsd.myhealthcare.mongo.startingdata;

import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.dao.CityDao;
import it.unipi.lsmsd.myhealthcare.mongo.dto.CityDTO;
import it.unipi.lsmsd.myhealthcare.mongo.repository.CityRepository;

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

	public static void read(){
		cities = new ArrayList<CityDTO>();
		readCities();
		readNeighbours();
	}
	public static void readCities() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					new File(MyHealthCareApplication.properties.cityFilename)));
			br.readLine();
			br.readLine();
			br.readLine();
			
			while ((line = br.readLine()) != null){
				String[] lineSplit = line.split(splitBy);
				if(lineSplit[0].equals("03") || lineSplit[0].equals("10")) {
					CityDTO city = new CityDTO(lineSplit[4],lineSplit[6].toUpperCase(),lineSplit[14]);
					cities.add(city);
				}
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

				if(lineSplit[5].equals("3") || lineSplit[5].equals("10")) {
					for(CityDTO city:cities)
						if(city.getCode().contains(lineSplit[1])) {
							CityDTO neighbour = getCityByCode(lineSplit[3]);
							if(neighbour != null)
								city.addNeighbour(neighbour.getCode());
						}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static void populateCity(CityRepository cityRepository) {
		cityRepository.deleteAll();
		read();
		System.out.println("read cities from starting data: " + cities.size());
		for (CityDTO city : cities)
			CityDao.create(city,cityRepository);

		for (CityDTO city:cityRepository.findAll()){
			List<String> neighbours = new ArrayList<String>();
			for(String neighbour:city.getNeighbours())
				neighbours.add(CityDao.readByCode(neighbour,cityRepository).getId());
			city.setNeighbours(neighbours);
			CityDao.update(city,cityRepository);
		}
	}

	public static CityDTO getCityByCode(String code) {
		for (CityDTO city: cities)
			if(city.getCode().contains(code))
				return city;

		return null;
	}

	public static CityDTO getCityByName(String name) {
		for (CityDTO city: cities)
			if(city.getName().replace("'","").toUpperCase().
					equals(name.replace("'","").toUpperCase()))
				return city;

		return null;
	}
}
