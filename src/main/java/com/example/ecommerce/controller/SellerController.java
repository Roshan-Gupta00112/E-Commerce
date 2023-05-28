package com.example.ecommerce.controller;

import com.example.ecommerce.dtos.request.SellerRequest;
import com.example.ecommerce.dtos.request.UpdateSellerUsingEmail;
import com.example.ecommerce.dtos.response.SellerResponseDto;
import com.example.ecommerce.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    SellerService sellerService;

    @PostMapping("/add")
    public ResponseEntity addSeller(@RequestBody SellerRequest sellerRequest){
        SellerResponseDto sellerResponseDto;
        try {
            sellerResponseDto=sellerService.addSeller(sellerRequest);
            return new ResponseEntity(sellerResponseDto, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/get-seller-by-id")
    public ResponseEntity getById(@RequestParam("id") int sellerId){
        try {
            SellerResponseDto sellerResponseDto=sellerService.getById(sellerId);
            return new ResponseEntity(sellerResponseDto, HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-sellers-by-name")
    public ResponseEntity getByName(@RequestParam("name")String sellerName){
        try {
            List<SellerResponseDto> sellerResponseDtoList= sellerService.getByName(sellerName);
            return new ResponseEntity(sellerResponseDtoList, HttpStatus.FOUND);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/get-seller-by-email")
    public ResponseEntity getByEmail(@RequestParam("email")String emailId){
        try {
            SellerResponseDto sellerResponseDto=sellerService.getByEmail(emailId);
            return new ResponseEntity(sellerResponseDto, HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-seller-by-mobNo")
    public ResponseEntity getByMobNo(@RequestParam("mobNo") String mobNo){

        try {
            SellerResponseDto sellerResponseDto=sellerService.getByMobNo(mobNo);
            return new ResponseEntity(sellerResponseDto, HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-all-sellers")
    public List<SellerResponseDto> getAllSellers(){
        return sellerService.getAllSellers();
    }


    @PutMapping("/update-seller-info-by-emailId")
    public ResponseEntity updateInfo(@RequestBody UpdateSellerUsingEmail updateSellerUsingEmail){
        try {
            SellerResponseDto sellerResponseDto=sellerService.updateInfo(updateSellerUsingEmail);
            return new ResponseEntity(sellerResponseDto, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/delete-seller-using-id")
    public ResponseEntity deleteUsingId(@RequestParam("id")int sellerId){
        try {
             String str=sellerService.deleteUsingId(sellerId);
             return new ResponseEntity<>(str, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete-seller-using-email")
    public ResponseEntity deleteUsingEmail(@RequestParam("email") String emailId){
         try {
             sellerService.deleteUsingEmail(emailId);
             return new ResponseEntity("Seller deleted successfully", HttpStatus.OK);
         }
         catch (Exception e){
             return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
         }
    }

}
