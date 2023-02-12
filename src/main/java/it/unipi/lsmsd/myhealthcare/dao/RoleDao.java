package it.unipi.lsmsd.myhealthcare.dao;

import it.unipi.lsmsd.myhealthcare.model.Role;
import it.unipi.lsmsd.myhealthcare.mongo.dto.RoleDTO;
import it.unipi.lsmsd.myhealthcare.mongo.repository.RoleRepository;

import java.util.ArrayList;
import java.util.List;

public class RoleDao {
    public static Role fromMongo(RoleDTO roleDTO) {
        Role role = new Role();
        role.setId(roleDTO.getId());
        role.setDescription(roleDTO.getDescription());
        return role;
    }

    public static RoleDTO toMongo(Role role){
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setDescription(role.getDescription());
        return roleDTO;
    }

    public static void create(RoleDTO object, RoleRepository roleRepository){
        roleRepository.save(object);
    }

    public static void create(Role object, RoleRepository roleRepository){
        roleRepository.save(toMongo(object));
    }

    public static RoleDTO readById(String id, RoleRepository roleRepository){
        return roleRepository.findById(id).get();
    }

    public static RoleDTO readByDescription(String description, RoleRepository roleRepository){
        return roleRepository.findByDescription(description);
    }

    public static List<RoleDTO> readAllMongo(RoleRepository roleRepository){
        return roleRepository.findAll();
    }

    public static List<Role> readAll(RoleRepository roleRepository){
        List<Role> objects = new ArrayList<Role>();
        for(RoleDTO object:readAllMongo(roleRepository))
            objects.add(fromMongo(object));
        return objects;
    }

    public static void update(RoleDTO object, RoleRepository roleRepository){
        roleRepository.save(object);
    }

    public static void update(Role object, RoleRepository roleRepository){
        roleRepository.save(toMongo(object));
    }

    public static void delete(RoleDTO object, RoleRepository roleRepository){
        roleRepository.delete(object);
    }

    public static void delete(Role object, RoleRepository roleRepository){
        roleRepository.delete(toMongo(object));
    }

    public static Long size(RoleRepository roleRepository){
        return roleRepository.count();
    }
}
