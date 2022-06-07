package com.herocompany.services;

import com.herocompany.entities.Admin;
import com.herocompany.entities.Basket;
import com.herocompany.entities.Customer;
import com.herocompany.repositories.BasketRepository;
import com.herocompany.utils.REnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class BasketService {
    final BasketRepository basketRepository;

    public BasketService(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    public ResponseEntity<Map<REnum,Object>> save(Basket basket){
        Map<REnum,Object> hashMap= new LinkedHashMap<>();

        Basket bas= basketRepository.save(basket);
        hashMap.put(REnum.status,true);
        hashMap.put(REnum.result,basket);

        return new ResponseEntity<>(hashMap, HttpStatus.OK);

    }

    public ResponseEntity<Map<REnum,Object>> update(Basket basket){
        Map<REnum,Object> hashMap= new LinkedHashMap<>();
        try {
            Optional<Basket> optionalBasket= basketRepository.findById(basket.getId());
            if (optionalBasket.isPresent()){
                basketRepository.saveAndFlush(basket);
                hashMap.put(REnum.status,true);
                hashMap.put(REnum.result, basket);
                return new  ResponseEntity(hashMap, HttpStatus.OK);
            }else {
                hashMap.put(REnum.status,false);
                hashMap.put(REnum.message,"Basket is null! try again");
                return new  ResponseEntity(hashMap, HttpStatus.BAD_REQUEST);
            }

        }catch (Exception ex){
            hashMap.put(REnum.status,false);
            hashMap.put(REnum.message,ex.getMessage());
            return new  ResponseEntity(hashMap, HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<Map<REnum,Object>> delete(Long id){
        Map<REnum,Object> hashMap =new LinkedHashMap<>();
        try {
            basketRepository.deleteById(id);
            hashMap.put(REnum.status,true);
            return new ResponseEntity<>(hashMap, HttpStatus.OK);
        }catch (Exception ex){
            hashMap.put(REnum.status,false);
            hashMap.put(REnum.message,ex.getMessage());
            return new ResponseEntity<>(hashMap, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Map<REnum,Object>> list(){
        Map<REnum,Object> hashMap =new LinkedHashMap<>();
        hashMap.put(REnum.status,true);
        hashMap.put(REnum.result,basketRepository.findAll());
        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }

}
