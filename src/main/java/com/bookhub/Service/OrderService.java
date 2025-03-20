package com.bookhub.Service;

import com.bookhub.CustomException.BookNotFoundException;
import com.bookhub.CustomException.BookOutOfStockException;
import com.bookhub.CustomException.NotEnoughStockException;
import com.bookhub.Model.Orders;
import com.bookhub.Repository.MySQL.BooksRepository;
import com.bookhub.Repository.MySQL.OrderItemsRepository;
import com.bookhub.Repository.MySQL.OrdersRepository;
import com.bookhub.Repository.MySQL.UsersRepository;
import com.bookhub.Security.JwtUtil;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.sql.Types;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final JdbcTemplate jdbcTemplate;
    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final UsersRepository usersRepository;
    private final BooksRepository booksRepository;
    private final JwtUtil jwtUtil;

    public OrderService(JdbcTemplate jdbcTemplate, JwtUtil jwtUtil, OrdersRepository ordersRepository,
                        OrderItemsRepository orderItemsRepository,
                        UsersRepository usersRepository, BooksRepository booksRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.jwtUtil = jwtUtil;
        this.ordersRepository = ordersRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.usersRepository = usersRepository;
        this.booksRepository = booksRepository;
    }

    @Transactional
    public List<OrderDTO> getAllOrders() {
        List<Orders> orders = ordersRepository.findAll();
        return ordersList(orders);
    }

    @Transactional
    public void updateOrderStatus(Integer orderId, String newStatus) {
        if(!newStatus.isEmpty()) {
            if(!ordersRepository.existsById(orderId)) {
                throw new RuntimeException("Order ID: " + orderId + " does not exist.");
            }
            Orders orders = ordersRepository.findById(orderId).get();
            if(newStatus.equalsIgnoreCase("cancelled")){
                if(orders.getOrderStatus().equalsIgnoreCase("cancelled")){
                    throw new RuntimeException("Order ID: " + orderId + " is already cancelled.");
                }
                orders.setOrderStatus("cancelled");
            }
            else if(newStatus.equalsIgnoreCase("confirmed")
            || newStatus.equalsIgnoreCase("processing")
            || newStatus.equalsIgnoreCase("shipped")
            || newStatus.equalsIgnoreCase("delivered")
            || newStatus.equalsIgnoreCase("completed")){
                if(orders.getOrderStatus().equalsIgnoreCase("cancelled")){
                    throw new RuntimeException("Order ID: " + orderId + " is already cancelled.");
                }
                orders.setOrderStatus(newStatus);
            }
            else {
                throw new RuntimeException("The order status is not correct.");
            }
            try{
                ordersRepository.save(orders);
            }
            catch (DataIntegrityViolationException e) {
                throw new RuntimeException("This order has already been cancelled.");
            }
            catch (JpaSystemException | PersistenceException e) {  // CATCH JPA EXCEPTION
                throw new RuntimeException("This order has already been cancelled.");
            }
            catch(DataAccessException e){
                throw new RuntimeException("Database access error occurred while updating order status.", e);
            }
            catch(Exception e){
                throw new RuntimeException("An unexpected error occurred while updating order status.", e);
            }
        }
        else {
            throw new RuntimeException("Order is empty!");
        }
    }

    @Transactional
    public int addOrderUsingStoredProcedure(String paymentMethod, String shippingAddress, String orderItemsJson, String token) {
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
                        callableStatement.setString(2, "pending");
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
    public record OrderDTO(
            int orderId,
            int userId,
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            String orderDate,
            BigDecimal totalAmount,
            String orderStatus,
            String paymentMethod,
            String shippingAddress,
            List<BookInfoDTO> books
    ) {
        public record BookInfoDTO(int bookId, String bookTitle, int orderQuantity, String pricePerUnit) {}
    }

    public List<OrderDTO> ordersList(List<Orders> orders) {
        return orders.stream().map(order -> {
            List<OrderDTO.BookInfoDTO> bookInfo = order.getOrderItems().stream().map(orderItem -> {
                BigDecimal pricePerUnit = orderItem.getPricePerUnit();
                String priceStr = pricePerUnit.toPlainString();

                return new OrderDTO.BookInfoDTO(
                        orderItem.getBooks().getBookId(),
                        orderItem.getBooks().getTitle(),
                        orderItem.getOrderQuantity(),
                        priceStr

                );
            }).collect(Collectors.toList());

            return new OrderDTO(
                    order.getOrderId(),
                    order.getUsers().getUserId(),
                    order.getUsers().getFirstName(),
                    order.getUsers().getLastName(),
                    order.getUsers().getEmail(),
                    order.getUsers().getPhoneNumber(),
                    order.getOrderDate().toString(),
                    order.getTotalAmount(),
                    order.getOrderStatus(),
                    order.getPaymentMethod(),
                    order.getShippingAddress(),
                    bookInfo
            );
        }).collect(Collectors.toList());
    }
}

