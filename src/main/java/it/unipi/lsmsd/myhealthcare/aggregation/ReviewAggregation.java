package it.unipi.lsmsd.myhealthcare.aggregation;

import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;

public class ReviewAggregation {
    private Integer _id; //rating
    private Long count;

    private String graphic;

    public ReviewAggregation(){}

    public Integer getRating() {
        return _id;
    }

    public void setRating(Integer rating) {
        this._id = rating;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public void setGraphic(){
        graphic = "";
        for(int i=0; i<_id; i++)
            graphic += MyHealthCareApplication.DOT;
        for(int i=_id+1; i<=5; i++)
            graphic += MyHealthCareApplication.EMPTY_DOT;
    }

    public String getGraphic(){
        return graphic;
    }
}
