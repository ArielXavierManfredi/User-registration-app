package com.arielxaviermanfredi.user_registration_app.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.arielxaviermanfredi.user_registration_app.model.User;
import com.arielxaviermanfredi.user_registration_app.model.UserRole;
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
            System.out.println("Edit: "+existentUser);
            model.addAttribute("user", existentUser);
        } else {
            model.addAttribute("user", new User());
        }

        model.addAttribute("page", "user-registration");
        return "index";
    }

    @GetMapping("/delete")
    public String deleteUser(Model model, UUID userId) {
        try {
            service.deleteUser(userId);
            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @PostMapping
    public String saveUser(User user, Model model) {
        System.out.println(user);
        user.setRole(user.getRole().getRole() == "ADMIN" ? UserRole.ADMIN : UserRole.USER);
        User existentUser = service.getUserFull(user.getName(), user.getEmail(), user.getPassword());

        if (existentUser != null) {
            model.addAttribute("page", "user-registration");
            model.addAttribute("return_type", "message");
            model.addAttribute("return_message", "User already registered!");
            return "index";
        }

        if (user.getPassword() == null) {
            model.addAttribute("page", "user-registration");
            model.addAttribute("return_type", "error");
            model.addAttribute("return_message", "Can not save the user without a password!");
            return "index";
        } else if (user.getEmail() == null && user.getName() == null) {
            model.addAttribute("page", "user-registration");
            model.addAttribute("return_type", "error");
            model.addAttribute("return_message", "Name or Email is needed");
            return "index";
        }

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        User savedUser = service.saveUser(user);

        if (savedUser != null) {
            model.addAttribute("page", "user-registration");
            model.addAttribute("return_type", "success");
            model.addAttribute("return_message", "User " + savedUser.getName() + " saved successfully!");
            return "redirect:/";
        }
        model.addAttribute("page", "user-registration");
        model.addAttribute("return_type", "error");
        model.addAttribute("return_message", "Could not register the user.");
        return "index";
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<?> getUser(String name, String email) {
        if (name != null || email != null) {
            User foundUser = service.getUser(name, email);

            if (foundUser != null) {
                return ResponseEntity.ok(foundUser);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Could not find the User by the name " + name + " or the email " + email);
        }
        return ResponseEntity.badRequest().body(
                "To get an User, put an Name or Email in a Path Variable, like: http://localhost:8080/user?name=Ariel or http://localhost:8080/user?email=ariel.xavier.manfredi@gmail.com");
    }

    @PutMapping
    public String editUser(@RequestBody User user, Model model) {

        if (user.getId() == null) {
            return "Can't edit the user without an ID.";
        } else if (user.getPassword() == null) {
            return "Can't save the user without a password.";
        } else if (user.getEmail() == null && user.getName() == null) {
            return "Name or Email is needed!";
        }
        try {

            User userGotById = service.getUserById(user.getId());

            if (userGotById.getName().equals(user.getName()) &&
                    userGotById.getEmail().equals(user.getEmail()) &&
                    userGotById.getPassword().equals(user.getPassword())) {
                // return "User credentials is identical the registered one.";
                model.addAttribute("user", userGotById);
                model.addAttribute("page", "user-registration");
                model.addAttribute("return_type", "message");
                model.addAttribute("return_message", "User credentials is identical the registered one.");
                return "index";
            }

            if (userGotById != null) {

                userGotById.setName(user.getName() != userGotById.getName() ? user.getName() : userGotById.getName());
                userGotById.setEmail(user.getEmail() != userGotById.getEmail() ? user.getEmail() : userGotById.getEmail());
                userGotById.setPassword(
                        user.getPassword() != userGotById.getPassword() ? new BCryptPasswordEncoder().encode(user.getPassword())
                        : userGotById.getPassword()
                );

                User savedUser = service.saveUser(userGotById);

                if (savedUser != null) {
                    // return "User " + savedUser.getName() + " edited successfully!\n" + savedUser;
                    model.addAttribute("user", savedUser);
                    model.addAttribute("page", "user-registration");
                    model.addAttribute("return_type", "success");
                    model.addAttribute("return_message", "User " + savedUser.getName() + " edited successfully!");
                    return "index";
                }
            }
        } catch (Exception e) {
            // return "Could not edit the user " + user.getName() + ", the Id is incorrect or some credential.";
            model.addAttribute("user", user);
            model.addAttribute("page", "user-registration");
            model.addAttribute("return_type", "error");
            model.addAttribute("return_message", "Could not edit the user " + user.getName() + ", the Id is incorrect or some credential.");
            return "index";
        }
        // return "Could not edit the user " + user.getName() + ", check your credentials and try again.";
        model.addAttribute("user", user);
        model.addAttribute("page", "user-registration");
        model.addAttribute("return_type", "error");
        model.addAttribute("return_message", "Could not edit the user " + user.getName() + ", check your credentials and try again.");
        return "index";
    }

    @DeleteMapping
    @ResponseBody
    public String deleteUser(UUID id) {
        System.out.println("ID: " + id);
        if (id == null) {
            return "Can't delete an user without an Id...";
        }

        String deleteMessage = service.deleteUser(id);

        return deleteMessage != null ? deleteMessage
                : "Could not delete the user of Id \"" + id + "\" or inexistent Id.";
    }

}
