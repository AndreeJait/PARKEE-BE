package com.andree.panjaitan.parkeebe.auth;

import com.andree.panjaitan.parkeebe.config.JwtService;
import com.andree.panjaitan.parkeebe.shared.CodeError;
import com.andree.panjaitan.parkeebe.shared.ErrorAppException;
import com.andree.panjaitan.parkeebe.token.Token;
import com.andree.panjaitan.parkeebe.token.TokenRepository;
import com.andree.panjaitan.parkeebe.token.TokenType;
import com.andree.panjaitan.parkeebe.user.User;
import com.andree.panjaitan.parkeebe.user.UserRepository;
import com.andree.panjaitan.parkeebe.utils.DateFormatUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final DateFormatUtils dateFormatUtils;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));
        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(
                        () -> new ErrorAppException(
                                CodeError.USER_NOT_FOUND.getCodeError(),
                                "User not found"));
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken.getToken());
        return AuthenticationResponse
                .builder()
                .accessToken(jwtToken.getToken())
                .refreshToken(refreshToken.getToken())
                .expiredToken(jwtToken.getTimeExpired())
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token
                .builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new ErrorAppException(CodeError.FORBIDDEN.getCodeError(), "Token is required");

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository
                    .findByEmail(userEmail)
                    .orElseThrow(() -> new ErrorAppException(
                            CodeError.USER_NOT_FOUND.getCodeError(),
                            "User not found"));
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                var newRefreshToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken.getToken());
                return AuthenticationResponse
                        .builder()
                        .accessToken(accessToken.getToken())
                        .refreshToken(newRefreshToken.getToken())
                        .expiredToken(newRefreshToken.getTimeExpired())
                        .build();
            }
        }
        throw new ErrorAppException(CodeError.FORBIDDEN.getCodeError(), "invalid refresh token");
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(false);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
