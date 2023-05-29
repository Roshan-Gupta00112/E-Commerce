package com.example.ecommerce.service;

import com.example.ecommerce.Enum.OrderStatus;
import com.example.ecommerce.exception.InvalidProductException;
import com.example.ecommerce.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderedService {

    @Autowired
    ProductService productService;


    public Ordered placeOrder(Customer customer, Card card) throws InvalidProductException {

        // fetching the cart of customer
        Cart cart= customer.getCart();

        // Creating Order object from DB & setting it's all attributes
        Ordered order= new Ordered();
        order.setOrderNo(String.valueOf(UUID.randomUUID()));
        order.setNoOfItems(cart.getNumberOfItems());
        order.setTotalOrderValue(cart.getCartTotal());
        order.setOrderStatus(OrderStatus.PLACED);

        // setting cardUsed attribute of order
        order.setCardUsed(maskedCard(card.getCardNo()));

        // setting items list attribute of order
        List<Item> orderedItems= new ArrayList<>();
        for(Item item: cart.getItems()){
            try {
                productService.decreaseProductQuantity(item);
                orderedItems.add(item);
            }
            catch (Exception e){
                throw new InvalidProductException(e.getMessage());
            }
        }
        order.setItems(orderedItems);

        // setting Order attribute of Item
        //orderedItems.forEach(item -> item.setOrder(order));  // using forEach
        for (Item item:orderedItems){
            item.setOrder(order);
        }

        // setting customer attribute of order
        order.setCustomer(customer);

        return order;
    }

    private String maskedCard(String cardNo){
        StringBuilder cardUsed= new StringBuilder();
        for(int i=0; i<cardNo.length(); i++){
            if(i<cardNo.length()-4){
                cardUsed.append("x");
            }
            else cardUsed.append(cardNo.charAt(i));
        }

        return cardUsed.toString();
    }
}
