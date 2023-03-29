package it.unipi.lsmsd.myhealthcare.dto;

import org.bson.Document;

import java.util.*;

public class UserBookingDTO extends AbstractBookingDTO {
    private String code;
    private StructureNoDetailsDTO structure;

    public UserBookingDTO(){}
    public UserBookingDTO(StructureNoDetailsDTO structure, String status,
                          Date creationDate, Date confirmationDate, Date bookingDate) {
        super(status, creationDate, confirmationDate, bookingDate);
        this.structure = structure;
    }

    public UserBookingDTO(StructureBookingDTO booking, StructureDTO structure) {
        super(booking.getStatus(), booking.getCreationDate(),
                booking.getConfirmationDate(), booking.getBookingDate());
        this.code = booking.getCode();
        this.structure = new StructureNoDetailsDTO(structure);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public StructureNoDetailsDTO getStructure() {
        return structure;
    }

    public void setStructure(StructureNoDetailsDTO structure) {
        this.structure = structure;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id='" + code + '\'' +
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
        for(StructureServiceDTO service : services)
            ret += "\n    " + service.getName() + ", " + service.getRate();
        return ret;
    }

    public Document toBsonDocument(){
        Document document = new Document();
        document.append("code", code);
        document.append("structure", structure.toBsonDocument());
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
