package it.unipi.lsmsd.myhealthcare.repository;

import it.unipi.lsmsd.myhealthcare.aggregation.BasicAggregation;
import it.unipi.lsmsd.myhealthcare.dto.StructureDTO;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StructureRepository extends MongoRepository<StructureDTO, String> {
    public List<StructureDTO> findByName(String name);
    public StructureDTO findByStructureCode(String structureCode);
    public List<StructureDTO> findByCityId(String id);
    public List<StructureDTO> findByRegion(String region);
    public List<StructureDTO> findByArea(String area);

    // given a structure and a year, sum the cost and count rendered bookings per each month
    @Aggregation(pipeline = {
            "{$match: {_id:?0}}",
            "{$unwind: '$bookings'}",
            "{$match: {'bookings.status':'rendered', 'bookings.year':?1}}",
            "{$group: {_id: '$bookings.month', count:{'$count':{}}, cost:{'$sum':'$bookings.total'}}}",
            "{$sort: {_id:1}}"
    })
    public List<BasicAggregation> getBookingsPerMonthByYearAndStructure(
            String structureId, Integer year);

    // given a structure, count the reviews per each rating
    @Aggregation(pipeline = {
            "{$match: {_id:?0}}",
            "{$unwind: '$reviews'}",
            "{$group: {_id: '$reviews.rating', count:{'$count':{}}}}",
            "{$sort: {_id:-1}}"
    })
    public List<BasicAggregation> getReviewsPerRatingByStructure(String structureId);

    // given a structure, compute the average of the reviews
    @Aggregation(pipeline = {
            "{$match: {_id:?0}}",
            "{$unwind: '$reviews'}",
            "{$group: {_id:null, average:{$avg:'$reviews.rating'}}}"
    })
    public Float getAverageOfReviewsByStructure(String structureId);

    // given a structure, get users with expenditure greater than an input threshold
    @Aggregation(pipeline = {
            "{$match: {_id:?0}}",
            "{$unwind: '$bookings'}",
            "{$match: {'bookings.status':'rendered'}}",
            "{$group: {_id: {$concat: [" +
                    "                  '$bookings.user.firstName', ' '," +
                    "                  '$bookings.user.lastName', ' ('," +
                    "                  '$bookings.user.username', '), '," +
                    "                  '$bookings.user.city.name', ' ('," +
                    "                  '$bookings.user.city.province', ')'" +
                    "                 ]}," +
                    "  cost:{'$sum':'$bookings.total'}}}",
            "{$project: {_id:1, cost:1, greater: {$gte: ['$cost',?1]}}}",
            "{$match: {greater:true}}",
            "{$project: {_id:1, cost:1}}",
            "{$sort: {cost:-1}}"
    })
    public List<BasicAggregation> getUsersWithExpenditureGreaterThanThreshold(
            String structureId, Float threshold);

    // given a month, count the usage of each service of the structure
    @Aggregation(pipeline = {
            "{$match: {_id:?0}}",
            "{$unwind: '$bookings'}",
            "{$unwind: '$bookings.services'}",
            "{$match: {'bookings.status':'rendered','bookings.year':?1,'bookings.month':?2}}",
            "{$group: {_id: {$concat: ['$bookings.services.code',' - ','$bookings.services.name']}," +
                    "count:{'$count':{}}}}",
            "{$sort: {count:-1}}"
    })
    public List<BasicAggregation> getServicesUsageByMonth(
            String structureId, Integer year, Integer month);

    // given a region and a month/year, get cost of the N most used structures
    @Aggregation(pipeline = {
            "{$unwind: '$bookings'}",
            "{$match: {'bookings.status':'rendered', region:?0," +
                      "'bookings.month':?1, 'bookings.year': ?2}}",
            "{$group: {_id: {$concat: [" +
                    "                   '$name', ' - '," +
                    "                   '$city.name', ' ('," +
                    "                   '$city.province', ')'" +
                    "                 ]}," +
                    "  cost:{'$sum':'$bookings.total'}}}",
            "{$sort: {cost:-1}}",
            "{$limit: ?3}"
    })
    public List<BasicAggregation> getFirstNStructureCostByRegionAndMonthAndYear(
            String region, Integer month, Integer year, Integer limit);

    // given a Region and a year, get cost and number of rendered bookings per structure
    @Aggregation(pipeline = {
            "{$unwind: '$bookings'}",
            "{$match: {'bookings.status':'rendered', region:?0, 'bookings.year': ?1}}",
            "{$group: {_id: {$concat: [" +
                    "                   '$name', ' - '," +
                    "                   '$city.name', ' ('," +
                    "                   '$city.province', ')'" +
                    "                 ]}," +
                    "  cost:{'$sum':'$bookings.total'}, count:{'$count':{}}}}}",
            "{$sort: {cost:-1}}"
    })
    public List<BasicAggregation> getBookingsPerStructureByRegionAndYear(
            String region, Integer year);
}