package ru.isshepelev.auto.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.isshepelev.auto.infrastructure.persistance.entity.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Configuration
@EnableKafka
public class KafkaConfiguration {

    @Bean
    public NewTopic orderTopic() {
        return new NewTopic("order", 1, (short) 1);
    }
    @Bean
    public KafkaTemplate<String, Order> uuidListKafkaTemplate(ObjectMapper objectMapper,
                                                              @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(), new JsonSerializer<>(objectMapper)));
    }
    @Bean
    public ConsumerFactory<String, Order> orderConsumerFactory(ObjectMapper objectMapper, @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "1");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Order.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(Order.class, objectMapper, false));
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Order> kafkaListenerContainerFactory(ObjectMapper objectMapper,
                                                                                                     @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        ConcurrentKafkaListenerContainerFactory<String, Order> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderConsumerFactory(objectMapper,bootstrapServers));
        return factory;
    }
}
