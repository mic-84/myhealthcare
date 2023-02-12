package it.unipi.lsmsd.myhealthcare.mongo.repository;

import it.unipi.lsmsd.myhealthcare.mongo.dto.CityDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CityRepository extends MongoRepository<CityDTO, String> {
    public CityDTO findByCode(String code);
    public CityDTO findByName(String name);
}