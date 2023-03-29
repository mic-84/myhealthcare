package it.unipi.lsmsd.myhealthcare.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesManager {
	public String cityFilename, cityNeigboursFilename;
	public String ministerialServices;
	public String lombardiaStructures, pugliaStructures, umbriaStructures;
	public String doctorReviews;
	public String users;

	public String mongoDatabase, mongoUriReplica;
	public String mongoCityCollection, mongoStructureCollection, mongoServiceCollection, mongoUserCollection;
	public static String redisHost, redisReplicaHost, redisNamespace;
	public static Integer redisPort, redisUserExpirationTime, redisBookingExpirationTime, redisStructureExpirationTime;
	
	public PropertiesManager() {
		try {
			InputStream input = Files.newInputStream(Paths.get("src/main/resources/application.properties"));
			Properties properties = new Properties();			
			properties.load(input);
			
			cityFilename = properties.getProperty("city.filename");
			cityNeigboursFilename = properties.getProperty("city.neigboursFilename");
			ministerialServices = properties.getProperty("services.filename");
			lombardiaStructures = properties.getProperty("lombardia.structures");
			pugliaStructures = properties.getProperty("puglia.structures");
			umbriaStructures = properties.getProperty("umbria.structures");
			users = properties.getProperty("users.filename");
			doctorReviews = properties.getProperty("review.filename");

			mongoUriReplica = properties.getProperty("spring.data.mongodb.uri");
			mongoDatabase = properties.getProperty("spring.data.mongodb.database");
			mongoCityCollection = properties.getProperty("mongodb.collections.city");
			mongoStructureCollection = properties.getProperty("mongodb.collections.structure");
			mongoServiceCollection = properties.getProperty("mongodb.collections.service");
			mongoUserCollection = properties.getProperty("mongodb.collections.user");

			redisHost = properties.getProperty("redis.host");
			redisReplicaHost = properties.getProperty("redis.replicaHost");
			redisPort = Integer.valueOf(properties.getProperty("redis.port"));
			redisNamespace = properties.getProperty("redis.namespace");
			redisUserExpirationTime = Integer.valueOf(properties.getProperty("redis.userExpirationTime"));
			redisBookingExpirationTime = Integer.valueOf(properties.getProperty("redis.bookingExpirationTime"));
			redisStructureExpirationTime = Integer.valueOf(properties.getProperty("redis.structureExpirationTime"));
		}
		catch (IOException e) {e.printStackTrace();}
	}
}
