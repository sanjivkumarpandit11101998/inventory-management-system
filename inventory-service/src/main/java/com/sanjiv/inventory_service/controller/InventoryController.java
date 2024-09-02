package com.sanjiv.inventory_service.controller;


import com.sanjiv.inventory_service.dto.InventoryResponse;
import com.sanjiv.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // http://localhost:8082/api/inventory?sku-code=iphone-13&sku-code=iphone13-red
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam("skuCode") List<String> skuCode) throws InterruptedException {
//        System.out.println(skuCode);
        for(String str: skuCode){
            System.out.println(str);
        }
        List<InventoryResponse> inventoryResponsesResult= inventoryService.isInStock(skuCode);
//        System.out.println(inventoryResponsesResult);
        return inventoryResponsesResult;
//        return inventoryService.isInStock(skuCode);
    }

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public String isInStock(){
        return "Hello Ana";
    }
}
