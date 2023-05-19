package com.example.ecommerce.dtos.response;


import com.example.ecommerce.Enum.ProductCategory;
import com.example.ecommerce.Enum.ProductStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductResponse {

    int id;

    String productName;

    String sellerName;

    int quantity;

    Double price;

    ProductCategory category;

    ProductStatus productStatus;
}
