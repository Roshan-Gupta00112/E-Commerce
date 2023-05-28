package com.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "item")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(nullable = false)
    int requiredQuantity;


    @ManyToOne
    @JoinColumn
    Cart cart;


    @ManyToOne
    @JoinColumn
    Ordered order;


    @ManyToOne
    @JoinColumn
    Product product;

}
