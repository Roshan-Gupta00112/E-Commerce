package com.example.ecommerce.transformer;

import com.example.ecommerce.dtos.request.CardRequest;
import com.example.ecommerce.dtos.response.CardResponse;
import com.example.ecommerce.model.Card;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CardTransformer {

    public static Card cardRequestToCard(CardRequest cardRequest){

        return Card.builder()
                .cardNo(cardRequest.getCardNo())
                .cvv(cardRequest.getCvv())
                .expiryDate(cardRequest.getExpiryDate())
                .build();
    }

    public static CardResponse cardToCardResponse(Card card){
        return CardResponse.builder()
                .customerName(card.getCustomer().getName())
                .cardNo(maskedCard(card.getCardNo()))
                .expiryDate(card.getExpiryDate())
                .cardType(card.getCardType())
                .build();
    }

    public String maskedCard(String cardNo){
        // Only showing last 4 digits to Customer
        StringBuilder cardUsed= new StringBuilder();
        for(int i=0; i<cardNo.length(); i++){
            if(i<cardNo.length()-4){
                cardUsed.append("x");
            }
            else cardUsed.append(cardNo.charAt(i));
        }

        return cardUsed.toString();
    }
}
