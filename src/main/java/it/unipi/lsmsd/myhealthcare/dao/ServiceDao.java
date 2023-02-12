package it.unipi.lsmsd.myhealthcare.dao;

import it.unipi.lsmsd.myhealthcare.model.Service;
import it.unipi.lsmsd.myhealthcare.mongo.dto.ServiceDTO;
import it.unipi.lsmsd.myhealthcare.mongo.repository.ServiceRepository;

import java.util.ArrayList;
import java.util.List;

public class ServiceDao {
    public static Service fromMongo(ServiceDTO serviceDTO) {
        Service service = new Service();
        service.setId(serviceDTO.getId());
        service.setCode(serviceDTO.getCode());
        service.setName(serviceDTO.getName());
        service.setRate(serviceDTO.getRate());
        service.setActive(serviceDTO.isActive());
        return service;
    }

    public static ServiceDTO toMongo(Service service){
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setId(service.getId());
        serviceDTO.setCode(service.getCode());
        serviceDTO.setName(service.getName());
        serviceDTO.setRate(service.getRate());
        serviceDTO.setActive(service.isActive());
        return serviceDTO;
    }

    public static void create(ServiceDTO object, ServiceRepository serviceRepository){
        serviceRepository.save(object);
    }

    public static void create(Service object, ServiceRepository serviceRepository){
        serviceRepository.save(toMongo(object));
    }

    public static ServiceDTO readById(String id, ServiceRepository serviceRepository){
        return serviceRepository.findById(id).get();
    }

    public static ServiceDTO readByName(String name, ServiceRepository serviceRepository){
        return serviceRepository.findByName(name.toUpperCase().trim());
    }

    public static List<ServiceDTO> readAllMongo(ServiceRepository serviceRepository){
        return serviceRepository.findAll();
    }

    public static List<Service> readAll(ServiceRepository serviceRepository){
        List<Service> objects = new ArrayList<Service>();
        for(ServiceDTO object:readAllMongo(serviceRepository))
            objects.add(fromMongo(object));
        return objects;
    }

    public static List<ServiceDTO> readAllActiveMongo(ServiceRepository serviceRepository){
        return serviceRepository.findAllActive();
    }

    public static List<Service> readAllActive(ServiceRepository serviceRepository){
        List<Service> objects = new ArrayList<Service>();
        for(ServiceDTO object:readAllActiveMongo(serviceRepository))
            objects.add(fromMongo(object));
        return objects;
    }

    public static void update(ServiceDTO object, ServiceRepository serviceRepository){
        serviceRepository.save(object);
    }

    public static void update(Service object, ServiceRepository serviceRepository){
        serviceRepository.save(toMongo(object));
    }

    public static void delete(ServiceDTO object, ServiceRepository serviceRepository){
        serviceRepository.delete(object);
    }

    public static void delete(Service object, ServiceRepository serviceRepository){
        serviceRepository.delete(toMongo(object));
    }

    public static Long size(ServiceRepository serviceRepository){
        return serviceRepository.count();
    }
}
