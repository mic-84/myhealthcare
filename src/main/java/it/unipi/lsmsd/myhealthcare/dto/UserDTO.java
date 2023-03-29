package it.unipi.lsmsd.myhealthcare.dto;

import it.unipi.lsmsd.myhealthcare.service.BookingUtility;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection="User")
public class UserDTO extends AbstractUserDTO {
    @Id
    private String id;
    private String password;
    private Date registrationDate;
    private List<UserRoleDTO> roles; // if role == admin -> Structure = null
    private List<UserBookingDTO> bookings;
    private List<UserReviewDTO> reviews;
    protected Integer numberOfRenderedBookings;
    protected Float costOfRenderedBookings;

    public UserDTO(){
        roles = new ArrayList<UserRoleDTO>();
        bookings = new ArrayList<UserBookingDTO>();
        reviews = new ArrayList<UserReviewDTO>();
        numberOfRenderedBookings = 0;
        costOfRenderedBookings = 0f;
    }

    public UserDTO(String username, String password, String firstName,
                   String lastName, String email, String phoneNumber,
                   CityNoDetailsDTO city, String address, String zipCode, Date registrationDate){
        super(username, firstName, lastName,
                email, phoneNumber, city, address, zipCode);
        this.password = password;
        this.registrationDate = registrationDate;
        roles = new ArrayList<UserRoleDTO>();
        bookings = new ArrayList<UserBookingDTO>();
        reviews = new ArrayList<UserReviewDTO>();
        numberOfRenderedBookings = 0;
        costOfRenderedBookings = 0f;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public List<UserRoleDTO> getRoles() {
        return roles;
    }

    public void addRole(String role, StructureNoDetailsDTO structure){
        roles.add(new UserRoleDTO(role, structure));
    }

    public List<UserBookingDTO> getBookings() {
        return bookings;
    }

    public void addBooking(UserBookingDTO booking){
        bookings.add(booking);
        if(booking.getStatus().equals(BookingUtility.getRenderedStatus())) {
            numberOfRenderedBookings++;
            costOfRenderedBookings += booking.getTotal();
        }
    }

    public List<UserReviewDTO> getReviews() {
        return reviews;
    }

    public void setReviews(List<UserReviewDTO> reviews) {
        this.reviews = reviews;
    }

    public void addReview(UserReviewDTO review){
        reviews.add(review);
    }

    public Integer getNumberOfRenderedBookings() {
        return numberOfRenderedBookings;
    }

    public Float getCostOfRenderedBookings() {
        return costOfRenderedBookings;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", registrationDate=" + registrationDate +
                ", roles=" + roles.size() +
                ", numberOfRenderedBookings=" + numberOfRenderedBookings +
                ", costOfRenderedBookings=" + costOfRenderedBookings +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", city=" + city +
                '}';
    }
}
