package com.example.ecommerce.repository;

import com.example.ecommerce.Enum.CardType;
import com.example.ecommerce.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {

    Card findByCardNo(String cardNo);
    List<Card> findByCardType(CardType cardType);

    @Query(value = "SELECT * FROM Card c WHERE c.card_type=:cardType AND c.expiry_date>=:expiryDate",
            nativeQuery = true)
    List<Card> cardsWithCardTypeAndMinExpiryDate(String cardType, Date expiryDate);


    @Query(value = "SELECT Card_Type FROM Card c GROUP BY c.card_type ORDER BY COUNT(c.card_type) DESC LIMIT 1",
           nativeQuery = true)
    CardType findCardTypeWithMaxCardCount();


    @Query(value = "SELECT card_type FROM Card c GROUP BY c.card_type ORDER BY COUNT(c.card_type) LIMIT 1",
            nativeQuery = true)
    CardType findCardTypeWithMinCardCount();
}
