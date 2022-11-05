package dev.forum.forum.enumeration;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum UserRole {
    USER, ADMIN;

    public SimpleGrantedAuthority getSimpleGrantedAuthority() {
        return new SimpleGrantedAuthority(this.name());
    }

}

