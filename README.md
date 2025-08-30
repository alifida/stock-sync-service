# Stock Sync Service

## Setup

1. Build the app:
   ```
   mvn clean package
   ```
2. Run with Docker Compose:
   ```
   docker-compose up --build
   ```

## Vendor Simulation

- **Vendor A:** Simulated via REST endpoint in `StockSyncSimulator.java`.
- **Vendor B:** CSV file at `/tmp/vendor-b/stock.csv`.

## Assumptions

- Full sync each run.
- Stock event detection logs and persists events.

## Improvements

- Add retry logic for vendor failures.
- Pagination for large product lists.

# Stock Sync Service – Design & Implementation Plan

## Overview

The Stock Sync Service is a Spring Boot–based microservice designed to:

- Synchronize product stock levels from multiple vendors
- Persist product data
- Detect stock changes
- Expose APIs for data retrieval

### Key Qualities

- **Generic & Extensible** – Easily onboard new vendors through database configuration without code changes.
- **Production-Ready** – Includes database connection pooling, error handling, and modular architecture.
- **Future-Focused** – Supports delta-based synchronization, event-driven notifications, and scalability patterns.

---

## Architecture

The microservice follows a clean, layered architecture:

- **Controller Layer**: REST APIs for product retrieval and potential future endpoints.
- **Service Layer**: Business logic for synchronization, data processing, and event generation.
- **Vendor Client Abstraction**: `VendorClient` interface implemented by vendor-specific clients (e.g., API or CSV).
- **Persistence Layer**: JPA repositories for vendors, products, and stock events.
- **Scheduler**: Triggers periodic synchronization tasks using `@Scheduled`.
- **Messaging Layer (Future)**: Pluggable integration with Kafka/RabbitMQ for real-time alerts.
- **Observability**: Spring Boot Actuator for health checks, metrics, and monitoring.

---

## Core Entities & Tables

### Vendor

| Field       | Type       | Notes                                 |
|-------------|------------|---------------------------------------|
| id          | UUID/Long  | Primary key                           |
| name        | String     | Vendor name                           |
| type        | Enum       | Vendor type (API, CSV, etc.)          |
| endpoint    | String     | URL or file path                      |
| enabled     | Boolean    | Controls sync inclusion               |
| lastSync    | Timestamp  | Last synchronization time             |

### Product

| Field         | Type       | Notes                                 |
|---------------|------------|---------------------------------------|
| id            | UUID/Long  | Primary key                           |
| sku           | String     | Vendor-specific SKU                   |
| name          | String     | Product name                          |
| stockQuantity | Integer    | Current stock level                   |
| vendorId      | FK         | References `Vendor.id`                |
| lastUpdated   | Timestamp  | Timestamp of last update              |

### StockEvent

| Field      | Type       | Notes                                                   |
|------------|------------|---------------------------------------------------------|
| id         | UUID/Long  | Primary key                                             |
| productId  | FK         | References `Product.id`                                 |
| sku        | String     | SKU at event time                                       |
| vendorId   | FK         | References `Vendor.id`                                  |
| eventType  | Enum       | `OUT_OF_STOCK`, `RESTOCKED` (future use)                |
| timestamp  | Timestamp  | Event occurrence time                                   |

> **Why Events?**  
> Persisting events instead of only logging ensures full audit history and enables real-time alerting through future messaging systems.

---

## Synchronization Workflow

1. **Scheduler Trigger**  
   - Runs periodically (e.g., every 5 minutes) via `@Scheduled`.

2. **Vendor Discovery**  
   - Fetches enabled vendors from the database.

3. **Data Fetching**  
   - Each vendor is processed through its corresponding `VendorClient`:
     - `VendorARestClient`: Simulates a REST API call.
     - `VendorBCsvClient`: Reads CSV file at `/tmp/vendor-b/stock.csv`.

4. **Delta-Based Sync**  
   - Compares incoming data against existing records.
   - Updates only changed stock quantities.
   - Detects >0 → 0 transitions and creates a `StockEvent`.

5. **Persistence**  
   - Products are upserted.
   - Stock events are recorded in the `StockEvent` table.

6. **API Exposure**  
   - `GET /products` provides a paginated product list.
   - Supports future filters (e.g., by vendor, stock status).

7. **Future Messaging**  
   - Events published to a message queue for notifications, analytics, and third-party integrations.

---

## Vendor Client Abstraction

A pluggable `VendorClient` interface defines the contract for vendor integrations:

```java
public interface VendorClient {
    Vendor getVendorMetadata();
    List<ProductDTO> fetchStock();
}

```

### Concrete Implementations

- `VendorARestClient`
- `VendorBCsvClient`

> Future clients (FTP, GraphQL, etc.) can be added without altering core logic.


### Technical Choices

| Component         | Technology                        | Notes                                         |
|-------------------|-----------------------------------|-----------------------------------------------|
| Language          | Java 17+                          | Modern LTS with records and sealed classes    |
| Framework         | Spring Boot 3+                    | Microservice standard                         |
| Scheduler         | Spring `@Scheduled`               | Lightweight periodic jobs                     |
| ORM & DB          | Spring Data JPA + H2 (demo)       | Swap to PostgreSQL/MySQL in production        |
| Connection Pool   | HikariCP                          | Stable and performant                         |
| CSV Parsing       | Apache Commons CSV                | Reliable CSV handling                         |
| API Documentation | Springdoc OpenAPI (Swagger UI)    | Interactive API docs                          |
| Testing           | JUnit 5, Mockito, Testcontainers  | Unit & integration tests                      |
| Deployment        | Dockerfile                        | Containerized for portability                 |
| Observability     | Spring Boot Actuator              | Health checks, metrics, readiness probes      |



### Error Handling & Resilience

- **Retries**: API calls retried with Spring Retry.
- **Vendor Isolation**: Failure to sync one vendor doesn’t block others.
- **Validation**: CSV schema validation and error reporting.
- **Custom Exceptions**: `VendorApiException`, `CsvProcessingException`, etc.
- **Graceful Degradation**: Fall back to last known stock data on vendor failure.


### Testing Strategy

#### Unit Tests

- Vendor client parsing (JSON/CSV)
- Delta sync logic and event detection
- Exception handling

#### Integration Tests

- End-to-end sync workflow
- `/products` API validation
- Vendor failure simulation

#### Edge Cases

- Duplicate SKUs across vendors
- Empty or malformed vendor data
- Restocking scenario (0 → >0)


### Future Enhancements

- **Delta-Based Sync** – Minimize DB updates and improve performance.
- **Event-Driven Alerts** – Publish `StockEvent` to Kafka/RabbitMQ for real-time processing.
- **Dynamic Vendor Onboarding** – Configure vendors entirely via DB without redeploying code.
- **Audit Trails** – Maintain full product versioning history.
- **Security** – Add role-based access control and request authentication.
- **Scalability** – Horizontal scaling, async processing, caching, and vendor-specific microservices.


### Extensibility Hooks

- **Listeners**: Event listeners to react to `StockEvent` creation.
- **Filters**: Request/response filters for logging, metrics, and authentication.
- **Validation Hooks**: Vendor-specific data validation strategies.
- **Messaging Integration**: Placeholder interfaces for future queue-based notifications.


### Deliverables

- ✅ Fully functional Spring Boot microservice
- ✅ Modular codebase with vendor abstraction
- ✅ Dockerfile for containerized deployment
- ✅ Comprehensive README 
- ✅ Git history demonstrating incremental progress
- ✅ Automated tests (unit + integration)
- ✅ Example vendor simulation (REST + CSV)
