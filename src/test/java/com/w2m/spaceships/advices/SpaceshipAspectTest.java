package com.w2m.spaceships.advices;

import com.w2m.spaceships.services.KafkaProducerService;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class SpaceshipAspectTest {

    @MockBean
    private KafkaProducerService kafkaProducerService;

    @MockBean
    private JoinPoint joinPoint;

    @Autowired
    private SpaceshipAspect spaceshipAspect;


    @BeforeEach
    void setUp() {
        spaceshipAspect = new SpaceshipAspect(kafkaProducerService);
    }

    @Test
    void testLogNegativeId_PositiveId() {
        Long positiveId = 1L;
        Object[] args = {positiveId};

        when(joinPoint.getArgs()).thenReturn(args);

        spaceshipAspect.logNegativeId(joinPoint);

        verify(kafkaProducerService, never()).produce(anyString());
    }

    @Test
    void testLogNegativeId_NegativeId() {
        Long negativeId = -1L;
        Object[] args = {negativeId};

        when(joinPoint.getArgs()).thenReturn(args);

        spaceshipAspect.logNegativeId(joinPoint);

        String expectedMessage = String.format("Attention is requested to search for a ship with a negative id  --> %s", negativeId);
        verify(kafkaProducerService, times(1)).produce(expectedMessage);
    }

}