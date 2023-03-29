package it.unipi.lsmsd.myhealthcare.startingdata;

import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.dao.*;
import it.unipi.lsmsd.myhealthcare.dto.StructureDTO;
import it.unipi.lsmsd.myhealthcare.databaseConnection.MongoConnectionManager;
import it.unipi.lsmsd.myhealthcare.repository.CityRepository;
import it.unipi.lsmsd.myhealthcare.repository.ServiceRepository;
import it.unipi.lsmsd.myhealthcare.repository.StructureRepository;
import it.unipi.lsmsd.myhealthcare.repository.UserRepository;
import it.unipi.lsmsd.myhealthcare.service.Utility;

import java.util.List;

public class StartingData {
    public static Integer numberOfLombardiaStructures, numberOfUmbriaStructures, numberOfPugliaStructures;

    public static void setup(ServiceRepository serviceRepository, CityRepository cityRepository,
                             StructureRepository structureRepository, UserRepository userRepository,
                             boolean bookingsAndReviews, boolean userRoles, boolean createIndexes){
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
            LombardiaReader.populateStructure(structureRepository, serviceRepository);
            numberOfLombardiaStructures = structureRepository.findByRegion("Lombardia").size();
        }

        numberOfUmbriaStructures = 0;
        try {
            numberOfUmbriaStructures = structureRepository.findByRegion("Umbria").size();
        }
        catch (Exception e){}
        if(numberOfUmbriaStructures == 0) {
            UmbriaReader.populateStructure(structureRepository, serviceRepository);
            numberOfUmbriaStructures = structureRepository.findByRegion("Umbria").size();
        }

        numberOfPugliaStructures = 0;
        try {
            numberOfPugliaStructures = structureRepository.findByRegion("Puglia").size();
        }
        catch (Exception e){}
        if(numberOfPugliaStructures == 0) {
            PugliaReader.populateStructure(structureRepository, serviceRepository);
            numberOfPugliaStructures = structureRepository.findByRegion("Puglia").size();
        }

        System.out.println("Structures in the collection: " + structureRepository.count()
                + " (" + numberOfLombardiaStructures + " from Lombardia, "
                + numberOfUmbriaStructures + " from Umbria, "
                + numberOfPugliaStructures + " from Puglia)");

        List<StructureDTO> storedStructures = StructureDao.readAllDTO(structureRepository);

        System.out.println("Operations completed on structures");

        Long numberOfUsers = 0L;
        try {
            numberOfUsers = UserDao.size(userRepository);
        }
        catch (Exception e){}
        if(numberOfUsers == 0) {
            UserReader.populateUser(userRepository, cityRepository, structureRepository);
            numberOfUsers = UserDao.size(userRepository);
        }
        System.out.println("Users in the collection: " + numberOfUsers);

        if(bookingsAndReviews)
            BookingAndReviewCreator.populateBookingAndReview(structureRepository, userRepository);

        if(userRoles)
            UserReader.populateUserRole(structureRepository, userRepository);

        if(createIndexes) {
            createIndexes();
            System.out.println("Indexes created");
        }
    }

    public static void createIndexes(){
        MongoConnectionManager mongo = new MongoConnectionManager();
        mongo.printCollections();
        mongo.createUniqueIndex("User","username");
        mongo.createIndex("Structure","city._id");
        mongo.createIndex("Structure","area");
        mongo.printIndexes();
    }
}
