package com.example.ecommerce.dtos.response;


import com.example.ecommerce.Enum.CardType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CardResponse {

    String customerName;

    String cardNo;

    Date expiryDate;

    CardType cardType;
}
