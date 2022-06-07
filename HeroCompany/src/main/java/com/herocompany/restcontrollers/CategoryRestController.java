package com.herocompany.restcontrollers;

import com.herocompany.entities.Category;
import com.herocompany.services.CategoryService;
import com.herocompany.utils.REnum;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/category")
public class CategoryRestController {

    final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/save")
    public ResponseEntity save(@Valid @RequestBody Category category){
        return categoryService.save(category);
    }
    @GetMapping("/list")
    public ResponseEntity list(){
        return categoryService.list();
    }
    @PutMapping("/update")
    public ResponseEntity update(@Valid @RequestBody Category category){
        return categoryService.update(category);
    }
    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestParam Long id){
        return categoryService.delete(id);
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
