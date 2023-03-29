package it.unipi.lsmsd.myhealthcare.dto;

import org.bson.Document;
import org.bson.types.ObjectId;

public class UserNoDetailsDTO extends AbstractUserDTO {
    private String id;

    public UserNoDetailsDTO(){}

    public UserNoDetailsDTO(String username, String firstName,
                            String lastName, String email, String phoneNumber,
                            CityNoDetailsDTO city, String address, String zipCode){
        super(username, firstName, lastName,
                email, phoneNumber, city, address, zipCode);
    }

    public UserNoDetailsDTO(UserDTO user){
        super(user.getUsername(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getPhoneNumber(),
                user.getCity(), user.getAddress(), user.getZipCode());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }

    public Document toBsonDocument() {
        Document document = new Document();
        document.append("_id", new ObjectId(id));
        document.append("username", username);
        document.append("firstName", firstName);
        document.append("lastName", lastName);
        document.append("email", email);
        document.append("phoneNumber", phoneNumber);
        document.append("city", city.toBsonDocument());
        document.append("address", address);
        document.append("zipCode", zipCode);

        return document;
    }
}
