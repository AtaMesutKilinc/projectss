package com.herocompany.services;

import com.herocompany.entities.Category;
import com.herocompany.repositories.CategoryRepository;
import com.herocompany.utils.REnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CategoryService {

    final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ResponseEntity<Map<REnum,Object>> save(Category category){
//        HttpHeaders headers=new HttpHeaders(); //bu bölüm header yani üst bilgi olarak gönderilir.
//        headers.add("customData","ex");
        Map<REnum,Object> hashMap= new LinkedHashMap<>();
        Category cat= categoryRepository.save(category); //u içinde id de var. yukardaki id de user yok.
        hashMap.put(REnum.status,true);
        hashMap.put(REnum.result,category);
        return new ResponseEntity<>(hashMap, HttpStatus.OK); //header kullanıcının görmediği hm kısmı kullanıcının gördüğü body json
    }

    //Optional türünden nesneler, null olma ihtimali olan alanları kolay yönetmek için oluşturulmuştur.

    public ResponseEntity<Map<String ,Object>> update(Category category){
        Map<REnum,Object> hashMap = new LinkedHashMap<>();
        try{
            Optional<Category> optionalCategory = categoryRepository.findById(category.getId());
            //burada 2.gun degişiklik yapıldı
            //optional null gelme durumunda patlamaması için optional yaparız.
            if(optionalCategory.isPresent()){
                categoryRepository.saveAndFlush(category);
                hashMap.put(REnum.status, true);
                hashMap.put(REnum.result, category);
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

    public ResponseEntity<Map<REnum,Object>> delete(Integer id){

        Map<REnum,Object> hashMap =new LinkedHashMap<>();
        //silinme işlemi void döner silinip silinmediğini anlamak için try cach kullanılır.

        try {
            categoryRepository.deleteById(id); //geriye void dönderiyor.

            hashMap.put(REnum.status,true);

            return new ResponseEntity<>(hashMap, HttpStatus.OK);
            //header kullanıcının görmediği hm kısmı kullanıcının gördüğü body json

        }catch (Exception ex){
            hashMap.put(REnum.status,false);
            hashMap.put(REnum.message,ex.getMessage());
            return new ResponseEntity<>(hashMap, HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<Map<REnum,Object>> list(){
        Map<REnum,Object> hashMap =new LinkedHashMap<>();
        hashMap.put(REnum.status,true);
        hashMap.put(REnum.result,categoryRepository.findAll());
        return new ResponseEntity<>(hashMap, HttpStatus.OK); //header kullanıcının görmediği hm kısmı kullanıcının gördüğü body json
    }
}
