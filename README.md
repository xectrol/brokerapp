# BrokerApp - Java Backend Developer Case

## Project Purpose

BrokerApp is a backend system designed to simulate basic functionalities of a brokerage platform. It allows users to place buy/sell orders on various assets, deposit or withdraw TRY as currency, and enables admin users to match orders manually.

## Features

- User registration with Basic Authentication
- Place buy/sell orders (LIMIT / MARKET)
- Filter and list orders by date range and customer
- Cancel pending orders
- Admin can match pending orders manually
- Manage customer asset holdings
- Deposit / Withdraw TRY balance

## How to Run

1. Build the project with Maven:
   ```bash
   mvn clean install
   mvn spring-boot:run

## Access H2 database console at:
http://localhost:8080/h2-console
