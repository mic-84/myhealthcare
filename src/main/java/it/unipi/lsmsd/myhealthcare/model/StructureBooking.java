package it.unipi.lsmsd.myhealthcare.model;

public class StructureBooking extends Booking {
    private User user;

    public StructureBooking(){
        super();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserBooking{" +
                "user=" + user.getUsername() +
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
