package com.arielxaviermanfredi.user_registration_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.arielxaviermanfredi.user_registration_app.model.User;
import com.arielxaviermanfredi.user_registration_app.service.LoginService;



@Controller
@RequestMapping("/login")
public class LoginController {

    

    @Autowired
    LoginService service;



    
    @GetMapping
    public String get() {
        return "login";
    }


    @PostMapping
    public String post(Model model, String name, String email, String password) {
        if (password==null) {
            // return "Oops! It looks like you forgot to enter your password. Please provide it to log-in.";
            model.addAttribute("return_type", "error");
            model.addAttribute("return_message", "Oops! It looks like you forgot to enter your password. Please provide it to log-in.");
            return "login";
        }
        if (name!=null) {
            User user = service.getUserByNameAndPassword(name, password);
            if(user!=null) {
                if (email!=null) {
                    // return ResponseEntity.ok().body("Welcome "+user.getName()+", you have successfully logged in the system using the name and password!\nYou are a cautious person, aren't you? You put both your name and password as well as your email address...");
                    model.addAttribute("return_type", "success");
                    model.addAttribute("return_message", "Welcome "+user.getName()+", you have successfully logged in the system using the name and password!");
                    return "login";
                }
                // return ResponseEntity.ok().body("Welcome "+user.getName()+", you have successfully logged in the system using the name and password!");
                model.addAttribute("return_type", "success");
                model.addAttribute("return_message", "Welcome "+user.getName()+", you have successfully logged in the system using the name and password!");
                return "login";
            } 
            // return ResponseEntity.badRequest().body("Could not find your User by Name or Password, please check your credentials and the request and try again.");
            model.addAttribute("return_type", "error");
            model.addAttribute("return_message", "Could not find your User by Name or Password, please check your credentials and the request and try again.");
            return "login";
        }
        
        // return ResponseEntity.ok().body("You need credentials to log in the system...");
        model.addAttribute("return_type", "error");
        model.addAttribute("return_message", "ou need credentials to log in the system...");
        return "login";
    }
    
    
}
