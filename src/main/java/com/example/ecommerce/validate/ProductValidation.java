package com.example.ecommerce.validate;

import com.example.ecommerce.Enum.ProductStatus;
import com.example.ecommerce.exception.InvalidProductException;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;

@UtilityClass
public class ProductValidation {

    @Autowired
    ProductRepository productRepository;

    public Product validateProductIdAndStatus(int productId) throws InvalidProductException {

        Product product;

        // 1st Step:- Fetching the product with validating product id
        product= productRepository.findById(productId).get();
        if(product==null){
            throw new InvalidProductException("Invalid product id!");
        }

        // 2nd Step:- Validating Product status
        if(product.getProductStatus()== ProductStatus.OUT_OF_STOCK){
            throw new InvalidProductException("Product is currently out of stock!");
        }

        return product;
    }
}
