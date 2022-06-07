package com.herocompany.repositories;

import com.herocompany.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    //	Kategoriye göre ürün listesi
    //	Gönderilecek kategori id’sine uyan ürünlerin listesi
    List<Product> findByCategory_IdEqualsOrderByProductNameAsc(Long id);



    //Ürün arama – (Valid 3 karakter)
    //o	Ürün içerisinde başlık ve detay
    List<Product> findByProductNameContainsIgnoreCaseOrDetailContainsIgnoreCase(String productName, String detail);





}