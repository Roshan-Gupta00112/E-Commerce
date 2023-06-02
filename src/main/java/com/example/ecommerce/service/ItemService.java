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



    public Item createItem(ItemRequest itemRequest) throws InvalidCustomerException, InvalidProductException {

        // 1st Step:- Validating Customer
        Customer customer= customerRepository.findByEmailId(itemRequest.getCustomerEmailId());
        if(customer==null){
            throw new InvalidCustomerException("Invalid customer email id!");
        }
        // Now customer is valid

        // 2nd Step:-fetching the product Object from the DB after validating product id & product status
        Product product;
        try {
            product= productRepository.findById(itemRequest.getProductId()).get();
        }
        catch (Exception e){
            throw new InvalidProductException("Invalid product id!");
        }
        // Validating Product status
        if(product.getProductStatus()== ProductStatus.OUT_OF_STOCK){
            throw new InvalidProductException("Product is currently out of stock!");
        }
        // Now Product is valid


        // 3rd Step:- Setting the required quantity within limit
        int orderQuantity= itemRequest.getRequiredQuantity();
        // If no of quantity ordered is greater than the max ordered quantity then setting it to max ordered quantity
        if(orderQuantity > Integer.parseInt(product.getMaxOrderedQuantity())){
            orderQuantity= Integer.parseInt(product.getMaxOrderedQuantity());
        }

        // 4th Step:- Checking whether the Ordered quantity is greater than the available quantity
        if(orderQuantity > product.getQuantity()){
            throw new InvalidProductException("There is only " +product.getQuantity()+ " quantity available. " +
                    "You can order utmost " +product.getQuantity()+ " quantity!");
        }


        // 5th Step:- setting the required quantity according to the condition within limit
        itemRequest.setRequiredQuantity(orderQuantity);


        // 6th step:- Now we can create an Item & Setting its Product attribute
        Item item= ItemTransformer.itemRequestToItem(itemRequest);
        item.setProduct(product);

        // 7th Step:- updating the attribute of product
        product.getItems().add(item);

        // 8th Step:- return after Saving the item in the DB
        return itemRepository.save(item);


        // NOTE:- In java the update of attribute happens in place
        //        So, here if we save only the child Object which is Item here then also the Item list of the Product
        //        get updated

//        Product savedProduct= productRepository.save(product); // It will save the item too
//        int size= product.getItems().size();
//        return savedProduct.getItems().get(size-1); // It will return the last added item that is the current item

    }
}
