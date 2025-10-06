# Kafka Producer Consumer

A Spring Boot application demonstrating Apache Kafka producer and consumer implementation with a multi-broker Kafka
cluster setup using Docker Compose.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [Kafka UI Interfaces](#kafka-ui-interfaces)
- [API Endpoints](#api-endpoints)
- [Technology Stack](#technology-stack)

## Overview

This project demonstrates a complete Kafka messaging system with:

- **Producer Application**: Generates and sends random numbers to a Kafka topic every second
- **Consumer Application**: Listens to the Kafka topic and processes incoming messages
- **Multi-Broker Kafka Cluster**: 3 Kafka brokers with ZooKeeper coordination
- **Management UIs**: Kafka Manager and Kafka UI for cluster monitoring and management

## Architecture

```
┌─────────────────┐         ┌─────────────────────────────────┐         ┌──────────────────┐
│                 │         │   Kafka Cluster                 │         │                  │
│   Producer      │────────▶│   ┌────────┐ ┌────────┐        │────────▶│   Consumer       │
│   Application   │         │   │Broker 1│ │Broker 2│        │         │   Application    │
│                 │         │   │:9091   │ │:9092   │        │         │                  │
└─────────────────┘         │   └────────┘ └────────┘        │         └──────────────────┘
                            │        ┌────────┐              │
                            │        │Broker 3│              │
                            │        │:9093   │              │
                            │        └────────┘              │
                            │             │                  │
                            │        ┌────────┐              │
                            │        │ZooKeeper│             │
                            │        │:2181   │              │
                            │        └────────┘              │
                            └─────────────────────────────────┘
```

## Features

- **Scheduled Message Production**: Automatically produces random numbers (10-100,000) every second
- **Message Consumption**: Kafka listener that processes messages in real-time
- **Multi-Broker Setup**: 3-broker Kafka cluster for high availability
- **Service Discovery**: ZooKeeper for cluster coordination
- **Management Tools**:
    - Kafka Manager for cluster administration
    - Kafka UI for modern web-based management
- **Spring Boot Integration**: Leverages Spring Kafka for simplified configuration
- **Logging**: Comprehensive logging with hostname tracking

## Prerequisites

- Java 21 or higher
- Maven 3.6+
- Docker and Docker Compose
- Minimum 4GB RAM for Docker

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/hendisantika/kafka-producer-consumer.git
cd kafka-producer-consumer
```

### 2. Start Kafka Cluster

```bash
docker compose up -d
```

This will start:

- 3 Kafka brokers (ports 9091, 9092, 9093)
- ZooKeeper (port 2181)
- Kafka Manager (port 9000)
- Kafka UI (port 8080)

### 3. Verify Cluster Status

Check if all services are running:

```bash
docker compose ps
```

### 4. Build the Applications

```bash
# Build producer
mvn clean install

# Build consumer
cd kafka-consumer
mvn clean install
```

### 5. Run the Applications

**Terminal 1 - Producer:**

```bash
mvn spring-boot:run
```

**Terminal 2 - Consumer:**

```bash
cd kafka-consumer
mvn spring-boot:run
```

## Project Structure

```
kafka-producer-consumer/
├── src/main/java/id/my/hendisantika/kafka/
│   ├── KafkaProducerConsumerApplication.java  # Producer application
│   └── RandomNumberProducer.java              # Scheduled producer component
├── kafka-consumer/
│   └── src/main/java/id/my/hendisantika/kafkaconsumer/
│       ├── KafkaConsumerApplication.java      # Consumer application
│       └── RandomNumberConsumer.java          # Consumer component
├── compose.yaml                                # Docker Compose configuration
└── pom.xml                                     # Maven configuration
```

## Configuration

### Producer Configuration (`application.yml`)

```yaml
spring:
  application:
    name: kafka-producer-consumer
  kafka:
    bootstrap-servers:
      - localhost:9091
    template:
      default-topic: random-number
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
```

### Consumer Configuration

The consumer listens to the `random-number` topic and automatically processes incoming messages.

### Kafka Cluster Configuration

- **Kafka Brokers**: 3 instances with internal and external listeners
- **Replication**: Configured for multi-broker replication
- **ZooKeeper**: Single instance for cluster coordination
- **Data Persistence**: Volumes mounted for Kafka and ZooKeeper data

## Running the Application

### Watch the Producer Logs

```bash
# Producer sends messages every second
2025-10-07 05:50:15.123  INFO --- [scheduling-1] i.m.h.kafka.RandomNumberProducer : hostname-producer produced 54321
2025-10-07 05:50:16.124  INFO --- [scheduling-1] i.m.h.kafka.RandomNumberProducer : hostname-producer produced 87654
```

### Watch the Consumer Logs

```bash
# Consumer receives and processes messages
2025-10-07 05:50:15.125  INFO --- [ntainer#0-0-C-1] i.m.h.kafkaconsumer.RandomNumberConsumer : hostname-consumer consumed 54321
2025-10-07 05:50:16.126  INFO --- [ntainer#0-0-C-1] i.m.h.kafkaconsumer.RandomNumberConsumer : hostname-consumer consumed 87654
```

## Kafka UI Interfaces

### Kafka UI (Modern Interface)

- **URL**: http://localhost:8080
- **Features**:
    - Topic management and browsing
    - Message inspection
    - Consumer group monitoring
    - Broker metrics
    - Schema registry support

### Kafka Manager (Classic Interface)

- **URL**: http://localhost:9000
- **Features**:
    - Cluster management
    - Topic creation and configuration
    - Partition management
    - Consumer monitoring

## API Endpoints

The applications expose Spring Boot Actuator endpoints (if enabled):

- Producer: http://localhost:8080/actuator
- Consumer: http://localhost:8081/actuator

## Technology Stack

- **Spring Boot**: 3.5.3
- **Spring Kafka**: Latest
- **Java**: 21
- **Kafka**: 7.6.1 (Confluent)
- **ZooKeeper**: 3.7.2
- **Kafka UI**: Latest (provectuslabs/kafka-ui)
- **Kafka Manager**: Latest (sheepkiller/kafka-manager)
- **Lombok**: For reducing boilerplate code
- **Maven**: Build tool

## Development

### Running Tests

```bash
mvn test
```

### Cleaning Up

Stop and remove all containers:

```bash
docker compose down
```

Remove volumes (deletes all Kafka data):

```bash
docker compose down -v
```

## Troubleshooting

### Kafka Connection Issues

If the application can't connect to Kafka:

1. Verify Kafka is running: `docker compose ps`
2. Check Kafka logs: `docker compose logs kafka1`
3. Ensure ports are not in use: `lsof -i :9091`

### Consumer Not Receiving Messages

1. Check topic exists: Visit Kafka UI at http://localhost:8080
2. Verify consumer group is active
3. Check consumer logs for errors

### Out of Memory

If Docker runs out of memory:

```bash
# Check Docker resources
docker stats

# Increase Docker memory limit in Docker Desktop settings
# Recommended: 4GB minimum
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Author

- **Hendi Santika**
- Email: hendisantika@gmail.com
- Telegram: @hendisantika34
- Website: https://s.id/hendisantika

## License

This project is available for educational and demonstration purposes.
