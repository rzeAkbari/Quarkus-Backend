package com.alpian.raziehakbari.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import com.alpian.raziehakbari.ExternalId;
import com.alpian.raziehakbari.ExternalIdReply;
import com.alpian.raziehakbari.ExternalIdRequest;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class CustomerGrpcServiceTest {

    @GrpcClient
    com.alpian.raziehakbari.ExternalId ExternalId;

    @Test
    public void whenGRPCGetExternalIdRequestIsMadeThenExternalIdIsReturned() {
        ExternalIdReply reply = ExternalId.getExternalId(ExternalIdRequest.newBuilder().setCustomerId(1).build())
                .await().atMost(Duration.ofSeconds(5));
        Assertions.assertNotNull(reply.getExternalId());
    }

}
