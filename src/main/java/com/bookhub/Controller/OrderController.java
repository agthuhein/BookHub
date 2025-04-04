package com.bookhub.Controller;

import com.bookhub.CustomException.OrderNotFoundException;
import com.bookhub.CustomException.ResourceNotFoundException;
import com.bookhub.Model.Books;
import com.bookhub.Model.Orders;
import com.bookhub.Service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @GetMapping("/admin/getAllOrder")
    public ResponseEntity<Object> getAllOrders() {
        try {
            List<OrderService.OrderDTO> orders = orderService.getAllOrders();
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>("An error occurred while fetching all the books.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/admin/getOrderById/{orderId}")
    public ResponseEntity<Object> getOrderById(@PathVariable Integer orderId) {
        try{
            List<OrderService.OrderDTO> orders = orderService.getOrderByOrderId(orderId);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        catch (OrderNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (Exception e) {
            return new ResponseEntity<>("An error occurred while fetching the order.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/admin/getOrderByStatus")
    public ResponseEntity<Object> getOrderByStatus(@RequestParam String status) {
        try{
            List<OrderService.OrderDTO> orders = orderService.getOrderByStatus(status.toLowerCase());
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        catch (OrderNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (Exception e) {
            return new ResponseEntity<>("An error occurred while fetching the order.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/submitOrder")
    public ResponseEntity<Object> submitOrder(@RequestBody OrderRequest orderRequest, @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.CREATED).body("Order is successfully submitted. The order ID is: " +
                orderService.addOrderUsingStoredProcedure(
                        orderRequest.paymentMethod,
                        orderRequest.shippingAddress,
                        orderRequest.orderItems,
                        token.replace("Bearer ", "")));
    }
    record OrderRequest(int userId, String orderStatus,
            String paymentMethod,
             String shippingAddress, String orderItems, String token) {}


    //Update Order Status
    @PutMapping("/admin/updateOrderStatus/{orderId}")
    public ResponseEntity<Object> updateOrderStatus(@PathVariable("orderId") Integer orderId,
                                                    @RequestBody OrderStatusRequest orderStatusRequest,
                                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("errors", bindingResult.getFieldErrors()
                    .stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList())));
        }
        try{
            orderService.updateOrderStatus(orderId, orderStatusRequest.orderStatus);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully updated the order status.");
        }
        catch (ResourceNotFoundException e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating status. " + e.getMessage());
        }
    }
    record OrderStatusRequest(String orderStatus) {}
}
