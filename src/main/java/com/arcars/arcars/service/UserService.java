package com.arcars.arcars.service;

import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.arcars.arcars.exception.InvalidOTPException;
import com.arcars.arcars.exception.OTPNotFoundException;
import com.arcars.arcars.exception.ResourceNotFoundException;
import com.arcars.arcars.model.Token;
import com.arcars.arcars.model.User;
import com.arcars.arcars.repository.TokenRepository;
import com.arcars.arcars.repository.UserRepository;
import com.arcars.arcars.security.JwtTokenProvider;
import com.arcars.arcars.utils.AppConstants;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final JavaMailSender javaMailSender;

    public void sendVerificationEmail() {
        String bearerToken = extractBearerTokenFromRequest();
        String username = jwtTokenProvider.getUsername(bearerToken);
        
        User user = userRepository.findByUsername(username);

        if (userRepository.existsByUsername(username)) {
            // Generate OTP
            String generatedOtp = generateOTP();

            // Save OTP along with user email
            Token otpToken = new Token(username, "OTP", generatedOtp);
            tokenRepository.save(otpToken);

            // Send email with OTP
            sendOtpByEmail(user.getEmail(), generatedOtp);

            user.setStatus(AppConstants.PENDING_CONFIRMATION.toString());
            userRepository.save(user);
        } else {
            throw new ResourceNotFoundException("User", "email", username);
        }
    }

    private String extractBearerTokenFromRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }

        throw new RuntimeException("Bearer token not found in request headers");
    }

    private String generateOTP() {
        Random random = new Random();
        int otpLength = 6; // 6-digit OTP
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10)); // Generate random digit (0-9)
        }

        return otp.toString();
    }

    private void sendOtpByEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verification Code");
        message.setText("Your OTP for registration is: " + otp);

        javaMailSender.send(message);
    }

    public void verifyEmail(Long otp) {
        String bearerToken = extractBearerTokenFromRequest();
        String username = jwtTokenProvider.getUsername(bearerToken);

        User user = userRepository.findByUsername(username);

        if (userRepository.existsByEmail(user.getEmail())) {
            // Retrieve OTP for the given user email
            Optional<Token> otpToken = tokenRepository.findByUsernameOfTokenGeneratedUserAndTokenType(username, "OTP");

            if (otpToken.isPresent()) {
                Token storedOtp = otpToken.get();
                if (storedOtp.getToken().equals(otp.toString())) {
                    // OTP matches, mark email as verified
                    // You can update the user entity here if needed
                    // For example: userRepository.updateEmailVerifiedStatus(userEmail, true);

                    // Delete the OTP record as it's no longer needed
                    user.setEmailVerified(true);
                    user.setStatus(AppConstants.REGISTERED.toString());
                    userRepository.save(user);
                    tokenRepository.delete(storedOtp);
                } else {
                    throw new InvalidOTPException("Invalid OTP");
                }
            } else {
                throw new OTPNotFoundException("OTP not found for the given email");
            }
        } else {
            throw new ResourceNotFoundException("User", "email", username);
        }
    }

}
