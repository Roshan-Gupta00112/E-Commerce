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
import com.example.ecommerce.validate.CardValidation;
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

    @Autowired
    CardRepository cardRepository;

    @Autowired
    OrderedService orderedService;

    @Autowired
    OrderedRepository orderedRepository;

    @Autowired
    ProductService productService;

    @Autowired
    ItemRepository itemRepository;

    public CartResponse saveToCart(String customerEmailId, Item item) throws InvalidCustomerException {

        // fetching the Customer Object from the DB and validating the customer
        Customer customer= customerRepository.findByEmailId(customerEmailId);
        if(customer==null){
            throw new InvalidCustomerException("Invalid customer email id!");
        }
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

        // 1st Step:- Fetching the customer object from the DB which is already validated in
        // item service while creating item
        Customer customer= customerRepository.findByEmailId(checkOutCartRequest.getCustomerEmailId());


        // 2nd Step:- Fetching card after validating it
        Card card;
        try {
            card= CardValidation.validateCardAndCvv(customer, checkOutCartRequest.getCardNo(),
                    checkOutCartRequest.getCvv());
        }
        catch (Exception e){
            throw new InvalidCardException(e.getMessage());
        }


        // 3rd Step:- Fetching the cart of the customer
        Cart cart= customer.getCart();
        if(cart.getNumberOfItems()==0){
            throw new InvalidCartException("Your cart is empty!");
        }
        // Now cart is valid

        // 4th Step:- Now we have items in cart so we can place an order
        Ordered order;
        try {
            // this will throw exception when either product goes out of stock or when the quantity of product
            // goes lesser than the required quantity
            order= orderedService.placeOrder(cart, card);
        }
        catch (Exception e){
            throw new InvalidOrderException(e.getMessage());
        }
        // Now Order is valid

        //Setting Customer attribute of order
        order.setCustomer(customer);

       // setting orderList attribute of customer
        customer.getOrderedList().add(order);

        // Now we have to reset cart & decrease the quantity of products
        resetCart(cart);

        // Preparing OrderResponse using Builder through OrderTransformer
        OrderResponse orderResponse= OrderTransformer.orderToOrderResponse(order);
        // setting itemResponse list attribute of OrderResponse
        List<ItemResponse> itemResponseList= new ArrayList<>();
        for (Item item:order.getItems()){
            //item.setOrder(savedOrder);  // Now we are ordering successfully. So, setting the Order attribute of item
            itemResponseList.add(ItemTransformer.itemToItemResponse(item));
        }
        orderResponse.setItemResponseList(itemResponseList);

        return orderResponse;
    }
    private void resetCart(Cart cart){
        cart.setNumberOfItems(0);
        cart.setCartTotal(0);
        cart.setItems(new ArrayList<>());
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

        List<Item> itemList= cart.getItems();
        boolean isFound= false;
        for(Item item:itemList){
            if(item.getId()==itemId){
                isFound=true;
                break;
            }
        }

        if(!isFound){
            throw new InvalidCartException("Sorry the item isn't present in your cart!");
        }

        // Now the item is present in the customer's cart

        // Now deleting the Item
        itemRepository.deleteById(itemId);

        return "Item is removed from your cart!";
    }

}
