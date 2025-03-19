package com.bookhub.Service;

import com.bookhub.CustomException.BookNotFoundException;
import com.bookhub.CustomException.BookOutOfStockException;
import com.bookhub.CustomException.NotEnoughStockException;
import com.bookhub.Security.JwtUtil;
import jakarta.transaction.Transactional;
import java.sql.SQLException;
import java.sql.Types;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final JdbcTemplate jdbcTemplate;
    private final JwtUtil jwtUtil;

    public OrderService(JdbcTemplate jdbcTemplate, JwtUtil jwtUtil) {
        this.jdbcTemplate = jdbcTemplate;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public int addOrderUsingStoredProcedure(String orderStatus, String paymentMethod, String shippingAddress, String orderItemsJson, String token) {
        Integer userId = jwtUtil.extractUserId(token);
        System.out.println(userId);
        // Call the stored procedure using JdbcTemplate
        String storedProcedureName = "AddOrder";
        try{
            // Setting up input and output parameters
            Map<String, Object> parameters = jdbcTemplate.call(
                    connection -> {
                        // Prepare the stored procedure call
                        java.sql.CallableStatement callableStatement = connection.prepareCall("{call " + storedProcedureName + "(?, ?, ?, ?, ?, ?)}");

                        // Set input parameters
                        callableStatement.setInt(1, userId);
                        callableStatement.setString(2, orderStatus);
                        callableStatement.setString(3, paymentMethod);
                        callableStatement.setString(4, shippingAddress);
                        callableStatement.setString(5, orderItemsJson);

                        // Set output parameter
                        callableStatement.registerOutParameter(6, Types.INTEGER);
                        return callableStatement;
                    },
                    List.of(
                            new SqlParameter("p_user_id", Types.INTEGER),
                            new SqlParameter("p_order_status", Types.VARCHAR),
                            new SqlParameter("p_payment_method", Types.VARCHAR),
                            new SqlParameter("p_shipping_address", Types.VARCHAR),
                            new SqlParameter("p_order_items", Types.VARCHAR),
                            new SqlOutParameter("p_new_order_id", Types.INTEGER)
                    )
            );

            // Get the result from the output parameter (the new order ID)
            return (int) parameters.get("p_new_order_id");
        }
        catch (DataAccessException e) {
            // Catch SQLException and check if it's a custom error raised by SIGNAL in stored procedure
            if (e.getMessage().contains("Book is out of stock")) {
                throw new BookOutOfStockException("Error: Book is out of stock.");
            } else if (e.getMessage().contains("Not enough stock available")) {
                throw new NotEnoughStockException("Error: Not enough stock available.");
            } else if (e.getMessage().contains("Book does not exist")) {
                throw new BookNotFoundException("Error: Book does not exist.");
            } else {
                // For other SQL errors, rethrow the exception
                throw new RuntimeException("An unexpected error occurred while processing the order.", e);
            }
        }

    }

}
