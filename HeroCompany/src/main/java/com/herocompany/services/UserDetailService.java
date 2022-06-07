package com.herocompany.services;

import com.herocompany.repositories.AdminRepository;
import com.herocompany.configs.JwtUtil;

import com.herocompany.entities.Login;
import com.herocompany.entities.Role;

import com.herocompany.repositories.CustomerRepository;
import com.herocompany.entities.Admin;
import com.herocompany.entities.Customer;
import com.herocompany.utils.REnum;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional //spring frameworkün
public class UserDetailService  implements UserDetailsService {
    //securitynin login işleminde userdetail servis türünde bir nesne beklediğimizden dolayı bu nesnenin kurulumu için bu interface buraya imp edilir.

    //    final UserJoinRepository userJoinRepository;
    final CustomerRepository customerRepository;
    final AdminRepository adminRepository;
    final AuthenticationManager authenticationManager; //spring securitye haber vermek için ara sınıf kullanolacak
    final JwtUtil jwtUtil;

    public UserDetailService(CustomerRepository customerRepository, AdminRepository adminRepository, @Lazy AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.customerRepository = customerRepository;
        this.adminRepository = adminRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
    //@Lazy yorgun yükleme manasındadır. Bu ifadeye göre içiçe çağrılmış injecte nesnelerinin circle a girmesini engeller.Sonsuz döngüye girmesini engeller.


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //UserDetails springin anlayacağı türdür. customer. admin vs.
        Optional<Admin> optionalAdmin=adminRepository.findByEmailEqualsIgnoreCase(username);
        Optional<Customer> optionalCustomer=customerRepository.findByEmailEqualsIgnoreCase(username);
        //email gidecek. kullanıcaı varmı dönen nesneyi doldur. nesne içindeki password encoderlı halde atılacak.
        //spring security biizim userımızı dikkari almaz userdetails türünde ister.
        if (optionalAdmin.isPresent()){ //nesne varsa null değilse.
            Admin admin= optionalAdmin.get(); //o emaile ait bilgileri getir.
            //securitynin giriş yapan kull rol yönetimini sağlayabilmesi üretilecek lan nesnenin içine kullanıcı rolleri authorize türünde rolü vermemiz lazım.
            UserDetails userDetails=new org.springframework.security.core.userdetails.User(
                    admin.getEmail(),
                    admin.getPassword(),
                    admin.isEnabled(),
                    admin.isTokenExpired(),
                    true,
                    true,
                    roles((List<Role>) admin.getRole())  //todo: hata varmı bak
            );
            return userDetails;
        }else {
            throw new UsernameNotFoundException("Böyle bir kullanıcı yok"); //403 gibi bir hata
        }

    }

    public Collection roles(List<Role> rolex){
        //
        List<GrantedAuthority> ls= new ArrayList<>();   //türü döndermek için buna GrantedAuthority türe ihtiyacımız var.GrantedAuthority --spring securitynin kullanıcı rollerini vermek için
        for (Role role:rolex){ //rollerin içinde gez
            ls.add(new SimpleGrantedAuthority(role.getName())); //rol türüne çevirdik içine ekledik listeye collection
        }
        return  ls;
    }

    public ResponseEntity register(Admin admin){//map olmasada olur<Map<REnum,Object>>
        //exceptiıonda sqlde uniqlik varsa burda try cache yazmamız lazımdı şuan
        Optional<Admin> optionalAdmin=adminRepository.findByEmailEqualsIgnoreCase(admin.getEmail());
        Map<REnum,Object> hm= new LinkedHashMap<>();
        if (!optionalAdmin.isPresent()){//var olup olmadığını kontrol ediyoruz yoksa yaz
            admin.setPassword(encoder().encode(admin.getPassword())); //springin anlıcağı password şekli encoder içinde encode diye bir method var şifreyi gönderince şifreliyor.
            Admin adm=adminRepository.save(admin);
            hm.put(REnum.status,true);
            hm.put(REnum.result,adm);
            return new ResponseEntity(hm, HttpStatus.OK);
        }else{
            hm.put(REnum.status,false);
            hm.put(REnum.message,"This email "+admin.getEmail() +" has been received");
            hm.put(REnum.result,admin);
            return new ResponseEntity(hm, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder(); //şifreyi arka planda şifrelenip yazıcak
    }

    //auth
    //jwt almak için login işlemi yaparak bu fonk tetiklenmelidir.
    public  ResponseEntity login (Login login){
        Map<REnum,Object> hm = new LinkedHashMap<>();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    login.getUsername(),login.getPassword()));
            UserDetails userDetails=loadUserByUsername(login.getUsername());
            String jwt= jwtUtil.generateToken(userDetails);
            hm.put(REnum.status,true);
            hm.put(REnum.jwt,jwt);
            hm.put(REnum.result,userJoinRepository.userJoin());
            return new ResponseEntity(hm,HttpStatus.OK);
        }catch (Exception ex){
            hm.put(REnum.status,false);
            hm.put(REnum.error,ex.getMessage());
            return new ResponseEntity(hm,HttpStatus.NOT_ACCEPTABLE);
        }

    }

    public Admin info(){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();  //usernameler sabit db e gitmeden yaparız böyle
        System.out.println(username);
        Optional<Admin> optionalAdmin=adminRepository.findByEmailEqualsIgnoreCase(username);
        if (optionalAdmin.isPresent()){
            return optionalAdmin.get();
        }
        return null;
    }
}
