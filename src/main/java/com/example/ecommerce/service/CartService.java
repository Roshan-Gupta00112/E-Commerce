package com.example.ecommerce.service;

import com.example.ecommerce.dtos.response.CartResponse;
import com.example.ecommerce.dtos.response.ItemResponse;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Item;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.transformer.CartTransformer;
import com.example.ecommerce.transformer.ItemTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CustomerRepository customerRepository;

    public CartResponse saveToCart(String customerEmailId, Item item){

        // Getting Customer Object from the DB
        Customer customer= customerRepository.findByEmailId(customerEmailId);
        // Getting the respective cart of the customer
        Cart cart= customer.getCart();

//        int newNumberOfItems= cart.getNumberOfItems() + item.getRequiredQuantity();
//        cart.setNumberOfItems(newNumberOfItems);

        // Only counting the Distinct Item Including the current Item
        cart.setNumberOfItems(cart.getItems().size()+1);

        // Calculating Cart Total by Adding the Total Price of this Item to the Previous Cart Total
        double newTotal= cart.getCartTotal() + item.getRequiredQuantity()*item.getProduct().getPrice();
        cart.setCartTotal(newTotal);

        // Adding the Item to the Item List of Cart
        cart.getItems().add(item);

        // Here we are updating the Parameters of the Cart which was already saved in the DB. So no need to Save it again
        // because updation in java happens in place. So when we fetch a record and update its parameters then no need
        // to save it again in DB
        Cart savedCart= cartRepository.save(cart);

        // Now, preparing CartResponse
        CartResponse cartResponse= CartTransformer.cartToCartResponse(savedCart);

        List<Item> itemList= savedCart.getItems();
        List<ItemResponse> itemResponseList= new ArrayList<>();

        for (Item itm: itemList){
            itemResponseList.add(ItemTransformer.itemToItemResponse(itm));
        }

        cartResponse.setItemResponseList(itemResponseList);

        return cartResponse;
    }
}
