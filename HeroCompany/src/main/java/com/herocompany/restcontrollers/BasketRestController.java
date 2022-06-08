package com.herocompany.restcontrollers;

import com.herocompany.entities.Basket;
import com.herocompany.entities.Customer;
import com.herocompany.services.BasketService;
import com.herocompany.services.UserDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/basket")
public class BasketRestController {
    final BasketService basketService;
    final UserDetailService userDetailService;
    public BasketRestController(BasketService basketService, UserDetailService userDetailService) {
        this.basketService = basketService;
        this.userDetailService = userDetailService;
    }
    @PostMapping("/save")
    public ResponseEntity save(@Valid @RequestBody Basket basket){
        return basketService.save(basket);
    }

    @GetMapping("/list")
    public ResponseEntity list(){
        return basketService.list();
    }

    @PutMapping("/update")
    public ResponseEntity update(@Valid @RequestBody Basket basket){
        return basketService.update(basket);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(Long id){
        return  basketService.delete(id);
    }

    @GetMapping("/MyOrders")
    public ResponseEntity MyOrders(){
        Customer customer=userDetailService.infoCustomer();
        return basketService.customerBasket(customer.getEmail());
    }


}
