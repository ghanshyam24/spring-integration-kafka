# spring-integration-kafka
Spring Integration + Kafka + Testcontainers

# 🛠 SETUP & REQUIREMENTS

* **JDK**: 17+ recommended (Required for Spring Boot 3.x)
* **Docker**: Docker Desktop (Required for Testcontainers)
* **Memory**: 4GB+ (Kafka containers are resource-heavy)

---

# 🚀 SETUP

1. **Start Docker**: Ensure the Docker engine is running.
2. **Run**: Execute `mvn clean test`.

---

# 🧠 TESTING LOGIC

Tests use **Testcontainers** to spin up a temporary Kafka broker.

* **Producer**: `KafkaTemplate` sends a message to the container.
* **Integration Flow**: Consumes the message and moves it to a `QueueChannel`.
* **Verification**: The test "polls" the `QueueChannel` to verify the payload.