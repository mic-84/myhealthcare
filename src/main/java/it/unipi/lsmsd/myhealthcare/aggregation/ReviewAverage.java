package it.unipi.lsmsd.myhealthcare.aggregation;

import it.unipi.lsmsd.myhealthcare.utility.Utility;

public class ReviewAverage {
    private Integer _id; //structure
    private Float average;

    public ReviewAverage(){}

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public Float getAverage() {
        return average;
    }

    public Float getRoundAverage() {
        return Utility.roundFloat(average);
    }

    public void setAverage(Float average) {
        this.average = average;
    }
}
