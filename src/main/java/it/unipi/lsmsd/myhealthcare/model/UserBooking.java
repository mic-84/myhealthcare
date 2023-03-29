package it.unipi.lsmsd.myhealthcare.model;

public class UserBooking extends Booking {
    private Structure structure;

    public UserBooking(){
        super();
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    @Override
    public String toString() {
        return "UserBooking{" +
                "structure=" + structure.getName() +
                ", code='" + code + '\'' +
                ", status='" + status + '\'' +
                ", creationDate=" + creationDate +
                ", confirmationDate=" + confirmationDate +
                ", bookingDate=" + bookingDate +
                ", year=" + year +
                ", month=" + month +
                ", services=" + services.size() +
                ", total=" + total +
                '}';
    }
}
