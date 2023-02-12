package it.unipi.lsmsd.myhealthcare.dao;

import it.unipi.lsmsd.myhealthcare.model.Service;
import it.unipi.lsmsd.myhealthcare.model.Structure;
import it.unipi.lsmsd.myhealthcare.mongo.dto.ServiceDTO;
import it.unipi.lsmsd.myhealthcare.mongo.dto.StructureDTO;
import it.unipi.lsmsd.myhealthcare.mongo.repository.*;
import it.unipi.lsmsd.myhealthcare.utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class StructureDao {
    public static Structure fromMongo(StructureDTO structureDTO,
                                      CityRepository cityRepository,
                                      ServiceRepository serviceRepository) {
        Structure structure = new Structure();
        structure.setId(structureDTO.getId());
        structure.setAslCode(structureDTO.getAslCode());
        structure.setStructureCode(structureDTO.getStructureCode());
        structure.setName(structureDTO.getName());
        structure.setAddress(structureDTO.getAddress());
        /*structure.setCity(CityDao.fromMongo(
                CityDao.readById(structureDTO.getCity(),cityRepository),cityRepository));
        */
        structure.setCity(Utility.getCityById(structureDTO.getCity()));
        structure.setRegion(structureDTO.getRegion());
        for(ServiceDTO serviceDTO : structureDTO.getServices()){
            Service service = ServiceDao.fromMongo(serviceDTO);
            service.setName(ServiceDao.readById(service.getId(),serviceRepository).getName());
            structure.addService(service);
        }
        structure.setServices(Utility.sortServices(structure.getServices()));
        return structure;
    }

    public static Structure fromMongoNoServices(
            StructureDTO structureDTO, CityRepository cityRepository) {
        Structure structure = new Structure();
        structure.setId(structureDTO.getId());
        structure.setAslCode(structureDTO.getAslCode());
        structure.setStructureCode(structureDTO.getStructureCode());
        structure.setName(structureDTO.getName());
        structure.setAddress(structureDTO.getAddress());
        /*structure.setCity(CityDao.fromMongo(
                CityDao.readById(structureDTO.getCity(),cityRepository),cityRepository));
         */
        structure.setCity(Utility.getCityById(structureDTO.getCity()));
        structure.setRegion(structureDTO.getRegion());

        return structure;
    }

    public static StructureDTO toMongo(Structure structure){
        StructureDTO structureDTO = new StructureDTO();
        structureDTO.setId(structure.getId());
        structureDTO.setId(structure.getId());
        structureDTO.setAslCode(structure.getAslCode());
        structureDTO.setStructureCode(structure.getStructureCode());
        structureDTO.setName(structure.getName());
        structureDTO.setAddress(structure.getAddress());
        structureDTO.setCity(structure.getCity().getId());
        structureDTO.setRegion(structure.getRegion());
        for(Service service:structure.getServices()) {
            ServiceDTO serviceDTO = new ServiceDTO(service.getCode(), null, service.getRate());
            serviceDTO.setId(service.getId());
            serviceDTO.setActive(service.isActive());
            structureDTO.addService(serviceDTO);
        }
        return structureDTO;
    }

    public static void create(StructureDTO object, StructureRepository structureRepository){
        structureRepository.save(object);
    }

    public static void createMany(List<StructureDTO> objects, StructureRepository structureRepository){
        structureRepository.saveAll(objects);
    }

    public static void create(Structure object, StructureRepository structureRepository){
        structureRepository.save(toMongo(object));
    }

    public static StructureDTO readById(String id, StructureRepository structureRepository){
        return structureRepository.findById(id).get();
    }

    public static StructureDTO readByStructureCode(String code, StructureRepository structureRepository){
        return structureRepository.findByStructureCode(code);
    }

    public static List<Structure> readByCity(
            String cityId, StructureRepository structureRepository,
            CityRepository cityRepository, ServiceRepository serviceRepository){
        List<StructureDTO> structuresMongo = structureRepository.findByCity(cityId);
        List<Structure> structures = new ArrayList<Structure>();
        for(StructureDTO structureDTO : structuresMongo)
            structures.add(fromMongo(structureDTO, cityRepository, serviceRepository));
        return structures;
    }

    public static List<StructureDTO> readAllMongo(StructureRepository structureRepository){
        return structureRepository.findAll();
    }

    public static List<Structure> readAll(StructureRepository structureRepository,
                                          CityRepository cityRepository,
                                          ServiceRepository serviceRepository){
        List<Structure> objects = new ArrayList<Structure>();
        for(StructureDTO object:readAllMongo(structureRepository))
            objects.add(fromMongo(object,cityRepository,serviceRepository));
        return objects;
    }

    public static void update(StructureDTO object, StructureRepository structureRepository){
        structureRepository.save(object);
    }

    public static void update(Structure object, StructureRepository structureRepository){
        structureRepository.save(toMongo(object));
    }

    public static void delete(StructureDTO object, StructureRepository structureRepository){
        structureRepository.delete(object);
    }

    public static void delete(Structure object, StructureRepository structureRepository){
        structureRepository.delete(toMongo(object));
    }

    public static Long size(StructureRepository structureRepository){
        return structureRepository.count();
    }
}
