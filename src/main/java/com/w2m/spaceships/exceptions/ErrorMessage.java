package com.w2m.spaceships.exceptions;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ErrorMessage {

    private int statusCode;
    private Date timestamp;
    private String message;
    private String description;

}
