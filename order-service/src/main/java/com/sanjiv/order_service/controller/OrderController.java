package com.sanjiv.order_service.controller;

import com.sanjiv.order_service.dto.InventoryResponse;
import com.sanjiv.order_service.dto.OrderRequest;
import com.sanjiv.order_service.model.Order;
import com.sanjiv.order_service.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final WebClient webClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "inventory")
    // CompletableFuture is synchronous
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest){
        System.out.println(orderRequest.toString());
        String response= orderService.placeOrder(orderRequest);
        return CompletableFuture.supplyAsync(()-> response);
    }

    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(()->"Oops! Something went wrong, please order after some time~!");
    }

    @GetMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String test(){
//        String result= webClient.get()
//                .uri("http://localhost:8082/api/inventory/test")
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
        return "result";
    }

}
