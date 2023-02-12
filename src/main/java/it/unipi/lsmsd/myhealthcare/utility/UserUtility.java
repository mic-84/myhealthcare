package it.unipi.lsmsd.myhealthcare.utility;

import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.dao.ReviewDao;
import it.unipi.lsmsd.myhealthcare.dao.UserDao;
import it.unipi.lsmsd.myhealthcare.model.*;
import it.unipi.lsmsd.myhealthcare.mongo.repository.ReviewRepository;
import it.unipi.lsmsd.myhealthcare.mongo.repository.UserRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class UserUtility {
    public static String getAdminRole(){
        return getRole("admin").getId();
    }

    public static String getEmployeeRole(){
        return getRole("employee").getId();
    }

    public static Role getRole(String description){
        for(Role Role: MyHealthCareApplication.roles){
            if(Role.getDescription().equals(description))
                return Role;
        }
        return null;
    }

    public static boolean existsUsername(String username, UserRepository userRepository){
        return UserDao.readByUsername(username, userRepository) != null;
    }

    public static boolean isPossibleReview(Booking booking,
                                           ReviewRepository reviewRepository){
        if(!booking.getStatus().getId().equals(BookingUtility.getRenderedStatus().getId()))
            return false;
        System.out.println("START search structure/user reviews " + Utility.getNow());
        List<Review> reviews = ReviewDao.readByUserAndStructure(
                booking.getUser(), booking.getStructure(), reviewRepository);
        System.out.println("END search structure/user reviews " + Utility.getNow());
        if(reviews.size() == 0)
            return true;
        Collections.sort(reviews, new Comparator<Review>() {
            @Override
            public int compare(Review o1, Review o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        System.out.println(reviews.get(0));
        return reviews.get(0).getDate().compareTo(booking.getBookingDate()) < 0;
    }
}