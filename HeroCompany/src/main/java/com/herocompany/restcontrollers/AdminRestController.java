package com.herocompany.restcontrollers;

import com.herocompany.configs.JwtUtil;
import com.herocompany.entities.Admin;
import com.herocompany.entities.AdminSettingsAttr;
import com.herocompany.services.AdminService;
import com.herocompany.services.CustomerService;
import com.herocompany.services.UserDetailService;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

    final AdminService adminService;
    final CustomerService customerService;
    final UserDetailService userDetailService;
    final JwtUtil jwtUtil;


    public AdminRestController(AdminService adminService, CustomerService customerService, UserDetailService userDetailService, JwtUtil jwtUtil) {
        this.adminService = adminService;
        this.customerService = customerService;
        this.userDetailService = userDetailService;
        this.jwtUtil = jwtUtil;

    }

    //nesneyi kabul eden bir json dosyası alamız biz bu nesneye dönüştürmemiz lazım.


    @PostMapping("/save")
    public ResponseEntity save(@Valid @RequestBody Admin admin){
        return adminService.save(admin);
    }

    @Cacheable("adminList")  //name'i keyi list
    @GetMapping("/list")
    public ResponseEntity list(){
        return adminService.list();
    }

    @PutMapping("/update")
    public ResponseEntity update(@Valid @RequestBody Admin admin){
        return adminService.update(admin);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestParam Integer id){
        return  adminService.delete(id);
    }

    @PutMapping("/settingsAdmin")
    public ResponseEntity settings(@Valid @RequestBody AdminSettingsAttr adminSettingsAttr){
        return adminService.settings(adminSettingsAttr);
    }

    @PostMapping("/changePassword")
    public ResponseEntity changePassword(@RequestParam String oldPwd,@RequestParam String newPwd,
                                         @RequestParam String newPwdConf){
        return adminService.changePassword(oldPwd,newPwd,newPwdConf);
    }
    @DeleteMapping("/customerDelete")
    public ResponseEntity customerDelete(@RequestParam Long id){
        return  customerService.delete(id);
    }



}
