package it.unipi.lsmsd.myhealthcare.dto;

public abstract class AbstractUserDTO {
    protected String username, firstName, lastName, email, phoneNumber, address, zipCode;
    protected CityNoDetailsDTO city;

    public AbstractUserDTO(){}

    public AbstractUserDTO(String username, String firstName,
                           String lastName, String email, String phoneNumber,
                           CityNoDetailsDTO city, String address, String zipCode){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.address = address;
        this.zipCode = zipCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public CityNoDetailsDTO getCity() {
        return city;
    }

    public void setCity(CityNoDetailsDTO city) {
        this.city = city;
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
}
