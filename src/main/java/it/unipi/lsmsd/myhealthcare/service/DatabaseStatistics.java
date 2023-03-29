package it.unipi.lsmsd.myhealthcare.service;

public class DatabaseStatistics {
    public String collection;
    public Integer numberOfDocuments;
    public Float storageSize, indexSize;

    public DatabaseStatistics(String collection, Integer numberOfDocuments,
                              Float storageSize, Float indexSize) {
        this.collection = collection;
        this.numberOfDocuments = numberOfDocuments;
        this.storageSize = storageSize;
        this.indexSize = indexSize;
    }

    public String toString() {
        return "collection " + collection +
                ":\n    documents: " + numberOfDocuments +
                ",\n    storage size: " + Utility.roundFloat(storageSize) + " MB" +
                ",\n    index size: " + Utility.roundFloat(indexSize) + " MB";
    }
}
