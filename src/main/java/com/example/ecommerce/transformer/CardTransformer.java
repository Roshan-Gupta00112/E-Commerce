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
                .cardType(cardRequest.getCardType())
                .build();
    }

    public static CardResponse cardToCardResponse(Card card){

        CardResponse cardResponse= CardResponse.builder()
                .customerName(card.getCustomer().getName())
                .expiryDate(card.getExpiryDate())
                .cardType(card.getCardType())
                .build();

        // Only showing last 4 digits to Customer
        StringBuilder cardNo= new StringBuilder();
        for(int i=0; i<card.getCardNo().length(); i++){
            if(i<card.getCardNo().length()-4) cardNo.append("x");
            else cardNo.append(card.getCardNo().charAt(i));
        }
        cardResponse.setCardNo(cardNo.toString());

        return cardResponse;
    }
}
