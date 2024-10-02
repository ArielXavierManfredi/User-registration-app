package com.arielxaviermanfredi.user_registration_app.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arielxaviermanfredi.user_registration_app.model.User;




public interface UserRepository extends JpaRepository<User, UUID>{
    User findByNameIgnoreCase(String name);
    User findByEmailIgnoreCase(String email);
    User findByNameIgnoreCaseOrEmailIgnoreCase(String name, String email);
    User findByNameIgnoreCaseAndEmailIgnoreCaseAndPassword(String name, String email, String password);

    //Login
    User findByNameIgnoreCaseAndPassword(String name, String password);
    User findByPassword(String password);
    User findByEmailIgnoreCaseAndPassword(String email, String password);
}
