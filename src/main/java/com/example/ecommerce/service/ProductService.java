package com.example.ecommerce.service;


import com.example.ecommerce.Enum.ProductCategory;
import com.example.ecommerce.Enum.ProductStatus;
import com.example.ecommerce.dtos.request.DeleteProductOfSeller;
import com.example.ecommerce.dtos.request.IncreaseProductCountRequest;
import com.example.ecommerce.dtos.request.ProductRequest;
import com.example.ecommerce.dtos.response.ProductResponse;
import com.example.ecommerce.exception.InvalidProductException;
import com.example.ecommerce.exception.InvalidSellerException;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.Seller;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.SellerRepository;
import com.example.ecommerce.transformer.ProductTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ProductService {


    @Autowired
    ProductRepository productRepository;

    @Autowired
    SellerRepository sellerRepository;

    public ProductResponse addProduct(ProductRequest productRequest) throws InvalidSellerException {

        // Getting the Seller Object from the Seller DB
        Seller seller;
        try {
            seller=sellerRepository.findById(productRequest.getSellerId()).get();
        }
        catch (Exception e){
            throw new InvalidSellerException("Invalid Seller id!. Seller with the given id doesn't exist");
        }

        // Creating Product Object using Builder through ProductTransformer
        Product product= ProductTransformer.productRequestToProduct(productRequest);
        // Setting the Seller attribute of Product
        product.setSeller(seller);

        // Adding the Product to the Products List of Seller
        seller.getProducts().add(product);

        // Saving in the DB
        Product savedProduct=productRepository.save(product);
        sellerRepository.save(seller);

        // Creating ProductResponse using Builder through ProductTransformer
        return ProductTransformer.productToProductResponse(savedProduct);
    }


    public ProductResponse increaseParticularProductCount(IncreaseProductCountRequest increaseProductCountRequest) throws InvalidSellerException, InvalidProductException {
        // Getting Seller Object from the DB and also verifying whether the seller exist or not
        Seller seller;
        try {
            seller= sellerRepository.findById(increaseProductCountRequest.getSellerId()).get();
        }
        catch (Exception e){
            throw new InvalidSellerException("Invalid Seller id!");
        }

        // Getting Product Object from the DB and also verifying whether the product exist or not
        Product product;
        try {
            product= productRepository.findById(increaseProductCountRequest.getProductId()).get();
        }
        catch (Exception e){
            throw new InvalidProductException("Invalid Product Id!");
        }

        // Now Seller & Product both get verified

        // Now increasing the count of the product
        product.setQuantity(product.getQuantity()+ increaseProductCountRequest.getCount());

        product.setTotalQuantityAdded(product.getTotalQuantityAdded()+ increaseProductCountRequest.getCount());

        sellerRepository.save(seller); // it will also save the Product

        return ProductTransformer.productToProductResponse(product);

    }


    public List<ProductResponse> getAllProductsBYCategory(ProductCategory productCategory) throws InvalidProductException {

        // Getting List of all Products of that Category from Product DB
        List<Product> productList=productRepository.findByProductCategory(productCategory);

        // Checking whether the Products of that Category exist in the DB or Not
        if(productList.size()==0){
            throw new InvalidProductException("Invalid product category!. Product of this category doesn't exist!");
        }

        List<ProductResponse> productResponseList=new ArrayList<>();

        for (Product product:productList){
            productResponseList.add(ProductTransformer.productToProductResponse(product));
        }

        return productResponseList;
    }



    public List<ProductResponse> getAllProductsOfSeller(String emailId) throws InvalidSellerException {
        // Getting Seller Object from the Seller DB
        Seller seller=sellerRepository.findByEmailId(emailId);

        // Checking whether Seller exist or Not
        if(seller==null){
            throw new InvalidSellerException("Invalid Seller id!");
        }

        // Getting List of Products by the Seller
        List<Product> productList=seller.getProducts();

        List<ProductResponse> productResponseList=new ArrayList<>();

        // Creating ProductResponse using Builder through ProductTransformer
        for (Product product:productList){

            productResponseList.add(ProductTransformer.productToProductResponse(product));
        }

        return productResponseList;
    }


    public List<ProductResponse> tenCheapestProducts(){

        /*
        // 1st Approach:- Using CUSTOM QUERY
        List<Product> cheapestProductList=productRepository.tenCheapestProduct();
        // Note:- Here, We can get the Same Products(or Products with the Same Name) but by Different Seller

        */



        //  2nd Approach:- USING LOGIC
        // Getting all Products from the DB
        List<Product> productList=productRepository.findAll();

        // Sorting it in Ascending Order of Price
        Collections.sort(productList, (a,b)->{
            //return (int) (a.getPrice()- b.getPrice());
            return Double.compare(a.getPrice(), b.getPrice());
        });

        // Storing top 10 cheapest Products as ProductResponse in another List
        List<ProductResponse> cheapestProductList=new ArrayList<>();

        for(Product product:productList){

            // for checking whether the List contain the Same Product(or Products with the Same Name) or Not
            boolean isContain=false;
            for (ProductResponse pr:cheapestProductList){
                if (product.getName().equals(pr.getProductName())){
                    isContain=true;
                    break;
                }
            }

            if (!isContain){
                cheapestProductList.add(ProductTransformer.productToProductResponse(product));
            }

            if(cheapestProductList.size()==10) break;
        }

        return cheapestProductList;
    }


    public List<ProductResponse> tenCostliestProducts(){

        // Getting Products List from the DB
        List<Product> productList=productRepository.findAll();

        // Sorting the List in Descending Order of Price
        Collections.sort(productList, (a,b)->{
            return Double.compare(b.getPrice(), a.getPrice());
        });

        // Storing 10 costliest Products as ProductResponse in another List
        List<ProductResponse> costliestProducts=new ArrayList<>();

        for (Product product:productList){

            // If a Product is selling by more than 1 seller then selecting that product which is Costliest
            boolean isContain=false;
            for (ProductResponse pr:costliestProducts){
                if(product.getName().equals(pr.getProductName())){
                    isContain=true;
                    break;
                }
            }

            if(!isContain){
                // Conversion from Product to ProductResponse using Builder through ProductTransformer
                ProductResponse productResponse= ProductTransformer.productToProductResponse(product);
                costliestProducts.add(productResponse);
            }

            if(costliestProducts.size()==10) break;
        }

        return costliestProducts;

    }


    public List<ProductResponse> getAllOutOfStockProducts(ProductStatus productStatus){

        List<Product> productList= productRepository.findByProductStatus(productStatus);

        List<ProductResponse> productResponseList=new ArrayList<>();

        for (Product product:productList){
            productResponseList.add(ProductTransformer.productToProductResponse(product));
        }

        return productResponseList;
    }



    public List<ProductResponse> getAllAvailableProducts(ProductStatus productStatus){

        List<Product> productList= productRepository.findByProductStatus(productStatus);

        List<ProductResponse> productResponseList=new ArrayList<>();

        for (Product product:productList){
            productResponseList.add(ProductTransformer.productToProductResponse(product));
        }

        return productResponseList;
    }



    public List<ProductResponse> productsWhoseQuantityIsLesser(int quantity){
        // Getting those Products from the DB whose Quantity is lesser or equal to the given Quantity
        List<Product> productList= productRepository.productsWhoseQuantityIsLesser(quantity);

        List<ProductResponse> productResponseList=new ArrayList<>();

        for (Product product: productList){
            productResponseList.add(ProductTransformer.productToProductResponse(product));
        }

        return productResponseList;
    }


    public ProductResponse cheapestProductOfParticularCategory(String productCategory) throws InvalidProductException {

        Product product= productRepository.cheapestProductOfParticularCategory(productCategory);

        if (product==null){
            throw new InvalidProductException("Invalid product category!");
        }

        return ProductTransformer.productToProductResponse(product);
    }



    public ProductResponse costliestProductOfParticularCategory(String productCategory) throws InvalidProductException {

        Product product= productRepository.costliestProductOfParticularCategory(productCategory);

        if (product==null){
            throw new InvalidProductException("Invalid product category!");
        }

        return ProductTransformer.productToProductResponse(product);
    }



    // Using Native Query
    public List<ProductResponse> getAllProductsByPriceAndCategory(double price, String productCategory) throws InvalidProductException {

        // Checking Whether Product Category is Valid or Not
//        if(!productCategory.equals(ProductCategory.ELECTRONICS) && !productCategory.equals(ProductCategory.FOOD) &&
//            !productCategory.equals(ProductCategory.FASHION) && !productCategory.equals(ProductCategory.SPORTS) &&
//            !productCategory.equals(ProductCategory.BEAUTY) && !productCategory.equals(ProductCategory.CLOTH) &&
//            !productCategory.equals(ProductCategory.HOME) && !productCategory.equals(ProductCategory.MOBILE_LAPTOP)
//            && !productCategory.equals(ProductCategory.GROCERY)){
//            throw new InvalidProductCategory("Invalid Product Category!");
//        }

        List<Product> productList= productRepository.getAllProductsByPriceAndCategory(price, productCategory);

         List<ProductResponse> productResponseList= new ArrayList<>();

        for (Product product: productList){

            productResponseList.add(ProductTransformer.productToProductResponse(product));
        }

        return productResponseList;

    }




    // Without Using Native Query OR Using JPA STANDARD QUERY
    public List<ProductResponse> getAllProductsUsingPriceAndCategory(double price, ProductCategory productCategory){

        List<Product> productList= productRepository.getAllProductsUsingPriceAndCategory(price, productCategory);

        List<ProductResponse> productResponseList= new ArrayList<>();

        for (Product product: productList){

            productResponseList.add(ProductTransformer.productToProductResponse(product));
        }

        return productResponseList;

    }



    public String deleteParticularProductOfParticularSeller(DeleteProductOfSeller deleteProductOfSeller) throws InvalidSellerException, InvalidProductException {
        // Getting Seller Object from the DB
        Seller seller;
        try {
            seller=sellerRepository.findById(deleteProductOfSeller.getSellerId()).get();
        }
        catch (Exception e){
            throw new InvalidSellerException("Invalid Seller id");
        }
        // Now seller is valid


        // Getting the Product Object from the DB
        Product product;
        try {
            product=productRepository.findById(deleteProductOfSeller.getProductId()).get();
        }
        catch (Exception e){
            throw new InvalidProductException("Invalid Product Id!");
        }
        // Now Product is valid

        // Removing The Product for the DB
        productRepository.delete(product);    // It will also remove the Product from the List of Products of Seller


        // Saving the Seller Object in the Db
        sellerRepository.save(seller);  // It will also save Product in the Db because of CASCADE Operation

        return "Product " +product.getName()+ " of Seller " +seller.getName()+ " deleted successfully";
    }
}
