package com.herocompany.restcontrollers;

import com.herocompany.entities.Category;
import com.herocompany.entities.Product;
import com.herocompany.services.ProductService;
import com.herocompany.utils.REnum;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/product")
public class ProductRestController {
    final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/save")
    public ResponseEntity save(@Valid @RequestBody Product product){
        return productService.save(product);
    }
    @GetMapping("/list")
    public ResponseEntity list(){
        return productService.list();
    }
    @PutMapping("/update")
    public ResponseEntity update(@Valid @RequestBody Product product){
        return productService.update(product);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestParam Long id){
        return productService.delete(id);
    }

    @GetMapping("/search")
    public ResponseEntity search(@RequestParam String q){
        return productService.search(q);
    }


    @GetMapping("/productByCategory")
    public ResponseEntity productByCategory(@RequestParam Integer id){
        return productService.productByCategory(id);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) //bad request hatalarında hep bu exception
    public Map handler(MethodArgumentNotValidException ex){
        Map<REnum,Object> hm = new LinkedHashMap<>();
        List<FieldError> errors=ex.getFieldErrors(); //aynı anda birden fazla hata olabilir.
        List< Map<String,String>> lss= new ArrayList<>();
        for (FieldError item:errors){
            Map<String,String> hmx=new HashMap<>();
            String fieldName=item.getField();  //adını verir fieldın
            String message= item.getDefaultMessage(); //mesajı veriri
//            System.out.println(fieldName+" "+message);

            hmx.put(fieldName,message);
            lss.add(hmx);

        }
        hm.put(REnum.status,false);
        hm.put(REnum.error,lss);

        return hm;
    }
}
