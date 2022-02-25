package com.alpian.raziehakbari.services;

import io.smallrye.mutiny.Uni;

public interface CustomerRepository {
    Uni<String> getCustomerExternalId(long customerId);
    Uni<String> storeNewCustomer(long customerId , String createdAt);
}
