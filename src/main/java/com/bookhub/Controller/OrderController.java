package com.bookhub.Controller;

import com.bookhub.Service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

//    @PostMapping("/submitOrder")
//    public int submitOrder(@RequestBody OrderRequest orderRequest, @RequestHeader("Authorization") String token) {
//        return orderService.addOrderUsingStoredProcedure(
//                orderRequest.orderStatus,
//                orderRequest.paymentMethod,
//                orderRequest.shippingAddress,
//                orderRequest.orderItems,
//                token.replace("Bearer ", "")
//        );
//    }
    @PostMapping("/submitOrder")
    public ResponseEntity<Object> submitOrder(@RequestBody OrderRequest orderRequest, @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.CREATED).body("Order is successfully submitted. The order ID is: " +
                orderService.addOrderUsingStoredProcedure(orderRequest.orderStatus,
                        orderRequest.paymentMethod,
                        orderRequest.shippingAddress,
                        orderRequest.orderItems,
                        token.replace("Bearer ", "")));
    }
    record OrderRequest(int userId, String orderStatus,
            String paymentMethod,
             String shippingAddress, String orderItems, String token) {}

}
