package it.unipi.lsmsd.myhealthcare.controller;

import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.model.User;
import it.unipi.lsmsd.myhealthcare.session.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    public IndexController(){}

    @GetMapping("/")
    public String index(ModelMap model) {
        System.out.println("IndexController.index");
        return "index";
    }

    @PostMapping("/toHomePage")
    public String toHomePage(
            @RequestParam("userId") String userId,
            ModelMap model) {
        System.out.println("IndexController.toHomePage");
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        if (user.isEmployee())
            model.put("structure_list", user.getStructures());
        model.addAttribute("user", user);
        return "index";
    }

    @PostMapping("/toSignupPage")
    public String toSignupPage(ModelMap model) {
        System.out.println("IndexController.toSignupPage");
        model.put("city_list", MyHealthCareApplication.cities);
        return "signup";
    }

    @PostMapping("/toProfilePage")
    public String toProfilePage(
            @RequestParam("userId") String userId,
            ModelMap model) {
        System.out.println("IndexController.toProfilePage");
        User user = UserSession.getUser(userId);
        if(user == null)
            return "index";
        model.addAttribute("user", user);
        model.put("city_list", MyHealthCareApplication.cities);
        return "profile";
    }
}
