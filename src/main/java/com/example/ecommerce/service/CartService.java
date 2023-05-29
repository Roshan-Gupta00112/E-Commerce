package com.example.ecommerce.service;

import com.example.ecommerce.dtos.request.CheckOutCartRequest;
import com.example.ecommerce.dtos.response.CartResponse;
import com.example.ecommerce.dtos.response.ItemResponse;
import com.example.ecommerce.dtos.response.OrderResponse;
import com.example.ecommerce.exception.InvalidCardException;
import com.example.ecommerce.exception.InvalidCartException;
import com.example.ecommerce.exception.InvalidCustomerException;
import com.example.ecommerce.exception.InvalidOrderException;
import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.CardRepository;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.repository.OrderedRepository;
import com.example.ecommerce.transformer.CartTransformer;
import com.example.ecommerce.transformer.ItemTransformer;
import com.example.ecommerce.transformer.OrderTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    OrderedService orderedService;

    @Autowired
    OrderedRepository orderedRepository;

    @Autowired
    ProductService productService;

    public CartResponse saveToCart(String customerEmailId, Item item){

        // Getting Customer Object from the DB
        Customer customer= customerRepository.findByEmailId(customerEmailId);
        // Getting the respective cart of the customer & Setting all the parameters of CART
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

        // Setting the cart attribute of item
        item.setCart(cart);

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



    public OrderResponse checkOutCart(CheckOutCartRequest checkOutCartRequest) throws InvalidCustomerException,
            InvalidCardException, InvalidCartException, InvalidOrderException {

        // Fetching the customer object from the DB & Checking whether the customer is valid or not
        Customer customer= customerRepository.findByEmailId(checkOutCartRequest.getCustomerEmailId());
        if(customer==null){
            throw new InvalidCustomerException("Invalid customer email id!");
        }
        // Now customer is valid


        // Fetching the Card object & Checking whether the Card is valid or not;
        if(checkOutCartRequest.getCardNo().length()!=16){
            throw new InvalidCardException("Invalid card no!");
        }
        Card card= cardRepository.findByCardNo(checkOutCartRequest.getCardNo());
        if(card==null){
            throw new InvalidCardException("Invalid card no!");
        }
        // checking whether the card belongs to the same customer or not
        if(card.getCustomer()!=customer){
            throw new InvalidCardException("Invalid card!");
        }
        // checking whether the customer has entered the correct cvv no or not
        if(!card.getCvv().equals(checkOutCartRequest.getCvv()) || checkOutCartRequest.getCvv().length()!=3){
            throw new InvalidCardException("Incorrect cvv!");
        }
        // checking whether the card is active or expired
        LocalDate todayDate= LocalDate.now();
        LocalDate cardExpiryDate= new Date(card.getExpiryDate().getTime()).toLocalDate();
        if(cardExpiryDate.isBefore(todayDate)){
            throw new InvalidCardException("Card is expired!");
        }
        // Now card is valid


        // Fetching the cart of the customer
        Cart cart= customer.getCart();
        if(cart.getNumberOfItems()==0){
            throw new InvalidCartException("Your cart is empty!");
        }

        // Now we have items in cart so can place an order
        Ordered order;
        try {
            // this will throw exception when either product goes out of stock or when the quantity of product
            // goes lesser than the required quantity
            order= orderedService.placeOrder(customer, card);
        }
        catch (Exception e){
            throw new InvalidOrderException(e.getMessage());
        }

        // Saving the Order in the DB
        Ordered savedOrder= orderedRepository.save(order);

        // setting orderList attribute of customer
        customer.getOrderedList().add(savedOrder);

        // Now we have to reset cart & decrease the quantity of products
        resetCart(cart);

        // Preparing OrderResponse using Builder through OrderTransformer
        OrderResponse orderResponse= OrderTransformer.orderToOrderResponse(savedOrder);
        // setting itemResponse list attribute of OrderResponse
        List<ItemResponse> itemResponseList= new ArrayList<>();
        for (Item item:savedOrder.getItems()){
            //item.setOrder(savedOrder);  // Now we are ordering successfully. So, setting the Order attribute of item
            itemResponseList.add(ItemTransformer.itemToItemResponse(item));
        }

        return orderResponse;
    }
    private void resetCart(Cart cart){
        cart.setNumberOfItems(0);
        cart.setCartTotal(0);
        cart.setItems(new ArrayList<>());
    }

}
