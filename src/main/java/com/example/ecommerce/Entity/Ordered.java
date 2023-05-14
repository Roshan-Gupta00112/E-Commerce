package com.example.ecommerce.Entity;

import com.example.ecommerce.Enum.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ordered {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String orderNo;

    Double totalOrderValue;

    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;

    Date orderDate;

    String cardUsed;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<Item> items=new ArrayList<>();

    @ManyToOne
    @JoinColumn
    Customer customer;


    // Order is Parent for Item and at the same time it is also Child for Customer
}
