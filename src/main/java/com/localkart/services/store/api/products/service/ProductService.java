package com.localkart.services.store.api.products.service;

import com.localkart.services.store.api.products.entity.ProductEntity;
import com.localkart.services.store.api.products.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final Logger LOG = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    @Cacheable(value = "product-offers")
    public List<ProductEntity> getOffers() {

        return productRepository.getOffers();
    }

    @Cacheable(value = "search-products", key = "#searchName")
    public List<ProductEntity> searchProducts(String searchName) {
        LOG.info("Searchin for products in DB with {}", searchName);
        return productRepository.getProducts().stream()
                .filter(product -> product.getProductName().contains(searchName))
                .toList();
    }

    public Map<String, List<ProductEntity>> getByCategory() {

        return productRepository.getProducts()
                .stream()
                .collect(Collectors.groupingBy(ProductEntity::getCategory, Collectors.toList()));
    }

    public ProductEntity getProduct(String productId) {

        return productRepository.getProduct(productId);
    }
}
