package it.unipi.lsmsd.myhealthcare.model;

import it.unipi.lsmsd.myhealthcare.utility.Utility;

import java.util.*;

public class Booking {
    private String id;
    private User user;
    private Structure structure;
    private BookingStatus status;
    private Date creationDate, confirmationDate, bookingDate;
    private List<Service> services;
    private Float total;

    public Booking(){
        total = 0f;
        services = new ArrayList<Service>();
    }

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

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
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
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }



    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public void addService(Service service){
        services.add(service);
    }

    public int getNumberOfServices(){
        return services.size();
    }

    public void computeTotal(){
        for(Service service:services)
            total += service.getRate();
    }

    @Override
    public String toString() {
        return "Booking" +
                "\n  id=" + id +
                "\n  user=" + user.getFirstName() + " " + user.getLastName() +
                "\n  structure=" + structure.getName() +
                "\n  status=" + status.getDescription() +
                "\n  creationDate=" + creationDate +
                "\n  confirmationDate=" + confirmationDate +
                "\n  bookingDate=" + bookingDate +
                "\n  services=" + services.size() +
                "\n  total=" + total;
    }

    public String getStringCreationDate(){
        return Utility.dateToString(creationDate);
    }

    public String getStringConfirmationDate(){
        if(confirmationDate == null)
            return " ";
        return Utility.dateToString(confirmationDate);
    }

    public String getStringBookingDate(){
        return Utility.dateToString(bookingDate);
    }
}
