package it.unipi.lsmsd.myhealthcare.utility;

import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.dao.BookingStatusDao;
import it.unipi.lsmsd.myhealthcare.model.BookingStatus;
import it.unipi.lsmsd.myhealthcare.mongo.repository.BookingStatusRepository;

import java.util.List;

public class BookingUtility {
    public static BookingStatus getCreatedStatus(){
        return getStatus("created");
    }

    public static BookingStatus getConfirmedStatus(){
        return getStatus("confirmed");
    }

    public static BookingStatus getRenderedStatus(){
        return getStatus("rendered");
    }

    public static BookingStatus getCancelledStatus(){
        return getStatus("cancelled");
    }
    public static BookingStatus getStatus(String description){
        for(BookingStatus status: MyHealthCareApplication.statuses){
            if(status.getDescription().equals(description))
                return status;
        }
        return null;
    }

    public static BookingStatus getStatusById(String id){
        for(BookingStatus status: MyHealthCareApplication.statuses){
            if(status.getId().equals(id))
                return status;
        }
        return null;
    }
}
