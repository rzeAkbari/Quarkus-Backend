package com.alpian.raziehakbari.controllers;

import com.alpian.raziehakbari.*;
import com.alpian.raziehakbari.exception.DemoInvalidRequestException;
import com.alpian.raziehakbari.impl.CustomerRepositoryImpl;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

@GrpcService
public class CustomerGrpcService implements ExternalId {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerGrpcService.class);

    @Inject
    CustomerRepositoryImpl customerRepositoryImpl;

    @Override
    public Uni<ExternalIdReply> getExternalId(ExternalIdRequest request) {
        return customerRepositoryImpl.getCustomerExternalId(request.getCustomerId())
                .onItem()
                .transform(externalId ->
                    ExternalIdReply.newBuilder()
                            .setExternalId(externalId)
                            .build())
                .onFailure()
                .transform(this::mapException);

    }

    @Override
    public Uni<StoreCustomerReply> storeNewCustomer(StoreCustomerRequest request) {
        return customerRepositoryImpl.storeNewCustomer(request.getCustomerId(),
                request.getCreatedAt())
                .onItem()
                .transform(externalId -> StoreCustomerReply
                        .newBuilder()
                        .setExternalId(externalId).build())
                .onFailure()
                .transform(this::mapException);

    }

    private StatusRuntimeException mapException(Throwable throwable){
        LOGGER.error("Error during communication with customer service");
        var metadata = createExceptionMetadata(throwable);

        try {
            throw throwable;
        }catch (DemoInvalidRequestException ex){
            return new StatusRuntimeException(Status.INVALID_ARGUMENT, metadata);
        }catch (Throwable ex){
            return new StatusRuntimeException(Status.INTERNAL,metadata);
        }
    }

    private Metadata createExceptionMetadata(Throwable throwable){
        var metadata = new Metadata();
        Optional.ofNullable(throwable.getMessage()).ifPresent(message -> {
            metadata.put(Metadata.Key.of(
                    "ExceptionMessage"
                    ,Metadata.ASCII_STRING_MARSHALLER)
                    ,message);
        });
        return metadata;
    }
}
