package com.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seller")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(nullable = false)
    String name;

    @Column(unique = true, nullable = false)
    String emailId;

    @Column(unique = true, nullable = false)
    String mobNo;

    @Column(nullable = false)
    String address;


    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    List<Product> products=new ArrayList<>();
}
