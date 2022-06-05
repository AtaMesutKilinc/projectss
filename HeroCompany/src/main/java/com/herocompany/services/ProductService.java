package com.herocompany.services;

import com.herocompany.entities.Category;
import com.herocompany.entities.Product;
import com.herocompany.repositories.CategoryRepository;
import com.herocompany.repositories.ProductRepository;
import com.herocompany.utils.REnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {

    final ProductRepository productRepository;
    final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }


    public ResponseEntity<Map<REnum,Object>> save(Product product){

        Map<REnum,Object> hashMap= new LinkedHashMap<>();
        System.out.println("gönderdiğimiz   "+product.getCategory().getId());
        try {
            Optional<Category> optionalCategory= Optional.of(categoryRepository.getReferenceById(product.getCategory().getId()));
            Category category= optionalCategory.get();
            System.out.println(category);
            if (optionalCategory.isPresent()){
                System.out.println("optinal içi: "+optionalCategory.get());
                product.setCategory(optionalCategory.get());
                productRepository.save(product);
                hashMap.put(REnum.status,true);
                hashMap.put(REnum.result,product);
                return new ResponseEntity<>(hashMap, HttpStatus.OK);
            }else{
                hashMap.put(REnum.status, false);
                return new  ResponseEntity(hashMap, HttpStatus.BAD_REQUEST);
            }

        }catch (Exception ex){
            hashMap.put(REnum.status, false);
            hashMap.put(REnum.message, ex.getMessage());
            return new  ResponseEntity(hashMap, HttpStatus.BAD_REQUEST);
        }


    }



    public ResponseEntity<Map<String ,Object>> update(Product product){
        Map<REnum,Object> hashMap = new LinkedHashMap<>();
        try{
            Optional<Product> optionalProduct = productRepository.findById(product.getId());
            if(optionalProduct.isPresent()){
                productRepository.saveAndFlush(product);
                hashMap.put(REnum.result, product);
                hashMap.put(REnum.status, true);
                return new  ResponseEntity(hashMap, HttpStatus.OK);
            }else{
                hashMap.put(REnum.status, false);
                return new  ResponseEntity(hashMap, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            hashMap.put(REnum.status, false);
            hashMap.put(REnum.message, ex.getMessage());
            return new  ResponseEntity(hashMap, HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<Map<REnum,Object>> delete(Long id){

        Map<REnum,Object> hashMap =new LinkedHashMap<>();
        //silinme işlemi void döner silinip silinmediğini anlamak için try cach kullanılır.

        try {
            productRepository.deleteById(id);
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
        hashMap.put(REnum.result,productRepository.findAll());
        return new ResponseEntity<>(hashMap, HttpStatus.OK);

    }
    public ResponseEntity<Map<REnum,Object>> search(String q){
        Map<REnum,Object> hashMap =new LinkedHashMap<>();
        //ToDO: 3 harften sonra search yap
        List<Product> productList=productRepository.findByProductNameContainsIgnoreCaseOrDetailContainsIgnoreCase(q,q);
        hashMap.put(REnum.status,true);
        hashMap.put(REnum.result,productList);
        return new ResponseEntity<>(hashMap, HttpStatus.OK);

    }

    public ResponseEntity<Map<REnum,Object>> productByCategory(Integer id){
        Map<REnum,Object> hashMap =new LinkedHashMap<>();
        List<Product> productList=productRepository.findByCategory_IdEqualsOrderByProductNameAsc(id);
        hashMap.put(REnum.status,true);
        hashMap.put(REnum.result,productList);
        return new ResponseEntity<>(hashMap, HttpStatus.OK);

    }


}
