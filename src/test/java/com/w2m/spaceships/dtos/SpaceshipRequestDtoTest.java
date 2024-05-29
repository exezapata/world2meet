package com.w2m.spaceships.dtos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class SpaceshipRequestDtoTest {

    private SpaceshipRequestDto spaceshipRequestDto;

    @BeforeEach
    void setUp() {
        spaceshipRequestDto = new SpaceshipRequestDto("Millennium Falcon", "Star Wars");
    }

    @Test
    void getName() {
        assertEquals("Millennium Falcon", spaceshipRequestDto.getName());
    }

    @Test
    void getSeries() {
        assertEquals("Star Wars", spaceshipRequestDto.getSeries());
    }

    @Test
    void setName() {
        spaceshipRequestDto.setName("USS Enterprise (NCC-1701)");

        assertEquals("USS Enterprise (NCC-1701)", spaceshipRequestDto.getName());
    }

    @Test
    void setSeries() {
        spaceshipRequestDto.setSeries("Star Trek");

        assertEquals("Star Trek", spaceshipRequestDto.getSeries());
    }

    @Test
    void testEquals() {
        SpaceshipRequestDto spaceshipRequestDto2 = new SpaceshipRequestDto("Millennium Falcon", "Star Wars");

        assertEquals(spaceshipRequestDto, spaceshipRequestDto2);
    }

    @Test
    void canEqual() {
        SpaceshipRequestDto spaceshipRequestDto2 = new SpaceshipRequestDto("Millennium Falcon", "Star Wars");

        assertTrue(spaceshipRequestDto.canEqual(spaceshipRequestDto2));
    }

    @Test
    void testHashCode() {
        SpaceshipRequestDto spaceshipRequestDto2 = new SpaceshipRequestDto("Millennium Falcon", "Star Wars");

        assertEquals(spaceshipRequestDto.hashCode(), spaceshipRequestDto2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("SpaceshipRequestDto(name=Millennium Falcon, series=Star Wars)", spaceshipRequestDto.toString());
    }

    @Test
    void builder() {
        SpaceshipRequestDto spaceshipRequestDto = SpaceshipRequestDto.builder()
                .name("Millennium Falcon")
                .series("Star Wars")
                .build();

        assertEquals("Millennium Falcon", spaceshipRequestDto.getName());
        assertEquals("Star Wars", spaceshipRequestDto.getSeries());
    }
}
