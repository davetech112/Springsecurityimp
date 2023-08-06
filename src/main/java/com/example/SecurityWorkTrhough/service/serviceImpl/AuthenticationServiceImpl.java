package com.example.SecurityWorkTrhough.service.serviceImpl;

import com.example.SecurityWorkTrhough.auth.DTOs.AuthenticationRequest;
import com.example.SecurityWorkTrhough.auth.DTOs.AuthenticationResponse;
import com.example.SecurityWorkTrhough.auth.DTOs.RegisterRequest;
import com.example.SecurityWorkTrhough.config.JwtService;
import com.example.SecurityWorkTrhough.service.AuthenticationService;
import com.example.SecurityWorkTrhough.user.Role;
import com.example.SecurityWorkTrhough.user.User;
import com.example.SecurityWorkTrhough.userRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private  final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwtToken);
        return authenticationResponse;
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()
        ));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var jwtToken = jwtService.generateToken(user);
        var authenticationResponse = AuthenticationResponse.builder().token(jwtToken).build();
        return authenticationResponse;
    }
}
