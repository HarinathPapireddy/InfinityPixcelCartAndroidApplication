# InfinityPixcelCartAndroidApplication

Welcome to the E-commerce Android App! This Java-based application serves as the frontend for our E-commerce platform, interacting with the backend developed using Spring Boot, Maven, Hibernate, Spring Security, and Redis. RESTful API requests are sent through Retrofit.

## Features

- User authentication and authorization using JWT tokens.
- Product browsing and search functionality.
- Shopping cart management.
- Order placement and tracking.
- Integration with the backend E-commerce API using Retrofit.

## Configuration

Adjust configuration settings in the `app/src/main/res/values/strings.xml` file, such as API endpoint URLs.

### Retrofit Setup

Retrofit is used for making RESTful API requests. Check the Retrofit configurations in the `app/src/main/java/com/example/service/RetrofitService.java` file.

### Authentication

The app uses JWT token-based authentication to interact with the backend. Ensure that the backend is configured to generate and validate JWT tokens.

## Usage

Use the app to browse products, add items to the cart, and place orders. Explore different features and functionalities offered by the InfinityCart
