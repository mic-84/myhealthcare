package it.unipi.lsmsd.myhealthcare.dto;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StructureBookingDTO extends AbstractBookingDTO {
    private String code;
    private UserNoDetailsDTO user;

    public StructureBookingDTO(){
        super();
    }
    public StructureBookingDTO(UserDTO user, String status,
                               Date creationDate, Date confirmationDate, Date bookingDate) {
        super(status, creationDate, confirmationDate, bookingDate);
        this.user = new UserNoDetailsDTO(user);
    }

    public StructureBookingDTO(UserBookingDTO booking, UserDTO user) {
        super(booking.getStatus(), booking.getCreationDate(),
                booking.getConfirmationDate(), booking.getBookingDate());
        this.code = booking.getCode();
        this.user = new UserNoDetailsDTO(user);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UserNoDetailsDTO getUser() {
        return user;
    }

    public void setUser(UserNoDetailsDTO user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id='" + code + '\'' +
                ", user='" + user + '\'' +
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
        for(StructureServiceDTO service : services)
            ret += "\n    " + service.getName() + ", " + service.getRate();
        return ret;
    }

    public Document toBsonDocument(){
        Document document = new Document();
        document.append("code", code);
        document.append("user", user.toBsonDocument());
        document.append("status", status);
        document.append("creationDate", creationDate);
        document.append("confirmationDate", confirmationDate);
        document.append("bookingDate", bookingDate);
        document.append("year", year);
        document.append("month", month);

        List<Document> services = new ArrayList<>();
        for(StructureServiceDTO service : this.services)
            services.add(service.toBsonDocument());
        document.append("services", services);

        document.append("total", total);

        return document;
    }
}
