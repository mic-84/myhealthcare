package it.unipi.lsmsd.myhealthcare.mongo.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="BookingStatus")
public class BookingStatusDTO {
    @Id
    private String id;
    private String description, meaning;

    public BookingStatusDTO(){}
    public BookingStatusDTO(String description, String meaning) {
        this.description = description;
        this.meaning = meaning;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    @Override
    public String toString() {
        return id + ", " + description + " - " + meaning;
    }
}
