package com.example.ecommerce.Entity;

import com.example.ecommerce.Enum.CardType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "card")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(nullable = false)
    Date expiryDate;

    @Column(nullable = false)
    int cvv;

    @Column(unique = true, nullable = false)
    String  cardNo;

    @Enumerated(EnumType.STRING)
    CardType cardType;

    @ManyToOne
    @JoinColumn
    Customer customer;
}
