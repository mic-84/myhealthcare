package it.unipi.lsmsd.myhealthcare.mongo.repository;

import it.unipi.lsmsd.myhealthcare.mongo.dto.ServiceDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ServiceRepository extends MongoRepository<ServiceDTO, String> {
    public ServiceDTO findByName(String name);

    @Query("{'active' : true}")
    public List<ServiceDTO> findAllActive();
}