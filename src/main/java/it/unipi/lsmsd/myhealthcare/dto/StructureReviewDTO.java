package it.unipi.lsmsd.myhealthcare.dto;

import org.bson.Document;

import java.util.Date;

public class StructureReviewDTO extends AbstractReviewDTO {
    private UserNoDetailsDTO user;

    public StructureReviewDTO(){}

    public StructureReviewDTO(UserNoDetailsDTO user, Integer rating,
                              Date date, String text) {
        super(rating, date, text);
        this.user = user;
    }

    public UserNoDetailsDTO getUser() {
        return user;
    }

    public void setUser(UserNoDetailsDTO user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Review{" +
                ", user='" + user + '\'' +
                ", date='" + date + '\'' +
                ", rating=" + rating +
                ", text=" + text +
                '}';
    }

    public Document toBsonDocument(){
        Document document = new Document();
        document.append("user", user.toBsonDocument());
        document.append("rating", rating);
        document.append("text", text);
        document.append("date", date);

        return document;
    }
}
