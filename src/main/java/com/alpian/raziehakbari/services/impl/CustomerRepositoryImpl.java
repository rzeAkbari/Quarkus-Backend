package com.alpian.raziehakbari.services.impl;

import com.alpian.raziehakbari.entities.Customer;
import com.alpian.raziehakbari.exception.DemoInvalidRequestException;
import com.alpian.raziehakbari.services.CustomerRepository;
import io.smallrye.mutiny.Uni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@ApplicationScoped
public class CustomerRepositoryImpl implements CustomerRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRepositoryImpl.class);

    @Override
    public Uni<String> getCustomerExternalId(long customerId) {
        return Uni.createFrom()
                .item(()-> getExternalId(customerId))
                .onFailure()
                .recoverWithItem("Not_Found");
    }

    @Override
    @Transactional
    public Uni<String> storeNewCustomer(long customerId, String createdAt) {
        return Uni.createFrom()
                .item(() -> {
                    var customer = createNewCustomer(customerId, createdAt);
                    var externalId = Customer.storeNewCustomer(customer);
                    return externalId;
                })
                .onFailure()
                .transform(throwable ->
                        new DemoInvalidRequestException(throwable.getMessage()));
    }

    private String getExternalId(long customerId) throws NullPointerException{
        var customer = Customer.getCustomerByCustomerId(customerId);
        return customer.externalId;
    }

    private Customer createNewCustomer(long customerId, String createdAt){

        var customer = new Customer();
        customer.customerId = customerId;
        customer.createdAt = getLocalDate(createdAt);
        customer.externalId = UUID.randomUUID().toString();
        return customer;
    }

    private LocalDate getLocalDate(String createdAt) throws DateTimeParseException {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        var date = LocalDate.parse(createdAt, formatter);
        if(date.isAfter(LocalDate.now())) {
            int INVALID_INDEX = 0;
            throw new DateTimeParseException(
                    "Created Date is in the future",
                    date.toString(),
                    INVALID_INDEX);
        }
        return  date;
    }

}
