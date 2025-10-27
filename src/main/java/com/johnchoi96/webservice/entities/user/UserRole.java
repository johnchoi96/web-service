package com.johnchoi96.webservice.entities.user;

import lombok.Getter;

@Getter
public enum UserRole {
    /**
     * regular users
     */
    USER(0),
    /**
     * most privileged users
     */
    ADMIN(1);

    private final int level;

    UserRole(final int level) {
        this.level = level;
    }

    public boolean canAccess(final UserRole required) {
        return this.level >= required.level;
    }
}