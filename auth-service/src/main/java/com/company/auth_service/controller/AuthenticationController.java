package com.company.auth_service.controller;

import com.company.auth_service.dto.UserDTO;
import com.company.auth_service.dto.UserLoginDTO;
import com.company.auth_service.dto.UserLoginResponse;
import com.company.auth_service.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) throws MessagingException {
           userService.registerUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public  ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginDTO userLoginDTO){
        return ResponseEntity.ok(userService.login(userLoginDTO));
    }


    @GetMapping("/activate-account")
    public void activateAccount(@RequestParam String passedEmailToken) throws MessagingException {
         userService.activateAccount(passedEmailToken);
    }

}
