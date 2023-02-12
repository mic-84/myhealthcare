package it.unipi.lsmsd.myhealthcare.controller;

import it.unipi.lsmsd.myhealthcare.dao.BookingDao;
import it.unipi.lsmsd.myhealthcare.dao.ServiceDao;
import it.unipi.lsmsd.myhealthcare.model.Service;
import it.unipi.lsmsd.myhealthcare.model.User;
import it.unipi.lsmsd.myhealthcare.mongo.repository.*;
import it.unipi.lsmsd.myhealthcare.userSession.UserSession;
import it.unipi.lsmsd.myhealthcare.utility.BookingUtility;
import it.unipi.lsmsd.myhealthcare.utility.Utility;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
    private final BookingRepository BOOKING_REPOSITORY;
    private final StructureRepository STRUCTURE_REPOSITORY;
    private final CityRepository CITY_REPOSITORY;
    private final ServiceRepository SERVICE_REPOSITORY;

    public AdminController(BookingRepository bookingRepository,
                           StructureRepository structureRepository,
                           CityRepository cityRepository,
                           ServiceRepository serviceRepository){
        this.BOOKING_REPOSITORY = bookingRepository;
        this.STRUCTURE_REPOSITORY = structureRepository;
        this.CITY_REPOSITORY = cityRepository;
        this.SERVICE_REPOSITORY = serviceRepository;
    }

    @PostMapping("/bookingsPerStructure")
    public String bookingsPerStructure(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            ModelMap model){
        System.out.println("AdminController.bookingsPerStructure");
        System.out.println("START aggregate bookings per structure and status " + Utility.getNow());
        model.put("list", BookingDao.getBookingsPerStructure(
                BookingUtility.getRenderedStatus().getId(),
                BOOKING_REPOSITORY, STRUCTURE_REPOSITORY, CITY_REPOSITORY));
        System.out.println("END aggregate bookings per structure and status " + Utility.getNow());
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        model.addAttribute("user", user);
        return "bookings_per_structure";
    }

    @PostMapping("/toNewService")
    public String toNewService(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            ModelMap model){
        System.out.println("AdminController.toNewService");
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        model.addAttribute("user", user);
        return "new_service";
    }

    @PostMapping("/newService")
    public String newService(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            @RequestParam("code") String code,
            @RequestParam("name") String name,
            ModelMap model){
        System.out.println("AdminController.newService " + code + ", " + name);
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        try {
            Service service = new Service();
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
