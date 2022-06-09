package com.herocompany.restcontrollers;

import com.herocompany.configs.JwtUtil;
import com.herocompany.entities.AdminSettingsAttr;
import com.herocompany.entities.Customer;
import com.herocompany.entities.CustomerSettingsAttr;
import com.herocompany.entities.Login;
import com.herocompany.services.CustomerService;
import com.herocompany.services.UserDetailService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

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


    //nesneyi kabul eden bir json dosyası alamız biz bu nesneye dönüştürmemiz lazım.


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

    @PutMapping("/settings")
    public ResponseEntity settings(@Valid @RequestBody CustomerSettingsAttr customerSettingsAttr){
        return customerService.settings(customerSettingsAttr);
    }
    @PostMapping("/changePassword")
    public ResponseEntity changePassword(@RequestParam String oldPwd,@RequestParam  String newPwd,
                                         @RequestParam String newPwdConf){
        return customerService.changePassword(oldPwd,newPwd,newPwdConf);
    }

}
