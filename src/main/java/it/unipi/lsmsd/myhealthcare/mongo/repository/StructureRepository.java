package it.unipi.lsmsd.myhealthcare.mongo.repository;

import it.unipi.lsmsd.myhealthcare.mongo.dto.StructureDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StructureRepository extends MongoRepository<StructureDTO, String> {
    public List<StructureDTO> findByName(String name);
    public StructureDTO findByStructureCode(String structureCode);
    public List<StructureDTO> findByCity(String cityId);
    public List<StructureDTO> findByRegion(String region);
}