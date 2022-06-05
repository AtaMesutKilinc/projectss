package com.herocompany.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class OrderDetail {
    @EmbeddedId  //
    OrderDetailKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    Orders orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    Product product;


    private int quantity;

}
