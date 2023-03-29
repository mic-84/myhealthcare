package it.unipi.lsmsd.myhealthcare;

import it.unipi.lsmsd.myhealthcare.controller.*;
import it.unipi.lsmsd.myhealthcare.dao.*;
import it.unipi.lsmsd.myhealthcare.databaseConnection.MongoConnectionManager;
import it.unipi.lsmsd.myhealthcare.model.*;
import it.unipi.lsmsd.myhealthcare.repository.CityRepository;
import it.unipi.lsmsd.myhealthcare.repository.ServiceRepository;
import it.unipi.lsmsd.myhealthcare.repository.StructureRepository;
import it.unipi.lsmsd.myhealthcare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.unipi.lsmsd.myhealthcare.service.*;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class MyHealthCareApplication implements CommandLineRunner {
	public static PropertiesManager properties;
	@Autowired
	public ServiceRepository serviceRepository;
	@Autowired
	public CityRepository cityRepository;

	@Autowired
	public StructureRepository structureRepository;
	@Autowired
	public UserRepository userRepository;
	public static MongoConnectionManager mongo;
	public static List<City> cities;
	public static Map<Integer, String> regions;
	public static List<String> areas;
	public static List<Service> services;
	public static Map<Integer, String> months;
	public static List<String> hours;

	public static IndexController indexController;
	public static UserController userController;
	public static StructureController structureController;
	public static AdminController adminController;
	public static BookingController bookingController;
	public static ReviewController reviewController;

	public static void main(String[] args) {
		SpringApplication.run(MyHealthCareApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		properties = new PropertiesManager();
		mongo = new MongoConnectionManager();

		/* watch the list of collections
		MongoConnectionManager connection = new MongoConnectionManager(true);
		connection.printCollections();
		 */

		/* watch the dimension of each collection
		new MongoConnectionManager(true).printStatistics();
		 */

		Utility.setRegionsAndAreas();
		Utility.setMonths();
		Utility.setHours();

		/* populate mongodb
		StartingData.setup(serviceRepository, cityRepository, structureRepository,
				userRepository, false, false, false);
		*/

		cities = Utility.sortCities(CityDao.readAll(cityRepository));
		services = Utility.sortServices(ServiceDao.readAll(serviceRepository));

		indexController = new IndexController();
		userController = new UserController(userRepository);
		structureController = new StructureController(
				cityRepository, structureRepository, serviceRepository);
		adminController = new AdminController(serviceRepository, structureRepository);
		bookingController = new BookingController(structureRepository, userRepository);
		reviewController = new ReviewController(structureRepository, userRepository);

		System.out.println("\nready...\n");
	}
}
