package com.example.ecommerce.dtos.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ItemResponse {

    String productName;

    double pricePerQuantity;

    int quantity;

    double totalPrice;
}
