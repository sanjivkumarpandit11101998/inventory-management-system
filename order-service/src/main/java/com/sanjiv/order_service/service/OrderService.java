package com.sanjiv.order_service.service;

import com.sanjiv.order_service.dto.InventoryResponse;
import com.sanjiv.order_service.dto.OrderLineItemsDto;
import com.sanjiv.order_service.dto.OrderRequest;
import com.sanjiv.order_service.event.OrderPlacedEvent;
import com.sanjiv.order_service.model.Order;
import com.sanjiv.order_service.model.OrderLineItems;
import com.sanjiv.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    private final WebClient.Builder webClientBuilder;

    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public String placeOrder(OrderRequest orderRequest){
        Order order= new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

//        System.out.println(orderRequest.toString());

        List<OrderLineItems> orderLineItems= orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItemsList(orderLineItems);

//        System.out.println("orderLineItems"+orderLineItems.toString());
//        System.out.println("orderLineItems");


        List<String> skuCodes= order.getOrderLineItemsList()
                .stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

//        System.out.println(skuCodes);

        // call Inventory Services, and place order if Product is in stock
        InventoryResponse[] inventoryResponsesArray= webClientBuilder.build().get()
                .uri("http://INVENTORY-SERVICE/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

//        assert inventoryResponsesArray != null;
//        for(InventoryResponse inventoryResponse: inventoryResponsesArray){
//            System.out.println(inventoryResponse.getSkuCode());
//        }
//        System.out.println(inventoryResponsesArray.length);

        boolean allProductInStock= Arrays.stream(inventoryResponsesArray)
                .allMatch(InventoryResponse::isInStock);

//        System.out.println(allProductInStock);
//        System.out.println(allProductInStock);

        if(allProductInStock){
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
            return  "Order Placed Successfully";
        }else{
            throw new IllegalArgumentException("Product Is Not In Stock");
        }
//        throw new IllegalArgumentException("Product Is Not In Stock");

//        orderRepository.save(order);

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems= new OrderLineItems();
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        return orderLineItems;
    }
}
