package com.herocompany.restcontrollers;

import com.herocompany.configs.JwtUtil;
import com.herocompany.entities.Admin;
import com.herocompany.entities.Customer;
import com.herocompany.entities.Login;
import com.herocompany.services.AdminService;
import com.herocompany.services.UserDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

    final AdminService adminService;
    final UserDetailService userDetailService;
    final JwtUtil jwtUtil;

    public AdminRestController(AdminService adminService, UserDetailService userDetailService, JwtUtil jwtUtil) {
        this.adminService = adminService;
        this.userDetailService = userDetailService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register") //save işlemi
    public ResponseEntity register(@Valid @RequestBody Admin admin){
        return userDetailService.registerAdmin(admin);
    }

    //nesneyi kabul eden bir json dosyası alamız biz bu nesneye dönüştürmemiz lazım.

    @PostMapping("/login") //auth da olur
    public ResponseEntity login (@Valid @RequestBody Login login){
        return  userDetailService.login(login);
    }

    @PostMapping("/save")
    public ResponseEntity save(@Valid @RequestBody Admin admin){
        return adminService.save(admin);
    }

    @GetMapping("/list")
    public ResponseEntity list(){
        return adminService.list();
    }

    @PutMapping("/update")
    public ResponseEntity update(@Valid @RequestBody Admin admin){
        return adminService.update(admin);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(Integer id){
        return  adminService.delete(id);
    }



}
