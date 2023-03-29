package it.unipi.lsmsd.myhealthcare.dto;

import it.unipi.lsmsd.myhealthcare.service.Utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class AbstractBookingDTO {
    protected String status;
    protected Date creationDate, confirmationDate, bookingDate;
    protected int year, month;
    protected List<StructureServiceDTO> services;
    protected Float total;

    public AbstractBookingDTO(){
        services = new ArrayList<StructureServiceDTO>();
    }
    public AbstractBookingDTO(String status,
                              Date creationDate, Date confirmationDate, Date bookingDate) {
        this.status = status;
        this.creationDate = creationDate;
        this.confirmationDate = confirmationDate;
        this.bookingDate = bookingDate;
        year = Utility.getYear(bookingDate);
        month = Utility.getMonth(bookingDate);
        total = 0f;

        services = new ArrayList<StructureServiceDTO>();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(Date confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
        this.year = Utility.getYear(bookingDate);
        this.month = Utility.getMonth(bookingDate);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public List<StructureServiceDTO> getServices() {
        return services;
    }

    public void setServices(List<StructureServiceDTO> services) {
        this.services = services;
    }

    public void addService(StructureServiceDTO service){
        services.add(service);
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public void computeTotal(){
        total = 0f;
        for(StructureServiceDTO service:services)
            total += service.getRate();
    }
}
