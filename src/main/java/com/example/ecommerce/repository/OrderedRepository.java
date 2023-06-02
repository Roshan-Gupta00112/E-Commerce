package com.example.ecommerce.repository;

import com.example.ecommerce.model.Ordered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderedRepository extends JpaRepository<Ordered, Integer> {

    Ordered findByOrderNo(String orderNo);


    @Query(value = "SELECT * FROM Ordered WHERE total_order_value= (SELECT MAX(total_order_value) FROM Ordered) " +
            "AND order_status='PLACED' ORDER BY order_date", nativeQuery = true)
    List<Ordered> highestOrderValue();



}
