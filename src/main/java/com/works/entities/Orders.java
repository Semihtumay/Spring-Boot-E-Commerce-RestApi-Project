package com.works.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Entity
@Data
public class Orders extends Base{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customerID")
    private Customer customer;


    @OneToMany(cascade ={CascadeType.ALL})
    @JoinTable(name="Order_Basket",joinColumns = @JoinColumn(name="order_id" ),
            inverseJoinColumns = @JoinColumn(name="basket_id",referencedColumnName ="id")
    )
    private List<Basket> baskets;

    private Integer totalPrice;









}
