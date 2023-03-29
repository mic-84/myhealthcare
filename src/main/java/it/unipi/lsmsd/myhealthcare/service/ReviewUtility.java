package it.unipi.lsmsd.myhealthcare.service;

import it.unipi.lsmsd.myhealthcare.dao.StructureDao;
import it.unipi.lsmsd.myhealthcare.dao.UserDao;
import it.unipi.lsmsd.myhealthcare.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReviewUtility {

    public static boolean isPossibleReview(UserBooking booking, User user){
        if(!booking.getStatus().equals(BookingUtility.getRenderedStatus()))
            return false;
        if(user.getReviews().size() == 0)
            return true;
        List<UserReview> reviews = new ArrayList<UserReview>();
        for(UserReview review : user.getReviews())
            if(review.getStructure().getId().equals(booking.getStructure().getId()))
                reviews.add(review);
        Collections.sort(reviews, new Comparator<UserReview>() {
            @Override
            public int compare(UserReview o1, UserReview o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        if(reviews.size() == 0)
            return true;
        System.out.println(reviews.get(0));
        return reviews.get(0).getDate().compareTo(booking.getBookingDate()) < 0;
    }

    public static void saveReview(
            Review review, Structure structure, User user){
        StructureReview structureReview = toStructureReview(review, user);
        structure.addReview(structureReview);

        UserReview userReview = toUserReview(review, structure);
        user.addReview(userReview);

        StructureDao.saveNewReview(structureReview, structure);
        UserDao.saveNewReview(userReview, user);
    }

    public static UserReview toUserReview(Review review, Structure structure){
        UserReview userReview = new UserReview();
        userReview.setDate(review.getDate());
        userReview.setRating(review.getRating());
        userReview.setText(review.getText());
        userReview.setStructure(structure);

        return userReview;
    }

    public static StructureReview toStructureReview(Review review, User user){
        StructureReview structureReview = new StructureReview();
        structureReview.setDate(review.getDate());
        structureReview.setRating(review.getRating());
        structureReview.setText(review.getText());
        structureReview.setUser(user);

        return structureReview;
    }

    public static List<StructureReview> sortStructureReviews(List<StructureReview> reviews, boolean ascending){
        Collections.sort(reviews, new Comparator<StructureReview>() {
            @Override
            public int compare(StructureReview o1, StructureReview o2) {
                if (ascending)
                    return o1.getDate().compareTo(o2.getDate());
                else
                    return o2.getDate().compareTo(o1.getDate());
            }
        });
        return reviews;
    }

    public static List<UserReview> sortUserReviews(List<UserReview> reviews, boolean ascending){
        Collections.sort(reviews, new Comparator<UserReview>() {
            @Override
            public int compare(UserReview o1, UserReview o2) {
                if (ascending)
                    return o1.getDate().compareTo(o2.getDate());
                else
                    return o2.getDate().compareTo(o1.getDate());
            }
        });
        return reviews;
    }

    public static String getGraphicRating(int rating, boolean html){
        String graphic = "";
        if(html) {
            for (int i = 0; i < rating; i++)
                graphic += "&#9733;";
        }
        for(int i=0; i<rating; i++)
            graphic += GraphicUtility.getStar(16);
        for(int i=rating+1; i<=5; i++)
            graphic += GraphicUtility.getEmptyStar(16);
        return graphic;
    }
}