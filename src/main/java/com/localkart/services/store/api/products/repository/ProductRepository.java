package com.localkart.services.store.api.products.repository;

import com.localkart.services.store.api.products.entity.ProductEntity;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchGetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Component
public class ProductRepository {

    private final Logger LOG = LoggerFactory.getLogger(ProductRepository.class);

    private DynamoDbClient dynamoDbClient;
    private DynamoDbEnhancedClient enhancedClient;

    private DynamoDbTable<ProductEntity> productTable;

    @PostConstruct
    public void init() {

        dynamoDbClient = DynamoDbClient.builder()
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();

        productTable = enhancedClient.table("Products", TableSchema.fromBean(ProductEntity.class));
    }

    public List<ProductEntity> getProducts() {

        var filterExpression = Expression.builder().expression("stock >= :stockValue")
                .expressionValues(Map.of(":stockValue", AttributeValue.fromN("1")))
                .build();
        var request = ScanEnhancedRequest.builder()
                .filterExpression(filterExpression)
                .build();
        return productTable.scan(request).items().stream()
                .sorted(Comparator.comparing(ProductEntity::getProductId))
                .toList();
    }

    public List<ProductEntity> getOffers() {

        LOG.info("Calling DB to fetch offers");
        var filterExpression = Expression.builder().expression("stock >= :stockValue AND attribute_exists(discount) AND discount <> :emptyString")
                .expressionValues(Map.of(":stockValue", AttributeValue.fromN("1"), ":emptyString", AttributeValue.fromS("")))
                .build();
        var request = ScanEnhancedRequest.builder()
                .filterExpression(filterExpression)
                .build();
        return productTable.scan(request).items().stream()
                .sorted(Comparator.comparing(ProductEntity::getProductId))
                .toList();
    }

    public ProductEntity getProduct(String productId) {

        QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder().partitionValue(productId).build());

        QueryEnhancedRequest query = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .build();

        return productTable.query(query).items()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

}
