package com.alpian.raziehakbari.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import java.time.LocalDate;

/**
 * Example JPA entity.
 *
 * To use it, get access to a JPA EntityManager via injection.
 *
 * {@code
 *     @Inject
 *     EntityManager em;
 *
 *     public void doSomething() {
 *         MyEntity entity1 = new MyEntity();
 *         entity1.setField("field-1");
 *         em.persist(entity1);
 *
 *         List<MyEntity> entities = em.createQuery("from MyEntity", MyEntity.class).getResultList();
 *     }
 * }
 */
@Entity
public class Customer extends PanacheEntity {

    public Long customerId;
    public String externalId;
    public LocalDate createdAt;

    public static Customer getCustomerByCustomerId(long customerId){
        return find("customerId", customerId).firstResult();
    }

    public static String storeNewCustomer(Customer customer){
         persist(customer);
         return customer.externalId;
    }
}
