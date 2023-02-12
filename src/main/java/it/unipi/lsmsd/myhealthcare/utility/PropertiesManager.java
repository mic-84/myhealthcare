package it.unipi.lsmsd.myhealthcare.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesManager {
	public String cityFilename, cityNeigboursFilename;
	public String ministerialServices;
	public String umbriaStructures, lombardiaStructures;
	public String doctorReviews;
	public String users;

	public String mongoUri, mongoDatabase, mongoUriReplica;
	public String mongoCityCollection, mongoStructureCollection, mongoServiceCollection;
	public String mongoBookingStatusCollection, mongoBookingCollection, mongoReviewCollection;
	public String mongoUserCollection, mongoRoleCollection;
	public static String redisHost, redisReplicaHost, redisNamespace;
	public static Integer redisPort, redisBookingExpirationTime;
	
	public PropertiesManager() {
		try {
			InputStream input = new FileInputStream("src/main/resources/application.properties");
			Properties properties = new Properties();			
			properties.load(input);
			
			cityFilename = properties.getProperty("city.filename");
			cityNeigboursFilename = properties.getProperty("city.neigboursFilename");
			ministerialServices = properties.getProperty("services.filename");
			umbriaStructures = properties.getProperty("umbria.structures");
			lombardiaStructures = properties.getProperty("lombardia.structures");
			users = properties.getProperty("users.filename");
			doctorReviews = properties.getProperty("review.filename");

			mongoUri = "mongodb://" + properties.getProperty("spring.data.mongodb.host")
				+ ":" + properties.getProperty("spring.data.mongodb.port");
			mongoUriReplica = properties.getProperty("spring.data.mongodb.uri");
			mongoDatabase = properties.getProperty("spring.data.mongodb.database");
			mongoCityCollection = properties.getProperty("mongodb.collections.city");
			mongoStructureCollection = properties.getProperty("mongodb.collections.structure");
			mongoServiceCollection = properties.getProperty("mongodb.collections.service");
			mongoBookingStatusCollection = properties.getProperty("mongodb.collections.bookingStatus");
			mongoBookingCollection = properties.getProperty("mongodb.collections.booking");
			mongoReviewCollection = properties.getProperty("mongodb.collections.review");
			mongoUserCollection = properties.getProperty("mongodb.collections.user");
			mongoRoleCollection = properties.getProperty("mongodb.collections.role");

			redisHost = properties.getProperty("redis.host");
			redisReplicaHost = properties.getProperty("redis.replicaHost");
			redisPort = Integer.valueOf(properties.getProperty("redis.port"));
			redisNamespace = properties.getProperty("redis.namespace");
			redisBookingExpirationTime = Integer.valueOf(properties.getProperty("redis.bookingExpirationTime"));
		}
		catch (IOException e) {e.printStackTrace();}
	}
}
