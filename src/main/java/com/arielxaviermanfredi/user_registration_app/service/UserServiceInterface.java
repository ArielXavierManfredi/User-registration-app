package com.arielxaviermanfredi.user_registration_app.service;

import java.util.List;

import com.arielxaviermanfredi.user_registration_app.model.User;

public interface UserServiceInterface {
    
    public void saveUser(User user);
    public List<Object> isUserPresent(User user);
}
