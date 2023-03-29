package it.unipi.lsmsd.myhealthcare.dto;

import org.bson.Document;

import java.util.Date;

public class UserReviewDTO extends AbstractReviewDTO {
    private StructureNoDetailsDTO structure;

    public UserReviewDTO(){}

    public UserReviewDTO(StructureNoDetailsDTO structure, Integer rating,
                         Date date, String text) {
        super(rating, date, text);
        this.structure = structure;
    }

    public StructureNoDetailsDTO getStructure() {
        return structure;
    }

    public void setStructure(StructureNoDetailsDTO structure) {
        this.structure = structure;
    }

    @Override
    public String toString() {
        return "Review{" +
                ", structure='" + structure + '\'' +
                ", date='" + date + '\'' +
                ", rating=" + rating +
                ", text=" + text +
                '}';
    }

    public Document toBsonDocument(){
        Document document = new Document();
        document.append("structure", structure.toBsonDocument());
        document.append("rating", rating);
        document.append("text", text);
        document.append("date", date);

        return document;
    }
}
