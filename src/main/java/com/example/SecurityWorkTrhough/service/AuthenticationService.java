package com.example.SecurityWorkTrhough.service;

import com.example.SecurityWorkTrhough.auth.DTOs.AuthenticationRequest;
import com.example.SecurityWorkTrhough.auth.DTOs.AuthenticationResponse;
import com.example.SecurityWorkTrhough.auth.DTOs.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
