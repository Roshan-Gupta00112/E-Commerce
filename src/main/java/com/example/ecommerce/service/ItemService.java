package com.example.ecommerce.service;


import com.example.ecommerce.Enum.ProductStatus;
import com.example.ecommerce.dtos.request.ItemRequest;
import com.example.ecommerce.exception.InvalidCustomerException;
import com.example.ecommerce.exception.InvalidProductException;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Item;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.repository.ItemRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.transformer.ItemTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ItemRepository itemRepository;


    // We are adding Item in the Cart and for that we have to create an Item and then add that item in our Cart.
    // So the return type of this createItem function is "Item"
    public Item createItem(ItemRequest itemRequest) throws InvalidCustomerException, InvalidProductException {

        // 1st Step:- Getting Customer Object from the DB and checking whether customer exist or Not
        Customer customer= customerRepository.findByEmailId(itemRequest.getCustomerEmailId());
        if(customer==null){
            throw new InvalidCustomerException("Invalid Customer Id!");
        }

        // 2nd Step:- Getting product Object from the DB and checking whether product exist or Not
        Product product;
        try {
            product= productRepository.findById(itemRequest.getProductId()).get();
        }
        catch (Exception e){
            throw new InvalidProductException("Invalid Product id!");
        }
        // Now both Customer and Product are valid

        // 3rd Step:- Checking whether the product is available or not
        if(product.getProductStatus()== ProductStatus.OUT_OF_STOCK){
            throw new InvalidProductException("Currently Product is Out Of Stock!");
        }

        // 4th Step:- Setting the required quantity within limit
        int orderQuantity= itemRequest.getRequiredQuantity();
        // If no of quantity ordered is greater than the max ordered quantity then setting it to max ordered quantity
        if(orderQuantity > Integer.parseInt(product.getMaxOrderedQuantity())){
            orderQuantity= Integer.parseInt(product.getMaxOrderedQuantity());
        }

        // 5th Step:- Checking whether the Ordered quantity is greater than the available quantity
        if(orderQuantity > product.getQuantity()){
            throw new InvalidProductException("There is only " +product.getQuantity()+ " piece available. " +
                    "You have to order less than " +product.getQuantity()+ " piece");
        }

        // 6th Step:- setting the required quantity according to the condition within limit
        itemRequest.setRequiredQuantity(orderQuantity);


        // 7th step:- Now we can create an Item & Setting its customer & Product attribute
        Item item= ItemTransformer.itemRequestToItem(itemRequest);
        item.setCart(customer.getCart());
        item.setProduct(product);

        // 8th Step:- updating the attribute of product
        product.getItems().add(item);

        // 9th Step:- Saving in the DB
        Item updatedItem= itemRepository.save(item);
        return updatedItem;


        // NOTE:- In java the updation of attribute happens in place
        //        So, here if we save only the child Object which is Item here then also the Item list of the Product
        //        get updated

//        Product savedProduct= productRepository.save(product); // It will save the item too
//        int size= product.getItems().size();
//        return savedProduct.getItems().get(size-1); // It will return the last added item that is the current item

    }
}
