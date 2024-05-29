package com.w2m.spaceships.utils;

public class Constants {
    private Constants() {
        throw new AssertionError();
    }

    public static final String BEARER_JWT = "bearer-jwt";
    public static final String TOPIC_NAME_SPACESHIP_EVENTS = "spaceship-events";
    public static final String SPACESHIP_GROUP = "spaceship-group";
    public static final String SPACESHIP_NOT_FOUND = "Spaceship not found with ID: ";
}
