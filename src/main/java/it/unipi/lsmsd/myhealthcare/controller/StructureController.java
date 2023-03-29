package it.unipi.lsmsd.myhealthcare.controller;

import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.dao.*;
import it.unipi.lsmsd.myhealthcare.model.*;
import it.unipi.lsmsd.myhealthcare.repository.*;
import it.unipi.lsmsd.myhealthcare.service.StructureUtility;
import it.unipi.lsmsd.myhealthcare.session.StructureSession;
import it.unipi.lsmsd.myhealthcare.session.UserSession;
import it.unipi.lsmsd.myhealthcare.service.Utility;
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

    public StructureController(CityRepository cityRepository,
                               StructureRepository structureRepository,
                               ServiceRepository serviceRepository){
        this.CITY_REPOSITORY = cityRepository;
        this.STRUCTURE_REPOSITORY = structureRepository;
        this.SERVICE_REPOSITORY = serviceRepository;
    }

    @PostMapping("/toSearchStructures")
    public String toSearchStructures(
            @RequestParam("userId") String userId,
            ModelMap model){
        System.out.println("StructureController.toSearchStructures");
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        model.put("city_list", MyHealthCareApplication.cities);
        model.put("service_list", MyHealthCareApplication.services);
        model.addAttribute("user", user);
        return "search_structures";
    }

    @PostMapping("/searchStructures")
    public String searchStructures(
            @RequestParam("userId") String userId,
            @RequestParam(value = "city", required = false) String cityName,
            @RequestParam(value = "neighbours", required = false) boolean neighbours,
            @RequestParam(value = "service", required = false) String serviceName,
            ModelMap model) {
        System.out.println("StructureController.searchStructures: " + cityName + ", " + neighbours + ", " + serviceName);
        cityName = cityName.trim().toUpperCase();
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        List<Structure> structures = new ArrayList<Structure>();
        String title = "List of the structures";
        if(cityName.length() > 0 || serviceName.length() > 0){
            if(cityName.length() > 0) {
                City city = CityDao.fromDTO(
                        CityDao.readByName(cityName.replace("\u00a0"," "), CITY_REPOSITORY));
                System.out.println("city id " + city.getId());
                if(city != null) {
                    title += " in " + city.getName();
                    System.out.println("START search structures by city " + Utility.getNow());
                    structures.addAll(StructureDao.readByCityId(city.getId(), STRUCTURE_REPOSITORY));
                    System.out.println("END search structures by city " + Utility.getNow());
                    if (neighbours) {
                        title += " and its neighbours";
                        for (City neighbourCity : city.getNeighbours()) {
                            List<Structure> neighbourStructures = StructureDao.readByCityId(
                                    neighbourCity.getId(), STRUCTURE_REPOSITORY);
                            System.out.println("neighbour " + neighbourCity.getName() + ": "
                                    + neighbourStructures.size());
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
                        model.put("quantity", structuresService.size());
                    } else {
                        model.put("structure_list", structures);
                        model.put("quantity", structures.size());
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
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "city", required = false) String cityName,
            @RequestParam(value = "neighbours", required = false) boolean neighbours,
            @RequestParam(value = "service", required = false) String serviceName,
            @RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "fromSearch", required = false) String fromSearch,
            ModelMap model) {
        System.out.println("StructureController.getStructure: " + id + ", " + cityName + ", " + neighbours + ", " + serviceName);
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        Structure structure = StructureSession.getStructure(id, STRUCTURE_REPOSITORY);
        model.addAttribute("structure", structure);
        List<StructureService> services = null;
        if(filter != null){
            services = new ArrayList<StructureService>();
            for(StructureService service : structure.getServices())
                if(service.isActive() &&
                        (service.getName().contains(filter.trim().toUpperCase()) ||
                            service.getCode().contains(filter.trim().toUpperCase())))
                    services.add(service);
            model.addAttribute("filter", filter);
        } else {
            services = structure.getActiveServices();
            model.addAttribute("filter", serviceName);
        }
        model.addAttribute("services", services);
        model.addAttribute("city", cityName);
        model.addAttribute("neighbours", neighbours);
        model.addAttribute("service", serviceName);
        model.addAttribute("user", user);
        boolean isEmployee = user.isEmployee(id);
        model.addAttribute("isEmployee", isEmployee);
        model.addAttribute("fromSearch", fromSearch);
        if (isEmployee)
            model.put("years", Utility.yearsList());
        return "structure";
    }

    @PostMapping("/toServicesManagement")
    public String toServicesManagement(
            @RequestParam("userId") String userId,
            @RequestParam("structureId") String structureId,
            @RequestParam(value = "filter", required = false) String filter,
            ModelMap model){
        System.out.println("StructureController.toServicesManagement " + structureId +
                " " + filter);
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        Structure structure = StructureSession.getStructure(structureId, STRUCTURE_REPOSITORY);
        List<StructureService> services = null;
        if(filter != null){
            services = new ArrayList<StructureService>();
            for(StructureService service : structure.getServices())
                if(service.getName().contains(filter.trim().toUpperCase()) ||
                        service.getCode().contains(filter.trim().toUpperCase()))
                    services.add(service);
            model.addAttribute("filter", filter);
        } else
            services = structure.getServices();
        model.addAttribute("structure", structure);
        model.addAttribute("service_list", services);
        model.addAttribute("user", user);
        model.addAttribute("isEmployee", user.isEmployee(structureId));
        return "structure_services_management";
    }

    @PostMapping("/toStructureService")
    public String toStructureService(
            @RequestParam("userId") String userId,
            @RequestParam("id") String serviceId,
            @RequestParam("structureId") String structureId,
            @RequestParam(value = "filter", required = false) String filter,
            ModelMap model){
        System.out.println("StructureController.toStructureService " + serviceId + ", " + structureId);
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        Structure structure = StructureSession.getStructure(structureId, STRUCTURE_REPOSITORY);
        for(Service service : structure.getServices())
            if(service.getId().equals(serviceId)){
                model.addAttribute("service", service);
                break;
            }
        if(filter != null)
            model.addAttribute("filter", filter);
        model.addAttribute("structureId", structure.getId());
        model.addAttribute("user", user);
        model.addAttribute("isEmployee", user.isEmployee(structureId));
        return "structure_service";
    }

    @PostMapping("/updateStructureService")
    public String updateStructureService(
            @RequestParam("userId") String userId,
            @RequestParam("id") String serviceId,
            @RequestParam("structureId") String structureId,
            @RequestParam("rate") String rate,
            @RequestParam("active") String active,
            @RequestParam(value = "filter", required = false) String filter,
            ModelMap model){
        System.out.println("StructureController.updateStructureService " + serviceId + ", " + structureId);
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        Structure structure = StructureSession.getStructure(structureId, STRUCTURE_REPOSITORY);
        try {
            for (StructureService service : structure.getServices())
                if (service.getId().equals(serviceId)) {
                    service.setRate(Float.valueOf(rate));
                    service.setActive(Boolean.valueOf(active));
                    model.addAttribute("service", service);
                    StructureDao.updateServiceActivation(service);
                    StructureDao.updateServiceRate(service);
                    StructureSession.refreshStructure(structure);
                    break;
                }
            model.addAttribute("message", "data successfully updated");

            List<StructureService> services = null;
            if(filter != null){
                services = new ArrayList<StructureService>();
                for(StructureService service : structure.getServices())
                    if(service.getName().contains(filter.trim().toUpperCase()) ||
                            service.getCode().contains(filter.trim().toUpperCase()))
                        services.add(service);
                model.addAttribute("filter", filter);
            } else
                services = structure.getServices();
            model.addAttribute("service_list", services);
        } catch (Exception e){
            model.addAttribute("message", "an error occurred while updating data");
        }
        if(filter != null)
            model.addAttribute("filter", filter);
        model.addAttribute("structureId", structure.getId());
        model.addAttribute("userId", user.getId());
        model.addAttribute("user", user);
        model.addAttribute("structure", structure);
        model.addAttribute("isEmployee", user.isEmployee(structureId));
        return "structure_services_management";
    }

    @PostMapping("/toAddStructureService")
    public String toAddStructureService(
            @RequestParam("userId") String userId,
            @RequestParam("structureId") String structureId,
            ModelMap model){
        System.out.println("StructureController.toAddStructureService " + structureId);
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        Structure structure = StructureSession.getStructure(structureId, STRUCTURE_REPOSITORY);
        MyHealthCareApplication.services = ServiceDao.readAll(SERVICE_REPOSITORY);
        List<Service> availableServices = Utility.sortServices(
                StructureUtility.getAvailableServices(structure));
        System.out.println("available services: " + availableServices.size());
        model.put("service_list", availableServices);
        model.addAttribute("structureId", structure.getId());
        model.addAttribute("user", user);
        model.addAttribute("isEmployee", user.isEmployee(structureId));
        return "add_structure_service";
    }

    @PostMapping("/addStructureService")
    public String addStructureService(
            @RequestParam("userId") String userId,
            @RequestParam("service") String serviceString,
            @RequestParam("structureId") String structureId,
            @RequestParam("rate") String rate,
            ModelMap model){
        System.out.println("StructureController.addStructureService " + serviceString + ", " + structureId);
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        Structure structure = StructureSession.getStructure(structureId, STRUCTURE_REPOSITORY);
        Service ministerialService = null;
        try {
            String code = serviceString.substring(0, serviceString.indexOf("-")-1);
            System.out.println("service code " + code);
            ministerialService = ServiceDao.fromDTO(
                    ServiceDao.readByCode(code, SERVICE_REPOSITORY));
            StructureService service = new StructureService();
            service.setId(ministerialService.getId());
            service.setCode(ministerialService.getCode());
            service.setName(ministerialService.getName());
            System.out.println("service id " + service.getId());
            service.setRate(Float.valueOf(rate));
            service.setActive(true);
            structure.addService(service);
            StructureDao.addService(service, structure);
            StructureSession.refreshStructure(structure);
            model.addAttribute("message", "data successfully updated");
        } catch (Exception e){
            model.addAttribute("message", "an error occurred while updating data");
        }
        List<StructureService> services = null;
        services = new ArrayList<StructureService>();
        for(StructureService service : structure.getServices())
            if(service.getCode().equals(ministerialService.getCode()))
                services.add(service);
        model.addAttribute("filter", ministerialService.getName());
        model.addAttribute("structure", structure);
        model.addAttribute("service_list", services);
        model.addAttribute("structureId", structure.getId());
        model.addAttribute("user", user);
        model.addAttribute("isEmployee", user.isEmployee(structureId));
        return "structure_services_management";
    }
}
