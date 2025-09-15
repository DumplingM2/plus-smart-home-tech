package ru.practicum.aggregator.config;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfiguration {
    private final Environment env;

    @Bean
    public KafkaConsumer<String, SpecificRecordBase> getConsumer() {
        Properties config = new Properties();
        config.put(ConsumerConfig.CLIENT_ID_CONFIG, "aggregator-consumer");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "aggregator-group");
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "kafka.deserializer.SensorEventDeserializer");
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");

        return new KafkaConsumer<>(config);
    }
}
