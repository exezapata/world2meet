package com.w2m.spaceships.dtos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
class SpaceshipTest {

    private SpaceshipDto spaceshipDto1;
    private SpaceshipDto spaceshipDto2;

    @BeforeEach
    public void setUp() {
        spaceshipDto1 = new SpaceshipDto(1L, "Enterprise", "NX-01");

        spaceshipDto2 = new SpaceshipDto(1L, "Enterprise", "NX-01");
    }

    @Test
    public void testEquals() {
        assertEquals(spaceshipDto1, spaceshipDto2);
    }

    @Test
    public void testHashCode() {
        assertEquals(spaceshipDto1.hashCode(), spaceshipDto2.hashCode());
    }

    @Test
    public void testToString() {

        SpaceshipDto spaceshipDtoNew = SpaceshipDto.builder()
                .id(1L)
                .name("Enterprise")
                .series("NX-01")
                .build();

        assertEquals(spaceshipDtoNew.toString(), spaceshipDto1.toString());
    }

    @Test
    public void testSetId() {
        assertEquals(1L, spaceshipDto1.getId());
    }

    @Test
    public void testCanEqual() {
        assertTrue(spaceshipDto1.canEqual(spaceshipDto2));
    }

}