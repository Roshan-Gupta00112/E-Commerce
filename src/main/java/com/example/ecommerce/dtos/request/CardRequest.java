package com.example.ecommerce.dtos.request;


import com.example.ecommerce.Enum.CardType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardRequest {

    String mobNo;

    String cardNo;

    String cvv;

    Date expiryDate;

    CardType cardType;
}
