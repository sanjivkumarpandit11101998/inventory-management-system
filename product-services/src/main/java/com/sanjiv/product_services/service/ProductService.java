package com.sanjiv.product_services.service;


import com.sanjiv.product_services.dto.ProductRequest;
import com.sanjiv.product_services.dto.ProductResponse;
import com.sanjiv.product_services.model.Product;
import com.sanjiv.product_services.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest){
        Product product= Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);

        log.info("Product is saved {}", product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products= productRepository.findAll();

        return products.stream().map(product -> {
            return ProductResponse.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .build();
        }).toList();
    }
}
