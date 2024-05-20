package com.w2m.spaceships.services;

import com.w2m.spaceships.utils.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = Constants.TOPIC_NAME_SPACESHIP_EVENTS)
@DirtiesContext
class KafkaConsumerServiceTest {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @MockBean
    private KafkaConsumerService kafkaConsumerService;

    private String receivedMessage;

    @Test
    public void testListen_Ok() throws InterruptedException {

        String message = "Test message";

        kafkaProducerService.produce(message);

        await().untilAsserted(() -> {
            assertEquals(message, receivedMessage);
        });
    }

    @KafkaListener(topics = Constants.TOPIC_NAME_SPACESHIP_EVENTS, groupId = Constants.SPACESHIP_GROUP)
    public void listen(String message) {
        receivedMessage = message;
    }
}