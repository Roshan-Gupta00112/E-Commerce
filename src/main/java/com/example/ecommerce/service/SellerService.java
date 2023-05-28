package com.example.ecommerce.service;

import com.example.ecommerce.dtos.request.SellerRequest;
import com.example.ecommerce.dtos.request.UpdateSellerUsingEmail;
import com.example.ecommerce.dtos.response.SellerResponseDto;
import com.example.ecommerce.exception.*;
import com.example.ecommerce.model.Seller;
import com.example.ecommerce.repository.SellerRepository;
import com.example.ecommerce.transformer.SellerTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SellerService {

    @Autowired
    SellerRepository sellerRepository;

    public SellerResponseDto addSeller(SellerRequest sellerRequest) throws InvalidMobNoException, InvalidSellerException {

//        Creating Seller Object and setting it's all attributes using setter
//       Seller seller=new Seller();
//        seller.setName(sellerRequestDto.getName());
//        seller.setEmail(sellerRequestDto.getEmail());
//        seller.setMobNo(sellerRequestDto.getMobNo());
//        seller.setAddress(sellerRequestDto.getAddress());

        //  Creating Seller Object and setting it's all attributes using Builder
//        Seller seller=Seller.builder()
//                .name(sellerRequestDto.getName())
//                .emailId(sellerRequestDto.getEmailId())
//                .mobNo(sellerRequestDto.getMobNo())
//                .address(sellerRequestDto.getAddress())
//                .build();

        // Checking whether the Seller with the same email id already registered
        if(sellerRepository.findByEmailId(sellerRequest.getEmailId())!=null){
            throw new InvalidSellerException("Seller with the same email id already exist!");
        }

        // Checking whether the seller provide valid mobNo or Not
        if(sellerRequest.getMobNo().length()!=10){
            throw new InvalidMobNoException("Invalid Mob no!");
        }

        // Checking whether the Seller with the same mob no already registered
        if(sellerRepository.findByMobNo(sellerRequest.getMobNo())!=null){
            throw new InvalidMobNoException("Seller with the same mob no already exist!");
        }

        // Creating Seller Object and setting it's all attributes using Builder through SellerTransformer
        Seller seller= SellerTransformer.sellerRequestDtoTOSeller(sellerRequest);

        // Saving the Seller Object in the Db
        Seller savedSeller=sellerRepository.save(seller);

        // Creating SellerResponseDto using Builder through SellerTransformer
        return SellerTransformer.sellerToSellerResponseDto(savedSeller);
    }



    public SellerResponseDto getById(int sellerId) throws InvalidIdException {
        // Getting Seller Object from the DB
        Seller seller;
        try {
            seller=sellerRepository.findById(sellerId).get();
        }
        catch (Exception e){
            throw new InvalidIdException("Invalid Id!");
        }

        return SellerTransformer.sellerToSellerResponseDto(seller);
    }



    public List<SellerResponseDto> getByName(String sellerName) throws InvalidNameException {
        // Getting List of Sellers Object from the DB
        List<Seller> sellerList=sellerRepository.findByName(sellerName);
        if(sellerList.size()==0){
            throw new InvalidNameException("Invalid name!");
        }

        List<SellerResponseDto> sellerResponseDtoList=new ArrayList<>();

        // Creating SellerResponseDto and setting it's all attributes using Builder through Transformer/Converter
        for(Seller seller:sellerList){

            SellerResponseDto sellerResponseDto=SellerTransformer.sellerToSellerResponseDto(seller);

            sellerResponseDtoList.add(sellerResponseDto);
        }

        return sellerResponseDtoList;
    }




    public SellerResponseDto getByEmail(String emailId) throws InvalidEmailException {
        // Getting Seller Object from the DB
        Seller seller=sellerRepository.findByEmailId(emailId);

        if (seller==null){
            throw new InvalidEmailException("Invalid emailId!");
        }

//        SellerResponseDto sellerResponseDto=SellerTransformer.sellerToSellerResponseDto(seller);
//
//        return sellerResponseDto;

        return SellerTransformer.sellerToSellerResponseDto(seller);
    }



    public SellerResponseDto getByMobNo(String mobNo) throws InvalidMobNoException {
        // Checking whether the Mob no is valid or Not
        if(mobNo.length()!=10){
            throw new InvalidMobNoException("Invalid mobNo!");
        }

        // Getting Seller Object from the DB and checking whether the seller with the given mob no
        // exist or not
        Seller seller=sellerRepository.findByMobNo(mobNo);
        if(seller==null){
            throw new InvalidMobNoException("Invalid mobNo!");
        }

        // Creating SellerResponseDto using Builder through SellerTransformer
        return SellerTransformer.sellerToSellerResponseDto(seller);
    }



    public List<SellerResponseDto> getAllSellers(){

        // Getting all Sellers Objects or List of Sellers from the DB
        List<Seller> sellerList=sellerRepository.findAll();

        List<SellerResponseDto> sellerResponseDtoList=new ArrayList<>();

        for(Seller seller:sellerList){

            // Creating SellerResponseDto using Builder through SellerTransformer
            SellerResponseDto sellerResponseDto=SellerTransformer.sellerToSellerResponseDto(seller);

            // Adding it to final List
            sellerResponseDtoList.add(sellerResponseDto);
        }

        return sellerResponseDtoList;
    }




    public SellerResponseDto updateInfo(UpdateSellerUsingEmail updateSellerUsingEmail) throws InvalidEmailException {

        // Getting Seller Object from the DB
        Seller seller=sellerRepository.findByEmailId(updateSellerUsingEmail.getEmailId());

        if (seller==null){
            throw new InvalidEmailException("Invalid email id!");
        }

        seller.setName(updateSellerUsingEmail.getNewName());
        seller.setMobNo(updateSellerUsingEmail.getNewMobNo());
        seller.setAddress(updateSellerUsingEmail.getNewAddress());

        // Saving the Seller Object in the DB
        Seller updatedSeller=sellerRepository.save(seller);


        // Creating SellerResponseDto using Builder through SellerTransformer
        return SellerTransformer.sellerToSellerResponseDto(updatedSeller);
    }


    public String deleteUsingId(int sellerId) throws InvalidIdException {

        // Checking whether the seller with the given id exist in the Db or Not
        try {
            Seller seller=sellerRepository.findById(sellerId).get();
        }
        catch (Exception e){
            throw new InvalidIdException("Invalid id!");
        }

        // Now Seller exist with the given id. So, we can delete it
        sellerRepository.deleteById(sellerId);

        return "Seller deleted successfully";
    }



    public void deleteUsingEmail(String emailId) throws InvalidEmailException {

        // Getting Seller Object from the DB
        Seller seller=sellerRepository.findByEmailId(emailId);

        // Checking whether the seller with the given emailId exist or not in the DB
        if(seller==null){
            throw new InvalidEmailException("Invalid email id!");
        }

        // Deleting the Seller Object from the DB
        sellerRepository.delete(seller);
    }
}
