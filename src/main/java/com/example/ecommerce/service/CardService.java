package com.example.ecommerce.service;

import com.example.ecommerce.Enum.CardType;
import com.example.ecommerce.dtos.request.CardRequest;
import com.example.ecommerce.dtos.response.CardResponse;
import com.example.ecommerce.exception.InvalidCardException;
import com.example.ecommerce.exception.InvalidCustomerException;
import com.example.ecommerce.exception.InvalidMobNoException;
import com.example.ecommerce.model.Card;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.repository.CardRepository;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.transformer.CardTransformer;
import com.example.ecommerce.validate.CardValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CardService {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    CustomerRepository customerRepository;

    public CardResponse addCard(CardRequest cardRequest) throws InvalidCustomerException, InvalidCardException, InvalidMobNoException {

        // checking whether the mob no is valid or not
        if(cardRequest.getMobNo().length()!=10){
            throw new InvalidMobNoException("Invalid mob no!");
        }
        // now mob no is valid

        // Getting Customer Object from the DB and checking whether the customer exist or Not
        Customer customer = customerRepository.findByMobNo(cardRequest.getMobNo());
        if (customer == null) {
            throw new InvalidCustomerException("Sorry! The mob no doesn't registered with any customer");
        }
        // Now Customer is valid


        // Validating cardNo, cvv and expiryDate
        try {
            CardValidation.validateCardRequest(cardRequest);  // It will validate cardNo, cvv and expiryDate
        }
        catch (Exception e){
            throw new InvalidCardException(e.getMessage());
        }

        // validating card type
        CardType cardType;
        try {
            cardType= CardType.valueOf(cardRequest.getCardType());
        }
        catch (Exception e){
            throw new InvalidCardException("Invalid card type!");
        }

        // Now We can successfully create card

        // Creating Card Object and setting its Customer attribute
        Card card = CardTransformer.cardRequestToCard(cardRequest);
        card.setCardType(cardType);
        card.setCustomer(customer);

        // Adding card in the Cards List of Customer
        customer.getCards().add(card);

        customerRepository.save(customer);  // It will also save Card

        // Preparing CardResponse using Builder through CardTransformer
        return CardTransformer.cardToCardResponse(card);
    }



    public List<CardResponse> getCardsUsingCardType(String cardType) throws InvalidCardException {

        // Validating card type
        CardType savedCardType;
        try {
            savedCardType= CardType.valueOf(cardType);
        }
        catch (Exception e){
            throw new InvalidCardException("Invalid card type!");
        }
        // Now card type is valid

        // Getting Cards From DB
        List<Card> cardList= cardRepository.findByCardType(savedCardType);

        List<CardResponse> cardResponseList= new ArrayList<>();

        for (Card card:cardList){

            cardResponseList.add(CardTransformer.cardToCardResponse(card));
        }

        return cardResponseList;
    }


    public List<CardResponse> cardsWithCardTypeAndMinExpiryDate(String cardType, Date expiryDate) throws InvalidCardException {

        // validate card type
        CardType savedCardType;
        try {
            savedCardType= CardType.valueOf(cardType);
        }
        catch (Exception e){
            throw new InvalidCardException("Invalid card type!");
        }

        // Getting List of Cards from DB
        List<Card> cardList= cardRepository.cardsWithCardTypeAndMinExpiryDate(cardType, expiryDate);

        List<CardResponse> cardResponseList= new ArrayList<>();

        for (Card card:cardList){
            cardResponseList.add(CardTransformer.cardToCardResponse(card));
        }

        return cardResponseList;
    }



    public CardType getMostUsedCardType(){
        return cardRepository.findCardTypeWithMaxCardCount();
    }


    public CardType getLeastUsedCardType(){
        return cardRepository.findCardTypeWithMinCardCount();
    }


    public String removeCard(String cstEmailId, String cardNo) throws InvalidCustomerException, InvalidCardException {

        // Checking whether Customer with the given email id exist or not
        Customer customer = customerRepository.findByEmailId(cstEmailId);
        if (customer == null) {
            throw new InvalidCustomerException("Invalid Customer Id!");
        }

        // fetching card after validating it
        Card card = CardValidation.validateCardNoAndCustomer(cardNo, customer);
        if (card == null) {
            throw new InvalidCardException("Invalid card No!");
        }

        // Now Card and Customer both are valid
        cardRepository.delete(card);

        return "Card " +card.getCardNo()+ "of " +customer.getName()+ " deleted successfully!";
    }
}
