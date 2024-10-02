package com.arielxaviermanfredi.user_registration_app.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication) 
        throws IOException, ServletException {

        // Custom actions upon logout (e.g., redirect to a specific page)
        response.sendRedirect("/"); // Redirect to the login page with a "logout" parameter
    }
}
