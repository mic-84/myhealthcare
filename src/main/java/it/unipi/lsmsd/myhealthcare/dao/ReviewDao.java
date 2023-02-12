package it.unipi.lsmsd.myhealthcare.dao;

import it.unipi.lsmsd.myhealthcare.model.Review;
import it.unipi.lsmsd.myhealthcare.model.Structure;
import it.unipi.lsmsd.myhealthcare.model.User;
import it.unipi.lsmsd.myhealthcare.aggregation.ReviewAggregation;
import it.unipi.lsmsd.myhealthcare.mongo.dto.ReviewDTO;
import it.unipi.lsmsd.myhealthcare.mongo.repository.*;
import it.unipi.lsmsd.myhealthcare.utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class ReviewDao {
    public static Review fromMongo(ReviewDTO reviewDTO, UserRepository userRepository,
                                   CityRepository cityRepository, RoleRepository roleRepository,
                                   StructureRepository structureRepository,
                                   ServiceRepository serviceRepository) {
        Review review = new Review();
        review.setId(reviewDTO.getId());
        review.setUser(UserDao.fromMongo(UserDao.readById(reviewDTO.getUser(),userRepository),
                cityRepository, roleRepository, structureRepository,serviceRepository));
        review.setStructure(StructureDao.fromMongo(StructureDao.readById(reviewDTO.getStructure(),
                structureRepository),cityRepository,serviceRepository));
        review.setDate(Utility.stringToDateHour(reviewDTO.getDate()));
        review.setRating(reviewDTO.getRating());
        review.setText(reviewDTO.getText());
        return review;
    }

    public static ReviewDTO toMongo(Review review){
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setUser(review.getUser().getId());
        reviewDTO.setStructure(review.getStructure().getId());
        reviewDTO.setDate(Utility.dateToHourString(review.getDate()));
        reviewDTO.setRating(review.getRating());
        reviewDTO.setText(review.getText());
        return reviewDTO;
    }

    public static void create(ReviewDTO object, ReviewRepository reviewRepository){
        reviewRepository.save(object);
    }

    public static void createMany(List<ReviewDTO> objects, ReviewRepository reviewRepository){
        reviewRepository.saveAll(objects);
    }

    public static void create(Review object, ReviewRepository reviewRepository){
        reviewRepository.save(toMongo(object));
    }

    public static ReviewDTO readById(String id, ReviewRepository reviewRepository){
        return reviewRepository.findById(id).get();
    }

    public static List<ReviewDTO> readByUserMongo(String user, ReviewRepository reviewRepository){
        return reviewRepository.findByUser(user);
    }

    public static List<ReviewDTO> readByStructureMongo(String structure, ReviewRepository reviewRepository){
        return reviewRepository.findByStructure(structure);
    }

    public static List<ReviewDTO> readMongoByUserAndStructure(
            String user, String structure, ReviewRepository reviewRepository){
        return reviewRepository.findByUserAndStructure(user, structure);
    }

    public static List<Review> readByUser(
            User user, ReviewRepository reviewRepository,
            StructureRepository structureRepository, CityRepository cityRepository){
        List<ReviewDTO> reviewsMongo = reviewRepository.findByUser(user.getId());
        List<Review> reviews = new ArrayList<Review>();
        for(ReviewDTO reviewDTO : reviewsMongo){
            Review review = new Review();
            review.setId(reviewDTO.getId());
            review.setUser(user);
            review.setStructure(StructureDao.fromMongoNoServices(
                    StructureDao.readById(reviewDTO.getStructure(), structureRepository),
                    cityRepository));
            review.setDate(Utility.stringToDate(reviewDTO.getDate()));
            review.setRating(reviewDTO.getRating());
            review.setText(reviewDTO.getText());
            reviews.add(review);
        }
        return reviews;
    }

    public static List<Review> readByStructure(
            Structure structure, ReviewRepository reviewRepository,
            UserRepository userRepository, CityRepository cityRepository){
        List<ReviewDTO> reviewsMongo = reviewRepository.findByStructure(structure.getId());
        List<Review> reviews = new ArrayList<Review>();
        for(ReviewDTO reviewDTO : reviewsMongo){
            Review review = new Review();
            review.setId(reviewDTO.getId());
            review.setUser(UserDao.fromMongoNoRoles(UserDao.readById(
                    reviewDTO.getUser(), userRepository),
                    cityRepository));
            review.setStructure(structure);
            review.setDate(Utility.stringToDate(reviewDTO.getDate()));
            review.setRating(reviewDTO.getRating());
            review.setText(reviewDTO.getText());
            reviews.add(review);
        }
        return reviews;
    }

    public static List<Review> readByUserAndStructure(
            User user, Structure structure, ReviewRepository reviewRepository){
        List<ReviewDTO> reviewsMongo = reviewRepository.findByUserAndStructure(
                user.getId(), structure.getId());
        List<Review> reviews = new ArrayList<Review>();
        for(ReviewDTO reviewDTO : reviewsMongo){
            Review review = new Review();
            review.setId(reviewDTO.getId());
            review.setUser(user);
            review.setStructure(structure);
            review.setDate(Utility.stringToDate(reviewDTO.getDate()));
            review.setRating(reviewDTO.getRating());
            review.setText(reviewDTO.getText());
            reviews.add(review);
        }
        return reviews;
    }

    public static List<ReviewDTO> readAllMongo(ReviewRepository reviewRepository){
        return reviewRepository.findAll();
    }

    public static List<Review> readAll(ReviewRepository reviewRepository,
                                       UserRepository userRepository, CityRepository cityRepository,
                                       RoleRepository roleRepository,
                                       StructureRepository structureRepository,
                                       ServiceRepository serviceRepository){
        List<Review> objects = new ArrayList<Review>();
        for(ReviewDTO object:readAllMongo(reviewRepository))
            objects.add(fromMongo(object,userRepository,cityRepository,roleRepository,
                    structureRepository,serviceRepository));
        return objects;
    }

    public static void update(ReviewDTO object, ReviewRepository reviewRepository){
        reviewRepository.save(object);
    }

    public static void update(Review object, ReviewRepository reviewRepository){
        reviewRepository.save(toMongo(object));
    }

    public static void delete(ReviewDTO object, ReviewRepository reviewRepository){
        reviewRepository.delete(object);
    }

    public static void delete(Review object, ReviewRepository reviewRepository){
        reviewRepository.delete(toMongo(object));
    }

    public static Long size(ReviewRepository reviewRepository){
        return reviewRepository.count();
    }

    public static Float getStructureReviewsRating(ReviewRepository reviewRepository, String structureId) {
        return reviewRepository.getStructureReviewsRating(structureId).getRoundAverage();
    }

    public static List<ReviewAggregation> getReviewsByStructure(
            ReviewRepository reviewRepository, String structureId){
        List<ReviewAggregation> list = reviewRepository.getReviewsByStructure(structureId);
        for(ReviewAggregation aggregation : list)
            aggregation.setGraphic();
        return list;
    }
}
