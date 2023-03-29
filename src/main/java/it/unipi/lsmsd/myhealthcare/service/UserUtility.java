package it.unipi.lsmsd.myhealthcare.service;

import it.unipi.lsmsd.myhealthcare.dao.UserDao;
import it.unipi.lsmsd.myhealthcare.repository.UserRepository;

public class UserUtility {
    public static String getAdminRole(){
        return "admin";
    }

    public static String getEmployeeRole(){
        return "employee";
    }

    public static boolean existsUsername(String username, UserRepository userRepository){
        return UserDao.readByUsername(username, userRepository) != null;
    }
}