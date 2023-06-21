package com.andree.panjaitan.parkeebe.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {
    USER(
            Collections.emptySet()
    ),
    ADMIN(Set.of(
            Permission.ADMIN_READ,
            Permission.ADMIN_CREATE,
            Permission.ADMIN_UPDATE,
            Permission.ADMIN_DELETE,
            Permission.PARKING_GUARD_CREATE,
            Permission.PARKING_GUARD_DELETE,
            Permission.PARKING_GUARD_UPDATE,
            Permission.PARKING_GUARD_READ
    )),
    PARKING_GUARD(Set.of(
            Permission.PARKING_GUARD_CREATE,
            Permission.PARKING_GUARD_DELETE,
            Permission.PARKING_GUARD_UPDATE,
            Permission.PARKING_GUARD_READ
    ));

    @Getter
    final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

}
