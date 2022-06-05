package com.herocompany.restcontrollers;

import com.herocompany.entities.Admin;
import com.herocompany.entities.Customer;
import com.herocompany.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/customer")
public class CustomerRestController {
    final CustomerService customerService;

    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }
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
