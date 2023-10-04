package org.movieBooking.annotations;


import org.movieBooking.enums.UserRole;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HasRoles {
    UserRole[] value();
}
