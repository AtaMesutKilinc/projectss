package com.herocompany.restcontrollers;

import com.herocompany.entities.Customer;
import com.herocompany.services.UserDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class RegisterCustomerRestController {
    final UserDetailService userDetailService;

    public RegisterCustomerRestController(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }
    @PostMapping("/register") //save işlemi
    public ResponseEntity register(@Valid @RequestBody Customer customer){
        return userDetailService.registerCustomer(customer);
    }
}
