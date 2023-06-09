package com.example.ecommerce.controller;


import com.example.ecommerce.Enum.ProductCategory;
import com.example.ecommerce.Enum.ProductStatus;
import com.example.ecommerce.dtos.request.DeleteProductOfSeller;
import com.example.ecommerce.dtos.request.IncreaseProductCountRequest;
import com.example.ecommerce.dtos.request.ProductRequest;
import com.example.ecommerce.dtos.response.ProductResponse;
import com.example.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;


   @PostMapping("/add")
    public ResponseEntity addProduct(@RequestBody ProductRequest productRequestDto){
       try {
           ProductResponse productResponse=productService.addProduct(productRequestDto);
           return new ResponseEntity(productResponse, HttpStatus.CREATED);
       }
       catch (Exception e){
           return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
       }
    }


    @PutMapping("/update-product-count")
    public ResponseEntity increaseParticularProductCount(@RequestBody IncreaseProductCountRequest increaseProductCountRequest){
       try {
           ProductResponse productResponse= productService.increaseParticularProductCount(increaseProductCountRequest);
           return new ResponseEntity(productResponse, HttpStatus.OK);
       }
       catch (Exception e){
           return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
       }
    }



    @GetMapping("/get-products-by-category/{category}")
    public ResponseEntity getAllProductsBYCategory(@PathVariable("category")String productCategory){
       try {
           List<ProductResponse> productResponseList=productService.getAllProductsBYCategory(productCategory);
           return new ResponseEntity(productResponseList, HttpStatus.FOUND);
       }
       catch (Exception e){
           return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
       }
    }


    @GetMapping("/get-all-products-of-seller")
    public ResponseEntity getAllProductsOfSeller(@RequestParam("email") String emailId){
       try{
           List<ProductResponse> productResponseList=productService.getAllProductsOfSeller(emailId);
           return new ResponseEntity(productResponseList, HttpStatus.ACCEPTED);
       }
       catch (Exception e){
           return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
       }
    }

    @GetMapping("/ten-cheapest-products")
    public List<ProductResponse> tenCheapestProducts(){
       return productService.tenCheapestProducts();
    }

    @GetMapping("/ten-costliest-products")
    public List<ProductResponse> tenCostliestProducts(){
       return productService.tenCostliestProducts();
    }

    @GetMapping("/get-all-out-of-stock-products")
    public ResponseEntity getAllOutOfStockProducts(){
       try {
           List<ProductResponse>productResponseList= productService.getAllOutOfStockProducts();
           return new ResponseEntity(productResponseList, HttpStatus.OK);
       }
       catch (Exception e){
           return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
       }
    }

    @GetMapping("/get-all-available-products")
    public  ResponseEntity getAllAvailableProducts(){
        try {
            List<ProductResponse> productResponseList= productService.getAllAvailableProducts();
            return new ResponseEntity(productResponseList, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/products-whose-quantity-is-less")
    public List<ProductResponse> productsWhoseQuantityIsLesser(@RequestParam("quantity") int quantity){
       return productService.productsWhoseQuantityIsLesser(quantity);
    }
    @GetMapping("/cheapest-product-in-category")
    public ResponseEntity cheapestProductOfParticularCategory(@RequestParam("category") String productCategory){
       try {
           ProductResponse productResponse= productService.cheapestProductOfCategory(productCategory);
           return new ResponseEntity(productResponse, HttpStatus.FOUND);
       }
       catch (Exception e){
           return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
       }
    }

    @GetMapping("/costliest-product-in-category")
    public ResponseEntity costliestProductOfParticularCategory(@RequestParam("category") String productCategory){
        try {
            ProductResponse productResponse= productService.costliestProductOfCategory(productCategory);
            return new ResponseEntity(productResponse, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Using Native Query
    @GetMapping("/of-category-and-minimum-price/{price}/{category}")
    public ResponseEntity getAllProductsByPriceAndCategory(
            @PathVariable("price") double price,
            @PathVariable("category") String productCategory){

        try {
            List<ProductResponse> productResponseList= productService.getAllProductsByPriceAndCategory(
                    price, productCategory);

            return new ResponseEntity(productResponseList, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    // Without Using Native Query Or Using JPA Standard Query
    @GetMapping("/using-category-and-minimum-price")
    public ResponseEntity getAllProductsUsingPriceAndCategory(
            @RequestParam("price") double price,
            @RequestParam("category") String productCategory){

        try {
            List<ProductResponse> productResponseList= productService.getAllProductsUsingPriceAndCategory(
                    price, productCategory);

            return new ResponseEntity(productResponseList, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }



    @DeleteMapping("/delete-particular-product-of-particular-seller")
    public ResponseEntity deleteParticularProductOfParticularSeller(@RequestBody DeleteProductOfSeller deleteProductOfSeller){
       try {
           String str=productService.deleteParticularProductOfParticularSeller(deleteProductOfSeller);
           return new ResponseEntity<>(str, HttpStatus.OK);
       }
       catch (Exception e){
           return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
       }
    }
}
