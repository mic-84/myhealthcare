package it.unipi.lsmsd.myhealthcare;

import it.unipi.lsmsd.myhealthcare.controller.*;
import it.unipi.lsmsd.myhealthcare.dao.*;
import it.unipi.lsmsd.myhealthcare.databaseConnection.RedisConnectionManager;
import it.unipi.lsmsd.myhealthcare.model.*;
import it.unipi.lsmsd.myhealthcare.mongo.repository.*;
import it.unipi.lsmsd.myhealthcare.mongo.startingdata.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.unipi.lsmsd.myhealthcare.utility.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
	@Autowired
	public RoleRepository roleRepository;
	@Autowired
	public BookingRepository bookingRepository;
	@Autowired
	public BookingStatusRepository bookingStatusRepository;
	@Autowired
	public ReviewRepository reviewRepository;
	public static List<BookingStatus> statuses;
	public static List<Role> roles;
	public static List<City> cities;
	public static List<Service> activeServices;
	public static Map<String, String> months;

	public static IndexController indexController;
	public static AdminController adminController;
	public static UserController userController;
	public static StructureController structureController;
	public static BookingController bookingController;
	public static final String DOT = "<img src=\"/resources/img/dot.png\" height=\"12\" width=\"12\">";
	public static final String EMPTY_DOT = "<img src=\"/resources/img/empty_dot.png\" height=\"12\" width=\"12\">";


	public static void main(String[] args) {
		SpringApplication.run(MyHealthCareApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		properties = new PropertiesManager();

		/* watch the list of collections
		MongoConnectionManager connection = new MongoConnectionManager(true);
		connection.printCollections();
		 */

		/* watch the dimension of each collection
		new MongoConnectionManager(true).printStatistics();
		 */

		Utility.setMonths();

		/* populate mongodb
		StartingData.setup(serviceRepository, cityRepository, structureRepository,
				userRepository, bookingRepository, roleRepository, bookingStatusRepository,
				reviewRepository,true,true);
		*/
		roles = RoleDao.readAll(roleRepository);
		statuses = BookingStatusDao.readAll(bookingStatusRepository);
		cities = Utility.sortCities(CityDao.readAll(cityRepository));
		activeServices = Utility.sortServices(ServiceDao.readAllActive(serviceRepository));

		indexController = new IndexController();
		structureController = new StructureController(
				cityRepository, structureRepository, serviceRepository, reviewRepository);
		userController = new UserController(
				userRepository, cityRepository, structureRepository, serviceRepository, roleRepository);
		adminController = new AdminController(bookingRepository, structureRepository,
				cityRepository, serviceRepository);
		bookingController = new BookingController(bookingRepository, structureRepository, cityRepository,
				serviceRepository, userRepository, roleRepository, bookingStatusRepository, reviewRepository);

		System.out.println("\nready...\n");
	}
}
