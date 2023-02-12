package it.unipi.lsmsd.myhealthcare.mongo.startingdata;

import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.dao.*;
import it.unipi.lsmsd.myhealthcare.model.BookingStatus;
import it.unipi.lsmsd.myhealthcare.model.Role;
import it.unipi.lsmsd.myhealthcare.mongo.dto.ServiceDTO;
import it.unipi.lsmsd.myhealthcare.mongo.dto.StructureDTO;
import it.unipi.lsmsd.myhealthcare.mongo.repository.*;
import it.unipi.lsmsd.myhealthcare.databaseConnection.MongoConnectionManager;
import it.unipi.lsmsd.myhealthcare.utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class StartingData {
    public static Integer numberOfLombardiaStructures;
    public static Integer numberOfUmbriaStructures;

    public static void setup(ServiceRepository serviceRepository, CityRepository cityRepository,
                             StructureRepository structureRepository, UserRepository userRepository,
                             BookingRepository bookingRepository, RoleRepository roleRepository,
                             BookingStatusRepository bookingStatusRepository,
                             ReviewRepository reviewRepository, boolean createIndexes, boolean replica){
        Long numberOfRoles = 0L;
        try {
            numberOfRoles = RoleDao.size(roleRepository);
        }
        catch (Exception e){}
        if(numberOfRoles == 0) {
            UserReader.populateRole(roleRepository);
            numberOfRoles = RoleDao.size(roleRepository);
        }
        System.out.println("Roles in the collection: " + numberOfRoles);
        MyHealthCareApplication.roles = RoleDao.readAll(roleRepository);
        for(Role role:MyHealthCareApplication.roles)
            System.out.println("- " + role);

        Long numberOfStatuses = 0L;
        try{
            numberOfStatuses = BookingStatusDao.size(bookingStatusRepository);
        }
        catch (Exception e){}
        if(numberOfStatuses == 0) {
            BookingCreator.populateStatus(bookingStatusRepository);
            numberOfStatuses = BookingStatusDao.size(bookingStatusRepository);
        }
        System.out.println("Statuses in the collection: " + numberOfStatuses);
        MyHealthCareApplication.statuses = BookingStatusDao.readAll(bookingStatusRepository);
        for(BookingStatus status:MyHealthCareApplication.statuses)
            System.out.println("- " + status);

        Long numberOfCities = 0L;
        try {
            numberOfCities = CityDao.size(cityRepository);
        }
        catch (Exception e){}
        if(numberOfCities == 0) {
            CityReader.populateCity(cityRepository);
            numberOfCities = CityDao.size(cityRepository);
        }
        System.out.println("Cities in the collection: " + numberOfCities);
        MyHealthCareApplication.cities = Utility.sortCities(CityDao.readAll(cityRepository));

        Long numberOfServices = 0L;
        try {
            numberOfServices = ServiceDao.size(serviceRepository);
        }
        catch (Exception e){}
        if(numberOfServices == 0) {
            ServiceReader.populateService(serviceRepository);
            numberOfServices = ServiceDao.size(serviceRepository);
        }
        System.out.println("Services in the collection: " + numberOfServices);

        numberOfLombardiaStructures = 0;
        try {
            numberOfLombardiaStructures = structureRepository.findByRegion("Lombardia").size();
        }
        catch (Exception e){}
        if(numberOfLombardiaStructures == 0) {
            LombardiaReader.populateStructure(structureRepository, serviceRepository, cityRepository);
            numberOfLombardiaStructures = structureRepository.findByRegion("Lombardia").size();
        }

        numberOfUmbriaStructures = 0;
        try {
            numberOfUmbriaStructures = structureRepository.findByRegion("Umbria").size();
        }
        catch (Exception e){}
        if(numberOfUmbriaStructures == 0) {
            UmbriaReader.populateStructure(structureRepository, serviceRepository, cityRepository);
            numberOfUmbriaStructures = structureRepository.findByRegion("Umbria").size();
        }

        System.out.println("Structures in the collection: " + structureRepository.count()
                + " (" + numberOfLombardiaStructures + " from Lombardia, "
                + numberOfUmbriaStructures + " from Umbria)");

        List<StructureDTO> storedStructures = StructureDao.readAllMongo(structureRepository);
        List<StructureDTO> structuresToUpdate = new ArrayList<StructureDTO>();
        for(StructureDTO structure : storedStructures){
            boolean flag = false;
            for(ServiceDTO service : structure.getServices())
                if(service.getRate() == null){
                    service.setRate(10f);
                    flag = true;
                }
            if(flag)
                structuresToUpdate.add(structure);
        }
        structureRepository.saveAll(structuresToUpdate);

        System.out.println("Operations completed on structures");

        Long numberOfUsers = 0L;
        try {
            numberOfUsers = UserDao.size(userRepository);
        }
        catch (Exception e){}
        if(numberOfUsers == 0) {
            UserReader.populateUser(userRepository, cityRepository, structureRepository, roleRepository);
            numberOfUsers = UserDao.size(userRepository);
        }
        System.out.println("Users in the collection: " + numberOfUsers);

        Long numberOfBookings = 0L;
        try {
            numberOfBookings = BookingDao.size(bookingRepository);
        }
        catch (Exception e){}
        if(numberOfBookings == 0) {
            BookingCreator.populateBooking(bookingRepository, structureRepository, userRepository);
            numberOfBookings = BookingDao.size(bookingRepository);
        }
        System.out.println("Bookings in the collection: " + numberOfBookings);

        Long numberOfReviews = 0L;
        try {
            numberOfReviews = ReviewDao.size(reviewRepository);
        }
        catch (Exception e){}
        if(numberOfReviews == 0) {
            ReviewReader.create(reviewRepository, structureRepository, bookingRepository);
            numberOfReviews = ReviewDao.size(reviewRepository);
        }
        System.out.println("Reviews in the collection: " + numberOfReviews);

        if(createIndexes) {
            createIndexes(replica);
            System.out.println("Indexes created");
        }
    }

    public static void createIndexes(boolean replica){
        MongoConnectionManager mongoConnectionManager = new MongoConnectionManager(replica);
        mongoConnectionManager.printCollections();
        mongoConnectionManager.createIndex("Structure","city");
        mongoConnectionManager.createIndex("Booking","user");
        mongoConnectionManager.createIndex("Booking","structure");
        mongoConnectionManager.createIndex("Booking","structure","status");
        mongoConnectionManager.createIndex("Review","structure");
        mongoConnectionManager.createIndex("Review","structure","rating");
        mongoConnectionManager.createIndex("Review","user");
        mongoConnectionManager.createIndex("Review","user","structure");
        mongoConnectionManager.createUniqueIndex("User","username");
        mongoConnectionManager.printIndexes();
    }
}
