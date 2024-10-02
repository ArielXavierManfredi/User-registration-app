package com.arielxaviermanfredi.user_registration_app.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        if(foundUser!=null) {
            return foundUser;
        }
        return null;
    }

    public User getUserById(UUID id) {
        User foundUser = userRepository.findById(id).get();

        if(foundUser!=null) {
            return foundUser;
        }
        return null;
    }

    public User getUserFull(String name, String email, String password) {
        User foundUser = userRepository.findByNameIgnoreCaseAndEmailIgnoreCaseAndPassword(name, email, password);

        if(foundUser!=null) {
            return foundUser;
        }
        return null;
    }

    public String deleteUser(UUID id) {
        
        try {
            User existentUser = userRepository.findById(id).get();
            
            if(existentUser==null){ return "User with Id "+id+" doesn't exist, please check the Id and try again."; }

            userRepository.delete(existentUser);
            return "User of id \""+id+"\" deleted successfully!";
        } catch (Exception e) {
            return null;
        }
    }
}
