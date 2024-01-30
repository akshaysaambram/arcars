package com.arcars.arcars.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arcars.arcars.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/sendVerificationEmail")
    @ResponseBody
    public ResponseEntity<String> sendVerificationEmail() {
        userService.sendVerificationEmail();
        return ResponseEntity.ok("Verification Email Sent!");
    }

    @PostMapping("/verifyEmail")
    @ResponseBody
    public ResponseEntity<String> verifyEmail(@RequestParam Long otp) {
        userService.verifyEmail(otp);
        return ResponseEntity.ok("Email Verified Successfully!");
    }
}
