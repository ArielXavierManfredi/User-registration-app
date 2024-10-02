package com.arielxaviermanfredi.user_registration_app.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.arielxaviermanfredi.user_registration_app.model.User;
import com.arielxaviermanfredi.user_registration_app.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User saveUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            return null;
        }
    }

    public User getUser(String name, String email) {
        User foundUser = userRepository.findByNameIgnoreCaseOrEmailIgnoreCase(name, email);

        if (foundUser != null) {
            return foundUser;
        }
        return null;
    }

    public User getUserById(UUID id) {
        User foundUser = userRepository.findById(id).get();

        if (foundUser != null) {
            return foundUser;
        }
        return null;
    }

    public User getUserFull(String name, String email, String password) {
        User foundUser = userRepository.findByNameIgnoreCaseAndEmailIgnoreCaseAndPassword(name, email, password);

        if (foundUser != null) {
            return foundUser;
        }
        return null;
    }

    public String deleteUser(Model model, UUID id) {
        
        if (id==null) {
            model.addAttribute("page", "user-listing");
            model.addAttribute("return_type", "error");
            model.addAttribute("return_message", "Can't delete an user without an Id...");
            return "index";
        }
        
        try {
            User existentUser = userRepository.findById(id).get();

            userRepository.delete(existentUser);
            model.addAttribute("page", "user-listing");
            model.addAttribute("return_type", "success");
            model.addAttribute("return_message", "User of id " + id + " deleted successfully!");
            return "index";
        } catch (Exception e) {
            model.addAttribute("page", "user-listing");
            model.addAttribute("return_type", "error");
            model.addAttribute("return_message", "Could not delete the user with Id " + id + ", please check the Id and try again.");
            return "index";
        }
    }

    public String addUser(Model model, User user) {
        System.out.println("---------------- Add -------------");
        System.out.println(user.getId());
        System.out.println(user);

        User existentUser = getUserFull(user.getName(), user.getEmail(), user.getPassword());

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
        }
        
        if (user.getName() == null) {
            model.addAttribute("page", "user-registration");
            model.addAttribute("return_type", "error");
            model.addAttribute("return_message", "Name is needed");
            return "index";
        }

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        User savedUser = saveUser(user);

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

    public String editUser(Model model, User user) {
        System.out.println("---------------- Edit -------------");
        System.out.println(user.getId());
        System.out.println(user);

        User existentUser = getUserById(user.getId());

        if (user.getName() == null) {
            model.addAttribute("user", user);
            model.addAttribute("page", "user-registration");
            model.addAttribute("return_type", "error");
            model.addAttribute("return_message", "Name is needed");
            return "index";
        }

        // Check if the password has been changed.
        if (user.getPassword() == null) {
            user.setPassword(existentUser.getPassword());

        } else {
            if (user.getPassword() != existentUser.getPassword()) {
                user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            }

            User savedUser = saveUser(user);
            if (savedUser != null) {
                model.addAttribute("user", savedUser);
                model.addAttribute("page", "user-registration");
                model.addAttribute("return_type", "success");
                model.addAttribute("return_message", "User " + savedUser.getName() + " saved successfully!");
                return "index";
            }
        }

        model.addAttribute("page", "user-registration");
        model.addAttribute("return_type", "error");
        model.addAttribute("return_message", "Could not edit the user " + user.getName() + ".");
        return "index";
    }
}
