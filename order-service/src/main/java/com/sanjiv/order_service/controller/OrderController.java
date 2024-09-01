package com.sanjiv.order_service.controller;

import com.sanjiv.order_service.dto.InventoryResponse;
import com.sanjiv.order_service.dto.OrderRequest;
import com.sanjiv.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final WebClient webClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        System.out.println(orderRequest.toString());
        orderService.placeOrder(orderRequest);
        return "Order Placed Successfully";
    }


    @GetMapping("/test")
    @ResponseStatus(HttpStatus.CREATED)
    public String test(){
        String result= webClient.get()
                .uri("http://localhost:8082/api/inventory/test")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return result;
    }

}
