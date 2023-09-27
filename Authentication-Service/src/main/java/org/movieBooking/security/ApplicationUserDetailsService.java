package org.movieBooking.security;

import org.movieBooking.dto.ApplicationUser;
import org.movieBooking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ApplicationUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService usersService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        var user = new ApplicationUser(usersService.getByUsername(s).orElseThrow(() -> new UsernameNotFoundException("Username Not Found")));
        System.out.println(user.getUsername() + "  " + user.getAuthorities());
        return user;
    }
}