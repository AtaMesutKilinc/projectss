package com.herocompany.restcontrollers;

import com.herocompany.entities.Basket;
import com.herocompany.services.BasketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/basket")
public class BasketRestController {
    final BasketService basketService;

    public BasketRestController(BasketService basketService) {
        this.basketService = basketService;
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
}
