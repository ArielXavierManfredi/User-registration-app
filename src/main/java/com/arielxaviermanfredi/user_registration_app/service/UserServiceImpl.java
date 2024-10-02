package com.arielxaviermanfredi.user_registration_app.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.arielxaviermanfredi.user_registration_app.model.User;
import com.arielxaviermanfredi.user_registration_app.model.UserRole;
import com.arielxaviermanfredi.user_registration_app.repository.UserRepository;

@Service
public class UserServiceImpl implements UserServiceInterface, UserDetailsService {
    
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveUser(User user) {
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(UserRole.USER);
        userRepository.save(user);
    }

    @Override
    public List<Object> isUserPresent(User user) {
        boolean userExists = false;
        String message = null;
        User existingUserLogin = userRepository.findByNameIgnoreCase(user.getName());
        if (existingUserLogin == null){
            throw new UsernameNotFoundException(String.format("USER_NOT_FOUND", user.getName()));
        }
        if(existingUserLogin.isEnabled()){
            userExists = true;
            message = "Login Already Present!";
        }
        User existingUserPassword = userRepository.findByPassword(user.getPassword());
        if(existingUserPassword.isEnabled()){
            userExists = true;
            message = "Password Already Present!";
        }
        if (existingUserLogin.isEnabled() && existingUserPassword.isEnabled()) {
            message = "Login and Password Both Already Present!";
        }
        System.out.println("existingUserLogin.isEnabled() - "+existingUserLogin.isEnabled()+"existingUserPassword.isEnabled() - "+existingUserPassword.isEnabled());
        return Arrays.asList(userExists, message);
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByNameIgnoreCase(login);
        if(user==null){
            new UsernameNotFoundException("USER_NOT_FOUND".formatted(login));
        }
        return user;
    }
}
