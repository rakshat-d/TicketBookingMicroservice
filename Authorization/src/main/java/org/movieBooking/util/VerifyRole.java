package org.movieBooking.util;

import org.movieBooking.enums.UserRole;
import org.movieBooking.enums.UserPermission;

import java.util.Arrays;
import java.util.Collections;

public class VerifyRole {
    public static final String headerName = "authorities";

    public static boolean verify(String rolesHeader, UserRole... role) {
        if (rolesHeader == null || rolesHeader.isEmpty()) return false;
        var roles = Arrays.stream(rolesHeader.split(",")).filter(x -> ! x.isEmpty()).map(UserPermission::valueOf).toList();
        if (roles.contains(UserPermission.ROLE_ADMIN)) return true;
        return !Collections.disjoint(roles, Arrays.stream(role).toList());
    }
}
