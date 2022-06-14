package com.herocompany.restcontrollers;

import com.herocompany.entities.Admin;
import com.herocompany.entities.Customer;
import com.herocompany.entities.Login;
import com.herocompany.services.LoginService;
import com.herocompany.services.UserDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@RestController
public class LoginRestController {
    final UserDetailService userDetailService;
    final LoginService loginService;


    public LoginRestController(UserDetailService userDetailService, LoginService loginService) {
        this.userDetailService = userDetailService;
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity login (@Valid @RequestBody Login login){
        return  userDetailService.login(login);
    }


    @PostMapping("/register") //save
    public ResponseEntity register(@Valid @RequestBody Customer customer){
        return userDetailService.registerCustomer(customer);
    }
    @PostMapping("/registerAdmin")
    public ResponseEntity register(@Valid @RequestBody Admin admin){
        return userDetailService.registerAdmin(admin);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity forgotPassword(@RequestParam @Email(message = "E-mail Format Error") String email) {
        return loginService.forgotPassword(email);
    }
    @PutMapping("/resetPassword")
    public ResponseEntity resetPassword(@RequestParam String resettoken,@RequestParam @Pattern(message = "Password must contain min one upper,lower letter and 0-9 digit number ",
            regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,})") String password){

        return loginService.resetPassword(resettoken,password);

    }

}
