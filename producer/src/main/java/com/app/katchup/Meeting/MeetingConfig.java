package com.app.katchup.Meeting;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.apache.kafka.common.serialization.StringSerializer;


import java.util.HashMap;
import java.util.Map;

@Configuration
public class MeetingConfig {

    @Bean
    public DefaultKafkaProducerFactory<String, String> producerMeetingFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, String.valueOf(StringSerializer.class.getName()));
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, String.valueOf(JsonSerializer.class.getName()));

        return new DefaultKafkaProducerFactory<>(config);
    }


    @Bean
    public KafkaTemplate<String, String> meetingTemplate() {
        return new KafkaTemplate<>(producerMeetingFactory());
    }

}
