package com.spring.integration.kafka.flow;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.messaging.MessageChannel;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class SpringIntegrationKafkaFlow {

//    @Bean
//    public IntegrationFlow kafkaInboundFlow(ConsumerFactory<String, String> kafkaConsumerFactory) {
//        return IntegrationFlow
//                .from(Kafka.messageDrivenChannelAdapter(
//                        kafkaConsumerFactory,
//                        KafkaMessageDrivenChannelAdapter.ListenerMode.record,
//                        "test-topic"
//                ))
//                .channel("kafkaInputChannel")
//                .get();
//    }
//
//    @Bean
//    public QueueChannel kafkaInputChannel() {
//        return new QueueChannel();
//    }
//
//    @Bean
//    public IntegrationFlow startKafkaInBound() {
//        return IntegrationFlow
//                .from(kafkaInputChannel())
//                .handle(message -> {
//                    System.out.println("Received message from Kafka: " + message.getPayload());
//                })
//                .get();
//    }

    @Bean
    public IntegrationFlow kafkaInboundFlow(ConsumerFactory<String, String> consumerFactory) {
        return IntegrationFlow
                .from(Kafka.messageDrivenChannelAdapter(
                        consumerFactory,
                        KafkaMessageDrivenChannelAdapter.ListenerMode.record,
                        "test-topic"
                ))
                .channel(kafkaInputChannel())
                .get();
    }

    @Bean
    public QueueChannel kafkaInputChannel() {
        return new QueueChannel();
    }


}
