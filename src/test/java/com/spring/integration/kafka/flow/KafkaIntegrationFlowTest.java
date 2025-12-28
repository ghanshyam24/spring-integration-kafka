package com.spring.integration.kafka.flow;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
class KafkaIntegrationFlowTest {

    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));

    @DynamicPropertySource
    static void kafkaProps(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    QueueChannel kafkaInputChannel;

    @BeforeEach
    void setUp() {
        // create topic manually
        Map<String, Object> props = new HashMap<>();
        props.put("bootstrap.servers", kafkaContainer.getBootstrapServers());
        try (AdminClient adminClient = AdminClient.create(props)) {
            NewTopic topic = new NewTopic("test-topic", 1, (short) 1);
            adminClient.createTopics(Collections.singletonList(topic)).all().get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldConsumeMessageFromKafka() {

        kafkaTemplate.send("test-topic", "key", "hello");


        Message<?> message = kafkaInputChannel.receive(20_000);

        assertNotNull(message, "Message not received from Kafka");
        assertEquals("hello", message.getPayload());
    }
}
