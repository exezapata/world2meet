package com.w2m.spaceships.services;

import com.w2m.spaceships.utils.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = Constants.TOPIC_NAME_SPACESHIP_EVENTS)
@DirtiesContext
class KafkaProducerServiceTest {
    @Autowired
    private KafkaProducerService kafkaProducerService;
    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;


    @Test
    void testSend_Ok() throws InterruptedException {

        String message = "Test message";

        kafkaProducerService.produce(message);

        verify(kafkaTemplate, times(1)).send(Constants.TOPIC_NAME_SPACESHIP_EVENTS, message);
    }


}