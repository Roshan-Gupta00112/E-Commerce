package com.example.ecommerce.repository;

import com.example.ecommerce.Enum.ProductCategory;
import com.example.ecommerce.Enum.ProductStatus;
import com.example.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByProductCategory(ProductCategory productCategory);

    @Query(value = "SELECT DISTINCT p from PRODUCT p WHERE  p.product_ status='AVAILABLE' ORDER BY p.price LIMIT 10", nativeQuery = true)
    List<Product> tenCheapestProduct();

    List<Product> findByProductStatus(ProductStatus productStatus);

    @Query(value = "SELECT * FROM product p WHERE p.quantity<=:quantity AND p.product_ status='AVAILABLE'", nativeQuery = true)
    List<Product> productsWhoseQuantityIsLesser(int quantity);


    @Query(value = "SELECT * FROM product p WHERE p.product_category=:productCategory AND p.product_ status='AVAILABLE' ORDER BY p.price LIMIT 1",
            nativeQuery = true)
    Product cheapestProductOfCategory(String productCategory);


    @Query(value = "SELECT * FROM product p WHERE p.product_category=:productCategory AND p.product_ status='AVAILABLE' ORDER BY p.price DESC LIMIT 1",
            nativeQuery = true)
    Product costliestProductOfCategory(String productCategory);


    // Using Native Query
    @Query(value = "SELECT * FROM PRODUCT p WHERE p.price>=:price AND p.product_category=:productCategory AND p.product_ status='AVAILABLE'",
            nativeQuery = true)
    List<Product> getAllProductsByPriceAndCategory(double price, String productCategory);


    // Using JPA STANDARD QUERY Or Without using Native Query
    @Query(value = "SELECT p FROM Product p WHERE p.price>=:price AND p.productCategory=:productCategory AND p.productStatus='AVAILABLE'")
    List<Product> getAllProductsUsingPriceAndCategory(double price, ProductCategory productCategory);
 }
