package it.unipi.lsmsd.myhealthcare.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.databaseConnection.MongoConnectionManager;
import it.unipi.lsmsd.myhealthcare.dto.*;
import it.unipi.lsmsd.myhealthcare.model.*;
import it.unipi.lsmsd.myhealthcare.repository.UserRepository;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public static User fromDTO(AbstractUserDTO abstractUserDTO) {
        User user = new User();
        user.setUsername(abstractUserDTO.getUsername());
        user.setFirstName(abstractUserDTO.getFirstName());
        user.setLastName(abstractUserDTO.getLastName());
        user.setEmail(abstractUserDTO.getEmail());
        user.setPhoneNumber(abstractUserDTO.getPhoneNumber());
        user.setAddress(abstractUserDTO.getAddress());
        user.setZipCode(abstractUserDTO.getZipCode());
        user.setCity(CityDao.fromDTO(abstractUserDTO.getCity()));

        if(abstractUserDTO instanceof UserDTO) {
            UserDTO userDTO = (UserDTO)abstractUserDTO;
            user.setId(userDTO.getId());
            user.setPassword(userDTO.getPassword());
            user.setRegistrationDate(userDTO.getRegistrationDate());

            for(UserRoleDTO userRole : userDTO.getRoles()){
                if(userRole.getStructure() != null)
                    user.addRole(userRole.getRole(), StructureDao.fromDTO(userRole.getStructure()));
                else
                    user.addRole(userRole.getRole(), null);
            }

            for(UserBookingDTO bookingDTO : userDTO.getBookings())
                user.addBooking(fromUserBookingDTO(bookingDTO));

            for(UserReviewDTO reviewDTO : userDTO.getReviews())
                user.addReview(fromUserReviewDTO(reviewDTO));
        } else if(abstractUserDTO instanceof UserNoDetailsDTO)
            user.setId(((UserNoDetailsDTO)abstractUserDTO).getId());

        return user;
    }

    public static UserDTO toDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setAddress(user.getAddress());
        userDTO.setZipCode(user.getZipCode());
        userDTO.setRegistrationDate(user.getRegistrationDate());
        userDTO.setCity(CityDao.toDTONoDetails(user.getCity()));

        if(user.getRoles().size() > 0)
            for(UserRole role:user.getRoles()) {
                if (role.getStructure() != null)
                    userDTO.addRole(role.getRole(), StructureDao.toDTONoDetails(role.getStructure()));
                else
                    userDTO.addRole(role.getRole(), null);
            }

        if(user.getBookings().size() > 0)
            for(UserBooking booking : user.getBookings())
                userDTO.addBooking(toUserBookingDTO(booking));

        if(user.getReviews().size() > 0)
            for(UserReview review : user.getReviews())
                userDTO.addReview(toUserReviewDTO(review));

        return userDTO;
    }

    public static UserNoDetailsDTO toDTONoDetails(User user) {
        UserNoDetailsDTO userDTO = new UserNoDetailsDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setAddress(user.getAddress());
        userDTO.setZipCode(user.getZipCode());
        userDTO.setCity(CityDao.toDTONoDetails(user.getCity()));

        return userDTO;
    }

    public static void create(UserDTO object, UserRepository userRepository){
        userRepository.save(object);
    }

    public static void createMany(List<UserDTO> objects, UserRepository userRepository){
        userRepository.saveAll(objects);
    }

    public static void create(User object, UserRepository userRepository){
        userRepository.save(toDTO(object));
    }

    public static UserDTO readById(String id, UserRepository userRepository){
        return userRepository.findById(id).get();
    }

    public static UserDTO readByUsername(String username, UserRepository userRepository){
        return userRepository.findByUsername(username.toLowerCase().trim());
    }

    public static UserDTO readByUsernameAndPassword(String username, String password,
                                                    UserRepository userRepository){
        return userRepository.findByUsernameAndPassword(
                username.toLowerCase().trim(), password);
    }

    public static List<UserDTO> readAllDTO(UserRepository userRepository){
        return userRepository.findAll();
    }

    public static List<User> readAll(UserRepository userRepository){
        List<User> objects = new ArrayList<User>();
        for(UserDTO object: readAllDTO(userRepository))
            objects.add(fromDTO(object));
        return objects;
    }

    public static void update(UserDTO object, UserRepository userRepository){
        userRepository.save(object);
    }

    public static void update(User object, UserRepository userRepository){
        userRepository.save(toDTO(object));
    }

    public static void delete(UserDTO object, UserRepository userRepository){
        userRepository.delete(object);
    }

    public static void delete(User object, UserRepository userRepository){
        userRepository.delete(toDTO(object));
    }

    public static Long size(UserRepository userRepository){
        return userRepository.count();
    }

    public static void saveNewBooking(UserBooking booking, User user){
        System.out.println(MyHealthCareApplication.mongo.getUserCollection().updateOne(
                Filters.eq("_id", new ObjectId(user.getId())),
                Updates.push("bookings", toUserBookingDTO(booking).toBsonDocument())));
    }

    public static void updateBookingStatus(StructureBooking booking) {
        System.out.println(MyHealthCareApplication.mongo.getUserCollection().updateOne(
                new BasicDBObject("bookings.code", booking.getCode()),
                new BasicDBObject("$set", new BasicDBObject("bookings.$.status", booking.getStatus()))));
    }

    public static void updateBookingDate(StructureBooking booking) {
        System.out.println(MyHealthCareApplication.mongo.getUserCollection().updateOne(
                new BasicDBObject("bookings.code", booking.getCode()),
                new BasicDBObject("$set", new BasicDBObject("bookings.$.bookingDate", booking.getBookingDate()))));
    }

    public static void updateBookingConfirmationDate(StructureBooking booking) {
        System.out.println(MyHealthCareApplication.mongo.getUserCollection().updateOne(
                new BasicDBObject("bookings.code", booking.getCode()),
                new BasicDBObject("$set", new BasicDBObject("bookings.$.confirmationDate", booking.getConfirmationDate()))));
    }
    public static UserBookingDTO toUserBookingDTO(UserBooking booking){
        UserBookingDTO bookingDTO = new UserBookingDTO();
        bookingDTO.setCode(booking.getCode());
        bookingDTO.setStructure(StructureDao.toDTONoDetails(booking.getStructure()));
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setCreationDate(booking.getCreationDate());
        bookingDTO.setConfirmationDate(booking.getConfirmationDate());
        bookingDTO.setBookingDate(booking.getBookingDate());
        bookingDTO.setYear(booking.getYear());
        bookingDTO.setMonth(booking.getMonth());
        for(StructureService service : booking.getServices()){
            bookingDTO.addService(ServiceDao.toStructureDTO(service));
        }
        bookingDTO.setTotal(booking.getTotal());

        return bookingDTO;
    }

    public static UserBooking fromUserBookingDTO(UserBookingDTO bookingDTO){
        UserBooking booking = new UserBooking();
        booking.setCode(bookingDTO.getCode());
        booking.setStructure(StructureDao.fromDTO(bookingDTO.getStructure()));
        booking.setStatus(bookingDTO.getStatus());
        booking.setCreationDate(bookingDTO.getCreationDate());
        booking.setConfirmationDate(bookingDTO.getConfirmationDate());
        booking.setBookingDate(bookingDTO.getBookingDate());
        booking.setYear(bookingDTO.getYear());
        booking.setMonth(bookingDTO.getMonth());
        for(StructureServiceDTO structureServiceDTO : bookingDTO.getServices()){
            booking.addService(ServiceDao.fromDTOtoStructureService(structureServiceDTO));
        }
        booking.setTotal(bookingDTO.getTotal());

        return booking;
    }

    public static void saveNewReview(UserReview review, User user){
        MongoConnectionManager mongo = MyHealthCareApplication.mongo;
        System.out.println(mongo.getUserCollection().updateOne(
                Filters.eq("_id", new ObjectId(user.getId())),
                Updates.push("reviews", toUserReviewDTO(review).toBsonDocument())));
    }

    public static UserReviewDTO toUserReviewDTO(UserReview review){
        UserReviewDTO reviewDTO = new UserReviewDTO();
        reviewDTO.setStructure(StructureDao.toDTONoDetails(review.getStructure()));
        reviewDTO.setDate(review.getDate());
        reviewDTO.setRating(review.getRating());
        reviewDTO.setText(review.getText());

        return reviewDTO;
    }

    public static UserReview fromUserReviewDTO(UserReviewDTO reviewDTO){
        UserReview review = new UserReview();
        review.setStructure(StructureDao.fromDTO(reviewDTO.getStructure()));
        review.setDate(reviewDTO.getDate());
        review.setRating(reviewDTO.getRating());
        review.setText(reviewDTO.getText());

        return review;
    }
}
