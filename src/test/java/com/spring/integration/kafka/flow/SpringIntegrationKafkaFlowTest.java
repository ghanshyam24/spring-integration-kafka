package com.spring.integration.kafka.flow;

import com.spring.integration.kafka.properties.KafkaServiceProperties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class SpringIntegrationKafkaFlowTest {

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));

    @DynamicPropertySource
    static void kafkaProps(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaServiceProperties kafkaServiceProperties;

    @Autowired
    private ConsumerFactory<String, String> consumerFactory;


    @Test
    void shouldMoveDataToAckTopic() {
        String payload = "Integration Test Data";
        String targetTopic = kafkaServiceProperties.getAckTopicName();

        Consumer<String, String> testConsumer = consumerFactory.createConsumer(kafkaServiceProperties.getGroupId(), "test-client");
        testConsumer.subscribe(Collections.singletonList(targetTopic));

        kafkaTemplate.send(kafkaServiceProperties.getTopicName(), payload);

        ConsumerRecord<String, String> received = KafkaTestUtils.getSingleRecord(testConsumer, targetTopic, Duration.ofSeconds(10));

        assertNotNull(received);
        assertEquals(payload, received.value());

        testConsumer.close();
    }
}