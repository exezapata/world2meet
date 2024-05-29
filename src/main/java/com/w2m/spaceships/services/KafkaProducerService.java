package com.w2m.spaceships.services;

import com.w2m.spaceships.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void produce(String message){
        log.info("Message sent -> {}", message);
        kafkaTemplate.send(Constants.TOPIC_NAME_SPACESHIP_EVENTS, message);
    }

}
