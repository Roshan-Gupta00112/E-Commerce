package com.example.ecommerce.dtos.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductRequest {

    int sellerId;

    String name;

    int quantity;

    Double price;

    String productCategory;
}
