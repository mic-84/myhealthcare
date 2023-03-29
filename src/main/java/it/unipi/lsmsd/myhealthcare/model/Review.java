package it.unipi.lsmsd.myhealthcare.model;

import it.unipi.lsmsd.myhealthcare.service.ReviewUtility;
import it.unipi.lsmsd.myhealthcare.service.Utility;

import java.util.Date;

public class Review {
    protected Date date;
    protected Integer rating;
    protected String text;

    public Review(){}

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

    public String getStringDate(){
        if(date == null)
            return "-";
        return Utility.dateToString(date);
    }

    public String getGraphicRating(){
        return ReviewUtility.getGraphicRating(rating, false);
    }
}
