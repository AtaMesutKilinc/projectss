package com.herocompany.entities;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)  //Date ayarları.
    private Date orderDate;

    //private Date deliveredDate;


    @ManyToOne
    @JoinColumn
    private Customer customer;

    @OneToMany(mappedBy = "orders")
    private List<OrderDetail> orderDetails;

    @ManyToMany //çok a çok ilişki
    @JoinTable(name = "order_details", //ara tablo adı
            joinColumns=@JoinColumn( //ilk çağrılan jwt user
                    name = "orders_id",referencedColumnName = "id"),//columna birleştirilecek olan sütunun adı :jwtuser_id,id de rolün idsi
            inverseJoinColumns = @JoinColumn(name = "product_id",referencedColumnName = "id") //2. sütunu kurdu.
    )
    private List<Product> products;

}
