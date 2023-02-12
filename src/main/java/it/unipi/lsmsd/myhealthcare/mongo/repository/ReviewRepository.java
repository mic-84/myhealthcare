package it.unipi.lsmsd.myhealthcare.mongo.repository;

import it.unipi.lsmsd.myhealthcare.aggregation.ReviewAggregation;
import it.unipi.lsmsd.myhealthcare.aggregation.ReviewAverage;
import it.unipi.lsmsd.myhealthcare.mongo.dto.ReviewDTO;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<ReviewDTO, String> {
    public List<ReviewDTO> findByUser(String user);
    public List<ReviewDTO> findByStructure(String structure);
    public List<ReviewDTO> findByUserAndStructure(String user, String structure);

    // given a structure, count the reviews grouped by rating
    @Aggregation(pipeline = {
            "{'$match':{'structure':?0}}",
            "{'$group':{'_id':'$rating','count':{'$count':{}}}}",
            "{'$sort':{'_id':-1}}"
    })
    public List<ReviewAggregation> getReviewsByStructure(String structure);

    // given a structure, get the average of reviews rating
    @Aggregation(pipeline = {
            "{'$match':{'structure':?0}}",
            "{'$group':{'_id':null,'average':{'$avg':$rating}}}"
    })
    public ReviewAverage getStructureReviewsRating(String structure);
}