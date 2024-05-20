package com.w2m.spaceships.models;

import com.w2m.spaceships.repositories.SpaceshipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class SpaceshipTest {

    @Autowired
    private SpaceshipRepository spaceshipRepository;

    private Spaceship spaceship1;
    private Spaceship spaceship2;

    @BeforeEach
    public void setUp() {
        spaceship1 = new Spaceship(1L, "Enterprise", "NX-01");

        spaceship2 = new Spaceship(1L, "Enterprise", "NX-01");
    }

    @Test
    public void testEquals() {
        assertEquals(spaceship1, spaceship2);
    }

    @Test
    public void testHashCode() {
        assertEquals(spaceship1.hashCode(), spaceship2.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("Spaceship(id=1, name=Enterprise, series=NX-01)", spaceship1.toString());
    }

    @Test
    public void testSetId() {
        Spaceship spaceship = new Spaceship(1L, "Discovery One", "A Space Odyssey");

        assertEquals(1L, spaceship.getId());
    }

    @Test
    public void testCanEqual() {
        assertTrue(spaceship1.canEqual(spaceship2));
    }

    @Test
    public void testCreateSpaceship() {

        Spaceship spaceship = new Spaceship(1L, "Discovery One", "A Space Odyssey");

        Spaceship savedSpaceship = spaceshipRepository.save(spaceship);

        assertThat(savedSpaceship.getId()).isNotNull();
        assertThat(savedSpaceship.getName()).isEqualTo("Discovery One");
        assertThat(savedSpaceship.getSeries()).isEqualTo("A Space Odyssey");
    }

}