package it.unipi.lsmsd.myhealthcare.mongo.repository;

import it.unipi.lsmsd.myhealthcare.aggregation.BookingAggregation;
import it.unipi.lsmsd.myhealthcare.mongo.dto.BookingDTO;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookingRepository extends MongoRepository<BookingDTO, String> {
    public List<BookingDTO> findByUser(String user);
    public List<BookingDTO> findByStructure(String structure);
    public List<BookingDTO> findByStructureAndStatus(String structure, String status);

    // sum the cost and count rendered bookings per each structure
    @Aggregation(pipeline = {
            "{'$match':{'status':?0}}",
            "{'$group':{'_id':'$structure','count':{'$count':{}},'cost':{'$sum':'$total'}}}",
            "{'$sort':{'cost':-1}}"
    })
    public List<BookingAggregation> getBookingsPerStructure(String status);

    // given a year and a structure, sum the cost and count rendered bookings per each month
    @Aggregation(pipeline = {
            "{'$project':{'structure':'$structure','status':'$status','total':'$total'," +
                    "'year':{'$substr':['$bookingDate',0,4]}," +
                    "'month':{'$substr':['$bookingDate',5,2]}}}",
            "{'$match':{'status':?0,'year':?1,'structure':?2}}",
            "{'$group':{'_id':'$month','count':{'$count':{}},'cost':{'$sum':'$total'}}}",
            "{'$sort':{'_id':1}}"
    })
    public List<BookingAggregation> getBookingsPerMonthByYearAndStructure(
            String status, String year, String structure);

    // given a structure, sum the cost and count rendered bookings per each user
    @Aggregation(pipeline = {
            "{'$match':{'status':?0,'structure':?1}}",
            "{'$group':{'_id':'$user','count':{'$count':{}},'cost':{'$sum':'$total'}}}",
            "{'$sort':{'cost':-1}}"
    })
    public List<BookingAggregation> getBookingsPerUserByStructure(String status, String structure);
}