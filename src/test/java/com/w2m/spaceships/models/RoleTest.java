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
    public void testEquals_SameObject() {
        assertTrue(role1.equals(role1));
    }

    @Test
    public void testEquals_NullObject() {
        assertFalse(role1.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        assertFalse(role1.equals("Not a Role object"));
    }

    @Test
    public void testEquals_SameFields() {
        Role role1Copy = Role.builder().id(1L).name("Admin").build();

        assertTrue(role1.equals(role1Copy));
    }

    @Test
    public void testEquals_DifferentId() {
        assertFalse(role1.equals(role2));
    }

    @Test
    public void testToString() {
        String expectedToString = "Role(id=1, name=Admin)";

        assertEquals(expectedToString, role1.toString());
    }

    @Test
    public void testHashCode() {
        assertEquals(role1.hashCode(), role1.hashCode());
    }

    @Test
    public void testCanEqual() {
        assertTrue(role1.canEqual(role2));
    }
}