package it.unipi.lsmsd.myhealthcare.dto;

import java.util.Date;

public abstract class AbstractReviewDTO {
    protected Date date;
    protected Integer rating;
    protected String text;

    public AbstractReviewDTO(){}

    public AbstractReviewDTO(Integer rating, Date date, String text) {
        this.date = date;
        this.rating = rating;
        this.text = text;
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
}
