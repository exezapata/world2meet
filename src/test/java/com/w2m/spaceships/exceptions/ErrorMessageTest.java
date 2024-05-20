package com.w2m.spaceships.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class ErrorMessageTest {

    private ErrorMessage  error1 ;
    private ErrorMessage error2 ;
    private ErrorMessage error3 ;

    @BeforeEach
    public void setUp() {
        error1 = ErrorMessage.builder().statusCode(404).build();

        error2 = ErrorMessage.builder().statusCode(404).build();

        error3 = ErrorMessage.builder().statusCode(500).build();
    }

    @Test
    public void testEquals() {
        assertEquals(error1, error2);
        assertNotEquals(error1, error3);
    }

    @Test
    public void testHashCode() {
        assertEquals(error1.hashCode(), error2.hashCode());
        assertNotEquals(error1.hashCode(), error3.hashCode());
    }

    @Test
    public void testToString() {
        Date timestamp = new Date();
        ErrorMessage error = ErrorMessage.builder()
                .statusCode(11)
                .timestamp(timestamp)
                .message("Error message")
                .description("Error description")
                .build();

        String expectedToString = "ErrorMessage(statusCode=11, timestamp=" + timestamp +
                ", message=Error message, description=Error description)";

        assertEquals(expectedToString, error.toString());
    }

    @Test
    public void testSetStatusCode() {
        assertEquals(404, error1.getStatusCode());
    }

    @Test
    public void testSetTimestamp() {
        Date timestamp = new Date();
        error1.setTimestamp(timestamp);

        assertEquals(timestamp, error1.getTimestamp());
    }

    @Test
    public void testSetMessage() {
        error1.setMessage("Test Message");

        assertEquals("Test Message", error1.getMessage());
    }

    @Test
    public void testSetDescription() {
        error1.setDescription("Test Description");

        assertEquals("Test Description", error1.getDescription());
    }

    @Test
    public void testCanEqual() {
        ErrorMessage error = ErrorMessage.builder()
                .statusCode(404)
                .timestamp(new Date())
                .message("Test Message")
                .description("Test Description")
                .build();

        assertTrue(error.canEqual(ErrorMessage.builder().build()));
    }

}