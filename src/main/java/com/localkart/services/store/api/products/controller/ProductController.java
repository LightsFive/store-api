package com.localkart.services.store.api.products.controller;

import com.localkart.services.store.api.products.entity.ProductEntity;
import com.localkart.services.store.api.products.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @GetMapping("/offers")
    public ResponseEntity<List<ProductEntity>> getProducts() {
        LOG.info("Fetch offers");
        List<ProductEntity> offers = productService.getOffers();
        LOG.info("Fetched offers {}", offers.size());
        return ResponseEntity.ok().cacheControl(CacheControl.maxAge(60, TimeUnit.MINUTES)).body(offers);
    }

    @GetMapping
    public List<ProductEntity> searchProducts(@RequestParam String searchName) {
        LOG.info("Fetching products for {}", searchName);

        return productService.searchProducts(searchName);
    }

    @GetMapping("/getByCategory")
    public Map<String, List<ProductEntity>> getByCategory() {
        LOG.info("Fetch Products By Category");
        return productService.getByCategory();
    }

    @GetMapping("/{productId}")
    public ProductEntity getProduct(@PathVariable String productId) {
        return productService.getProduct(productId);
    }

    @PostMapping("/payment")
    public String makePayment(String name) {
        return "SUCCESS";
    }
}
