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
import com.arcars.arcars.repository.TokenRepository;
import com.arcars.arcars.repository.UserRepository;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JavaMailSender javaMailSender;

    public void sendVerificationEmail() {
        String bearerToken = extractBearerTokenFromRequest();
        String userEmail = extractEmailFromToken(bearerToken);

        if (userRepository.existsByEmail(userEmail)) {
            // Generate OTP
            String generatedOtp = generateOTP();

            // Save OTP along with user email
            Token otpToken = new Token(userEmail, "OTP", generatedOtp);
            tokenRepository.save(otpToken);

            // Send email with OTP
            sendOtpByEmail(userEmail, generatedOtp);
        } else {
            throw new ResourceNotFoundException("User", "email", userEmail);
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

    private String extractEmailFromToken(String bearerToken) {
        String token = bearerToken.substring(7); // Remove "Bearer " prefix
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.secretKeyFor(SignatureAlgorithm.HS256))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("email", String.class);
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
        String userEmail = extractEmailFromToken(bearerToken);

        if (userRepository.existsByEmail(userEmail)) {
            // Retrieve OTP for the given user email
            Optional<Token> otpToken = tokenRepository.findByEmailOfTokenGeneratedUserAndTokenType(userEmail, "OTP");

            if (otpToken.isPresent()) {
                Token storedOtp = otpToken.get();
                if (storedOtp.getToken().equals(otp.toString())) {
                    // OTP matches, mark email as verified
                    // You can update the user entity here if needed
                    // For example: userRepository.updateEmailVerifiedStatus(userEmail, true);

                    // Delete the OTP record as it's no longer needed
                    tokenRepository.delete(storedOtp);
                } else {
                    throw new InvalidOTPException("Invalid OTP");
                }
            } else {
                throw new OTPNotFoundException("OTP not found for the given email");
            }
        } else {
            throw new ResourceNotFoundException("User", "email", userEmail);
        }
    }

}
