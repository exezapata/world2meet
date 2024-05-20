package com.w2m.spaceships.configurators.kafkaConfig;

import com.w2m.spaceships.utils.Constants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic spaceshipEventTopic(){
        return TopicBuilder.name(Constants.TOPIC_NAME_SPACESHIP_EVENTS)
                .build();
    }

}
