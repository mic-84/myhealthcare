package it.unipi.lsmsd.myhealthcare.controller;

import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.dao.CityDao;
import it.unipi.lsmsd.myhealthcare.dao.UserDao;
import it.unipi.lsmsd.myhealthcare.model.City;
import it.unipi.lsmsd.myhealthcare.model.User;
import it.unipi.lsmsd.myhealthcare.mongo.repository.*;
import it.unipi.lsmsd.myhealthcare.userSession.UserSession;
import it.unipi.lsmsd.myhealthcare.utility.UserUtility;
import it.unipi.lsmsd.myhealthcare.utility.Utility;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    private final UserRepository USER_REPOSITORY;
    private final CityRepository CITY_REPOSITORY;
    private final StructureRepository STRUCTURE_REPOSITORY;
    private final ServiceRepository SERVICE_REPOSITORY;
    private final RoleRepository ROLE_REPOSITORY;

    public UserController(
            UserRepository userRepository, CityRepository cityRepository,
            StructureRepository structureRepository, ServiceRepository serviceRepository,
            RoleRepository roleRepository){
        this.USER_REPOSITORY = userRepository;
        this.CITY_REPOSITORY = cityRepository;
        this.STRUCTURE_REPOSITORY = structureRepository;
        this.SERVICE_REPOSITORY = serviceRepository;
        this.ROLE_REPOSITORY = roleRepository;
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            ModelMap model
    ){
        System.out.println("UserController.login " + username);
        try {
            User user = UserDao.fromMongo(UserDao.readByUsernameAndPassword(
                            username, Utility.crypt(password), USER_REPOSITORY),
                    CITY_REPOSITORY, ROLE_REPOSITORY, STRUCTURE_REPOSITORY, SERVICE_REPOSITORY);
            System.out.println("user id " + user.getId());
            System.out.println("admin: " + user.isAdmin());
            System.out.println("employee: " + user.isEmployee());
            UserSession.login(user);
            user = UserSession.getUser(user.getId(), user.getPassword());
            model.addAttribute("user", user);
            if(user.isEmployee())
                model.put("structure_list", user.getStructures());
            return "index";
        } catch (Exception e){
            model.addAttribute("message", "wrong username or password");
            e.printStackTrace();
            return "index";
        }
    }

    @PostMapping("/signin")
    public String signin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("firstname") String firstname,
            @RequestParam("lastname") String lastname,
            @RequestParam("email") String email,
            @RequestParam("phonenumber") String phoneNumber,
            @RequestParam("city") String cityId,
            @RequestParam("address") String address,
            @RequestParam("zipcode") String zipcode,
            ModelMap model
    ){
        System.out.println("UserController.signin " + username + ", " + cityId);
        try {
            if(username.length() == 0){
                model.addAttribute("message", "the username is mandatory!");
                model.addAttribute("password", password);
                model.addAttribute("firstname", firstname);
                model.addAttribute("lastname", lastname);
                model.addAttribute("email", email);
                model.addAttribute("phonenumber", phoneNumber);
                model.addAttribute("address", address);
                model.addAttribute("zipcode", zipcode);
                model.put("city_list", MyHealthCareApplication.cities);
                return "signin";
            } else if(UserUtility.existsUsername(username, USER_REPOSITORY)){
                model.addAttribute("message", "the username already exists!");
                model.addAttribute("username", username);
                model.addAttribute("password", password);
                model.addAttribute("firstname", firstname);
                model.addAttribute("lastname", lastname);
                model.addAttribute("email", email);
                model.addAttribute("phonenumber", phoneNumber);
                model.addAttribute("address", address);
                model.addAttribute("zipcode", zipcode);
                model.put("city_list", MyHealthCareApplication.cities);
                return "signin";
            } else {
                /*City city = CityDao.fromMongo(
                        CityDao.readById(cityId, CITY_REPOSITORY), CITY_REPOSITORY);*/
                City city = Utility.getCityById(cityId);
                User user = new User();
                user.setUsername(username);
                user.setPassword(Utility.crypt(password));
                user.setFirstName(firstname);
                user.setLastName(lastname);
                user.setEmail(email);
                user.setPhoneNumber(phoneNumber);
                user.setCity(city);
                user.setAddress(address);
                user.setZipCode(zipcode);
                user.setRegistrationDate(Utility.getToday());

                UserDao.create(user, USER_REPOSITORY);

                user = UserDao.fromMongo(UserDao.readByUsername(
                                username, USER_REPOSITORY),
                        CITY_REPOSITORY, ROLE_REPOSITORY, STRUCTURE_REPOSITORY, SERVICE_REPOSITORY);
                UserSession.login(user);
                System.out.println("user id " + user.getId());
                model.addAttribute("user", user);
                return "index";
            }
        } catch (Exception e){
            model.addAttribute("message", "an error occurred during registration");
            model.addAttribute("username", username);
            model.addAttribute("password", password);
            model.addAttribute("firstname", firstname);
            model.addAttribute("lastname", lastname);
            model.addAttribute("email", email);
            model.addAttribute("phonenumber", phoneNumber);
            model.addAttribute("address", address);
            model.addAttribute("zipcode", zipcode);
            model.put("city_list", MyHealthCareApplication.cities);
            return "signin";
        }
    }

    @PostMapping("/updateProfile")
    public String updateProfile(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            @RequestParam("firstname") String firstname,
            @RequestParam("lastname") String lastname,
            @RequestParam("email") String email,
            @RequestParam("phonenumber") String phoneNumber,
            @RequestParam("city") String cityId,
            @RequestParam("address") String address,
            @RequestParam("zipcode") String zipcode,
            ModelMap model
    ){
        System.out.println("UserController.updateProfile");
        try {
            User user = UserSession.getUser(userId, cryptedPassword);
            if(user == null)
                return "index";
            /*City city = CityDao.fromMongo(
                    CityDao.readById(cityId, CITY_REPOSITORY), CITY_REPOSITORY);*/
            City city = Utility.getCityById(cityId);
            user.setFirstName(firstname);
            user.setLastName(lastname);
            user.setEmail(email);
            user.setPhoneNumber(phoneNumber);
            user.setCity(city);
            user.setAddress(address);
            user.setZipCode(zipcode);
            user.setRegistrationDate(Utility.getToday());

            UserDao.update(user, USER_REPOSITORY);
            user = UserDao.fromMongo(UserDao.readById(
                            userId, USER_REPOSITORY),
                    CITY_REPOSITORY, ROLE_REPOSITORY, STRUCTURE_REPOSITORY, SERVICE_REPOSITORY);
            UserSession.login(user);
            model.addAttribute("message", "profile correctly updated");
            model.addAttribute("user", user);
            model.put("city_list", MyHealthCareApplication.cities);
            return "profile";
        } catch (Exception e){
            User user = UserDao.fromMongo(UserDao.readById(
                            userId, USER_REPOSITORY),
                    CITY_REPOSITORY, ROLE_REPOSITORY, STRUCTURE_REPOSITORY, SERVICE_REPOSITORY);
            model.addAttribute("message", "an error occurred while saving data");
            model.addAttribute("user", user);
            model.put("city_list", MyHealthCareApplication.cities);
            return "profile";
        }
    }

    @PostMapping("/logout")
    public String logout(
            @RequestParam("userId") String userId,
            ModelMap model){
        System.out.println("UserController.logout");
        UserSession.logout(userId);
        return "index";
    }

    @PostMapping("/toChangePassword")
    public String toChangePassword(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            ModelMap model) {
        System.out.println("UserController.toChangePassword");
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        model.addAttribute("user", user);
        return "change_password";
    }

    @PostMapping("/changePassword")
    public String changePassword(
            @RequestParam("userId") String userId,
            @RequestParam("cryptedPassword") String cryptedPassword,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            ModelMap model
    ){
        System.out.println("UserController.changePassword");
        User user = UserSession.getUser(userId, cryptedPassword);
        if(user == null)
            return "index";
        try {
            if(!Utility.crypt(oldPassword).equals(user.getPassword())){
                model.addAttribute("message", "current password is wrong");
                return "change_password";
            }
            user.setPassword(Utility.crypt(newPassword));
            UserDao.update(user, USER_REPOSITORY);
            UserSession.login(user);
            model.addAttribute("message", "password successfully changed");
            model.addAttribute("user", user);
            if(user.isEmployee())
                model.put("structure_list", user.getStructures());
            return "index";
        } catch (Exception e){
            model.addAttribute("message", "an error occurred while changing password");
            return "index";
        }
    }
}
