package com.w2m.spaceships.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class RoleTest {

    private Role role1;
    private Role role2;

    @BeforeEach
    public void setUp() {
        role1 = Role.builder().id(1L).name("Admin").build();

        role2 = Role.builder().id(2L).name("User").build();
    }

    @Test
    void testEquals_SameObject() {
        assertEquals(role1, role1);
    }

    @Test
    void testEquals_NullObject() {
        assertNotEquals( null, role1);
    }

    @Test
    void testEquals_DifferentClass() {
        assertNotEquals("Not a Role object",role1);
    }

    @Test
    void testEquals_SameFields() {
        Role role1Copy = Role.builder().id(1L).name("Admin").build();

        assertEquals(role1, role1Copy);
    }

    @Test
    void testEquals_DifferentId() {
        assertNotNull(role1);
    }

    @Test
    void testToString() {
        String expectedToString = "Role(id=1, name=Admin)";

        assertEquals(expectedToString, role1.toString());
    }

    @Test
    void testHashCode() {
        assertEquals(role1.hashCode(), role1.hashCode());
    }

    @Test
    void testCanEqual() {
        assertTrue(role1.canEqual(role2));
    }
}