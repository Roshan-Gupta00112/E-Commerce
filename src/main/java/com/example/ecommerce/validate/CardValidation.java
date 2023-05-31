package com.example.ecommerce.validate;

import com.example.ecommerce.dtos.request.CardRequest;
import com.example.ecommerce.exception.InvalidCardException;
import com.example.ecommerce.model.Card;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.repository.CardRepository;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.time.LocalDate;

@UtilityClass  // since I've use this annotation. So it's not necessary to make method static
public class CardValidation {

    @Autowired
    CardRepository cardRepository;

    public static Card validateCardAndCvv(Customer customer, String cardNo, String cvv) throws InvalidCardException {

        // 1st Step:- It will validate cardNo and also check whether the card belongs to the given customer or not
        Card card= validateCardNoAndCustomer(cardNo, customer);

        // 2nd Step:- validating card expiry date
        LocalDate todayDate= LocalDate.now();
        LocalDate cardExpiryDate= new Date(card.getExpiryDate().getTime()).toLocalDate();
        if(cardExpiryDate.isBefore(todayDate)){
            throw new InvalidCardException("Card is expired!");
        }

        // 3rd Step:- validating cvv
        if(cvv.length()!=3){
            throw new InvalidCardException("Invalid cvv!");
        }

        // 4th Step:- validating card & cvv
        if(card.getCvv().equals(cvv)){
            throw new InvalidCardException("Incorrect cvv!");
        }

        // 5th Step:-
        return card;
    }


    public static void validateCardRequest(CardRequest cardRequest) throws InvalidCardException {

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

        // Checking Whether the date of expiry is valid or not
        LocalDate todayDate = LocalDate.now();
        LocalDate expiry = new Date(cardRequest.getExpiryDate().getTime()).toLocalDate();
        if (expiry.equals(todayDate) || expiry.isBefore(todayDate)) {
            throw new InvalidCardException("Your card is already EXPIRED!");
        }
    }


    public static Card validateCardNoAndCustomer(String cardNo, Customer customer) throws InvalidCardException {

        // Validating cardNoLength
        if(cardNo.length()!=16){
            throw new InvalidCardException("Incorrect card no!");
        }

        // validating cardNo
        Card card= cardRepository.findByCardNo(cardNo);
        if(card==null){
            throw new InvalidCardException("Invalid card no!");
        }

        // validating card and customer
        if(card.getCustomer()!=customer){
            throw new InvalidCardException("Card doesn't belong to you!");
        }

        return card;
    }
}
