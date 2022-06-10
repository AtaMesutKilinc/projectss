package com.herocompany.configs;

import com.herocompany.entities.Customer;
import com.herocompany.services.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //egale ettik login işini.
    //yetkilendirmeler auth  burada yönetilecek.
    final JwtFilter jwtFilter;
    final UserDetailService userDetailService;

    public SecurityConfig(JwtFilter jwtFilter, UserDetailService userDetailService) {
        this.jwtFilter = jwtFilter;
        this.userDetailService = userDetailService;
    }//springte passwordde kullandığın şifreleme yöntemini değiştirebiliyoruz.yöntemini springe belirtmemiz lazım.
                    //2 ana rol var . 1. rol db de varlık denetimidir. 2. rol http securitydir.

    //veritabanı yönetim.Kullanoco varlık denetimi

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(userDetailService.encoder());

    }


    //rol ve yönetim
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable().formLogin().disable();

        http
                .authorizeHttpRequests() //giriş rolleri ile çalış
                .antMatchers("/admin/**").hasRole("admin")
                .antMatchers("/basket/save","/basket/update","/basket/delete","/basket/MyBaskets").hasRole("customer")  //hangi servis hangi rolle çalışcak emrini veriyoruz.
                .antMatchers("/basket/list").hasRole("admin")   //giriş olac
                .antMatchers("/customer/settings","/customer/changePassword").hasRole("customer") //bunlar açık oldu. rol ve giriş şartı aramıyorz.
                .antMatchers("/customer/**").hasRole("admin") //bunlar açık oldu. rol ve giriş şartı aramıyorz.
                .antMatchers("/category/list").hasRole("customer")
                .antMatchers("/category/**").hasRole("admin")
                .antMatchers("/orders/**").hasRole("admin")
                .antMatchers("/orders/save", "/orders/MyOrders","/orders/update","/orders/delete").hasRole("customer")
                .antMatchers("/product/**").hasRole("admin")
                .antMatchers("/product/list","/product/search","/product/productByCategory").hasRole("customer")
                .antMatchers("/forgot_password/**","reset_password/**").permitAll()
                .antMatchers("/registerAdmin/**").hasRole("admin")
                .antMatchers("/login/**","/register/**").permitAll()
                .and()       //tanımlar dışında config var onları da koy
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);  //jwt nin üreteceği sessionun oluşturulmasına izin veriyor

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        //öncesinde filtrele securityden önce farklı bir kontrol yapmak istersek.
        //jwt filterın hangi class ı alarak filtreleyeceğini belirtiyoruz.

    }
    /*
    session  arka planda
    bir kişinin bir web sayafası ziyaretinden sonra obje oluşturulur. bu objeyi sa

    --------------------------------------------------------------------------------
    jwt eksta bir  lib biz jwt yi kontrol edip bunun geçerli olduğunu söylemezsek
    sessionCreationPolicy bununla bu jwt nesnesini kabul etmez.

     */




    @Bean  //spring
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }






}
