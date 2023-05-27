package com.example.ecommerce.service;


import com.example.ecommerce.dtos.request.ItemRequest;
import com.example.ecommerce.exception.InvalidCustomerException;
import com.example.ecommerce.exception.InvalidProductException;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Item;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductRepository productRepository;


    // We are adding Item in the Cart and for that we have to create an Item and then add that item in our Cart.
    // So the return type of this createItem function is "Item"
    public Item createItem(ItemRequest itemRequest) throws InvalidCustomerException, InvalidProductException {

        // Getting Customer Object from the DB and checking whether customer exist or Not
        Customer customer= customerRepository.findByEmailId(itemRequest.getCustomerEmailId());
        if(customer==null){
            throw new InvalidCustomerException("Invalid Customer Id!");
        }

        // Getting product Object from the DB and checking whether product exist or Not
        Product product;
        try {
            product= productRepository.findById(itemRequest.getProductId()).get();
        }
        catch (Exception e){
            throw new InvalidProductException("Invalid Product id!");
        }
        // Now both Customer and Product are valid

        int orderQuantity= itemRequest.getRequiredQuantity();

        // If no of quantity ordered is greater than the max ordered quantity then setting it to max ordered quantity
        if(orderQuantity > Integer.parseInt(product.getMaxOrderedQuantity())){
            orderQuantity= Integer.parseInt(product.getMaxOrderedQuantity());
        }

        if(orderQuantity > product.getQuantity()){
            throw new InvalidProductException("There is only " +product.getQuantity()+ " piece available. " +
                    "You have to order less than " +product.getQuantity()+ " piece");
        }
    }
}
