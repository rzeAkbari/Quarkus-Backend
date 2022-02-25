package com.alpian.raziehakbari.test.integration;

import com.alpian.raziehakbari.*;
import io.grpc.StatusRuntimeException;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDate;

@QuarkusTest
public class CustomerRpcIT{

    @Inject
    @GrpcClient("customer")
    ExternalIdGrpc.ExternalIdBlockingStub client;

    @Test
    void whenGetExternalIdAPIIsCalled_thenCustomerExternalIdIsReturned(){
        var request = ExternalIdRequest.newBuilder().setCustomerId(1).build();
        var response = client.getExternalId(request);
        Assertions.assertInstanceOf(ExternalIdReply.class, response);
        Assertions.assertEquals(response.getExternalId(), "10");
    }

    @Test
    void whenGetExternalIdAPIIsCalled_andCustomerDoesNotExist_thenEmptyStringIsReturned(){
        var request = ExternalIdRequest.newBuilder().setCustomerId(100).build();
        var response = client.getExternalId(request);
        Assertions.assertInstanceOf(ExternalIdReply.class, response);
        Assertions.assertEquals(response.getExternalId(), "Not_Found");
    }

    @Test
    void whenStoreCustomerAPIIsCalled_thenCustomerIsStoredAndExternalIdIsReturned(){
        var request = StoreCustomerRequest.newBuilder()
                .setCustomerId(7)
                .setCreatedAt(LocalDate.now().toString())
                .build();
        var response = client.storeNewCustomer(request);
        Assertions.assertInstanceOf(StoreCustomerReply.class, response);
        Assertions.assertNotNull(response.getExternalId());
    }

    @Test
    void whenStoreCustomerAPIIsCalled_andCreatedAtIsNotValid_thenACorrectMessageIsReturned(){
        var request = StoreCustomerRequest.newBuilder()
                .setCustomerId(7)
                .setCreatedAt("Invalid_Time")
                .build();
        Assertions.assertThrowsExactly(
                StatusRuntimeException.class,
                ()-> client.storeNewCustomer(request));
    }

    @Test
    void whenStoreCustomerAPIIsCalled_andCreatedAtIsInFuture_thenACorrectMessageIsReturned(){
        var request = StoreCustomerRequest.newBuilder()
                .setCustomerId(7)
                .setCreatedAt("2025-10-01")
                .build();
        Assertions.assertThrowsExactly(
                StatusRuntimeException.class,
                ()-> client.storeNewCustomer(request));
    }
}
