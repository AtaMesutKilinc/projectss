package com.herocompany.restcontrollers;

import com.herocompany.entities.Customer;
import com.herocompany.entities.Orders;
import com.herocompany.services.BasketService;
import com.herocompany.services.OrdersService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrdersRestController {
    final OrdersService ordersService;
    final HttpSession httpSession;
    final BasketService basketService;

    public OrdersRestController(OrdersService ordersService, HttpSession httpSession, BasketService basketService) {
        this.ordersService = ordersService;
        this.httpSession = httpSession;
        this.basketService = basketService;
    }


    @PostMapping("/save")
    public ResponseEntity save(){
        return ordersService.save();
    }

    @Cacheable("orderList")
    @GetMapping("/list")
    public ResponseEntity list(){
        return ordersService.list();
    }

    @PutMapping("/update")
    public ResponseEntity update(@Valid @RequestBody Orders orders){
        return ordersService.update(orders);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestParam Long id){
        return  ordersService.delete(id);
    }

    @GetMapping("/MyOrders")
    public ResponseEntity MyOrders(){
        return basketService.getMyOrder();
    }

}
