package com.example.ecommerce.dtos.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ItemRequest {

    String customerEmailId;

    int productId;

    int requiredQuantity;
}
