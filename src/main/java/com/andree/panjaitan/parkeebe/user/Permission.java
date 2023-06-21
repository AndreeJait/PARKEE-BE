package com.andree.panjaitan.parkeebe.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),
    ADMIN_CREATE("admin:create"),

    PARKING_GUARD_READ("parking-guard:post"),
    PARKING_GUARD_UPDATE("parking-guard:read"),
    PARKING_GUARD_DELETE("parking-guard:delete"),
    PARKING_GUARD_CREATE("parking-guard:create");

    @Getter
    private final String permission;
}
