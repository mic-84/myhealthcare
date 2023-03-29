package it.unipi.lsmsd.myhealthcare.controller;

import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.aggregation.AggregationUtility;
import it.unipi.lsmsd.myhealthcare.dao.*;
import it.unipi.lsmsd.myhealthcare.model.*;
import it.unipi.lsmsd.myhealthcare.repository.*;
import it.unipi.lsmsd.myhealthcare.service.Utility;
import it.unipi.lsmsd.myhealthcare.session.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {
    private final ServiceRepository SERVICE_REPOSITORY;
    private final StructureRepository STRUCTURE_REPOSITORY;

    public AdminController(ServiceRepository serviceRepository, StructureRepository structureRepository){
        this.SERVICE_REPOSITORY = serviceRepository;
        this.STRUCTURE_REPOSITORY = structureRepository;
    }

    @PostMapping("/toAdminStatistics")
    public String toAdminStatistics(
            @RequestParam("userId") String userId,
            ModelMap model){
        System.out.println("AdminController.toAdminStatistics");
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        model.addAttribute("regions", new ArrayList<String>(MyHealthCareApplication.regions.values()));
        model.addAttribute("months", MyHealthCareApplication.months);
        model.addAttribute("years", Utility.yearsList());
        List<Integer> limits = new ArrayList<Integer>();
        for(int i=1; i<=10; i++)
            limits.add(i);
        model.addAttribute("limits", limits);
        model.addAttribute("user", user);
        return "admin_statistics";
    }

    @PostMapping("/bookingsPerStructure")
    public String bookingsPerStructure(
            @RequestParam("userId") String userId,
            @RequestParam("region") String region,
            @RequestParam("year") String year,
            ModelMap model){
        System.out.println("AdminController.bookingsPerStructure " + region + " " + year);
        System.out.println("START aggregate bookings per structure " + Utility.getNow());
        model.put("list", AggregationUtility.getBasicAggregation(
                StructureDao.getBookingsPerStructureByRegionAndYear(
                    region, Integer.valueOf(year), STRUCTURE_REPOSITORY)));
        System.out.println("END aggregate bookings per structure " + Utility.getNow());
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        model.addAttribute("user", user);
        model.addAttribute("bookingsPerStructure", "true");
        return "bookings_per_structure";
    }

    @PostMapping("/firstNStructureCost")
    public String firstNStructureCost(
            @RequestParam("userId") String userId,
            @RequestParam("region") String region,
            @RequestParam("year") String year,
            @RequestParam("month") String month,
            @RequestParam("limit") String limit,
            ModelMap model){
        System.out.println("AdminController.firstNStructureCost "
                + region + " " + year + " " + month + " " + limit);
        System.out.println("START aggregate bookings per structure " + Utility.getNow());
        model.put("list", AggregationUtility.getBasicAggregation(
                StructureDao.getFirstNStructureCostByRegionAndMonthAndYear(
                        region,  Integer.valueOf(month), Integer.valueOf(year),
                        Integer.valueOf(limit), STRUCTURE_REPOSITORY)));
        System.out.println("END aggregate bookings per structure " + Utility.getNow());
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        model.addAttribute("user", user);
        return "bookings_per_structure";
    }

    @PostMapping("/toNewService")
    public String toNewService(
            @RequestParam("userId") String userId,
            ModelMap model){
        System.out.println("AdminController.toNewService");
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        model.addAttribute("user", user);
        return "new_service";
    }

    @PostMapping("/newService")
    public String newService(
            @RequestParam("userId") String userId,
            @RequestParam("code") String code,
            @RequestParam("name") String name,
            ModelMap model){
        System.out.println("AdminController.newService " + code + ", " + name);
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        try {
            StructureService service = new StructureService();
            service.setCode(code.toUpperCase().trim());
            service.setName(name.toUpperCase().trim());
            service.setActive(true);
            ServiceDao.create(service, SERVICE_REPOSITORY);
            model.addAttribute("message", "data successfully saved");
        } catch (Exception e){
            model.addAttribute("message", "an error occurred while updating data");
        }
        model.addAttribute("user", user);
        return "new_service";
    }
}
