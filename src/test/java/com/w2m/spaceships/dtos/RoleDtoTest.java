package com.w2m.spaceships.dtos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class RoleDtoTest {

    private RoleDto roleDto;

    @BeforeEach
    void setUp() {
        roleDto = new RoleDto(1L, "ROLE_USER");
    }

    @Test
    void getId() {
        assertEquals(1L, roleDto.getId());
    }

    @Test
    void getName() {
        assertEquals("ROLE_USER", roleDto.getName());
    }

    @Test
    void setId() {
        roleDto.setId(2L);

        assertEquals(2L, roleDto.getId());
    }

    @Test
    void setName() {
        roleDto.setName("ROLE_ADMIN");

        assertEquals("ROLE_ADMIN", roleDto.getName());
    }

    @Test
    void testEquals() {
        RoleDto roleDto2 = new RoleDto(1L, "ROLE_USER");

        assertEquals(roleDto, roleDto2);
    }

    @Test
    void canEqual() {
        RoleDto roleDto2 = new RoleDto(1L, "ROLE_USER");

        assertTrue(roleDto.canEqual(roleDto2));
    }

    @Test
    void testHashCode() {
        RoleDto roleDto2 = new RoleDto(1L, "ROLE_USER");

        assertEquals(roleDto.hashCode(), roleDto2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("RoleDto(id=1, name=ROLE_USER)", roleDto.toString());
    }

    @Test
    void builder() {
        RoleDto roleDto = RoleDto.builder()
                .id(1L)
                .name("ROLE_USER")
                .build();

        assertEquals(1L, roleDto.getId());
        assertEquals("ROLE_USER", roleDto.getName());
    }
}