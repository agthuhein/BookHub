package com.bookhub.Repository.MySQL;

import com.bookhub.Model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
}
