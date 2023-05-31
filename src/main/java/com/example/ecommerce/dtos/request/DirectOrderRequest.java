package com.example.ecommerce.dtos.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DirectOrderRequest {

    String customerEmailId;

    int productId;

    int requiredQuantity;

    String  cardNo;

    String cvv;
}
