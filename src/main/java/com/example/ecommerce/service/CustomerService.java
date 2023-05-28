package com.example.ecommerce.service;

import com.example.ecommerce.Enum.CardType;
import com.example.ecommerce.dtos.request.CustomerRequest;
import com.example.ecommerce.dtos.request.UpdateInfoUsingEmail;
import com.example.ecommerce.dtos.request.UpdateInfoUsingMobNo;
import com.example.ecommerce.dtos.response.CardResponse;
import com.example.ecommerce.dtos.response.CustomerResponse;
import com.example.ecommerce.dtos.response.ItemResponse;
import com.example.ecommerce.exception.InvalidCustomerException;
import com.example.ecommerce.exception.InvalidEmailException;
import com.example.ecommerce.exception.InvalidIdException;
import com.example.ecommerce.exception.InvalidMobNoException;
import com.example.ecommerce.model.Card;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Item;
import com.example.ecommerce.repository.CardRepository;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.transformer.CardTransformer;
import com.example.ecommerce.transformer.CustomerTransformer;
import com.example.ecommerce.transformer.ItemTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CardRepository cardRepository;

    public CustomerResponse addCustomer(CustomerRequest customerRequest) throws InvalidEmailException, InvalidMobNoException {

        // Checking whether the Customer with the same Email already Registered
        if(customerRepository.findByEmailId(customerRequest.getEmailId())!=null){
            throw new InvalidEmailException("Sorry! Customer with this email id already registered!");
        }

        // Checking whether the mob no is valid or Not
        if(customerRequest.getMobNo().length()!=10){
            throw new InvalidMobNoException("Invalid mob no!");
        }

        // Checking whether the Customer with the same mob No already Registered
        if(customerRepository.findByMobNo(customerRequest.getMobNo())!=null){
            throw new InvalidMobNoException("Customer with this mob no already registered!");
        }

        // Now we can create Customer
        Customer customer= CustomerTransformer.customerRequestToCustomer(customerRequest);

        // Creating Cart Object using Builder and setting it's all attributes
        Cart cart= Cart.builder()
                .cartTotal(0)
                .numberOfItems(0)
                .customer(customer)
                .build();
        // Setting Cart attributes of Customer
        customer.setCart(cart);

        // Saving Customer in the DB
        Customer savedCustomer=customerRepository.save(customer);  // It will save both Customer and Cart

        // Creating CustomerResponse using Builder through CustomerTransformer
        return CustomerTransformer.customerTocustomerResponse(savedCustomer);
    }


    public CustomerResponse getCustomerUsingEmail(String emailId) throws InvalidEmailException {

        // Getting Customer Using Email Id from the DB
        Customer customer= customerRepository.findByEmailId(emailId);

        // Checking Whether the emailId is Valid Or Not
        if(customer==null){
            throw new InvalidEmailException("Invalid email id!");
        }

        return CustomerTransformer.customerTocustomerResponse(customer);
    }


    public List<CustomerResponse> getAllCustomersUsingCardType(CardType cardType){

        // Getting List Cards from DB
        List<Card> cardList= cardRepository.findByCardType(cardType);

        List<CustomerResponse> customerResponseList= new ArrayList<>();

        for (Card card:cardList){

            customerResponseList.add(CustomerTransformer.customerTocustomerResponse(card.getCustomer()));
        }

        return customerResponseList;
    }

    public List<CustomerResponse> getAllCustomers(){

        // Getting all customers from the DB
        List<Customer> customerList= customerRepository.findAll();

        List<CustomerResponse> customerResponseList= new ArrayList<>();

        for (Customer customer:customerList){
            customerResponseList.add(CustomerTransformer.customerTocustomerResponse(customer));
        }

        return customerResponseList;

    }

    public List<CardResponse> getAllCards(String emailId) throws InvalidEmailException {

        // Getting Customer Object from the DB and Checking whether it Exist or Not
        Customer customer= customerRepository.findByEmailId(emailId);
        if(customer==null){
            throw new InvalidEmailException("Invalid email id!");
        }

        // Getting List of Cards of Customer
        List<Card> cardList=customer.getCards();

        List<CardResponse> cardResponseList= new ArrayList<>();

        for (Card card:cardList){
            cardResponseList.add(CardTransformer.cardToCardResponse(card));
        }

        return cardResponseList;
    }


    public List<ItemResponse> getAllItemsFromCart(String emailId) throws InvalidEmailException {

        // Getting Customer Object from the DB and checking Whether customer exist or not
        Customer customer= customerRepository.findByEmailId(emailId);
        if(customer==null){
            throw new InvalidEmailException("Invalid customer email id!");
        }

        // Getting Cart of Customer
        Cart cart= customer.getCart();

        // Getting List of all items present in the cart
        List<Item> itemList= cart.getItems();

        List<ItemResponse> itemResponseList= new ArrayList<>();

        // Creating ItemResponse from Item using Builder through ItemTransformer
        for (Item item:itemList){
            itemResponseList.add(ItemTransformer.itemToItemResponse(item));
        }

        return itemResponseList;
    }




    public CustomerResponse updateInfoByEmail(UpdateInfoUsingEmail updateInfoUsingEmail) throws InvalidEmailException {

        // Getting the Customer Object from the DB
        Customer customer= customerRepository.findByEmailId(updateInfoUsingEmail.getEmailId());
        // Checking whether the email was valid or NOT
        if(customer==null){
            throw new InvalidEmailException("Invalid email id!");
        }

        // Now Customer is valid so updating customer's those attributes whose value are passed
        if(updateInfoUsingEmail.getNewName()!=null){
            customer.setName(updateInfoUsingEmail.getNewName());
        }
        if(updateInfoUsingEmail.getNewDob()!=null){
            customer.setDob(updateInfoUsingEmail.getNewDob());
        }
        if(updateInfoUsingEmail.getNewMobNo()!=null){
            customer.setMobNo(updateInfoUsingEmail.getNewMobNo());
        }
        if(updateInfoUsingEmail.getNewAddress()!=null){
            customer.setAddress(updateInfoUsingEmail.getNewAddress());
        }

        // Saving the Customer in the DB
        Customer updatedCustomer= customerRepository.save(customer);

        return CustomerTransformer.customerTocustomerResponse(updatedCustomer);

    }


    public CustomerResponse updateInfoByMobNo(UpdateInfoUsingMobNo updateInfoUsingMobNo) throws InvalidMobNoException {

        // Checking whether the mob no is valid or not
        if(updateInfoUsingMobNo.getMobNo().length()!=10){
            throw new InvalidMobNoException("Invalid mob no!");
        }
        // Now mob no is valid

        // Getting the Customer Object from the DB
        Customer customer= customerRepository.findByMobNo(updateInfoUsingMobNo.getMobNo());

        // Checking whether the customer has been registered with the given mob no or NOT
        if(customer==null){
            throw new InvalidMobNoException("Invalid mob no!");
        }

        // Now Customer is valid. So, Updating only those attributes of customer whose value are passed by customer
        if(updateInfoUsingMobNo.getNewName()!=null){
            customer.setName(updateInfoUsingMobNo.getNewName());
        }
        if(updateInfoUsingMobNo.getNewDob()!=null){
            customer.setDob(updateInfoUsingMobNo.getNewDob());
        }
        if(updateInfoUsingMobNo.getNewEmailId()!=null){
            customer.setEmailId(updateInfoUsingMobNo.getNewEmailId());
        }
        if(updateInfoUsingMobNo.getNewAddress()!=null){
            customer.setAddress(updateInfoUsingMobNo.getNewAddress());
        }

        // saving the Customer in the DB
        Customer updatedCustomer= customerRepository.save(customer);

        return CustomerTransformer.customerTocustomerResponse(updatedCustomer);
    }



    public String deleteCustomer(String emailId, String mobNo) throws InvalidCustomerException, InvalidMobNoException {

        // checking whether the mob no is valid or not
        if(mobNo.length()!=10){
            throw new InvalidMobNoException("Invalid mob no!");
        }

        // Checking whether the Customer Exist or Not
        Customer customer= customerRepository.findByEmailId(emailId);
        if(customer==null){
            throw new InvalidCustomerException("Invalid Customer Email Id!");
        }
        // Checking whether the Customer MobNo match with the given mobNo or Not
        if(!customer.getMobNo().equals(mobNo)){
            throw new InvalidMobNoException("Given mob no does not matched with the registered mob no!");
        }

        // Now we can delete the Customer
        customerRepository.delete(customer);

        return "Customer " +customer.getName()+ " deleted successfully!";
    }


    public String deleteCustomerEitherBYEmailIdOrByMobNo(String emailId, String mobNo) throws InvalidEmailException, InvalidMobNoException {

        if(emailId.length()==0 && mobNo.length()==0){
            throw new InvalidEmailException("Provide either Email or MobNo!");
        }
        // Now Either Email or MobNo or Both value has been given.
        // Now, have to check whether the user passes the valid data(emailId or MobNo) or Not

        Customer customer;

        // When Deleting By Email
        if(emailId.length()!=0) {
            customer = customerRepository.findByEmailId(emailId);
            // Checking whether the given email Exist or not
            if (customer == null) {
                throw new InvalidEmailException("Invalid Email Id!");
            }

        }

        // When Deleting By Mob No
        else {
            // checking whether the mob no is valid or not
            if(mobNo.length()!=10){
                throw new InvalidMobNoException("Invalid mob no!");
            }

            customer= customerRepository.findByMobNo(mobNo);
            // Checking whether the customer with the given mob no exist or not
            if(customer==null){
                throw new InvalidMobNoException("Invalid MobNo!");
            }

        }
        customerRepository.delete(customer);
        return customer.getName() + " deleted successfully!";
    }

}
