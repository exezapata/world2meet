package com.w2m.spaceships;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@SpringJUnitWebConfig(SpaceshipsApplication.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = {
		"listeners=PLAINTEXT://localhost:9092",
		"port=9092"
})
class SpaceshipsApplicationTests {

	@Test
	void contextLoads() {
		assertNotNull(SpaceshipsApplication.class);
	}

	@Test
	public void testMainMethod() {
		SpaceshipsApplication.main(new String[]{});
	}
}
