package org.movieBooking.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.movieBooking.dto.UserDTO;
import org.movieBooking.entities.ConnValidationResponse;
import org.movieBooking.entities.User;
import org.movieBooking.exceptions.UsernameAlreadyExistsException;
import org.movieBooking.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class AuthenticationController {

    @Autowired private UserService userService;

    @GetMapping("/hello")
    public ResponseEntity<String> helloRoute() {
        return ResponseEntity.ok("World");
    }

    @PostMapping(value = {"/check"})
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Checked");
    }

    @PostMapping(value = {"/create"})
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO addUser(@Valid @RequestBody UserDTO userDto) throws UsernameAlreadyExistsException {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user = userService.encryptAndSave(user);
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    @PutMapping(value = {"/"})
    @ResponseStatus(HttpStatus.OK)
    public UserDTO updateUser(@Valid @RequestBody UserDTO userDto, HttpServletRequest request) throws UsernameAlreadyExistsException {
        var userId = request.getAttribute("userId");
        if (!(userId instanceof Integer)) {
            throw new RuntimeException("User Id is null");
        }
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setId((int) userId);
        user = userService.encryptAndUpdate(user);
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    @PostMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnValidationResponse> validatePost() {
        return ResponseEntity.ok(ConnValidationResponse.builder().status("OK").methodType(HttpMethod.POST.name())
                .isAuthenticated(true).build());
    }

    @GetMapping(value = "/validate", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnValidationResponse> validateGet(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        int userId = (Integer) request.getAttribute("userId");
        log.info("User id : " + userId);
        String token = (String) request.getAttribute("jwt");
        log.info("username "  + username);
        List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) request.getAttribute("authorities");
        return ResponseEntity.ok(ConnValidationResponse.builder().status("OK").methodType(HttpMethod.GET.name())
                        .username(username).token(token).authorities(grantedAuthorities)
                        .userId(userId)
                .isAuthenticated(true).build());
    }



    @PostMapping(value = "/whitelisted", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnValidationResponse> validateWhitelistedPost() {
        return ResponseEntity.ok(ConnValidationResponse.builder().status("OK").methodType(HttpMethod.POST.name()).build());
    }

    @GetMapping(value = "/whitelisted", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnValidationResponse> validateWhitelistedGet(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(ConnValidationResponse.builder().status("OK").methodType(HttpMethod.GET.name()).username(username).build());
    }

}