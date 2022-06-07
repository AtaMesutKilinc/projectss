package com.herocompany.services;

import com.herocompany.entities.Admin;
import com.herocompany.entities.Customer;
import com.herocompany.repositories.CustomerRepository;
import com.herocompany.utils.REnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomerService {

    final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    public ResponseEntity<Map<REnum,Object>> save(Customer customer){
        Map<REnum,Object> hashMap= new LinkedHashMap<>();
        Customer cus= customerRepository.save(customer);
        hashMap.put(REnum.status,true);
        hashMap.put(REnum.result,customer);

        return new ResponseEntity<>(hashMap, HttpStatus.OK);

    }

    public ResponseEntity<Map<REnum,Object>> update(Customer customer){
        Map<REnum,Object> hashMap= new LinkedHashMap<>();
        try {
            Optional<Customer> optionalCustomer= customerRepository.findById(customer.getId());
            if (optionalCustomer.isPresent()){
                customerRepository.saveAndFlush(customer);
                hashMap.put(REnum.status,true);
                hashMap.put(REnum.result, customer);
                return new  ResponseEntity(hashMap, HttpStatus.OK);
            }else {
                hashMap.put(REnum.status,false);
                hashMap.put(REnum.message,"Customer is null! try again");
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
            customerRepository.deleteById(id);
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
        hashMap.put(REnum.result,customerRepository.findAll());
        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }


//    public GenericResponse resetPassword(HttpServletRequest request,
//                                         String userEmail) {
//        Map<REnum,Object> hashMap =new LinkedHashMap<>();
//        Optional<Customer> optionalCustomer = customerRepository.findByEmailEqualsIgnoreCase(userEmail);
//       try {
//           if (!optionalCustomer.isPresent()) {
//               hashMap.put(REnum.status,false);
//               hashMap.put(REnum.message,"Customer is null! try again");
//               return new  ResponseEntity(hashMap, HttpStatus.BAD_REQUEST);
//
//           }
//           String token = UUID.randomUUID().toString();
//           userService.createPasswordResetTokenForUser(user, token);
//           mailSender.send(constructResetTokenEmail(getAppUrl(request),
//                   request.getLocale(), token, user));
//           return new GenericResponse(
//                   messages.getMessage("message.resetPasswordEmail", null,
//                           request.getLocale()));
//
//       }catch (Exception ex){
//           hashMap.put(REnum.status,false);
//           hashMap.put(REnum.message,ex.getMessage());
//       }
//
//    }
}
