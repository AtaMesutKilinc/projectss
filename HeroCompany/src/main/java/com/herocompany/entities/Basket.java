package com.herocompany.entities;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Basket extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "product_id")
    private Product product;

    //pasife al sessiondan sonra.
    @ManyToOne
    @JoinColumn(name = "customer_Id")
    private Customer customer;

    boolean status= false;

    @Range(min = 1,message ="Please enter the number of products." )
    @NotNull
    private int quantity;
}
