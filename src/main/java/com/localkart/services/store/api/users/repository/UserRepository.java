package com.localkart.services.store.api.users.repository;

import com.localkart.services.store.api.products.entity.ProductEntity;
import com.localkart.services.store.api.users.entity.UserEntity;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;
import java.util.Optional;

@Component
public class UserRepository {

    private DynamoDbClient dynamoDbClient;
    private DynamoDbEnhancedClient enhancedClient;

    private DynamoDbTable<UserEntity> usersTable;

    @PostConstruct
    public void init() {

        dynamoDbClient = DynamoDbClient.builder()
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();

        usersTable = enhancedClient.table("users", TableSchema.fromBean(UserEntity.class));
    }

    public Optional<UserEntity> getUserByPhoneNumber(String phoneNumber) {

        QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder().partitionValue(phoneNumber).build());

        QueryEnhancedRequest query = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .build();

        return usersTable.query(query)
                .items()
                .stream()
                .findFirst();
    }

    public void saveUser(UserEntity userEntity) {

        usersTable.putItem(userEntity);
    }

    public UserEntity deleteUser(String phoneNumber) {

        return usersTable.deleteItem(
                Key.builder().partitionValue(phoneNumber).build()
        );
    }

    public List<UserEntity> getAllUsers() {

        return usersTable.scan().items().stream().toList();
    }
}
