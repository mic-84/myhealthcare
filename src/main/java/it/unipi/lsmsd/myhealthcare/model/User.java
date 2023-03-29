package it.unipi.lsmsd.myhealthcare.model;

import com.google.gson.Gson;
import it.unipi.lsmsd.myhealthcare.service.BookingUtility;
import it.unipi.lsmsd.myhealthcare.service.UserUtility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private String id;
    private String username, password, firstName, lastName, email, phoneNumber, address, zipCode;
    private City city;
    private Date registrationDate;
    private List<UserRole> roles;
    private List<UserBooking> bookings;
    private List<UserReview> reviews;
    protected Integer numberOfRenderedBookings;
    protected Float costOfRenderedBookings;

    public User(){
        roles = new ArrayList<UserRole>();
        bookings = new ArrayList<UserBooking>();
        reviews = new ArrayList<UserReview>();
        numberOfRenderedBookings = 0;
        costOfRenderedBookings = 0f;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }

    public void addRole(String role, Structure structure){
        roles.add(new UserRole(role,structure));
    }

    public List<UserBooking> getBookings() {
        return bookings;
    }

    public void setBookings(List<UserBooking> bookings) {
        this.bookings = bookings;
        for(Booking booking : bookings)
            if(booking.getStatus().equals(BookingUtility.getRenderedStatus())) {
                numberOfRenderedBookings++;
                costOfRenderedBookings += booking.getTotal();
            }
    }

    public void addBooking(UserBooking booking){
        bookings.add(booking);
        if(booking.getStatus().equals(BookingUtility.getRenderedStatus())) {
            numberOfRenderedBookings++;
            costOfRenderedBookings += booking.getTotal();
        }
    }

    public UserBooking getBookingByCode(String bookingCode){
        for(UserBooking booking : bookings)
            if(booking.getCode().equals(bookingCode))
                return booking;
        return null;
    }

    public List<UserReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<UserReview> reviews) {
        this.reviews = reviews;
    }

    public void addReview(UserReview review){
        reviews.add(review);
    }

    public Integer getNumberOfRenderedBookings() {
        return numberOfRenderedBookings;
    }

    public void setNumberOfRenderedBookings(Integer numberOfRenderedBookings) {
        this.numberOfRenderedBookings = numberOfRenderedBookings;
    }

    public Float getCostOfRenderedBookings() {
        return costOfRenderedBookings;
    }

    public void setCostOfRenderedBookings(Float costOfRenderedBookings) {
        this.costOfRenderedBookings = costOfRenderedBookings;
    }

    @Override
    public String toString() {
        String ret = "User{" +
                "\n id='" + id + '\'' +
                ",\n  username='" + username + '\'' +
                ",\n  password='" + password + '\'' +
                ",\n  firstName='" + firstName + '\'' +
                ",\n  lastName='" + lastName + '\'' +
                ",\n  email='" + email + '\'' +
                ",\n  phoneNumber='" + phoneNumber + '\'' +
                ",\n  address='" + address + '\'' +
                ",\n  zipCode='" + zipCode + '\'' +
                ",\n  city=" + city +
                ",\n  registrationDate=" + registrationDate +
                ",\n  bookings=" + numberOfRenderedBookings + " - " + costOfRenderedBookings;

        if(roles.size() > 0) {
            ret += "\n  roles: ";
            for (UserRole userRole:roles) {
                try {
                    ret += "\n    " + userRole.getRole() + " " + userRole.getStructure().getName();
                } catch(Exception e){
                    ret += "\n    " + userRole.getRole();
                }
            }
        }
        return ret + '}';
    }

    public boolean isAdmin(){
        try {
            for (UserRole role : roles)
                if (role.getRole().equals(UserUtility.getAdminRole()))
                    return true;
            return false;
        } catch (Exception e){
            return false;
        }
    }

    public boolean isEmployee(String structureId){
        try {
            for (UserRole role : roles)
                if (role.getRole().equals(UserUtility.getEmployeeRole())
                        && role.getStructure().getId().equals(structureId))
                    return true;
            return false;
        } catch (Exception e){
            return false;
        }
    }

    public boolean isEmployee(){
        int count = 0;
        try {
            for (UserRole role : roles)
                if (role.getRole().equals(UserUtility.getEmployeeRole()))
                    count++;
        } catch (Exception e){
            return false;
        }
        return count > 0;
    }

    public List<Structure> getStructures(){
        List<Structure> list = new ArrayList<Structure>();
        for(UserRole role : getRoles())
            if(role.getStructure() != null)
                list.add(role.getStructure());
        return list;
    }

    public String toJSONString(){
        return new Gson().toJson(this);
    }

    public String getShortToString(){
        return firstName + " " + lastName +  " (username: " + username + "), " + city.getName();
    }
}
