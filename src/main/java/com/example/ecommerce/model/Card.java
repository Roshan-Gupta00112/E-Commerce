package com.example.ecommerce.model;

import com.example.ecommerce.Enum.CardType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "card")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(unique = true, nullable = false)
    String  cardNo;

    @Column(nullable = false)
    String cvv;

    @Column(nullable = false)
    Date expiryDate;

    @Enumerated(EnumType.STRING)
    CardType cardType;

    @UpdateTimestamp
    Date lastUsedOn;

    @ManyToOne
    @JoinColumn
    Customer customer;
}
