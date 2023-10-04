package org.movieBooking.util;

import org.movieBooking.enums.UserRole;
import org.movieBooking.enums.UserPermission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class VerifyRole {
    public static final String headerName = "authorities";

    public static boolean verify(String rolesHeader, UserRole... role) {
        if (rolesHeader == null || rolesHeader.isEmpty()) return false;
        var roles = Arrays.stream(rolesHeader.split(",")).filter(x -> ! x.isEmpty()).map(UserPermission::valueOf).toList();
        var permissions = Arrays.stream(role)
                .map(e -> e.getPermissions().stream().toList())
                .reduce(new ArrayList<>(), (l1, l2) -> {
                    l1.addAll(l2);
                    return l1;
                });
        if (roles.contains(UserPermission.ROLE_ADMIN)) return true;
        return !Collections.disjoint(roles, permissions);
    }
}
