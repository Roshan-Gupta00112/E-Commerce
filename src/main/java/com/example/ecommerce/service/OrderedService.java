package com.example.ecommerce.service;

import com.example.ecommerce.Enum.OrderStatus;
import com.example.ecommerce.Enum.ProductStatus;
import com.example.ecommerce.dtos.request.DirectOrderRequest;
import com.example.ecommerce.dtos.request.ItemRequest;
import com.example.ecommerce.dtos.response.ItemResponse;
import com.example.ecommerce.dtos.response.OrderResponse;
import com.example.ecommerce.exception.*;
import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.CardRepository;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.repository.OrderedRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.transformer.ItemTransformer;
import com.example.ecommerce.transformer.OrderTransformer;
import com.example.ecommerce.validate.CardValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderedService {

    @Autowired
    ProductService productService;

    @Autowired
    ItemService itemService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    OrderedRepository orderedRepository;


    public Ordered placeOrder(Cart cart, Card card) throws InvalidProductException {

        // Creating Order object using Builder through Order Transformer
        Ordered order= OrderTransformer.cartToOrder(cart, card.getCardNo());

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

        // Saving order in the DB
        Ordered saveOrder= orderedRepository.save(order);

        return saveOrder;
    }



    public OrderResponse directPlaceOrder(DirectOrderRequest directOrderRequest) throws InvalidOrderException, InvalidCardException {

        // 1st Step:- Preparing ItemRequest to generate an Item
        ItemRequest itemRequest= ItemTransformer.directOrderRequestToItemRequest(directOrderRequest);

        // 2nd Step:- Creating an Item and validate the respective parameter
        Item item;
        try {
            item= itemService.createItem(itemRequest);
        } catch (Exception e){
            throw new InvalidOrderException(e.getMessage());
        }
        // Now Customer & Product are valid and hence Item created.

        // 3rd Step:- fetching customer and product
        Customer customer= customerRepository.findByEmailId(directOrderRequest.getCustomerEmailId());
        Product product= productRepository.findById(directOrderRequest.getProductId()).get();

        // 4th Step:- Fetching Card after validating cardNo
        Card card;
        try {
            card= CardValidation.validateCardAndCvv(customer, directOrderRequest.getCardNo(),
                    directOrderRequest.getCvv());
        }
        catch (Exception e){
            throw new InvalidCardException(e.getMessage());
        }
        // Now cardNo & cvv both are valid


        // 5th Step:- Now creating Order using Builder through Order Transformer and setting it's all attributes
        Ordered ordered= OrderTransformer.directItemToOrder(customer, item, card.getCardNo());
        // setting items of ordered
        ordered.getItems().add(item);
        // saving the ordered in the DB
        Ordered savedOrder= orderedRepository.save(ordered);


        // 6th Step:- Now fetching the product and decreasing the product count and
        //            if it's quantity becomes 0 then setting the product status to out of stock.
        //            And setting attributes of product
        product.setQuantity(product.getQuantity()-directOrderRequest.getRequiredQuantity());
        if(product.getQuantity()==0){
            product.setProductStatus(ProductStatus.OUT_OF_STOCK);
        }
        product.setTotalQuantitySold(product.getTotalQuantitySold() + directOrderRequest.getRequiredQuantity());
        product.getItems().add(item);


        // 7th Step:- Setting attribute of Item
        item.setOrder(savedOrder);

        // 8th Step:- setting attribute of customer
        customer.getOrderedList().add(savedOrder);

        // 9th Step:- Preparing OrderResponse
        OrderResponse orderResponse= OrderTransformer.orderToOrderResponse(savedOrder);
        ItemResponse itemResponse= ItemTransformer.itemToItemResponse(item);
        List<ItemResponse> itemResponseList=new ArrayList<>();
        itemResponseList.add(itemResponse);
        orderResponse.setItemResponseList(itemResponseList);

        return orderResponse;

    }


    public String cancelOrder(String customerEmailId, String orderNo) throws InvalidOrderException, InvalidEmailException, InvalidCustomerException {

        // Validating customerEmailId and fetching the Customer
        Customer customer= customerRepository.findByEmailId(customerEmailId);
        if(customer==null){
            throw new InvalidEmailException("Invalid email id!");
        }
        // Now the Customer is valid

        // Validating orderNo And fetching the Ordered Object
        Ordered order= orderedRepository.findByOrderNo(orderNo);
        if(order==null){
            throw new InvalidOrderException("Invalid order no!");
        }
        // checking whether the order belong to same customer or not
        if(order.getCustomer()!=customer){
            throw new InvalidCustomerException("This isn't your order!");
        }
        // Now the order is valid


        // Getting item list of order
        List<Item> itemList= order.getItems();

        // fetching the product and setting its attribute accordingly
        for(Item item: itemList){
            Product product= item.getProduct();
            product.setQuantity(product.getQuantity() + item.getRequiredQuantity());
            product.setTotalQuantitySold(product.getTotalQuantitySold() - item.getRequiredQuantity());
        }

        // Deleting the Order
        orderedRepository.delete(order);

        return "Order cancelled successfully!";
    }

}
