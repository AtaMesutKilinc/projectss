package com.herocompany.restcontrollers;

import com.herocompany.configs.JwtUtil;
import com.herocompany.entities.Customer;
import com.herocompany.entities.Login;
import com.herocompany.services.CustomerService;
import com.herocompany.services.UserDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/customer")
public class CustomerRestController {

    final CustomerService customerService;
    final UserDetailService userDetailService;
    final JwtUtil jwtUtil;

    public CustomerRestController(CustomerService customerService, UserDetailService userDetailService, JwtUtil jwtUtil) {
        this.customerService = customerService;
        this.userDetailService = userDetailService;
        this.jwtUtil = jwtUtil;
    }
    @PostMapping("/register") //save işlemi
    public ResponseEntity register(@Valid @RequestBody Customer customer){
        return userDetailService.registerCustomer(customer);
    }

    //nesneyi kabul eden bir json dosyası alamız biz bu nesneye dönüştürmemiz lazım.

//    @PostMapping("/login") //auth da olur
//    public ResponseEntity login (@Valid @RequestBody Login login){
//        return  userDetailService.login(login);
//    }

    @PostMapping("/save")
    public ResponseEntity save(@Valid @RequestBody Customer customer){
        return customerService.save(customer);
    }

    @GetMapping("/list")
    public ResponseEntity list(){
        return customerService.list();
    }

    @PutMapping("/update")
    public ResponseEntity update(@Valid @RequestBody Customer customer){
        return customerService.update(customer);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(Long id){
        return  customerService.delete(id);
    }

}
