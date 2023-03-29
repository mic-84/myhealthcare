package it.unipi.lsmsd.myhealthcare.dao;

import it.unipi.lsmsd.myhealthcare.model.City;
import it.unipi.lsmsd.myhealthcare.dto.AbstractCityDTO;
import it.unipi.lsmsd.myhealthcare.dto.CityDTO;
import it.unipi.lsmsd.myhealthcare.dto.CityNoDetailsDTO;
import it.unipi.lsmsd.myhealthcare.repository.CityRepository;

import java.util.ArrayList;
import java.util.List;

public class CityDao {
    public static City fromDTO(AbstractCityDTO cityDTO) {
        City city = new City();
        city.setCode(cityDTO.getCode());
        city.setName(cityDTO.getName());
        city.setProvince(cityDTO.getProvince());
        city.setRegion(cityDTO.getRegion());

        if(cityDTO instanceof CityDTO) {
            city.setId(((CityDTO)cityDTO).getId());
            for (CityNoDetailsDTO neighbourDTO : ((CityDTO)cityDTO).getNeighbours()) {
                City neighbour = new City();
                neighbour.setId(neighbourDTO.getId());
                neighbour.setName(neighbourDTO.getName());
                neighbour.setProvince(neighbourDTO.getProvince());
                city.addNeighbour(neighbour);
            }
        } else if(cityDTO instanceof CityNoDetailsDTO)
            city.setId(((CityNoDetailsDTO)cityDTO).getId());
        return city;
    }

    public static CityDTO toDTO(City city){
        CityDTO cityDTO = new CityDTO();
        cityDTO.setId(city.getId());
        cityDTO.setCode(city.getCode());
        cityDTO.setName(city.getName());
        cityDTO.setProvince(city.getProvince());
        cityDTO.setRegion(city.getRegion());
        for(City neighbour: city.getNeighbours())
            cityDTO.addNeighbour(new CityNoDetailsDTO(neighbour.getId(), neighbour.getCode(),
                    neighbour.getName(), neighbour.getProvince(), neighbour.getRegion()));
        return cityDTO;
    }

    public static CityNoDetailsDTO toDTONoDetails(City city){
        CityNoDetailsDTO cityDTO = new CityNoDetailsDTO();
        cityDTO.setId(city.getId());
        cityDTO.setCode(city.getCode());
        cityDTO.setName(city.getName());
        cityDTO.setProvince(city.getProvince());
        cityDTO.setRegion(city.getRegion());
        return cityDTO;
    }

    public static void create(CityDTO object, CityRepository cityRepository){
        cityRepository.save(object);
    }

    public static void createMany(List<CityDTO> objects, CityRepository cityRepository){
        cityRepository.saveAll(objects);
    }

    public static void create(City object, CityRepository cityRepository){
        cityRepository.save(toDTO(object));
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

    public static List<CityDTO> readAllDTO(CityRepository cityRepository){
        return cityRepository.findAll();
    }

    public static List<City> readAll(CityRepository cityRepository){
        List<City> objects = new ArrayList<City>();
        List<CityDTO> cities = readAllDTO(cityRepository);
        for(CityDTO object:cities)
            objects.add(fromDTO(object));
        return objects;
    }

    public static void update(CityDTO object, CityRepository cityRepository){
        cityRepository.save(object);
    }

    public static void update(City object, CityRepository cityRepository){
        cityRepository.save(toDTO(object));
    }

    public static void delete(CityDTO object, CityRepository cityRepository){
        cityRepository.delete(object);
    }

    public static void deleteMany(CityRepository cityRepository){
        cityRepository.deleteAll();
    }

    public static void delete(City object, CityRepository cityRepository){
        cityRepository.delete(toDTO(object));
    }

    public static Long size(CityRepository cityRepository){
        return cityRepository.count();
    }
}
