package it.unipi.lsmsd.myhealthcare.mongo.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Review")
public class ReviewDTO {
    @Id
    private String id;
    private String user, structure, date;
    private Integer rating;
    private String text;

    public ReviewDTO(){}

    public ReviewDTO(String user, String structure, Integer rating, String date, String text) {
        this.user = user;
        this.structure = structure;
        this.date = date;
        this.rating = rating;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "MongoReview{" +
                "id='" + id + '\'' +
                ", user='" + user + '\'' +
                ", structure='" + structure + '\'' +
                ", date='" + date + '\'' +
                ", rating=" + rating +
                ", text=" + text +
                '}';
    }
}
