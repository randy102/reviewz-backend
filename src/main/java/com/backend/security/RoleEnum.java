package com.backend.security;

public enum RoleEnum {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_USER("ROLE_USER");

    public final String value;

    RoleEnum(String value) {
        this.value = value;
    }
}
