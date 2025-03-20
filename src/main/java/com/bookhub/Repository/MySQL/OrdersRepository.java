package com.bookhub.Repository.MySQL;

import com.bookhub.Model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    Orders getUserIdByOrderId(Integer orderId);
    List<Orders> findByOrderId(Integer orderId);
    List<Orders> findByOrderStatus(String status);
}
