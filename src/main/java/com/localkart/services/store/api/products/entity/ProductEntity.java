package com.localkart.services.store.api.products.entity;

import lombok.*;
import lombok.extern.jackson.Jacksonized;
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

@DynamoDbBean
@Builder
public class ProductEntity {

    private String productId;
    private String productName;
    private String category;
    private String description;
    private String discount;
    private String imageURL;
    //private Map<String, Object> netQuantity;
    private Double price;
    private Integer stock;

    @DynamoDbPartitionKey
    public String getProductId() {
        return this.productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = {"productName-category-index"})
    public String getProductName() {
        return this.productName;
    }

    @DynamoDbSecondarySortKey(indexNames = {"productName-category-index"})
    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    /*public Map<String, Object> getNetQuantity() {
        return netQuantity;
    }

    public void setNetQuantity(Map<String, Object> netQuantity) {
        this.netQuantity = netQuantity;
    }*/

    public ProductEntity() {
    }

    public ProductEntity(String productId, String productName, String category, String description, String discount, String imageURL, Double price, Integer stock) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.description = description;
        this.discount = discount;
        this.imageURL = imageURL;
        this.price = price;
        this.stock = stock;
    }
}
