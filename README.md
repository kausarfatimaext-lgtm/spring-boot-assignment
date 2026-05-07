# Microservices Mini Platform

## Problem Statement

As modern applications scale, monolithic architectures often become difficult to maintain, deploy, and scale effectively. The challenge is to design a small distributed system where distinct business domains (Products, Orders, and Notifications) are completely decoupled. 

These domains must run independently in their own environments, manage their own data, but still seamlessly communicate with one another to execute a complete business transaction (e.g., placing an order). Furthermore, the system needs to handle secondary tasks—like sending notifications—asynchronously so that the primary user flow isn't bottlenecked.

## Solution Architecture

To solve this, we have built a containerized microservices architecture using Spring Boot and Docker. The system is divided into three distinct services, each running in its own Docker container and communicating over a shared internal Docker network.

### The Business Flow
1. **Product Browsing:** Users can interact with the `product-service` to add new products or check the available catalog.
2. **Order Creation:** When a user wants to place an order, they send a request to the `order-service`.
3. **Cross-Service Validation:** Before saving the order, the `order-service` makes a synchronous HTTP call to the `product-service` to verify that the requested product actually exists and is available.
4. **Triggering Notifications:** If the product is valid, the order is successfully created in the database. The `order-service` then immediately calls the `notification-service` to report the newly created order.
5. **Asynchronous Processing (Background Job):** The `notification-service` saves this alert to its own database. A scheduled background job continuously polls this database for unread notifications. When it finds one, it "sends" the notification (logging it to the console) and then deletes the record from the database to keep the queue clean.

---

## Service Breakdown & API Reference

All services are containerized via Docker Compose, ensuring that ports and environment variables are automatically mapped.

### 1. Product Service
Acts as the source of truth for the product catalog.
* **Port:** `8081`
* **Endpoints:**
  * `POST /products` - Add a new product to the catalog.
  * `GET /products` - Retrieve a list of all available products.
  * `GET /products/{id}` - Retrieve details of a specific product.

### 2. Order Service
Handles customer checkout and orchestrates validation.
* **Port:** `8082`
* **Endpoints:**
  * `POST /orders` - Create a new order (triggers calls to Product and Notification services).
  * `GET /orders/{id}` - Retrieve a specific order's details.

### 3. Notification Service
Handles alert logging and background processing independently of the main user thread.
* **Port:** `8083`
* **Endpoints:**
  * `POST /notifications` - Receive an alert that an order has been created.
* **Background Task:** Runs on a cron schedule, checks the DB for new notifications, prints them to the console, and clears them.

---

## Technologies Used
* **Java / Spring Boot:** Core framework for the microservices.
* **Spring WebFlux (WebClient):** For non-blocking, synchronous HTTP communication between microservices.
* **Docker & Docker Compose:** For containerization, network isolation, and easy local deployment.
* **MySQL:** Relational database for persistent storage (configured with Docker volumes to prevent data loss).

## How to Run

1. Ensure Docker and Docker Compose are installed on your machine.
2. Navigate to the root directory containing the `docker-compose.yml` file.
3. Run the following command to build the images and spin up the containers:
   ```bash
   docker-compose up --build
