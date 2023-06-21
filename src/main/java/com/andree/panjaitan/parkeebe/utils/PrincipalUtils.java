package com.andree.panjaitan.parkeebe.utils;


import com.andree.panjaitan.parkeebe.shared.CodeError;
import com.andree.panjaitan.parkeebe.shared.ErrorAppException;
import com.andree.panjaitan.parkeebe.user.User;
import com.andree.panjaitan.parkeebe.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class PrincipalUtils {
    private final UserRepository userRepository;

    public User getUser(Principal principal) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
        var sec = (User) usernamePasswordAuthenticationToken.getPrincipal();
        return userRepository.findByEmail(sec.getEmail())
                .orElseThrow(() ->
                        new ErrorAppException(CodeError.USER_NOT_FOUND.getCodeError(), "user not found"));
    }
}
