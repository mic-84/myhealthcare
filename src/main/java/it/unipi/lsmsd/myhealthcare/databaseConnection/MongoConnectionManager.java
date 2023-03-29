package it.unipi.lsmsd.myhealthcare.databaseConnection;

import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.service.DatabaseStatistics;
import it.unipi.lsmsd.myhealthcare.service.PropertiesManager;

import javax.swing.text.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MongoConnectionManager {
    private static MongoDatabase mongodb;
    private static PropertiesManager properties;

    public MongoConnectionManager() {
        properties = MyHealthCareApplication.properties;
        mongodb = MongoClients.create(new ConnectionString(properties.mongoUriReplica))
                .getDatabase(properties.mongoDatabase);
    }
    public void printCollections(){
        System.out.println("Collections found into MongoDB:");
        for (String name : mongodb.listCollectionNames())
            System.out.println("- " + name);
    }

    public List<DatabaseStatistics> getStatistics(){
        List<DatabaseStatistics> list = new ArrayList<DatabaseStatistics>();
        for (String collection : mongodb.listCollectionNames()) {
            org.bson.Document stats = mongodb.runCommand(
                    new org.bson.Document("collStats", collection));
            list.add(new DatabaseStatistics(collection, Integer.valueOf(stats.get("count").toString()),
                    Float.valueOf(stats.get("storageSize").toString())/1000000,
                    Float.valueOf(stats.get("totalIndexSize").toString())/1000000));
        }
        return list;
    }

    public void printStatistics(){
        for(DatabaseStatistics statistics : getStatistics())
            System.out.println(statistics);
    }

    public MongoDatabase getDatabase(){
        return mongodb;
    }

    public MongoCollection getCollection(String collection){
        return mongodb.getCollection(collection);
    }

    public MongoCollection getUserCollection(){
        return getCollection(MyHealthCareApplication.properties.mongoUserCollection);
    }

    public MongoCollection getStructureCollection(){
        return getCollection(MyHealthCareApplication.properties.mongoStructureCollection);
    }

    public void createIndex(String collection, String field){
        String index = mongodb.getCollection(collection).createIndex(Indexes.ascending(field));
        System.out.println("Index " + index + " created in the collection " + collection);
    }

    public void createIndex(String collection, String field1, String field2){
        String index = mongodb.getCollection(collection).createIndex(Indexes.ascending(field1,field2));
        System.out.println("Index " + index + " created in the collection " + collection);
    }

    public void createUniqueIndex(String collection, String field){
        String index = mongodb.getCollection(collection).createIndex(
                Indexes.ascending(field),new IndexOptions().unique(true));
        System.out.println("Unique index " + index + " created in the collection " + collection);
    }

    public void printIndexes() {
        System.out.println("Indexes in the collections:");
        for (String name : mongodb.listCollectionNames())
            for (org.bson.Document set : mongodb.getCollection(name).listIndexes()) {
                String ret = "collection " + name + ", fields: ";
                for (Map.Entry<String, Object> index : set.entrySet())
                    if(index.getKey().equals("key"))
                        ret += index.getValue();
                    else if(index.getKey().equals("name"))
                        ret += ", index " + index.getValue();
                    System.out.println(ret);
            }
    }

    public void printJson(String collectionName, String id){
        MongoCollection collection = mongodb.getCollection(collectionName);
        BasicDBObject query = new BasicDBObject("_id", id);

        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while(cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        }
    }
}
