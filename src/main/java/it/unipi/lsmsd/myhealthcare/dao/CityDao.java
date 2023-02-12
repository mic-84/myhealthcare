package it.unipi.lsmsd.myhealthcare.dao;

import it.unipi.lsmsd.myhealthcare.model.City;
import it.unipi.lsmsd.myhealthcare.mongo.dto.CityDTO;
import it.unipi.lsmsd.myhealthcare.mongo.repository.CityRepository;

import java.util.ArrayList;
import java.util.List;

public class CityDao {
    public static City fromMongo(CityDTO cityDTO, List<CityDTO> cities) {
        City city = new City();
        city.setId(cityDTO.getId());
        city.setCode(cityDTO.getCode());
        city.setName(cityDTO.getName());
        city.setProvince(cityDTO.getProvince());
        for(String neighbourId: cityDTO.getNeighbours())
            for(CityDTO mongoNeighbour : cities)
                if(mongoNeighbour.getId().equals(neighbourId)){
                    City neighbour = new City();
                    neighbour.setId(mongoNeighbour.getId());
                    neighbour.setName(mongoNeighbour.getName());
                    neighbour.setProvince(cityDTO.getProvince());
                    city.addNeighbour(neighbour);
                    break;
                }
        return city;
    }

    public static CityDTO toMongo(City city){
        CityDTO cityDTO = new CityDTO();
        cityDTO.setId(city.getId());
        cityDTO.setCode(city.getCode());
        cityDTO.setName(city.getName());
        cityDTO.setProvince(city.getProvince());
        for(City neighbour: city.getNeighbours())
            cityDTO.addNeighbour(neighbour.getId());
        return cityDTO;
    }

    public static void create(CityDTO object, CityRepository cityRepository){
        cityRepository.save(object);
    }

    public static void create(City object, CityRepository cityRepository){
        cityRepository.save(toMongo(object));
    }

    public static CityDTO readById(String id, CityRepository cityRepository){
        return cityRepository.findById(id).get();
    }

    public static CityDTO readByName(String name, CityRepository cityRepository){
        return cityRepository.findByName(name.toUpperCase().trim());
    }

    public static CityDTO readByCode(String code, CityRepository cityRepository){
        return cityRepository.findByCode(code);
    }

    public static List<CityDTO> readAllMongo(CityRepository cityRepository){
        return cityRepository.findAll();
    }

    public static List<City> readAll(CityRepository cityRepository){
        List<City> objects = new ArrayList<City>();
        List<CityDTO> cities = readAllMongo(cityRepository);
        for(CityDTO object:cities)
            objects.add(fromMongo(object,cities));
        return objects;
    }

    public static void update(CityDTO object, CityRepository cityRepository){
        cityRepository.save(object);
    }

    public static void update(City object, CityRepository cityRepository){
        cityRepository.save(toMongo(object));
    }

    public static void delete(CityDTO object, CityRepository cityRepository){
        cityRepository.delete(object);
    }

    public static void delete(City object, CityRepository cityRepository){
        cityRepository.delete(toMongo(object));
    }

    public static Long size(CityRepository cityRepository){
        return cityRepository.count();
    }
}
