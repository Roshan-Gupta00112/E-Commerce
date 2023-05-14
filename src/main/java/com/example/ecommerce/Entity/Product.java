package com.example.ecommerce.Entity;

import com.example.ecommerce.Enum.ProductCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    int quantity;

    @Column(nullable = false)
    Double price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    ProductCategory category;

    @Column(nullable = false)
    String warrantyPeriods;

    Double rating;

    @ManyToOne
    @JoinColumn
    Seller seller;
}
