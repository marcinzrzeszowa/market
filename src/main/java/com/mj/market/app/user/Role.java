package com.mj.market.app.user;

public enum Role {
    USER("USER"),
    ADMIN("ADMIN"),
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private String name;

    Role(String name) {
        this.name = name;
    }
}
