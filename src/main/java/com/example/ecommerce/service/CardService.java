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

        // 1st Step:- checking whether the mob no is valid or not
        if(cardRequest.getMobNo().length()!=10){
            throw new InvalidMobNoException("Invalid mob no!");
        }
        // now mobNo is valid

        // 2nd Step:-  Getting Customer Object from the DB and checking whether the customer exist or Not
        Customer customer = customerRepository.findByMobNo(cardRequest.getMobNo());
        if (customer == null) {
            throw new InvalidCustomerException("Sorry! The mob no doesn't registered with any customer");
        }
        // Now Customer is valid


        // 3rd Step:- Validating cardNo, cvv and expiryDate
        //           a . validating cardNo length
        if (cardRequest.getCardNo().length() != 16) {
            throw new InvalidCardException("Incorrect Card no!");
        }
        //           b. Checking Whether the Card no already existing in the DB or NOT
        if (cardRepository.findByCardNo(cardRequest.getCardNo()) != null) {
            throw new InvalidCardException("Card no already exist!");
        }
        //           c. Checking whether the CVV is Valid or Not
        if (cardRequest.getCvv().length() != 3) {
            throw new InvalidCardException("Invalid cvv!");
        }
        //           d. Checking Whether the date of expiry is valid or not
        LocalDate todayDate = LocalDate.now();
        LocalDate expiry = new Date(cardRequest.getExpiryDate().getTime()).toLocalDate();
        if (expiry.equals(todayDate) || expiry.isBefore(todayDate)) {
            throw new InvalidCardException("Your card is already EXPIRED!");
        }
        //           e. validating card type
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
        //            a . Validating cardNo length
        if(cardNo.length()!=16){
            throw new InvalidCardException("Incorrect card no!");
        }
        //            b. validating cardNo
        Card card= cardRepository.findByCardNo(cardNo);
        if(card==null){
            throw new InvalidCardException("Invalid card no!");
        }
        //            c. validating card and customer
        if(card.getCustomer()!=customer){
            throw new InvalidCardException("Card doesn't belong to you!");
        }

        // Now Card and Customer both are valid
        cardRepository.delete(card);

        return "Card " +card.getCardNo()+ "of " +customer.getName()+ " deleted successfully!";
    }
}
