package com.herocompany.services;

import com.herocompany.entities.Basket;
import com.herocompany.entities.Customer;
import com.herocompany.entities.Product;
import com.herocompany.repositories.BasketRepository;
import com.herocompany.repositories.ProductRepository;
import com.herocompany.utils.REnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BasketService {
    final BasketRepository basketRepository;
    final UserDetailService userDetailService;
    final ProductRepository productRepository;
    final HttpSession httpSession;

    public BasketService(BasketRepository basketRepository, UserDetailService userDetailService, ProductRepository productRepository, HttpSession httpSession) {
        this.basketRepository = basketRepository;
        this.userDetailService = userDetailService;
        this.productRepository = productRepository;
        this.httpSession = httpSession;
    }

    public ResponseEntity<Map<REnum,Object>> save(Basket basket){
        Map<REnum,Object> hashMap= new LinkedHashMap<>();
        Customer customer= (Customer) httpSession.getAttribute("customer");
        List<Basket> baskets=basketRepository.findByCustomer_EmailEqualsIgnoreCaseAndStatusFalse(customer.getEmail());
        Optional<Product> optionalProduct=productRepository.findById(basket.getProduct().getId());
        boolean isSameProduct=false;
        Long basketId= Long.valueOf(0);
        int oldQuantityBasket=0;
        if (optionalProduct.isPresent()){
            for (Basket item:baskets){
                if (item.getProduct().getId()==basket.getProduct().getId()){
                    isSameProduct=true;
                    basketId=item.getId();
                    oldQuantityBasket=item.getQuantity();
                    break;
                }
            }

            Product product=optionalProduct.get();
            Integer stockQuantity= product.getStockQuantity();
            Integer basketQuantity=basket.getQuantity();

            if (basketQuantity <= stockQuantity){
                product.setStockQuantity(stockQuantity-basketQuantity);
                productRepository.save(product);
                if (isSameProduct){
                    basket.setId(basketId);
                    basket.setQuantity(basketQuantity+oldQuantityBasket);
                }
                basket.setCustomer(customer);
                basket.getCustomer().setId(customer.getId());
                basketRepository.save(basket);
                hashMap.put(REnum.status,true);
                hashMap.put(REnum.result,basket);
                return new ResponseEntity<>(hashMap, HttpStatus.OK);

            }else {
                hashMap.put(REnum.status, false);
                hashMap.put(REnum.message, "Out of stock, Please contact customer representative");
                return new ResponseEntity<>(hashMap, HttpStatus.OK);
            }

        }
        else {
        hashMap.put(REnum.status,false);
        hashMap.put(REnum.message,"There is not such a product");
        return new ResponseEntity<>(hashMap, HttpStatus.OK);
        }
    }

    public ResponseEntity<Map<REnum,Object>> update(Basket basket){
        Map<REnum,Object> hashMap= new LinkedHashMap<>();
        try {

            Optional<Basket> optionalBasket= basketRepository.findById(basket.getId());
            Optional<Product> optionalProduct=productRepository.findById(basket.getProduct().getId());
            //basketin productunu getir.
            //stoğu kontrol et kaydet

            if (optionalBasket.isPresent()){
                Basket oldBasket=optionalBasket.get(); //basketi getir.
                int oldBasketQuantity=oldBasket.getQuantity();
                int newBasketQuantity=basket.getQuantity();
                Integer diff=oldBasketQuantity-newBasketQuantity;
                oldBasket.setQuantity(newBasketQuantity);
                basketRepository.saveAndFlush(oldBasket);
                Product product=optionalProduct.get();
                if (newBasketQuantity<oldBasketQuantity){
                    //eski stok yenisinden büyükse
                    product.setStockQuantity(product.getStockQuantity()+diff);

                }else {//eski stok yenisinden küçüjse
                    product.setStockQuantity(product.getStockQuantity()+diff);
                }
                productRepository.save(product);

                hashMap.put(REnum.status,true);
                hashMap.put(REnum.message,"Update is sucsess.");
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
            Optional<Basket> optionalBasket=basketRepository.findById(id);

            Optional<Product> optionalProduct= productRepository.findById(optionalBasket.get().getProduct().getId());
            if (optionalBasket.isPresent()){
                Product product=optionalBasket.get().getProduct();
                basketRepository.deleteById(id);
                int quantity=optionalProduct.get().getStockQuantity();
                int basketQuantity=optionalBasket.get().getQuantity();
                product.setStockQuantity(quantity+basketQuantity);
                productRepository.save(product);
                hashMap.put(REnum.status,true);
                hashMap.put(REnum.message, "Delete is success");
                return new ResponseEntity<>(hashMap, HttpStatus.OK);
            }else {
                hashMap.put(REnum.status, false);
                hashMap.put(REnum.message, "Basket not found.");
                return new ResponseEntity<>(hashMap, HttpStatus.BAD_REQUEST);
            }

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

    public ResponseEntity<Map<REnum,Object>> getMyOrder(){
        Map<REnum,Object> hashMap =new LinkedHashMap<>();
        Customer customer= (Customer) httpSession.getAttribute("customer");
        List<Basket> baskets=basketRepository.findByStatusIsTrueAndCustomer_EmailEqualsIgnoreCase(customer.getEmail());
        hashMap.put(REnum.status,true);
        hashMap.put(REnum.result,baskets);
        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }

    public ResponseEntity<Map<REnum,Object>> getMyBasket(){
        Map<REnum,Object> hashMap =new LinkedHashMap<>();
        Customer customer= (Customer) httpSession.getAttribute("customer");
        List<Basket> baskets=basketRepository.findByCustomer_EmailEqualsIgnoreCaseAndStatusFalse(customer.getEmail());
        hashMap.put(REnum.status,true);
        hashMap.put(REnum.result,baskets);
        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }

}
