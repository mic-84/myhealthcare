package it.unipi.lsmsd.myhealthcare.controller;

import it.unipi.lsmsd.myhealthcare.dao.*;
import it.unipi.lsmsd.myhealthcare.model.*;
import it.unipi.lsmsd.myhealthcare.repository.*;
import it.unipi.lsmsd.myhealthcare.service.ReviewUtility;
import it.unipi.lsmsd.myhealthcare.session.UserSession;
import it.unipi.lsmsd.myhealthcare.service.Utility;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReviewController {
    private final StructureRepository STRUCTURE_REPOSITORY;
    private final UserRepository USER_REPOSITORY;

    public ReviewController(StructureRepository structureRepository,
                            UserRepository userRepository){
        this.STRUCTURE_REPOSITORY = structureRepository;
        this.USER_REPOSITORY = userRepository;
    }

    @PostMapping("/toMyReviews")
    public String toMyReviews(
            @RequestParam("userId") String userId,
            ModelMap model) {
        System.out.println("ReviewController.toMyReviews");
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        System.out.println("user reviews " + user.getReviews().size());
        model.put("list", ReviewUtility.sortUserReviews(user.getReviews(),false));
        model.addAttribute("user", user);
        return "user_reviews";
    }

    @PostMapping("/toStructureReviews")
    public String toStructureReviews(
            @RequestParam("structureId") String structureId,
            @RequestParam("userId") String userId,
            ModelMap model) {
        System.out.println("ReviewController.toStructureReviews " + structureId);
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        Structure structure = StructureDao.fromDTO(
                StructureDao.readById(structureId, STRUCTURE_REPOSITORY));
        model.addAttribute("structureName", structure.getName());
        model.addAttribute("structureId", structure.getId());
        System.out.println("START get average of the reviews " + Utility.getNow());
        model.addAttribute("average", Utility.roundFloat(
                StructureDao.getAverageOfReviewsByStructure(structure.getId(), STRUCTURE_REPOSITORY)));
        System.out.println("END get average of the reviews " + Utility.getNow());
        System.out.println("START group structure reviews per rating " + Utility.getNow());
        model.addAttribute("reviews",
                StructureDao.getReviewsPerRatingByStructure(structure.getId(), STRUCTURE_REPOSITORY));
        System.out.println("END group structure reviews per rating " + Utility.getNow());
        model.addAttribute("list", ReviewUtility.sortStructureReviews(structure.getReviews(), false));
        model.addAttribute("user", user);
        return "structure_reviews";
    }


    @PostMapping("/review")
    public String review(
            @RequestParam("userId") String userId,
            @RequestParam("provenance") String provenance,
            @RequestParam("bookingCode") String bookingCode,
            @RequestParam("rating") String rating,
            @RequestParam("text") String text,
            ModelMap model) {
        System.out.println("BookingController.review " + bookingCode + ", " + rating);
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        model.addAttribute("user", user);
        UserBooking booking = user.getBookingByCode(bookingCode);
        model.addAttribute("booking", booking);
        Review review = new Review();
        review.setRating(Integer.valueOf(rating));
        review.setText(text);
        review.setDate(Utility.getToday());
        ReviewUtility.saveReview(review, booking.getStructure(), user);
        user = UserDao.fromDTO(UserDao.readById(userId, USER_REPOSITORY));
        UserSession.refreshUser(user);
        model.addAttribute("message", "review successfully saved");
        return "booking_management";
    }
}
