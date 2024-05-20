package com.w2m.spaceships.advices;

import com.w2m.spaceships.services.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SpaceshipAspect {

    private final KafkaProducerService kafkaProducerService;

    @Pointcut(value = "execution(* com.w2m.spaceships.controllers.SpaceshipController.getSpaceshipById(..))")
    private void logNegativeIdCut(){}


    @Before(value = "logNegativeIdCut()")
    public void logNegativeId(JoinPoint joinPoint) {
        try{
            Object[] args = joinPoint.getArgs();
            Long id = (Long) args[0];
            if (id < 0) {
                String message = String.format("Attention is requested to search for a ship with a negative id  --> %s", id);
                kafkaProducerService.produce(message);
            }
        }catch(Exception exception){
            log.error("Error in logNegativeId : {} ", exception.getMessage());
        }
    }

}
