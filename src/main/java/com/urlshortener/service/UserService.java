package com.urlshortener.service;

import com.urlshortener.dto.LoginRequest;
import com.urlshortener.dto.RegisterRequest;
import com.urlshortener.model.User;
import com.urlshortener.security.jwt.JwtAuthResponse;

public interface UserService {
    void registerUser(RegisterRequest registerRequest);
    JwtAuthResponse authenticateUser(LoginRequest loginRequest);
    User findByUsername(String name);
}
