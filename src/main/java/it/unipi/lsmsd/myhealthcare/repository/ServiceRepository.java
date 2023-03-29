package it.unipi.lsmsd.myhealthcare.repository;

import it.unipi.lsmsd.myhealthcare.dto.ServiceDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServiceRepository extends MongoRepository<ServiceDTO, String> {
    public ServiceDTO findByName(String name);
    public ServiceDTO findByCode(String code);
}