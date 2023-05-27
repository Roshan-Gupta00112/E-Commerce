package com.example.ecommerce.controller;


import com.example.ecommerce.Enum.CardType;
import com.example.ecommerce.dtos.request.CardRequest;
import com.example.ecommerce.dtos.response.CardResponse;
import com.example.ecommerce.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/card")
public class CardController {

    @Autowired
    CardService cardService;

    @PostMapping("/add")
    public ResponseEntity addCard(@RequestBody CardRequest cardRequest){
        try {
            CardResponse cardResponse=cardService.addCard(cardRequest);
            return new ResponseEntity(cardResponse, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/get-cards-using-card-type/{cardType}")
    public List<CardResponse> getCardsUsingCardType(@PathVariable("cardType")CardType cardType){
        return cardService.getCardsUsingCardType(cardType);
    }

    @GetMapping("/get-cards-with-card-type-and-min-expiry-date")
    public List<CardResponse>  cardsWithCardTypeAndMinExpiryDate(@RequestParam("cardType") String cardType,
                                                                 @RequestParam("expiryDate") Date expiryDate){
        return cardService.cardsWithCardTypeAndMinExpiryDate(cardType, expiryDate);
    }


    @GetMapping("/most-widely-used-card-type")
    public CardType getMostUsedCardType(){
        return cardService.getMostUsedCardType();
    }

    @GetMapping("/least-used-card-type")
    public CardType getLeastUsedCardType(){
        return cardService.getLeastUsedCardType();
    }



    @DeleteMapping("/delete")
    public ResponseEntity deleteCard(@RequestParam("customerEmailId") String emailId, @RequestParam("cardNo") String cardNo){
        try {
            String str= cardService.deleteCard(emailId, cardNo);
            return new ResponseEntity(str, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
