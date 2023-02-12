package it.unipi.lsmsd.myhealthcare.dao;

import it.unipi.lsmsd.myhealthcare.model.*;
import it.unipi.lsmsd.myhealthcare.mongo.dto.UserDTO;
import it.unipi.lsmsd.myhealthcare.mongo.repository.*;
import it.unipi.lsmsd.myhealthcare.utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public static User fromMongo(UserDTO userDTO,
                                 CityRepository cityRepository,
                                 RoleRepository roleRepository,
                                 StructureRepository structureRepository,
                                 ServiceRepository serviceRepository) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setAddress(userDTO.getAddress());
        user.setZipCode(userDTO.getZipCode());
        user.setRegistrationDate(Utility.stringToDateHour(userDTO.getRegistrationDate()));
        /*user.setCity(CityDao.fromMongo(CityDao.readById(userDTO.getCity(),cityRepository),
                cityRepository));*/
        user.setCity(Utility.getCityById(userDTO.getCity()));

        for(UserDTO.UserRole userRole: userDTO.getRoles()){
            Role role = RoleDao.fromMongo(RoleDao.readById(userRole.getRole(),roleRepository));
            Structure structure = null;
            if(userRole.getStructure() != null)
                structure = StructureDao.fromMongo(
                        StructureDao.readById(userRole.getStructure(),structureRepository),
                        cityRepository,serviceRepository);
            user.addRole(role,structure);
        }
        return user;
    }

    public static User fromMongoNoRoles(UserDTO userDTO,
                                        CityRepository cityRepository) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setAddress(userDTO.getAddress());
        user.setZipCode(userDTO.getZipCode());
        user.setRegistrationDate(Utility.stringToDateHour(userDTO.getRegistrationDate()));
        /*user.setCity(CityDao.fromMongo(CityDao.readById(userDTO.getCity(),cityRepository),
                cityRepository));*/
        user.setCity(Utility.getCityById(userDTO.getCity()));
        return user;
    }

    public static UserDTO toMongo(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setAddress(user.getAddress());
        userDTO.setZipCode(user.getZipCode());
        userDTO.setRegistrationDate(Utility.dateToHourString(user.getRegistrationDate()));
        userDTO.setCity(user.getCity().getId());

        for(User.UserRole role:user.getRoles())
            if(role.getStructure() != null)
                userDTO.addRole(role.getRole().getId(),role.getStructure().getId());
            else
                userDTO.addRole(role.getRole().getId(),null);
        return userDTO;
    }

    public static void create(UserDTO object, UserRepository userRepository){
        userRepository.save(object);
    }

    public static void createMany(List<UserDTO> objects, UserRepository userRepository){
        userRepository.saveAll(objects);
    }

    public static void create(User object, UserRepository userRepository){
        userRepository.save(toMongo(object));
    }

    public static UserDTO readById(String id, UserRepository userRepository){
        return userRepository.findById(id).get();
    }

    public static UserDTO readByUsername(String username, UserRepository userRepository){
        return userRepository.findByUsername(username.toLowerCase().trim());
    }

    public static UserDTO readByUsernameAndPassword(String username, String password,
                                                    UserRepository userRepository){
        return userRepository.findByUsernameAndPassword(
                username.toLowerCase().trim(), password);
    }

    public static List<UserDTO> readAllMongo(UserRepository userRepository){
        return userRepository.findAll();
    }

    public static List<User> readAll(UserRepository userRepository,
                                     CityRepository cityRepository,
                                     RoleRepository roleRepository,
                                     StructureRepository structureRepository,
                                     ServiceRepository serviceRepository){
        List<User> objects = new ArrayList<User>();
        for(UserDTO object:readAllMongo(userRepository))
            objects.add(fromMongo(object,cityRepository,roleRepository,structureRepository,
                    serviceRepository));
        return objects;
    }

    public static void update(UserDTO object, UserRepository userRepository){
        userRepository.save(object);
    }

    public static void update(User object, UserRepository userRepository){
        userRepository.save(toMongo(object));
    }

    public static void delete(UserDTO object, UserRepository userRepository){
        userRepository.delete(object);
    }

    public static void delete(User object, UserRepository userRepository){
        userRepository.delete(toMongo(object));
    }

    public static Long size(UserRepository userRepository){
        return userRepository.count();
    }
}
