package com.arcars.arcars.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.arcars.arcars.payload.JwtAuthResponse;
import com.arcars.arcars.payload.LoginDTO;
import com.arcars.arcars.payload.RegisterDTO;
import com.arcars.arcars.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = { "/login", "/signin" })
    @ResponseBody
    public JwtAuthResponse login(@RequestBody LoginDTO loginDTO) {
        String accessToken = authService.login(loginDTO);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(accessToken);

        return jwtAuthResponse;
    }

    @PostMapping(value = { "/register", "/signup" })
    @ResponseBody
    public JwtAuthResponse register(@RequestBody RegisterDTO registerDTO) {
        String accessToken = authService.register(registerDTO);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(accessToken);

        return jwtAuthResponse;
    }

}
