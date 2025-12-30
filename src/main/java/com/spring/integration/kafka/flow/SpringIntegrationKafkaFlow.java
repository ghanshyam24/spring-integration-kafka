package com.spring.integration.kafka.flow;

import com.spring.integration.kafka.properties.KafkaServiceProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class SpringIntegrationKafkaFlow {

    KafkaServiceProperties kafkaProperties;

    @Bean
    public IntegrationFlow kafkaInboundFlow(ConsumerFactory<String, String> consumerFactory) {
        return IntegrationFlow
                .from(Kafka.messageDrivenChannelAdapter(
                        consumerFactory,
                        KafkaMessageDrivenChannelAdapter.ListenerMode.record,
                        kafkaProperties.getTopicName()
                ).configureListenerContainer(c -> c
                        .groupId(kafkaProperties.getGroupId())
                        .concurrency(kafkaProperties.getConcurrency())
                ))
                .channel(kafkaInputChannel())
                .get();
    }


    @Bean
    public QueueChannel kafkaInputChannel() {
        return new QueueChannel();
    }


    @Bean
    public IntegrationFlow moveDataFlow(ConsumerFactory<String, String> consumerFactory,
                                        KafkaTemplate<String, String> kafkaTemplate) {
        return IntegrationFlow
                .from(kafkaInputChannel())
                .handle(Kafka.outboundChannelAdapter(kafkaTemplate)
                        .topic(kafkaProperties.getAckTopicName()))
                .get();
    }




}