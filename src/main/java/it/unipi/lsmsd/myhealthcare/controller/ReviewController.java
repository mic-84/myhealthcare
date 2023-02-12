package it.unipi.lsmsd.myhealthcare.controller;

import it.unipi.lsmsd.myhealthcare.dao.ReviewDao;
import it.unipi.lsmsd.myhealthcare.dao.StructureDao;
import it.unipi.lsmsd.myhealthcare.model.*;
import it.unipi.lsmsd.myhealthcare.mongo.repository.*;
import it.unipi.lsmsd.myhealthcare.userSession.UserSession;
import it.unipi.lsmsd.myhealthcare.utility.Utility;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReviewController {
    private final StructureRepository STRUCTURE_REPOSITORY;
    private final CityRepository CITY_REPOSITORY;
    private final UserRepository USER_REPOSITORY;
    private final ReviewRepository REVIEW_REPOSITORY;

    public ReviewController(
            StructureRepository structureRepository,
            CityRepository cityRepository,
            UserRepository userRepository,
            ReviewRepository reviewRepository){
        this.STRUCTURE_REPOSITORY = structureRepository;
        this.CITY_REPOSITORY = cityRepository;
        this.USER_REPOSITORY = userRepository;
        this.REVIEW_REPOSITORY = reviewRepository;
    }

    @PostMapping("/toMyReviews")
    public String toMyReviews(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            ModelMap model) {
        System.out.println("ReviewController.toMyReviews");
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        System.out.println("START search user reviews " + Utility.getNow());
        model.put("list", Utility.sortReviews(
                ReviewDao.readByUser(user, REVIEW_REPOSITORY, STRUCTURE_REPOSITORY, CITY_REPOSITORY),false));
        System.out.println("END search user reviews " + Utility.getNow());
        model.addAttribute("user", user);
        return "user_reviews";
    }

    @PostMapping("/toStructureReviews")
    public String toStructureReviews(
            @RequestParam("structureId") String structureId,
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            ModelMap model) {
        System.out.println("ReviewController.toStructureReviews " + structureId);
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        Structure structure = StructureDao.fromMongoNoServices(
                StructureDao.readById(structureId, STRUCTURE_REPOSITORY),
                CITY_REPOSITORY);
        model.put("list", Utility.sortReviews(ReviewDao.readByStructure(
                structure, REVIEW_REPOSITORY, USER_REPOSITORY, CITY_REPOSITORY),false));
        model.addAttribute("structureName", structure.getName());
        model.addAttribute("user", user);
        return "structure_reviews";
    }
}
