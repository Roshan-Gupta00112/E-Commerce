package com.example.ecommerce.service;

import com.example.ecommerce.dtos.request.CheckOutCartRequest;
import com.example.ecommerce.dtos.response.CartResponse;
import com.example.ecommerce.dtos.response.ItemResponse;
import com.example.ecommerce.dtos.response.OrderResponse;
import com.example.ecommerce.exception.*;
import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.*;
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
    ItemRepository itemRepository;

    public CartResponse saveToCart(String customerEmailId, Item item) {

        // fetching the Customer Object from the DB, and it is already validated while creating item
        Customer customer= customerRepository.findByEmailId(customerEmailId);

        // Getting the respective cart of the customer & Setting all the parameters of CART
        Cart cart= customer.getCart();


        // Calculating Cart Total by Adding the Total Price of this Item to the Previous Cart Total
        double newTotal= cart.getCartTotal() + item.getRequiredQuantity()*item.getProduct().getPrice();
        cart.setCartTotal(newTotal);

        // Adding the Item to the Item List of Cart
//        List<Item> cardItems= cart.getItems();
//        cardItems.add(item);
//        cart.setItems(cardItems);
        cart.getItems().add(item);

        // Only counting the Distinct Item Including the current Item
        cart.setNumberOfItems(cart.getNumberOfItems()+1);
        // int newNumberOfItems= cart.getNumberOfItems() + item.getRequiredQuantity();
//        cart.setNumberOfItems(newNumberOfItems);

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
            InvalidCardException, InvalidOrderException {

        // 1st Step:- Fetching the customer object from the DB
        Customer customer= customerRepository.findByEmailId(checkOutCartRequest.getCustomerEmailId());
        if(customer==null){
            throw new InvalidCustomerException("Invalid customer email id!");
        }


        // 2nd Step:-  Validating cardNo
        //            a . Validating cardNo length
        if(checkOutCartRequest.getCardNo().length()!=16){
            throw new InvalidCardException("Incorrect card no!");
        }
        //            b. validating cardNo
        Card card= cardRepository.findByCardNo(checkOutCartRequest.getCardNo());
        if(card==null){
            throw new InvalidCardException("Invalid card no!");
        }
        //            c. validating card and customer
        if(card.getCustomer()!=customer){
            throw new InvalidCardException("Card doesn't belong to you!");
        }
        //            d. validating card expiry date
        LocalDate todayDate= LocalDate.now();
        LocalDate cardExpiryDate= new Date(card.getExpiryDate().getTime()).toLocalDate();
        if(cardExpiryDate.isBefore(todayDate)){
            throw new InvalidCardException("Card is expired!");
        }
        //          e. validating cvv
        if(checkOutCartRequest.getCvv().length()!=3){
            throw new InvalidCardException("Incorrect cvv!");
        }
        //          f. validating card & cvv
        if(!card.getCvv().equals(checkOutCartRequest.getCvv())){
            throw new InvalidCardException("Invalid cvv!");
        }


        // 3rd Step:- Now placing an Order
        Ordered order;
        try {
            // this will throw exception when either product goes out of stock or when the quantity of product
            // goes lesser than the required quantity
            order= orderedService.placeOrder(customer, card);
        }
        catch (Exception e){
            throw new InvalidOrderException(e.getMessage());
        }
        // Now Order is valid

        // Preparing OrderResponse using Builder through OrderTransformer
        OrderResponse orderResponse= OrderTransformer.orderToOrderResponse(order);
        // setting itemResponse list attribute of OrderResponse
//        List<ItemResponse> itemResponseList= new ArrayList<>();
//        for (Item item:order.getItems()){
//            //item.setOrder(savedOrder);  // Now we are ordering successfully. So, setting the Order attribute of item
//            itemResponseList.add(ItemTransformer.itemToItemResponse(item));
//        }
//        orderResponse.setItemResponseList(itemResponseList);

        return orderResponse;
    }


    public List<ItemResponse> viewItemsOfCart(String customerEmailId) throws InvalidCartException, InvalidCustomerException {
        // Fetching customer object with validating it
        Customer customer= customerRepository.findByEmailId(customerEmailId);
        if (customer==null){
            throw new InvalidCustomerException("Invalid customer email id!");
        }

        // fetching cart of customer
        Cart cart= customer.getCart();
        if(cart.getNumberOfItems()==0){
            throw new InvalidCartException("Your cart is empty!");
        }

        // Converting Item to item response
        List<Item> itemList= cart.getItems();
        List<ItemResponse> itemResponseList= new ArrayList<>();
        for (Item item:itemList){
            itemResponseList.add(ItemTransformer.itemToItemResponse(item));
        }

        return itemResponseList;
    }



    public String removeItemFromCart(String customerEmailId, int itemId) throws InvalidEmailException, InvalidCartException {
        // Validating customer email id and fetching customer object
        Customer customer= customerRepository.findByEmailId(customerEmailId);
        if (customer==null){
            throw new InvalidEmailException("Invalid email id!");
        }
        // Now customer is valid

        // Getting the cart of customer and checking whether the Item belong to the cart or Not
        Cart cart= customer.getCart();
        if(cart.getNumberOfItems()==0){
            throw new InvalidCartException("Your cart is already empty!");
        }

        boolean isFound=false;
        for(int i=0; i<cart.getItems().size(); i++){
            Item item= cart.getItems().get(i);
            if(item.getId()==itemId){
                cart.getItems().remove(i);
                cart.setNumberOfItems(cart.getNumberOfItems()-1);
                cart.setCartTotal(cart.getCartTotal()-item.getRequiredQuantity()*item.getProduct().getPrice());
                itemRepository.delete(item);
                isFound=true;
                break;
            }
        }

        if(!isFound) { // it will execute when item not found in cart
            throw new InvalidCartException("Sorry the item isn't present in your cart!");
        }


        return "Item is removed from your cart!";
    }
}
