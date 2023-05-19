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

    @Query(value = "SELECT * from PRODUCT p ORDER BY p.price LIMIT 10", nativeQuery = true)
    List<Product> tenCheapestProduct();

    List<Product> findByProductStatus(ProductStatus productStatus);

    @Query(value = "SELECT * FROM product p WHERE p.quantity<=:quantity", nativeQuery = true)
    List<Product> productsWhoseQuantityIsLesser(int quantity);


    @Query(value = "SELECT * FROM product p WHERE p.product_category=:productCategory ORDER BY p.price LIMIT 1",
            nativeQuery = true)
    Product cheapestProductOfParticularCategory(ProductCategory productCategory);


    @Query(value = "SELECT * FROM product p WHERE p.product_category=:productCategory ORDER BY p.price DESC LIMIT 1",
            nativeQuery = true)
    Product costliestProductOfParticularCategory(ProductCategory productCategory);
 }
