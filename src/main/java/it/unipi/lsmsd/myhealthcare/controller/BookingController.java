package it.unipi.lsmsd.myhealthcare.controller;

import it.unipi.lsmsd.myhealthcare.bookingCart.BookingCart;
import it.unipi.lsmsd.myhealthcare.dao.BookingDao;
import it.unipi.lsmsd.myhealthcare.dao.ReviewDao;
import it.unipi.lsmsd.myhealthcare.dao.ServiceDao;
import it.unipi.lsmsd.myhealthcare.dao.StructureDao;
import it.unipi.lsmsd.myhealthcare.model.*;
import it.unipi.lsmsd.myhealthcare.mongo.repository.*;
import it.unipi.lsmsd.myhealthcare.userSession.UserSession;
import it.unipi.lsmsd.myhealthcare.utility.BookingUtility;
import it.unipi.lsmsd.myhealthcare.utility.UserUtility;
import it.unipi.lsmsd.myhealthcare.utility.Utility;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
public class BookingController {
    private BookingCart cart;
    private Structure structure;
    private final BookingRepository BOOKING_REPOSITORY;
    private final StructureRepository STRUCTURE_REPOSITORY;
    private final CityRepository CITY_REPOSITORY;
    private final ServiceRepository SERVICE_REPOSITORY;
    private final UserRepository USER_REPOSITORY;
    private final RoleRepository ROLE_REPOSITORY;
    private final BookingStatusRepository BOOKING_STATUS_REPOSITORY;
    private final ReviewRepository REVIEW_REPOSITORY;

    public BookingController(
            BookingRepository bookingRepository, StructureRepository structureRepository,
            CityRepository cityRepository, ServiceRepository serviceRepository,
            UserRepository userRepository, RoleRepository roleRepository,
            BookingStatusRepository bookingStatusRepository, ReviewRepository reviewRepository){
        this.BOOKING_REPOSITORY = bookingRepository;
        this.STRUCTURE_REPOSITORY = structureRepository;
        this.CITY_REPOSITORY = cityRepository;
        this.SERVICE_REPOSITORY = serviceRepository;
        this.USER_REPOSITORY = userRepository;
        this.ROLE_REPOSITORY = roleRepository;
        this.BOOKING_STATUS_REPOSITORY = bookingStatusRepository;
        this.REVIEW_REPOSITORY = reviewRepository;
    }

    @PostMapping("/newBooking")
    public String newBooking(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            @RequestParam("structureId") String structureId, ModelMap model) {
        System.out.println("BookingController.newBooking " + structureId);
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        structure = StructureDao.fromMongo(
                StructureDao.readById(structureId, STRUCTURE_REPOSITORY),
                CITY_REPOSITORY, SERVICE_REPOSITORY
        );
        cart = new BookingCart(user, structure);
        model.addAttribute("user", user);
        model.addAttribute("structure", structure);
        return "booking_creation";
    }

    @PostMapping("/addServiceToBooking")
    public String addServiceToBooking(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            @RequestParam("structureId") String structureId,
            @RequestParam("serviceId") String serviceId,
            ModelMap model) {
        System.out.println("BookingController.addServiceToBooking " + structureId + ", " + serviceId);
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        structure = StructureDao.fromMongo(
                StructureDao.readById(structureId, STRUCTURE_REPOSITORY),
                CITY_REPOSITORY, SERVICE_REPOSITORY
        );
        Service service = ServiceDao.fromMongo(ServiceDao.readById(serviceId, SERVICE_REPOSITORY));
        cart.addService(service);
        model.addAttribute("user", user);
        model.addAttribute("structure", structure);
        model.put("services", cart.getServices(SERVICE_REPOSITORY));
        model.addAttribute("total", cart.getTotal(SERVICE_REPOSITORY));
        return "booking_creation";
    }

    @PostMapping("/removeServiceFromBooking")
    public String removeServiceFromBooking(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            @RequestParam("structureId") String structureId,
            @RequestParam("serviceId") String serviceId,
            ModelMap model) {
        System.out.println("BookingController.removeServiceFromBooking " + structureId + ", " + serviceId);
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        structure = StructureDao.fromMongo(
                StructureDao.readById(structureId, STRUCTURE_REPOSITORY),
                CITY_REPOSITORY, SERVICE_REPOSITORY
        );
        cart.removeService(serviceId);
        model.addAttribute("user", user);
        model.addAttribute("structure", structure);
        model.put("services", cart.getServices(SERVICE_REPOSITORY));
        model.addAttribute("total", cart.getTotal(SERVICE_REPOSITORY));
        return "booking_creation";
    }

    @PostMapping("/createBooking")
    public String createBooking(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            @RequestParam("date") String date,
            @RequestParam("structureId") String structureId,
            ModelMap model) {
        System.out.println("BookingController.createBooking " + structureId + ", " + date);
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        model.addAttribute("user", user);

        if(cart.isExpired()) {
            model.addAttribute("message", "the session is expired");
            structure = StructureDao.fromMongo(
                    StructureDao.readById(structureId, STRUCTURE_REPOSITORY),
                    CITY_REPOSITORY, SERVICE_REPOSITORY
            );
            model.addAttribute("structure", structure);
            return "structure";
        }
        Date bookingDate = null;
        try{
            bookingDate = Utility.stringToDate(date);
        } catch (Exception e){}
        structure = StructureDao.fromMongo(
                StructureDao.readById(structureId, STRUCTURE_REPOSITORY),
                CITY_REPOSITORY, SERVICE_REPOSITORY
        );
        List<Service> selectedServices = cart.getServices(SERVICE_REPOSITORY);
        if(bookingDate == null || bookingDate.compareTo(Utility.getToday()) < 0
                || selectedServices.size() == 0) {
            model.addAttribute("message", "select at least a service and insert a valid future date");
            model.put("services", cart.getServices(SERVICE_REPOSITORY));
            model.addAttribute("total", cart.getTotal(SERVICE_REPOSITORY));
            model.addAttribute("structure", structure);
            return "booking_creation";
        }
        for(Service selectedService : selectedServices){
            for(Service service : structure.getServices())
                if(service.getId().equals(selectedService.getId()) && !service.isActive()){
                    model.addAttribute("message",
                            "the service " + service.getName() + " is no longer available. Please unselect it");
                    model.put("services", cart.getServices(SERVICE_REPOSITORY));
                    model.addAttribute("total", cart.getTotal(SERVICE_REPOSITORY));
                    model.addAttribute("structure", structure);
                    return "booking_creation";
                }
        }
        BookingDao.create(
                cart.getBooking(bookingDate, SERVICE_REPOSITORY), BOOKING_REPOSITORY);
        model.addAttribute("message", "booking successfully created");
        return toMyBookings(userId, cryptedPassword, model);
    }

    @PostMapping("/toMyBookings")
    public String toMyBookings(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            ModelMap model) {
        System.out.println("BookingController.toMyBookings");
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        System.out.println("START search bookings by user " + Utility.getNow());
        model.put("list", Utility.sortBookings(
                BookingDao.readByUser(user.getId(),
                        BOOKING_REPOSITORY, STRUCTURE_REPOSITORY, SERVICE_REPOSITORY,
                    CITY_REPOSITORY, ROLE_REPOSITORY, USER_REPOSITORY,
                    BOOKING_STATUS_REPOSITORY),
                "bookingDate", false));
        System.out.println("START search bookings by user " + Utility.getNow());
        model.addAttribute("user", user);
        return "user_bookings";
    }

    @PostMapping("/toStructureBookings")
    public String toStructureBookings(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            @RequestParam("structureId") String structureId,
            ModelMap model) {
        System.out.println("BookingController.toStructureBookings " + structureId);
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        System.out.println("START search structure bookings " + Utility.getNow());
        model.put("list", Utility.sortBookings(
                BookingDao.readByStructure(structureId,
                        BOOKING_REPOSITORY, SERVICE_REPOSITORY,
                        CITY_REPOSITORY, USER_REPOSITORY),
                "bookingDate", false));
        System.out.println("END search structure bookings " + Utility.getNow());
        model.addAttribute("user", user);
        model.addAttribute("isEmployee", user.isEmployee(structureId));
        return "structure_bookings";
    }

    @PostMapping("/bookingManagement")
    public String bookingManagement(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            @RequestParam("provenance") String provenance,
            @RequestParam("bookingId") String bookingId,
            ModelMap model) {
        System.out.println("BookingController.bookingManagement " + bookingId + ", " + provenance);
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        Booking booking = BookingDao.fromMongo(
                BookingDao.readById(bookingId, BOOKING_REPOSITORY),
                USER_REPOSITORY, CITY_REPOSITORY, SERVICE_REPOSITORY, STRUCTURE_REPOSITORY,
                ROLE_REPOSITORY, BOOKING_STATUS_REPOSITORY);
        if(provenance.equals("user") && UserUtility.isPossibleReview(
                booking, REVIEW_REPOSITORY))
            model.addAttribute("isPossibleReview","ok");
        model.addAttribute("provenance", provenance);
        model.addAttribute("user", user);
        model.addAttribute("booking", booking);
        model.addAttribute("isEmployee", user.isEmployee(booking.getStructure().getId()));
        return "booking_management";
    }

    @PostMapping("/updateBooking")
    public String updateBooking(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            @RequestParam("provenance") String provenance,
            @RequestParam("operation") String operation,
            @RequestParam("bookingId") String bookingId,
            @RequestParam(value = "date", required = false) String date,
            ModelMap model) {
        System.out.println("BookingController.updateBooking " + bookingId + ", " + provenance);
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        model.addAttribute("user", user);
        Booking booking = BookingDao.fromMongo(
                BookingDao.readById(bookingId, BOOKING_REPOSITORY),
                USER_REPOSITORY, CITY_REPOSITORY, SERVICE_REPOSITORY, STRUCTURE_REPOSITORY,
                ROLE_REPOSITORY, BOOKING_STATUS_REPOSITORY);
        model.addAttribute("booking", booking);
        if(operation.equals("confirm")) {
            Date bookingDate = null;
            try{
                bookingDate = Utility.stringToDate(date);
            } catch (Exception e){}
            if(bookingDate == null || bookingDate.compareTo(Utility.getToday()) < 0) {
                model.addAttribute("message", "insert a valid future date");
                return "booking_management";
            }
            booking.setBookingDate(bookingDate);
            booking.setConfirmationDate(Utility.getToday());
            booking.setStatus(BookingUtility.getConfirmedStatus());
        } else if(operation.equals("render"))
            booking.setStatus(BookingUtility.getRenderedStatus());
        else if(operation.equals("cancel"))
            booking.setStatus(BookingUtility.getCancelledStatus());
        BookingDao.update(booking, BOOKING_REPOSITORY);
        model.addAttribute("message", "booking successfully updated");
        if(provenance.equals("user"))
            return toMyBookings(userId, cryptedPassword, model);
        else
            return toStructureBookings(userId, cryptedPassword, booking.getStructure().getId(), model);
    }

    @PostMapping("/review")
    public String review(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            @RequestParam("provenance") String provenance,
            @RequestParam("bookingId") String bookingId,
            @RequestParam("rating") String rating,
            @RequestParam("text") String text,
            ModelMap model) {
        System.out.println("BookingController.review " + bookingId + ", " + rating);
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        model.addAttribute("user", user);
        Booking booking = BookingDao.fromMongo(
                BookingDao.readById(bookingId, BOOKING_REPOSITORY),
                USER_REPOSITORY, CITY_REPOSITORY, SERVICE_REPOSITORY, STRUCTURE_REPOSITORY,
                ROLE_REPOSITORY, BOOKING_STATUS_REPOSITORY);
        model.addAttribute("booking", booking);
        Review review = new Review();
        review.setUser(user);
        review.setStructure(booking.getStructure());
        review.setRating(Integer.valueOf(rating));
        review.setText(text);
        review.setDate(Utility.getToday());
        ReviewDao.create(review, REVIEW_REPOSITORY);
        model.addAttribute("message", "review successfully saved");
        return "booking_management";
    }

    @PostMapping("/bookingsPerUser")
    public String bookingsPerUser(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            @RequestParam("structureId") String structureId,
            ModelMap model){
        System.out.println("BookingController.bookingsPerUser " + structureId);
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        model.addAttribute("structureId", structureId);
        model.put("list", BookingDao.getBookingsPerUserByStructure(
                BookingUtility.getRenderedStatus().getId(), structureId,
                BOOKING_REPOSITORY, USER_REPOSITORY, CITY_REPOSITORY));
        model.addAttribute("user", user);
        model.addAttribute("isEmployee", user.isEmployee(structureId));
        return "bookings_per_user";
    }

    @PostMapping("/bookingsPerService")
    public String bookingsPerService(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            @RequestParam("structureId") String structureId,
            ModelMap model){
        System.out.println("BookingController.bookingsPerService " + structureId);
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        structure = StructureDao.fromMongo(
                StructureDao.readById(structureId, STRUCTURE_REPOSITORY),
                CITY_REPOSITORY, SERVICE_REPOSITORY
        );
        model.put("list", BookingDao.getBookingsPerServiceByStructure(
                structure, BookingUtility.getRenderedStatus().getId(),
                BOOKING_REPOSITORY));
        model.addAttribute("user", user);
        model.addAttribute("isEmployee", user.isEmployee(structureId));
        return "bookings_per_service";
    }

    @PostMapping("/bookingsPerMonth")
    public String bookingsPerMonth(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            @RequestParam("structureId") String structureId,
            @RequestParam("year") String year,
            ModelMap model){
        System.out.println("AdminController.bookingsPerMonth " + year);
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        if(year == null) {
            model.addAttribute("structureId", structureId);
            model.addAttribute("user", user);
            model.addAttribute("isEmployee", user.isEmployee(structureId));
            return "structure";
        }
        model.addAttribute("year", year);
        structure = StructureDao.fromMongo(
                StructureDao.readById(structureId, STRUCTURE_REPOSITORY),
                CITY_REPOSITORY, SERVICE_REPOSITORY
        );
        model.put("list", BookingDao.getBookingsPerMonthByYearAndStructure(
                structure, BookingUtility.getRenderedStatus().getId(), year,
                BOOKING_REPOSITORY));
        model.addAttribute("user", user);
        model.addAttribute("isEmployee", user.isEmployee(structureId));
        return "bookings_per_month";
    }
}
