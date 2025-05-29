# Products Shop Elasticsearch

This is a tech store backend project built using **Spring Boot** and **Elasticsearch**. It integrates a PostgreSQL database with Elasticsearch for full-text search functionality and provides a RESTful API to manage and search products.

## Technologies Used

- Java 21
- Spring Boot 3.5.0
- Spring Data JPA
- Spring Data Elasticsearch
- Elasticsearch 8.13.4
- PostgreSQL
- Flyway for database migration
- Docker & Docker Compose
- Lombok
- Maven

## Project Setup

### Build the Project (skip tests)

```
mvn clean package -DskipTests
```

### Docker Commands

```bash
docker system prune -a --volumes                         # Clean all Docker data
docker-compose build --no-cache                         # Build without using cache
docker-compose up                                       # Start containers
docker-compose up -d                                    # Start containers in detached mode
docker-compose down                                     # Stop containers
docker-compose down --volumes --remove-orphans          # Deep cleanup
docker ps                                               # Show running containers
```

### Global Rebuild

```bash
./mvnw clean package -DskipTests
docker-compose down --volumes --remove-orphans
docker-compose up --build
```

## REST API Endpoints

### Sync PostgreSQL and Elasticsearch

```
POST http://localhost:8080/api/v1/products/reindex
Response: "Reindexing started successfully"
```

### Add New Product

```
POST http://localhost:8080/api/v1/products/add
Body (JSON):
{
  "name": "Samsung Galaxy S24",
  "description": "Samsung flagship smartphone, 256GB, Phantom Black",
  "price": 949.5
}
```

### Search using Spring Data Elasticsearch

```
GET http://localhost:8080/api/v1/products/name/samsung
Response (JSON): List of matching products
```

### Search using REST API

```
GET http://localhost:8080/api/v1/products/search/rest?name=samsung
Response: Elasticsearch search result object
```

### Search using Java Client

```
GET http://localhost:8080/api/v1/products/search/java?name=samsung
Response (JSON): List of matching products
```

## Useful URLs

- [Kibana](http://localhost:5601) - Kibana Homepage
- [Elasticsearch](http://localhost:9200) - Elasticsearch API Homepage
- [Elasticsearch Indices](http://localhost:9200/_cat/indices?v)
- [All Products](http://localhost:9200/products/_search?pretty)
- [More Products per Page](http://localhost:9200/products/_search?pretty&size=20)
- [Spring Data Elasticsearch Docs](https://docs.spring.io/spring-data/elasticsearch/reference/elasticsearch/versions.html)

## Author

**Roman Manzhula**  
[roman1985al@ukr.net](mailto:roman1985al@ukr.net)  
[GitHub Repository](https://github.com/RoMANzhula/products-shop-elasticsearch)

## License

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).