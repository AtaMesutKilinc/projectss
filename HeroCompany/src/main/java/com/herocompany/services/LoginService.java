package com.herocompany.services;

import com.herocompany.configs.JwtUtil;
import com.herocompany.entities.Admin;
import com.herocompany.entities.Customer;
import com.herocompany.repositories.AdminRepository;
import com.herocompany.repositories.CustomerRepository;
import com.herocompany.repositories.utils.REnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoginService {


    final JwtUtil jwtUtil;
    final UserDetailService userDetailService;
    final CustomerRepository customerRepository;
    final PasswordEncoder passwordEncoder;
    final JavaMailSender emailSender;
    final AdminRepository adminRepository;

    public LoginService(JwtUtil jwtUtil, UserDetailService userDetailService, CustomerRepository customerRepository, PasswordEncoder passwordEncoder, JavaMailSender emailSender, AdminRepository adminRepository) {
        this.jwtUtil = jwtUtil;
        this.userDetailService = userDetailService;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailSender = emailSender;
        this.adminRepository = adminRepository;
    }


    public ResponseEntity forgotPassword(String email) {
        Map<REnum, Object> hm = new LinkedHashMap();
        Optional<Customer> optionalCustomer = customerRepository.findByEmailEqualsIgnoreCase(email);
        Optional<Admin> optionalAdmin = adminRepository.findByEmailEqualsIgnoreCase(email);

        if (optionalCustomer.isPresent()||optionalAdmin.isPresent()) {
            UUID uuid = UUID.randomUUID();
            String verifyCode = uuid.toString();
            String resetPasswordLink = "http://localhost:8092/resetPassword?resettoken=" + verifyCode;

            try {
                if (optionalCustomer.isPresent()) {
                    Customer customer = optionalCustomer.get();
                    customer.setResetPasswordToken(uuid.toString());
                    customerRepository.save(customer);
                    sendSimpleMessage("katamesut@gmail.com", "Password Reset Link", resetPasswordLink);
                }else{
                    Admin admin=optionalAdmin.get();
                    admin.setResetPasswordToken(verifyCode);
                    adminRepository.save(admin);
                    sendSimpleMessage("katamesut@gmail.com", "Password Reset Link", resetPasswordLink);
                }
                hm.put(REnum.status, "true");
                hm.put(REnum.result, resetPasswordLink);
                return new ResponseEntity<>(hm, HttpStatus.OK);
            } catch (Exception exception) {
                System.out.println("mail Error" + exception);
                hm.put(REnum.status, false);
                hm.put(REnum.error, exception);
                return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
            }
        } else {
            hm.put(REnum.status, "false");
            hm.put(REnum.status, "There is not such a e-mail address");
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity resetPassword(String verificationCode,  String password) {
        Map<REnum, Object> hm = new LinkedHashMap();
        Optional<Customer> optionalCustomer = customerRepository.findByResetPasswordTokenEquals(verificationCode);
        Optional<Admin> optionalAdmin = adminRepository.findByResetPasswordTokenEquals(verificationCode);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            customer.setPassword(passwordEncoder.encode(password));
            customer.setResetPasswordToken(null);
            customerRepository.save(customer);
            hm.put(REnum.status, true);
            return new ResponseEntity<>(hm, HttpStatus.OK);
        }
        else if(optionalAdmin.isPresent()){
            Admin admin =optionalAdmin.get();
            admin.setPassword(passwordEncoder.encode(password));
            admin.setResetPasswordToken(null);
            hm.put(REnum.status, true);
            return new ResponseEntity<>(hm, HttpStatus.OK);

        }  else {
            hm.put(REnum.status, false);
            hm.put(REnum.message, "Invalid verification code");
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("javalover138@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);

    }
}
