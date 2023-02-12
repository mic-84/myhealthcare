package it.unipi.lsmsd.myhealthcare.model;

public class BookingStatus {
    private String id;
    private String description, meaning;

    public BookingStatus() {}

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
