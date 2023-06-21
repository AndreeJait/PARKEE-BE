package com.andree.panjaitan.parkeebe.user;

import com.andree.panjaitan.parkeebe.shared.BlankResponse;
import com.andree.panjaitan.parkeebe.shared.SuccessResponse;
import com.andree.panjaitan.parkeebe.utils.PrincipalUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

    private final UserService service;
    private final PrincipalUtils principalUtils;

    @PostMapping("/create-user")
    public SuccessResponse<BlankResponse> createUser(@Valid @RequestBody CreateUserRequest request, Principal principal) {
        service.createUser(principalUtils.getUser(principal), request);
        return new SuccessResponse<>(new BlankResponse(), "Success to create user");
    }
}
