package it.unipi.lsmsd.myhealthcare.model;

import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.utility.Utility;

import java.util.Date;

public class Review {
    private String id;
    private User user;
    private Structure structure;
    private Date date;
    private Integer rating;
    private String text;

    public Review(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public String toString(){
        return id + ": user " + user.getUsername() + ", structure "
                + structure.getName() + ", rating " + rating + ", " + date
                + " " + text;
    }

    public String getStringDate(){
        return Utility.dateToString(date);
    }

    public String getGraphicRating(){
        String graphic = "";
        for(int i=0; i<rating; i++)
            graphic += MyHealthCareApplication.DOT;
        for(int i=rating+1; i<=5; i++)
            graphic += MyHealthCareApplication.EMPTY_DOT;
        return graphic;
    }
}
