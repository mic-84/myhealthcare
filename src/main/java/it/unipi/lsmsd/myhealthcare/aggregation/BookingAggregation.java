package it.unipi.lsmsd.myhealthcare.aggregation;

import it.unipi.lsmsd.myhealthcare.model.Service;
import it.unipi.lsmsd.myhealthcare.model.Structure;
import it.unipi.lsmsd.myhealthcare.model.User;

public class BookingAggregation {
    private String _id;
    private Structure structure;
    private Service service;
    private User user;
    private String month;
    private Long count;
    private Float cost;

    public BookingAggregation(){}

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public String toString(){
        return _id + ", " + count + ", " + cost;
    }
}
