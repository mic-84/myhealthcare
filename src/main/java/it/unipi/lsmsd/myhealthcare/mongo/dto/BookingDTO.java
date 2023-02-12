package it.unipi.lsmsd.myhealthcare.mongo.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection="Booking")
public class BookingDTO {
    @Id
    private String id;
    private String user, structure, status;
    private String creationDate, confirmationDate, bookingDate;
    private Set<BookingService> services;
    private Float total;

    public class BookingService{
        private String id;
        private Float rate;
        public BookingService(String id, Float rate){
            this.id = id;
            this.rate = rate;
        }
        public String getId() { return id;}
        public Float getRate() { return rate;}
        public void setRate(Float rate){ this.rate = rate;}
    }

    public BookingDTO(){
        services = new HashSet<BookingService>();
    }
    public BookingDTO(String user, String structure, String status,
                      String creationDate, String confirmationDate, String bookingDate) {
        this.user = user;
        this.structure = structure;
        this.status = status;
        this.creationDate = creationDate;
        this.confirmationDate = confirmationDate;
        this.bookingDate = bookingDate;
        total = 0f;

        services = new HashSet<BookingService>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(String confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Set<BookingService> getServices() {
        return services;
    }

    public void setServices(Set<BookingService> services) {
        this.services = services;
    }

    public void addService(ServiceDTO service){
        services.add(new BookingService(service.getId(), service.getRate()));
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public void computeTotal(){
        total = 0f;
        for(BookingService service:services)
            total += service.getRate();
    }

    @Override
    public String toString() {
        return "MongoBooking{" +
                "id='" + id + '\'' +
                ", user='" + user + '\'' +
                ", structure='" + structure + '\'' +
                ", status='" + status + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", confirmationDate='" + confirmationDate + '\'' +
                ", bookingDate='" + bookingDate + '\'' +
                ", total=" + total +
                ", services='" + services.size() + '\'' +
                '}';
    }

    public String completeToString(){
        String ret = toString();
        for(BookingService service : services)
            ret += "\n    " + service.getId() + ", " + service.getRate();
        return ret;
    }
}
