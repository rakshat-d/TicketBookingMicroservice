package org.movieBooking.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum UserRole {

    GUEST(new HashSet<>(List.of(UserPermission.ROLE_READ_ONLY))),
    USER(new HashSet<>(Arrays.asList(UserPermission.ROLE_USER, UserPermission.ROLE_WRITE_ONLY))),
    MODERATOR(new HashSet<>(Arrays.asList(UserPermission.ROLE_READ_ONLY, UserPermission.ROLE_WRITE_ONLY, UserPermission.ROLE_READ_ONLY_ADMIN))),
    ADMIN(new HashSet<>(List.of(UserPermission.ROLE_ADMIN)));

    final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions=permissions;
    }

    public Set<GrantedAuthority> getAuthorities() {
        return this.permissions.stream().map(authority -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toSet());
    }

}