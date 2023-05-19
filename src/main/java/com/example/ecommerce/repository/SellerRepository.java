package com.example.ecommerce.repository;

import com.example.ecommerce.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Integer> {

    List<Seller> findByName(String name);
    Seller findByEmailId(String emailId);
    Seller findByMobNo(String mobNo);


}
