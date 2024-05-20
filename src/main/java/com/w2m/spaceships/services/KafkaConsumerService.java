package com.w2m.spaceships.services;

import com.w2m.spaceships.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumerService {

    @KafkaListener(topics = Constants.TOPIC_NAME_SPACESHIP_EVENTS, groupId = Constants.SPACESHIP_GROUP)
    public void listen(String message) {
       log.info("This is a message received on KAFKA CONSUMER:  " + message);
    }

}
