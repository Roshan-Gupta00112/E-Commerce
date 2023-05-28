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

        // checking whether the mob no is valid or not
        if(cardRequest.getMobNo().length()!=10){
            throw new InvalidMobNoException("Invalid mob no!");
        }
        // now mob no is valid

        // Getting Customer Object from the DB and checking whether the customer exist or Not
        Customer customer = customerRepository.findByMobNo(cardRequest.getMobNo());
        if (customer == null) {
            throw new InvalidCustomerException("Sorry! The Customer doesn't exist");
        }
        // Now Customer exist

        // Checking whether CardNo is Valid or Not
        if (cardRequest.getCardNo().length() != 16) {
            throw new InvalidCardException("Card no is invalid!");
        }
        // Checking Whether the Card no already existing in the DB or NOT
        if (cardRepository.findByCardNo(cardRequest.getCardNo()) != null) {
            throw new InvalidCardException("Card no already exist!");
        }

        // Checking whether the CVV is Valid or Not
        if (cardRequest.getCvv().length() != 3) {
            throw new InvalidCardException("Invalid cvv!");
        }

        // Checking Whether the Card is Expired or NOT
        LocalDate todayDate = LocalDate.now();
        LocalDate expiry = new Date(cardRequest.getExpiryDate().getTime()).toLocalDate();
        if (expiry.equals(todayDate) || expiry.isBefore(todayDate)) {
            throw new InvalidCardException("Your card is already EXPIRED!");
        }
//        // Checking whether the Card Type Exist or Not
//        if((cardRequest.getCardType()!=CardType.MASTERCARD) && (cardRequest.getCardType()!=CardType.RUPAY) && (cardRequest.getCardType()!=CardType.VISA)){
//            throw new InvalidCardException("Invalid Card Type!");
//        }

        // Now Card is Valid


        // Creating Card Object and setting its Customer attribute
        Card card = CardTransformer.cardRequestToCard(cardRequest);
        card.setCustomer(customer);

        // Adding card in the Cards List of Customer
        customer.getCards().add(card);

        customerRepository.save(customer);  // It will also save Card

        // Preparing CardResponse using Builder through CardTransformer
        return CardTransformer.cardToCardResponse(card);
    }



    public List<CardResponse> getCardsUsingCardType(CardType cardType){

        // Getting Cards From DB
        List<Card> cardList= cardRepository.findByCardType(cardType);

        List<CardResponse> cardResponseList= new ArrayList<>();

        for (Card card:cardList){

            cardResponseList.add(CardTransformer.cardToCardResponse(card));
        }

        return cardResponseList;
    }


    public List<CardResponse> cardsWithCardTypeAndMinExpiryDate(String cardType, Date expiryDate){

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


    public String deleteCard(String cstEmailId, String cardNo) throws InvalidCustomerException, InvalidCardException {

        // checking whether the card no is valid or not
        if(cardNo.length()!=16){
            throw new InvalidCardException("Invalid card no!");
        }

        // Checking whether Customer with the given email id exist or not
        Customer customer = customerRepository.findByEmailId(cstEmailId);
        if (customer == null) {
            throw new InvalidCustomerException("Invalid Customer Id!");
        }

        // Checking whether the card exist or Not
        Card card = cardRepository.findByCardNo(cardNo);
        if (card == null) {
            throw new InvalidCardException("Invalid card No!");
        }

        // Now Card and Customer both are valid
        cardRepository.delete(card);

        return "Card " +card.getCardNo()+ "of " +customer.getName()+ " deleted successfully!";
    }
}
