##Spring Integration + Kafka + Testcontainers

#Requirements
JDK: 17+ recommended (Required for Spring Boot 3.x)
Docker: Docker Desktop (Required for Testcontainers)
Memory: 4GB+ (Kafka containers are resource-heavy)

#Setup
Start Docker: Ensure the Docker engine is running.
Run: Execute mvn clean test.


#Testing Logic
Tests use Testcontainers to spin up a temporary Kafka broker.
Producer: KafkaTemplate sends a message to the container.
Integration Flow: Consumes the message and moves it to a QueueChannel.
Verification: The test "polls" the QueueChannel to verify the payload.