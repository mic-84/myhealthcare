package it.unipi.lsmsd.myhealthcare.mongo.repository;

import it.unipi.lsmsd.myhealthcare.mongo.dto.BookingStatusDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookingStatusRepository extends MongoRepository<BookingStatusDTO, String> {

}