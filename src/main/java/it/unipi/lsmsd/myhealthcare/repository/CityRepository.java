package it.unipi.lsmsd.myhealthcare.repository;

import it.unipi.lsmsd.myhealthcare.dto.CityDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CityRepository extends MongoRepository<CityDTO, String> {
    public CityDTO findByCode(String code);
    public CityDTO findByName(String name);
}