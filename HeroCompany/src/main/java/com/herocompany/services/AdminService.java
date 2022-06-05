package com.herocompany.services;

import com.herocompany.entities.Admin;
import com.herocompany.repositories.AdminRepository;
import com.herocompany.utils.REnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminService {
    final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public ResponseEntity<Map<REnum,Object>> save(Admin admin){
        Map<REnum,Object> hashMap= new LinkedHashMap<>();

        Admin adm= adminRepository.save(admin);
        hashMap.put(REnum.status,true);
        hashMap.put(REnum.result,admin);

        return new ResponseEntity<>(hashMap, HttpStatus.OK);

    }

    public ResponseEntity<Map<REnum,Object>> update(Admin admin){
        Map<REnum,Object> hashMap= new LinkedHashMap<>();
        try {
            Optional<Admin> optionalAdmin= adminRepository.findById(admin.getId());
            if (optionalAdmin.isPresent()){
                adminRepository.saveAndFlush(admin);
                hashMap.put(REnum.status,true);
                hashMap.put(REnum.result, admin);
                return new  ResponseEntity(hashMap, HttpStatus.OK);
            }else {
                hashMap.put(REnum.status,false);
                hashMap.put(REnum.message,"Admin is null! try again");
                return new  ResponseEntity(hashMap, HttpStatus.BAD_REQUEST);
            }

        }catch (Exception ex){
            hashMap.put(REnum.status,false);
            hashMap.put(REnum.message,ex.getMessage());
            return new  ResponseEntity(hashMap, HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<Map<REnum,Object>> delete(Integer id){
        Map<REnum,Object> hashMap =new LinkedHashMap<>();
        try {
            adminRepository.deleteById(id);
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
        hashMap.put(REnum.result,adminRepository.findAll());
        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }


}
