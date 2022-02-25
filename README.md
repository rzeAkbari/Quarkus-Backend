# Razieh Akbari Demo Alpian
This is a quarkus project, written with quarkus reactive and mutiny.
the service endpoints are based on GRPC protocol. 
There are two endpoints:
- one to get a customer externalId by providing
the customerId.
- another to post a new customer passing customerId and the date of creation.

An H2 in memory database is used as a database, and is populated with
initial data.
## Run the Application
simply use <b>./mvnw clean quarkus:dev</b>, you can see in the console
the creation if H2 and it's population. 

## Run the Tests
Check it the integration tests to have an idea of the implementation
you can run them using ./mvnw clean verify.


