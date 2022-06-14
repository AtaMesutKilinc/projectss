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

    final JwtFilter jwtFilter;
    final UserDetailService userDetailService;

    public SecurityConfig(JwtFilter jwtFilter, UserDetailService userDetailService) {
        this.jwtFilter = jwtFilter;
        this.userDetailService = userDetailService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(userDetailService.encoder());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable().formLogin().disable();

        http
                .authorizeHttpRequests() //giriş rolleri ile çalış
                .antMatchers("/category/save","/category/delete","/category/update",
                         "/customer/delete","/customer/list",
                         "/product/add","/product/delete","/product/update",
                         "/order/list",
                        "/admin/**").hasRole("admin")
                .antMatchers("/customer/changePassword","/customer/settings",
                        "/basket/save","/basket/delete","/basket/update","/basket/MyBaskets",
                        "/order/save","/order/delete").hasRole("customer")  //hangi servis hangi rolle çalışcak emrini veriyoruz.
                .antMatchers( "/category/list",
                        "/product/list",
                        "/product/productByCategory",
                        "/basket/customer",
                        "/product/search").hasAnyRole("admin","customer")
                .antMatchers("/forgot_password/**",
                        "reset_password/**","/login/**","/register/**","/registerAdmin/**","/forgotPassword","/resetPassword").permitAll()
                .and()       //tanımlar dışında config var onları da koy
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);  //jwt nin üreteceği sessionun oluşturulmasına izin veriyor

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }






}
