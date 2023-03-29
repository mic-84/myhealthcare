package it.unipi.lsmsd.myhealthcare.model;

import it.unipi.lsmsd.myhealthcare.service.Utility;

import java.sql.Timestamp;
import java.util.*;

public class Booking {
    protected String code;
    protected String status;
    protected Date creationDate, confirmationDate, bookingDate;
    protected int year, month;
    protected List<StructureService> services;
    protected Float total;

    public Booking(){
        total = 0f;
        services = new ArrayList<StructureService>();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        year = Utility.getYear(bookingDate);
        month = Utility.getMonth(bookingDate);
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

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public List<StructureService> getServices() {
        return services;
    }

    public void setServices(List<StructureService> services) {
        this.services = services;
    }

    public void addService(StructureService service){
        services.add(service);
    }

    public int getNumberOfServices(){
        return services.size();
    }

    public void computeCode(Structure structure, User user){
        if(code == null)
            code = Utility.getHash(
                    structure.getId() + user.getId() + new Timestamp(System.currentTimeMillis()));
    }
    public void computeTotal(){
        for(StructureService service:services)
            total += service.getRate();
    }

    public String getStringCreationDate(){
        return Utility.dateHourToString(creationDate);
    }

    public String getStringConfirmationDate(){
        if(confirmationDate == null)
            return " ";
        return Utility.dateHourToString(confirmationDate);
    }

    public String getStringBookingDate(){
        return Utility.dateToString(bookingDate);
    }

    public String getStringBookingTime(){
        return Utility.hourToString(bookingDate);
    }

    public String getStringCompleteBookingDate(){
        return getStringBookingDate() + " " + getStringBookingTime();
    }
}
