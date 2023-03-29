package it.unipi.lsmsd.myhealthcare.controller;

import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.aggregation.AggregationUtility;
import it.unipi.lsmsd.myhealthcare.session.BookingCart;
import it.unipi.lsmsd.myhealthcare.dao.*;
import it.unipi.lsmsd.myhealthcare.model.*;
import it.unipi.lsmsd.myhealthcare.repository.*;
import it.unipi.lsmsd.myhealthcare.session.StructureSession;
import it.unipi.lsmsd.myhealthcare.session.UserSession;
import it.unipi.lsmsd.myhealthcare.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class BookingController {
    private final StructureRepository STRUCTURE_REPOSITORY;
    private final UserRepository USER_REPOSITORY;

    public BookingController(StructureRepository structureRepository,
            UserRepository userRepository){
        this.STRUCTURE_REPOSITORY = structureRepository;
        this.USER_REPOSITORY = userRepository;
    }

    @PostMapping("/newBooking")
    public String newBooking(
            @RequestParam("userId") String userId,
            @RequestParam("structureId") String structureId,
            @RequestParam(value = "filter", required = false) String filter,
            ModelMap model) {
        System.out.println("BookingController.newBooking " + structureId);
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        Structure structure = StructureSession.getStructure(structureId, STRUCTURE_REPOSITORY);
        BookingCart cart = new BookingCart(user, structure);
        model.put("services", cart.getServices(user, structure));
        model.addAttribute("total", cart.getTotal(user, structure));
        model.addAttribute("user", user);
        model.addAttribute("structure", structure);
        List<StructureService> services = null;
        if(filter != null){
            services = new ArrayList<StructureService>();
            for(StructureService service : structure.getServices())
                if(service.isActive() &&
                        (service.getName().contains(filter.trim().toUpperCase()) ||
                                service.getCode().contains(filter.trim().toUpperCase())))
                    services.add(service);
        } else
            services = structure.getActiveServices();
        model.addAttribute("filter", filter);
        model.addAttribute("hours", MyHealthCareApplication.hours);
        model.addAttribute("structure_services", services);
        return "booking_creation";
    }

    @PostMapping("/addServiceToBooking")
    public String addServiceToBooking(
            @RequestParam("userId") String userId,
            @RequestParam("structureId") String structureId,
            @RequestParam("serviceId") String serviceId,
            @RequestParam(value = "filter", required = false) String filter,
            ModelMap model) {
        System.out.println("BookingController.addServiceToBooking " + structureId + ", " + serviceId);
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        Structure structure = StructureSession.getStructure(structureId, STRUCTURE_REPOSITORY);
        BookingCart cart = new BookingCart(user, structure);
        String ret = cart.addService(structure.getServiceById(serviceId), user.getId(), structureId);
        model.addAttribute("message", ret);
        List<StructureService> services = null;
        List<StructureService> selectedServices = cart.getServices(user, structure);
        System.out.println(selectedServices.size() + " services selected");
        if(filter != null){
            services = new ArrayList<StructureService>();
            for(StructureService service : structure.getServices())
                if(service.isActive() &&
                        (service.getName().contains(filter.trim().toUpperCase()) ||
                                service.getCode().contains(filter.trim().toUpperCase()))) {
                    boolean flag = true;
                    for (StructureService selectedService : selectedServices)
                        if(selectedService.getId().equals(service.getId()))
                            flag = false;
                    if(flag)
                        services.add(service);
                }
        } else
            services = structure.getActiveServices();
        model.addAttribute("user", user);
        model.addAttribute("structure", structure);
        model.put("services", selectedServices);
        model.addAttribute("total", cart.getTotal(user, structure));
        model.addAttribute("filter", filter);
        model.addAttribute("structure_services", services);
        model.addAttribute("hours", MyHealthCareApplication.hours);
        return "booking_creation";
    }

    @PostMapping("/filterServices")
    public String filterServices(
            @RequestParam("userId") String userId,
            @RequestParam("structureId") String structureId,
            @RequestParam(value = "filter", required = false) String filter,
            ModelMap model) {
        System.out.println("BookingController.filterServices " + structureId);
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        Structure structure = StructureSession.getStructure(structureId, STRUCTURE_REPOSITORY);
        model.addAttribute("user", user);
        model.addAttribute("structure", structure);
        BookingCart cart = new BookingCart(user, structure);
        model.put("services", cart.getServices(user, structure));
        model.addAttribute("total", cart.getTotal(user, structure));
        List<StructureService> services = null;
        if(filter != null){
            services = new ArrayList<StructureService>();
            for(StructureService service : structure.getServices())
                if(service.isActive() &&
                        (service.getName().contains(filter.trim().toUpperCase()) ||
                                service.getCode().contains(filter.trim().toUpperCase())))
                    services.add(service);
        } else
            services = structure.getActiveServices();
        model.addAttribute("filter", filter);
        model.addAttribute("structure_services", services);
        return "booking_creation";
    }

    @PostMapping("/removeServiceFromBooking")
    public String removeServiceFromBooking(
            @RequestParam("userId") String userId,
            @RequestParam("structureId") String structureId,
            @RequestParam("serviceId") String serviceId,
            @RequestParam(value = "filter", required = false) String filter,
            ModelMap model) {
        System.out.println("BookingController.removeServiceFromBooking " + structureId + ", " + serviceId);
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        Structure structure = StructureSession.getStructure(structureId, STRUCTURE_REPOSITORY);
        BookingCart cart = new BookingCart(user, structure);
        cart.removeService(user.getId(), serviceId);
        model.addAttribute("user", user);
        model.addAttribute("structure", structure);
        model.put("services", cart.getServices(user, structure));
        model.addAttribute("total", cart.getTotal(user, structure));
        List<StructureService> services = null;
        if(filter != null){
            services = new ArrayList<StructureService>();
            for(StructureService service : structure.getServices())
                if(service.isActive() &&
                        (service.getName().contains(filter.trim().toUpperCase()) ||
                                service.getCode().contains(filter.trim().toUpperCase())))
                    services.add(service);
        } else
            services = structure.getActiveServices();
        model.addAttribute("filter", filter);
        model.addAttribute("structure_services", services);
        model.addAttribute("hours", MyHealthCareApplication.hours);
        return "booking_creation";
    }

    @PostMapping("/createBooking")
    public String createBooking(
            @RequestParam("userId") String userId,
            @RequestParam("date") String date,
            @RequestParam("time") String time,
            @RequestParam("structureId") String structureId,
            @RequestParam("filter") String filter,
            ModelMap model) {
        System.out.println("BookingController.createBooking " + structureId +
                ", " + date + ", " + time);
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        model.addAttribute("user", user);
        Structure structure = StructureSession.getStructure(structureId, STRUCTURE_REPOSITORY);
        BookingCart cart = new BookingCart(user, structure);
        if(cart.isExpired(user)) {
            model.addAttribute("message", "the session is expired");
            model.addAttribute("structure", structure);
            return "structure";
        }
        Date bookingDate = null;
        try{
            bookingDate = Utility.stringToDateNoSeconds(date + " " + time);
        } catch (Exception e){}
        structure = StructureSession.getStructure(structureId, STRUCTURE_REPOSITORY);
        List<StructureService> selectedServices = cart.getServices(user, structure);
        if(bookingDate == null || bookingDate.compareTo(Utility.getToday()) < 0
                || selectedServices.size() == 0) {
            model.addAttribute("message", "select at least a service and insert a valid future date");
            model.put("services", selectedServices);
            model.addAttribute("total", cart.getTotal(user, structure));
            model.addAttribute("structure", structure);
            List<StructureService> services = null;
            if(filter != null){
                services = new ArrayList<StructureService>();
                for(StructureService service : structure.getServices())
                    if(service.isActive() &&
                            (service.getName().contains(filter.trim().toUpperCase()) ||
                                    service.getCode().contains(filter.trim().toUpperCase())))
                        services.add(service);
            } else
                services = structure.getActiveServices();
            model.addAttribute("filter", filter);
            model.addAttribute("hours", MyHealthCareApplication.hours);
            model.addAttribute("structure_services", services);
            return "booking_creation";
        }
        for(Service selectedService : selectedServices){
            for(StructureService service : structure.getServices())
                if(service.getId().equals(selectedService.getId()) && !service.isActive()){
                    model.addAttribute("message",
                            "the service " + service.getName() + " is no longer available. Please unselect it");
                    model.put("services", cart.getServices(user, structure));
                    model.addAttribute("total", cart.getTotal(user, structure));
                    model.addAttribute("structure", structure);
                    return "booking_creation";
                }
        }
        Booking booking = cart.saveBooking(user, structure, bookingDate);
        user = UserDao.fromDTO(UserDao.readById(userId, USER_REPOSITORY));
        UserSession.refreshUser(user);
        if(booking.getCode() != null)
            model.addAttribute("message", "booking successfully created");
        else
            model.addAttribute("message", "an error occurred during the creation of the booking");
        return toMyBookings(userId, model);
    }

    @PostMapping("/toMyBookings")
    public String toMyBookings(
            @RequestParam("userId") String userId,
            ModelMap model) {
        System.out.println("BookingController.toMyBookings");
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        System.out.println("START search bookings by user " + Utility.getNow());
        model.put("list", BookingUtility.sortUserBookings(user.getBookings(),"bookingDate", false));
        System.out.println("END search bookings by user " + Utility.getNow());
        model.addAttribute("user", user);
        return "user_bookings";
    }

    @PostMapping("/toStructureBookings")
    public String toStructureBookings(
            @RequestParam("userId") String userId,
            @RequestParam("structureId") String structureId,
            @RequestParam("criteria") String criteria,
            ModelMap model) {
        System.out.println("BookingController.toStructureBookings " + structureId);
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        System.out.println("START search structure bookings " + Utility.getNow());
        Structure structure = StructureDao.fromDTO(StructureDao.readById(structureId, STRUCTURE_REPOSITORY));
        model.put("list", BookingUtility.getStructureBookings(
                structure.getBookings(), criteria, true, "bookingDate", false));
        System.out.println("END search structure bookings " + Utility.getNow());
        model.addAttribute("user", user);
        model.addAttribute("structureId", structureId);
        model.addAttribute("isEmployee", user.isEmployee(structureId));
        return "structure_bookings";
    }

    @PostMapping("/bookingManagement")
    public String bookingManagement(
            @RequestParam("userId") String userId,
            @RequestParam("structureId") String structureId,
            @RequestParam("provenance") String provenance,
            @RequestParam("bookingCode") String bookingCode,
            ModelMap model) {
        System.out.println("BookingController.bookingManagement " + bookingCode + ", " + provenance);
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        // creare booking in base alla provenance
        if(provenance.equals("user")){
            UserBooking booking = user.getBookingByCode(bookingCode);
            model.addAttribute("booking", booking);
            model.addAttribute("structure", booking.getStructure());
            if(ReviewUtility.isPossibleReview(booking, user)) {
                model.addAttribute("isPossibleReview", "ok");
                Map<Integer, String> ratings = new HashMap<Integer, String>();
                for(int i=1; i<=5; i++)
                    ratings.put(i, ReviewUtility.getGraphicRating(i, true));
                model.addAttribute("ratings", ratings);
            }
            model.addAttribute("isEmployee", user.isEmployee(booking.getStructure().getId()));
        } else if(provenance.equals("structure")) {
            Structure structure = StructureDao.fromDTO(StructureDao.readById(structureId, STRUCTURE_REPOSITORY));
            StructureBooking booking = structure.getBookingByCode(bookingCode);
            //System.out.println(booking);
            model.addAttribute("structure", structure);
            model.addAttribute("booking", booking);
            model.addAttribute("isEmployee", user.isEmployee(structure.getId()));
        }
        model.addAttribute("provenance", provenance);
        model.addAttribute("user", user);
        model.addAttribute("hours", MyHealthCareApplication.hours);
        return "booking_management";
    }

    @PostMapping("/confirmBooking")
    public String confirmBooking(
            @RequestParam("userId") String userId,
            @RequestParam("structureId") String structureId,
            @RequestParam("provenance") String provenance,
            @RequestParam("bookingCode") String bookingCode,
            @RequestParam("date") String date,
            @RequestParam("time") String time,
            ModelMap model) {
        System.out.println("BookingController.confirmBooking "
                + bookingCode + ", " + provenance + ", " + date + ", " + time);
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        model.addAttribute("user", user);

        Structure structure = StructureSession.getStructure(structureId, STRUCTURE_REPOSITORY);
        StructureBooking booking = structure.getBookingByCode(bookingCode);
        Date bookingDate = null;
        try{
            bookingDate = Utility.stringToDateNoSeconds(date + " " + time);
        } catch (Exception e){}
        if(bookingDate == null || bookingDate.compareTo(Utility.getToday()) < 0) {
            model.addAttribute("message", "insert a valid future date");
            return "booking_management";
        }
        booking.setBookingDate(bookingDate);
        booking.setConfirmationDate(Utility.getToday());
        booking.setStatus(BookingUtility.getConfirmedStatus());
        model.addAttribute("booking", booking);
        StructureDao.updateBookingDate(booking);
        StructureDao.updateBookingStatus(booking);
        StructureDao.updateBookingConfirmationDate(booking);
        UserDao.updateBookingDate(booking);
        UserDao.updateBookingStatus(booking);
        UserDao.updateBookingConfirmationDate(booking);
        model.addAttribute("message", "booking successfully updated");
        return toStructureBookings(userId, structure.getId(), "all", model);
    }

    @PostMapping("/updateBooking")
    public String updateBooking(
            @RequestParam("userId") String userId,
            @RequestParam("structureId") String structureId,
            @RequestParam("provenance") String provenance,
            @RequestParam("operation") String operation,
            @RequestParam("bookingCode") String bookingCode,
            ModelMap model) {
        System.out.println("BookingController.updateBooking " + bookingCode + ", " +
                operation + ", " + provenance + ", " + userId + ", " + structureId);
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        model.addAttribute("user", user);

        Structure structure = StructureDao.fromDTO(StructureDao.readById(structureId, STRUCTURE_REPOSITORY));
        StructureBooking booking = structure.getBookingByCode(bookingCode);
        if(operation.equals("render"))
            booking.setStatus(BookingUtility.getRenderedStatus());
        else if(operation.equals("cancel"))
            booking.setStatus(BookingUtility.getCancelledStatus());
        model.addAttribute("booking", booking);
        StructureDao.updateBookingStatus(booking);
        UserDao.updateBookingStatus(booking);
        model.addAttribute("message", "booking successfully updated");
        if(provenance.equals("structure"))
            return toStructureBookings(userId, structure.getId(), "all", model);
        else {
            user = UserDao.fromDTO(UserDao.readById(userId, USER_REPOSITORY));
            UserSession.refreshUser(user);
            return toMyBookings(userId, model);
        }
    }

    @PostMapping("/bookingsPerUser")
    public String bookingsPerUser(
            @RequestParam("userId") String userId,
            @RequestParam("structureId") String structureId,
            @RequestParam("threshold") String threshold,
            ModelMap model){
        System.out.println("BookingController.bookingsPerUser " + structureId);
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        model.addAttribute("structureId", structureId);
        model.put("list", AggregationUtility.getBasicAggregation(
                StructureDao.getUsersWithExpenditureGreaterThanThreshold(
                    structureId, Float.valueOf(threshold), STRUCTURE_REPOSITORY)));
        model.addAttribute("user", user);
        model.addAttribute("threshold", threshold);
        model.addAttribute("isEmployee", user.isEmployee(structureId));
        return "bookings_per_user";
    }

    @PostMapping("/bookingsPerServiceLastMonth")
    public String bookingsPerServiceLastMonth(
            @RequestParam("userId") String userId,
            @RequestParam("structureId") String structureId,
            ModelMap model){
        System.out.println("BookingController.bookingsPerServiceLastMonth " + structureId);
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        Integer lastMonth[] = Utility.getLastMonth();
        System.out.println("last month: " + lastMonth[1] + "/" + lastMonth[0]);
        lastMonth[1] = 10;
        lastMonth[0] = 2022;
        model.put("list", AggregationUtility.getBasicAggregation(
                StructureDao.getServicesUsageByMonth(
                    structureId, lastMonth[0], lastMonth[1], STRUCTURE_REPOSITORY)));
        model.addAttribute("user", user);
        model.addAttribute("structureId", structureId);
        model.addAttribute("lastMonth", Utility.getMonth(lastMonth[1].toString()) + " " + lastMonth[0]);
        model.addAttribute("isEmployee", user.isEmployee(structureId));
        return "bookings_per_service";
    }

    @PostMapping("/bookingsPerMonth")
    public String bookingsPerMonth(
            @RequestParam("userId") String userId,
            @RequestParam("structureId") String structureId,
            @RequestParam("year") String year,
            ModelMap model){
        System.out.println("BookingController.bookingsPerMonth " + year);
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        if(year == null) {
            model.addAttribute("structureId", structureId);
            model.addAttribute("user", user);
            model.addAttribute("isEmployee", user.isEmployee(structureId));
            return "structure";
        }
        model.addAttribute("year", year);
        model.addAttribute("structureId", structureId);
        model.put("list", StructureDao.getBookingsPerMonthByYearAndStructure(
                structureId, Integer.valueOf(year), STRUCTURE_REPOSITORY
        ));
        model.addAttribute("user", user);
        model.addAttribute("isEmployee", user.isEmployee(structureId));
        return "bookings_per_month";
    }
}
