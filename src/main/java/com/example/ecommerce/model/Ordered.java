package com.example.ecommerce.model;

import com.example.ecommerce.Enum.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Table(name ="order")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Ordered {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(unique = true, nullable = false)
    String orderNo;

    @Column(nullable = false)
    int noOfItems;

    @Column(nullable = false)
    Double totalOrderValue;

    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;

   @CreationTimestamp
   Date orderDate;

    @Column(nullable = false)
    String cardUsed;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<Item> items=new ArrayList<>();

    @ManyToOne
    @JoinColumn
    Customer customer;


    // Order is Parent for Item and at the same time it is also Child for Customer
}
