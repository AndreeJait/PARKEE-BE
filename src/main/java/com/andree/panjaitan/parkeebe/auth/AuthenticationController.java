package com.andree.panjaitan.parkeebe.auth;

import com.andree.panjaitan.parkeebe.shared.SuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/authentication")
    public SuccessResponse<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        return new SuccessResponse<>(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public SuccessResponse<AuthenticationResponse> refreshToken(HttpServletRequest request) {
        return new SuccessResponse<>(service.refreshToken(request));
    }
}
