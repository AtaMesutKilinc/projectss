package com.herocompany.restcontrollers;

import com.herocompany.entities.Admin;
import com.herocompany.services.UserDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class RegisterAdminRestController {
    final UserDetailService userDetailService;

    public RegisterAdminRestController(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity register(@Valid @RequestBody Admin admin){
        return userDetailService.registerAdmin(admin);
    }
}
