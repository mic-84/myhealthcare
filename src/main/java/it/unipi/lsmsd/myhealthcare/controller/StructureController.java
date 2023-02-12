package it.unipi.lsmsd.myhealthcare.controller;

import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.dao.CityDao;
import it.unipi.lsmsd.myhealthcare.dao.ReviewDao;
import it.unipi.lsmsd.myhealthcare.dao.ServiceDao;
import it.unipi.lsmsd.myhealthcare.dao.StructureDao;
import it.unipi.lsmsd.myhealthcare.model.Service;
import it.unipi.lsmsd.myhealthcare.model.Structure;
import it.unipi.lsmsd.myhealthcare.model.User;
import it.unipi.lsmsd.myhealthcare.mongo.dto.CityDTO;
import it.unipi.lsmsd.myhealthcare.mongo.repository.CityRepository;
import it.unipi.lsmsd.myhealthcare.mongo.repository.ReviewRepository;
import it.unipi.lsmsd.myhealthcare.mongo.repository.ServiceRepository;
import it.unipi.lsmsd.myhealthcare.mongo.repository.StructureRepository;
import it.unipi.lsmsd.myhealthcare.userSession.UserSession;
import it.unipi.lsmsd.myhealthcare.utility.Utility;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class StructureController {
    private final CityRepository CITY_REPOSITORY;
    private final StructureRepository STRUCTURE_REPOSITORY;
    private final ServiceRepository SERVICE_REPOSITORY;
    private final ReviewRepository REVIEW_REPOSITORY;

    public StructureController(CityRepository cityRepository,
                               StructureRepository structureRepository,
                               ServiceRepository serviceRepository,
                               ReviewRepository reviewRepository){
        this.CITY_REPOSITORY = cityRepository;
        this.STRUCTURE_REPOSITORY = structureRepository;
        this.SERVICE_REPOSITORY = serviceRepository;
        this.REVIEW_REPOSITORY = reviewRepository;
    }

    @PostMapping("/toSearchStructures")
    public String toSearchStructures(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            ModelMap model){
        System.out.println("StructureController.toSearchStructures");
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        model.put("city_list", MyHealthCareApplication.cities);
        model.put("service_list", MyHealthCareApplication.activeServices);
        model.addAttribute("user", user);
        return "search_structures";
    }

    @PostMapping("/searchStructures")
    public String searchStructures(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            @RequestParam(value = "city", required = false) String cityName,
            @RequestParam(value = "neighbours", required = false) boolean neighbours,
            @RequestParam(value = "service", required = false) String serviceName,
            ModelMap model) {
        System.out.println("StructureController.searchStructures: " + cityName + ", " + neighbours + ", " + serviceName);
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        List<Structure> structures = new ArrayList<Structure>();
        String title = "List of the structures";
        if(cityName.length() > 0 || serviceName.length() > 0){
            if(cityName.length() > 0) {
                CityDTO city = CityDao.readByName(cityName.replace("\u00a0"," "), CITY_REPOSITORY);
                if(city != null) {
                    title += " in " + city.getName();
                    System.out.println("START search structures by city " + Utility.getNow());
                    structures.addAll(StructureDao.readByCity(city.getId(), STRUCTURE_REPOSITORY,
                            CITY_REPOSITORY, SERVICE_REPOSITORY));
                    System.out.println("END search structures by city " + Utility.getNow());
                    if (neighbours) {
                        title += " and its neighbours";
                        for (String neighbourId : city.getNeighbours()) {
                            CityDTO neighbourCity = CityDao.readById(neighbourId, CITY_REPOSITORY);
                            List<Structure> neighbourStructures = StructureDao.readByCity(
                                    neighbourCity.getId(), STRUCTURE_REPOSITORY,
                                    CITY_REPOSITORY, SERVICE_REPOSITORY);
                            structures.addAll(neighbourStructures);
                        }
                    }
                    if (serviceName.length() > 0) {
                        title += "<br/>(service " + serviceName + ")";
                        List<Structure> structuresService = new ArrayList<Structure>();
                        for(Structure structure : structures) {
                            boolean found = false;
                            for (Service service : structure.getActiveServices())
                                if (service.getName().contains(serviceName.toUpperCase().trim()
                                        .replace("\u00a0"," "))) {
                                    found = true;
                                    break;
                                }
                            if(found)
                                structuresService.add(structure);
                        }
                        model.put("structure_list", structuresService);
                    } else {
                        model.put("structure_list", structures);
                    }
                }
            }
        }
        model.addAttribute("title", title);
        model.addAttribute("city", cityName);
        model.addAttribute("neighbours", neighbours);
        model.addAttribute("service", serviceName);
        model.addAttribute("user", user);
        return "structures";
    }

    @PostMapping("/getStructure")
    public String getStructure(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "city", required = false) String cityName,
            @RequestParam(value = "neighbours", required = false) boolean neighbours,
            @RequestParam(value = "service", required = false) String serviceName,
            ModelMap model) {
        System.out.println("StructureController.getStructure: " + id + ", " + cityName + ", " + neighbours + ", " + serviceName);
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        Structure structure = StructureDao.fromMongo(
                StructureDao.readById(id, STRUCTURE_REPOSITORY), CITY_REPOSITORY, SERVICE_REPOSITORY);
        model.addAttribute("structure", structure);
        System.out.println("START group structure reviews per rating " + Utility.getNow());
        model.addAttribute("average",
                ReviewDao.getStructureReviewsRating(REVIEW_REPOSITORY,structure.getId()));
        System.out.println("END group structure reviews per rating " + Utility.getNow());
        System.out.println("START search structure reviews " + Utility.getNow());
        model.addAttribute("reviews",ReviewDao.getReviewsByStructure(
                REVIEW_REPOSITORY,structure.getId()));
        System.out.println("END search structure reviews " + Utility.getNow());
        model.addAttribute("city", cityName);
        model.addAttribute("neighbours", neighbours);
        model.addAttribute("service", serviceName);
        model.addAttribute("user", user);
        boolean isEmployee = user.isEmployee(id);
        model.addAttribute("isEmployee", isEmployee);
        if (isEmployee)
            model.put("years", Utility.yearsList());
        return "structure";
    }

    @PostMapping("/toServicesManagement")
    public String toServicesManagement(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            @RequestParam("structureId") String structureId,
            ModelMap model){
        System.out.println("StructureController.toServicesManagement " + structureId);
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        Structure structure = StructureDao.fromMongo(
                StructureDao.readById(structureId, STRUCTURE_REPOSITORY),
                CITY_REPOSITORY, SERVICE_REPOSITORY
        );
        model.addAttribute("structure", structure);
        model.addAttribute("user", user);
        model.addAttribute("isEmployee", user.isEmployee(structureId));
        return "structure_services_management";
    }

    @PostMapping("/toStructureService")
    public String toStructureService(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            @RequestParam("id") String serviceId,
            @RequestParam("structureId") String structureId,
            ModelMap model){
        System.out.println("StructureController.toStructureService " + serviceId + ", " + structureId);
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        Structure structure = StructureDao.fromMongo(
                StructureDao.readById(structureId, STRUCTURE_REPOSITORY),
                CITY_REPOSITORY, SERVICE_REPOSITORY
        );
        for(Service service : structure.getServices())
            if(service.getId().equals(serviceId)){
                model.addAttribute("service", service);
                break;
            }
        model.addAttribute("structureId", structure.getId());
        model.addAttribute("user", user);
        model.addAttribute("isEmployee", user.isEmployee(structureId));
        return "structure_service";
    }

    @PostMapping("/updateStructureService")
    public String updateStructureService(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            @RequestParam("id") String serviceId,
            @RequestParam("structureId") String structureId,
            @RequestParam("rate") String rate,
            @RequestParam("active") String active,
            ModelMap model){
        System.out.println("StructureController.updateStructureService " + serviceId + ", " + structureId);
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        Structure structure = StructureDao.fromMongo(
                StructureDao.readById(structureId, STRUCTURE_REPOSITORY),
                CITY_REPOSITORY, SERVICE_REPOSITORY
        );
        try {
            for (Service service : structure.getServices())
                if (service.getId().equals(serviceId)) {
                    service.setRate(Float.valueOf(rate));
                    service.setActive(Boolean.valueOf(active));
                    model.addAttribute("service", service);
                    break;
                }
            StructureDao.update(structure, STRUCTURE_REPOSITORY);
            model.addAttribute("message", "data successfully updated");
        } catch (Exception e){
            model.addAttribute("message", "an error occurred while updating data");
        }
        model.addAttribute("structureId", structure.getId());
        model.addAttribute("user", user);
        model.addAttribute("isEmployee", user.isEmployee(structureId));
        return "structure_service";
    }

    @PostMapping("/toAddStructureService")
    public String toAddStructureService(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            @RequestParam("structureId") String structureId,
            ModelMap model){
        System.out.println("StructureController.toAddStructureService " + structureId);
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        Structure structure = StructureDao.fromMongo(
                StructureDao.readById(structureId, STRUCTURE_REPOSITORY),
                CITY_REPOSITORY, SERVICE_REPOSITORY
        );
        List<Service> allServices = ServiceDao.readAll(SERVICE_REPOSITORY);
        List<Service> services = new ArrayList<Service>();
        for(Service service : allServices)
            if (service.isActive()) {
                boolean found = false;
                for (Service structureService : structure.getServices())
                    if (structureService.getId().equals(service.getId())) {
                        found = true;
                        break;
                    }
                if (!found)
                    services.add(service);
            }
        services = Utility.sortServices(services);
        model.put("services", services);
        model.addAttribute("structureId", structure.getId());
        model.addAttribute("user", user);
        model.addAttribute("isEmployee", user.isEmployee(structureId));
        return "add_structure_service";
    }

    @PostMapping("/addStructureService")
    public String addStructureService(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            @RequestParam("id") String serviceId,
            @RequestParam("structureId") String structureId,
            @RequestParam("rate") String rate,
            ModelMap model){
        System.out.println("StructureController.addStructureService " + serviceId + ", " + structureId);
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        Structure structure = StructureDao.fromMongo(
                StructureDao.readById(structureId, STRUCTURE_REPOSITORY),
                CITY_REPOSITORY, SERVICE_REPOSITORY
        );
        try {
            Service service = ServiceDao.fromMongo(ServiceDao.readById(serviceId, SERVICE_REPOSITORY));
            service.setRate(Float.valueOf(rate));
            service.setActive(true);
            structure.addService(service);
            StructureDao.update(structure, STRUCTURE_REPOSITORY);
            model.addAttribute("message", "data successfully updated");
        } catch (Exception e){
            model.addAttribute("message", "an error occurred while updating data");
        }
        model.addAttribute("structure", structure);
        model.addAttribute("structureId", structure.getId());
        model.addAttribute("user", user);
        model.addAttribute("isEmployee", user.isEmployee(structureId));
        return "structure_services_management";
    }
}
