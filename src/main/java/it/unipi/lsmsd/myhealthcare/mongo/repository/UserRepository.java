package it.unipi.lsmsd.myhealthcare.mongo.repository;

import it.unipi.lsmsd.myhealthcare.mongo.dto.UserDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserDTO, String> {
    public UserDTO findByUsername(String username);
    public UserDTO findByUsernameAndPassword(String username, String password);
}