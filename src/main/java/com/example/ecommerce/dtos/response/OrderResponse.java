package com.example.ecommerce.dtos.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderResponse {

    String customerName;

    String orderNo;

    int noOfItems;

    Double totalOrderValue;

    Date orderDate;

    String cardUsed;

    List<ItemResponse> itemResponseList;
}
