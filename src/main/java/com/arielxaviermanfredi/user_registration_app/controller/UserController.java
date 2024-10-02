package com.arielxaviermanfredi.user_registration_app.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.arielxaviermanfredi.user_registration_app.model.User;
import com.arielxaviermanfredi.user_registration_app.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService service;

    @GetMapping({ "/register", "/edit" })
    public String addUser(Model model, @RequestParam(required = false) UUID id) {
        if (id != null) {
            User existentUser = service.getUserById(id);
            System.out.println("Edit: " + existentUser);
            model.addAttribute("user", existentUser);
        } else {
            model.addAttribute("user", new User());
        }

        model.addAttribute("page", "user-registration");
        return "index";
    }

    @GetMapping("/delete")
    public String deleteUser(Model model, @RequestParam UUID id) {
        return service.deleteUser(model, id);
    }

    @PostMapping
    public String saveUser(User user, Model model) {
        return service.addUser(model, user);
    }

}
