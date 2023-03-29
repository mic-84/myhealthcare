package it.unipi.lsmsd.myhealthcare.dao;

import it.unipi.lsmsd.myhealthcare.model.Service;
import it.unipi.lsmsd.myhealthcare.dto.ServiceDTO;
import it.unipi.lsmsd.myhealthcare.dto.StructureServiceDTO;
import it.unipi.lsmsd.myhealthcare.model.StructureService;
import it.unipi.lsmsd.myhealthcare.repository.ServiceRepository;

import java.util.ArrayList;
import java.util.List;

public class ServiceDao {
    public static Service fromDTO(ServiceDTO serviceDTO) {
        Service service = new Service();
        service.setCode(serviceDTO.getCode());
        service.setName(serviceDTO.getName());
        service.setId(serviceDTO.getId());
        return service;
    }

    public static StructureService fromDTOtoStructureService(StructureServiceDTO serviceDTO) {
        StructureService service = new StructureService();
        service.setCode(serviceDTO.getCode());
        service.setName(serviceDTO.getName());
        service.setId(serviceDTO.getId());
        service.setRate(serviceDTO.getRate());
        service.setActive(serviceDTO.isActive());
        return service;
    }

    public static StructureServiceDTO toStructureDTO(StructureService service){
        StructureServiceDTO structureServiceDTO = new StructureServiceDTO();
        structureServiceDTO.setId(service.getId());
        structureServiceDTO.setCode(service.getCode());
        structureServiceDTO.setName(service.getName());
        structureServiceDTO.setRate(service.getRate());
        structureServiceDTO.setActive(service.isActive());
        return structureServiceDTO;
    }

    public static ServiceDTO toDTO(Service service){
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setId(service.getId());
        serviceDTO.setCode(service.getCode());
        serviceDTO.setName(service.getName());
        return serviceDTO;
    }

    public static void create(ServiceDTO object, ServiceRepository serviceRepository){
        serviceRepository.save(object);
    }

    public static void create(Service object, ServiceRepository serviceRepository){
        serviceRepository.save(toDTO(object));
    }

    public static ServiceDTO readById(String id, ServiceRepository serviceRepository){
        return serviceRepository.findById(id).get();
    }

    public static ServiceDTO readByName(String name, ServiceRepository serviceRepository){
        return serviceRepository.findByName(name.toUpperCase().trim());
    }

    public static ServiceDTO readByCode(String code, ServiceRepository serviceRepository){
        return serviceRepository.findByCode(code.toUpperCase().trim());
    }

    public static List<ServiceDTO> readAllDTO(ServiceRepository serviceRepository){
        return serviceRepository.findAll();
    }

    public static List<Service> readAll(ServiceRepository serviceRepository){
        List<Service> objects = new ArrayList<Service>();
        for(ServiceDTO object: readAllDTO(serviceRepository))
            objects.add(fromDTO(object));
        return objects;
    }

    public static void update(ServiceDTO object, ServiceRepository serviceRepository){
        serviceRepository.save(object);
    }

    public static void update(Service object, ServiceRepository serviceRepository){
        serviceRepository.save(toDTO(object));
    }

    public static void delete(ServiceDTO object, ServiceRepository serviceRepository){
        serviceRepository.delete(object);
    }

    public static void delete(Service object, ServiceRepository serviceRepository){
        serviceRepository.delete(toDTO(object));
    }

    public static Long size(ServiceRepository serviceRepository){
        return serviceRepository.count();
    }
}
