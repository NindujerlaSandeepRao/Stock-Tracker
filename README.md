# Stock Tracker (Reactive Java Backend)

A reactive stock tracking application built using Spring Boot WebFlux. The application integrates with external APIs to fetch real-time stock data and provides functionality to manage favorite stocks and retrieve historical data.

---

## Features

* Real-time stock price retrieval
* Historical stock data (time series)
* Add and manage favorite stocks
* Non-blocking reactive architecture using WebFlux
* Parallel API calls for improved performance
* Graceful error handling for external API failures

---

## Tech Stack

* Backend: Java, Spring Boot, Spring WebFlux
* Database: H2
* API Integration: Alpha Vantage
* Build Tool: Maven
* Architecture: Reactive Programming (Mono, Flux)

---

## Project Structure

```
stock-tracker/
│
├── client/        # External API integration using WebClient
├── service/       # Business logic (reactive)
├── controller/    # REST endpoints
├── repository/    # Data access layer
├── entity/        # Database entities
├── dto/           # Data transfer objects
└── exception/     # Global exception handling
```

---

## Setup and Installation

### Clone the repository

```
git clone https://github.com/your-username/stock-tracker.git
cd stock-tracker
```

---

### Configure API Key

Obtain an API key from Alpha Vantage and update `application.properties`:

```
alpha.vantage.api.key=YOUR_API_KEY
```

---

### Run the application

```
mvn spring-boot:run
```

---

## API Endpoints

### Get Stock Price

```
GET /api/v1/stocks/{symbol}
```

---

### Get Stock Overview

```
GET /api/v1/stocks/{symbol}/overview
```

---

### Get Stock History

```
GET /api/v1/stocks/{symbol}/history?days=30
```

---

### Add Favorite Stock

```
POST /api/v1/stocks/favorites/{symbol}
```

---

### Get Favorite Stocks with Live Prices

```
GET /api/v1/stocks/favorites
```

---

## Key Concepts

* Reactive programming using Mono and Flux
* Non-blocking HTTP client using WebClient
* Functional transformations using map and flatMap
* Error handling with reactive operators
* DTO-based response mapping

---

## Notes

* Alpha Vantage API enforces rate limits (5 requests per minute)
* For production usage, consider adding caching and rate limiting
* Proper error handling is implemented to handle API inconsistencies

---

## Future Improvements

* Add Redis caching for performance optimization
* Implement rate limiting and retries
* Add authentication and authorization
* Integrate a frontend application

---

## Author

Sandeep Rao
Java Full Stack Developer
