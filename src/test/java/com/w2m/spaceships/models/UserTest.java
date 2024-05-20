package com.w2m.spaceships.models;

import com.w2m.spaceships.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserTest {

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        user1 = new User(1L, "username", "password", null);

        user2 = new User(1L, "username", "password", null);
    }

    @Test
    public void testEquals() {
        assertEquals(user1, user2);
    }

    @Test
    public void testHashCode() {
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testToString() {
        assertNotNull(user1.toString());
    }

    @Test
    public void testBuilder() {
        User user = User.builder().id(1L).username("username").password("password").build();

        assertNotNull(user);
    }

    @Test
    public void testSetId() {
        user1.setId(1L);

        assertEquals(1L, user1.getId());
    }

    @Test
    public void testCanEqual() {
        User user3 = new User();

        assertTrue(user1.canEqual(user3));
    }

    @Test
    public void testCreateUser() {

        User user = new User();
        user.setUsername("username");
        user.setPassword("password");

        Role role = Role.builder().id(1L).name("ROLE_USER").build();
        user.setRoles(Set.of(role));

        User savedUser = userRepository.save(user);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("username");
        assertThat(savedUser.getPassword()).isEqualTo("password");
        assertThat(savedUser.getRoles()).hasSize(1).extracting("name").contains("ROLE_USER");
    }
}